package digitallibrary.model;
import java.util.*;
public final class SearchResult {
    private final List<Resource> resources;
    private final Map<String,Object> metrics;
    public SearchResult(List<Resource> resources, Map<String,Object> metrics){this.resources=resources;this.metrics=metrics;}
    public List<Resource> resources(){return resources;} public Map<String,Object> metrics(){return metrics;}
}
