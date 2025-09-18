package Day_27To31.library_management_system.src.main.java.com.library.model;

import java.math.BigDecimal;

public class Book {
    private int bookId;
    private String title;
    private String isbn;
    private Integer publicationYear;
    private BigDecimal price;
    private int copiesAvailable;
    private int authorId;
    private String authorName; // For joined queries

    // Constructors
    public Book() {}

    public Book(String title, String isbn, Integer publicationYear,
                BigDecimal price, int copiesAvailable, int authorId) {
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.price = price;
        this.copiesAvailable = copiesAvailable;
        this.authorId = authorId;
    }

    // Full constructor
    public Book(int bookId, String title, String isbn, Integer publicationYear,
                BigDecimal price, int copiesAvailable, int authorId) {
        this.bookId = bookId;
        this.title = title;
        this.isbn = isbn;
        this.publicationYear = publicationYear;
        this.price = price;
        this.copiesAvailable = copiesAvailable;
        this.authorId = authorId;
    }

    // Getters and Setters
    public int getBookId() { return bookId; }
    public void setBookId(int bookId) { this.bookId = bookId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public int getCopiesAvailable() { return copiesAvailable; }
    public void setCopiesAvailable(int copiesAvailable) { this.copiesAvailable = copiesAvailable; }

    public int getAuthorId() { return authorId; }
    public void setAuthorId(int authorId) { this.authorId = authorId; }

    public String getAuthorName() { return authorName; }
    public void setAuthorName(String authorName) { this.authorName = authorName; }

    @Override
    public String toString() {
        return String.format("Book{id=%d, title='%s', isbn='%s', author='%s', copies=%d}",
                bookId, title, isbn, authorName != null ? authorName : "ID:" + authorId, copiesAvailable);
    }
}