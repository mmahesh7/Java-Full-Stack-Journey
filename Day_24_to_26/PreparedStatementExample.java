package Day_24_to_26;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PreparedStatementExample {
    private static final String URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mahesh@mySQL";

    public void searchStudentsByName(String searchName) {
        String sql = "SELECT student_id, name, email, course FROM students WHERE name LIKE ?";

        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
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

    // * Batch insert multiple students
    public void insertMultipleStudents(List<Student> students) {
        String query = "INSERT INto students  (name, email, age, course) VALUES (?, ?, ?, ?);";

        try(Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            PreparedStatement pstmt = connection.prepareStatement(query);){

            connection.setAutoCommit(false);

            for (Student student : students) {
                pstmt.setString(1, student.getName());
                pstmt.setString(2, student.getEmail());
                pstmt.setInt(3, student.getAge());
                pstmt.setString(4, student.getCourse());

                pstmt.addBatch();
            }

            int[] results = pstmt.executeBatch();
            connection.commit();

            System.out.println("Inserted "+ results.length + " students successfully!");
        }catch (SQLException e) {
            System.err.println("Batch insert error: "+e.getMessage());
        }
    }
}



// Student class for reference
class Student {
    private String name;
    private String email;
    private int age;
    private String course;

    // Constructors
    public Student() {}

    public Student(String name, String email, int age, String course) {
        this.name = name;
        this.email = email;
        this.age = age;
        this.course = course;
    }

    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public String getCourse() { return course; }
    public void setCourse(String course) { this.course = course; }

    @Override
    public String toString() {
        return String.format("Student{name='%s', email='%s', age=%d, course='%s'}",
                name, email, age, course);
    }

    public static void main(String[] args) {
        PreparedStatementExample example = new PreparedStatementExample();

        // Create sample students
        List<Student> students = new ArrayList<>();
        students.add(new Student("Mahesh", "mmahesh@23022@gmail.com", 20, "Computer Science"));
        students.add(new Student("Anuvind", "anuvind233@gmail.com", 21, "Cyber Security"));
        students.add(new Student("Koushik", "mkoushik@apple.com", 21, "Data Science"));

        // Insert students into DB
//        example.insertMultipleStudents(students);

        // Search by name
        example.searchStudentsByName("Mahesh");
        example.searchStudentsByName("Anuvind");
    }
}
