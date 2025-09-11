# JDBC Core Concepts - Learn Once, Remember Forever

## 🎯 Memory Framework: The JDBC Story

Think of JDBC as **ordering food at a restaurant**:
1. **Menu** = Database Schema (tables, columns)
2. **Waiter** = JDBC Driver (connects you to kitchen)
3. **Order** = SQL Statement
4. **Kitchen** = Database Server
5. **Food** = ResultSet (data you get back)

---

## 🧠 Core Concept #1: What is JDBC? (The Bridge Analogy)

### Simple Definition
**JDBC = Java Database Connectivity**
- It's a **bridge** between Java applications and databases
- Like a **translator** who speaks both Java and SQL

### Real-World Analogy
```
You (Java App) → Translator (JDBC) → Foreign Person (Database)
```

### Why JDBC Exists?
**Problem**: Java doesn't understand database languages (SQL)
**Solution**: JDBC acts as a translator

### Memory Trick: JDBC = "Just Database Connection"
- **J**ava needs to connect to
- **D**atabase to get
- **B**ack some data through
- **C**onnection

### Detailed Information: JDBC Components
1. **JDBC API**: Standard Java interfaces (java.sql package)
2. **JDBC Driver**: Database-specific implementation
3. **JDBC URL**: Connection string format
4. **JDBC Manager**: Manages drivers and connections

### JDBC URL Format (MEMORIZE THIS!)
```
jdbc:<subprotocol>://<host>:<port>/<database>
```

**Examples:**
- MySQL: `jdbc:mysql://localhost:3306/library_db`
- PostgreSQL: `jdbc:postgresql://localhost:5432/library_db`
- Oracle: `jdbc:oracle:thin:@localhost:1521:library_db`

### 🎯 Assignment 1: JDBC Basics
**Task**: Create a simple connection test program

```java
/**
 * Assignment 1: Test JDBC Connection
 * Create a class that tests database connectivity
 */
public class JDBCConnectionTest {
    public static void main(String[] args) {
        // TODO: Write code to test MySQL connection
        // 1. Load MySQL driver
        // 2. Create connection using DriverManager
        // 3. Print success/failure message
        // 4. Close connection properly
        
        // Expected Output:
        // ✅ Connection successful!
        // 🔒 Connection closed successfully!
    }
}
```

**Solution Template:**
```java
import java.sql.*;

public class JDBCConnectionTest {
    private static final String URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_password";
    
    public static void main(String[] args) {
        Connection connection = null;
        
        try {
            // Step 1: Load MySQL Driver (optional in JDBC 4.0+)
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Step 2: Establish connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            if (connection != null) {
                System.out.println("✅ Connection successful!");
                System.out.println("Database: " + connection.getCatalog());
                System.out.println("URL: " + connection.getMetaData().getURL());
            }
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("❌ Connection failed: " + e.getMessage());
        } finally {
            // Step 3: Close connection
            if (connection != null) {
                try {
                    connection.close();
                    System.out.println("🔒 Connection closed successfully!");
                } catch (SQLException e) {
                    System.err.println("❌ Error closing connection: " + e.getMessage());
                }
            }
        }
    }
}
```

---

## 🧠 Core Concept #2: JDBC Architecture (The Highway System)

### The 4-Layer Architecture
```
┌─────────────────────┐
│   Java Application  │ ← Your code (BookDAO, etc.)
├─────────────────────┤
│     JDBC API        │ ← Standard Java interfaces
├─────────────────────┤
│   JDBC Driver       │ ← Database-specific implementation
├─────────────────────┤
│     Database        │ ← MySQL, PostgreSQL, Oracle
└─────────────────────┘
```

### Memory Analogy: Postal System
1. **You** write a letter (SQL query)
2. **Post Office** (JDBC API) takes your letter
3. **Delivery Truck** (JDBC Driver) knows the route
4. **Recipient** (Database) receives and responds

### Detailed Information: JDBC Driver Types

#### Type 1: JDBC-ODBC Bridge Driver
- **How it works**: Converts JDBC calls to ODBC calls
- **Pros**: Can connect to any ODBC-compliant database
- **Cons**: Slow, platform-dependent, deprecated
- **Memory Tip**: "Type 1 = Old school, don't use"

