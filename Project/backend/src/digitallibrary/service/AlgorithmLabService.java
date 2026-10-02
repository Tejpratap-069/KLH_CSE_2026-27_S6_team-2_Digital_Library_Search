package digitallibrary.service;

import digitallibrary.algorithms.ApproximationAlgorithms;
import digitallibrary.algorithms.DynamicProgrammingAlgorithms;
import digitallibrary.algorithms.FlowAlgorithms;
import digitallibrary.algorithms.RandomizedParallelAlgorithms;
import digitallibrary.algorithms.StringAlgorithms;
import digitallibrary.algorithms.SuffixAlgorithms;
import digitallibrary.model.Resource;
import digitallibrary.model.SearchResult;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Handout-accurate CO1-CO6 demonstrations for 25CS2103E DSA-3.
 * The normal Digital Library remains a useful application; this service exposes the
 * course-outcome demonstrations against the same repository.
 */
public final class AlgorithmLabService {
    private final LibraryService library;
    public AlgorithmLabService(LibraryService library) { this.library = library; }

    /** CO1: evaluate problem-class signature and select an appropriate advanced strategy. */
    public Map<String,Object> co1Classification(String query) {
        Map<String,Object> m = base("CO1", "Problem Classification & Algorithm Selection");
        String q = query == null ? "" : query.trim();
        String lower = q.toLowerCase();
        String problemClass;
        String strategy;
        String reason;
        String cost;

        if (q.matches("(?i)LIB\\d+") || q.matches("(?i)(ISBN|ID)[: -].+")) {
            problemClass = "Exact identifier lookup";
            strategy = "Custom hash index";
            reason = "An exact key lookup does not need a full substring scan.";
            cost = "Expected O(1) lookup after indexing";
        } else if (lower.contains("typo") || lower.contains("misspell") || lower.contains("fuzzy")) {
            problemClass = "Approximate string matching";
            strategy = "Dynamic Programming / Edit Distance";
            reason = "Edit distance models insertions, deletions, substitutions and transpositions.";
            cost = "O(n·m) for two strings";
        } else if (lower.contains("similar") || lower.contains("suffix") || lower.contains("longest common")) {
            problemClass = "Document / substring similarity";
            strategy = "Suffix Array + LCP (Kasai)";
            reason = "Suffix structures support repeated-substring and similarity analysis.";
            cost = "Suffix build O(n log² n) here; LCP O(n)";
        } else if (lower.contains("flow") || lower.contains("assign") || lower.contains("allocate") || lower.contains("matching")) {
            problemClass = "Capacity-constrained assignment / matching";
            strategy = "Network Flow (Edmonds-Karp / Dinic)";
            reason = "Users and limited resources can be reduced to a capacitated flow network.";
            cost = "Edmonds-Karp O(VE²)";
        } else if (lower.contains("np") || lower.contains("cover") || lower.contains("schedule") || lower.contains("optim")) {
            problemClass = "NP-hard optimisation";
            strategy = "Approximation algorithm";
            reason = "For NP-hard optimisation, a provable approximation can be practical when exact enumeration is expensive.";
            cost = "Depends on approximation; Vertex Cover demo is polynomial";
        } else if (lower.contains("prime") || lower.contains("random") || lower.contains("sample")) {
            problemClass = "Randomised computation";
            strategy = "Miller-Rabin / Reservoir Sampling / Randomised QuickSort";
            reason = "Randomness can improve expected performance or provide efficient probabilistic tests/sampling.";
            cost = "Algorithm-specific";
        } else if (lower.contains("parallel") || lower.contains("prefix") || lower.contains("reduce")) {
            problemClass = "Parallel data processing";
            strategy = "Parallel reduce / prefix-sum with work-span analysis";
            reason = "Independent data partitions can use multiple processors.";
            cost = "O(n) work; logarithmic ideal span for tree-style primitives";
        } else {
            problemClass = "Substring / pattern search";
            strategy = "Linear-time String Matching (KMP / Z)";
            reason = "The query is a pattern inside larger title/author/keyword text.";
            cost = "O(n+m)";
        }

        m.put("query", q);
        m.put("problemClass", problemClass);
        m.put("selectedStrategy", strategy);
        m.put("reason", reason);
        m.put("asymptoticCost", cost);
        m.put("handoutExamples", List.of(
                "substring search → string algorithms",
                "sequence/fuzzy alignment → dynamic programming",
                "flow/assignment → network flow",
                "NP-hard optimisation → approximation",
                "primality/sampling → randomised algorithms",
                "large data primitives → parallel algorithms"));
        return m;
    }

