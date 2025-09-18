package Day_27To31.library_management_system.src.main.java.com.library.test;


import Day_27To31.library_management_system.src.main.java.com.library.dao.BookLoanDAO;
import Day_27To31.library_management_system.src.main.java.com.library.model.BookLoan;
import Day_27To31.library_management_system.src.main.java.com.library.model.Member;
import Day_27To31.library_management_system.src.main.java.com.library.service.LibraryService;

import java.util.List;
import java.util.Scanner;

public class Phase2TestApp {
    private static LibraryService libraryService = new LibraryService();
    private static BookLoanDAO bookLoanDAO = new BookLoanDAO();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Library Management System - Phase 2 Testing ===");

        while (true) {
            printMenu();
            int choice = getIntInput("Enter your choice: ");

            switch (choice) {
                case 1:
                    testMemberRegistration();
                    break;
                case 2:
                    testBookIssuing();
                    break;
                case 3:
                    testBookReturn();
                    break;
                case 4:
                    viewActiveLoans();
                    break;
                case 5:
                    viewOverdueLoans();
                    break;
                case 6:
                    processDailyOperations();
                    break;
                case 7:
                    viewMemberHistory();
                    break;
                case 8:
                    viewLibraryStatistics();
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
        System.out.println("\n--- Phase 2 Menu ---");
        System.out.println("1. Test Member Registration");
        System.out.println("2. Test Book Issuing");
        System.out.println("3. Test Book Return");
        System.out.println("4. View Active Loans");
        System.out.println("5. View Overdue Loans");
        System.out.println("6. Process Daily Operations");
        System.out.println("7. View Member History");
        System.out.println("8. View Library Statistics");
        System.out.println("0. Exit");
    }

    private static void testMemberRegistration() {
        System.out.println("\n--- Testing Member Registration ---");

        // Test basic member
        Member basicMember = new Member("Srikanth Manga", "srikanth@google.com", "9000321013", Member.MembershipType.BASIC);
        System.out.println("1. Registering basic member: " + basicMember.getName());
        if (libraryService.registerMember(basicMember)) {
            System.out.println("Basic member registered with ID: " + basicMember.getMemberId());
            System.out.println("Max books allowed: " + basicMember.getMaxBooksAllowed());
            System.out.println("Loan duration: " + basicMember.getLoanDurationDays() + " days");
        }

        // Test premium member
        Member premiumMember = new Member("Srikanth Manga", "srikanth@google.com", "0987654321", Member.MembershipType.PREMIUM);
        System.out.println("\n2. Registering premium member: " + premiumMember.getName());
        if (libraryService.registerMember(premiumMember)) {
            System.out.println("✓ Premium member registered with ID: " + premiumMember.getMemberId());
            System.out.println("  Max books allowed: " + premiumMember.getMaxBooksAllowed());
            System.out.println("  Loan duration: " + premiumMember.getLoanDurationDays() + " days");
        }

        // Test duplicate email
        Member duplicateMember = new Member("Srikanth Manga", "srikanth@google.com", "555-555-5555", Member.MembershipType.BASIC);
        System.out.println("\n3. Testing duplicate email: " + duplicateMember.getEmail());
        if (!libraryService.registerMember(duplicateMember)) {
            System.out.println("Correctly rejected duplicate email");
        }
    }

    private static void testBookIssuing() {
        System.out.println("\n--- Testing Book Issuing ---");

        // Get available books and members for testing
        LibraryService.LibraryStatistics stats = libraryService.getLibraryStatistics();
        if (stats.getTotalBooks() == 0 || stats.getTotalMembers() == 0) {
            System.out.println("Need books and members in the system first. Run Phase 1 tests.");
            return;
        }

        // Test normal book issuing
        System.out.println("1. Testing normal book issue (Book ID: 1, Member ID: 1)");
        boolean issued = libraryService.issueBook(1, 1);
        if (issued) {
            System.out.println("Book issued successfully");
        } else {
            System.out.println("Failed to issue book");
        }

        // Test issuing same book again (should fail due to reduced copies)
        System.out.println("\n2. Testing multiple copies of same book");
        boolean issued2 = libraryService.issueBook(1, 1);
        if (!issued2) {
            System.out.println("Correctly handled limited copies");
        }

        // Test member loan limit
        System.out.println("\n3. Testing member loan limits");
        // Issue books up to limit for basic member
        for (int bookId = 2; bookId <= 4; bookId++) {
            boolean result = libraryService.issueBook(bookId, 1);
            System.out.println("  Book " + bookId + " issue result: " + (result ? "Success" : "Failed"));
        }
    }

    private static void testBookReturn() {
        System.out.println("\n--- Testing Book Return ---");

        // Get active loans
        List<BookLoan> activeLoans = bookLoanDAO.getActiveLoans();
        if (activeLoans.isEmpty()) {
            System.out.println("No active loans to return. Issue some books first.");
            return;
        }

        BookLoan loanToReturn = activeLoans.get(0);
        System.out.println("Returning loan: " + loanToReturn);

        boolean returned = libraryService.returnBook(loanToReturn.getLoanId());
        if (returned) {
            System.out.println("Book returned successfully");
        } else {
            System.out.println("Failed to return book");
        }
    }

    private static void viewActiveLoans() {
        System.out.println("\n--- Active Loans ---");
        List<BookLoan> activeLoans = bookLoanDAO.getActiveLoans();

        if (activeLoans.isEmpty()) {
            System.out.println("No active loans.");
        } else {
            System.out.printf("%-5s %-20s %-20s %-12s %-12s%n",
                    "ID", "Book", "Member", "Due Date", "Status");
            System.out.println("-".repeat(80));

            for (BookLoan loan : activeLoans) {
                System.out.printf("%-5d %-20s %-20s %-12s %-12s%n",
                        loan.getLoanId(),
                        loan.getBookTitle() != null ? loan.getBookTitle().substring(0, Math.min(20, loan.getBookTitle().length())) : "N/A",
                        loan.getMemberName() != null ? loan.getMemberName().substring(0, Math.min(20, loan.getMemberName().length())) : "N/A",
                        loan.getDueDate(),
                        loan.getStatus());
            }
        }
    }

    private static void viewOverdueLoans() {
        System.out.println("\n--- Overdue Loans ---");
        List<BookLoan> overdueLoans = bookLoanDAO.getOverdueLoans();

        if (overdueLoans.isEmpty()) {
            System.out.println("No overdue loans.");
        } else {
            System.out.printf("%-5s %-20s %-20s %-12s %-10s %-10s%n",
                    "ID", "Book", "Member", "Due Date", "Days Late", "Fine");
            System.out.println("-".repeat(90));

            for (BookLoan loan : overdueLoans) {
                System.out.printf("%-5d %-20s %-20s %-12s %-10d $%-9.2f%n",
                        loan.getLoanId(),
                        loan.getBookTitle() != null ? loan.getBookTitle().substring(0, Math.min(20, loan.getBookTitle().length())) : "N/A",
                        loan.getMemberName() != null ? loan.getMemberName().substring(0, Math.min(20, loan.getMemberName().length())) : "N/A",
                        loan.getDueDate(),
                        loan.getDaysOverdue(),
                        loan.getFineAmount());
            }
        }
    }

    private static void processDailyOperations() {
        System.out.println("\n--- Processing Daily Operations ---");
        LibraryService.DailyOperationResult result = libraryService.processDailyOperations();
        System.out.println(result);
    }

    private static void viewMemberHistory() {
        System.out.println("\n--- Member Loan History ---");
        System.out.print("Enter member ID: ");
        int memberId = getIntInput("");

        LibraryService.MemberLoanSummary summary = libraryService.getMemberLoanSummary(memberId);
        if (summary != null) {
            System.out.println("\n" + summary);
            System.out.println("\nLoan History:");
            for (BookLoan loan : summary.getLoans()) {
                System.out.println("  " + loan);
            }
        } else {
            System.out.println("Member not found.");
        }
    }

    private static void viewLibraryStatistics() {
        System.out.println("\n--- Library Statistics ---");
        LibraryService.LibraryStatistics stats = libraryService.getLibraryStatistics();
        System.out.println(stats);
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