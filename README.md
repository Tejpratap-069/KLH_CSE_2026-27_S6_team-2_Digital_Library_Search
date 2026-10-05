# Digital Library Search System

A Java-based Digital Library project developed for the **Data Structures and Algorithms - 3 (25CS2103E)** course.

---

## 📌 About the Project

The **Digital Library Search System** is developed to make searching and managing library resources easier using Data Structures and Algorithms.

The project works with around **50,000 metadata records** containing:

- Books
- Journals
- Research Papers
- Magazines

Users can search resources using:

- Title
- Author
- Identifier / ISBN
- Category
- Subject
- Keywords

The project does **not use MySQL or any other DBMS**. The records are stored in structured text files and are processed using the Java backend.

The project also contains an **Algorithm Lab** to demonstrate the concepts covered from **CO1 to CO6** of DSA-3.

---

## 🎯 Objectives

- Provide fast searching of library resources.
- Support searching using different resource fields.
- Apply DSA concepts in a real-world application.
- Support exact, pattern-based and fuzzy searching.
- Work with a large repository of 50,000 records.
- Demonstrate DSA-3 CO1 to CO6.
- Provide a simple web interface for searching and managing resources.

---

## ✨ Features

- 50,000 library metadata records
- Books, Journals, Research Papers and Magazines
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
- Admin Add / Update / Delete
- Algorithm Lab for CO1–CO6
- Algorithm runtime display
- Complexity display
- Sequential and parallel execution analytics
- Structured text-file storage

---

## 🧠 DSA Concepts Used

### CO1 - Problem Classification and Algorithm Selection

The system identifies the type of problem and selects a suitable algorithm.

Examples:

- Exact identifier lookup → Hash Table
- Pattern search → KMP / Z / Rabin-Karp
- Fuzzy matching → Dynamic Programming
- Text similarity / repeated substring → Suffix Array + LCP
- Assignment problems → Network Flow
- NP-hard problems → Approximation Algorithms
- Random sampling → Randomized Algorithms
- Large-data operations → Parallel Algorithms

---

### CO2 - String Algorithms

Implemented algorithms:

- Naive Pattern Matching
- Knuth-Morris-Pratt (KMP)
- LPS / Failure Function
- Z-Function
- Rabin-Karp
- Aho-Corasick
- Suffix Array
- LCP Array
- Kasai Algorithm

These algorithms are mainly used for searching textual data such as titles, authors and keywords.

---

### CO3 - Advanced Dynamic Programming

Implemented concepts:

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

Fuzzy Search uses Edit Distance to handle spelling mistakes in search queries.

Example:

```text
Entered:
algoritms

Expected:
algorithms
```

---

### CO4 - Network Flow

Implemented algorithms:

- Ford-Fulkerson
- Edmonds-Karp
- Dinic Algorithm
- Residual Graph
- Max-Flow / Min-Cut
- Bipartite Matching

Example model used in the project:

```text
Source
   ↓
Students
   ↓
Available Library Resources
   ↓
Sink
```

This can be used to calculate the maximum possible assignment of limited resources to students.

---

### CO5 - NP-Completeness and Approximation

Implemented concepts:

- P
- NP
- NP-Complete
- NP-Hard
- Polynomial Reductions
- Vertex Cover 2-Approximation
- Greedy Set Cover
- Exact small-instance comparison

Reduction example:

```text
3-SAT
  ↓
CLIQUE
  ↓
INDEPENDENT SET
  ↓
VERTEX COVER
```

---

### CO6 - Randomized and Parallel Algorithms

Implemented algorithms:

- Randomized QuickSort
- Miller-Rabin Primality Test
- Randomized / Universal Hashing
- Reservoir Sampling
- Parallel Search
- Parallel Reduce
- Parallel Prefix Sum
- Work-Span Analysis

The project also compares sequential and parallel execution time.

---

## 🛠️ Technologies Used

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
| Version Control | Git & GitHub |

### Database

No database management system is used.

The main resource data is stored in:

```text
data/resources.txt
```

Other text files include:

```text
data/users.txt
data/requests.txt
data/search_history.txt
data/activity_log.txt
```

---

## 🏗️ Project Structure

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

---

## ⚙️ How It Works

The basic flow of the application is:

```text
User
  ↓
HTML / CSS / JavaScript Frontend
  ↓
HTTP Request
  ↓
Java Backend
  ↓
DSA Algorithms
  ↓
Text File Repository
  ↓
JSON Response
  ↓
Results Displayed on Website
```

