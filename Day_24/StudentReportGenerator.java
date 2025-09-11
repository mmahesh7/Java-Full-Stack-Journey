package Day_24;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


/**
 * Assignment 4: Advanced ResultSet Operations
 * Create a comprehensive student report system
 * // Expected output format:
 *
 *         ========== STUDENT REPORT ==========
 *         Total Students: 25
 *         Average Age: 21.4
 *         Age Range: 18-28
 *
 *         === COURSE DISTRIBUTION ===
 *         Computer Science: 12 students
 *         Engineering: 8 students
 *         Mathematics: 5 students
 *
 *         === STUDENTS BY AGE (ASCENDING) ===
 *         John Doe (18) - Computer Science
 *         Jane Smith (19) - Engineering
 *         ...
 *
 *         === DETAILED STATISTICS ===
 *         Oldest Student: Bob Johnson (28) - Mathematics
 *         Youngest Student: John Doe (18) - Computer Science
 *
 */

public class StudentReportGenerator {

    private static final String URL = "jdbc:mysql://localhost:3306/jdbc";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "Mahesh@mySQL";

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    /**
     * Generate a detailed student report with statistics
     */
    public void generateStudentReport() {
        try(Connection conn = getConnection()) {
            System.out.println("========== STUDENT REPORT ==========");
            // 1. Count total students
            int totalStudents = getTotalStudents(conn);
            System.out.println("Total Students: "+ totalStudents);

            // 2. Find average age
            double averageAge = getAverageAge(conn);
            System.out.println("Average");
            // 3. Group students by course
            int[] ageRange = getAgeRange(conn);
            System.out.println("Age Range: " + ageRange[0] + "-" + ageRange[1]);

            System.out.println();

            // 4. Group students by course
            System.out.println("=== COURSE DISTRIBUTION ===");
            getCourseDistribution(conn);

            System.out.println();

            // 5. List students sorted by age
            System.out.println("=== STUDENTS BY AGE (ASCENDING) ===");
            getStudentsByAge(conn);

            System.out.println();

            // 6. Detailed statistics with oldest and youngest
            System.out.println("=== DETAILED STATISTICS ===");
            getDetailedStatistics(conn);

        } catch (SQLException e) {
            System.err.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //Count total students
    private int getTotalStudents(Connection conn) throws SQLException{
        String query = "SELECT COUNT(*) as total from students";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if(rs.next()) {
                return rs.getInt("total");
            }
            return 0;
        }
    }

    //Calculating Average Age (handling null values)
    private double getAverageAge(Connection conn) throws SQLException {
        String query = "SELect AVG(age) as avg_age from students;";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            if(rs.next()) {
                return rs.getInt("avg_age");
            }
            return 0.0;
        }
    }

