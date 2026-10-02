package digitallibrary.algorithms;

import digitallibrary.structures.ArrayQueue;

/**
 * CO2 - String Algorithms from the DSA-3 handout.
 * All core matching logic is implemented manually with arrays; no library search helpers are used.
 */
public final class StringAlgorithms {
    private StringAlgorithms() {}

    public static int naiveIndexOf(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        for (int i = 0; i + pattern.length() <= text.length(); i++) {
            int j = 0;
            while (j < pattern.length() && text.charAt(i + j) == pattern.charAt(j)) j++;
            if (j == pattern.length()) return i;
        }
        return -1;
    }

    public static int kmpIndexOf(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        int[] lps = lps(pattern);
        for (int i = 0, j = 0; i < text.length();) {
            if (text.charAt(i) == pattern.charAt(j)) {
                i++; j++;
                if (j == pattern.length()) return i - j;
            } else if (j > 0) {
                j = lps[j - 1];
            } else {
                i++;
            }
        }
        return -1;
    }

    public static int[] lps(String pattern) {
        int[] lps = new int[pattern.length()];
        for (int i = 1, len = 0; i < pattern.length();) {
            if (pattern.charAt(i) == pattern.charAt(len)) lps[i++] = ++len;
            else if (len > 0) len = lps[len - 1];
            else lps[i++] = 0;
        }
        return lps;
    }

    public static int zIndexOf(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        String joined = pattern + '\u0001' + text;
        int[] z = zArray(joined);
        for (int i = pattern.length() + 1; i < joined.length(); i++) {
            if (z[i] >= pattern.length()) return i - pattern.length() - 1;
        }
        return -1;
    }

    public static int[] zArray(String s) {
        int[] z = new int[s.length()];
        for (int i = 1, l = 0, r = 0; i < s.length(); i++) {
            if (i <= r) z[i] = Math.min(r - i + 1, z[i - l]);
            while (i + z[i] < s.length() && s.charAt(z[i]) == s.charAt(i + z[i])) z[i]++;
            if (i + z[i] - 1 > r) { l = i; r = i + z[i] - 1; }
        }
        return z;
    }

    public static int rabinKarpIndexOf(String text, String pattern) {
        if (pattern.isEmpty()) return 0;
        if (pattern.length() > text.length()) return -1;
        final long mod1 = 1_000_000_007L, mod2 = 1_000_000_009L, base = 911382323L;
        long p1 = 0, p2 = 0, t1 = 0, t2 = 0, pow1 = 1, pow2 = 1;
        for (int i = 0; i < pattern.length(); i++) {
            p1 = (p1 * base + pattern.charAt(i)) % mod1;
            p2 = (p2 * base + pattern.charAt(i)) % mod2;
            t1 = (t1 * base + text.charAt(i)) % mod1;
            t2 = (t2 * base + text.charAt(i)) % mod2;
            if (i < pattern.length() - 1) {
                pow1 = (pow1 * base) % mod1;
                pow2 = (pow2 * base) % mod2;
            }
        }
        for (int i = 0; i + pattern.length() <= text.length(); i++) {
            if (p1 == t1 && p2 == t2 && regionEquals(text, i, pattern)) return i;
            if (i + pattern.length() < text.length()) {
                t1 = (t1 - (text.charAt(i) * pow1) % mod1 + mod1) % mod1;
                t1 = (t1 * base + text.charAt(i + pattern.length())) % mod1;
                t2 = (t2 - (text.charAt(i) * pow2) % mod2 + mod2) % mod2;
                t2 = (t2 * base + text.charAt(i + pattern.length())) % mod2;
            }
        }
        return -1;
    }

    /**
     * Aho-Corasick multi-pattern matching using a hand-built ASCII trie and failure links.
     * Returns how many pattern endings are encountered while scanning the text.
     */
    public static int ahoCorasickMatchCount(String text, String[] patterns) {
        int maxNodes = 1;
        for (String p : patterns) maxNodes += p.length();
        int[][] next = new int[maxNodes][128];
        int[] fail = new int[maxNodes];
        int[] out = new int[maxNodes];
        int nodes = 1;

        for (String raw : patterns) {
            String p = raw.toLowerCase();
            int state = 0;
            for (int i = 0; i < p.length(); i++) {
                int c = ascii(p.charAt(i));
                if (next[state][c] == 0) next[state][c] = nodes++;
                state = next[state][c];
            }
            out[state]++;
        }

        ArrayQueue<Integer> q = new ArrayQueue<>();
        for (int c = 0; c < 128; c++) {
            int child = next[0][c];
            if (child != 0) { fail[child] = 0; q.offer(child); }
        }
        while (!q.isEmpty()) {
            int v = q.poll();
            for (int c = 0; c < 128; c++) {
                int u = next[v][c];
                if (u != 0) {
                    int f = fail[v];
                    while (f != 0 && next[f][c] == 0) f = fail[f];
                    if (next[f][c] != 0) f = next[f][c];
                    fail[u] = f;
                    out[u] += out[f];
                    q.offer(u);
                }
            }
        }

        int state = 0, matches = 0;
        String t = text.toLowerCase();
        for (int i = 0; i < t.length(); i++) {
            int c = ascii(t.charAt(i));
            while (state != 0 && next[state][c] == 0) state = fail[state];
            if (next[state][c] != 0) state = next[state][c];
            matches += out[state];
        }
        return matches;
    }

    private static int ascii(char c) { return c < 128 ? c : '?'; }

    private static boolean regionEquals(String text, int at, String pattern) {
        for (int j = 0; j < pattern.length(); j++) if (text.charAt(at + j) != pattern.charAt(j)) return false;
        return true;
    }
}
