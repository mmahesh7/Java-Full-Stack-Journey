package Day_24;//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import java.sql.*;

public class MyFirstJDBCProject {
    private static final String URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mahesh@mySQL";

    public static void main(String[] args) {
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL,USERNAME, PASSWORD);

            if(connection != null) {
                System.out.println("Connection successful!");
                System.out.println("Database: "+connection.getCatalog()); //returns the database name
                System.out.println("URL: "+ connection.getMetaData().getURL());
            }
        } catch (SQLException e) {
            System.err.println("Connection failed: "+ e.getMessage());
        } finally {
            if(connection != null) {
                try {
                    connection.close();
                    System.out.println("Connection closed successfully!");
                } catch (SQLException e) {
                    System.err.println("Error colsing connection: "+ e.getMessage());
                }
            }
        }
    }
}