When the application starts, Java reads the resource data from `resources.txt`.

The records are loaded into Java objects and custom data structures.

When a user performs a search, the selected algorithm processes the records and the backend sends the matching results to the frontend as JSON.

---

## 🚀 Installation and Setup

### Requirements

Install:

```text
JDK 17 or above
Git
VS Code or any Java IDE
Web Browser
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

Move into the project folder:

```powershell
cd KLH_CSE_2026-27_S6_team-2_Digital_Library_Search
cd Project
```

---

## 💻 Running the Project

On Windows:

```powershell
.\run.bat
```

The script will:

1. Compile the Java files.
2. Create the build folder.
3. Generate the dataset if required.
4. Start the Java server.

Open the application in a browser:

```text
http://localhost:8080
```

If port `8080` is already in use:

```powershell
.\run.bat 8090
```

Then open:

```text
http://localhost:8090
```

---

## 📊 Dataset

The project works with:

```text
50,000 Resource Records
```

Resource types:

```text
BOOK
JOURNAL
RESEARCH_PAPER
MAGAZINE
```

Each resource follows the format:

```text
id|type|title|author|identifier|category|subject|year|keywords|availability|publisher
```

If `resources.txt` is empty, the dataset can be generated using:

```powershell
.\scripts\generate.bat 50000
```

---

## 🌐 Main Pages

### Home

Displays project information, repository statistics and the main search option.

### Explore Library

Allows users to browse resources with filtering, sorting and pagination.

### Advanced Search

Allows users to select the search field and algorithm.

### Resource Details

Displays complete information about a selected library resource.

### Algorithm Lab

Contains demonstrations for CO1 to CO6.

### Analytics

Displays algorithm runtime and sequential vs parallel performance.

### Admin

Allows resources to be:

- Added
- Updated
- Deleted

Changes are stored in the text-file repository.

---

## 🔗 Main API Endpoints

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

Algorithm APIs:

```text
GET /api/algorithms/classify
GET /api/algorithms/string
GET /api/algorithms/dp
GET /api/algorithms/flow
GET /api/algorithms/approximation
GET /api/algorithms/co6
GET /api/algorithms/fuzzy
GET /api/algorithms/randomized
GET /api/algorithms/parallel
GET /api/analytics
```

---

## 🧪 Testing

To run the project tests:

```powershell
.\scripts\test.bat
```

The tests cover:

- Custom Data Structures
- String Algorithms
- Dynamic Programming
- Network Flow
- Approximation Algorithms
- Randomized Algorithms
- Parallel Algorithms
- Text File Storage
- Dataset Generation
- Admin CRUD
- HTTP APIs

---

## 📸 Screenshots

Screenshots of the following pages can be added here:

- Home Page
- Explore Page
- Advanced Search
- Search Results
- Resource Details
- Algorithm Lab
- Analytics
- Admin Page

---

## 🔮 Future Improvements

Possible future improvements include:

- Student and Faculty Login
- Book Borrowing and Return System
- Real University Library Data
- Better Recommendation System
- Ranked Search Results
- Full-text Document Search
- Cloud Deployment
- Mobile Application
- Borrowing Notifications

---

## 📚 What We Learned

Through this project, we gained practical experience with:

- Java Programming
- Data Structures
- String Matching Algorithms
- Dynamic Programming
- Network Flow
- Approximation Algorithms
- Randomized Algorithms
- Parallel Algorithms
- Java File I/O
- HTTP APIs
- Frontend and Backend Communication
- Git and GitHub
- Working with Large Datasets

---

## 👨‍💻 Team Members

| Name | Student ID |
|---|---|
| Tej Pratap Singh | 2520030476 |
| B. Aryan | 2520030353 |

### Project Guide

**Dr. V. Sireesha**  
Professor, Department of Computer Science and Engineering  
K L Deemed to be University – Hyderabad Campus

---

## 📌 Course Details

**Course:** Data Structures and Algorithms - 3  
**Course Code:** 25CS2103E  
**Academic Year:** 2026–2027  
**Team Number:** 2

---

## 📖 Project Repository

```text
KLH_CSE_2026-27_S6_team-2_Digital_Library_Search
```

This project was developed as an academic project to understand and demonstrate the practical application of Data Structures and Algorithms through a Digital Library Search System.
