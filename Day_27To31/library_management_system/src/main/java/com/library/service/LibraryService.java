package Day_27To31.library_management_system.src.main.java.com.library.service;

import Day_27To31.library_management_system.src.main.java.com.library.dao.AuthorDAO;
import Day_27To31.library_management_system.src.main.java.com.library.dao.BookDAO;
import Day_27To31.library_management_system.src.main.java.com.library.dao.BookLoanDAO;
import Day_27To31.library_management_system.src.main.java.com.library.dao.MemberDAO;
import Day_27To31.library_management_system.src.main.java.com.library.model.Author;
import Day_27To31.library_management_system.src.main.java.com.library.model.Book;
import Day_27To31.library_management_system.src.main.java.com.library.model.BookLoan;
import Day_27To31.library_management_system.src.main.java.com.library.model.Member;

import java.math.BigDecimal;
import java.util.List;

public class LibraryService {
    private AuthorDAO authorDAO;
    private BookDAO bookDAO;
    private MemberDAO memberDAO;
    private BookLoanDAO bookLoanDAO;

    public LibraryService() {
        this.authorDAO = new AuthorDAO();
        this.bookDAO = new BookDAO();
        this.memberDAO = new MemberDAO();
        this.bookLoanDAO = new BookLoanDAO();
    }

    public boolean registerMember(Member member) {
        // validate email format
        if (!isValidEmail(member.getEmail())) {
            System.out.println("Invalid email format");
            return false;
        }

        // validate phone number
        if (!isValidPhone(member.getPhone())) {
            System.out.println("Invalid phone number format");
            return false;
        }

        return memberDAO.createMember(member);
    }

    public boolean issueBook(int bookId, int memberId) {
        // Get member details for loan duration
        Member member = memberDAO.getMemberById(memberId);
        if (member == null) {
            System.out.println("Member not found");
            return false;
        }

        // Get book details
        Book book = bookDAO.getBookById(bookId);
        if (book == null) {
            System.out.println("Book not found");
            return false;
        }

        // Check if book is available
        if (book.getCopiesAvailable() <= 0) {
            System.out.println("Book is not available");
            return false;
        }

        // Check member's current loan count against limit
        int currentLoans = memberDAO.getActiveLoanCount(memberId);
        int maxAllowed = member.getMaxBooksAllowed();

        if (currentLoans >= maxAllowed) {
            System.out.println("Member has reached maximum loan limit (" + maxAllowed + " books)");
            return false;
        }

        // Issue book with appropriate loan duration
        int loanDuration = member.getLoanDurationDays();
        return bookLoanDAO.issueBook(bookId, memberId, loanDuration);
    }

    public boolean returnBook(int loanId) {
        return bookLoanDAO.returnBook(loanId);
    }

    public MemberLoanSummary getMemberLoanSummary(int memberId) {
        Member member = memberDAO.getMemberById(memberId);
        if (member == null) return null;

        List<BookLoan> loans = bookLoanDAO.getMemberLoans(memberId);
        return new MemberLoanSummary(member, loans);
    }