    /** CO2: Naive, KMP, Z, Rabin-Karp, Aho-Corasick, Suffix Array and LCP/Kasai. */
    public Map<String,Object> stringDemo(String query) {
        String q = (query == null || query.isBlank()) ? "algorithm" : query.toLowerCase();
        List<Resource> sample = library.page(1, Math.min(200, library.size()), "", "", "title", "asc");
        StringBuilder sb = new StringBuilder();
        for (Resource r : sample) sb.append(r.title().toLowerCase()).append(" | ");
        String text = sb.toString();

        Map<String,Object> m = base("CO2", "Linear-Time String Algorithms & Suffix Structures");
        m.put("query", q);
        m.put("sampleCharacters", text.length());
        List<Map<String,Object>> runs = new ArrayList<>();
        runs.add(runString("Naive Pattern Matching", "O(n·m)", () -> StringAlgorithms.naiveIndexOf(text, q)));
        runs.add(runString("KMP", "O(n+m)", () -> StringAlgorithms.kmpIndexOf(text, q)));
        runs.add(runString("Z-Function", "O(n+m)", () -> StringAlgorithms.zIndexOf(text, q)));
        runs.add(runString("Rabin-Karp (double rolling hash)", "Average O(n+m)", () -> StringAlgorithms.rabinKarpIndexOf(text, q)));
        m.put("singlePatternRuns", runs);
        m.put("runs", runs); // compatibility with Analytics UI

        String[] patterns = {q, "data", "algorithm"};
        m.put("ahoCorasick", Map.of(
                "patterns", List.of(patterns),
                "matchEndings", StringAlgorithms.ahoCorasickMatchCount(text, patterns),
                "purpose", "Multi-pattern matching with trie + failure links"));

        String suffixText = sample.isEmpty() ? "digital library" : sample.get(0).title().toLowerCase();
        int[] sa = SuffixAlgorithms.buildSuffixArray(suffixText);
        int[] lcp = SuffixAlgorithms.buildLcp(suffixText, sa);
        m.put("suffixStructures", Map.of(
                "text", suffixText,
                "suffixArrayPreview", preview(sa, 14),
                "lcpPreview", preview(lcp, 14),
                "longestRepeatedSubstringLength", SuffixAlgorithms.longestRepeatedSubstringLength(suffixText),
                "construction", "doubling + hand-built merge sort; LCP via Kasai"));
        return m;
    }

