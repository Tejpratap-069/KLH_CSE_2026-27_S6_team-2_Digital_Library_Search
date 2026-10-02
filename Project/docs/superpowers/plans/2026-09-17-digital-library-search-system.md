# Digital Library Search System Implementation Plan

**Course:** Data Structures and Algorithms - 3 (25CS2103E)

**Goal:** Build a complete Digital Library Search System with Java 17, HTML/CSS/JavaScript, text-file-only persistence, a deterministic 50,000-record repository, and CO1–CO6 demonstrations aligned with the official DSA-3 handout.

## Completed architecture

- Java 17 backend using the JDK HttpServer
- HTML5/CSS3/Vanilla JavaScript frontend
- Structured UTF-8 text-file persistence
- Custom DynamicArray, LinkedList, Queue, Stack and StringHashTable
- Dataset generator for 10K/25K/50K/100K records
- Search, resource details, analytics and Admin CRUD
- Algorithm Lab mapped to CO1 through CO6

## CO implementation plan

### CO1
Problem classification and algorithm selection based on query/problem signature.

### CO2
Naive Pattern Matching, KMP, Z-Function, Rabin-Karp, Aho-Corasick, Suffix Array and Kasai LCP.

### CO3
Edit-distance and sequence-alignment module demonstrations plus formal Interval DP, Bitmask DP, DP on Trees and DP on Subsets/SOS DP.

### CO4
Ford-Fulkerson, Edmonds-Karp, Dinic, residual networks, max-flow/min-cut and bipartite matching.

### CO5
P/NP/NP-complete/NP-hard classification, canonical reductions, Vertex Cover 2-approximation and Set Cover demonstration.

### CO6
Randomized QuickSort, Miller-Rabin, randomized hashing, Reservoir Sampling, parallel reduce, prefix-sum and work-span analysis.

## Verification

The final build includes compile/run/test scripts, Java tests, frontend checks and dataset validation. The runnable workflow is:

Windows:
```bat
run.bat
```

Linux/macOS:
```bash
./run.sh
```

Then open `http://localhost:8080`.
