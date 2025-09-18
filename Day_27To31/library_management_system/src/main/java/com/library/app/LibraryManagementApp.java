package Day_27To31.library_management_system.src.main.java.com.library.app;

import Day_27To31.library_management_system.src.main.java.com.library.dao.AuthorDAO;
import Day_27To31.library_management_system.src.main.java.com.library.dao.BookDAO;
import Day_27To31.library_management_system.src.main.java.com.library.dao.BookLoanDAO;
import Day_27To31.library_management_system.src.main.java.com.library.dao.MemberDAO;
import Day_27To31.library_management_system.src.main.java.com.library.model.Author;
import Day_27To31.library_management_system.src.main.java.com.library.model.Book;
import Day_27To31.library_management_system.src.main.java.com.library.model.BookLoan;
import Day_27To31.library_management_system.src.main.java.com.library.model.Member;
import Day_27To31.library_management_system.src.main.java.com.library.service.LibraryService;
import Day_27To31.library_management_system.src.main.java.com.library.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.util.List;
import java.util.Scanner;

/**
 * Integrated Console Application for Library Management System
 */
public class LibraryManagementApp {

    // DAOs and Services
    private static AuthorDAO authorDAO = new AuthorDAO();
    private static BookDAO bookDAO = new BookDAO();
    private static MemberDAO memberDAO = new MemberDAO();
    private static BookLoanDAO bookLoanDAO = new BookLoanDAO();
    private static LibraryService libraryService = new LibraryService();

    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("====================================================");
        System.out.println("    LIBRARY MANAGEMENT SYSTEM - CONSOLE APP");
        System.out.println("====================================================");

        // Test database connection
        DatabaseConnection.testConnection();

        if (!isDatabaseConnected()) {
            System.out.println("Cannot connect to database. Exiting...");
            return;
        }

        boolean running = true;
        while (running) {
            displayMainMenu();
            int choice = getIntInput("Enter your choice: ");

            try {
                switch (choice) {
                    case 1:
                        handleAuthorManagement();
                        break;
                    case 2:
                        handleBookManagement();
                        break;
                    case 3:
                        handleMemberManagement();
                        break;
                    case 4:
                        handleLoanManagement();
                        break;
                    case 5:
                        handleReports();
                        break;
                    case 6:
                        handleSystemOperations();
                        break;
                    case 0:
                        System.out.println("Thank you for using Library Management System!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice! Please try again.");
                }
            } catch (Exception e) {
                System.err.println("An error occurred: " + e.getMessage());
                System.out.println("Please try again or contact administrator.");
            }

            if (running) {
                System.out.println("\nPress Enter to continue...");
                scanner.nextLine();
            }
        }