    /** CO3 formal mapping: interval DP, bitmask DP, DP on trees, DP on subsets; module demos also include edit/alignment. */
    public Map<String,Object> advancedDpDemo(String query) {
        String q = (query == null || query.isBlank()) ? "algoritms" : query;
        Map<String,Object> m = base("CO3", "Advanced Dynamic Programming");

        SearchResult fuzzy = library.search(q, "title", "fuzzy", 8);
        m.put("moduleEditDistance", Map.of(
                "query", q,
                "levenshteinToAlgorithms", DynamicProgrammingAlgorithms.levenshtein(q.toLowerCase(), "algorithms"),
                "damerauToAlgorithms", DynamicProgrammingAlgorithms.damerauLevenshtein(q.toLowerCase(), "algorithms"),
                "weightedToAlgorithm", DynamicProgrammingAlgorithms.weightedEditDistance(q.toLowerCase(), "algorithm", 1, 2, 1),
                "fuzzySearchMetrics", fuzzy.metrics()));

        m.put("sequenceAlignment", Map.of(
                "needlemanWunschGlobalScore", DynamicProgrammingAlgorithms.needlemanWunschScore("ALGORITHM", "ALGORITM", 2, -1, -2),
                "smithWatermanLocalScore", DynamicProgrammingAlgorithms.smithWatermanScore("ALGORITHM", "LOGARITHM", 2, -1, -2)));

        int[] dims = {40,20,30,10,30};
        int[] freqs = {4,2,6,3};
        m.put("intervalDp", Map.of(
                "matrixDimensions", ints(dims),
                "matrixChainMinimumMultiplications", DynamicProgrammingAlgorithms.matrixChainCost(dims),
                "optimalBstFrequencies", ints(freqs),
                "optimalBstCost", DynamicProgrammingAlgorithms.optimalBstCost(freqs)));

        DynamicProgrammingAlgorithms.Selection selection = DynamicProgrammingAlgorithms.bitmaskSelection(
                new int[]{8,6,5,9,4}, new int[]{4,3,2,5,2}, 9);
        int[][] tsp = {
                {0,10,15,20}, {10,0,35,25}, {15,35,0,30}, {20,25,30,0}
        };
        boolean[][] ham = {
                {false,true,true,false}, {true,false,true,true}, {true,true,false,true}, {false,true,true,false}
        };
        m.put("bitmaskDp", Map.of(
                "resourceSelection", Map.of("mask", selection.mask(), "totalValue", selection.totalValue(), "totalCost", selection.totalCost()),
                "tspMinimumTourCost", DynamicProgrammingAlgorithms.tspBitmask(tsp),
                "hamiltonianPathExists", DynamicProgrammingAlgorithms.hamiltonianPathExists(ham),
                "tspComplexity", "O(2^n · n²)"));

        int treeValue = DynamicProgrammingAlgorithms.treeMaxIndependentSet(
                new int[]{-1,0,0,1,1,2}, new int[]{5,4,6,3,2,7});
        m.put("dpOnTrees", Map.of(
                "problem", "Maximum-weight independent set on a small resource-category tree",
                "maximumWeight", treeValue));

        int[] sos = DynamicProgrammingAlgorithms.sumOverSubsets(new int[]{1,2,3,4,5,6,7,8}, 3);
        m.put("dpOnSubsetsSOS", Map.of(
                "input", List.of(1,2,3,4,5,6,7,8),
                "sumOverSubsets", ints(sos),
                "complexity", "O(n · 2^n)"));
        return m;
    }

    /** Backward-compatible endpoint used by fuzzy-search UI/tests, still labelled as a Module-3 feature. */
    public Map<String,Object> fuzzyDemo(String query) {
        Map<String,Object> m = advancedDpDemo(query);
        m.put("note", "Edit distance is a Module-3 application; the formal CO3 mapping is demonstrated by interval, bitmask, tree and subset DP in this same output.");
        return m;
    }

    /** CO4: Ford-Fulkerson, Edmonds-Karp, Dinic, max-flow/min-cut and bipartite assignment. */
    public Map<String,Object> flowDemo() {
        Map<String,Object> m = base("CO4", "Network Flow, Matching & Capacity Constraints");
        List<Resource> rs = library.page(1, 5, "", "", "title", "asc");
        int students = 4, items = Math.max(3, Math.min(5, rs.size()));
        int n = 2 + students + items, source = 0, sink = n - 1;
        int[][] cap = new int[n][n];
        for (int i = 0; i < students; i++) cap[source][1 + i] = 1;
        for (int j = 0; j < items; j++) cap[1 + students + j][sink] = 1;
        boolean[][] allowed = new boolean[students][items];
        for (int i = 0; i < students; i++) {
            for (int j = 0; j < items; j++) {
                allowed[i][j] = ((i + j) % 2 == 0 || j == i % items);
                if (allowed[i][j]) cap[1 + i][1 + students + j] = 1;
            }
        }

        FlowAlgorithms.FlowResult ff = FlowAlgorithms.maxFlowFordFulkerson(cap, source, sink);
        FlowAlgorithms.FlowResult ek = FlowAlgorithms.maxFlowEdmondsKarp(cap, source, sink);
        FlowAlgorithms.FlowResult dinic = FlowAlgorithms.maxFlowDinic(cap, source, sink);
        m.put("model", "Source → students → limited library resources → sink");
        m.put("students", students);
        m.put("resources", rs.stream().limit(items).map(r -> r.title() + " (" + r.id() + ")").toList());
        m.put("fordFulkersonMaxFlow", ff.maxFlow());
        m.put("edmondsKarpMaxFlow", ek.maxFlow());
        m.put("dinicMaxFlow", dinic.maxFlow());
        m.put("bipartiteMatchingSize", FlowAlgorithms.maximumBipartiteMatching(allowed));
        m.put("edmondsKarpComplexity", "O(VE²)");
        m.put("dinicHandoutNote", "Dinic uses level graphs and blocking flows; included as a full implementation although the handout requires intuition level.");
        List<Integer> cut = new ArrayList<>();
        for (int i = 0; i < ek.minCutReachable().length; i++) if (ek.minCutReachable()[i]) cut.add(i);
        m.put("minCutSourceSide", cut);
        return m;
    }

