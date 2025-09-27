# Complete Servlet & JSP Tutorial Notes
---

## Introduction to Servlets

### What is a Servlet?
A **Servlet** is a Java class that handles requests and generates responses in web applications. It runs on the server side and extends the capabilities of servers hosting Java-based web applications.

### Key Features
- Platform independent
- Server-side component
- Handles HTTP requests/responses
- Better performance than CGI
- Thread-based (multiple requests handled simultaneously)

### Servlet Architecture
```
Client Browser → HTTP Request → Web Server → Web Container → Servlet → Response → Client
```

---

## Development Environment Setup

### Required Components
1. **JDK** (Java Development Kit)
2. **IDE** (Eclipse/IntelliJ/NetBeans)
3. **Web Server** (Apache Tomcat)

### Eclipse Setup Steps

#### 1. Download and Install Eclipse IDE for Java EE
- Download from eclipse.org
- Choose "Eclipse IDE for Java EE Developers"
- Extract and run eclipse.exe

#### 2. Workspace Configuration
```
Create a dedicated folder for your projects (e.g., "ServletWorkspace")
```

#### 3. Switch to Java EE Perspective
- Window → Perspective → Open Perspective → Java EE
- This provides web development specific views and tools

### Tomcat Configuration

#### 1. Download Apache Tomcat
- Visit tomcat.apache.org
- Download Tomcat 8.0 or later
- Extract to a folder (e.g., C:\apache-tomcat-8.0.x)

#### 2. Configure in Eclipse
```java
// Steps:
1. Go to Window → Show View → Servers
2. Right-click in Servers tab → New → Server
3. Select Apache → Tomcat v8.0 Server → Next
4. Browse to Tomcat installation directory
5. Click Finish
```

#### 3. Test Tomcat
```java
// To verify Tomcat is working:
1. Right-click on server → Start
2. Open browser → localhost:8080
3. Should see Tomcat welcome page
```

---

## Creating Web Projects

### Dynamic Web Project Creation

#### Step 1: Create Project
```
File → New → Dynamic Web Project
Project Name: FirstWebApp
Target Runtime: Apache Tomcat v8.0
Dynamic Web Module Version: 3.1
```

#### Step 2: Project Structure
```
FirstWebApp/
├── src/                    // Java source files
├── WebContent/            // Web resources
│   ├── WEB-INF/
│   │   ├── web.xml       // Deployment descriptor
│   │   └── lib/          // JAR files
│   ├── META-INF/
│   └── index.html        // Welcome page
```

#### Step 3: Create HTML Page
```html
<!-- WebContent/index.html -->
<!DOCTYPE html>
<html>
<head>
    <title>First Web App</title>
</head>
<body>
    <h1>Welcome to Servlet Tutorial</h1>
    <form action="add" method="get">
        Number 1: <input type="text" name="num1"><br><br>
        Number 2: <input type="text" name="num2"><br><br>
        <input type="submit" value="Add">
    </form>
</body>
</html>
```

---

## Servlet Configuration

### Method 1: web.xml Configuration

#### Basic Servlet Class
```java
package com.telusko;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class AddServlet extends HttpServlet {
    
    public void service(HttpServletRequest req, HttpServletResponse res) 
            throws IOException {
        
        int i = Integer.parseInt(req.getParameter("num1"));
        int j = Integer.parseInt(req.getParameter("num2"));
        int k = i + j;
        
        PrintWriter out = res.getWriter();
        out.println("Result is: " + k);
    }
}
```

#### web.xml Configuration
```xml
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee"
         version="3.1">
  
  <display-name>FirstWebApp</display-name>
  
  <!-- Servlet Declaration -->
  <servlet>
    <servlet-name>AddServlet</servlet-name>
    <servlet-class>com.telusko.AddServlet</servlet-class>
  </servlet>
  
  <!-- Servlet Mapping -->
  <servlet-mapping>
    <servlet-name>AddServlet</servlet-name>
    <url-pattern>/add</url-pattern>
  </servlet-mapping>
  
</web-app>
```

### Method 2: Annotation Configuration (Servlet 3.0+)
```java
package com.telusko;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/add")
public class AddServlet extends HttpServlet {
    
    protected void service(HttpServletRequest req, HttpServletResponse res) 
            throws IOException {
        
        int i = Integer.parseInt(req.getParameter("num1"));
        int j = Integer.parseInt(req.getParameter("num2"));
        int k = i + j;
        
        PrintWriter out = res.getWriter();
        out.println("Result is: " + k);
    }
}
```

