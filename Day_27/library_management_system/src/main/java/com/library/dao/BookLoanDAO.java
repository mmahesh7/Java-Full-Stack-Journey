package Day_27.library_management_system.src.main.java.com.library.dao;

import java.math.BigDecimal;

public class BookLoanDAO {
    private static final BigDecimal DAILY_FINE_RATE = new BigDecimal("1.00");

    private static final String INSERT_LOAN =
            """
                insert into book_loans (book_id, member_id, loan_date, due_date, fine_amount, status) values
                (?,?,?,?,?,?)";
            """;
    private static final String SELECT_LOAN_BY_ID =
            """
                select bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type
                from book_loans bl
                join books b on b.book_id = b.book_id 
                join members m on b.book_id = m.book_id 
                where bl.loan_id = ?
            """;
    private static final String SELECT_ACTIVE_LOANS =
            "SELECT bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type " +
                    "FROM book_loans bl " +
                    "JOIN books b ON bl.book_id = b.book_id " +
                    "JOIN members m ON bl.member_id = m.member_id " +
                    "WHERE bl.status = 'ACTIVE' AND bl.due_date < CURRENT_DATE ORDER BY bl.due_date";

    private static final String SELECT_OVERDUE_LOANS =
            "SELECT bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type " +
                    "FROM book_loans bl " +
                    "JOIN books b ON bl.book_id = b.book_id " +
                    "JOIN members m ON bl.member_id = m.member_id " +
                    "WHERE bl.status = 'ACTIVE' AND bl.due_date < CURRENT_DATE ORDER BY bl.due_date";

    private static final String SELECT_MEMBER_LOANS =
            "SELECT bl.*, b.title as book_title, m.name as member_name, m.membership_type as member_type " +
                    "FROM book_loans bl " +
                    "JOIN books b ON bl.book_id = b.book_id " +
                    "JOIN members m ON bl.member_id = m.member_id " +
                    "WHERE bl.member_id = ? ORDER BY bl.loan_date DESC";
}