#### Type 2: Native-API Driver
- **How it works**: Uses database-specific native libraries
- **Pros**: Faster than Type 1
- **Cons**: Platform-dependent, requires native libraries
- **Memory Tip**: "Type 2 = Native but not portable"

#### Type 3: Network Protocol Driver
- **How it works**: Uses middleware to convert JDBC calls
- **Pros**: Platform-independent, no client-side libraries
- **Cons**: Requires middleware server
- **Memory Tip**: "Type 3 = Three-tier architecture"

#### Type 4: Thin Driver (Pure Java Driver)
- **How it works**: Direct communication with database
- **Pros**: Fast, platform-independent, no additional software
- **Cons**: Database-specific
- **Memory Tip**: "Type 4 = Four stars, best choice!"

### 🎯 Assignment 2: Driver Types Research
**Task**: Create a comparison table of JDBC driver types

```java
/**
 * Assignment 2: JDBC Driver Analysis
 * Research and create a comprehensive comparison
 */
public class JDBCDriverComparison {
    public static void main(String[] args) {
        // TODO: Create a method that prints driver comparison
        // Include: Type, Name, Pros, Cons, Use Cases
        printDriverComparison();
    }
    
    public static void printDriverComparison() {
        // TODO: Implement this method
        // Format output as a neat table
    }
}
```

---

## 🧠 Core Concept #3: The JDBC Workflow (The Restaurant Process)

### The 6-Step Process (MEMORIZE THIS!)
```
1. LOAD DRIVER     → Find the waiter
2. GET CONNECTION  → Sit at table
3. CREATE STATEMENT → Place your order
4. EXECUTE QUERY   → Kitchen prepares food
5. PROCESS RESULTS → Eat the food
6. CLOSE RESOURCES → Pay bill and leave
```

### Memory Acronym: **"L-G-C-E-P-C"**
**"Lazy Guys Can't Execute Proper Code"**

### Detailed Step-by-Step Explanation

#### Step 1: Load Driver (Automatic in JDBC 4.0+)
```java
// Old way (JDBC 3.0 and earlier)
Class.forName("com.mysql.cj.jdbc.Driver");

// New way (JDBC 4.0+) - Automatic
// Driver is loaded automatically when DriverManager is used
```

#### Step 2: Get Connection
```java
// Basic connection
Connection conn = DriverManager.getConnection(url, username, password);

// Connection with properties
Properties props = new Properties();
props.setProperty("user", username);
props.setProperty("password", password);
props.setProperty("useSSL", "false");
Connection conn = DriverManager.getConnection(url, props);
```

#### Step 3: Create Statement
```java
// Statement (avoid in production)
Statement stmt = conn.createStatement();

// PreparedStatement (recommended)
PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM books WHERE id = ?");

// CallableStatement (for stored procedures)
CallableStatement cstmt = conn.prepareCall("{call getBooksByAuthor(?)}");
```

#### Step 4: Execute Query
```java
// For SELECT queries
ResultSet rs = pstmt.executeQuery();

// For INSERT, UPDATE, DELETE
int rowsAffected = pstmt.executeUpdate();

// For any SQL statement
boolean hasResultSet = pstmt.execute();
```

#### Step 5: Process Results
```java
while (rs.next()) {
    int id = rs.getInt("book_id");
    String title = rs.getString("title");
    BigDecimal price = rs.getBigDecimal("price");
    
    // Create Book object or process data
}
```

#### Step 6: Close Resources (CRITICAL!)
```java
// Close in reverse order
if (rs != null) rs.close();
if (pstmt != null) pstmt.close();
if (conn != null) conn.close();

// Or use try-with-resources (recommended)
try (Connection conn = DriverManager.getConnection(url, user, pass);
     PreparedStatement pstmt = conn.prepareStatement(sql);
     ResultSet rs = pstmt.executeQuery()) {
    
    // Process results here
    
} // Resources closed automatically
```

### 🎯 Assignment 3: Complete CRUD Operations
**Task**: Implement all CRUD operations for a Student table

