# Digital Library Search System

A Java-based Digital Library project developed for the **Data Structures and Algorithms - 3 (25CS2103E)** course.

## About the Project

The **Digital Library Search System** is a college project developed to make searching and managing digital library resources easier using Data Structures and Algorithms.

The system works with around **50,000 metadata records** containing Books, Journals, Research Papers, and Magazines. Users can search resources using different fields such as Title, Author, Identifier / ISBN, Category, Subject, and Keywords.

The main aim of the project is to apply the concepts learned in DSA-3 to a practical information-retrieval system. Instead of using MySQL or another database management system, the project stores persistent data in structured text files. The Java backend reads the records, loads them into custom data structures, performs the required algorithms, and sends the results to the frontend.

The project also includes an **Algorithm Lab** where the concepts from **CO1 to CO6** are demonstrated using the same Digital Library application.

---

## Objectives

The main objectives of the project are:

- To provide fast and organized searching of library resources.
- To allow searching using multiple fields such as title, author, ISBN, category, subject, and keywords.
- To apply Data Structures and Algorithms to a real-world problem.
- To support exact search, pattern matching, and fuzzy search.
- To work with a large repository of 50,000 records.
- To demonstrate the DSA-3 Course Outcomes from CO1 to CO6.
- To provide a simple web interface for users and administrators.
- To show algorithm runtime and complexity for selected operations.

---

## Main Features

The current project includes the following features:

- 50,000 library resource records
- Books, Journals, Research Papers, and Magazines
- Search by Title
- Search by Author
- Search by Identifier / ISBN
- Search by Category
- Search by Subject
- Search by Keywords
- Advanced Search
- Fuzzy Search for spelling mistakes
- Explore Library page
- Resource Details page
- Search History
- Search Suggestions
- Admin Add / Update / Delete
- Algorithm Lab for CO1 to CO6
- Algorithm runtime display
- Complexity display
- Sequential and parallel analytics
- Text-file based persistent storage

---

## DSA Algorithms Used

### CO1 - Problem Classification and Algorithm Selection

The system identifies the type of problem and selects a suitable algorithmic approach.

Examples:

- Exact identifier lookup → Hash Table
- Pattern search → KMP / Z / Rabin-Karp
- Fuzzy matching → Dynamic Programming
- Text similarity / repeated substring → Suffix Array + LCP
- Assignment problems → Network Flow
- NP-hard problems → Approximation Algorithms
- Random sampling → Randomized Algorithms
- Large-data processing → Parallel Algorithms

---

### CO2 - String Algorithms

The project implements the following string algorithms:

- Naive Pattern Matching
- Knuth-Morris-Pratt (KMP)
- LPS / Failure Function
- Z-Function
- Rabin-Karp
- Aho-Corasick
- Suffix Array
- LCP Array
- Kasai Algorithm

These algorithms are mainly used for searching textual metadata such as titles, authors, subjects, and keywords.

---

### CO3 - Advanced Dynamic Programming

The Dynamic Programming section includes:

- Levenshtein Distance
- Damerau-Levenshtein Distance
- Weighted Edit Distance
- Needleman-Wunsch
- Smith-Waterman
- Matrix Chain Multiplication
- Optimal Binary Search Tree
- TSP using Bitmask DP
- Hamiltonian Path
- DP on Trees
- DP on Subsets / SOS DP

Fuzzy Search uses edit-distance techniques to handle spelling mistakes.

Example:

```text
Entered Query:
algoritms

Expected Term:
algorithms
```

Instead of failing because of the spelling mistake, the system calculates the similarity using Dynamic Programming and can return related results.

---

### CO4 - Network Flow

The project implements:

- Ford-Fulkerson
- Edmonds-Karp
- Dinic Algorithm
- Residual Graph
- Max-Flow / Min-Cut
- Bipartite Matching

A library-based example is used for resource allocation.

```text
Source
   ↓
Students
   ↓
Available Library Resources
   ↓
Sink
```

This model can be used to calculate the maximum valid assignment of limited resources to students.

---

### CO5 - NP-Completeness and Approximation

The Algorithm Lab includes concepts such as:

