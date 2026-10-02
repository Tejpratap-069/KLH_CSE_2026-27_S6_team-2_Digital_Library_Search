package digitallibrary;
import digitallibrary.storage.*;import digitallibrary.service.*;import digitallibrary.http.*;
import java.nio.file.*;
public final class Main{
    public static void main(String[]args)throws Exception{
        int port=args.length>0?Integer.parseInt(args[0]):Integer.parseInt(System.getenv().getOrDefault("PORT","8080"));
        Path root=Path.of(System.getProperty("user.dir")).toAbsolutePath().normalize();Path data=root.resolve("data"),frontend=root.resolve("frontend");
        Files.createDirectories(data);Path resources=data.resolve("resources.txt");if(!Files.exists(resources)||Files.size(resources)==0){System.out.println("resources.txt empty; generating 50,000 records...");DatasetGenerator.generate(resources,50_000,2520030477L);}
        TextRepository repo=new TextRepository(data);LibraryService library=new LibraryService(repo);AlgorithmLabService lab=new AlgorithmLabService(library);ApiServer server=new ApiServer(port,library,lab,frontend);server.start();
        System.out.println("Digital Library Search System running at http://localhost:"+server.port());
        System.out.println("Loaded "+library.size()+" resources from text files. Database: NONE.");
    }
}
