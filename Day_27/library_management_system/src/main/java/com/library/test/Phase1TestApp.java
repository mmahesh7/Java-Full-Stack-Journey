package Day_27.library_management_system.src.main.java.com.library.test;

import Day_27.library_management_system.src.main.java.com.library.dao.AuthorDAO;
import Day_27.library_management_system.src.main.java.com.library.dao.BookDAO;
import Day_27.library_management_system.src.main.java.com.library.model.Author;
import Day_27.library_management_system.src.main.java.com.library.model.Book;
import Day_27.library_management_system.src.main.java.com.library.util.DatabaseConnection;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class Phase1TestApp {
    private static AuthorDAO authorDAO = new AuthorDAO();
    private static BookDAO bookDAO = new BookDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Library Management System - Phase 1 Testing ===");

        // Test database connection
        DatabaseConnection.testConnection();

        while (true) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    testAuthorOperations();
                    break;
                case 2:
                    testBookOperations();
                    break;
                case 3:
                    displayAllData();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Main Menu ---");
        System.out.println("1. Test Author Operations");
        System.out.println("2. Test Book Operations");
        System.out.println("3. Display All Data");
        System.out.println("0. Exit");
    }

    private static void testAuthorOperations() {
        System.out.println("\n--- Testing Author CRUD Operations ---");

        // Create test author
        Author author = new Author(1,"J.K. Rowling", "jk@example.com", 1965,
                "British author, best known for Harry Potter series");

        System.out.println("1. Creating author: " + author.getName());
        if (authorDAO.createAuthor(author)) {
            System.out.println("Author created with ID: " + author.getAuthorId());
        } else {
            System.out.println("Failed to create author");
        }

        // Read author
        System.out.println("\n2. Reading author by ID: " + author.getAuthorId());
        Author retrievedAuthor = authorDAO.getAuthorById(author.getAuthorId());
        if (retrievedAuthor != null) {
            System.out.println("Retrieved: " + retrievedAuthor);
        }

        // Update author
        System.out.println("\n3. Updating author biography");
        author.setBiography("British author famous for the Harry Potter fantasy series");
        if (authorDAO.updateAuthor(author)) {
            System.out.println("Author updated successfully");
        }

        // List all authors
        System.out.println("\n4. Listing all authors:");
        List<Author> authors = authorDAO.getAllAuthors();
        authors.forEach(System.out::println);
    }

    private static void testBookOperations() {
        System.out.println("\n--- Testing Book CRUD Operations ---");

        // First, ensure we have an author
        List<Author> authors = authorDAO.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("No authors found. Creating a sample author first.");
            Author author = new Author("George Orwell", "orwell@example.com", 1903, "English novelist");
            authorDAO.createAuthor(author);
            authors = authorDAO.getAllAuthors();
        }

        Author author = authors.get(0);

        // Create test book
        Book book = new Book("1984", "978-0-452-28423-4", 1949,
                new BigDecimal("12.99"), 5, author.getAuthorId());

        System.out.println("1. Creating book: " + book.getTitle());
        if (bookDAO.createBook(book)) {
            System.out.println("Book created with ID: " + book.getBookId());
        } else {
            System.out.println("Failed to create book");
        }

        // Read book
        System.out.println("\n2. Reading book by ID: " + book.getBookId());
        Book retrievedBook = bookDAO.getBookById(book.getBookId());
        if (retrievedBook != null) {
            System.out.println("Retrieved: " + retrievedBook);
        }

        // Update book
        System.out.println("\n3. Updating book price");
        book.setPrice(new BigDecimal("14.99"));
        if (bookDAO.updateBook(book)) {
            System.out.println("Book updated successfully");
        }

        // List all books
        System.out.println("\n4. Listing all books:");
        List<Book> books = bookDAO.getAllBooks();
        books.forEach(System.out::println);
    }

    private static void displayAllData() {
        System.out.println("\n--- Current Database State ---");

        System.out.println("\nAUTHORS:");
        List<Author> authors = authorDAO.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("No authors found.");
        } else {
            authors.forEach(author -> System.out.println("  " + author));
        }

        System.out.println("\nBOOKS:");
        List<Book> books = bookDAO.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books found.");
        } else {
            books.forEach(book -> System.out.println("  " + book));
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        return scanner.nextInt();
    }
}