- P
- NP
- NP-Complete
- NP-Hard
- Polynomial Reductions
- Vertex Cover 2-Approximation
- Greedy Set Cover
- Exact small-instance comparison

The project also demonstrates the reduction relationship:

```text
3-SAT
  ↓
CLIQUE
  ↓
INDEPENDENT SET
  ↓
VERTEX COVER
```

For selected small instances, the approximate result is compared with an exact solution.

---

### CO6 - Randomized and Parallel Algorithms

The project implements:

- Randomized QuickSort
- Miller-Rabin Primality Test
- Randomized / Universal Hashing
- Reservoir Sampling
- Parallel Search
- Parallel Reduce
- Parallel Prefix Sum
- Work-Span Analysis

The project also compares sequential and parallel execution results and reports the measured runtime on the current system.

---

## Technologies Used

| Part | Technology |
|---|---|
| Programming Language | Java |
| Frontend | HTML, CSS, JavaScript |
| Backend | Java 17+ |
| Server | Java HttpServer |
| Storage | Structured `.txt` Files |
| Data Handling | Java File I/O |
| Parallel Processing | Java ForkJoin |
| IDE | Visual Studio Code |
| Version Control | Git and GitHub |

### Database

This project does **not use a database management system**.

The main resource data is stored in:

```text
data/resources.txt
```

Other text files used in the project are:

```text
data/users.txt
data/requests.txt
data/search_history.txt
data/activity_log.txt
```

---

## Project Structure

```text
Project/
│
├── backend/
│   ├── src/
│   │   └── digitallibrary/
│   │       ├── algorithms/
│   │       ├── http/
│   │       ├── model/
│   │       ├── service/
│   │       ├── storage/
│   │       ├── structures/
│   │       └── Main.java
│   │
│   └── test/
│
├── frontend/
│   ├── index.html
│   ├── explore.html
│   ├── search.html
│   ├── resource.html
│   ├── algorithm-lab.html
│   ├── analytics.html
│   ├── admin.html
│   ├── css/
│   └── js/
│
├── data/
│   ├── resources.txt
│   ├── users.txt
│   ├── requests.txt
│   ├── search_history.txt
│   └── activity_log.txt
│
├── docs/
├── scripts/
├── README.md
├── run.bat
└── run.sh
```

### Important Folders

- `backend/src/digitallibrary/algorithms/` contains the DSA algorithms.
- `backend/src/digitallibrary/http/` contains the Java HTTP server and API handling.
- `backend/src/digitallibrary/service/` contains the main library and Algorithm Lab logic.
- `backend/src/digitallibrary/storage/` handles text-file storage and dataset generation.
- `backend/src/digitallibrary/structures/` contains custom data structures.
- `frontend/` contains the website pages, CSS, and JavaScript.
- `data/` contains the persistent text files.
- `scripts/` contains compile, generate, test, and verification scripts.

---

## How the Project Works

The basic application flow is:

```text
User
  ↓
HTML / CSS / JavaScript Frontend
  ↓
HTTP Request
  ↓
Java Backend
  ↓
Library Service / Algorithm Lab
  ↓
DSA Algorithms
  ↓
Text File Repository
  ↓
JSON Response
  ↓
Results Displayed on Website
```

When the application starts, Java reads the resource data from the structured text files.

The records are converted into Java objects and loaded into project-owned data structures.

When a user performs a search, the selected algorithm processes the records and returns matching resources. The backend sends the response as JSON, and JavaScript displays the results on the webpage.

---

## Main Website Pages

### Home

The Home page contains:

- Project introduction
- Repository statistics
- Main search option
- Navigation to the other modules

### Explore Library

The Explore page allows users to:

- Browse resources
- Filter by type
- Sort records
- Move through pages of results

### Advanced Search

The Advanced Search page allows users to:

- Enter a search query
- Select a search field
- Select an algorithm
- View execution time
- View records scanned
- View number of matches
- View complexity

### Resource Details

Displays complete information about a selected library resource.

### Algorithm Lab

Contains the main demonstrations for:

```text
CO1
CO2
CO3
CO4
CO5
CO6
```