```java
/**
 * Assignment 3: Student CRUD Operations
 * Create a complete CRUD system for student management
 */

// First, create this table in your database:
/*
CREATE TABLE students (
    student_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    age INT,
    course VARCHAR(50)
);
*/

public class StudentCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/library_management";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "your_password";
    
    // TODO: Implement these methods
    
    /**
     * Create a new student
     * @param name Student name
     * @param email Student email
     * @param age Student age
     * @param course Student course
     * @return Generated student ID
     */
    public int createStudent(String name, String email, int age, String course) {
        // TODO: Implement INSERT operation
        // Use PreparedStatement to prevent SQL injection
        // Return the generated student_id
        return -1;
    }
    
    /**
     * Read student by ID
     * @param studentId Student ID to search
     * @return Student details as formatted string
     */
    public String getStudentById(int studentId) {
        // TODO: Implement SELECT by ID operation
        // Return formatted string with student details
        return null;
    }
    
    /**
     * Update student information
     * @param studentId Student ID to update
     * @param name New name
     * @param email New email
     * @param age New age
     * @param course New course
     * @return Number of rows affected
     */
    public int updateStudent(int studentId, String name, String email, int age, String course) {
        // TODO: Implement UPDATE operation
        // Return number of rows affected
        return 0;
    }
    
    /**
     * Delete student by ID
     * @param studentId Student ID to delete
     * @return Number of rows affected
     */
    public int deleteStudent(int studentId) {
        // TODO: Implement DELETE operation
        // Return number of rows affected
        return 0;
    }
    
    /**
     * Get all students
     * @return List of all students
     */
    public void getAllStudents() {
        // TODO: Implement SELECT ALL operation
        // Print all students in a formatted way
    }
    
    public static void main(String[] args) {
        StudentCRUD crud = new StudentCRUD();
        
        // Test all operations
        System.out.println("=== Testing Student CRUD Operations ===");
        
        // Test CREATE
        int studentId = crud.createStudent("John Doe", "john@email.com", 20, "Computer Science");
        System.out.println("Created student with ID: " + studentId);
        
        // Test READ
        String student = crud.getStudentById(studentId);
        System.out.println("Retrieved student: " + student);
        
        // Test UPDATE
        int updated = crud.updateStudent(studentId, "John Smith", "johnsmith@email.com", 21, "Software Engineering");
        System.out.println("Updated " + updated + " student(s)");
        
        // Test READ ALL
        crud.getAllStudents();
        
        // Test DELETE
        int deleted = crud.deleteStudent(studentId);
        System.out.println("Deleted " + deleted + " student(s)");
    }
}
```

---

## 🧠 Core Concept #4: Key JDBC Classes (The Cast of Characters)

### 1. DriverManager (The Receptionist)
**Role**: Manages database drivers and creates connections
**Memory**: Think of hotel receptionist who gives you room keys

#### Key Methods:
```java
// Get connection with URL, username, password
static Connection getConnection(String url, String user, String password)

// Get connection with properties
static Connection getConnection(String url, Properties info)

// Register a driver
static void registerDriver(Driver driver)

// Get all registered drivers
static Enumeration<Driver> getDrivers()
```

#### Detailed Example:
```java
public class DriverManagerExample {
    public static void demonstrateDriverManager() {
        try {
            // Method 1: Basic connection
            Connection conn1 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management",
                "root",
                "password"
            );
            
            // Method 2: Connection with properties
            Properties props = new Properties();
            props.setProperty("user", "root");
            props.setProperty("password", "password");
            props.setProperty("useSSL", "false");
            props.setProperty("serverTimezone", "UTC");
            
            Connection conn2 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management",
                props
            );
            
            // Method 3: Connection with timeout
            DriverManager.setLoginTimeout(30); // 30 seconds timeout
            Connection conn3 = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/library_management",
                "root",
                "password"
            );
            
            System.out.println("All connections successful!");
            
            // Close connections
            conn1.close();
            conn2.close();
            conn3.close();
            
        } catch (SQLException e) {
            System.err.println("Connection failed: " + e.getMessage());
        }
    }
}
```

### 2. Connection (The Phone Line)
**Role**: Represents active connection to database
**Memory**: Like a phone call - once connected, you can talk

