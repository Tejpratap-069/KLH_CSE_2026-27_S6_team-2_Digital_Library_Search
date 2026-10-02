# Digital Library Search System — Final DSA-3 Project

This folder contains the complete final **Digital Library Search System** revised against the official **DSA-3 (25CS2103E)** handout.

## Restore and run on Windows

1. Clone or download this GitHub repository.
2. Open `Project/Final_Project`.
3. Double-click `restore_project.bat`.
4. It creates a folder named `Digital-Library-Search-System-FINAL`.
5. Open that folder and run `run.bat`.
6. Open `http://localhost:8080`.

### Requirement
- JDK 17 or newer

## Included in the final project

- Java backend
- HTML/CSS/JavaScript frontend
- Home, Explore, Advanced Search, Resource Details, Algorithm Lab, Analytics and Admin pages
- Search by title, author, identifier/ISBN, category and keywords
- Text-file persistence with Java File I/O
- Admin add/update/delete operations
- Handout-accurate CO1–CO6 Algorithm Lab
- Verification scripts and documentation
- No MySQL, MongoDB, JDBC or other DBMS dependency

## Dataset

The full application uses **50,000 deterministic library records**. The large generated `data/resources.txt` file is not committed to GitHub. If it is missing or empty, the Java backend automatically generates the same 50,000-record dataset when the project starts.

## Archive layout

The source archive is stored as nine base64 parts:

`archive_part_00.b64` through `archive_part_08.b64`.

`restore_project.bat` combines and extracts them automatically.

Expected SHA-256 of the reconstructed `.tar.xz` archive:

`0d22ea2659509eaeff2ba0cbb293ba0123dcec84b975452ba526754208a14841`
