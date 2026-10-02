package digitallibrary.storage;
import digitallibrary.model.Resource;
import digitallibrary.structures.DynamicArray;
import java.nio.file.*;import java.nio.charset.StandardCharsets;import java.io.*;
public final class TextRepository{
    private final Path dir; public TextRepository(Path dir)throws IOException{this.dir=dir;Files.createDirectories(dir);ensure("resources.txt");ensure("users.txt");ensure("requests.txt");ensure("search_history.txt");ensure("activity_log.txt");}
    public Path dir(){return dir;} private void ensure(String n)throws IOException{Path p=dir.resolve(n);if(!Files.exists(p))Files.createFile(p);}
    public DynamicArray<Resource> loadResources()throws IOException{DynamicArray<Resource>a=new DynamicArray<>();try(BufferedReader br=Files.newBufferedReader(dir.resolve("resources.txt"),StandardCharsets.UTF_8)){String line;while((line=br.readLine())!=null){if(line.isBlank())continue;try{a.add(Resource.fromPipe(line));}catch(RuntimeException ex){appendLine("activity_log.txt","MALFORMED|"+System.currentTimeMillis()+"|"+line.replace('|','/'));}}}return a;}
    public synchronized void saveResources(DynamicArray<Resource> a)throws IOException{Path target=dir.resolve("resources.txt"),tmp=dir.resolve("resources.txt.tmp");try(BufferedWriter bw=Files.newBufferedWriter(tmp,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)){for(Resource r:a){bw.write(r.toPipe());bw.newLine();}}try{Files.move(tmp,target,StandardCopyOption.REPLACE_EXISTING,StandardCopyOption.ATOMIC_MOVE);}catch(AtomicMoveNotSupportedException e){Files.move(tmp,target,StandardCopyOption.REPLACE_EXISTING);}}
    public synchronized void appendLine(String file,String line)throws IOException{Files.writeString(dir.resolve(file),line+System.lineSeparator(),StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.APPEND);}
    public java.util.List<String> readLines(String file)throws IOException{return Files.readAllLines(dir.resolve(file),StandardCharsets.UTF_8);}
}