#### Key Methods:
```java
// Create different types of statements
Statement createStatement()
PreparedStatement prepareStatement(String sql)
CallableStatement prepareCall(String sql)

// Transaction management
void setAutoCommit(boolean autoCommit)
void commit()
void rollback()

// Connection information
String getCatalog()
DatabaseMetaData getMetaData()
boolean isClosed()
void close()
```

#### Detailed Transaction Example:
```java
public class TransactionExample {
    public void transferMoney(int fromAccount, int toAccount, double amount) {
        Connection conn = null;
        PreparedStatement debitStmt = null;
        PreparedStatement creditStmt = null;
        
        try {
            conn = DriverManager.getConnection(url, user, password);
            
            // Disable auto-commit for transaction
            conn.setAutoCommit(false);
            
            // Debit from source account
            debitStmt = conn.prepareStatement(
                "UPDATE accounts SET balance = balance - ? WHERE account_id = ?"
            );
            debitStmt.setDouble(1, amount);
            debitStmt.setInt(2, fromAccount);
            int debitRows = debitStmt.executeUpdate();
            
            // Credit to destination account
            creditStmt = conn.prepareStatement(
                "UPDATE accounts SET balance = balance + ? WHERE account_id = ?"
            );
            creditStmt.setDouble(1, amount);
            creditStmt.setInt(2, toAccount);
            int creditRows = creditStmt.executeUpdate();
            
            // Check if both operations succeeded
            if (debitRows == 1 && creditRows == 1) {
                conn.commit(); // Save changes
                System.out.println("✅ Transfer successful!");
            } else {
                conn.rollback(); // Undo changes
                System.out.println("❌ Transfer failed - rolled back");
            }
            
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback(); // Undo changes on error
                    System.out.println("❌ Transaction rolled back due to error");
                }
            } catch (SQLException rollbackEx) {
                System.err.println("Error during rollback: " + rollbackEx.getMessage());
            }
            System.err.println("Transfer error: " + e.getMessage());
        } finally {
            // Close resources
            try {
                if (debitStmt != null) debitStmt.close();
                if (creditStmt != null) creditStmt.close();
                if (conn != null) {
                    conn.setAutoCommit(true); // Reset auto-commit
                    conn.close();
                }
            } catch (SQLException e) {
                System.err.println("Error closing resources: " + e.getMessage());
            }
        }
    }
}
```

### 3. Statement Types (The Order Types)
Think of different ways to order food:

#### a) Statement (Shouting your order)
- **Risk**: Anyone can hear (SQL Injection)
- **Use**: Never use in production!

```java
// ❌ NEVER DO THIS - Vulnerable to SQL injection
Statement stmt = conn.createStatement();
String userInput = "'; DROP TABLE students; --";
String sql = "SELECT * FROM students WHERE name = '" + userInput + "'";
ResultSet rs = stmt.executeQuery(sql); // Dangerous!
```

#### b) PreparedStatement (Writing order on paper)
- **Safe**: Pre-compiled, prevents SQL injection
- **Use**: 99% of the time use this!

#### Key Methods:
```java
// Set parameters (index starts from 1)
void setString(int parameterIndex, String value)
void setInt(int parameterIndex, int value)
void setBigDecimal(int parameterIndex, BigDecimal value)
void setDate(int parameterIndex, Date value)
void setNull(int parameterIndex, int sqlType)

// Execute queries
ResultSet executeQuery() // For SELECT
int executeUpdate() // For INSERT, UPDATE, DELETE
boolean execute() // For any SQL statement
```

#### Detailed PreparedStatement Example:
```java
public class PreparedStatementExample {
    
    /**
     * Safe way to search students by name
     */
    public void searchStudentsByName(String searchName) {
        String sql = "SELECT student_id, name, email, course FROM students WHERE name LIKE ?";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Set parameter safely
            pstmt.setString(1, "%" + searchName + "%");
            
            try (ResultSet rs = pstmt.executeQuery()) {
                System.out.println("🔍 Search results for: " + searchName);
                System.out.println("ID\tName\t\tEmail\t\tCourse");
                System.out.println("--------------------------------------------------");
                
                while (rs.next()) {
                    System.out.printf("%d\t%s\t\t%s\t\t%s%n",
                        rs.getInt("student_id"),
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("course")
                    );
                }
            }
            
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
        }
    }
    
    /**
     * Batch insert multiple students
     */
    public void insertMultipleStudents(List<Student> students) {
        String sql = "INSERT INTO students (name, email, age, course) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // Disable auto-commit for better performance
            conn.setAutoCommit(false);
            
            for (Student student : students) {
                pstmt.setString(1, student.getName());
                pstmt.setString(2, student.getEmail());
                pstmt.setInt(3, student.getAge());
                pstmt.setString(4, student.getCourse());
                pstmt.addBatch(); // Add to batch
            }
            
            // Execute all at once
            int[] results = pstmt.executeBatch();
            conn.commit();
            
            System.out.println("✅ Inserted " + results.length + " students successfully!");
            
        } catch (SQLException e) {
            System.err.println("Batch insert error: " + e.getMessage());
        }
    }
}
```

