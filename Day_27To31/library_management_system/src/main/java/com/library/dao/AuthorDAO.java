package Day_27To31.library_management_system.src.main.java.com.library.dao;

import Day_27To31.library_management_system.src.main.java.com.library.model.Author;
import Day_27To31.library_management_system.src.main.java.com.library.util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AuthorDAO {
    private static final String INSERT_AUTHOR =
            "INSERT INTO AUTHORS (NAME, EMAIL, BIRTH_YEAR, BIOGRAPHY) VALUES (?,?,?,?)";
    private static final String SELECT_AUTHOR_BY_ID =
            "SELECT * FROM AUTHORS WHERE AUTHOR_ID = ?";
    private static final String SELECT_ALL_AUTHORS =
            "SELECT * FROM AUTHORS ORDER BY NAME";
    private static final String UPDATE_AUTHOR =
            "UPDATE AUTHORS SET NAME = ?, EMAIL =?, BIRTH_YEAR = ?, BIOGRAPHY = ? WHERE AUTHOR_ID = ?";
    private static final String DELETE_AUTHOR =
            "DELETE FROM AUTHORS WHERE AUTHOR_ID = ?";
    private static final String CHECK_AUTHOR_EXISTS =
            "SELECT COUNT(*) FROM AUTHORS WHERE email = ?";

    public boolean createAuthor(Author author) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DatabaseConnection.getConnection();

            if (emailExists(author.getEmail())) {
                System.out.println("Author with email " + author.getEmail() + " already exists!");
                return false;
            }
            pstmt = conn.prepareStatement(INSERT_AUTHOR, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getEmail());
            if (author.getBirthYear() != null) {
                pstmt.setInt(3, author.getBirthYear());
            } else {
                pstmt.setNull(3, Types.INTEGER);
            }

            pstmt.setString(4, author.getBiography());

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    author.setAuthorId(generatedKeys.getInt(1));
                }
                return true;
            }
        } catch (SQLException e) {
            System.err.println("Error creating author: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }

        return false;
    }

    public Author getAuthorById(int authorId) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_AUTHOR_BY_ID);
            pstmt.setInt(1, authorId);

            rs = pstmt.executeQuery();

            if (rs.next()) {
                return extractAuthorFromResultSet(rs);
            }

        } catch (SQLException e) {
            System.err.println("Error retrieving author: " + e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }

        return null;
    }

    public List<Author> getAllAuthors() {
        List<Author> authors = new ArrayList<>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(SELECT_ALL_AUTHORS);
            rs = pstmt.executeQuery();

            while(rs.next()) {
                authors.add(extractAuthorFromResultSet(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving authors:" +e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return authors;
    }

    public boolean updateAuthor(Author author) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(UPDATE_AUTHOR);

            pstmt.setString(1, author.getName());
            pstmt.setString(2, author.getEmail());
            if(author.getBirthYear() != null) {
                pstmt.setInt(3, author.getBirthYear());
            }else{
                pstmt.setNull(3, Types.INTEGER);
            }
            pstmt.setString(4, author.getBiography());
            pstmt.setInt(5,author.getAuthorId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating author: "+ e.getMessage());
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    public boolean deleteAuthor(int authorId) {
        Connection conn = null;
        PreparedStatement pstmt = null;

        try{
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(DELETE_AUTHOR);
            pstmt.setInt(1, authorId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error deleting author: "+ e.getMessage());
            if (e.getMessage().contains("foreign key constraint")) {
                System.err.println("Cannot delete author - books exist for this author");
            }
        } finally {
            closeResources(conn, pstmt, null);
        }
        return false;
    }

    private boolean emailExists(String email) {
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = DatabaseConnection.getConnection();
            pstmt = conn.prepareStatement(CHECK_AUTHOR_EXISTS);
            pstmt.setString(1, email);
            rs = pstmt.executeQuery();

            if(rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.err.println("Error checking email existence: "+ e.getMessage());
        } finally {
            closeResources(conn, pstmt, rs);
        }
        return false;
    }

    private Author extractAuthorFromResultSet(ResultSet rs) throws SQLException {
        return new Author(
                rs.getInt("author_id"),
                rs.getString("name"),
                rs.getString("email"),
                rs.getObject("birth_year", Integer.class), // Handles null values properly
                rs.getString("biography")
        );
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