    /** CO5: classify NP-hard structure and demonstrate a provable Vertex-Cover 2-approximation. */
    public Map<String,Object> approximationDemo() {
        Map<String,Object> m = base("CO5", "NP-Completeness, Reductions & Approximation");
        m.put("complexityClasses", Map.of(
                "P", "Problems solvable in polynomial time",
                "NP", "Solutions verifiable in polynomial time",
                "NPComplete", "Problems in NP that are at least as hard as every NP problem",
                "NPHard", "At least as hard as NP-complete problems; may be optimisation problems"));
        m.put("canonicalReductionChain", List.of("3-SAT", "CLIQUE", "INDEPENDENT-SET", "VERTEX-COVER"));

        int vertices = 6;
        int[][] edges = {{0,1},{1,2},{2,3},{3,4},{4,5},{5,0},{1,4}};
        int[] approx = ApproximationAlgorithms.vertexCover2Approx(vertices, edges);
        int exact = ApproximationAlgorithms.exactMinimumVertexCoverSize(vertices, edges);
        m.put("vertexCoverDemo", Map.of(
                "vertices", vertices,
                "edges", edgesAsLists(edges),
                "approxCover", ints(approx),
                "isValidCover", ApproximationAlgorithms.isVertexCover(vertices, edges, approx),
                "approxSize", approx.length,
                "exactSmallInstanceSize", exact,
                "observedRatio", exact > 0 ? (double) approx.length / exact : 0.0,
                "provableGuarantee", "≤ 2 × OPT via maximal matching"));

        boolean[][] sets = {
                {true,true,false,false,false},
                {false,true,true,true,false},
                {false,false,false,true,true},
                {true,false,true,false,true}
        };
        int[] greedy = ApproximationAlgorithms.greedySetCover(sets, 5);
        int exactSet = ApproximationAlgorithms.exactMinimumSetCoverSize(sets, 5);
        m.put("additionalSetCoverDemo", Map.of(
                "greedyChosenSets", ints(greedy),
                "greedySize", greedy.length,
                "exactSmallInstanceSize", exactSet,
                "note", "Greedy Set Cover is an additional NP-hard optimisation demonstration; Vertex Cover 2-approx is the handout's worked approximation example."));
        return m;
    }