---

## HTTP Methods: GET vs POST

### doGet() Method
Handles HTTP GET requests - data sent via URL parameters.

```java
@Override
protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException {
    
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    
    String name = req.getParameter("name");
    out.println("<h1>Hello " + name + " from GET method!</h1>");
}
```

### doPost() Method
Handles HTTP POST requests - data sent in request body.

```java
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException {
    
    res.setContentType("text/html");
    PrintWriter out = res.getWriter();
    
    String username = req.getParameter("username");
    String password = req.getParameter("password");
    
    out.println("<h1>Login Attempt</h1>");
    out.println("<p>Username: " + username + "</p>");
    // Note: Never display password in real applications!
}
```

### GET vs POST Comparison

| Feature | GET | POST |
|---------|-----|------|
| Data Location | URL parameters | Request body |
| Visibility | Visible in browser | Hidden |
| Security | Less secure | More secure |
| Data Size Limit | ~2KB (browser dependent) | No practical limit |
| Caching | Can be cached | Not cached |
| Bookmarking | Can be bookmarked | Cannot be bookmarked |
| Back Button | Safe | May resubmit data |
| Use Cases | Search, read data | Form submission, file upload |

---

## Request and Response Objects

### HttpServletRequest Theory
The request object contains all information about the HTTP request from the client.

#### Key Methods
```java
// Parameter handling
String value = request.getParameter("paramName");
String[] values = request.getParameterValues("paramName");

// Request information
String method = request.getMethod();
String uri = request.getRequestURI();
String url = request.getRequestURL().toString();
String contextPath = request.getContextPath();

// Session and attributes
HttpSession session = request.getSession();
request.setAttribute("key", value);
Object value = request.getAttribute("key");
```

### HttpServletResponse Theory
The response object is used to send data back to the client.

#### Key Methods
```java
// Content and headers
response.setContentType("text/html");
response.setCharacterEncoding("UTF-8");

// Output streams
PrintWriter out = response.getWriter();

// Redirection
response.sendRedirect("otherServlet");
```

---

## Servlet Communication

### RequestDispatcher - Forward Method

#### Theory
RequestDispatcher forwards the request to another resource on the server side. The client makes only one request.

#### Implementation
```java
// First Servlet - AddServlet
@WebServlet("/add")
public class AddServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        int i = Integer.parseInt(req.getParameter("num1"));
        int j = Integer.parseInt(req.getParameter("num2"));
        int k = i + j;
        
        // Store result in request attribute
        req.setAttribute("result", k);
        
        // Forward to square servlet
        RequestDispatcher rd = req.getRequestDispatcher("/square");
        rd.forward(req, res);
    }
}

// Second Servlet - SquareServlet  
@WebServlet("/square")
public class SquareServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        // Get result from request attribute
        int k = (int) req.getAttribute("result");
        k = k * k;
        
        PrintWriter out = res.getWriter();
        out.println("Square result is: " + k);
    }
}
```

### SendRedirect Method

#### Theory
SendRedirect sends a redirect response to the client, causing the client to make a new request to a different URL.

#### Implementation
```java
@WebServlet("/add")
public class AddServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        int i = Integer.parseInt(req.getParameter("num1"));
        int j = Integer.parseInt(req.getParameter("num2"));
        int k = i + j;
        
        // Redirect with URL rewriting to pass data
        res.sendRedirect("square?result=" + k);
    }
}

@WebServlet("/square")
public class SquareServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        // Get result from URL parameter
        int k = Integer.parseInt(req.getParameter("result"));
        k = k * k;
        
        PrintWriter out = res.getWriter();
        out.println("Square result is: " + k);
    }
}
```

### RequestDispatcher vs SendRedirect

| Aspect | RequestDispatcher | SendRedirect |
|--------|------------------|--------------|
| Number of requests | 1 | 2 |
| URL change in browser | No | Yes |
| Data sharing | Request attributes | URL parameters/Session |
| Performance | Faster | Slower |
| Use case | Internal navigation | External URLs |

---

## Session Management

### URL Rewriting
Passing data through URL parameters.

```java
// Sending data
response.sendRedirect("nextServlet?data=" + value + "&name=" + name);

// Receiving data
String data = request.getParameter("data");
String name = request.getParameter("name");
```

