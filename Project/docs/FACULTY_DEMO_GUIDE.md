# Faculty Demo Guide — Digital Library Search System

## 1. Start with the application

Say:

> Our project is a Digital Library Search System with 50,000 metadata records. Java is the backend, HTML/CSS/JavaScript is the frontend, and persistent storage uses structured text files only. We do not use a DBMS.

Show **Home → Advanced Search**.

## 2. Show normal search

Search `algorithm` using KMP, Z and Rabin-Karp.

Say:

> This is the normal user-facing information retrieval part of the project. The search output also reports the algorithm, scanned records, matches, complexity and measured time.

## 3. Open Algorithm Lab

Explain that the Algorithm Lab is the course-outcome evidence layer.

### CO1

> CO1 evaluates the problem signature first. Exact lookup, substring search, DP, flow, NP-hard optimisation and randomised/parallel problems should not all use the same algorithm.

### CO2

> CO2 is string processing. We implemented KMP, Z, Rabin-Karp, Aho-Corasick, Suffix Array and Kasai LCP. The algorithms are hand-built and run on text taken from the library repository.

### CO3

> CO3 is Advanced Dynamic Programming. The formal evidence is Interval DP, Bitmask DP, DP on Trees and DP on Subsets. We also implemented edit distance and sequence alignment because they are Module-3 topics in the handout.

Point out:

- Matrix Chain cost: 26,000 for `[40,20,30,10,30]`
- Bitmask TSP demo
- Hamiltonian Path result
- Tree DP maximum weight
- SOS DP output

### CO4

> CO4 is Network Flow. We model students and limited resources as a bipartite flow network. Ford-Fulkerson, Edmonds-Karp and Dinic compute the maximum feasible assignment, and min-cut is obtained from the residual graph.

### CO5

> CO5 covers P, NP, NP-complete, NP-hard, reductions and approximation. Our concrete approximation algorithm is Vertex Cover 2-approximation using maximal matching. We compare it with an exact small-instance baseline and report the ratio.

### CO6

> CO6 covers Randomised and Parallel Algorithms. We show Randomized QuickSort as Las Vegas style, Miller-Rabin as Monte Carlo, randomized hashing, reservoir sampling, parallel reduce, prefix sum and work-span analysis.

## 4. Finish with Admin

Add a temporary resource and then delete it.

Say:

> The record is actually written to `data/resources.txt`, so the persistence layer is real even though no DBMS is used.

## Short CO summary

> CO1 chooses the strategy. CO2 performs advanced string processing. CO3 demonstrates Interval, Bitmask, Tree and Subset DP. CO4 solves capacity-constrained assignment with flow. CO5 handles NP-hardness and approximation. CO6 demonstrates randomized and parallel algorithms.
