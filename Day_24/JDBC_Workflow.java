package Day_24;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Date;
import java.util.Properties;

public class JDBC_Workflow {
    private static final String URL = "jdbc:mysql://localhost:3306/bookstore";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mahesh@mySQL";
    public static void main(String[] args) throws ClassNotFoundException{
        //Step 1: Load Driver(optional from java 4)
        Class.forName("com.mysql.cj.jdbc.Driver");
        try{
            //Step 2: Get Connection
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            System.out.println("Connection successful!");

            //Step 3: Create Statement
            String query = "SELECT * FROM books;";
            Statement stmt = conn.createStatement();
            ResultSet rs0 = stmt.executeQuery(query);

            PreparedStatement pstmt = conn.prepareStatement("SELECT *FROM books where price > ?;");
            pstmt.setInt(1,30);

            //Step 4: Execute Query
            ResultSet rs = pstmt.executeQuery();

            //Step 5: Process Results
            while(rs0.next()) {
                int id = rs0.getInt("book_id");
                String title = rs0.getString("title");
                BigDecimal price = rs0.getBigDecimal("price");
                String author = rs0.getString("author");
                Date published_date = rs0.getDate("publication_date");
                String category = rs0.getString("category");
                int stock = rs0.getInt("in_stock");
                System.out.println(id+ " | "+ title+" | "+author+" | " + published_date+" | "+price);
            }
            System.out.println("==================================");
            while(rs.next()) {
                int id = rs.getInt("book_id");
                String title = rs.getString("title");
                BigDecimal price = rs.getBigDecimal("price");
                String author = rs.getString("author");
                Date published_date = rs.getDate("publication_date");
                String category = rs.getString("category");
                int stock = rs.getInt("in_stock");
                System.out.println(id+ " | "+ title+" | "+author+" | " + published_date+" | "+price);
            }
            rs.close();
            pstmt.close();
            conn.close();
        } catch(SQLException e) {
            System.out.println("Connection failed!: "+e.getMessage());
        }
    }
}
