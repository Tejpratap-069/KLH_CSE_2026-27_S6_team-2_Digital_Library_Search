package digitallibrary.algorithms;

import digitallibrary.structures.ArrayQueue;

/** CO4 - Flow networks, Ford-Fulkerson, Edmonds-Karp, Dinic, min-cut and matching. */
public final class FlowAlgorithms {
    public record FlowResult(int maxFlow, int[][] residual, boolean[] minCutReachable) {}
    private FlowAlgorithms() {}

    /** Ford-Fulkerson using DFS augmenting paths. */
    public static FlowResult maxFlowFordFulkerson(int[][] capacity, int source, int sink) {
        int[][] residual = copy(capacity);
        int flow = 0;
        while (true) {
            boolean[] seen = new boolean[residual.length];
            int pushed = dfsAugment(residual, source, sink, Integer.MAX_VALUE, seen);
            if (pushed == 0) break;
            flow += pushed;
        }
        return new FlowResult(flow, residual, reachable(residual, source));
    }

    /** Edmonds-Karp = Ford-Fulkerson with BFS shortest augmenting paths: O(VE^2). */
    public static FlowResult maxFlowEdmondsKarp(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residual = copy(capacity);
        int flow = 0;
        int[] parent = new int[n];
        while (bfs(residual, source, sink, parent)) {
            int augment = Integer.MAX_VALUE;
            for (int v = sink; v != source; v = parent[v]) augment = Math.min(augment, residual[parent[v]][v]);
            for (int v = sink; v != source; v = parent[v]) {
                int u = parent[v];
                residual[u][v] -= augment;
                residual[v][u] += augment;
            }
            flow += augment;
        }
        return new FlowResult(flow, residual, reachable(residual, source));
    }

    /** Dinic with level graph + blocking flow. */
    public static FlowResult maxFlowDinic(int[][] capacity, int source, int sink) {
        int n = capacity.length;
        int[][] residual = copy(capacity);
        int[] level = new int[n];
        int flow = 0;
        while (buildLevelGraph(residual, source, sink, level)) {
            int[] next = new int[n];
            while (true) {
                int pushed = dinicDfs(residual, source, sink, Integer.MAX_VALUE, level, next);
                if (pushed == 0) break;
                flow += pushed;
            }
        }
        return new FlowResult(flow, residual, reachable(residual, source));
    }

    /** Bipartite matching reduced to max-flow. matrix[left][right] indicates an allowed assignment. */
    public static int maximumBipartiteMatching(boolean[][] matrix) {
        int left = matrix.length;
        int right = left == 0 ? 0 : matrix[0].length;
        int n = 2 + left + right, source = 0, sink = n - 1;
        int[][] cap = new int[n][n];
        for (int i = 0; i < left; i++) cap[source][1 + i] = 1;
        for (int j = 0; j < right; j++) cap[1 + left + j][sink] = 1;
        for (int i = 0; i < left; i++) for (int j = 0; j < right; j++) if (matrix[i][j]) cap[1 + i][1 + left + j] = 1;
        return maxFlowEdmondsKarp(cap, source, sink).maxFlow();
    }

    private static int dfsAugment(int[][] residual, int u, int sink, int flow, boolean[] seen) {
        if (u == sink) return flow;
        seen[u] = true;
        for (int v = 0; v < residual.length; v++) {
            if (!seen[v] && residual[u][v] > 0) {
                int pushed = dfsAugment(residual, v, sink, Math.min(flow, residual[u][v]), seen);
                if (pushed > 0) {
                    residual[u][v] -= pushed;
                    residual[v][u] += pushed;
                    return pushed;
                }
            }
        }
        return 0;
    }

    private static boolean bfs(int[][] residual, int source, int sink, int[] parent) {
        for (int i = 0; i < parent.length; i++) parent[i] = -1;
        boolean[] seen = new boolean[residual.length];
        ArrayQueue<Integer> q = new ArrayQueue<>();
        q.offer(source); seen[source] = true;
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v = 0; v < residual.length; v++) if (!seen[v] && residual[u][v] > 0) {
                seen[v] = true; parent[v] = u;
                if (v == sink) return true;
                q.offer(v);
            }
        }
        return false;
    }

    private static boolean buildLevelGraph(int[][] residual, int source, int sink, int[] level) {
        for (int i = 0; i < level.length; i++) level[i] = -1;
        ArrayQueue<Integer> q = new ArrayQueue<>();
        level[source] = 0; q.offer(source);
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v = 0; v < residual.length; v++) if (level[v] < 0 && residual[u][v] > 0) {
                level[v] = level[u] + 1;
                q.offer(v);
            }
        }
        return level[sink] >= 0;
    }

    private static int dinicDfs(int[][] residual, int u, int sink, int pushed, int[] level, int[] next) {
        if (u == sink) return pushed;
        for (int v = next[u]; v < residual.length; v++, next[u]++) {
            if (level[v] == level[u] + 1 && residual[u][v] > 0) {
                int send = dinicDfs(residual, v, sink, Math.min(pushed, residual[u][v]), level, next);
                if (send > 0) {
                    residual[u][v] -= send;
                    residual[v][u] += send;
                    return send;
                }
            }
        }
        return 0;
    }

    private static boolean[] reachable(int[][] residual, int source) {
        boolean[] seen = new boolean[residual.length];
        ArrayQueue<Integer> q = new ArrayQueue<>();
        q.offer(source); seen[source] = true;
        while (!q.isEmpty()) {
            int u = q.poll();
            for (int v = 0; v < residual.length; v++) if (!seen[v] && residual[u][v] > 0) {
                seen[v] = true; q.offer(v);
            }
        }
        return seen;
    }

    private static int[][] copy(int[][] a) {
        int[][] b = new int[a.length][a.length];
        for (int i = 0; i < a.length; i++) System.arraycopy(a[i], 0, b[i], 0, a[i].length);
        return b;
    }
}
