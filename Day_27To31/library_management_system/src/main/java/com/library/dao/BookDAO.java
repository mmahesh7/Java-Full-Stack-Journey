package Day_27To31.library_management_system.src.main.java.com.library.dao;

import Day_27To31.library_management_system.src.main.java.com.library.model.Book;
import Day_27To31.library_management_system.src.main.java.com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    // SQL Queries
    private static final String INSERT_BOOK =
            "INSERT INTO books (title, isbn, publication_year, price, copies_available, author_id) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SELECT_BOOK_BY_ID =
            "SELECT b.*, a.name as author_name FROM books b JOIN authors a ON b.author_id = a.author_id WHERE b.book_id = ?";
    private static final String SELECT_ALL_BOOKS =
            "SELECT b.*, a.name as author_name FROM books b JOIN authors a ON b.author_id = a.author_id ORDER BY b.title";
    private static final String UPDATE_BOOK =
            "UPDATE books SET title = ?, isbn = ?, publication_year = ?, price = ?, copies_available = ?, author_id = ? WHERE book_id = ?";
    private static final String DELETE_BOOK =
            "DELETE FROM books WHERE book_id = ?";
    private static final String CHECK_ISBN_EXISTS =
            "SELECT COUNT(*) FROM books WHERE isbn = ?";
    private static final String UPDATE_COPIES =
            "UPDATE books SET copies_available = copies_available + ? WHERE book_id = ?";

    /**
     * Create a new book
     */
    public boolean createBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Check if ISBN already exists
            if (isbnExists(book.getIsbn())) {
                System.out.println("Book with ISBN " + book.getIsbn() + " already exists!");
                return false;
            }

            pstmt = conn.prepareStatement(INSERT_BOOK, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getIsbn());

            if (book.getPublicationYear() != null) {
                pstmt.setInt(3, book.getPublicationYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            if (book.getPrice() != null) {
                pstmt.setBigDecimal(4, book.getPrice());
            } else {
                pstmt.setNull(4, Types.DECIMAL);
            }

            pstmt.setInt(5, book.getCopiesAvailable());
            pstmt.setInt(6, book.getAuthorId());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    book.setBookId(generatedKeys.getInt(1));
                }
                return true;
            }

        } catch (SQLException e) {
            System.err.println("Error creating book: " + e.getMessage());
            if (e.getMessage().contains("foreign key constraint")) {
                System.err.println("Invalid author ID - author does not exist");
            }
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Get book by ID with author information
     */
    public Book getBookById(int bookId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_BOOK_BY_ID);
            pstmt.setInt(1, bookId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractBookFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving book: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return null;
    }

    /**
     * Get all books with author information
     */
    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL_BOOKS);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                books.add(extractBookFromResultSet(rs));
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving books: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return books;
    }

    /**
     * Update book information
     */
    public boolean updateBook(Book book) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(UPDATE_BOOK);

            pstmt.setString(1, book.getTitle());
            pstmt.setString(2, book.getIsbn());

            if (book.getPublicationYear() != null) {
                pstmt.setInt(3, book.getPublicationYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            if (book.getPrice() != null) {
                pstmt.setBigDecimal(4, book.getPrice());
            } else {
                pstmt.setNull(4, Types.DECIMAL);
            }

            pstmt.setInt(5, book.getCopiesAvailable());
            pstmt.setInt(6, book.getAuthorId());
            pstmt.setInt(7, book.getBookId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating book: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Delete book
     */
    public boolean deleteBook(int bookId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(DELETE_BOOK);
            pstmt.setInt(1, bookId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting book: " + e.getMessage());
            if (e.getMessage().contains("foreign key constraint")) {
                System.err.println("Cannot delete book - active loans exist for this book");
            }
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Update book copies (for lending/returning)
     */
    public boolean updateCopies(int bookId, int changeInCopies) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            // First, get current values to validate the change
            Book currentBook = getBookById(bookId);
            if (currentBook == null) {
                System.out.println("Book not found");
                return false;
            }

            int newAvailable = currentBook.getCopiesAvailable() + changeInCopies;
            if (newAvailable < 0) {
                System.out.println("Cannot reduce copies below 0");
                return false;
            }

            // Update both copies_available and total_copies
            String sql = "UPDATE books SET copies_available = copies_available + ?, " +
                    "total_copies = GREATEST(total_copies, copies_available + ?) " +
                    "WHERE book_id = ?";

            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, changeInCopies);
            pstmt.setInt(2, changeInCopies);
            pstmt.setInt(3, bookId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating book copies: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    /**
     * Check if ISBN exists
     */
    private boolean isbnExists(String isbn) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(CHECK_ISBN_EXISTS);
            pstmt.setString(1, isbn);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            System.err.println("Error checking ISBN existence: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return false;
    }

    /**
     * Extract Book object from ResultSet
     */
    private Book extractBookFromResultSet(ResultSet rs) throws SQLException {
        Book book = new Book(
                rs.getInt("book_id"),
                rs.getString("title"),
                rs.getString("isbn"),
                rs.getObject("publication_year", Integer.class),
                rs.getBigDecimal("price"),
                rs.getInt("copies_available"),
                rs.getInt("author_id")
        );
        book.setAuthorName(rs.getString("author_name"));
        return book;
    }

    /**
     * Close database resources
     */
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
