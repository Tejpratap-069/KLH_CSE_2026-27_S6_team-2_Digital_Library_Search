package digitallibrary.algorithms;

import digitallibrary.structures.DynamicArray;

/** CO5 - NP-completeness / approximation demonstrations used by the project. */
public final class ApproximationAlgorithms {
    private ApproximationAlgorithms() {}

    /** Greedy Set Cover; useful as an additional NP-hard optimization demonstration. */
    public static int[] greedySetCover(boolean[][] sets, int universe) {
        boolean[] covered = new boolean[universe];
        boolean[] used = new boolean[sets.length];
        DynamicArray<Integer> chosen = new DynamicArray<>();
        int left = universe;
        while (left > 0) {
            int best = -1, gain = 0;
            for (int i = 0; i < sets.length; i++) if (!used[i]) {
                int g = 0;
                for (int j = 0; j < universe; j++) if (sets[i][j] && !covered[j]) g++;
                if (g > gain) { gain = g; best = i; }
            }
            if (best < 0 || gain == 0) break;
            used[best] = true;
            chosen.add(best);
            for (int j = 0; j < universe; j++) if (sets[best][j] && !covered[j]) { covered[j] = true; left--; }
        }
        int[] out = new int[chosen.size()];
        for (int i = 0; i < out.length; i++) out[i] = chosen.get(i);
        return out;
    }

    /** Vertex Cover 2-approximation via maximal matching, exactly as highlighted in the handout. */
    public static int[] vertexCover2Approx(int vertices, int[][] edges) {
        boolean[] chosen = new boolean[vertices];
        boolean[] matchedVertex = new boolean[vertices];
        for (int[] edge : edges) {
            int u = edge[0], v = edge[1];
            if (!matchedVertex[u] && !matchedVertex[v]) {
                matchedVertex[u] = matchedVertex[v] = true;
                chosen[u] = chosen[v] = true;
            }
        }
        int count = 0;
        for (boolean b : chosen) if (b) count++;
        int[] out = new int[count];
        for (int i = 0, k = 0; i < vertices; i++) if (chosen[i]) out[k++] = i;
        return out;
    }

    public static boolean isVertexCover(int vertices, int[][] edges, int[] cover) {
        boolean[] in = new boolean[vertices];
        for (int v : cover) if (v >= 0 && v < vertices) in[v] = true;
        for (int[] e : edges) if (!in[e[0]] && !in[e[1]]) return false;
        return true;
    }

    /** Exact baseline for tiny graphs only, used to report the observed approximation ratio. */
    public static int exactMinimumVertexCoverSize(int vertices, int[][] edges) {
        if (vertices > 24) return -1;
        int best = vertices + 1;
        int total = 1 << vertices;
        for (int mask = 0; mask < total; mask++) {
            int count = Integer.bitCount(mask);
            if (count >= best) continue;
            boolean ok = true;
            for (int[] e : edges) {
                if ((mask & (1 << e[0])) == 0 && (mask & (1 << e[1])) == 0) { ok = false; break; }
            }
            if (ok) best = count;
        }
        return best == vertices + 1 ? -1 : best;
    }

    public static int exactMinimumSetCoverSize(boolean[][] sets, int universe) {
        if (sets.length > 24) return -1;
        int best = Integer.MAX_VALUE;
        for (int mask = 0; mask < (1 << sets.length); mask++) {
            int count = Integer.bitCount(mask);
            if (count >= best) continue;
            boolean[] covered = new boolean[universe];
            for (int i = 0; i < sets.length; i++) if ((mask & (1 << i)) != 0) {
                for (int j = 0; j < universe; j++) if (sets[i][j]) covered[j] = true;
            }
            boolean ok = true;
            for (boolean b : covered) if (!b) { ok = false; break; }
            if (ok) best = count;
        }
        return best == Integer.MAX_VALUE ? -1 : best;
    }
}