#### c) CallableStatement (Calling chef directly)
- **Use**: For stored procedures only

```java
// Example stored procedure call
CallableStatement cstmt = conn.prepareCall("{call getStudentsByAge(?, ?)}");
cstmt.setInt(1, 18); // Input parameter
cstmt.registerOutParameter(2, Types.INTEGER); // Output parameter
cstmt.execute();
int count = cstmt.getInt(2); // Get output parameter
```

### 4. ResultSet (The Food Tray)
**Role**: Contains the results of your query
**Memory**: Like a food tray with multiple dishes

#### Key Methods:
```java
// Navigation methods
boolean next() // Move to next row
boolean previous() // Move to previous row
boolean first() // Move to first row
boolean last() // Move to last row

// Data retrieval methods
String getString(int columnIndex)
String getString(String columnLabel)
int getInt(int columnIndex)
int getInt(String columnLabel)
BigDecimal getBigDecimal(String columnLabel)
Date getDate(String columnLabel)

// Utility methods
boolean wasNull() // Check if last read value was NULL
int getRow() // Get current row number
```

#### Detailed ResultSet Example:
```java
public class ResultSetExample {
    
    /**
     * Demonstrate different ways to read ResultSet
     */
    public void demonstrateResultSetReading() {
        String sql = "SELECT student_id, name, email, age, course FROM students";
        
        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement pstmt = conn.prepareStatement(sql, 
                 ResultSet.TYPE_SCROLL_INSENSITIVE, 
                 ResultSet.CONCUR_READ_ONLY);
             ResultSet rs = pstmt.executeQuery()) {
            
            // Method 1: Reading by column index
            System.out.println("=== Reading by Column Index ===");
            while (rs.next()) {
                int id = rs.getInt(1);        // student_id
                String name = rs.getString(2);  // name
                String email = rs.getString(3); // email
                int age = rs.getInt(4);         // age
                String course = rs.getString(5); // course
                
                System.out.printf("ID: %d, Name: %s, Email: %s, Age: %d, Course: %s%n",
                    id, name, email, age, course);
            }
            
            // Method 2: Reading by column name (recommended)
            rs.beforeFirst(); // Reset cursor to beginning
            System.out.println("\n=== Reading by Column Name ===");
            while (rs.next()) {
                int id = rs.getInt("student_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int age = rs.getInt("age");
                String course = rs.getString("course");
                
                // Handle NULL values
                if (rs.wasNull()) {
                    System.out.println("Warning: NULL value encountered");
                }
                
                System.out.printf("Student: %s (%d) - %s, Age: %d, Course: %s%n",
                    name, id, email, age, course);
            }
            
            // Method 3: Navigate backwards (requires scrollable ResultSet)
            System.out.println("\n=== Reading Backwards ===");
            rs.last(); // Move to last row
            do {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                System.out.printf("Student: %s, Age: %d (Row: %d)%n", 
                    name, age, rs.getRow());
            } while (rs.previous());
            
        } catch (SQLException e) {
            System.err.println("ResultSet error: " + e.getMessage());
        }
    }
}
```

### 🎯 Assignment 4: Advanced ResultSet Operations
**Task**: Create a student report generator with advanced ResultSet features

