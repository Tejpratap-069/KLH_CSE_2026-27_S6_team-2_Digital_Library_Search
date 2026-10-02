package digitallibrary;

import digitallibrary.model.Resource;
import digitallibrary.model.SearchResult;
import digitallibrary.structures.*;
import digitallibrary.storage.*;
import digitallibrary.algorithms.*;
import digitallibrary.service.*;
import digitallibrary.http.*;

import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public final class AllTests {
    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) throws Exception {
        run("resourceRoundTrip", AllTests::resourceRoundTrip);
        run("dynamicArrayGrowth", AllTests::dynamicArrayGrowth);
        run("linkedListBasics", AllTests::linkedListBasics);
        run("queueAndStack", AllTests::queueAndStack);
        run("hashTableBasics", AllTests::hashTableBasics);
        run("textRepositoryRoundTrip", AllTests::textRepositoryRoundTrip);

        run("stringAlgorithms", AllTests::stringAlgorithms);
        run("suffixArrayAndLcp", AllTests::suffixArrayAndLcp);
        run("dynamicProgramming", AllTests::dynamicProgramming);
        run("flowAlgorithms", AllTests::flowAlgorithms);
        run("approximationAlgorithms", AllTests::approximationAlgorithms);
        run("randomizedAndParallel", AllTests::randomizedAndParallel);

        run("datasetGenerator", AllTests::datasetGenerator);
        run("libraryServiceSearchAndCrud", AllTests::libraryServiceSearchAndCrud);
        run("algorithmLabSchemas", AllTests::algorithmLabSchemas);
        run("jsonUtil", AllTests::jsonUtil);
        run("apiServerSmoke", AllTests::apiServerSmoke);

        System.out.println("\nTests passed: " + passed + ", failed: " + failed);
        if (failed > 0) System.exit(1);
    }

    private static void run(String name, ThrowingRunnable test) {
        try { test.run(); passed++; System.out.println("PASS " + name); }
        catch (Throwable t) { failed++; System.out.println("FAIL " + name + " -> " + t); t.printStackTrace(System.out); }
    }

    private static void resourceRoundTrip() {
        Resource r = new Resource("LIB000001", "BOOK", "Algorithms", "Cormen", "9780001","Computer Science", "Algorithms", 2022, "dsa,graphs", "Available", "MIT Press");
        Resource p = Resource.fromPipe(r.toPipe()); eq(r.id(), p.id()); eq(r.title(), p.title()); eq(2022, p.year()); eq("dsa,graphs", p.keywords());
    }
    private static void dynamicArrayGrowth() { DynamicArray<Integer> a=new DynamicArray<>(); for(int i=0;i<100;i++)a.add(i); eq(100,a.size()); eq(73,a.get(73)); eq(50,a.removeAt(50)); eq(99,a.size()); }
    private static void linkedListBasics(){SinglyLinkedList<String> l=new SinglyLinkedList<>();l.add("a");l.add("b");l.addFirst("z");eq(3,l.size());eq("z",l.get(0));check(l.remove("a"),"remove should succeed");eq(2,l.size());}
    private static void queueAndStack(){ArrayQueue<Integer> q=new ArrayQueue<>();q.offer(1);q.offer(2);q.offer(3);eq(1,q.poll());eq(2,q.poll());ArrayStack<String>s=new ArrayStack<>();s.push("a");s.push("b");eq("b",s.pop());eq("a",s.peek());}
    private static void hashTableBasics(){StringHashTable<Integer> h=new StringHashTable<>(8);h.put("isbn-1",10);h.put("isbn-2",20);h.put("isbn-1",11);eq(11,h.get("isbn-1"));eq(20,h.remove("isbn-2"));check(h.get("isbn-2")==null,"removed key should be absent");}

    private static void textRepositoryRoundTrip() throws Exception {
        Path dir=Files.createTempDirectory("dl-test");TextRepository repo=new TextRepository(dir);DynamicArray<Resource> resources=new DynamicArray<>();resources.add(new Resource("LIB000001","BOOK","Algorithms","Cormen","ID1","CS","DSA",2022,"dsa","Available","MIT"));repo.saveResources(resources);DynamicArray<Resource> loaded=repo.loadResources();eq(1,loaded.size());eq("Algorithms",loaded.get(0).title());repo.appendLine("activity_log.txt","TEST|OK");check(Files.readString(dir.resolve("activity_log.txt")).contains("TEST|OK"),"append should persist");
    }
    private static void stringAlgorithms(){String text="digital library search algorithms",pat="library";int expected=text.indexOf(pat);eq(expected,StringAlgorithms.naiveIndexOf(text,pat));eq(expected,StringAlgorithms.kmpIndexOf(text,pat));eq(expected,StringAlgorithms.zIndexOf(text,pat));eq(expected,StringAlgorithms.rabinKarpIndexOf(text,pat));eq(-1,StringAlgorithms.kmpIndexOf(text,"quantum"));eq(3,StringAlgorithms.ahoCorasickMatchCount("data algorithms and data",new String[]{"data","algorithm"}));}
    private static void suffixArrayAndLcp(){int[]sa=SuffixAlgorithms.buildSuffixArray("banana");arrEq(new int[]{5,3,1,0,4,2},sa);arrEq(new int[]{1,3,0,0,2},SuffixAlgorithms.buildLcp("banana",sa));}
    private static void dynamicProgramming(){eq(3,DynamicProgrammingAlgorithms.levenshtein("kitten","sitting"));eq(1,DynamicProgrammingAlgorithms.damerauLevenshtein("ca","ac"));eq(1,DynamicProgrammingAlgorithms.weightedEditDistance("abc","axc",1,2,1));DynamicProgrammingAlgorithms.Selection sel=DynamicProgrammingAlgorithms.bitmaskSelection(new int[]{7,4,5},new int[]{4,3,2},5);eq(9,sel.totalValue());eq(5,sel.totalCost());eq(26000,DynamicProgrammingAlgorithms.matrixChainCost(new int[]{40,20,30,10,30}));eq(3, DynamicProgrammingAlgorithms.optimalBstCost(new int[]{1,1}));
        check(DynamicProgrammingAlgorithms.needlemanWunschScore("ABC", "ABC", 2, -1, -2) > 0, "global alignment score");
        check(DynamicProgrammingAlgorithms.smithWatermanScore("ALGORITHM", "LOGARITHM", 2, -1, -2) > 0, "local alignment score");
        int[][] tsp={{0,10,15,20},{10,0,35,25},{15,35,0,30},{20,25,30,0}};
        eq(80, DynamicProgrammingAlgorithms.tspBitmask(tsp));
        boolean[][] ham={{false,true,false},{true,false,true},{false,true,false}};
        check(DynamicProgrammingAlgorithms.hamiltonianPathExists(ham), "Hamiltonian path should exist");
        eq(17, DynamicProgrammingAlgorithms.treeMaxIndependentSet(new int[]{-1,0,0,1,1,2}, new int[]{5,4,6,3,2,7}));
        arrEq(new int[]{1,3,4,10}, DynamicProgrammingAlgorithms.sumOverSubsets(new int[]{1,2,3,4},2));
    }

    private static void flowAlgorithms() {
        int[][] cap = new int[6][6]; cap[0][1]=16; cap[0][2]=13; cap[1][2]=10; cap[2][1]=4; cap[1][3]=12; cap[3][2]=9; cap[2][4]=14; cap[4][3]=7; cap[3][5]=20; cap[4][5]=4;
        FlowAlgorithms.FlowResult result=FlowAlgorithms.maxFlowEdmondsKarp(cap,0,5);eq(23,result.maxFlow());eq(23,FlowAlgorithms.maxFlowFordFulkerson(cap,0,5).maxFlow());eq(23,FlowAlgorithms.maxFlowDinic(cap,0,5).maxFlow());check(result.minCutReachable()[0],"source should be reachable in residual min-cut");boolean[][]matching={{true,false,true},{true,true,false},{false,true,true}};eq(3,FlowAlgorithms.maximumBipartiteMatching(matching));
    }
    private static void approximationAlgorithms(){boolean[][]sets={{true,true,false,false,false},{false,true,true,true,false},{false,false,false,true,true},{true,false,true,false,true}};int[]chosen=ApproximationAlgorithms.greedySetCover(sets,5);boolean[]covered=new boolean[5];for(int idx:chosen)for(int j=0;j<5;j++)if(sets[idx][j])covered[j]=true;for(boolean b:covered)check(b,"set cover must cover universe");int[][]edges={{0,1},{1,2},{2,3},{3,0}};int[]vc=ApproximationAlgorithms.vertexCover2Approx(4,edges);check(ApproximationAlgorithms.isVertexCover(4,edges,vc),"2-approx must be valid");eq(2,ApproximationAlgorithms.exactMinimumVertexCoverSize(4,edges));}
    private static void randomizedAndParallel(){int[]arr={9,2,7,3,8,1,4,6,5};RandomizedParallelAlgorithms.randomizedQuickSort(arr,42L);for(int i=1;i<arr.length;i++)check(arr[i-1]<=arr[i],"array sorted");eq(10,RandomizedParallelAlgorithms.reservoirSample(100,10,42L).length);String[]values={"alpha beta","beta","gamma","alphabet","none"};eq(RandomizedParallelAlgorithms.sequentialCountMatches(values,"alpha"),RandomizedParallelAlgorithms.parallelCountMatches(values,"alpha"));check(RandomizedParallelAlgorithms.millerRabin(2_147_483_647L,8,42L),"prime");check(!RandomizedParallelAlgorithms.millerRabin(221L,8,42L),"composite");eq(15L,RandomizedParallelAlgorithms.parallelReduceSum(new int[]{1,2,3,4,5}));arrEq(new int[]{0,3,4,8},RandomizedParallelAlgorithms.parallelPrefixSum(new int[]{3,1,4,1}));}
    private static void datasetGenerator() throws Exception {Path dir=Files.createTempDirectory("dl-gen");Path out=dir.resolve("resources.txt");DatasetGenerator.generate(out,10000,12345L);List<String>lines=Files.readAllLines(out,StandardCharsets.UTF_8);eq(10000,lines.size());Set<String>ids=new HashSet<>(),identifiers=new HashSet<>(),types=new HashSet<>();for(String line:lines){Resource r=Resource.fromPipe(line);ids.add(r.id());identifiers.add(r.identifier());types.add(r.type());}eq(10000,ids.size());eq(10000,identifiers.size());check(types.containsAll(List.of("BOOK","JOURNAL","RESEARCH_PAPER","MAGAZINE")),"all resource types expected");}
    private static void libraryServiceSearchAndCrud() throws Exception {Path dir=Files.createTempDirectory("dl-service");DatasetGenerator.generate(dir.resolve("resources.txt"),300,77L);LibraryService svc=new LibraryService(new TextRepository(dir));eq(300,((Number)svc.stats().get("totalResources")).intValue());SearchResult result=svc.search("data","all","kmp",20);check(result.metrics().containsKey("elapsedMicros"),"metrics");Resource n=new Resource("LIB999999","BOOK","Special Testing Book","Test Author","T-999999","Testing","QA",2026,"test,verification","Available","OpenAI Press");svc.add(n);check(svc.getById("LIB999999")!=null,"add");svc.update(n.withTitle("Special Testing Book Revised"));check(svc.delete("LIB999999"),"delete");}
    private static void algorithmLabSchemas() throws Exception {
        Path dir=Files.createTempDirectory("dl-lab");DatasetGenerator.generate(dir.resolve("resources.txt"),1000,88L);LibraryService svc=new LibraryService(new TextRepository(dir));AlgorithmLabService lab=new AlgorithmLabService(svc);eq("CO1",lab.co1Classification("machine learning").get("co"));eq("CO2",lab.stringDemo("algorithm").get("co"));Map<String,Object>dp=lab.advancedDpDemo("algoritms");eq("CO3",dp.get("co"));check(dp.containsKey("intervalDp")&&dp.containsKey("bitmaskDp")&&dp.containsKey("dpOnTrees")&&dp.containsKey("dpOnSubsetsSOS"),"formal CO3 patterns expected");eq("CO4",lab.flowDemo().get("co"));eq("CO5",lab.approximationDemo().get("co"));eq("CO6",lab.co6Demo("data").get("co"));
    }
    private static void jsonUtil(){Map<String,Object>m=new LinkedHashMap<>();m.put("name","A \"Book\"");m.put("count",3);m.put("ok",true);String json=JsonUtil.toJson(m);check(json.contains("\\\"Book\\\""),"JSON escaping");Map<String,String>parsed=JsonUtil.parseFlatObject("{\"title\":\"Hello\",\"year\":\"2026\"}");eq("Hello",parsed.get("title"));}
    private static void apiServerSmoke() throws Exception {Path dir=Files.createTempDirectory("dl-api");DatasetGenerator.generate(dir.resolve("resources.txt"),120,99L);Path frontend=Files.createTempDirectory("dl-front");Files.writeString(frontend.resolve("index.html"),"<h1>Digital Library</h1>");LibraryService svc=new LibraryService(new TextRepository(dir));ApiServer server=new ApiServer(0,svc,new AlgorithmLabService(svc),frontend);server.start();try{int port=server.port();java.net.http.HttpClient client=java.net.http.HttpClient.newHttpClient();java.net.http.HttpRequest req=java.net.http.HttpRequest.newBuilder(java.net.URI.create("http://127.0.0.1:"+port+"/api/stats")).GET().build();java.net.http.HttpResponse<String>res=client.send(req,java.net.http.HttpResponse.BodyHandlers.ofString());eq(200,res.statusCode());check(res.body().contains("totalResources"),"stats json expected");}finally{server.stop();}}
    private static void eq(Object expected,Object actual){if(!Objects.equals(expected,actual))throw new AssertionError("expected="+expected+" actual="+actual);}
    private static void arrEq(int[]e,int[]a){if(!Arrays.equals(e,a))throw new AssertionError("expected="+Arrays.toString(e)+" actual="+Arrays.toString(a));}
    private static void check(boolean cond,String msg){if(!cond)throw new AssertionError(msg);}
    @FunctionalInterface interface ThrowingRunnable{void run()throws Exception;}
}
