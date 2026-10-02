package digitallibrary.http;

import com.sun.net.httpserver.*;
import digitallibrary.model.*;
import digitallibrary.service.*;

import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public final class ApiServer {
    private final HttpServer server;
    private final LibraryService library;
    private final AlgorithmLabService lab;
    private final Path frontendRoot;
    private final ExecutorService executor;

    public ApiServer(int port, LibraryService library, AlgorithmLabService lab, Path frontendRoot) throws IOException {
        this.library=library; this.lab=lab; this.frontendRoot=frontendRoot.toAbsolutePath().normalize();
        this.server=HttpServer.create(new InetSocketAddress("0.0.0.0",port),0);
        this.executor=Executors.newFixedThreadPool(Math.max(4,Runtime.getRuntime().availableProcessors()));
        server.setExecutor(executor); server.createContext("/",this::handle);
    }
    public void start(){server.start();}
    public void stop(){server.stop(0);executor.shutdownNow();}
    public int port(){return server.getAddress().getPort();}

    private void handle(HttpExchange ex) throws IOException {
        try {
            if(ex.getRequestMethod().equalsIgnoreCase("OPTIONS")){cors(ex);ex.sendResponseHeaders(204,-1);return;}
            String path=ex.getRequestURI().getPath();
            if(path.startsWith("/api/")) handleApi(ex,path); else handleStatic(ex,path);
        } catch(MethodException e){json(ex,405,Map.of("error",e.getMessage()));}
          catch(IllegalArgumentException e){json(ex,400,Map.of("error",e.getMessage()==null?"Invalid request":e.getMessage()));}
          catch(Exception e){e.printStackTrace();json(ex,500,Map.of("error","Internal server error","detail",String.valueOf(e.getMessage())));}
    }

    private void handleApi(HttpExchange ex,String path)throws Exception{
        Map<String,String> q=query(ex.getRequestURI().getRawQuery());String method=ex.getRequestMethod().toUpperCase(Locale.ROOT);
        switch(path){
            case "/api/stats" -> require(method,"GET",()->json(ex,200,library.stats()));
            case "/api/resources" -> require(method,"GET",()->{
                int page=intParam(q,"page",1),pageSize=intParam(q,"pageSize",24);String type=q.getOrDefault("type",""),cat=q.getOrDefault("category","");
                List<Resource> rows=library.page(page,pageSize,type,cat,q.getOrDefault("sort","title"),q.getOrDefault("order","asc"));
                json(ex,200,Map.of("page",page,"pageSize",pageSize,"total",library.filteredCount(type,cat),"resources",rows));});
            case "/api/resource" -> require(method,"GET",()->{Resource r=library.getById(required(q,"id"));if(r==null)json(ex,404,Map.of("error","Resource not found"));else json(ex,200,r);});
            case "/api/search" -> require(method,"GET",()->{SearchResult r=library.search(required(q,"q"),q.getOrDefault("field","all"),q.getOrDefault("algorithm","auto"),intParam(q,"limit",40));json(ex,200,Map.of("resources",r.resources(),"metrics",r.metrics()));});
            case "/api/suggest" -> require(method,"GET",()->json(ex,200,Map.of("resources",library.suggest(q.getOrDefault("q",""),intParam(q,"limit",8)))));
            case "/api/history" -> require(method,"GET",()->json(ex,200,Map.of("history",library.history(intParam(q,"limit",30)))));
            case "/api/admin/resource" -> handleAdmin(ex,method,q);
            case "/api/algorithms/classify" -> require(method,"GET",()->json(ex,200,lab.co1Classification(q.getOrDefault("q","algorithm search"))));
            case "/api/algorithms/string" -> require(method,"GET",()->json(ex,200,lab.stringDemo(q.getOrDefault("q","algorithm"))));
            case "/api/algorithms/fuzzy" -> require(method,"GET",()->json(ex,200,lab.fuzzyDemo(q.getOrDefault("q","algoritms"))));
            case "/api/algorithms/dp" -> require(method,"GET",()->json(ex,200,lab.advancedDpDemo(q.getOrDefault("q","algoritms"))));
            case "/api/algorithms/flow" -> { if(!method.equals("GET")&&!method.equals("POST")) methodNotAllowed(ex); else json(ex,200,lab.flowDemo()); }
            case "/api/algorithms/approximation" -> { if(!method.equals("GET")&&!method.equals("POST")) methodNotAllowed(ex); else json(ex,200,lab.approximationDemo()); }
            case "/api/algorithms/randomized" -> require(method,"GET",()->json(ex,200,lab.randomizedDemo()));
            case "/api/algorithms/co6" -> require(method,"GET",()->json(ex,200,lab.co6Demo(q.getOrDefault("q","data"))));
            case "/api/algorithms/parallel" -> require(method,"GET",()->json(ex,200,lab.parallelDemo(q.getOrDefault("q","data"))));
            case "/api/analytics" -> require(method,"GET",()->json(ex,200,lab.analytics(q.getOrDefault("q","algorithm"))));
            default -> json(ex,404,Map.of("error","API endpoint not found"));
        }
    }

    private void handleAdmin(HttpExchange ex,String method,Map<String,String>q)throws Exception{
        switch(method){
            case "POST" -> {Map<String,String>b=JsonUtil.parseFlatObject(readBody(ex));Resource r=resourceFrom(b);library.add(r);json(ex,201,Map.of("message","Resource added","resource",r));}
            case "PUT" -> {Map<String,String>b=JsonUtil.parseFlatObject(readBody(ex));Resource r=resourceFrom(b);library.update(r);json(ex,200,Map.of("message","Resource updated","resource",r));}
            case "DELETE" -> {String id=required(q,"id");if(!library.delete(id))json(ex,404,Map.of("error","Resource not found"));else json(ex,200,Map.of("message","Resource deleted","id",id));}
            default -> methodNotAllowed(ex);
        }
    }
    private static Resource resourceFrom(Map<String,String>b){return new Resource(reqBody(b,"id"),reqBody(b,"type"),reqBody(b,"title"),reqBody(b,"author"),reqBody(b,"identifier"),b.getOrDefault("category","General"),b.getOrDefault("subject","General"),parseYear(b.get("year")),b.getOrDefault("keywords",""),b.getOrDefault("availability","Available"),b.getOrDefault("publisher","Unknown"));}
    private static int parseYear(String y){try{return y==null||y.isBlank()?2026:Integer.parseInt(y);}catch(Exception e){throw new IllegalArgumentException("Invalid year");}}
    private static String reqBody(Map<String,String>b,String k){String v=b.get(k);if(v==null||v.isBlank())throw new IllegalArgumentException("Missing field: "+k);return v;}

    private void handleStatic(HttpExchange ex,String path)throws IOException{
        if(!ex.getRequestMethod().equalsIgnoreCase("GET")){methodNotAllowed(ex);return;}if(path.equals("/"))path="/index.html";
        Path file=frontendRoot.resolve(path.substring(1)).normalize();if(!file.startsWith(frontendRoot)||Files.isDirectory(file)||!Files.exists(file)){json(ex,404,Map.of("error","Page not found"));return;}
        byte[]bytes=Files.readAllBytes(file);Headers h=ex.getResponseHeaders();h.set("Content-Type",contentType(file));h.set("Cache-Control","no-cache");cors(ex);ex.sendResponseHeaders(200,bytes.length);try(OutputStream os=ex.getResponseBody()){os.write(bytes);}
    }

    private static String contentType(Path p){String n=p.getFileName().toString().toLowerCase();if(n.endsWith(".html"))return"text/html; charset=utf-8";if(n.endsWith(".css"))return"text/css; charset=utf-8";if(n.endsWith(".js"))return"application/javascript; charset=utf-8";if(n.endsWith(".svg"))return"image/svg+xml";if(n.endsWith(".png"))return"image/png";return"application/octet-stream";}
    private static Map<String,String> query(String raw){Map<String,String>m=new LinkedHashMap<>();if(raw==null||raw.isBlank())return m;for(String part:raw.split("&")){String[]kv=part.split("=",2);String k=decode(kv[0]),v=kv.length>1?decode(kv[1]):"";m.put(k,v);}return m;}
    private static String decode(String s){return URLDecoder.decode(s,StandardCharsets.UTF_8);}
    private static int intParam(Map<String,String>q,String k,int d){try{return q.containsKey(k)?Integer.parseInt(q.get(k)):d;}catch(Exception e){throw new IllegalArgumentException("Invalid integer: "+k);}}
    private static String required(Map<String,String>q,String k){String v=q.get(k);if(v==null||v.isBlank())throw new IllegalArgumentException("Missing query parameter: "+k);return v;}
    private static String readBody(HttpExchange ex)throws IOException{byte[]b=ex.getRequestBody().readNBytes(1_000_000);return new String(b,StandardCharsets.UTF_8);}

    @FunctionalInterface private interface IOAction{void run()throws Exception;}
    private static void require(String actual,String expected,IOAction action)throws Exception{if(!actual.equals(expected))throw new MethodException();action.run();}
    private static final class MethodException extends IllegalArgumentException{MethodException(){super("Method not allowed");}}
    private static void methodNotAllowed(HttpExchange ex)throws IOException{json(ex,405,Map.of("error","Method not allowed"));}
    private static void cors(HttpExchange ex){Headers h=ex.getResponseHeaders();h.set("Access-Control-Allow-Origin","*");h.set("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,OPTIONS");h.set("Access-Control-Allow-Headers","Content-Type");}
    private static void json(HttpExchange ex,int status,Object obj)throws IOException{byte[]b=JsonUtil.toJson(obj).getBytes(StandardCharsets.UTF_8);Headers h=ex.getResponseHeaders();h.set("Content-Type","application/json; charset=utf-8");h.set("Cache-Control","no-store");cors(ex);ex.sendResponseHeaders(status,b.length);try(OutputStream os=ex.getResponseBody()){os.write(b);}}
}