### HttpSession
Server-side session management.

```java
// Creating and using session
@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        String username = req.getParameter("username");
        
        // Get session (create if doesn't exist)
        HttpSession session = req.getSession();
        
        // Store data in session
        session.setAttribute("username", username);
        session.setAttribute("loginTime", new Date());
        
        // Set session timeout (in seconds)
        session.setMaxInactiveInterval(30 * 60); // 30 minutes
        
        res.sendRedirect("welcome");
    }
}

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        HttpSession session = req.getSession(false);
        PrintWriter out = res.getWriter();
        
        if (session != null) {
            String username = (String) session.getAttribute("username");
            out.println("Welcome " + username);
            
            // Remove specific attribute
            // session.removeAttribute("someKey");
            
            // Invalidate entire session
            // session.invalidate();
        } else {
            out.println("Please login first");
        }
    }
}
```

### Cookies
Client-side data storage.

```java
// Setting cookies
@WebServlet("/setCookie")
public class SetCookieServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        String username = req.getParameter("username");
        
        // Create cookie
        Cookie cookie = new Cookie("username", username);
        cookie.setMaxAge(24 * 60 * 60); // 1 day
        cookie.setPath("/"); // Available for entire application
        
        // Add cookie to response
        res.addCookie(cookie);
        
        PrintWriter out = res.getWriter();
        out.println("Cookie set successfully");
    }
}

// Reading cookies
@WebServlet("/readCookie")
public class ReadCookieServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        Cookie[] cookies = req.getCookies();
        PrintWriter out = res.getWriter();
        
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("username".equals(cookie.getName())) {
                    out.println("Welcome back " + cookie.getValue());
                    return;
                }
            }
        }
        out.println("No username cookie found");
    }
}
```

---

## ServletConfig and ServletContext

### ServletConfig
Configuration specific to a single servlet.

#### web.xml Configuration
```xml
<servlet>
    <servlet-name>ConfigServlet</servlet-name>
    <servlet-class>com.telusko.ConfigServlet</servlet-class>
    <init-param>
        <param-name>driver</param-name>
        <param-value>com.mysql.jdbc.Driver</param-value>
    </init-param>
    <init-param>
        <param-name>url</param-name>
        <param-value>jdbc:mysql://localhost:3306/testdb</param-value>
    </init-param>
</servlet>
```

#### Servlet Implementation
```java
@WebServlet(
    urlPatterns = "/config",
    initParams = {
        @WebInitParam(name = "driver", value = "com.mysql.jdbc.Driver"),
        @WebInitParam(name = "url", value = "jdbc:mysql://localhost:3306/testdb")
    }
)
public class ConfigServlet extends HttpServlet {
    
    private String driver;
    private String url;
    
    @Override
    public void init() throws ServletException {
        ServletConfig config = getServletConfig();
        driver = config.getInitParameter("driver");
        url = config.getInitParameter("url");
    }
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        PrintWriter out = res.getWriter();
        out.println("Driver: " + driver);
        out.println("URL: " + url);
    }
}
```

### ServletContext
Application-wide configuration shared by all servlets.

#### web.xml Configuration
```xml
<web-app>
    <context-param>
        <param-name>appName</param-name>
        <param-value>Student Management System</param-value>
    </context-param>
    <context-param>
        <param-name>adminEmail</param-name>
        <param-value>admin@example.com</param-value>
    </context-param>
</web-app>
```

#### Using ServletContext
```java
@WebServlet("/context")
public class ContextServlet extends HttpServlet {
    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
        
        ServletContext context = getServletContext();
        
        // Reading context parameters
        String appName = context.getInitParameter("appName");
        String adminEmail = context.getInitParameter("adminEmail");
        
        // Setting and getting attributes
        context.setAttribute("userCount", 100);
        Integer userCount = (Integer) context.getAttribute("userCount");
        
        PrintWriter out = res.getWriter();
        out.println("App Name: " + appName);
        out.println("Admin Email: " + adminEmail);
        out.println("User Count: " + userCount);
    }
}
```

### ServletConfig vs ServletContext

| Feature | ServletConfig | ServletContext |
|---------|---------------|----------------|
| Scope | Single servlet | Entire application |
| Parameters | Init parameters for one servlet | Context parameters for all servlets |
| Attributes | Not supported | Application-wide attributes |
| Access | getServletConfig() | getServletContext() |

---
