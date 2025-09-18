package Day_27To31.library_management_system.src.main.java.com.library.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BookLoan {
    private int loanId;
    private int bookId;
    private int memberId;
    private LocalDate loanDate;
    private LocalDate returnDate;
    private LocalDate dueDate;
    private BigDecimal fineAmount;
    private LoanStatus status;

    // Additional fields for joined queries
    private String bookTitle;
    private String memberName;
    private String memberType;

    // Enum for loan status
    public enum LoanStatus {
        ACTIVE, RETURNED, OVERDUE
    }

    // Constructors
    public BookLoan() {}

    public BookLoan(int bookId, int memberId, LocalDate dueDate) {
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = LocalDate.now();
        this.dueDate = dueDate;
        this.status = LoanStatus.ACTIVE;
        this.fineAmount = BigDecimal.ZERO;
    }

    // Full constructor
    public BookLoan(int loanId, int bookId, int memberId, LocalDate loanDate,
                    LocalDate returnDate, LocalDate dueDate, BigDecimal fineAmount, LoanStatus status) {
        this.loanId = loanId;
        this.bookId = bookId;
        this.memberId = memberId;
        this.loanDate = loanDate;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.fineAmount = fineAmount;
        this.status = status;
    }

    // Getters and Setters
    public int getLoanId() { return loanId; }
    public void setLoanId(int loanId) { this.loanId = loanId; }

    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public int getMemberId() { return memberId; }
    public void setMemberId(int memberId) { this.memberId = memberId; }

    public LocalDate getLoanDate() { return loanDate; }
    public void setLoanDate(LocalDate loanDate) { this.loanDate = loanDate; }

    public LocalDate getReturnDate() { return returnDate; }
    public void setReturnDate(LocalDate returnDate) { this.returnDate = returnDate; }

    public LocalDate getDueDate() { return dueDate; }
    public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }

    public BigDecimal getFineAmount() { return fineAmount; }
    public void setFineAmount(BigDecimal fineAmount) { this.fineAmount = fineAmount; }

    public LoanStatus getStatus() { return status; }
    public void setStatus(LoanStatus status) { this.status = status; }

    public String getBookTitle() { return bookTitle; }
    public void setBookTitle(String bookTitle) { this.bookTitle = bookTitle; }

    public String getMemberName() { return memberName; }
    public void setMemberName(String memberName) { this.memberName = memberName; }

    public String getMemberType() { return memberType; }
    public void setMemberType(String memberType) { this.memberType = memberType; }

    // Business methods
    public boolean isOverdue() {
        return (status == LoanStatus.ACTIVE || status == LoanStatus.OVERDUE)  && LocalDate.now().isAfter(dueDate);
    }

    public long getDaysOverdue() {
        if (!isOverdue()) return 0;
        return ChronoUnit.DAYS.between(dueDate, LocalDate.now());
    }

    public BigDecimal calculateFine(BigDecimal dailyFineRate) {
        long daysOverdue = getDaysOverdue();
        if (daysOverdue <= 0) return BigDecimal.ZERO;
        return dailyFineRate.multiply(BigDecimal.valueOf(daysOverdue));
    }

    @Override
    public String toString() {
        return String.format("BookLoan{id=%d, book='%s', member='%s', status=%s, dueDate=%s}",
                loanId, bookTitle != null ? bookTitle : "ID:" + bookId,
                memberName != null ? memberName : "ID:" + memberId,
                status, dueDate);
    }
}
