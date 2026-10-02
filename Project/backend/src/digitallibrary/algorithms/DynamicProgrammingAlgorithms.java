package digitallibrary.algorithms;

/**
 * CO3 - Advanced Dynamic Programming.
 * Covers the handout topics used by the project: edit distance, sequence alignment,
 * interval DP, bitmask DP, DP on trees, and DP on subsets/SOS DP.
 */
public final class DynamicProgrammingAlgorithms {
    public record Selection(int mask, int totalValue, int totalCost) {}
    private DynamicProgrammingAlgorithms() {}

    /** Wagner-Fischer Levenshtein distance. */
    public static int levenshtein(String a, String b) {
        int[][] d = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) d[i][0] = i;
        for (int j = 0; j <= b.length(); j++) d[0][j] = j;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                d[i][j] = min3(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
            }
        }
        return d[a.length()][b.length()];
    }

    public static int damerauLevenshtein(String a, String b) {
        int[][] d = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) d[i][0] = i;
        for (int j = 0; j <= b.length(); j++) d[0][j] = j;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                int v = min3(d[i - 1][j] + 1, d[i][j - 1] + 1, d[i - 1][j - 1] + cost);
                if (i > 1 && j > 1 && a.charAt(i - 1) == b.charAt(j - 2) && a.charAt(i - 2) == b.charAt(j - 1)) {
                    v = Math.min(v, d[i - 2][j - 2] + 1);
                }
                d[i][j] = v;
            }
        }
        return d[a.length()][b.length()];
    }

    public static int weightedEditDistance(String a, String b, int insertCost, int deleteCost, int substituteCost) {
        int[][] d = new int[a.length() + 1][b.length() + 1];
        for (int i = 0; i <= a.length(); i++) d[i][0] = i * deleteCost;
        for (int j = 0; j <= b.length(); j++) d[0][j] = j * insertCost;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int sub = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : substituteCost;
                d[i][j] = min3(d[i - 1][j] + deleteCost, d[i][j - 1] + insertCost, d[i - 1][j - 1] + sub);
            }
        }
        return d[a.length()][b.length()];
    }

    /** Needleman-Wunsch global alignment score. */
    public static int needlemanWunschScore(String a, String b, int match, int mismatch, int gap) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        for (int i = 1; i <= a.length(); i++) dp[i][0] = dp[i - 1][0] + gap;
        for (int j = 1; j <= b.length(); j++) dp[0][j] = dp[0][j - 1] + gap;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int diag = dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? match : mismatch);
                int up = dp[i - 1][j] + gap;
                int left = dp[i][j - 1] + gap;
                dp[i][j] = Math.max(diag, Math.max(up, left));
            }
        }
        return dp[a.length()][b.length()];
    }

    /** Smith-Waterman local alignment score. */
    public static int smithWatermanScore(String a, String b, int match, int mismatch, int gap) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];
        int best = 0;
        for (int i = 1; i <= a.length(); i++) {
            for (int j = 1; j <= b.length(); j++) {
                int diag = dp[i - 1][j - 1] + (a.charAt(i - 1) == b.charAt(j - 1) ? match : mismatch);
                int up = dp[i - 1][j] + gap;
                int left = dp[i][j - 1] + gap;
                dp[i][j] = Math.max(0, Math.max(diag, Math.max(up, left)));
                if (dp[i][j] > best) best = dp[i][j];
            }
        }
        return best;
    }

    /** Interval DP - Matrix Chain Multiplication. */
    public static int matrixChainCost(int[] p) {
        int n = p.length - 1;
        if (n <= 0) return 0;
        int[][] dp = new int[n][n];
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    long cost = (long) dp[i][k] + dp[k + 1][j] + (long) p[i] * p[k + 1] * p[j + 1];
                    if (cost < dp[i][j]) dp[i][j] = (int) cost;
                }
            }
        }
        return dp[0][n - 1];
    }

    /** Interval DP - Optimal BST expected search cost for successful keys only. */
    public static int optimalBstCost(int[] frequencies) {
        int n = frequencies.length;
        if (n == 0) return 0;
        int[][] dp = new int[n][n];
        int[] prefix = new int[n + 1];
        for (int i = 0; i < n; i++) prefix[i + 1] = prefix[i] + frequencies[i];
        for (int i = 0; i < n; i++) dp[i][i] = frequencies[i];
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;
                int sum = prefix[j + 1] - prefix[i];
                int best = Integer.MAX_VALUE;
                for (int root = i; root <= j; root++) {
                    int left = root > i ? dp[i][root - 1] : 0;
                    int right = root < j ? dp[root + 1][j] : 0;
                    best = Math.min(best, left + right + sum);
                }
                dp[i][j] = best;
            }
        }
        return dp[0][n - 1];
    }

    /** Bitmask DP subset/resource selection under a small budget. */
    public static Selection bitmaskSelection(int[] values, int[] costs, int budget) {
        if (values.length != costs.length || values.length > 24) throw new IllegalArgumentException("bitmask demo supports <=24 items");
        int bestValue = -1, bestCost = 0, bestMask = 0;
        int total = 1 << values.length;
        for (int mask = 0; mask < total; mask++) {
            int value = 0, cost = 0;
            for (int i = 0; i < values.length; i++) if ((mask & (1 << i)) != 0) { value += values[i]; cost += costs[i]; }
            if (cost <= budget && (value > bestValue || (value == bestValue && cost < bestCost))) {
                bestValue = value; bestCost = cost; bestMask = mask;
            }
        }
        return new Selection(bestMask, bestValue, bestCost);
    }

    /** Bitmask DP - Travelling Salesperson Problem, start/end at city 0. */
    public static int tspBitmask(int[][] distance) {
        int n = distance.length;
        if (n == 0) return 0;
        if (n > 18) throw new IllegalArgumentException("TSP demo supports <=18 cities");
        int states = 1 << n, inf = 1_000_000_000;
        int[][] dp = new int[states][n];
        for (int mask = 0; mask < states; mask++) for (int i = 0; i < n; i++) dp[mask][i] = inf;
        dp[1][0] = 0;
        for (int mask = 1; mask < states; mask++) {
            if ((mask & 1) == 0) continue;
            for (int u = 0; u < n; u++) if ((mask & (1 << u)) != 0 && dp[mask][u] < inf) {
                for (int v = 0; v < n; v++) if ((mask & (1 << v)) == 0) {
                    int next = mask | (1 << v);
                    int candidate = dp[mask][u] + distance[u][v];
                    if (candidate < dp[next][v]) dp[next][v] = candidate;
                }
            }
        }
        int answer = inf, full = states - 1;
        for (int u = 1; u < n; u++) answer = Math.min(answer, dp[full][u] + distance[u][0]);
        return answer;
    }

    /** Bitmask DP - Hamiltonian Path existence. */
    public static boolean hamiltonianPathExists(boolean[][] graph) {
        int n = graph.length;
        if (n == 0) return true;
        if (n > 22) throw new IllegalArgumentException("Hamiltonian demo supports <=22 vertices");
        boolean[][] dp = new boolean[1 << n][n];
        for (int v = 0; v < n; v++) dp[1 << v][v] = true;
        for (int mask = 1; mask < (1 << n); mask++) {
            for (int end = 0; end < n; end++) if (dp[mask][end]) {
                for (int next = 0; next < n; next++) if ((mask & (1 << next)) == 0 && graph[end][next]) {
                    dp[mask | (1 << next)][next] = true;
                }
            }
        }
        int full = (1 << n) - 1;
        for (int v = 0; v < n; v++) if (dp[full][v]) return true;
        return false;
    }

    /** DP on trees - maximum-weight independent set. parent[0] should be -1. */
    public static int treeMaxIndependentSet(int[] parent, int[] weight) {
        if (parent.length != weight.length) throw new IllegalArgumentException();
        int n = parent.length;
        int[] include = new int[n], exclude = new int[n];
        for (int i = n - 1; i >= 0; i--) {
            include[i] += weight[i];
            int p = parent[i];
            if (p >= 0) {
                include[p] += exclude[i];
                exclude[p] += Math.max(include[i], exclude[i]);
            }
        }
        return n == 0 ? 0 : Math.max(include[0], exclude[0]);
    }

    /** SOS DP: result[mask] = sum of values[submask] over all submasks. */
    public static int[] sumOverSubsets(int[] values, int bits) {
        int size = 1 << bits;
        if (values.length != size) throw new IllegalArgumentException("values length must be 2^bits");
        int[] dp = values.clone();
        for (int bit = 0; bit < bits; bit++) {
            for (int mask = 0; mask < size; mask++) {
                if ((mask & (1 << bit)) != 0) dp[mask] += dp[mask ^ (1 << bit)];
            }
        }
        return dp;
    }

    private static int min3(int a, int b, int c) { return Math.min(a, Math.min(b, c)); }
}