```java
/**
 * Assignment 4: Advanced ResultSet Operations
 * Create a comprehensive student report system
 */
public class StudentReportGenerator {
    
    /**
     * Generate a detailed student report with statistics
     */
    public void generateStudentReport() {
        // TODO: Implement the following features:
        
        // 1. Count total students
        // 2. Find average age
        // 3. Group students by course
        // 4. Find oldest and youngest student
        // 5. List students sorted by age
        // 6. Handle NULL values properly
        // 7. Use scrollable ResultSet to navigate
        
        // Expected output format:
        /*
        ========== STUDENT REPORT ==========
        Total Students: 25
        Average Age: 21.4
        Age Range: 18-28
        
        === COURSE DISTRIBUTION ===
        Computer Science: 12 students
        Engineering: 8 students
        Mathematics: 5 students
        
        === STUDENTS BY AGE (ASCENDING) ===
        John Doe (18) - Computer Science
        Jane Smith (19) - Engineering
        ...
        
        === DETAILED STATISTICS ===
        Oldest Student: Bob Johnson (28) - Mathematics
        Youngest Student: John Doe (18) - Computer Science
        */
    }
    
    /**
     * Search students with advanced filtering
     */
    public void advancedStudentSearch(String namePattern, String course, 
                                     int minAge, int maxAge) {
        // TODO: Implement advanced search with multiple criteria
        // Use PreparedStatement with multiple parameters
        // Handle empty/null parameters appropriately
    }
    
    public static void main(String[] args) {
        StudentReportGenerator generator = new StudentReportGenerator();
        generator.generateStudentReport();
        generator.advancedStudentSearch("John%", "Computer Science", 18, 25);
    }
}
```

---

## 🧠 Core Concept #5: Exception Handling (The Safety Net)

### SQLException Hierarchy
```
Throwable
  └── Exception
      └── SQLException
          ├── SQLNonTransientException
          ├── SQLTransientException
          ├── SQLRecoverableException
          └── SQLWarning
```

### Key SQLException Methods
```java
try {
    // Database operations
} catch (SQLException e) {
    System.out.println("Error Code: " + e.getErrorCode());
    System.out.println("SQL State: " + e.getSQLState());
    System.out.println("Message: " + e.getMessage());
    
    // Print stack trace for debugging
    e.printStackTrace();
}
```

### Common SQL States to Remember
- **08001**: Connection error
- **23000**: Constraint violation (duplicate key, etc.)
- **42000**: Syntax error
- **HY000**: General error

---

## 🎯 Complete Practice Project: Library Management System

### Final Assignment: Build Complete Library System
Create a full-featured library management system using all JDBC concepts:

#### Database Schema:
```sql
-- Authors table
CREATE TABLE authors (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    birth_year INT,
    biography TEXT
);

-- Books table  
CREATE TABLE books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    publication_year INT,
    price DECIMAL(10,2),
    copies_available INT DEFAULT 1,
    author_id INT,
    FOREIGN KEY (author_id) REFERENCES authors(author_id)
);

-- Members table
CREATE TABLE members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    phone VARCHAR(15),
    join_date DATE DEFAULT CURRENT_DATE,
    membership_type ENUM('BASIC', 'PREMIUM') DEFAULT 'BASIC'
);

-- Book lending table
CREATE TABLE book_loans (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT,
    member_id INT,
    loan_date DATE DEFAULT CURRENT_DATE,
    return_date DATE,
    due_date DATE,
    fine_amount DECIMAL(10,2) DEFAULT 0,
    status ENUM('ACTIVE', 'RETURNED', 'OVERDUE') DEFAULT 'ACTIVE',
    FOREIGN KEY (book_id) REFERENCES books(book_id),
    FOREIGN KEY (member_id) REFERENCES members(member_id)
);
```

#### Required Features to Implement:
1. **Author Management**: CRUD operations with validation
2. **Book Management**: CRUD with inventory tracking
3. **Member Management**: Registration and profile management
4. **Book Lending System**: Check-out/return with due dates
5. **Fine Calculation**: Automatic fine calculation for overdue books
6. **Search & Reports**: Advanced search and reporting features
7. **Transaction Management**: Proper transaction handling
8. **Error Handling**: Comprehensive error handling
9. **Connection Pooling**: Efficient connection management

This comprehensive learning document gives you everything you need to master JDBC concepts with detailed explanations, practical examples, and hands-on assignments. Each concept builds upon the previous one, ensuring you understand not just the "how" but also the "why" behind JDBC operations.