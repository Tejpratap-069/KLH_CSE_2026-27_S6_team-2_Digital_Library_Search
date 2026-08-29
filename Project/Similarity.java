import java.util.*;

class Similarity {

    static double jaccardSimilarity(String text1, String text2) {

        Set<String> set1 =
                new HashSet<>(Arrays.asList(
                        text1.toLowerCase().split("\\s+")));

        Set<String> set2 =
                new HashSet<>(Arrays.asList(
                        text2.toLowerCase().split("\\s+")));

        Set<String> intersection =
                new HashSet<>(set1);

        intersection.retainAll(set2);

        Set<String> union =
                new HashSet<>(set1);

        union.addAll(set2);

        if (union.isEmpty())
            return 0;

        return (double) intersection.size()
                / union.size();
    }
}