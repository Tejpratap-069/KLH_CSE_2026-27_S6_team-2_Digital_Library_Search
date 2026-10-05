# Digital Library Search System

A Java-based Digital Library project developed for the **Data Structures and Algorithms - 3 (25CS2103E)** course.

## About the Project

The **Digital Library Search System** is designed to make searching and managing library resources easier using Data Structures and Algorithms.

The project works with around **50,000 metadata records** containing:

- Books
- Journals
- Research Papers
- Magazines

Users can search using:

- Title
- Author
- Identifier / ISBN
- Category
- Subject
- Keywords

The project does not use MySQL or any other DBMS. Data is stored in structured text files and processed by the Java backend.

## Objectives

- Provide fast searching of library resources.
- Support multiple search fields.
- Apply DSA concepts in a practical project.
- Support exact, pattern-based and fuzzy searching.
- Demonstrate DSA-3 CO1 to CO6.
- Work with a large repository of 50,000 records.

## Main Features

- 50,000 resource records
- Advanced Search
- Fuzzy Search
- Explore Library page
- Resource Details page
- Search History
- Admin Add / Update / Delete
- Algorithm Lab for CO1 to CO6
- Runtime and complexity display
- Sequential and parallel analytics
- Text-file based storage

## DSA Algorithms Used

### CO1 - Algorithm Selection

- Exact lookup → Hash Table
- Pattern search → KMP / Z / Rabin-Karp
- Fuzzy matching → Dynamic Programming
- Assignment problems → Network Flow
- NP-hard problems → Approximation
- Large-data operations → Parallel Algorithms

### CO2 - String Algorithms

- Naive Pattern Matching
- KMP
- Z-Function
- Rabin-Karp
- Aho-Corasick
- Suffix Array
- LCP / Kasai Algorithm

### CO3 - Advanced Dynamic Programming

- Levenshtein Distance
- Damerau-Levenshtein Distance
- Weighted Edit Distance
- Needleman-Wunsch
- Smith-Waterman
- Matrix Chain Multiplication
- Optimal BST
- TSP using Bitmask DP
- Hamiltonian Path
- DP on Trees
- SOS DP

### CO4 - Network Flow

- Ford-Fulkerson
- Edmonds-Karp
- Dinic Algorithm
- Max-Flow / Min-Cut
- Bipartite Matching

### CO5 - NP-Completeness and Approximation

- P, NP, NP-Complete and NP-Hard
- Vertex Cover 2-Approximation
- Greedy Set Cover
- Exact small-instance comparison

### CO6 - Randomized and Parallel Algorithms

- Randomized QuickSort
- Miller-Rabin Primality Test
- Randomized Hashing
- Reservoir Sampling
- Parallel Search
- Parallel Reduce
- Parallel Prefix Sum
- Work-Span Analysis

## Technologies Used

| Part | Technology |
|---|---|
| Frontend | HTML, CSS, JavaScript |
| Backend | Java 17+ |
| Server | Java HttpServer |
| Storage | Structured `.txt` files |
| Data Handling | Java File I/O |
| Parallel Processing | Java ForkJoin |
| IDE | VS Code |
| Version Control | Git & GitHub |

## Project Structure

```text
Project/
│
├── backend/
│   ├── src/digitallibrary/
│   │   ├── algorithms/
│   │   ├── http/
│   │   ├── model/
│   │   ├── service/
│   │   ├── storage/
│   │   ├── structures/
│   │   └── Main.java
│   └── test/
│
├── frontend/
│   ├── index.html
│   ├── explore.html
│   ├── search.html
│   ├── resource.html
│   ├── algorithm-lab.html
│   ├── analytics.html
│   └── admin.html
│
├── data/
├── docs/
├── scripts/
├── README.md
├── run.bat
└── run.sh