    public DailyOperationResult processDailyOperations() {
        System.out.println("Running daily operations...");

        int overdueUpdated = bookLoanDAO.updateOverdueLoans();
        List<BookLoan> currentOverdue = bookLoanDAO.getOverdueLoans();

        BigDecimal totalFines = currentOverdue.stream()
                .map(BookLoan::getFineAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new DailyOperationResult(overdueUpdated, currentOverdue.size(), totalFines);
    }

    public List<Book> searchBooks(String searchTerm) {
        // This would involve more complex search logic
        // For now, returning all books (implement proper search in Phase 3)
        return bookDAO.getAllBooks();
    }

    public LibraryStatistics getLibraryStatistics() {
        List<Author> authors = authorDAO.getAllAuthors();
        List<Book> books = bookDAO.getAllBooks();
        List<Member> members = memberDAO.getAllMembers();
        List<BookLoan> activeLoans = bookLoanDAO.getActiveLoans();
        List<BookLoan> overdueLoans = bookLoanDAO.getOverdueLoans();

        int totalCopies = books.stream().mapToInt(Book::getCopiesAvailable).sum();

        return new LibraryStatistics(
                authors.size(),
                books.size(),
                totalCopies,
                members.size(),
                activeLoans.size(),
                overdueLoans.size()
        );
    }

    private boolean isValidEmail(String email) {
        return email != null && email.contains("@") && email.contains(".");
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.replaceAll("[^0-9]", "").length() >= 10;
    }

    public static class MemberLoanSummary {
        private Member member;
        private List<BookLoan> loans;
        private int totalLoans;
        private int activeLoans;
        private BigDecimal totalFines;

        public MemberLoanSummary(Member member, List<BookLoan> loans) {
            this.member = member;
            this.loans = loans;
            this.totalLoans = loans.size();
            this.activeLoans = (int) loans.stream()
                    .filter(loan -> loan.getStatus() == BookLoan.LoanStatus.ACTIVE)
                    .count();
            this.totalFines = loans.stream()
                    .map(BookLoan::getFineAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }

        // Getters
        public Member getMember() {
            return member;
        }

        public List<BookLoan> getLoans() {
            return loans;
        }

        public int getTotalLoans() {
            return totalLoans;
        }

        public int getActiveLoans() {
            return activeLoans;
        }

        public BigDecimal getTotalFines() {
            return totalFines;
        }

        @Override
        public String toString() {
            return String.format("Member: %s, Total Loans: %d, Active: %d, Total Fines: $%.2f",
                    member.getName(), totalLoans, activeLoans, totalFines);
        }
    }

    public static class DailyOperationResult {
        private int overdueLoansUpdated;
        private int totalOverdueLoans;
        private BigDecimal totalFinesAccrued;

        public DailyOperationResult(int overdueLoansUpdated, int totalOverdueLoans, BigDecimal totalFinesAccrued) {
            this.overdueLoansUpdated = overdueLoansUpdated;
            this.totalOverdueLoans = totalOverdueLoans;
            this.totalFinesAccrued = totalFinesAccrued;
        }

        // Getters
        public int getOverdueLoansUpdated() { return overdueLoansUpdated; }
        public int getTotalOverdueLoans() { return totalOverdueLoans; }
        public BigDecimal getTotalFinesAccrued() { return totalFinesAccrued; }

        @Override
        public String toString() {
            return String.format("Daily Operations: %d loans marked overdue, %d total overdue, $%.2f in fines",
                    overdueLoansUpdated, totalOverdueLoans, totalFinesAccrued);
        }
    }

    public static class LibraryStatistics {
        private int totalAuthors;
        private int totalBooks;
        private int totalCopies;
        private int totalMembers;
        private int activeLoans;
        private int overdueLoans;

        public LibraryStatistics(int totalAuthors, int totalBooks, int totalCopies,
                                 int totalMembers, int activeLoans, int overdueLoans) {
            this.totalAuthors = totalAuthors;
            this.totalBooks = totalBooks;
            this.totalCopies = totalCopies;
            this.totalMembers = totalMembers;
            this.activeLoans = activeLoans;
            this.overdueLoans = overdueLoans;
        }

        // Getters
        public int getTotalAuthors() { return totalAuthors; }
        public int getTotalBooks() { return totalBooks; }
        public int getTotalCopies() { return totalCopies; }
        public int getTotalMembers() { return totalMembers; }
        public int getActiveLoans() { return activeLoans; }
        public int getOverdueLoans() { return overdueLoans; }

        @Override
        public String toString() {
            return String.format("Library Statistics:\n" +
                            "Authors: %d, Books: %d, Copies: %d\n" +
                            "Members: %d, Active Loans: %d, Overdue: %d",
                    totalAuthors, totalBooks, totalCopies,
                    totalMembers, activeLoans, overdueLoans);
        }
    }
}
