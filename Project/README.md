# Digital Library Search System — Handout-Accurate Final Build

**Course:** Data Structures and Algorithms - 3 (25CS2103E)  
**Academic year:** 2026-2027, Odd Semester  
**Backend:** Java 17+  
**Frontend:** HTML5 + CSS3 + Vanilla JavaScript  
**Persistent storage:** structured UTF-8 `.txt` files only  
**DBMS:** None  
**Dataset:** 50,000 deterministic library metadata records

This is the final Digital Library Search System revised directly against the official DSA-3 handout. The Digital Library remains a complete working application, while the **Algorithm Lab** now maps the project accurately to **CO1 through CO6**.

## Quick Start — Windows

1. Install **JDK 17 or newer**.
2. Extract the ZIP.
3. Open the extracted project folder — `run.bat` is directly in the root.
4. Double-click `run.bat`, or run:

```bat
run.bat
```

5. Open **http://localhost:8080**.

No Node.js, MySQL, MongoDB, XAMPP, Maven, Gradle, JDBC driver or external database is required.

If port 8080 is busy:

```bat
run.bat 8090
```

Then open `http://localhost:8090`.

## Project Structure

```text
Digital-Library-Search-System-HANDOUT-ACCURATE/
├── backend/
│   ├── src/digitallibrary/
│   │   ├── algorithms/      # hand-built CO2-CO6 algorithms
│   │   ├── http/            # Java HttpServer + JSON APIs
│   │   ├── model/           # Resource/SearchResult
│   │   ├── service/         # LibraryService + AlgorithmLabService
│   │   ├── storage/         # text-file repository + dataset generator
│   │   └── structures/      # custom DynamicArray, Queue, Stack, HashTable, LinkedList
│   └── test/                # dependency-free Java test harness
├── frontend/
│   ├── index.html
│   ├── explore.html
│   ├── search.html
│   ├── resource.html
│   ├── algorithm-lab.html
│   ├── analytics.html
│   └── admin.html
├── data/
│   ├── resources.txt        # 50,000 resources
│   ├── users.txt
│   ├── requests.txt
│   ├── search_history.txt
│   └── activity_log.txt
├── docs/
│   ├── HANDOUT_CO_MAPPING.md
│   ├── FACULTY_DEMO_GUIDE.md
│   ├── VERIFICATION.md
│   └── reference/DSA3_Handout_25CS2103E.pdf
├── scripts/
├── run.bat
└── run.sh
```

## Text-File Storage

The project deliberately does **not** use a DBMS. `data/resources.txt` is the persistent source of truth.

Each line uses:

```text
id|type|title|author|identifier|category|subject|year|keywords|availability|publisher
```

At startup, Java File I/O loads the rows into `Resource` objects and builds project-owned in-memory indexes. Admin add/update/delete operations rewrite the text file safely and append activity entries to `activity_log.txt`.

## Dataset

The bundled repository contains **50,000 records** across:

- BOOK
- JOURNAL
- RESEARCH_PAPER
- MAGAZINE

The generator supports 10K, 25K, 50K and 100K experiments.

```bat
scripts\generate.bat 50000
```

## Exact DSA-3 CO Mapping

### CO1 — Problem Classification & Algorithm Selection

The handout requires evaluation of a problem signature and selection of the correct advanced-algorithm family.

Implemented in `AlgorithmLabService.co1Classification()`:

- exact identifier lookup → custom hash index
- substring / pattern search → KMP / Z / Rabin-Karp
- fuzzy or sequence-style comparison → Dynamic Programming
- similarity / repeated substring → Suffix Array + LCP
- assignment / matching / capacity problem → Network Flow
- NP-hard optimisation → Approximation
- primality / sampling → Randomised Algorithms
- large-data reduce/prefix/search → Parallel Algorithms

The output includes the selected strategy, reason and asymptotic cost.

### CO2 — String Algorithms

Handout-aligned implementations:

