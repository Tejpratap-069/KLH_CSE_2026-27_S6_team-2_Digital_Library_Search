package digitallibrary.service;

import digitallibrary.model.*;
import digitallibrary.structures.*;
import digitallibrary.storage.TextRepository;
import digitallibrary.algorithms.*;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public final class LibraryService {
    private final TextRepository repo;
    private DynamicArray<Resource> resources;
    private StringHashTable<Resource> byId;
    private StringHashTable<Resource> byIdentifier;

    public LibraryService(TextRepository repo)throws IOException{this.repo=repo;reload();}
    public synchronized void reload()throws IOException{resources=repo.loadResources();rebuildIndexes();}
    private void rebuildIndexes(){byId=new StringHashTable<>(Math.max(1024,resources.size()*2+1));byIdentifier=new StringHashTable<>(Math.max(1024,resources.size()*2+1));for(Resource r:resources){byId.put(r.id().toLowerCase(),r);byIdentifier.put(r.identifier().toLowerCase(),r);}}
    public int size(){return resources.size();}
    public Resource getById(String id){return id==null?null:byId.get(id.toLowerCase());}
    public Resource getByIdentifier(String id){return id==null?null:byIdentifier.get(id.toLowerCase());}
    public List<Resource> snapshot(){List<Resource> out=new ArrayList<>(resources.size());for(Resource r:resources)out.add(r);return out;}

    public Map<String,Object> stats(){
        Map<String,Integer> types=new LinkedHashMap<>(),cats=new LinkedHashMap<>();int available=0;
        for(Resource r:resources){types.merge(r.type(),1,Integer::sum);cats.merge(r.category(),1,Integer::sum);if(!r.availability().equalsIgnoreCase("Issued"))available++;}
        Map<String,Object> m=new LinkedHashMap<>();m.put("totalResources",resources.size());m.put("availableResources",available);m.put("types",types);m.put("categories",cats);m.put("storage","Structured UTF-8 text files");m.put("database","None");return m;
    }

    public List<Resource> page(int page,int pageSize,String type,String category,String sort,String order){
        page=Math.max(1,page);pageSize=Math.max(1,Math.min(100,pageSize));
        DynamicArray<Resource> filtered=new DynamicArray<>();
        for(Resource r:resources){if(type!=null&&!type.isBlank()&&!type.equalsIgnoreCase("all")&&!r.type().equalsIgnoreCase(type))continue;if(category!=null&&!category.isBlank()&&!category.equalsIgnoreCase("all")&&!r.category().equalsIgnoreCase(category))continue;filtered.add(r);}
        Resource[] arr=new Resource[filtered.size()];for(int i=0;i<arr.length;i++)arr[i]=filtered.get(i);mergeSort(arr,0,arr.length,sort==null?"title":sort,order==null?"asc":order);
        int from=Math.min(arr.length,(page-1)*pageSize),to=Math.min(arr.length,from+pageSize);List<Resource> out=new ArrayList<>(to-from);for(int i=from;i<to;i++)out.add(arr[i]);return out;
    }
    public int filteredCount(String type,String category){int c=0;for(Resource r:resources)if((type==null||type.isBlank()||type.equalsIgnoreCase("all")||r.type().equalsIgnoreCase(type))&&(category==null||category.isBlank()||category.equalsIgnoreCase("all")||r.category().equalsIgnoreCase(category)))c++;return c;}

    private static void mergeSort(Resource[]a,int l,int h,String sort,String order){if(h-l<=1)return;int m=(l+h)>>>1;mergeSort(a,l,m,sort,order);mergeSort(a,m,h,sort,order);Resource[]tmp=new Resource[h-l];int i=l,j=m,k=0;while(i<m||j<h){if(j>=h||(i<m&&compare(a[i],a[j],sort,order)<=0))tmp[k++]=a[i++];else tmp[k++]=a[j++];}System.arraycopy(tmp,0,a,l,tmp.length);}
    private static int compare(Resource a,Resource b,String sort,String order){int c=switch(sort.toLowerCase()){case"author"->a.author().compareToIgnoreCase(b.author());case"year"->Integer.compare(a.year(),b.year());case"category"->a.category().compareToIgnoreCase(b.category());default->a.title().compareToIgnoreCase(b.title());};return order.equalsIgnoreCase("desc")?-c:c;}

    public SearchResult search(String query,String field,String algorithm,int limit){
        if(query==null||query.isBlank())throw new IllegalArgumentException("Search query is required");
        String q=query.toLowerCase(Locale.ROOT).trim();field=field==null?"all":field.toLowerCase();algorithm=algorithm==null||algorithm.isBlank()?"auto":algorithm.toLowerCase();limit=Math.max(1,Math.min(200,limit));
        if(algorithm.equals("auto")) algorithm = field.equals("isbn")||field.equals("identifier")||field.equals("id") ? "hash" : "kmp";
        long start=System.nanoTime();List<Resource> out=new ArrayList<>();int scanned=0,total=0;
        if(algorithm.equals("hash")&&(field.equals("isbn")||field.equals("identifier")||field.equals("id"))){Resource r=field.equals("id")?getById(q):getByIdentifier(q);if(r!=null){out.add(r);total=1;}scanned=1;}
        else {
            for(Resource r:resources){scanned++;String hay=fieldText(r,field).toLowerCase(Locale.ROOT);boolean match=switch(algorithm){case"naive"->StringAlgorithms.naiveIndexOf(hay,q)>=0;case"z"->StringAlgorithms.zIndexOf(hay,q)>=0;case"rabin-karp","rabinkarp","rk"->StringAlgorithms.rabinKarpIndexOf(hay,q)>=0;case"fuzzy"->fuzzyMatches(r,q,field);default->StringAlgorithms.kmpIndexOf(hay,q)>=0;};if(match){total++;if(out.size()<limit)out.add(r);}
            }
        }
        long micros=(System.nanoTime()-start)/1000;
        Map<String,Object> metrics=new LinkedHashMap<>();metrics.put("query",query);metrics.put("field",field);metrics.put("algorithm",algorithmName(algorithm));metrics.put("elapsedMicros",micros);metrics.put("scanned",scanned);metrics.put("totalMatches",total);metrics.put("returned",out.size());metrics.put("datasetSize",resources.size());metrics.put("complexity",complexity(algorithm));
        try{repo.appendLine("search_history.txt",Instant.now()+"|"+safe(query)+"|"+field+"|"+algorithm+"|"+total+"|"+micros);}catch(IOException ignored){}
        return new SearchResult(out,metrics);
    }
    private static String safe(String s){return s.replace('|','/').replace('
',' ');}
    private static String algorithmName(String a){return switch(a){case"z"->"Z-Function";case"rabin-karp","rabinkarp","rk"->"Rabin–Karp";case"naive"->"Naive Search";case"fuzzy"->"Levenshtein / Damerau DP";case"hash"->"Custom Hash Table";default->"Knuth–Morris–Pratt (KMP)";};}
    private static String complexity(String a){return switch(a){case"hash"->"Average O(1) lookup";case"fuzzy"->"O(n·m) per candidate";case"naive"->"O(n·m)";case"z"->"O(n+m)";case"rabin-karp","rabinkarp","rk"->"Average O(n+m)";default->"O(n+m)";};}
    private static String fieldText(Resource r,String f){return switch(f){case"title"->r.title();case"author"->r.author();case"isbn","identifier"->r.identifier();case"category"->r.category();case"subject"->r.subject();case"keyword","keywords"->r.keywords();case"id"->r.id();default->String.join(" ",r.title(),r.author(),r.identifier(),r.category(),r.subject(),r.keywords(),r.publisher());};}
    private static boolean fuzzyMatches(Resource r,String q,String field){String text=fieldText(r,field).toLowerCase(Locale.ROOT);String[]words=text.split("[^a-z0-9]+",-1);int threshold=Math.max(1,Math.min(3,q.length()/4));if(DynamicProgrammingAlgorithms.damerauLevenshtein(text,q)<=threshold)return true;for(String w:words)if(!w.isBlank()&&Math.abs(w.length()-q.length())<=threshold&&DynamicProgrammingAlgorithms.damerauLevenshtein(w,q)<=threshold)return true;return false;}

    public synchronized void add(Resource r)throws IOException{if(getById(r.id())!=null)throw new IllegalArgumentException("Duplicate resource ID");if(getByIdentifier(r.identifier())!=null)throw new IllegalArgumentException("Duplicate identifier");resources.add(r);rebuildIndexes();repo.saveResources(resources);repo.appendLine("activity_log.txt",Instant.now()+"|ADD|"+r.id());}
    public synchronized void update(Resource r)throws IOException{Resource old=getById(r.id());if(old==null)throw new IllegalArgumentException("Unknown resource ID");Resource sameIdentifier=getByIdentifier(r.identifier());if(sameIdentifier!=null&&!sameIdentifier.id().equalsIgnoreCase(r.id()))throw new IllegalArgumentException("Duplicate identifier");for(int i=0;i<resources.size();i++)if(resources.get(i).id().equalsIgnoreCase(r.id())){resources.set(i,r);break;}rebuildIndexes();repo.saveResources(resources);repo.appendLine("activity_log.txt",Instant.now()+"|UPDATE|"+r.id());}
    public synchronized boolean delete(String id)throws IOException{if(id==null)return false;for(int i=0;i<resources.size();i++)if(resources.get(i).id().equalsIgnoreCase(id)){Resource old=resources.removeAt(i);rebuildIndexes();repo.saveResources(resources);repo.appendLine("activity_log.txt",Instant.now()+"|DELETE|"+old.id());return true;}return false;}
    public List<String> history(int limit)throws IOException{List<String> lines=repo.readLines("search_history.txt");int from=Math.max(0,lines.size()-Math.max(1,limit));return new ArrayList<>(lines.subList(from,lines.size()));}
    public List<Resource> suggest(String q,int limit){if(q==null||q.isBlank())return List.of();String n=q.toLowerCase();List<Resource> out=new ArrayList<>();for(Resource r:resources)if(r.title().toLowerCase().contains(n)||r.author().toLowerCase().contains(n)){out.add(r);if(out.size()>=limit)break;}return out;}
}
