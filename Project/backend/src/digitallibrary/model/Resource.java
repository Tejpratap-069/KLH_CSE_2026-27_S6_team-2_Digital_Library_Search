package digitallibrary.model;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Resource {
    private final String id;
    private final String type;
    private final String title;
    private final String author;
    private final String identifier;
    private final String category;
    private final String subject;
    private final int year;
    private final String keywords;
    private final String availability;
    private final String publisher;

    public Resource(String id, String type, String title, String author, String identifier,
                    String category, String subject, int year, String keywords,
                    String availability, String publisher) {
        this.id = clean(id); this.type = clean(type); this.title = clean(title); this.author = clean(author);
        this.identifier = clean(identifier); this.category = clean(category); this.subject = clean(subject);
        this.year = year; this.keywords = clean(keywords); this.availability = clean(availability); this.publisher = clean(publisher);
        if (this.id.isEmpty() || this.title.isEmpty() || this.identifier.isEmpty())
            throw new IllegalArgumentException("id, title and identifier are required");
    }

    private static String clean(String s) { return s == null ? "" : s.replace('|','/').replace('\n',' ').replace('\r',' ').trim(); }

    public String id(){return id;} public String type(){return type;} public String title(){return title;}
    public String author(){return author;} public String identifier(){return identifier;} public String category(){return category;}
    public String subject(){return subject;} public int year(){return year;} public String keywords(){return keywords;}
    public String availability(){return availability;} public String publisher(){return publisher;}

    public String toPipe() {
        return String.join("|", id,type,title,author,identifier,category,subject,Integer.toString(year),keywords,availability,publisher);
    }

    public static Resource fromPipe(String line) {
        String[] p = line.split("\\|", -1);
        if (p.length != 11) throw new IllegalArgumentException("Expected 11 fields, got " + p.length);
        int y;
        try { y = Integer.parseInt(p[7]); } catch(NumberFormatException e) { throw new IllegalArgumentException("Invalid year", e); }
        return new Resource(p[0],p[1],p[2],p[3],p[4],p[5],p[6],y,p[8],p[9],p[10]);
    }

    public Resource withTitle(String newTitle){ return new Resource(id,type,newTitle,author,identifier,category,subject,year,keywords,availability,publisher); }

    public Map<String,Object> toMap(){
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("id",id); m.put("type",type); m.put("title",title); m.put("author",author); m.put("identifier",identifier);
        m.put("category",category); m.put("subject",subject); m.put("year",year); m.put("keywords",keywords);
        m.put("availability",availability); m.put("publisher",publisher); return m;
    }
}
