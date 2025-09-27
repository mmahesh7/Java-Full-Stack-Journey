# MVC Design Pattern with Servlet & JSP

## Table of Contents
1. [Introduction to MVC Design Pattern](#1-introduction-to-mvc-design-pattern)
2. [MVC Components](#2-mvc-components)
3. [Static vs Dynamic Pages](#3-static-vs-dynamic-pages)
4. [MVC Implementation with Servlet & JSP](#4-mvc-implementation-with-servlet--jsp)
5. [Model - POJO Classes](#5-model---pojo-classes)
6. [View - JSP Pages](#6-view---jsp-pages)
7. [Controller - Servlets](#7-controller---servlets)
8. [Service Layer](#8-service-layer)
9. [DAO Layer](#9-dao-layer)

---

## 1. Introduction to MVC Design Pattern

### Definition
**MVC (Model-View-Controller)** is a design pattern that separates an application into three interconnected components: Model (data), View (presentation), and Controller (logic). This separation helps organize code better and makes maintenance easier.

### Why MVC?
- **Separation of Concerns**: Different responsibilities are handled by different components
- **Better Maintenance**: Changes in one layer don't affect others
- **Code Reusability**: Components can be reused across different parts of application
- **Team Development**: Different developers can work on different layers

### Use Cases
- Web applications
- Enterprise applications
- Large-scale projects
- Applications requiring frequent UI changes
- Multi-developer projects

### Basic Flow
```
Client Request → Controller → Model → Controller → View → Client Response
```

---

## 2. MVC Components

### Model
**Definition**: Represents data and business logic of the application.

**Responsibilities**:
- Hold application data
- Define data structure
- No processing logic (just data container)

**Implementation**: Java POJO classes

### View
**Definition**: Handles the presentation layer - what user sees.

**Responsibilities**:
- Display data to user
- Handle user interface
- No business logic
- Only concerned with presentation

**Implementation**: JSP pages, HTML

### Controller
**Definition**: Handles user input and coordinates between Model and View.

**Responsibilities**:
- Accept user requests
- Process requests
- Call appropriate Model
- Forward to appropriate View
- Control application flow

**Implementation**: Servlet classes

---

## 3. Static vs Dynamic Pages

### Static Pages
**Definition**: Pre-built HTML pages that don't change regardless of user or request.

**Characteristics**:
- Same content for all users
- No server-side processing
- Fast loading
- Easy to cache

**Example**:
```html
<html>
<body>
    <h1>Welcome to Our Website</h1>
    <p>This content never changes</p>
</body>
</html>
```

### Dynamic Pages
**Definition**: Pages that change based on user input, database data, or other factors.

**Characteristics**:
- Content varies per user/request
- Server-side processing required
- Personalized content
- Real-time data

**Example**: Facebook feed, Amazon product recommendations, user profiles

---

## 4. MVC Implementation with Servlet & JSP

### Technology Mapping
- **Model**: Java POJO (Plain Old Java Object)
- **View**: JSP (Java Server Pages)
- **Controller**: Servlet

### Architecture Flow
```
Client → Servlet (Controller) → POJO (Model) → JSP (View) → Client
```

### Sample Request Flow
1. User sends request to servlet
2. Servlet processes request
3. Servlet creates/fetches model data
4. Servlet forwards request to JSP with model
5. JSP displays data using model
6. Response sent to client

---

## 5. Model - POJO Classes

### Definition
**POJO (Plain Old Java Object)** is a simple Java class that holds data without any special restrictions or frameworks.

### Characteristics
- Simple Java class
- Private fields with getters/setters
- No business logic
- Only data container
- Serializable (optional)

### Syntax
```java
public class ModelClass {
    private dataType field1;
    private dataType field2;
    
    // Constructors
    // Getters and Setters
    // toString() method (optional)
}
```

### Use Cases
- Represent database entities
- Transfer data between layers
- Hold form data
- Store user information

### Sample Code - Student Model
```java
public class Student {
    private int rollNumber;
    private String name;
    private int marks;
    
    public Student() {}
    
    public Student(int rollNumber, String name, int marks) {
        this.rollNumber = rollNumber;
        this.name = name;
        this.marks = marks;
    }
    
    // Getters and Setters
    public int getRollNumber() { return rollNumber; }
    public void setRollNumber(int rollNumber) { this.rollNumber = rollNumber; }
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public int getMarks() { return marks; }
    public void setMarks(int marks) { this.marks = marks; }
}
```

---

## 6. View - JSP Pages

### Definition
JSP pages handle the presentation layer, displaying data received from controller without containing business logic.

### Characteristics
- Contains HTML structure
- Displays model data
- No business processing
- Uses JSP tags and EL (Expression Language)
- Focus only on presentation

### Syntax
```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<body>
    <h1>Display Title</h1>
    <!-- Display model data here -->
    <p>Data: ${modelAttribute}</p>
</body>
</html>
```

### Use Cases
- User interfaces
- Reports and dashboards
- Form displays
- Data presentation pages

### Sample Code - Student Profile View
```jsp
<%@ page contentType="text/html; charset=UTF-8" %>
<html>
<body>
    <h1>Student Profile</h1>
    
    <%
        Student student = (Student) request.getAttribute("student");
        if(student != null) {
    %>
    
    <p>Roll Number: <%= student.getRollNumber() %></p>
    <p>Name: <%= student.getName() %></p>
    <p>Marks: <%= student.getMarks() %></p>
    
    <% } else { %>
        <p>No student data found.</p>
    <% } %>
</body>
</html>
```

---

## 7. Controller - Servlets

### Definition
Servlets act as controllers that handle user requests, process them, and coordinate between Model and View.

### Responsibilities
- Accept HTTP requests
- Process request parameters
- Call service/business logic
- Create/fetch model objects
- Forward to appropriate view
- Handle errors

### Syntax
```java
@WebServlet("/controllerURL")
public class ControllerServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Process request
        // Create/fetch model
        // Forward to view
    }
}
```

### Use Cases
- Handle form submissions
- Process user authentication
- Manage application workflow
- Route requests to appropriate views

### Sample Code - Student Controller
```java
@WebServlet("/student")
public class StudentController extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // Get roll number from request
        String rollNumberStr = request.getParameter("rollNumber");
        int rollNumber = Integer.parseInt(rollNumberStr);
        
        // Create student object (normally from database)
        Student student = new Student(rollNumber, "John Doe", 85);
        
        // Set student as request attribute
        request.setAttribute("student", student);
        
        // Forward to JSP view
        RequestDispatcher dispatcher = request.getRequestDispatcher("/studentProfile.jsp");
        dispatcher.forward(request, response);
    }
}
```

---

## 8. Service Layer

### Definition
Service layer contains business logic and acts as an intermediary between Controller and Data Access layer.

### Responsibilities
- Business logic implementation
- Transaction management
- Data validation
- Coordinate with DAO layer
- Process business rules

### Characteristics
- No direct database operations
- Calls DAO methods
- Contains business rules
- Stateless operations

### Use Cases
- Complex business calculations
- Data validation
- Multi-step operations
- Business rule enforcement

### Sample Code - Student Service
```java
public class StudentService {
    
    // Get student (normally from database)
    public Student getStudent(int rollNumber) {
        // Business logic - simple validation
        if (rollNumber <= 0) {
            return null;
        }
        
        // In real app, this would call DAO
        // For demo, creating student object
        return new Student(rollNumber, "John Doe", 85);
    }
    
    // Save student
    public boolean saveStudent(Student student) {
        // Basic validation
        if (student == null || student.getName() == null) {
            return false;
        }
        
        // In real app, would save to database via DAO
        return true;
    }
}
```

---

## 9. DAO Layer

### Definition
**DAO (Data Access Object)** layer handles all database operations and provides an abstraction layer for data access.

### Responsibilities
- Database connectivity
- CRUD operations
- SQL query execution
- Data mapping
- Transaction handling

### Characteristics
- Direct database interaction
- Contains SQL queries
- No business logic
- Returns model objects

### Use Cases
- Database operations
- Data persistence
- Query execution
- Connection management

### Sample Code - Student DAO
```java
public class StudentDAO {
    
    // Get student by roll number (simplified)
    public Student getStudentByRollNumber(int rollNumber) {
        // In real application, this would query database
        // For demo, returning hardcoded student
        if (rollNumber == 101) {
            return new Student(101, "John Doe", 85);
        }
        return null;
    }
    
    // Save student to database (simplified)
    public boolean saveStudent(Student student) {
        // In real application, would execute SQL INSERT
        // For demo, just return true
        System.out.println("Saving student: " + student.getName());
        return true;
    }
}
```

---

### Complete Request Flow Example
1. **User Input**: User enters roll number "101" in index.html
2. **Controller**: StudentController receives GET request
3. **Service**: StudentService validates roll number and calls DAO
4. **DAO**: StudentDAO queries database and returns Student object
5. **Controller**: Receives Student object, sets as request attribute
6. **View**: JSP displays student data using model object
7. **Response**: User sees formatted student profile page

### Key Benefits of This MVC Implementation
- **Separation**: Each layer has distinct responsibility
- **Maintainability**: Easy to modify one layer without affecting others
- **Testability**: Each component can be tested independently
- **Scalability**: Easy to add new features
- **Reusability**: Components can be reused across different parts

---

## Quick Reference

### MVC Layer Responsibilities
- **Model**: Data structure (POJO classes)
- **View**: Presentation (JSP pages)
- **Controller**: Request handling (Servlets)
- **Service**: Business logic (Java classes)
- **DAO**: Data access (Database operations)

### Best Practices
- Never put business logic in JSP pages
- Keep controllers lightweight
- Use service layer for complex operations
- Implement proper error handling
- Follow naming conventions
- Use connection pooling for database operations