        scanner.close();
    }

    private static boolean isDatabaseConnected() {
        try {
            DatabaseConnection.getConnection().close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ==================== MAIN MENU ====================

    private static void displayMainMenu() {
        clearScreen();
        System.out.println("====================================================");
        System.out.println("                   MAIN MENU");
        System.out.println("====================================================");
        System.out.println("1. Author Management");
        System.out.println("2. Book Management");
        System.out.println("3. Member Management");
        System.out.println("4. Loan Management");
        System.out.println("5. Reports & Statistics");
        System.out.println("6. System Operations");
        System.out.println("0. Exit");
        System.out.println("====================================================");
    }

    // ==================== AUTHOR MANAGEMENT ====================

    private static void handleAuthorManagement() {
        while (true) {
            clearScreen();
            System.out.println("====================================================");
            System.out.println("               AUTHOR MANAGEMENT");
            System.out.println("====================================================");
            System.out.println("1. Add New Author");
            System.out.println("2. View All Authors");
            System.out.println("3. Search Author by ID");
            System.out.println("4. Update Author");
            System.out.println("5. Delete Author");
            System.out.println("0. Back to Main Menu");
            System.out.println("====================================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addNewAuthor();
                    break;
                case 2:
                    viewAllAuthors();
                    break;
                case 3:
                    searchAuthorById();
                    break;
                case 4:
                    updateAuthor();
                    break;
                case 5:
                    deleteAuthor();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            pauseForUser();
        }
    }

    private static void addNewAuthor() {
        System.out.println("\n--- Add New Author ---");

        scanner.nextLine(); // consume newline
        System.out.print("Enter author name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Name cannot be empty!");
            return;
        }

        System.out.print("Enter author email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter birth year (optional, press Enter to skip): ");
        String birthYearStr = scanner.nextLine().trim();
        Integer birthYear = null;
        if (!birthYearStr.isEmpty()) {
            try {
                birthYear = Integer.parseInt(birthYearStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid birth year. Setting to null.");
            }
        }

        System.out.print("Enter biography (optional): ");
        String biography = scanner.nextLine().trim();
        if (biography.isEmpty()) biography = null;

        Author author = new Author(name, email, birthYear, biography);

        if (authorDAO.createAuthor(author)) {
            System.out.println("Author added successfully with ID: " + author.getAuthorId());
        } else {
            System.out.println("Failed to add author. Email might already exist.");
        }
    }

    private static void viewAllAuthors() {
        System.out.println("\n--- All Authors ---");
        List<Author> authors = authorDAO.getAllAuthors();

        if (authors.isEmpty()) {
            System.out.println("No authors found.");
            return;
        }

        System.out.printf("%-5s %-30s %-30s %-10s%n", "ID", "Name", "Email", "Birth Year");
        System.out.println("-".repeat(80));

        for (Author author : authors) {
            System.out.printf("%-5d %-30s %-30s %-10s%n",
                    author.getAuthorId(),
                    truncate(author.getName(), 30),
                    truncate(author.getEmail(), 30),
                    author.getBirthYear() != null ? author.getBirthYear().toString() : "N/A");
        }
    }

    private static void searchAuthorById() {
        System.out.println("\n--- Search Author by ID ---");
        int authorId = getIntInput("Enter author ID: ");

        Author author = authorDAO.getAuthorById(authorId);
        if (author != null) {
            displayAuthorDetails(author);
        } else {
            System.out.println("Author not found with ID: " + authorId);
        }
    }

    private static void displayAuthorDetails(Author author) {
        System.out.println("\n--- Author Details ---");
        System.out.println("ID: " + author.getAuthorId());
        System.out.println("Name: " + author.getName());
        System.out.println("Email: " + author.getEmail());
        System.out.println("Birth Year: " + (author.getBirthYear() != null ? author.getBirthYear() : "N/A"));
        System.out.println("Biography: " + (author.getBiography() != null ? author.getBiography() : "N/A"));
    }

    private static void updateAuthor() {
        System.out.println("\n--- Update Author ---");
        int authorId = getIntInput("Enter author ID to update: ");

        Author author = authorDAO.getAuthorById(authorId);
        if (author == null) {
            System.out.println("Author not found!");
            return;
        }

        displayAuthorDetails(author);
        System.out.println("\nEnter new values (press Enter to keep current value):");

        scanner.nextLine(); // consume newline

        System.out.print("New name [" + author.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) author.setName(name);

        System.out.print("New email [" + author.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) author.setEmail(email);

        System.out.print("New birth year [" + author.getBirthYear() + "]: ");
        String birthYearStr = scanner.nextLine().trim();
        if (!birthYearStr.isEmpty()) {
            try {
                author.setBirthYear(Integer.parseInt(birthYearStr));
            } catch (NumberFormatException e) {
                System.out.println("Invalid birth year. Keeping current value.");
            }
        }

        System.out.print("New biography [" + (author.getBiography() != null ? author.getBiography() : "N/A") + "]: ");
        String biography = scanner.nextLine().trim();
        if (!biography.isEmpty()) author.setBiography(biography);

        if (authorDAO.updateAuthor(author)) {
            System.out.println("Author updated successfully!");
        } else {
            System.out.println("Failed to update author.");
        }
    }

    private static void deleteAuthor() {
        System.out.println("\n--- Delete Author ---");
        int authorId = getIntInput("Enter author ID to delete: ");

        Author author = authorDAO.getAuthorById(authorId);
        if (author == null) {
            System.out.println("Author not found!");
            return;
        }

        displayAuthorDetails(author);
        System.out.print("Are you sure you want to delete this author? (y/N): ");
        scanner.nextLine(); // consume newline
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y") || confirmation.equals("yes")) {
            if (authorDAO.deleteAuthor(authorId)) {
                System.out.println("Author deleted successfully!");
            } else {
                System.out.println("Failed to delete author. Books may exist for this author.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    // ==================== BOOK MANAGEMENT ====================

    private static void handleBookManagement() {
        while (true) {
            clearScreen();
            System.out.println("====================================================");
            System.out.println("                BOOK MANAGEMENT");
            System.out.println("====================================================");
            System.out.println("1. Add New Book");
            System.out.println("2. View All Books");
            System.out.println("3. Search Book by ID");
            System.out.println("4. Update Book");
            System.out.println("5. Delete Book");
            System.out.println("6. Update Book Copies");
            System.out.println("0. Back to Main Menu");
            System.out.println("====================================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    addNewBook();
                    break;
                case 2:
                    viewAllBooks();
                    break;
                case 3:
                    searchBookById();
                    break;
                case 4:
                    updateBook();
                    break;
                case 5:
                    deleteBook();
                    break;
                case 6:
                    updateBookCopies();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            pauseForUser();
        }
    }

    private static void addNewBook() {
        System.out.println("\n--- Add New Book ---");

        // First, show available authors
        List<Author> authors = authorDAO.getAllAuthors();
        if (authors.isEmpty()) {
            System.out.println("No authors available. Please add an author first.");
            return;
        }

        System.out.println("Available Authors:");
        for (Author author : authors) {
            System.out.printf("%d. %s%n", author.getAuthorId(), author.getName());
        }

        int authorId = getIntInput("Enter author ID: ");
        Author selectedAuthor = authorDAO.getAuthorById(authorId);
        if (selectedAuthor == null) {
            System.out.println("Invalid author ID!");
            return;
        }

        scanner.nextLine(); // consume newline
        System.out.print("Enter book title: ");
        String title = scanner.nextLine().trim();

        System.out.print("Enter ISBN: ");
        String isbn = scanner.nextLine().trim();

        System.out.print("Enter publication year (optional): ");
        String pubYearStr = scanner.nextLine().trim();
        Integer publicationYear = null;
        if (!pubYearStr.isEmpty()) {
            try {
                publicationYear = Integer.parseInt(pubYearStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid publication year. Setting to null.");
            }
        }

        System.out.print("Enter price (optional): ");
        String priceStr = scanner.nextLine().trim();
        BigDecimal price = null;
        if (!priceStr.isEmpty()) {
            try {
                price = new BigDecimal(priceStr);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Setting to null.");
            }
        }

        int copies = getIntInput("Enter number of copies: ");

        Book book = new Book(title, isbn, publicationYear, price, copies, authorId);

        if (bookDAO.createBook(book)) {
            System.out.println("Book added successfully with ID: " + book.getBookId());
        } else {
            System.out.println("Failed to add book. ISBN might already exist or invalid author ID.");
        }
    }

    private static void viewAllBooks() {
        System.out.println("\n--- All Books ---");
        List<Book> books = bookDAO.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        System.out.printf("%-5s %-30s %-20s %-25s %-8s %-8s%n",
                "ID", "Title", "ISBN", "Author", "Copies", "Price");
        System.out.println("-".repeat(100));

        for (Book book : books) {
            System.out.printf("%-5d %-30s %-20s %-25s %-8d $%-7s%n",
                    book.getBookId(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getIsbn(), 20),
                    truncate(book.getAuthorName() != null ? book.getAuthorName() : "N/A", 25),
                    book.getCopiesAvailable(),
                    book.getPrice() != null ? book.getPrice().toString() : "N/A");
        }
    }

    private static void searchBookById() {
        System.out.println("\n--- Search Book by ID ---");
        int bookId = getIntInput("Enter book ID: ");

        Book book = bookDAO.getBookById(bookId);
        if (book != null) {
            displayBookDetails(book);
        } else {
            System.out.println("Book not found with ID: " + bookId);
        }
    }

    private static void displayBookDetails(Book book) {
        System.out.println("\n--- Book Details ---");
        System.out.println("ID: " + book.getBookId());
        System.out.println("Title: " + book.getTitle());
        System.out.println("ISBN: " + book.getIsbn());
        System.out.println("Author: " + (book.getAuthorName() != null ? book.getAuthorName() : "ID: " + book.getAuthorId()));
        System.out.println("Publication Year: " + (book.getPublicationYear() != null ? book.getPublicationYear() : "N/A"));
        System.out.println("Price: " + (book.getPrice() != null ? "$" + book.getPrice() : "N/A"));
        System.out.println("Copies Available: " + book.getCopiesAvailable());
    }

    private static void updateBook() {
        System.out.println("\n--- Update Book ---");
        int bookId = getIntInput("Enter book ID to update: ");

        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        displayBookDetails(book);
        System.out.println("\nEnter new values (press Enter to keep current value):");

        scanner.nextLine(); // consume newline

        System.out.print("New title [" + book.getTitle() + "]: ");
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) book.setTitle(title);

        System.out.print("New ISBN [" + book.getIsbn() + "]: ");
        String isbn = scanner.nextLine().trim();
        if (!isbn.isEmpty()) book.setIsbn(isbn);

        if (bookDAO.updateBook(book)) {
            System.out.println("Book updated successfully!");
        } else {
            System.out.println("Failed to update book.");
        }
    }

    private static void deleteBook() {
        System.out.println("\n--- Delete Book ---");
        int bookId = getIntInput("Enter book ID to delete: ");

        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        displayBookDetails(book);
        System.out.print("Are you sure you want to delete this book? (y/N): ");
        scanner.nextLine(); // consume newline
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y") || confirmation.equals("yes")) {
            if (bookDAO.deleteBook(bookId)) {
                System.out.println("Book deleted successfully!");
            } else {
                System.out.println("Failed to delete book. Active loans may exist for this book.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void updateBookCopies() {
        System.out.println("\n--- Update Book Copies ---");
        int bookId = getIntInput("Enter book ID: ");

        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("Book not found!");
            return;
        }

        System.out.println("Current copies: " + book.getCopiesAvailable());
        int changeInCopies = getIntInput("Enter change in copies (positive to add, negative to remove): ");

        if (bookDAO.updateCopies(bookId, changeInCopies)) {
            System.out.println("Book copies updated successfully!");
            System.out.println("New copies available: " + (book.getCopiesAvailable() + changeInCopies));
        } else {
            System.out.println("Failed to update book copies.");
        }
    }

    // ==================== MEMBER MANAGEMENT ====================

    private static void handleMemberManagement() {
        while (true) {
            clearScreen();
            System.out.println("====================================================");
            System.out.println("               MEMBER MANAGEMENT");
            System.out.println("====================================================");
            System.out.println("1. Register New Member");
            System.out.println("2. View All Members");
            System.out.println("3. Search Member by ID");
            System.out.println("4. Search Members by Name");
            System.out.println("5. Update Member");
            System.out.println("6. Delete Member");
            System.out.println("7. View Member Loan History");
            System.out.println("0. Back to Main Menu");
            System.out.println("====================================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    registerNewMember();
                    break;
                case 2:
                    viewAllMembers();
                    break;
                case 3:
                    searchMemberById();
                    break;
                case 4:
                    searchMembersByName();
                    break;
                case 5:
                    updateMember();
                    break;
                case 6:
                    deleteMember();
                    break;
                case 7:
                    viewMemberLoanHistory();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            pauseForUser();
        }
    }

    private static void registerNewMember() {
        System.out.println("\n--- Register New Member ---");

        scanner.nextLine(); // consume newline
        System.out.print("Enter member name: ");
        String name = scanner.nextLine().trim();

        System.out.print("Enter email: ");
        String email = scanner.nextLine().trim();

        System.out.print("Enter phone number: ");
        String phone = scanner.nextLine().trim();

        System.out.println("Membership Types:");
        System.out.println("1. BASIC (3 books, 14 days)");
        System.out.println("2. PREMIUM (10 books, 21 days)");
        int typeChoice = getIntInput("Enter membership type (1-2): ");

        Member.MembershipType membershipType = typeChoice == 2 ?
                Member.MembershipType.PREMIUM : Member.MembershipType.BASIC;

        Member member = new Member(name, email, phone, membershipType);

        if (libraryService.registerMember(member)) {
            System.out.println("Member registered successfully with ID: " + member.getMemberId());
            System.out.println("Max books allowed: " + member.getMaxBooksAllowed());
            System.out.println("Loan duration: " + member.getLoanDurationDays() + " days");
        } else {
            System.out.println("Failed to register member. Email might already exist or invalid data.");
        }
    }

    private static void viewAllMembers() {
        System.out.println("\n--- All Members ---");
        List<Member> members = memberDAO.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.printf("%-5s %-25s %-30s %-15s %-12s %-10s%n",
                "ID", "Name", "Email", "Phone", "Join Date", "Type");
        System.out.println("-".repeat(100));

        for (Member member : members) {
            System.out.printf("%-5d %-25s %-30s %-15s %-12s %-10s%n",
                    member.getMemberId(),
                    truncate(member.getName(), 25),
                    truncate(member.getEmail(), 30),
                    truncate(member.getPhone(), 15),
                    member.getJoinDate(),
                    member.getMembershipType());
        }
    }

    private static void searchMemberById() {
        System.out.println("\n--- Search Member by ID ---");
        int memberId = getIntInput("Enter member ID: ");

        Member member = memberDAO.getMemberById(memberId);
        if (member != null) {
            displayMemberDetails(member);
        } else {
            System.out.println("Member not found with ID: " + memberId);
        }
    }

    private static void searchMembersByName() {
        System.out.println("\n--- Search Members by Name ---");
        scanner.nextLine(); // consume newline
        System.out.print("Enter name pattern to search: ");
        String namePattern = scanner.nextLine().trim();

        List<Member> members = memberDAO.searchMembersByName(namePattern);

        if (members.isEmpty()) {
            System.out.println("No members found matching: " + namePattern);
            return;
        }

        System.out.println("Found " + members.size() + " member(s):");
        System.out.printf("%-5s %-25s %-30s %-15s %-10s%n",
                "ID", "Name", "Email", "Phone", "Type");
        System.out.println("-".repeat(90));

        for (Member member : members) {
            System.out.printf("%-5d %-25s %-30s %-15s %-10s%n",
                    member.getMemberId(),
                    truncate(member.getName(), 25),
                    truncate(member.getEmail(), 30),
                    truncate(member.getPhone(), 15),
                    member.getMembershipType());
        }
    }

    private static void displayMemberDetails(Member member) {
        System.out.println("\n--- Member Details ---");
        System.out.println("ID: " + member.getMemberId());
        System.out.println("Name: " + member.getName());
        System.out.println("Email: " + member.getEmail());
        System.out.println("Phone: " + member.getPhone());
        System.out.println("Join Date: " + member.getJoinDate());
        System.out.println("Membership Type: " + member.getMembershipType());
        System.out.println("Max Books Allowed: " + member.getMaxBooksAllowed());
        System.out.println("Loan Duration: " + member.getLoanDurationDays() + " days");

        int activeLoans = memberDAO.getActiveLoanCount(member.getMemberId());
        System.out.println("Current Active Loans: " + activeLoans);
    }

    private static void updateMember() {
        System.out.println("\n--- Update Member ---");
        int memberId = getIntInput("Enter member ID to update: ");

        Member member = memberDAO.getMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }

        displayMemberDetails(member);
        System.out.println("\nEnter new values (press Enter to keep current value):");

        scanner.nextLine(); // consume newline

        System.out.print("New name [" + member.getName() + "]: ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) member.setName(name);

        System.out.print("New email [" + member.getEmail() + "]: ");
        String email = scanner.nextLine().trim();
        if (!email.isEmpty()) member.setEmail(email);

        System.out.print("New phone [" + member.getPhone() + "]: ");
        String phone = scanner.nextLine().trim();
        if (!phone.isEmpty()) member.setPhone(phone);

        if (memberDAO.updateMember(member)) {
            System.out.println("Member updated successfully!");
        } else {
            System.out.println("Failed to update member.");
        }
    }

    private static void deleteMember() {
        System.out.println("\n--- Delete Member ---");
        int memberId = getIntInput("Enter member ID to delete: ");

        Member member = memberDAO.getMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found!");
            return;
        }

        displayMemberDetails(member);
        System.out.print("Are you sure you want to delete this member? (y/N): ");
        scanner.nextLine(); // consume newline
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equals("y") || confirmation.equals("yes")) {
            if (memberDAO.deleteMember(memberId)) {
                System.out.println("Member deleted successfully!");
            } else {
                System.out.println("Failed to delete member. Active loans may exist.");
            }
        } else {
            System.out.println("Deletion cancelled.");
        }
    }

    private static void viewMemberLoanHistory() {
        System.out.println("\n--- Member Loan History ---");
        int memberId = getIntInput("Enter member ID: ");

        LibraryService.MemberLoanSummary summary = libraryService.getMemberLoanSummary(memberId);
        if (summary == null) {
            System.out.println("Member not found!");
            return;
        }

        System.out.println("\n" + summary);

        if (!summary.getLoans().isEmpty()) {
            System.out.println("\nDetailed Loan History:");
            System.out.printf("%-5s %-25s %-12s %-12s %-10s %-10s%n",
                    "ID", "Book", "Loan Date", "Due Date", "Status", "Fine");
            System.out.println("-".repeat(80));

            for (BookLoan loan : summary.getLoans()) {
                System.out.printf("%-5d %-25s %-12s %-12s %-10s $%-9s%n",
                        loan.getLoanId(),
                        truncate(loan.getBookTitle() != null ? loan.getBookTitle() : "N/A", 25),
                        loan.getLoanDate(),
                        loan.getDueDate(),
                        loan.getStatus(),
                        loan.getFineAmount());
            }
        }
    }

    // ==================== LOAN MANAGEMENT ====================

    private static void handleLoanManagement() {
        while (true) {
            clearScreen();
            System.out.println("====================================================");
            System.out.println("                LOAN MANAGEMENT");
            System.out.println("====================================================");
            System.out.println("1. Issue Book");
            System.out.println("2. Return Book");
            System.out.println("3. View Active Loans");
            System.out.println("4. View Overdue Loans");
            System.out.println("5. Search Loan by ID");
            System.out.println("0. Back to Main Menu");
            System.out.println("====================================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    issueBook();
                    break;
                case 2:
                    returnBook();
                    break;
                case 3:
                    viewActiveLoans();
                    break;
                case 4:
                    viewOverdueLoans();
                    break;
                case 5:
                    searchLoanById();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            pauseForUser();
        }
    }

    private static void issueBook() {
        System.out.println("\n--- Issue Book ---");

        // Show available books
        List<Book> availableBooks = bookDAO.getAllBooks().stream()
                .filter(book -> book.getCopiesAvailable() > 0)
                .toList();

        if (availableBooks.isEmpty()) {
            System.out.println("No books available for lending.");
            return;
        }

        System.out.println("Available Books:");
        System.out.printf("%-5s %-30s %-20s %-8s%n", "ID", "Title", "Author", "Copies");
        System.out.println("-".repeat(70));

        for (Book book : availableBooks) {
            System.out.printf("%-5d %-30s %-20s %-8d%n",
                    book.getBookId(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthorName() != null ? book.getAuthorName() : "N/A", 20),
                    book.getCopiesAvailable());
        }

        int bookId = getIntInput("\nEnter book ID to issue: ");

        // Show available members
        List<Member> members = memberDAO.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.println("\nMembers:");
        System.out.printf("%-5s %-25s %-10s %-12s%n", "ID", "Name", "Type", "Active Loans");
        System.out.println("-".repeat(55));

        for (Member member : members) {
            int activeLoans = memberDAO.getActiveLoanCount(member.getMemberId());
            System.out.printf("%-5d %-25s %-10s %-12d%n",
                    member.getMemberId(),
                    truncate(member.getName(), 25),
                    member.getMembershipType(),
                    activeLoans);
        }

        int memberId = getIntInput("\nEnter member ID: ");

        if (libraryService.issueBook(bookId, memberId)) {
            System.out.println("Book issued successfully!");
        } else {
            System.out.println("Failed to issue book. Check availability and member limits.");
        }
    }

    private static void returnBook() {
        System.out.println("\n--- Return Book ---");

        // Show active loans
        List<BookLoan> activeLoans = bookLoanDAO.getActiveLoans();
        if (activeLoans.isEmpty()) {
            System.out.println("No active loans found.");
            return;
        }

        System.out.println("Active Loans:");
        System.out.printf("%-5s %-25s %-20s %-12s %-10s%n",
                "ID", "Book", "Member", "Due Date", "Days Late");
        System.out.println("-".repeat(80));

        for (BookLoan loan : activeLoans) {
            long daysLate = loan.getDaysOverdue();
            System.out.printf("%-5d %-25s %-20s %-12s %-10s%n",
                    loan.getLoanId(),
                    truncate(loan.getBookTitle() != null ? loan.getBookTitle() : "N/A", 25),
                    truncate(loan.getMemberName() != null ? loan.getMemberName() : "N/A", 20),
                    loan.getDueDate(),
                    daysLate > 0 ? daysLate : "On time");
        }

        int loanId = getIntInput("\nEnter loan ID to return: ");

        if (libraryService.returnBook(loanId)) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Failed to return book. Invalid loan ID or book already returned.");
        }
    }

    private static void viewActiveLoans() {
        System.out.println("\n--- Active Loans ---");
        List<BookLoan> activeLoans = bookLoanDAO.getActiveLoans();

        if (activeLoans.isEmpty()) {
            System.out.println("No active loans found.");
            return;
        }

        System.out.printf("%-5s %-25s %-20s %-12s %-12s %-10s%n",
                "ID", "Book", "Member", "Loan Date", "Due Date", "Status");
        System.out.println("-".repeat(90));

        for (BookLoan loan : activeLoans) {
            System.out.printf("%-5d %-25s %-20s %-12s %-12s %-10s%n",
                    loan.getLoanId(),
                    truncate(loan.getBookTitle() != null ? loan.getBookTitle() : "N/A", 25),
                    truncate(loan.getMemberName() != null ? loan.getMemberName() : "N/A", 20),
                    loan.getLoanDate(),
                    loan.getDueDate(),
                    loan.isOverdue() ? "OVERDUE" : "ACTIVE");
        }
    }

    private static void viewOverdueLoans() {
        System.out.println("\n--- Overdue Loans ---");
        List<BookLoan> overdueLoans = bookLoanDAO.getOverdueLoans();

        if (overdueLoans.isEmpty()) {
            System.out.println("No overdue loans found.");
            return;
        }

        System.out.printf("%-5s %-25s %-20s %-12s %-10s %-10s%n",
                "ID", "Book", "Member", "Due Date", "Days Late", "Fine");
        System.out.println("-".repeat(90));

        for (BookLoan loan : overdueLoans) {
            System.out.printf("%-5d %-25s %-20s %-12s %-10d $%-9.2f%n",
                    loan.getLoanId(),
                    truncate(loan.getBookTitle() != null ? loan.getBookTitle() : "N/A", 25),
                    truncate(loan.getMemberName() != null ? loan.getMemberName() : "N/A", 20),
                    loan.getDueDate(),
                    loan.getDaysOverdue(),
                    loan.getFineAmount());
        }
    }

    private static void searchLoanById() {
        System.out.println("\n--- Search Loan by ID ---");
        int loanId = getIntInput("Enter loan ID: ");

        BookLoan loan = bookLoanDAO.getLoanById(loanId);
        if (loan != null) {
            displayLoanDetails(loan);
        } else {
            System.out.println("Loan not found with ID: " + loanId);
        }
    }

    private static void displayLoanDetails(BookLoan loan) {
        System.out.println("\n--- Loan Details ---");
        System.out.println("Loan ID: " + loan.getLoanId());
        System.out.println("Book: " + (loan.getBookTitle() != null ? loan.getBookTitle() : "ID: " + loan.getBookId()));
        System.out.println("Member: " + (loan.getMemberName() != null ? loan.getMemberName() : "ID: " + loan.getMemberId()));
        System.out.println("Loan Date: " + loan.getLoanDate());
        System.out.println("Due Date: " + loan.getDueDate());
        System.out.println("Return Date: " + (loan.getReturnDate() != null ? loan.getReturnDate() : "Not returned"));
        System.out.println("Status: " + loan.getStatus());
        System.out.println("Fine Amount: $" + loan.getFineAmount());

        if (loan.isOverdue() && loan.getStatus() == BookLoan.LoanStatus.ACTIVE) {
            System.out.println("Days Overdue: " + loan.getDaysOverdue());
        }
    }

    // ==================== REPORTS & STATISTICS ====================

    private static void handleReports() {
        while (true) {
            clearScreen();
            System.out.println("====================================================");
            System.out.println("              REPORTS & STATISTICS");
            System.out.println("====================================================");
            System.out.println("1. Library Statistics Overview");
            System.out.println("2. Member Loan Summary");
            System.out.println("3. Book Availability Report");
            System.out.println("4. Overdue Loans Report");
            System.out.println("5. Author-wise Book Count");
            System.out.println("6. Membership Type Distribution");
            System.out.println("0. Back to Main Menu");
            System.out.println("====================================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    showLibraryStatistics();
                    break;
                case 2:
                    showMemberLoanSummary();
                    break;
                case 3:
                    showBookAvailabilityReport();
                    break;
                case 4:
                    showOverdueLoansReport();
                    break;
                case 5:
                    showAuthorWiseBookCount();
                    break;
                case 6:
                    showMembershipDistribution();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            pauseForUser();
        }
    }

    private static void showLibraryStatistics() {
        System.out.println("\n--- Library Statistics Overview ---");
        LibraryService.LibraryStatistics stats = libraryService.getLibraryStatistics();

        System.out.println("====================================================");
        System.out.println("                LIBRARY OVERVIEW");
        System.out.println("====================================================");
        System.out.println("Total Authors: " + stats.getTotalAuthors());
        System.out.println("Total Books: " + stats.getTotalBooks());
        System.out.println("Total Copies: " + stats.getTotalCopies());
        System.out.println("Total Members: " + stats.getTotalMembers());
        System.out.println("Active Loans: " + stats.getActiveLoans());
        System.out.println("Overdue Loans: " + stats.getOverdueLoans());

        if (stats.getTotalMembers() > 0) {
            double loanRatio = (double) stats.getActiveLoans() / stats.getTotalMembers();
            System.out.printf("Average Loans per Member: %.2f%n", loanRatio);
        }

        if (stats.getActiveLoans() > 0) {
            double overduePercentage = (double) stats.getOverdueLoans() / stats.getActiveLoans() * 100;
            System.out.printf("Overdue Percentage: %.2f%%%n", overduePercentage);
        }
        System.out.println("====================================================");
    }

    private static void showMemberLoanSummary() {
        System.out.println("\n--- Member Loan Summary ---");
        List<Member> members = memberDAO.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        System.out.printf("%-5s %-25s %-10s %-12s %-10s%n",
                "ID", "Name", "Type", "Active Loans", "Max Allowed");
        System.out.println("-".repeat(70));

        for (Member member : members) {
            int activeLoans = memberDAO.getActiveLoanCount(member.getMemberId());
            System.out.printf("%-5d %-25s %-10s %-12d %-10d%n",
                    member.getMemberId(),
                    truncate(member.getName(), 25),
                    member.getMembershipType(),
                    activeLoans,
                    member.getMaxBooksAllowed());
        }
    }

    private static void showBookAvailabilityReport() {
        System.out.println("\n--- Book Availability Report ---");
        List<Book> books = bookDAO.getAllBooks();

        if (books.isEmpty()) {
            System.out.println("No books found.");
            return;
        }

        int availableBooks = 0;
        int outOfStockBooks = 0;

        System.out.printf("%-5s %-30s %-20s %-10s %-8s%n",
                "ID", "Title", "Author", "Copies", "Status");
        System.out.println("-".repeat(80));

        for (Book book : books) {
            String status = book.getCopiesAvailable() > 0 ? "Available" : "Out of Stock";
            if (book.getCopiesAvailable() > 0) {
                availableBooks++;
            } else {
                outOfStockBooks++;
            }

            System.out.printf("%-5d %-30s %-20s %-10d %-8s%n",
                    book.getBookId(),
                    truncate(book.getTitle(), 30),
                    truncate(book.getAuthorName() != null ? book.getAuthorName() : "N/A", 20),
                    book.getCopiesAvailable(),
                    status);
        }

        System.out.println("\nSummary:");
        System.out.println("Available Books: " + availableBooks);
        System.out.println("Out of Stock Books: " + outOfStockBooks);
    }

    private static void showOverdueLoansReport() {
        System.out.println("\n--- Overdue Loans Report ---");
        List<BookLoan> overdueLoans = bookLoanDAO.getOverdueLoans();

        if (overdueLoans.isEmpty()) {
            System.out.println("No overdue loans found.");
            return;
        }

        BigDecimal totalFines = BigDecimal.ZERO;

        System.out.printf("%-5s %-25s %-20s %-12s %-10s %-10s%n",
                "ID", "Book", "Member", "Due Date", "Days Late", "Fine");
        System.out.println("-".repeat(90));

        for (BookLoan loan : overdueLoans) {
            totalFines = totalFines.add(loan.getFineAmount());
            System.out.printf("%-5d %-25s %-20s %-12s %-10d $%-9.2f%n",
                    loan.getLoanId(),
                    truncate(loan.getBookTitle() != null ? loan.getBookTitle() : "N/A", 25),
                    truncate(loan.getMemberName() != null ? loan.getMemberName() : "N/A", 20),
                    loan.getDueDate(),
                    loan.getDaysOverdue(),
                    loan.getFineAmount());
        }

        System.out.println("\nTotal Overdue Loans: " + overdueLoans.size());
        System.out.printf("Total Fines: $%.2f%n", totalFines);
    }

    private static void showAuthorWiseBookCount() {
        System.out.println("\n--- Author-wise Book Count ---");
        List<Author> authors = authorDAO.getAllAuthors();
        List<Book> books = bookDAO.getAllBooks();

        if (authors.isEmpty()) {
            System.out.println("No authors found.");
            return;
        }

        System.out.printf("%-5s %-30s %-12s %-15s%n",
                "ID", "Author Name", "Book Count", "Total Copies");
        System.out.println("-".repeat(70));

        for (Author author : authors) {
            int bookCount = 0;
            int totalCopies = 0;

            for (Book book : books) {
                if (book.getAuthorId() == author.getAuthorId()) {
                    bookCount++;
                    totalCopies += book.getCopiesAvailable();
                }
            }

            System.out.printf("%-5d %-30s %-12d %-15d%n",
                    author.getAuthorId(),
                    truncate(author.getName(), 30),
                    bookCount,
                    totalCopies);
        }
    }

    private static void showMembershipDistribution() {
        System.out.println("\n--- Membership Type Distribution ---");
        List<Member> members = memberDAO.getAllMembers();

        if (members.isEmpty()) {
            System.out.println("No members found.");
            return;
        }

        int basicCount = 0;
        int premiumCount = 0;

        for (Member member : members) {
            if (member.getMembershipType() == Member.MembershipType.BASIC) {
                basicCount++;
            } else {
                premiumCount++;
            }
        }

        int total = members.size();
        double basicPercentage = (double) basicCount / total * 100;
        double premiumPercentage = (double) premiumCount / total * 100;

        System.out.println("====================================================");
        System.out.println("           MEMBERSHIP DISTRIBUTION");
        System.out.println("====================================================");
        System.out.printf("Basic Members: %d (%.1f%%)%n", basicCount, basicPercentage);
        System.out.printf("Premium Members: %d (%.1f%%)%n", premiumCount, premiumPercentage);
        System.out.println("Total Members: " + total);
        System.out.println("====================================================");
    }

    // ==================== SYSTEM OPERATIONS ====================

    private static void handleSystemOperations() {
        while (true) {
            clearScreen();
            System.out.println("====================================================");
            System.out.println("              SYSTEM OPERATIONS");
            System.out.println("====================================================");
            System.out.println("1. Process Daily Operations");
            System.out.println("2. Database Connection Test");
            System.out.println("3. Initialize Sample Data");
            System.out.println("4. Clear All Data (Dangerous!)");
            System.out.println("5. Backup Database");
            System.out.println("0. Back to Main Menu");
            System.out.println("====================================================");

            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    processDailyOperations();
                    break;
                case 2:
                    testDatabaseConnection();
                    break;
                case 3:
                    initializeSampleData();
                    break;
                case 4:
                    clearAllData();
                    break;
                case 5:
                    System.out.println("Database backup feature not implemented yet.");
                    break;
                case 0:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }

            pauseForUser();
        }
    }

    private static void processDailyOperations() {
        System.out.println("\n--- Processing Daily Operations ---");
        LibraryService.DailyOperationResult result = libraryService.processDailyOperations();

        System.out.println("====================================================");
        System.out.println("           DAILY OPERATIONS RESULT");
        System.out.println("====================================================");
        System.out.println("Loans marked as overdue: " + result.getOverdueLoansUpdated());
        System.out.println("Total overdue loans: " + result.getTotalOverdueLoans());
        System.out.printf("Total fines accrued: $%.2f%n", result.getTotalFinesAccrued());
        System.out.println("====================================================");
    }

    private static void testDatabaseConnection() {
        System.out.println("\n--- Testing Database Connection ---");
        DatabaseConnection.testConnection();
    }

    private static void initializeSampleData() {
        System.out.println("\n--- Initialize Sample Data ---");
        System.out.print("This will add sample authors, books, and members. Continue? (y/N): ");
        scanner.nextLine(); // consume newline
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (!confirmation.equals("y") && !confirmation.equals("yes")) {
            System.out.println("Operation cancelled.");
            return;
        }

        // Create sample authors
        Author[] sampleAuthors = {
                new Author("J.K. Rowling", "jk.rowling@example.com", 1965, "British author of Harry Potter series"),
                new Author("George Orwell", "g.orwell@example.com", 1903, "English novelist and essayist"),
                new Author("Agatha Christie", "a.christie@example.com", 1890, "English writer of detective fiction")
        };

        System.out.println("Creating sample authors...");
        for (Author author : sampleAuthors) {
            if (authorDAO.createAuthor(author)) {
                System.out.println("Created: " + author.getName());
            }
        }

        // Create sample books
        List<Author> authors = authorDAO.getAllAuthors();
        if (!authors.isEmpty()) {
            System.out.println("Creating sample books...");

            Book[] sampleBooks = {
                    new Book("Harry Potter and the Philosopher's Stone", "978-0-7475-3269-9", 1997,
                            new BigDecimal("15.99"), 3, authors.get(0).getAuthorId()),
                    new Book("1984", "978-0-452-28423-4", 1949,
                            new BigDecimal("12.99"), 2, authors.size() > 1 ? authors.get(1).getAuthorId() : authors.get(0).getAuthorId()),
                    new Book("Murder on the Orient Express", "978-0-06-207350-4", 1934,
                            new BigDecimal("13.99"), 4, authors.size() > 2 ? authors.get(2).getAuthorId() : authors.get(0).getAuthorId())
            };

            for (Book book : sampleBooks) {
                if (bookDAO.createBook(book)) {
                    System.out.println("Created: " + book.getTitle());
                }
            }
        }

        // Create sample members
        System.out.println("Creating sample members...");
        Member[] sampleMembers = {
                new Member("John Doe", "john.doe@email.com", "123-456-7890", Member.MembershipType.BASIC),
                new Member("Jane Smith", "jane.smith@email.com", "098-765-4321", Member.MembershipType.PREMIUM),
                new Member("Bob Johnson", "bob.johnson@email.com", "555-555-5555", Member.MembershipType.BASIC)
        };

        for (Member member : sampleMembers) {
            if (libraryService.registerMember(member)) {
                System.out.println("Created: " + member.getName());
            }
        }

        System.out.println("Sample data initialization completed!");
    }

    private static void clearAllData() {
        System.out.println("\n--- Clear All Data ---");
        System.out.println("WARNING: This will delete ALL data from the database!");
        System.out.print("Type 'DELETE ALL DATA' to confirm: ");
        scanner.nextLine(); // consume newline
        String confirmation = scanner.nextLine().trim();

        if (!confirmation.equals("DELETE ALL DATA")) {
            System.out.println("Operation cancelled.");
            return;
        }

        System.out.println("This feature is not implemented for safety reasons.");
        System.out.println("Please use database administration tools to clear data.");
    }

    // ==================== DEBUG METHODS ====================

    private static void debugLoanIssues() {
        System.out.println("\n--- Debug Loan Issues ---");

        try {
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("Database connection successful");

            // Check if tables exist and have data
            String[] queries = {
                    "SELECT COUNT(*) as count, 'books' as table_name FROM books",
                    "SELECT COUNT(*) as count, 'members' as table_name FROM members",
                    "SELECT COUNT(*) as count, 'book_loans' as table_name FROM book_loans",
                    "SELECT COUNT(*) as active_loans, 'active_loans' as table_name FROM book_loans WHERE status IN ('ACTIVE', 'OVERDUE') AND return_date IS NULL",
                    "SELECT COUNT(*) as overdue_loans, 'overdue_loans' as table_name FROM book_loans WHERE status IN ('ACTIVE', 'OVERDUE') AND due_date < CURRENT_DATE AND return_date IS NULL"
            };

            for (String query : queries) {
                PreparedStatement pstmt = conn.prepareStatement(query);
                ResultSet rs = pstmt.executeQuery();
                if (rs.next()) {
                    System.out.println("Table " + rs.getString(2) + ": " + rs.getInt(1) + " records");
                }
                rs.close();
                pstmt.close();
            }

            // Test the specific query that's failing
            System.out.println("\n--- Testing Active Loans Query ---");
            String testQuery = "SELECT bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type " +
                    "FROM book_loans bl " +
                    "JOIN books b ON bl.book_id = b.book_id " +
                    "JOIN members m ON bl.member_id = m.member_id " +
                    "WHERE bl.status IN ('ACTIVE', 'OVERDUE') AND bl.return_date IS NULL " +
                    "ORDER BY bl.due_date";

            PreparedStatement pstmt = conn.prepareStatement(testQuery);
            ResultSet rs = pstmt.executeQuery();

            int count = 0;
            while (rs.next()) {
                count++;
                System.out.printf("Loan %d: Book='%s', Member='%s', Status='%s', Due='%s'%n",
                        rs.getInt("loan_id"),
                        rs.getString("book_title"),
                        rs.getString("member_name"),
                        rs.getString("status"),
                        rs.getDate("due_date"));
            }
            System.out.println("Total active loans found: " + count);

            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void forceUpdateOverdueLoans() {
        System.out.println("\n--- Force Update Overdue Loans ---");

        try {
            Connection conn = DatabaseConnection.getConnection();

            // First, show current state
            System.out.println("Current loan status:");
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT loan_id, status, due_date, fine_amount, " +
                            "DATEDIFF(CURRENT_DATE, due_date) as days_overdue " +
                            "FROM book_loans WHERE return_date IS NULL"
            );
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                System.out.printf("Loan %d: Status=%s, Due=%s, Days Overdue=%d, Fine=$%.2f%n",
                        rs.getInt("loan_id"),
                        rs.getString("status"),
                        rs.getDate("due_date"),
                        rs.getInt("days_overdue"),
                        rs.getBigDecimal("fine_amount"));
            }
            rs.close();
            pstmt.close();

            // Now update overdue loans
            String updateQuery =
                    "UPDATE book_loans SET " +
                            "fine_amount = CASE " +
                            "  WHEN due_date < CURRENT_DATE AND status IN ('ACTIVE', 'OVERDUE') " +
                            "  THEN DATEDIFF(CURRENT_DATE, due_date) * 1.00 " +
                            "  ELSE fine_amount " +
                            "END, " +
                            "status = CASE " +
                            "  WHEN due_date < CURRENT_DATE AND status = 'ACTIVE' " +
                            "  THEN 'OVERDUE' " +
                            "  ELSE status " +
                            "END " +
                            "WHERE status IN ('ACTIVE', 'OVERDUE') AND return_date IS NULL";

            pstmt = conn.prepareStatement(updateQuery);
            int updateCount = pstmt.executeUpdate();
            System.out.println("Updated " + updateCount + " loan records");

            pstmt.close();
            conn.close();

            System.out.println("Overdue loans update completed");

        } catch (SQLException e) {
            System.err.println("Error updating overdue loans: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void showRawLoanData() {
        System.out.println("\n--- Raw Loan Data ---");

        try {
            Connection conn = DatabaseConnection.getConnection();
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT bl.*, b.title, m.name " +
                            "FROM book_loans bl " +
                            "LEFT JOIN books b ON bl.book_id = b.book_id " +
                            "LEFT JOIN members m ON bl.member_id = m.member_id " +
                            "ORDER BY bl.loan_id"
            );
            ResultSet rs = pstmt.executeQuery();

            System.out.printf("%-5s %-10s %-10s %-12s %-12s %-12s %-10s %-10s %-20s %-15s%n",
                    "ID", "Book ID", "Member ID", "Loan Date", "Due Date", "Return Date", "Status", "Fine", "Book Title", "Member Name");
            System.out.println("-".repeat(140));

            while (rs.next()) {
                System.out.printf("%-5d %-10d %-10d %-12s %-12s %-12s %-10s $%-9.2f %-20s %-15s%n",
                        rs.getInt("loan_id"),
                        rs.getInt("book_id"),
                        rs.getInt("member_id"),
                        rs.getDate("loan_date"),
                        rs.getDate("due_date"),
                        rs.getDate("return_date"),
                        rs.getString("status"),
                        rs.getBigDecimal("fine_amount"),
                        truncate(rs.getString("title") != null ? rs.getString("title") : "NULL", 20),
                        truncate(rs.getString("name") != null ? rs.getString("name") : "NULL", 15));
            }

            rs.close();
            pstmt.close();
            conn.close();

        } catch (SQLException e) {
            System.err.println("Error showing raw loan data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void diagnoseDatabaseConstraints() {
        System.out.println("\n--- Diagnose Database Constraints ---");

        try {
            Connection conn = DatabaseConnection.getConnection();

            // Check book copies and active loans
            System.out.println("Checking book inventory vs active loans:");
            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT " +
                            "  b.book_id, " +
                            "  b.title, " +
                            "  b.copies_available, " +
                            "  COUNT(bl.loan_id) as active_loans, " +
                            "  (b.copies_available + COUNT(bl.loan_id)) as total_should_be " +
                            "FROM books b " +
                            "LEFT JOIN book_loans bl ON b.book_id = bl.book_id " +
                            "  AND bl.status IN ('ACTIVE', 'OVERDUE') " +
                            "  AND bl.return_date IS NULL " +
                            "GROUP BY b.book_id, b.title, b.copies_available " +
                            "ORDER BY b.book_id"
            );
            ResultSet rs = pstmt.executeQuery();

            System.out.printf("%-5s %-30s %-10s %-12s %-15s%n",
                    "ID", "Title", "Available", "Active Loans", "Total Copies");
            System.out.println("-".repeat(80));

            while (rs.next()) {
                System.out.printf("%-5d %-30s %-10d %-12d %-15d%n",
                        rs.getInt("book_id"),
                        truncate(rs.getString("title"), 30),
                        rs.getInt("copies_available"),
                        rs.getInt("active_loans"),
                        rs.getInt("total_should_be"));
            }
            rs.close();
            pstmt.close();

            // Check specific loan that's failing
            System.out.println("\nChecking loan ID 5 specifically:");
            pstmt = conn.prepareStatement(
                    "SELECT " +
                            "  bl.loan_id, " +
                            "  bl.book_id, " +
                            "  b.title, " +
                            "  b.copies_available, " +
                            "  bl.status, " +
                            "  bl.return_date " +
                            "FROM book_loans bl " +
                            "JOIN books b ON bl.book_id = b.book_id " +
                            "WHERE bl.loan_id = 5"
            );
            rs = pstmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                int currentCopies = rs.getInt("copies_available");
                System.out.printf("Loan 5: Book ID=%d, Title='%s', Current Copies=%d%n",
                        bookId, rs.getString("title"), currentCopies);
                System.out.println("Attempting to return this book would set copies to: " + (currentCopies + 1));
            }
            rs.close();
            pstmt.close();

            // Try to identify the constraint
            System.out.println("\nChecking for constraints on books table:");
            try {
                pstmt = conn.prepareStatement(
                        "SELECT CONSTRAINT_NAME, CHECK_CLAUSE " +
                                "FROM information_schema.CHECK_CONSTRAINTS " +
                                "WHERE TABLE_NAME = 'books' AND TABLE_SCHEMA = DATABASE()"
                );
                rs = pstmt.executeQuery();

                boolean hasConstraints = false;
                while (rs.next()) {
                    hasConstraints = true;
                    System.out.println("Constraint: " + rs.getString("CONSTRAINT_NAME"));
                    System.out.println("Rule: " + rs.getString("CHECK_CLAUSE"));
                }

                if (!hasConstraints) {
                    System.out.println("No check constraints found - the constraint might be at column level");
                }

                rs.close();
                pstmt.close();
            } catch (SQLException e) {
                System.out.println("Could not check constraints (might not be supported): " + e.getMessage());
            }

            // Suggest solution
            System.out.println("\n--- SOLUTION SUGGESTIONS ---");
            System.out.println("1. The constraint 'chk_total_copies' is preventing the return");
            System.out.println("2. This might be due to a maximum copies limit being exceeded");
            System.out.println("3. Try the 'Fix Constraint Issue' option below");

            System.out.print("\nWould you like to try fixing the constraint issue? (y/N): ");
            Scanner tempScanner = new Scanner(System.in);
            String response = tempScanner.nextLine().trim().toLowerCase();

            if (response.equals("y") || response.equals("yes")) {
                fixConstraintIssue(conn);
            }

            conn.close();

        } catch (SQLException e) {
            System.err.println("Error diagnosing constraints: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void fixConstraintIssue(Connection conn) {
        System.out.println("\n--- Attempting to Fix Constraint Issue ---");

        try {
            // First, let's see what the current constraint allows
            System.out.println("Checking current book data for book ID in loan 5...");

            PreparedStatement pstmt = conn.prepareStatement(
                    "SELECT b.book_id, b.title, b.copies_available " +
                            "FROM books b " +
                            "JOIN book_loans bl ON b.book_id = bl.book_id " +
                            "WHERE bl.loan_id = 5"
            );
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                int bookId = rs.getInt("book_id");
                int currentCopies = rs.getInt("copies_available");
                String title = rs.getString("title");

                System.out.printf("Book: %s (ID: %d), Current copies: %d%n", title, bookId, currentCopies);

                // The issue might be that the constraint has a maximum limit
                // Let's try to temporarily increase the limit or modify the data

                System.out.println("Attempting to modify the constraint...");

                try {
                    // Try to drop the problematic constraint
                    PreparedStatement dropStmt = conn.prepareStatement("ALTER TABLE books DROP CHECK chk_total_copies");
                    dropStmt.executeUpdate();
                    dropStmt.close();
                    System.out.println("Dropped problematic constraint");

                    // Add a more reasonable constraint
                    PreparedStatement addStmt = conn.prepareStatement(
                            "ALTER TABLE books ADD CONSTRAINT chk_total_copies CHECK (copies_available >= 0 AND copies_available <= 1000)"
                    );
                    addStmt.executeUpdate();
                    addStmt.close();
                    System.out.println("Added new constraint allowing up to 1000 copies");

                } catch (SQLException e) {
                    System.out.println("Could not modify constraint: " + e.getMessage());
                    System.out.println("You may need to run this SQL manually:");
                    System.out.println("ALTER TABLE books DROP CHECK chk_total_copies;");
                    System.out.println("ALTER TABLE books ADD CONSTRAINT chk_total_copies CHECK (copies_available >= 0 AND copies_available <= 1000);");
                }
            }

            rs.close();
            pstmt.close();

        } catch (SQLException e) {
            System.err.println("Error fixing constraint: " + e.getMessage());
            System.out.println("\nManual fix required:");
            System.out.println("1. Connect to your MySQL database");
            System.out.println("2. Run: ALTER TABLE books DROP CHECK chk_total_copies;");
            System.out.println("3. Run: ALTER TABLE books ADD CONSTRAINT chk_total_copies CHECK (copies_available >= 0 AND copies_available <= 1000);");
        }
    }

    // ==================== UTILITY METHODS ====================

    private static void clearScreen() {
        // Simple way to clear screen for better UX
        // for (int i = 0; i < 50; i++) {
        //   System.out.println();
        // }

        System.out.println("\n");
    }

    private static void pauseForUser() {
        System.out.println("\nPress Enter to continue...");
        try {
            System.in.read();
        } catch (Exception e) {
            // Ignore
        }
    }

    private static int getIntInput(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a valid number: ");
            scanner.next();
        }
        int value = scanner.nextInt();
        return value;
    }

    private static String truncate(String str, int maxLength) {
        if (str == null) return "N/A";
        if (str.length() <= maxLength) return str;
        return str.substring(0, maxLength - 3) + "...";
    }
}