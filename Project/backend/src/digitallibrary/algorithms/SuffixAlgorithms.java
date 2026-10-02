package digitallibrary.algorithms;

/** CO2 suffix structures: suffix array plus Kasai LCP. */
public final class SuffixAlgorithms {
    private SuffixAlgorithms() {}

    /** Practical doubling construction with a hand-built merge sort: O(n log^2 n). */
    public static int[] buildSuffixArray(String s) {
        int n = s.length();
        if (n == 0) return new int[0];
        int[] sa = new int[n], rank = new int[n], tempRank = new int[n], work = new int[n];
        for (int i = 0; i < n; i++) { sa[i] = i; rank[i] = s.charAt(i); }
        for (int k = 1; k < n; k <<= 1) {
            mergeSort(sa, work, 0, n, rank, k, n);
            tempRank[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                tempRank[sa[i]] = tempRank[sa[i - 1]] + (compare(sa[i - 1], sa[i], rank, k, n) < 0 ? 1 : 0);
            }
            System.arraycopy(tempRank, 0, rank, 0, n);
            if (rank[sa[n - 1]] == n - 1) break;
        }
        return sa;
    }

    /** Kasai algorithm: O(n) after suffix array construction. */
    public static int[] buildLcp(String s, int[] sa) {
        int n = s.length();
        if (n <= 1) return new int[0];
        int[] rank = new int[n];
        for (int i = 0; i < n; i++) rank[sa[i]] = i;
        int[] lcp = new int[n - 1];
        for (int i = 0, h = 0; i < n; i++) {
            int r = rank[i];
            if (r == n - 1) { h = 0; continue; }
            int j = sa[r + 1];
            while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) h++;
            lcp[r] = h;
            if (h > 0) h--;
        }
        return lcp;
    }

    public static int longestRepeatedSubstringLength(String s) {
        int[] sa = buildSuffixArray(s);
        int[] lcp = buildLcp(s, sa);
        int best = 0;
        for (int v : lcp) if (v > best) best = v;
        return best;
    }

    private static void mergeSort(int[] sa, int[] work, int lo, int hi, int[] rank, int k, int n) {
        if (hi - lo <= 1) return;
        int mid = (lo + hi) >>> 1;
        mergeSort(sa, work, lo, mid, rank, k, n);
        mergeSort(sa, work, mid, hi, rank, k, n);
        int i = lo, j = mid, p = lo;
        while (i < mid || j < hi) {
            if (j >= hi || (i < mid && compare(sa[i], sa[j], rank, k, n) <= 0)) work[p++] = sa[i++];
            else work[p++] = sa[j++];
        }
        for (i = lo; i < hi; i++) sa[i] = work[i];
    }

    private static int compare(int a, int b, int[] rank, int k, int n) {
        if (rank[a] != rank[b]) return Integer.compare(rank[a], rank[b]);
        int ra = a + k < n ? rank[a + k] : -1;
        int rb = b + k < n ? rank[b + k] : -1;
        return Integer.compare(ra, rb);
    }
}
