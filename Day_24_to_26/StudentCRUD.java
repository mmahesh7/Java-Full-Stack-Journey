package Day_24_to_26;

import java.sql.*;

//First create this table in database
/*
create table students(
	student_id Int primary key auto_increment,
    name varchar(20) not null,
    email varchar(30) unique,
    age int,
    course varchar(50)
);
 */
public class StudentCRUD {
    private static final String URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mahesh@mySQL";


    //* Create a new student
    public int createStudent(String name, String email, int age, String course) {
        String query = "Insert into students (name,email,age,course) values (?,?,?,?);";
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, age);
            pstmt.setString(4, course);

            int rowsAdded = pstmt.executeUpdate();

            int generatedId = -1;
            if(rowsAdded > 0) {
                ResultSet keys = pstmt.getGeneratedKeys();
                if(keys.next()) {
                    generatedId = keys.getInt(1);
                }
                keys.close();
            }
            pstmt.close();
            conn.close();

            return generatedId;

        } catch (SQLException e) {
            System.out.println("Error creating student: " + e.getMessage());
            return -1;
        }
    }

    //* Read student by ID
    public String getStudentById(int studentId) {
        String query = "SELECT * FROM Students where student_id = ?;";
        try {
            Connection conn = DriverManager.getConnection(URL,USERNAME,PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, studentId);
            ResultSet res = pstmt.executeQuery();
            String studentInfo = null;
            if(res.next()){
                int id = res.getInt("student_id");
                String name = res.getString("name");
                String email = res.getString("email");
                int age = res.getInt("age");
                String course = res.getString("course");

                studentInfo = "ID: " + id + ", Name: " + name + ", Email: " + email +
                        ", Age: " + age + ", Course: " + course;
            }
            res.close();
            pstmt.close();
            conn.close();

            return studentInfo;
        } catch (SQLException e) {
            System.out.println("Error finding student: " + e.getMessage());
            return null;
        }
    }

    // * Update a student's information
    public int updateStudent(int studentId, String name, String email, int age, String course) {
        String query = "Update students set name = ?, email = ?, age = ?, course = ? where student_id = ?;";
        try{
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);

            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, name);
            pstmt.setString(2, email);
            pstmt.setInt(3, age);
            pstmt.setString(4, course);
            pstmt.setInt(5, studentId);

            int rowsUpdated = pstmt.executeUpdate();

            pstmt.close();
            conn.close();

            return rowsUpdated;
        } catch (SQLException e) {
            System.out.println("Error updating student: " + e.getMessage());
            return 0;
        }
    }

    //* Delete a student from the database
    public int deleteStudent(int studentId) {
        String query = "DELETE FROM Students where student_id = ?;";
        try {
            Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, studentId);

            int rowsDeleted = pstmt.executeUpdate();

            pstmt.close();
            conn.close();

            return rowsDeleted;

        } catch (SQLException e) {
            System.out.println("Error deleting student: " + e.getMessage());
            return 0;
        }
    }

    // * Show all students in the database
    public void getAllStudents() {
        String query = "select * from students;";

        try {
            Connection conn = DriverManager.getConnection(URL,USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            System.out.println("\n--- All Students ---");

            boolean foundAny = false;
            while(rs.next()) {
                foundAny = true;
                int id = rs.getInt("student_id");
                String name = rs.getString("name");
                String email = rs.getString("email");
                int age = rs.getInt("age");
                String course = rs.getString("course");

                System.out.println("ID: " + id + " | Name: " + name + " | Email: " + email +
                        " | Age: " + age + " | Course: " + course);
            }
            if(!foundAny) {
                System.out.println("No students found in the database.");
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println("Error getting all students: " + e.getMessage());
        }
    }

    // Test all our methods
    public static void main(String[] args) {
        StudentCRUD crud = new StudentCRUD();

        System.out.println("=== Testing Student Management System ===");

        // Test 1: Create a new student
        System.out.println("\n1. Creating a new student...");
        int newStudentId = crud.createStudent("John Doe", "john@email.com", 20, "Computer Science");

        if (newStudentId != -1) {
            System.out.println("SUCCESS! Created student with ID: " + newStudentId);
        } else {
            System.out.println("FAILED to create student");
            return; // Stop testing if this fails
        }

        // Test 2: Find the student we just created
        System.out.println("\n2. Finding the student we just created...");
        String studentInfo = crud.getStudentById(newStudentId);
        if (studentInfo != null) {
            System.out.println("FOUND: " + studentInfo);
        } else {
            System.out.println("Could not find the student");
        }

        // Test 3: Update the student
        System.out.println("\n3. Updating the student's information...");
        int updated = crud.updateStudent(newStudentId, "John Smith", "johnsmith@email.com", 21, "Software Engineering");
        if (updated > 0) {
            System.out.println("SUCCESS! Updated the student");
            // Show the updated info
            String updatedInfo = crud.getStudentById(newStudentId);
            System.out.println("New info: " + updatedInfo);
        } else {
            System.out.println("FAILED to update student");
        }

        // Test 4: Create more students for testing
        System.out.println("\n4. Creating more test students...");
        crud.createStudent("Jane Smith", "jane@email.com", 19, "Mathematics");
        crud.createStudent("Bob Johnson", "bob@email.com", 22, "Physics");

        // Test 5: Show all students
        System.out.println("\n5. Showing all students:");
        crud.getAllStudents();

        // Test 6: Delete a student
        System.out.println("\n6. Deleting the first student we created...");
        int deleted = crud.deleteStudent(newStudentId);
        if (deleted > 0) {
            System.out.println("SUCCESS! Deleted the student");
            System.out.println("Remaining students:");
            crud.getAllStudents();
        } else {
            System.out.println("FAILED to delete student");
        }

        System.out.println("\n=== Testing Complete! ===");
    }
}