    //Get age range (min and max age)
    private int[] getAgeRange(Connection conn) throws SQLException {
        String query = "SELECT MIn(age) as min_age , Max(age) as max_age from students;";
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery();) {
            if(rs.next()) {
                return new int[]{rs.getInt("min_age"), rs.getInt("max_age")};
            }
            return new int[]{0,0};
        }
    }

    //Grouping students by course - Get Course Distribution
    private void getCourseDistribution(Connection conn) throws SQLException{
        String query = """
                SELECT course, count(*) as student_count from students
                where course is not null and course <> 'null' group by course order by student_count DESC
                """;
        try (PreparedStatement pstmt = conn.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String course = rs.getString("course");
                int cnt = rs.getInt("student_count");
                System.out.println(course + ": " + cnt + " students");
            }
        }
    }

    // Get students sorted by age using scrollable ResultSet
    private void getStudentsByAge(Connection conn) throws SQLException{
        String query = """
                         SELECT name, age, course from students
                         where age is not null order by age;
                       """;
        try (PreparedStatement pstmt = conn.prepareStatement(query);
            ResultSet rs = pstmt.executeQuery();) {
            while(rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String course = rs.getString("course");

                course = (course != null) ? course : "No course assigned !!!";
                System.out.println(name+" ("+age+") - "+ course);
            }
        }
    }

    private void getDetailedStatistics(Connection conn) throws SQLException {
        String oldestSql = """
                        SElect name, age, course from students
                         where age = (select max(age) from students where age is not null) limit 1;
                        """;
        try (PreparedStatement pstmt = conn.prepareStatement(oldestSql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String course = rs.getString("course");
                course = (course != null) ? course : "No Course";
                System.out.println("Oldest Student: " + name + " (" + age + ") - " + course);
            }
        }

        // Get youngest student
        String youngestSql = "SELECT name, age, course FROM students WHERE age = " +
                "(SELECT MIN(age) FROM students WHERE age IS NOT NULL) LIMIT 1";

        try (PreparedStatement pstmt = conn.prepareStatement(youngestSql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                String name = rs.getString("name");
                int age = rs.getInt("age");
                String course = rs.getString("course");
                course = (course != null) ? course : "No Course";
                System.out.println("Youngest Student: " + name + " (" + age + ") - " + course);
            }
        }
    }

    /**
     * Search students with advanced filtering
     */
    public void advancedStudentSearch(String namePattern, String course,
                                      int minAge, int maxAge) throws SQLException{

        System.out.println("\n========== ADVANCED SEARCH RESULTS ==========");
        System.out.printf("Search Criteria: Name='%s', Course='%s', Age Range=%d-%d%n",
                (namePattern == null ? "ANY" : namePattern),
                (course == null ? "ANY" : course),
                minAge, maxAge);
        System.out.println("=" .repeat(50));

        try (Connection conn = getConnection()) {
            StringBuilder sql = new StringBuilder("select * from students where 1=1 ");
            List<Object> parameters = new ArrayList<>();

            if (namePattern != null && !namePattern.trim().isEmpty()) {
                sql.append("and name like ? ");
                parameters.add(namePattern);
            }

            if (course != null && !course.trim().isEmpty()) {
                sql.append(" and course = ? ");
                parameters.add(course);
            }

            if (minAge > 0) {
                sql.append("and age >= ? ");
                parameters.add(minAge);
            }

            if (maxAge > 0 && maxAge >= minAge) {
                sql.append("and age <= ? ");
                parameters.add(maxAge);
            }

            sql.append("order by name;");

            try (PreparedStatement pstmt = conn.prepareStatement(sql.toString())) {

                //Set parameters
                for (int i=0; i<parameters.size(); i++) {
                    pstmt.setObject(i+1, parameters.get(i));
                }

                try (ResultSet rs = pstmt.executeQuery()) {
                    int cnt = 0;

                    while(rs.next()) {
                        cnt++;
                        int studentId = rs.getInt("student_id");
                        String name = rs.getString("name");
                        String email = rs.getString("email");
                        int age = rs.getInt("age");
                        String studentCourse = rs.getString("course");

                        //Handle NULL values
                        email = (email != null) ? email : "No Email";
                        studentCourse = (studentCourse != null) ? studentCourse : "No Course";
                        String ageStr = (rs.wasNull()) ? "Unknown" : String.valueOf(age);

                        System.out.printf("ID: %-3d | %-15s | Age: %-7s | Course: %-20s | Email: %s%n",
                                studentId, name, ageStr, studentCourse, email);
                    }

                    System.out.println("-" .repeat(50));
                    System.out.println("Total results found: "+ cnt);

                    if(cnt == 0) {
                        System.out.println("No students match the search criteria.");
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Search error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws SQLException {
        StudentReportGenerator generator = new StudentReportGenerator();
        generator.generateStudentReport();
        generator.advancedStudentSearch("A%", null, 0, 0);  // Names starting with 'A'
        generator.advancedStudentSearch(null, "Computer Science", 18, 25);  // CS students aged 18-25
        generator.advancedStudentSearch("John%", "Computer Science", 18, 25);  // Specific search
    }
}