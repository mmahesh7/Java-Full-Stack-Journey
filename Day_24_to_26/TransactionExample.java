package Day_24_to_26;

import java.sql.*;

//CREATE TABLE IF NOT EXISTS accounts (
//        account_id INT PRIMARY KEY,
//        account_name VARCHAR(100) NOT NULL,
//balance DECIMAL(10,2) NOT NULL DEFAULT 0.00
//        );
public class TransactionExample {
    private static final String URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mahesh@mySQL";

    private double getAccountBalance(Connection conn, int accountId) {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error checking account " + accountId + ": " + e.getMessage());
        }

        return -1; // Account doesn't exist
    }

    private boolean accountExists(Connection conn, int accountId) {
        return getAccountBalance(conn, accountId) >= 0;
    }

    public void transferMoney(int fromAccount, int toAccount, double amount) {
        Connection connection = null;
        PreparedStatement debitStmt = null;
        PreparedStatement creditStmt = null;


        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            connection.setAutoCommit(false);

            if (!accountExists(connection, fromAccount)) {
                System.out.println("Sender Account ID " + fromAccount + " does not exist!");
                return;
            }
            if (!accountExists(connection, toAccount)) {
                System.out.println("Receiver Account ID " + toAccount + " does not exist!");
                return;
            }

            String checkBalanceSQL = "SELECT balance FROM accounts WHERE account_id = ?";
            PreparedStatement checkStmt = connection.prepareStatement(checkBalanceSQL);
            checkStmt.setInt(1, fromAccount);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                System.out.println("Current balance in Account " + fromAccount + ": $" + currentBalance);

                // Check if there's enough money
                if (currentBalance < amount) {
                    System.out.println(" INSUFFICIENT FUNDS! Cannot transfer $" + amount);
                    System.out.println("   Account only has $" + currentBalance);
                    System.out.println("   Transfer CANCELLED!");

                    // Clean up and exit
                    rs.close();
                    checkStmt.close();
                    connection.rollback();
                    return; // Stop here - don't do the transfer
                }
                System.out.println(" Sufficient funds available for transfer");
            } else {
                System.out.println(" Account " + fromAccount + " does not exist!");
                rs.close();
                checkStmt.close();
                connection.rollback();
                return;
            }

            // Clean up the check statement
            rs.close();
            checkStmt.close();

            // Step 3: Take money OUT of the first account (debit)
            String debitSQL = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
            debitStmt = connection.prepareStatement(debitSQL);
            debitStmt.setDouble(1, amount);
            debitStmt.setInt(2, fromAccount);

            int debitedRows = debitStmt.executeUpdate();
            System.out.println("Debited $" + amount + " from Account " + fromAccount);

            // Step 4: Add money TO the second account (credit)
            String creditSQL = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            creditStmt = connection.prepareStatement(creditSQL);
            creditStmt.setDouble(1, amount);
            creditStmt.setInt(2, toAccount);

            int creditedRows = creditStmt.executeUpdate();
            System.out.println("Credited $" + amount + " to Account " + toAccount);


            if(creditedRows == 1 && debitedRows == 1) {
                connection.commit();
                System.out.println("Money transferred successfully!");
            } else {
                connection.rollback();
                System.out.println("Money transfer unsuccessful - rollback");
            }
        } catch (SQLException e) {
            try{
                if (connection != null) {
                    connection.rollback(); // Undo changes on error
                    System.out.println("Transaction rolled back due to error");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Failed to initiate transaction! "+ e.getMessage());
        } finally {
            try{
                if(debitStmt != null) {
                    debitStmt.close();
                }
                if(creditStmt != null) {
                    creditStmt.close();
                }
                if(connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    public void showAccountBalances() {
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            String sql = "SELECT * FROM accounts ORDER BY account_id";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n--- Current Account Balances ---");
            while (rs.next()) {
                int id = rs.getInt("account_id");
                String name = rs.getString("account_name");
                double balance = rs.getDouble("balance");

                System.out.printf("Account %d (%s): $%.2f%n", id, name, balance);
            }
            System.out.println();

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            System.out.println("Error showing balances: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        TransactionExample example = new TransactionExample();

        System.out.println("\n1. Starting balances:");
        example.showAccountBalances();

        System.out.println("2. Transferring $200 from Account to Account ...");
        example.transferMoney(6, 5, 200.00);

        System.out.println("\n3. Balances after transfer:");
        example.showAccountBalances();

//        System.out.println("\n4. Trying to transfer $2000 (more than available)...");
//        example.transferMoney(1, 2, 2000.00);
//
//        System.out.println("\n5. Final balances:");
//        example.showAccountBalances();

        System.out.println();
    }
}