### Analytics

Displays algorithm timing information and sequential vs parallel execution results.

### Admin

The Admin page supports:

- Add Resource
- Update Resource
- Delete Resource

Changes are stored in `resources.txt`.

---

## How to Run the Project

### Requirements

Install:

```text
JDK 17 or above
Git
Visual Studio Code or another Java IDE
A Web Browser
```

Check Java installation:

```powershell
java -version
javac -version
```

---

### Clone the Repository

```bash
git clone https://github.com/Tejpratap-069/KLH_CSE_2026-27_S6_team-2_Digital_Library_Search.git
```

Open the project folder:

```powershell
cd KLH_CSE_2026-27_S6_team-2_Digital_Library_Search
cd Project
```

---

### Run on Windows

Run:

```powershell
.\run.bat
```

The script will:

1. Compile the Java source files.
2. Create the build folder.
3. Generate the dataset if `resources.txt` is empty.
4. Start the Java server.

Open the application in the browser:

```text
http://localhost:8080
```

If port 8080 is already being used:

```powershell
.\run.bat 8090
```

Then open:

```text
http://localhost:8090
```

---

## Dataset

The project works with:

```text
50,000 Resource Records
```

The available resource types are:

```text
BOOK
JOURNAL
RESEARCH_PAPER
MAGAZINE
```

Each record follows this structure:

```text
id|type|title|author|identifier|category|subject|year|keywords|availability|publisher
```

If the dataset is missing or empty, generate it using:

```powershell
.\scripts\generate.bat 50000
```

---

## Main API Endpoints

Repository and search APIs:

```text
GET /api/stats
GET /api/resources
GET /api/resource
GET /api/search
GET /api/suggest
GET /api/history
```

Admin APIs:

```text
POST   /api/admin/resource
PUT    /api/admin/resource
DELETE /api/admin/resource
```

Algorithm Lab APIs:

```text
GET /api/algorithms/classify
GET /api/algorithms/string
GET /api/algorithms/dp
GET /api/algorithms/flow
GET /api/algorithms/approximation
GET /api/algorithms/co6
```

Other available endpoints include:

```text
GET /api/algorithms/fuzzy
GET /api/algorithms/randomized
GET /api/algorithms/parallel
GET /api/analytics
```

---

## Testing

The project includes test and verification scripts.

Run the tests using:

```powershell
.\scripts\test.bat
```

The testing covers:

- Custom data structures
- String algorithms
- Dynamic Programming
- Network Flow
- Approximation Algorithms
- Randomized Algorithms
- Parallel Algorithms
- Text-file storage
- Dataset generation
- Admin CRUD
- HTTP APIs

---

## Screenshots

Screenshots can be added here for:

- Home Page
- Explore Library
- Advanced Search
- Search Results
- Resource Details
- Algorithm Lab
- Analytics
- Admin Page

---

## Future Improvements

Some realistic improvements that can be added later are:

- Student and Faculty Login
- Book Borrowing and Return System
- Real University Library Integration
- Ranked Search Results
- Better Recommendation System
- Full-text Document Search
- Cloud Deployment
- Mobile Application
- Notifications for borrowed resources

---

## Learning From This Project

Through this project, we gained practical experience with:

- Java Programming
- Data Structures and Algorithms
- String Matching
- Dynamic Programming
- Network Flow
- Approximation Algorithms
- Randomized Algorithms
- Parallel Algorithms
- Java File I/O
- HTTP APIs
- Frontend and Backend Communication
- Git and GitHub
- Working with a large dataset

---

## Team Members

| Name | Student ID |
|---|---|
| Tej Pratap Singh | 2520030476 |
| B. Aryan | 2520030353 |

### Project Guide

**Dr. V. Sireesha**  
Professor, Department of Computer Science and Engineering  
K L Deemed to be University – Hyderabad Campus

---

## Course Details

**Course:** Data Structures and Algorithms - 3  
**Course Code:** 25CS2103E  
**Academic Year:** 2026–2027  
**Team Number:** 2

---

This project was developed as an academic project to understand and demonstrate the practical use of Data Structures and Algorithms through a Digital Library Search System.
