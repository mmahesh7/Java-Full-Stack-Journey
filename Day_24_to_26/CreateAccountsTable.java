package Day_24_to_26;

import java.sql.*;

/**
 * Simple program to create accounts table and add test data
 */
public class CreateAccountsTable {
    // Change these to match your database setup
    private static final String URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mahesh@mySQL";

    public static void main(String[] args) {
        System.out.println("Creating accounts table...");

        try {
            // Step 1: Connect to database
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();

            // Step 2: Create the accounts table
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS accounts (
                    account_id INT PRIMARY KEY,
                    account_name VARCHAR(100) NOT NULL,
                    balance DECIMAL(10,2) NOT NULL DEFAULT 0.00
                );
                """;

            stmt.executeUpdate(createTableSQL);
            System.out.println("Accounts table created successfully!");

            // Step 3: Add test data
//            String insertDataSQL = "INSERT IGNORE INTO accounts (account_id, account_name, balance) VALUES (1, 'MK Savings Account', 1000.00), (2, 'MK Checking Account', 500.00), (3, 'MK Savings Account', 750.00);";
//
//            int rowsAdded = stmt.executeUpdate(insertDataSQL);
//            System.out.println("Added " + rowsAdded + " test accounts!");

            // Step 4: Show what we created
            String selectSQL = "SELECT * FROM accounts ORDER BY account_id";
            ResultSet rs = stmt.executeQuery(selectSQL);

            System.out.println("\n--- Accounts Created ---");
            System.out.printf("%-5s %-25s %-10s%n", "ID", "Account Name", "Balance");
            System.out.println("------------------------------------------");

            while (rs.next()) {
                int id = rs.getInt("account_id");
                String name = rs.getString("account_name");
                double balance = rs.getDouble("balance");
                System.out.printf("%-5d %-25s $%-9.2f%n", id, name, balance);
            }

            // Step 5: Close connections
            rs.close();
            stmt.close();
            conn.close();

            System.out.println("\nAll done! You can now run the transaction example.");

        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}