package digitallibrary.algorithms;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

/** CO6 - randomized algorithms and parallel primitives. */
public final class RandomizedParallelAlgorithms {
    private RandomizedParallelAlgorithms() {}

    /** Small hand-built xorshift PRNG so the algorithm engine does not depend on java.util.Random. */
    private static final class XorShift64 {
        private long x;
        XorShift64(long seed) { x = seed == 0 ? 0x9E3779B97F4A7C15L : seed; }
        long nextLong() { x ^= x << 13; x ^= x >>> 7; x ^= x << 17; return x; }
        int nextInt(int bound) {
            if (bound <= 0) throw new IllegalArgumentException();
            long v = nextLong() & Long.MAX_VALUE;
            return (int)(v % bound);
        }
    }

    /** Las-Vegas-style randomized QuickSort: result is always sorted; runtime depends on random pivots. */
    public static void randomizedQuickSort(int[] a, long seed) {
        quick(a, 0, a.length - 1, new XorShift64(seed));
    }

    private static void quick(int[] a, int lo, int hi, XorShift64 rng) {
        if (lo >= hi) return;
        int pivotIndex = lo + rng.nextInt(hi - lo + 1);
        swap(a, pivotIndex, hi);
        int p = lo;
        for (int i = lo; i < hi; i++) if (a[i] <= a[hi]) swap(a, i, p++);
        swap(a, p, hi);
        quick(a, lo, p - 1, rng);
        quick(a, p + 1, hi, rng);
    }

    /** Reservoir sampling: O(n) time, O(k) memory, uniform sample from a stream. */
    public static int[] reservoirSample(int n, int k, long seed) {
        if (k < 0 || k > n) throw new IllegalArgumentException();
        int[] reservoir = new int[k];
        for (int i = 0; i < k; i++) reservoir[i] = i;
        XorShift64 rng = new XorShift64(seed);
        for (int i = k; i < n; i++) {
            int j = rng.nextInt(i + 1);
            if (j < k) reservoir[j] = i;
        }
        return reservoir;
    }

    /** Universal/randomized hash family demonstration. */
    public static int universalHash(String s, int buckets, long seed) {
        if (buckets <= 0) throw new IllegalArgumentException();
        XorShift64 rng = new XorShift64(seed);
        long prime = 1_000_003L;
        long a = 1 + rng.nextInt(1_000_002);
        long b = rng.nextInt(1_000_003);
        long h = 0;
        for (int i = 0; i < s.length(); i++) h = (h * 257 + s.charAt(i)) % prime;
        return (int)(((a * h + b) % prime) % buckets);
    }

    /** Monte-Carlo Miller-Rabin primality test for positive 32-bit-scale inputs. */
    public static boolean millerRabin(long n, int rounds, long seed) {
        if (n < 2) return false;
        if (n == 2 || n == 3) return true;
        if ((n & 1L) == 0) return false;
        long d = n - 1;
        int s = 0;
        while ((d & 1L) == 0) { d >>= 1; s++; }
        XorShift64 rng = new XorShift64(seed);
        for (int r = 0; r < rounds; r++) {
            long a = 2 + (rng.nextLong() & Long.MAX_VALUE) % (n - 3);
            long x = modPow(a, d, n);
            if (x == 1 || x == n - 1) continue;
            boolean witness = true;
            for (int i = 1; i < s; i++) {
                x = (x * x) % n;
                if (x == n - 1) { witness = false; break; }
            }
            if (witness) return false;
        }
        return true;
    }

    private static long modPow(long base, long exp, long mod) {
        long result = 1 % mod;
        long x = base % mod;
        while (exp > 0) {
            if ((exp & 1L) != 0) result = (result * x) % mod;
            x = (x * x) % mod;
            exp >>= 1;
        }
        return result;
    }

    public static int sequentialCountMatches(String[] values, String query) {
        String needle = query.toLowerCase();
        int count = 0;
        for (String s : values) if (s.toLowerCase().contains(needle)) count++;
        return count;
    }

