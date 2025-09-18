package Day_27To31.library_management_system.src.main.java.com.library.util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/library_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mahesh@mySQL";

    static {
        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("MySQL JDBC Driver not found", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error closing connection: " + e.getMessage());
            }
        }
    }

    public static void testConnection() {
        try (Connection conn = getConnection()) {
            if (conn != null && !conn.isClosed()) {
                System.out.println("✓ Database connection successful!");
                System.out.println("Database: " + conn.getMetaData().getDatabaseProductName());
            }
        } catch (SQLException e) {
            System.err.println("✗ Database connection failed: " + e.getMessage());
        }
    }

    public static void main(String[] args) {

    }
}
