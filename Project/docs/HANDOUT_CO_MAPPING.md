# DSA-3 Handout → Digital Library Project Mapping

**Course:** Data Structures and Algorithms - 3  
**Code:** 25CS2103E  
**Basis:** Official 2026-2027 Odd Semester handout supplied for the project revision.

This document distinguishes the **formal Course Outcomes** from extra module demonstrations so the project can be explained accurately during review.

## CO1 — Evaluate the problem class and select the advanced strategy

**Project location:** `AlgorithmLabService.co1Classification()` and Algorithm Lab → CO1.

| Problem signature | Project strategy |
|---|---|
| exact ID / identifier | custom hash index |
| substring / pattern | KMP / Z / Rabin-Karp |
| fuzzy / alignment | Dynamic Programming |
| repeated substring / similarity | Suffix Array + LCP |
| assignment / capacity | Network Flow |
| NP-hard optimisation | Approximation |
| primality / sampling | Randomised Algorithms |
| reduce/prefix/large data | Parallel Algorithms |

CO1 is the algorithm-selection layer, not one specific search algorithm.

## CO2 — Linear-time string algorithms + suffix structures

**Implemented:**

- Naive Pattern Matching (baseline)
- KMP + LPS
- Z-Function
- Rabin-Karp with double rolling hash
- Aho-Corasick (module multi-pattern topic)
- Suffix Array
- Kasai LCP

**Project use:** title/author/keyword pattern retrieval and Algorithm Lab benchmarks.

## CO3 — Advanced Dynamic Programming

**Formal CO3 mapping implemented:**

- Interval DP → Matrix Chain Multiplication, Optimal BST
- Bitmask DP → TSP, Hamiltonian Path, bounded resource-selection demo
- DP on Trees → maximum-weight independent set
- DP on Subsets / SOS DP → sum-over-subsets

**Additional Module-3 implementations:**

- Wagner-Fischer / Levenshtein
- Damerau-Levenshtein
- weighted edit distance
- Needleman-Wunsch
- Smith-Waterman

Important review wording: fuzzy search is a useful Module-3 DP application, but the **formal CO3 evidence** is the Interval/Bitmask/Tree/Subset DP output in Algorithm Lab.

## CO4 — Network Flow

**Implemented:**

- residual graph
- Ford-Fulkerson (DFS augmenting path)
- Edmonds-Karp (BFS augmenting path)
- Dinic (level graph + blocking flow)
- max-flow/min-cut
- bipartite matching reduction

**Project scenario:** students requesting limited library-resource access slots.

## CO5 — NP-Completeness & Approximation

**Conceptual evidence in UI/API:** P, NP, NP-complete, NP-hard and canonical reduction chain.

**Implemented approximation:**

- Vertex Cover 2-approximation via maximal matching
- exact small-instance Vertex Cover baseline
- observed ratio + validity check
- Greedy Set Cover as an additional demonstration

The project does not claim to prove Cook-Levin inside the application; it demonstrates the classification/reduction framework and a provable approximation algorithm as required by the CO.

## CO6 — Randomised & Parallel Algorithms

**Randomised:**

- Randomized QuickSort — Las Vegas style
- Miller-Rabin — Monte Carlo style
- universal/randomized hashing
- Reservoir Sampling

**Parallel:**

- ForkJoin repository match counting
- parallel reduce
- Blelloch-style exclusive prefix sum
- work/span explanation and measured speedup

Runtime speedup is machine-dependent and is displayed as a measurement, not a fixed claim.
