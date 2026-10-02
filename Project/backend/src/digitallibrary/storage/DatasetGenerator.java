package digitallibrary.storage;

import digitallibrary.model.Resource;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.*;
import java.util.Random;

public final class DatasetGenerator {
    private static final String[] TYPES={"BOOK","JOURNAL","RESEARCH_PAPER","MAGAZINE"};
    private static final String[] TOPICS={"Algorithms","Data Structures","Machine Learning","Artificial Intelligence","Operating Systems","Computer Networks","Databases","Cyber Security","Cloud Computing","Software Engineering","Internet of Things","Computer Vision","Natural Language Processing","Distributed Systems","Theory of Computation","Web Technologies","Data Analytics","Robotics","Quantum Computing","Bioinformatics"};
    private static final String[] CATEGORIES={"Computer Science","Engineering","Mathematics","Technology","Science","Research","Computing"};
    private static final String[] AUTHORS={"Aarav Sharma","Ananya Reddy","Vikram Iyer","Meera Nair","Arjun Rao","Ishita Verma","Rohan Gupta","Kavya Menon","Aditya Singh","Nisha Patel","Rahul Das","Sneha Kulkarni","Thomas Cormen","Stuart Russell","Abraham Silberschatz","Martin Kleppmann"};
    private static final String[] PUBLISHERS={"Academic Press","TechSphere Publishing","University Research Press","Open Knowledge House","Scholars International","Computing Review","Engineering World","Digital Science Press"};
    private static final String[] ADJ={"Foundations of","Advanced","Practical","Modern","Applied","Introduction to","Efficient","Scalable","Intelligent","Contemporary"};

    private DatasetGenerator(){}

    public static void generate(Path out,int count,long seed)throws IOException{
        if(count<1)throw new IllegalArgumentException("count must be positive");
        Files.createDirectories(out.toAbsolutePath().getParent());
        Random r=new Random(seed);
        try(BufferedWriter bw=Files.newBufferedWriter(out,StandardCharsets.UTF_8,StandardOpenOption.CREATE,StandardOpenOption.TRUNCATE_EXISTING)){
            for(int i=1;i<=count;i++){
                String type=TYPES[(i-1)%TYPES.length];
                String topic=TOPICS[(i*7+r.nextInt(TOPICS.length))%TOPICS.length];
                String second=TOPICS[(i*11+3)%TOPICS.length];
                String title=ADJ[(i+r.nextInt(ADJ.length))%ADJ.length]+" "+topic+(i%9==0?" and "+second:"")+(i%17==0?" — Volume "+(1+i%6):"");
                if(type.equals("RESEARCH_PAPER")) title="Study on "+topic+" for Large-Scale Digital Systems"+(i%23==0?" using "+second:"");
                if(type.equals("JOURNAL")) title="Journal of "+topic+" Research — Issue "+(1+i%12);
                if(type.equals("MAGAZINE")) title="Digital "+topic+" Review — "+(2020+i%7)+" Edition";
                String author=AUTHORS[(i*5+r.nextInt(AUTHORS.length))%AUTHORS.length];
                String id=String.format("LIB%06d",i);
                String identifier=switch(type){
                    case "BOOK" -> String.format("978-%010d",1000000000L+i);
                    case "JOURNAL" -> String.format("ISSN-%08d",i);
                    case "RESEARCH_PAPER" -> String.format("DOI-10.25%02d/dl.%06d",i%90,i);
                    default -> String.format("MAG-%06d",i);
                };
                String category=CATEGORIES[(i*3)%CATEGORIES.length];
                int year=2012+(i%15);
                String keywords=(topic+","+second+",digital library,search,indexing,"+(i%2==0?"algorithm":"retrieval")).toLowerCase();
                String availability=type.equals("BOOK")?(i%7==0?"Issued":"Available"):"Digital";
                Resource resource=new Resource(id,type,title,author,identifier,category,topic,year,keywords,availability,PUBLISHERS[(i*5)%PUBLISHERS.length]);
                bw.write(resource.toPipe()); bw.newLine();
            }
        }
    }

    public static void main(String[]args)throws Exception{
        int count=args.length>0?Integer.parseInt(args[0]):50_000;
        Path out=args.length>1?Path.of(args[1]):Path.of("data/resources.txt");
        long seed=args.length>2?Long.parseLong(args[2]):2520030477L;
        generate(out,count,seed);
        System.out.println("Generated "+count+" resources at "+out.toAbsolutePath());
    }
}