    public static int parallelCountMatches(String[] values, String query) {
        if (values.length < 128) return sequentialCountMatches(values, query);
        return ForkJoinPool.commonPool().invoke(new CountTask(values, query.toLowerCase(), 0, values.length));
    }

    /** Parallel reduce primitive: sum all values. */
    public static long parallelReduceSum(int[] values) {
        if (values.length == 0) return 0;
        return ForkJoinPool.commonPool().invoke(new SumTask(values, 0, values.length));
    }

    /**
     * Blelloch exclusive prefix sum. The up-sweep and down-sweep operations at each level
     * are executed as ForkJoin range tasks, preserving the classic work/span structure.
     */
    public static int[] parallelPrefixSum(int[] input) {
        int n = 1;
        while (n < input.length) n <<= 1;
        int[] a = new int[n];
        System.arraycopy(input, 0, a, 0, input.length);
        ForkJoinPool pool = ForkJoinPool.commonPool();

        for (int d = 1; d < n; d <<= 1) {
            int step = d << 1;
            pool.invoke(new LevelTask(a, step, d, true, 0, n / step));
        }
        a[n - 1] = 0;
        for (int d = n >> 1; d >= 1; d >>= 1) {
            int step = d << 1;
            pool.invoke(new LevelTask(a, step, d, false, 0, n / step));
        }
        int[] out = new int[input.length];
        System.arraycopy(a, 0, out, 0, input.length);
        return out;
    }

    private static final class CountTask extends RecursiveTask<Integer> {
        final String[] values; final String query; final int lo, hi;
        CountTask(String[] values, String query, int lo, int hi) { this.values = values; this.query = query; this.lo = lo; this.hi = hi; }
        @Override protected Integer compute() {
            if (hi - lo <= 512) {
                int c = 0;
                for (int i = lo; i < hi; i++) if (values[i].toLowerCase().contains(query)) c++;
                return c;
            }
            int mid = (lo + hi) >>> 1;
            CountTask left = new CountTask(values, query, lo, mid);
            CountTask right = new CountTask(values, query, mid, hi);
            left.fork();
            int r = right.compute();
            return left.join() + r;
        }
    }

    private static final class SumTask extends RecursiveTask<Long> {
        final int[] values; final int lo, hi;
        SumTask(int[] values, int lo, int hi) { this.values = values; this.lo = lo; this.hi = hi; }
        @Override protected Long compute() {
            if (hi - lo <= 1024) {
                long sum = 0;
                for (int i = lo; i < hi; i++) sum += values[i];
                return sum;
            }
            int mid = (lo + hi) >>> 1;
            SumTask left = new SumTask(values, lo, mid);
            SumTask right = new SumTask(values, mid, hi);
            left.fork();
            long r = right.compute();
            return left.join() + r;
        }
    }

    private static final class LevelTask extends RecursiveAction {
        final int[] a; final int step, d; final boolean upsweep; final int loBlock, hiBlock;
        LevelTask(int[] a, int step, int d, boolean upsweep, int loBlock, int hiBlock) {
            this.a = a; this.step = step; this.d = d; this.upsweep = upsweep; this.loBlock = loBlock; this.hiBlock = hiBlock;
        }
        @Override protected void compute() {
            if (hiBlock - loBlock <= 64) {
                for (int block = loBlock; block < hiBlock; block++) {
                    int start = block * step;
                    int left = start + d - 1;
                    int right = start + step - 1;
                    if (upsweep) a[right] += a[left];
                    else {
                        int t = a[left];
                        a[left] = a[right];
                        a[right] += t;
                    }
                }
                return;
            }
            int mid = (loBlock + hiBlock) >>> 1;
            invokeAll(new LevelTask(a, step, d, upsweep, loBlock, mid), new LevelTask(a, step, d, upsweep, mid, hiBlock));
        }
    }

    private static void swap(int[] a, int i, int j) { int t = a[i]; a[i] = a[j]; a[j] = t; }
}