- Naive Pattern Matching
- Knuth-Morris-Pratt (KMP) + LPS/failure function
- Z-Function
- Rabin-Karp with **double polynomial rolling hash**
- Aho-Corasick multi-pattern matching using a hand-built trie and failure links
- Suffix Array using doubling + hand-built merge sort
- LCP Array using the **Kasai algorithm**
- longest repeated substring demo from LCP

These algorithms are used in the search/Algorithm Lab to process titles, authors and keywords.

### CO3 — Advanced Dynamic Programming

The formal CO3 mapping is now explicit and no longer presented as “fuzzy search only”.

Implemented:

**Module-3 DP applications**
- Wagner-Fischer / Levenshtein Edit Distance
- Damerau-Levenshtein Distance
- Weighted Edit Distance
- Needleman-Wunsch global sequence alignment
- Smith-Waterman local sequence alignment

**Formal CO3 advanced DP patterns**
- Interval DP — Matrix Chain Multiplication
- Interval DP — Optimal Binary Search Tree
- Bitmask DP — Travelling Salesperson Problem
- Bitmask DP — Hamiltonian Path detection
- bounded bitmask resource-selection optimisation
- DP on Trees — maximum-weight independent set on a tree
- DP on Subsets / SOS DP — sum over subsets

The Algorithm Lab exposes all four formal CO3 categories: **Interval DP, Bitmask DP, DP on Trees, DP on Subsets**.

### CO4 — Network Flow

Implemented:

- flow network with source, sink and capacities
- residual graph
- Ford-Fulkerson using DFS augmenting paths
- Edmonds-Karp using BFS augmenting paths
- Dinic using level graphs + blocking flow
- max-flow / min-cut reachability
- bipartite matching reduced to max-flow

Digital Library demo:

```text
Source → Students → Limited Library Resources → Sink
```

The lab compares Ford-Fulkerson, Edmonds-Karp and Dinic on the same assignment network and verifies that they produce the same maximum flow.

### CO5 — NP-Completeness & Approximation

The lab explains:

- P
- NP
- NP-complete
- NP-hard
- canonical reduction chain: `3-SAT → CLIQUE → INDEPENDENT-SET → VERTEX-COVER`

Implemented approximation:

- **Vertex Cover 2-Approximation via maximal matching**
- exact minimum Vertex Cover baseline for small graphs
- observed approximation ratio
- validity checker for the returned cover

Additional demonstration:

- Greedy Set Cover
- exact small-instance Set Cover baseline

The Vertex Cover demo reports the provable **≤ 2 × OPT** guarantee required by the handout's worked approximation example.

### CO6 — Randomised & Parallel Algorithms

**Randomised algorithms**

- Las-Vegas-style Randomized QuickSort
- Monte-Carlo Miller-Rabin primality test
- universal/randomized hashing
- Reservoir Sampling for streaming data
- hand-built xorshift PRNG inside the algorithm engine

**Parallel algorithms**

- parallel repository search/count using ForkJoin
- parallel reduce
- Blelloch-style exclusive prefix sum / parallel scan
- work-span explanation
- measured sequential-vs-parallel runtime comparison

The UI clearly distinguishes **Las Vegas** from **Monte Carlo** behavior and reports real machine-dependent timings rather than claiming a fixed speedup.

## Hand-Built Engine Constraint

The handout says `java.util.*` is forbidden inside the algorithm engine and students should build the algorithms themselves.

This final build follows that intent:

- no Java collection classes are used inside `backend/src/digitallibrary/algorithms/`
- custom queue/data structures are used where needed
- string, DP, flow, approximation and randomized logic is implemented manually
- concurrency primitives are used only for the actual parallel-algorithm demonstrations

Java collection classes are still used in the **HTTP/service presentation layer** to construct JSON responses; they are not used to replace the algorithms being demonstrated.

## Main Website Features