    /** CO6 combined demo: Las Vegas/Monte Carlo, hashing, sampling, prefix/reduce and work-span. */
    public Map<String,Object> co6Demo(String query) {
        String q = (query == null || query.isBlank()) ? "data" : query;
        Map<String,Object> m = base("CO6", "Randomised & Parallel Algorithms");
        List<Resource> rs = library.page(1, Math.min(30, library.size()), "", "", "year", "desc");
        int[] years = new int[rs.size()];
        for (int i = 0; i < years.length; i++) years[i] = rs.get(i).year();
        long s = System.nanoTime();
        RandomizedParallelAlgorithms.randomizedQuickSort(years, 2520030476L);
        long quickMicros = (System.nanoTime() - s) / 1000;

        int k = Math.min(8, library.size());
        int[] sample = RandomizedParallelAlgorithms.reservoirSample(library.size(), k, 2520030476L);
        List<String> titles = new ArrayList<>();
        List<Resource> snapshot = library.snapshot();
        for (int index : sample) titles.add(snapshot.get(index).title());

        m.put("randomized", Map.of(
                "lasVegasQuickSort", Map.of("sortedYears", ints(years), "elapsedMicros", quickMicros, "expectedTime", "O(n log n)"),
                "monteCarloMillerRabin", Map.of(
                        "2147483647ProbablePrime", RandomizedParallelAlgorithms.millerRabin(2_147_483_647L, 8, 42L),
                        "221ProbablePrime", RandomizedParallelAlgorithms.millerRabin(221L, 8, 42L),
                        "rounds", 8),
                "reservoirSampling", Map.of("k", k, "sampleTitles", titles, "complexity", "O(n) time / O(k) space"),
                "universalHashBucket", RandomizedParallelAlgorithms.universalHash("digital-library", 97, 42L)));

        String[] corpus = new String[snapshot.size()];
        int[] yearCorpus = new int[snapshot.size()];
        for (int i = 0; i < corpus.length; i++) {
            Resource r = snapshot.get(i);
            corpus[i] = r.title() + " " + r.keywords() + " " + r.subject();
            yearCorpus[i] = r.year();
        }
        long t1s = System.nanoTime();
        int sequentialMatches = RandomizedParallelAlgorithms.sequentialCountMatches(corpus, q);
        long t1 = System.nanoTime() - t1s;
        long tps = System.nanoTime();
        int parallelMatches = RandomizedParallelAlgorithms.parallelCountMatches(corpus, q);
        long tp = System.nanoTime() - tps;
        long yearSum = RandomizedParallelAlgorithms.parallelReduceSum(yearCorpus);
        int[] prefix = RandomizedParallelAlgorithms.parallelPrefixSum(new int[]{3,1,4,1,5,9,2,6});

        Map<String,Object> parallel = new LinkedHashMap<>();
        parallel.put("query", q);
        parallel.put("sequentialMatches", sequentialMatches);
        parallel.put("parallelMatches", parallelMatches);
        parallel.put("equivalent", sequentialMatches == parallelMatches);
        parallel.put("sequentialNanos", t1);
        parallel.put("parallelNanos", tp);
        parallel.put("measuredSpeedup", tp == 0 ? 0.0 : (double)t1 / tp);
        parallel.put("parallelReduceYearSum", yearSum);
        parallel.put("blellochExclusivePrefixInput", List.of(3,1,4,1,5,9,2,6));
        parallel.put("blellochExclusivePrefixOutput", ints(prefix));
        parallel.put("workSpan", "Tree-style reduce/prefix has O(n) work and O(log n) ideal span; real speedup depends on cores and overhead.");
        m.put("parallel", parallel);
        return m;
    }

    /** Backward-compatible CO6 randomized view. */
    public Map<String,Object> randomizedDemo() {
        Map<String,Object> all = co6Demo("data");
        Map<String,Object> m = base("CO6", "Randomised Algorithms");
        m.put("randomized", all.get("randomized"));
        return m;
    }

    /** Backward-compatible CO6 parallel view. */
    @SuppressWarnings("unchecked")
    public Map<String,Object> parallelDemo(String query) {
        Map<String,Object> all = co6Demo(query);
        Map<String,Object> p = (Map<String,Object>) all.get("parallel");
        Map<String,Object> m = base("CO6", "Parallel Algorithms");
        m.putAll(p);
        return m;
    }

    public Map<String,Object> analytics(String q) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("stats", library.stats());
        m.put("co2", stringDemo(q));
        Map<String,Object> co6 = co6Demo(q);
        m.put("co6", co6);
        m.put("co6Parallel", parallelDemo(q)); // compatibility with existing analytics widgets
        return m;
    }

    private interface IntCall { int run(); }
    private Map<String,Object> runString(String name, String complexity, IntCall call) {
        long s = System.nanoTime();
        int index = call.run();
        long us = (System.nanoTime() - s) / 1000;
        return Map.of("algorithm", name, "complexity", complexity, "index", index, "elapsedMicros", us);
    }

    private Map<String,Object> base(String co, String title) {
        Map<String,Object> m = new LinkedHashMap<>();
        m.put("co", co);
        m.put("title", title);
        m.put("course", "25CS2103E Data Structures and Algorithms - 3");
        m.put("datasetSize", library.size());
        return m;
    }

    private static List<Integer> preview(int[] a, int n) {
        List<Integer> out = new ArrayList<>();
        for (int i = 0; i < Math.min(n, a.length); i++) out.add(a[i]);
        return out;
    }

    private static List<Integer> ints(int[] a) {
        List<Integer> out = new ArrayList<>();
        for (int v : a) out.add(v);
        return out;
    }

    private static List<List<Integer>> edgesAsLists(int[][] edges) {
        List<List<Integer>> out = new ArrayList<>();
        for (int[] e : edges) out.add(List.of(e[0], e[1]));
        return out;
    }
}
