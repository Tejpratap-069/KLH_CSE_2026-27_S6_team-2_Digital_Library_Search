class Book {
    int id;
    String title;
    String author;
    String isbn;
    String category;
    String keywords;

    Book(int id, String title, String author,
         String isbn, String category, String keywords) {

        this.id = id;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.category = category;
        this.keywords = keywords;
    }

    void display() {
        System.out.println("--------------------------------");
        System.out.println("ID       : " + id);
        System.out.println("Title    : " + title);
        System.out.println("Author   : " + author);
        System.out.println("ISBN     : " + isbn);
        System.out.println("Category : " + category);
        System.out.println("Keywords : " + keywords);
    }
}