- cinematic responsive Home page
- Explore page with resource browsing
- Advanced Search by title, author, identifier, category, subject and keywords
- selectable Naive / KMP / Z / Rabin-Karp / Fuzzy / Hash search modes
- Resource Details page
- handout-accurate CO1-CO6 Algorithm Lab
- Analytics with string and parallel runtime measurements
- Admin add/update/delete operations
- text-file persistence
- search history and activity log

## Main APIs

```text
GET    /api/stats
GET    /api/resources
GET    /api/resource?id=LIB000001
GET    /api/search?q=algorithm&field=all&algorithm=kmp
GET    /api/suggest?q=algo
GET    /api/history
POST   /api/admin/resource
PUT    /api/admin/resource
DELETE /api/admin/resource?id=LIB900001

GET    /api/algorithms/classify       # CO1
GET    /api/algorithms/string         # CO2
GET    /api/algorithms/dp             # CO3 formal + Module-3 DP demos
GET    /api/algorithms/flow           # CO4
GET    /api/algorithms/approximation  # CO5
GET    /api/algorithms/co6            # CO6 randomised + parallel

# compatibility / focused endpoints
GET    /api/algorithms/fuzzy
GET    /api/algorithms/randomized
GET    /api/algorithms/parallel
GET    /api/analytics
```

## Compile and Test

Windows:

```bat
scripts\test.bat
```

Linux/macOS:

```bash
bash scripts/test.sh
```

The test harness checks the application, custom structures and handout-aligned algorithms including:

- KMP / Z / Rabin-Karp / Aho-Corasick
- Suffix Array / Kasai LCP
- edit distance and sequence alignment
- Interval / Bitmask / Tree / Subset DP
- Ford-Fulkerson / Edmonds-Karp / Dinic / bipartite matching
- Vertex Cover approximation and exact baseline
- Randomized QuickSort / Miller-Rabin / Reservoir Sampling / randomized hashing
- Parallel search / reduce / prefix sum
- text repository, dataset generation, CRUD and HTTP APIs

## Recommended Faculty Demo

1. **Home** — explain 50K records and TXT-only persistent storage.
2. **Advanced Search** — run `algorithm` with KMP, Z and Rabin-Karp.
3. **Algorithm Lab → CO1** — show problem classification.
4. **CO2** — show single-pattern, multi-pattern and suffix outputs.
5. **CO3** — point out Interval DP, Bitmask DP, Tree DP and SOS DP in the JSON output.
6. **CO4** — show all three max-flow algorithms and matching result.
7. **CO5** — show Vertex Cover 2-approximation, exact baseline and ratio.
8. **CO6** — show Miller-Rabin, sampling, prefix/reduce and sequential-vs-parallel measurements.
9. **Admin** — add/update/delete a record and explain that `resources.txt` changes permanently.

## One-Minute Faculty Explanation

> This is a Java-based Digital Library Search System operating on 50,000 text-file records. The normal application supports multi-field searching, browsing, analytics and admin CRUD without a DBMS. The Algorithm Lab is mapped directly to the DSA-3 handout: CO1 selects the appropriate advanced paradigm; CO2 implements KMP, Z, Rabin-Karp, Aho-Corasick and suffix structures; CO3 demonstrates interval, bitmask, tree and subset DP; CO4 implements Ford-Fulkerson, Edmonds-Karp, Dinic and matching; CO5 demonstrates NP-hardness concepts and Vertex Cover 2-approximation; and CO6 implements randomized QuickSort, Miller-Rabin, reservoir sampling, randomized hashing, parallel reduce, prefix sum and work-span analysis.

## Troubleshooting

**`javac is not recognized`**  
Install JDK 17+ and add its `bin` directory to PATH.

**Project path contains spaces**  
The Windows scripts support spaces in usernames, OneDrive paths and project folders. Use `run.bat` normally.

**No `package.json`**  
Correct. This is not a Node backend. Do **not** run `npm start`.

**Dataset file was removed**  
Run `scripts\generate.bat 50000`; startup also regenerates a missing/empty dataset.

## Academic Note

The 50K dataset contains synthetic metadata created for this project. It does not include copyrighted full-text books or papers.
