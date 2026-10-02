    import java.util.ArrayList;

class Library {

    // Stores all books
    ArrayList<Book> books = new ArrayList<>();


    // ==========================================
    // ADD BOOK
    // ==========================================
    void addBook(Book book) {

        books.add(book);

        System.out.println("Book added successfully!");
    }


    // ==========================================
    // DISPLAY ALL BOOKS
    // ==========================================
    void displayAllBooks() {

        if (books.isEmpty()) {
            System.out.println("\nNo books available in the library.");
            return;
        }

        System.out.println("\n========== ALL BOOKS ==========");

        for (Book book : books) {
            book.display();
            System.out.println("-------------------------------");
        }
    }


    // ==========================================
    // SEARCH BOOK
    // ==========================================
    void search(String query) {

        System.out.println("\n================================");
        System.out.println("SEARCH RESULTS FOR: " + query);
        System.out.println("================================");

        boolean found = false;


        // ------------------------------------------
        // FIRST: KMP EXACT PATTERN SEARCH
        // ------------------------------------------
        for (Book book : books) {

            if (
                PatternMatcher.KMP(book.title, query) ||
                PatternMatcher.KMP(book.author, query) ||
                PatternMatcher.KMP(book.category, query) ||
                PatternMatcher.KMP(book.keywords, query)
            ) {

                book.display();

                System.out.println("Match Type: KMP Exact Pattern Search");
                System.out.println("-------------------------------");

                found = true;
            }
        }


        // ------------------------------------------
        // IF KMP FINDS NOTHING
        // TRY FUZZY SEARCH
        // ------------------------------------------
        if (!found) {

            System.out.println("No exact pattern found.");
            System.out.println("\nTrying fuzzy search...");

            fuzzySearch(query);
        }
    }


    // ==========================================
    // FUZZY SEARCH
    // ==========================================
    void fuzzySearch(String query) {

        boolean found = false;

        for (Book book : books) {

            // Compare query with title
            int distance = FuzzySearch.levenshtein(
                query.toLowerCase(),
                book.title.toLowerCase()
            );


            // Distance <= 3 means reasonably similar
            if (distance <= 3) {

                System.out.println("\nPossible Match:");

                book.display();

                System.out.println(
                    "Levenshtein Distance: " + distance
                );

                found = true;
            }
        }


        if (!found) {

            System.out.println(
                "\nNo similar books found."
            );
        }
    }
}