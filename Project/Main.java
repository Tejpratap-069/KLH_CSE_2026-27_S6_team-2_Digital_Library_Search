import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Library library = new Library();
        Scanner sc = new Scanner(System.in);

        // Sample books
        library.addBook(
            new Book(
                1,
                "Introduction to Data Structures",
                "Mark Allen",
                "ISBN1001",
                "Computer Science",
                "data structures algorithms java"
            )
        );

        library.addBook(
            new Book(
                2,
                "Java Programming",
                "James Gosling",
                "ISBN1002",
                "Programming",
                "java programming oop"
            )
        );

        library.addBook(
            new Book(
                3,
                "Database Management Systems",
                "Raghu Ramakrishnan",
                "ISBN1003",
                "Database",
                "database sql management"
            )
        );

        library.addBook(
            new Book(
                4,
                "Computer Networks",
                "Andrew Tanenbaum",
                "ISBN1004",
                "Networking",
                "network tcp ip protocols"
            )
        );

        while (true) {

            System.out.println("\n========== DIGITAL LIBRARY ==========");
            System.out.println("1. Add Book");
            System.out.println("2. Search Book");
            System.out.println("3. Display All Books");
            System.out.println("4. Exit");
            System.out.print("Enter your choice: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    System.out.println("\n----- ADD BOOK -----");

                    System.out.print("Enter Book ID: ");
                    int id = sc.nextInt();
                    sc.nextLine();

                    System.out.print("Enter Book Title: ");
                    String title = sc.nextLine();

                    System.out.print("Enter Author: ");
                    String author = sc.nextLine();

                    System.out.print("Enter ISBN: ");
                    String isbn = sc.nextLine();

                    System.out.print("Enter Category: ");
                    String category = sc.nextLine();

                    System.out.print("Enter Keywords: ");
                    String keywords = sc.nextLine();

                    Book book = new Book(
                        id,
                        title,
                        author,
                        isbn,
                        category,
                        keywords
                    );

                    library.addBook(book);

                    System.out.println("Book added successfully!");

                    break;

                case 2:
                    System.out.println("\n----- SEARCH BOOK -----");

                    System.out.print("Enter search text: ");
                    String query = sc.nextLine();

                    library.search(query);

                    break;

                case 3:
                    System.out.println("\n----- ALL BOOKS -----");

                    library.displayAllBooks();

                    break;

                case 4:
                    System.out.println("Thank you for using Digital Library!");
                    sc.close();
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }
}