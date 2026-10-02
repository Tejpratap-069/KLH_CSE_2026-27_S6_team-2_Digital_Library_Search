# Digital Library Search System — Design Specification

**Course:** Data Structures and Algorithms - 3 (25CS2103E)  
**Project:** Digital Library Search System  
**Date:** 2026-09-17

## 1. Goal

Build a launchable, presentation-ready Digital Library Search System with a polished responsive website and a Java backend. Persistent storage must use structured `.txt` files only. No MySQL, MongoDB, SQLite, Firebase, JDBC database, or external database service is permitted.

The project must demonstrate DSA-3 CO1 through CO6 using the same large Digital Library dataset rather than unrelated sample programs.

## 2. Final Technology Stack

- Frontend: HTML5, CSS3, vanilla JavaScript
- Backend: Java 17+
- HTTP layer: JDK built-in `com.sun.net.httpserver.HttpServer`
- Persistence: UTF-8 structured `.txt` files only
- Data exchange: JSON generated/parsed by project-owned lightweight utilities
- Build/run: plain `javac` / `java`; no Maven/Gradle requirement
- Version control: Git-ready project
- Database: none

## 3. Dataset and Persistence

The ZIP will include a generated repository of approximately 50,000 resources with deterministic IDs and realistic fields. A Java dataset generator will also support regenerating 10K, 25K, 50K, and 100K records for benchmarking.

Resource types:
- Books
- Journals
- Research papers
- Magazines

Primary text files:
- `data/resources.txt`
- `data/users.txt`
- `data/requests.txt`
- `data/search_history.txt`
- `data/activity_log.txt`

Resource record format:

`id|type|title|author|identifier|category|subject|year|keywords|availability|publisher`

Text files are the only persistent source of truth. On startup, Java loads them into project-owned in-memory structures and builds indexes. Searches do not repeatedly scan the file unless the selected demonstration intentionally uses linear scanning.

## 4. Core Custom Data Structures

Inside the DSA engine, core structures will be implemented by the project rather than delegating the algorithm to high-level Java collections:

- Dynamic array
- Singly linked list
- Queue
- Stack
- Hash table
- Graph / flow network structures
- Suffix-array helper structures

Standard JDK facilities may be used for HTTP, files, threads, strings, and low-level utility plumbing, but algorithm results must come from the project implementations.

## 5. CO1–CO6 Mapping

### CO1 — Advanced Algorithm Selection
- Query classifier determines exact lookup, substring search, fuzzy search, optimization, flow/matching, or randomized/parallel operation.
- Algorithm recommendation panel shows selected strategy and asymptotic time/space complexity.
- Benchmark comparison between naive and advanced approaches.

### CO2 — String Algorithms
Implemented: Naive search, KMP, Z-function, Rabin-Karp rolling hash, suffix array, LCP.
Applications: title/author/category/subject/keyword search and benchmark comparison.

### CO3 — Advanced Dynamic Programming
Implemented: Levenshtein, Damerau/weighted edit distance, fuzzy search, bounded bitmask-DP resource selection, interval-DP educational demonstration.

### CO4 — Network Flow
Implemented: Ford-Fulkerson, Edmonds-Karp, residual graph, min-cut.
Application: allocate limited resource-access slots to student requests using a bipartite flow network.

### CO5 — NP-Completeness and Approximation
Implemented: decision vs optimization lab, Greedy Set Cover approximation, Vertex-Cover 2-approximation, small-instance exact baseline where safe.

### CO6 — Randomized and Parallel Algorithms
Implemented: Randomized QuickSort, randomized hashing demonstration, Reservoir Sampling, sequential-vs-parallel search/reduce benchmark, work/span explanation.

## 6. Website Pages

- Home: hero, global search, repository statistics, featured resources.
- Explore Library: paginated resources, filters and sorting.
- Advanced Search: fields + selectable search algorithms + live metrics.
- Resource Details: metadata and similar resources.
- Algorithm Lab: CO1–CO6 demonstrations.
- Analytics: repository and algorithm performance charts.
- Admin: CRUD, recent activity, repository controls.

All mutations persist to `.txt` files.

## 7. Representative Backend APIs

- `GET /api/stats`
- `GET /api/resources`
- `GET /api/resource?id=`
- `GET /api/search`
- `GET /api/suggest`
- `POST /api/admin/resource`
- `PUT /api/admin/resource`
- `DELETE /api/admin/resource`
- `GET /api/history`
- `GET /api/algorithms/string`
- `GET /api/algorithms/fuzzy`
- `POST /api/algorithms/flow`
- `POST /api/algorithms/approximation`
- `GET /api/algorithms/randomized`
- `GET /api/algorithms/parallel`

The Java server also serves the static frontend.

## 8. Search Architecture

1. Load text records.
2. Build custom indexes.
3. Accept web search request.
4. CO1 classifier or explicit selection chooses an algorithm.
5. Algorithm executes against in-memory data.
6. Metrics collector records comparisons, elapsed time, matches, and complexity.
7. JSON response is returned.
8. Frontend renders results.
9. Search is appended to `search_history.txt`.

## 9. UI Direction

Premium academic/digital-library interface using graphite, warm gold, ivory, and subtle glass/gradient effects inspired by the review presentation but redesigned as a modern product.

Requirements:
- Responsive desktop/mobile
- Smooth restrained animation
- Readable resource cards
- CO badges and algorithm metric chips
- Accessible contrast/focus states
- No fake benchmark numbers

## 10. Error Handling

- Malformed rows are skipped and logged.
- Invalid queries return HTTP 400 JSON.
- Unknown resources return HTTP 404.
- Duplicate identifiers are rejected.
- Writes use temporary replacement where supported.
- Missing required data files are created safely.

## 11. Testing and Verification

Test harnesses cover:
- KMP, Z, Rabin-Karp
- Suffix array/LCP
- Edit distance
- Custom hash table
- Max-flow known networks
- Approximation basic guarantees
- Reservoir sampling bounds
- Randomized QuickSort sortedness
- Parallel/sequential result equivalence
- Text persistence round trip
- API smoke tests

Final verification:
- Clean compilation
- Test run
- Server startup
- Core API checks
- Frontend asset checks
- No SQL/database dependency
- ZIP includes dataset, generator, README, run scripts

## 12. Deliverable

`Digital-Library-Search-System.zip`, containing:
- `frontend/`
- `backend/src/`
- `backend/test/`
- `data/`
- `docs/`
- `scripts/`
- `README.md`
- `run.bat`
- `run.sh`

Windows workflow: extract, run `run.bat`, open `http://localhost:8080`.

## 13. Scope Boundary

Included: functional web UI, Java backend, ~50K text repository, CRUD, CO1–CO6 lab, analytics, benchmarks, tests, documentation.

Excluded unless requested later: production cloud deployment, institutional SSO, copyrighted full-text book distribution, paid APIs, production-grade auth/security.
