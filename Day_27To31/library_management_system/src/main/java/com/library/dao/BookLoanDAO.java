package Day_27To31.library_management_system.src.main.java.com.library.dao;

import Day_27To31.library_management_system.src.main.java.com.library.model.BookLoan;
import Day_27To31.library_management_system.src.main.java.com.library.util.DatabaseConnection;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class BookLoanDAO {
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("1.00");

    private static final String INSERT_LOAN =
            "INSERT INTO book_loans (book_id, member_id, loan_date, due_date, fine_amount, status) VALUES (?,?,?,?,?,?)";

    // FIXED: Corrected the JOIN syntax in SELECT_LOAN_BY_ID
    private static final String SELECT_LOAN_BY_ID =
            "SELECT bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type " +
                    "FROM book_loans bl " +
                    "JOIN books b ON bl.book_id = b.book_id " +
                    "JOIN members m ON bl.member_id = m.member_id " +
                    "WHERE bl.loan_id = ?";

    // FIXED: Corrected the status filter for active loans
    private static final String SELECT_ACTIVE_LOANS =
            "SELECT bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type " +
                    "FROM book_loans bl " +
                    "JOIN books b ON bl.book_id = b.book_id " +
                    "JOIN members m ON bl.member_id = m.member_id " +
                    "WHERE bl.status IN ('ACTIVE', 'OVERDUE') AND bl.return_date IS NULL " +
                    "ORDER BY bl.due_date";

    private static final String SELECT_OVERDUE_LOANS =
            "SELECT bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type " +
                    "FROM book_loans bl " +
                    "JOIN books b ON bl.book_id = b.book_id " +
                    "JOIN members m ON bl.member_id = m.member_id " +
                    "WHERE bl.status IN ('ACTIVE','OVERDUE') AND bl.due_date < CURRENT_DATE AND bl.return_date IS NULL " +
                    "ORDER BY bl.due_date";

    private static final String SELECT_MEMBER_LOANS =
            "SELECT bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type " +
                    "FROM book_loans bl " +
                    "JOIN books b ON bl.book_id = b.book_id " +
                    "JOIN members m ON bl.member_id = m.member_id " +
                    "WHERE bl.member_id = ? ORDER BY bl.loan_date DESC";

    private static final String UPDATE_LOAN_RETURN =
            "UPDATE book_loans SET return_date = ?, fine_amount = ?, status = 'RETURNED' WHERE loan_id = ?";

    private static final String UPDATE_LOAN_STATUS =
            "UPDATE book_loans SET status = ?, fine_amount = ? WHERE loan_id = ?";

    private static final String CHECK_BOOK_AVAILABILITY =
            "SELECT copies_available FROM books WHERE book_id = ?";

    private static final String UPDATE_BOOK_COPIES =
            "UPDATE books SET copies_available = copies_available + ? WHERE book_id = ?";

    // NEW: Query to update all overdue loans with current fines
    private static final String UPDATE_ALL_OVERDUE_FINES =
            "UPDATE book_loans SET " +
                    "fine_amount = CASE " +
                    "  WHEN due_date < CURRENT_DATE AND status IN ('ACTIVE', 'OVERDUE') " +
                    "  THEN DATEDIFF(CURRENT_DATE, due_date) * ? " +
                    "  ELSE fine_amount " +
                    "END, " +
                    "status = CASE " +
                    "  WHEN due_date < CURRENT_DATE AND status = 'ACTIVE' " +
                    "  THEN 'OVERDUE' " +
                    "  ELSE status " +
                    "END " +
                    "WHERE status IN ('ACTIVE', 'OVERDUE') AND return_date IS NULL";

    public boolean issueBook(int bookId, int memberId, int loanDurationDays) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            if (!isBookAvailable(conn, bookId)) {
                System.out.println("Book is not available for lending");
                conn.rollback();
                return false;
            }

            MemberDAO memberDAO = new MemberDAO();
            int currentLoans = memberDAO.getActiveLoanCount(memberId);

            if (currentLoans >= 3) {
                System.out.println("Member has reached loan limit (already has " + currentLoans + " loans)");
                conn.rollback();
                return false;
            }

            BookLoan loan = new BookLoan(bookId, memberId, LocalDate.now().plusDays(loanDurationDays));

            pstmt = conn.prepareStatement(INSERT_LOAN, Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, loan.getBookId());
            pstmt.setInt(2, loan.getMemberId());
            pstmt.setDate(3, Date.valueOf(loan.getLoanDate()));
            pstmt.setDate(4, Date.valueOf(loan.getDueDate()));
            pstmt.setBigDecimal(5, loan.getFineAmount());
            pstmt.setString(6, loan.getStatus().toString());

            int loanResult = pstmt.executeUpdate();

            if (loanResult > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    loan.setLoanId(generatedKeys.getInt(1));
                }

                PreparedStatement bookStmt = conn.prepareStatement(UPDATE_BOOK_COPIES);
                bookStmt.setInt(1, -1);
                bookStmt.setInt(2, bookId);

                int bookResult = bookStmt.executeUpdate();

                if (bookResult > 0) {
                    conn.commit();
                    System.out.println("Book issued successfully. Loan ID: " + loan.getLoanId());
                    return true;
                } else {
                    conn.rollback();
                    System.out.println("Failed to update book inventory");
                }
                bookStmt.close();
            } else {
                conn.rollback();
                System.out.println("Failed to create loan record");
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error issuing book: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    public boolean returnBook(int loanId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            BookLoan loan = getLoanById(loanId);
            if (loan == null || loan.getStatus() == BookLoan.LoanStatus.RETURNED) {
                System.out.println("Invalid loan or book already returned");
                conn.rollback();
                return false;
            }

            // Calculate fine based on current date
            BigDecimal fine = calculateCurrentFine(loan);

            pstmt = conn.prepareStatement(UPDATE_LOAN_RETURN);
            pstmt.setDate(1, Date.valueOf(LocalDate.now()));
            pstmt.setBigDecimal(2, fine);
            pstmt.setInt(3, loanId);

            int loanResult = pstmt.executeUpdate();
            if (loanResult > 0) {
                PreparedStatement bookStmt = conn.prepareStatement(UPDATE_BOOK_COPIES);
                bookStmt.setInt(1, 1);
                bookStmt.setInt(2, loan.getBookId());

                int bookResult = bookStmt.executeUpdate();

                if (bookResult > 0) {
                    conn.commit();
                    if (fine.compareTo(BigDecimal.ZERO) > 0) {
                        System.out.println("Book returned successfully. Fine amount: $" + fine);
                    } else {
                        System.out.println("Book returned successfully. No fine.");
                    }
                    return true;
                } else {
                    conn.rollback();
                    System.out.println("Failed to update book inventory");
                }
                bookStmt.close();
            } else {
                conn.rollback();
                System.out.println("Failed to update the loan record.");
            }
        } catch (SQLException e) {
            try {
                if (conn != null) conn.rollback();
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Error returning book: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    public BookLoan getLoanById(int loanId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_LOAN_BY_ID);
            pstmt.setInt(1, loanId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractLoanFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving loan: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return null;
    }

    public List<BookLoan> getActiveLoans() {
        // First update overdue loans and fines
        updateOverdueFines();
        return getLoansWithQuery(SELECT_ACTIVE_LOANS);
    }

    public List<BookLoan> getOverdueLoans() {
        // First update overdue loans and fines
        updateOverdueFines();
        return getLoansWithQuery(SELECT_OVERDUE_LOANS);
    }

    public List<BookLoan> getMemberLoans(int memberId) {
        List<BookLoan> loans = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_MEMBER_LOANS);
            pstmt.setInt(1, memberId);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                loans.add(extractLoanFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving member loans: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return loans;
    }

    // NEW: Method to update overdue fines for all loans
    public int updateOverdueFines() {
        Connection conn = null;
        PreparedStatement pstmt = null;
        int updateCount = 0;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(UPDATE_ALL_OVERDUE_FINES);
            pstmt.setBigDecimal(1, DAILY_FINE_RATE);

            updateCount = pstmt.executeUpdate();

            if (updateCount > 0) {
                System.out.println("Updated fines and status for " + updateCount + " loans");
            }
        } catch (SQLException e) {
            System.err.println("Error updating overdue fines: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return updateCount;
    }

    public int updateOverdueLoans() {
        // This method now just calls updateOverdueFines which handles both status and fines
        return updateOverdueFines();
    }

    // NEW: Helper method to calculate current fine for a loan
    private BigDecimal calculateCurrentFine(BookLoan loan) {
        if (loan.getDueDate().isBefore(LocalDate.now())) {
            long daysOverdue = java.time.temporal.ChronoUnit.DAYS.between(
                    loan.getDueDate(), LocalDate.now());
            return DAILY_FINE_RATE.multiply(BigDecimal.valueOf(daysOverdue));
        }
        return BigDecimal.ZERO;
    }

    private boolean isBookAvailable(Connection conn, int bookId) throws SQLException {
        PreparedStatement pstmt = conn.prepareStatement(CHECK_BOOK_AVAILABILITY);
        pstmt.setInt(1, bookId);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }
        return false;
    }

    private List<BookLoan> getLoansWithQuery(String query) {
        List<BookLoan> loans = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(query);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                loans.add(extractLoanFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error executing loan query: " + e.getMessage());
            e.printStackTrace();
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return loans;
    }

    private BookLoan extractLoanFromResultSet(ResultSet rs) throws SQLException {
        BookLoan loan = new BookLoan(
                rs.getInt("loan_id"),
                rs.getInt("book_id"),
                rs.getInt("member_id"),
                rs.getDate("loan_date").toLocalDate(),
                rs.getDate("return_date") != null ? rs.getDate("return_date").toLocalDate() : null,
                rs.getDate("due_date").toLocalDate(),
                rs.getBigDecimal("fine_amount"),
                BookLoan.LoanStatus.valueOf(rs.getString("status"))
        );

        // Set additional fields from joined tables
        loan.setBookTitle(rs.getString("book_title"));
        loan.setMemberName(rs.getString("member_name"));
        loan.setMemberType(rs.getString("member_type"));

        return loan;
    }

    private void closeResources(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
            if (conn != null) conn.close();
        } catch (SQLException e) {
            System.err.println("Error closing resources: " + e.getMessage());
        }
    }
}