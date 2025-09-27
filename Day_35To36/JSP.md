
## Introduction to JSP

### Why JSP?
Java Server Pages (JSP) addresses limitations of servlets for presentation logic.

#### Problems with Servlets for UI
```java
// Servlet code mixing business logic with HTML
protected void doGet(HttpServletRequest req, HttpServletResponse res) 
        throws ServletException, IOException {
    
    PrintWriter out = res.getWriter();
    out.println("<!DOCTYPE html>");
    out.println("<html>");
    out.println("<head><title>User List</title></head>");
    out.println("<body>");
    out.println("<h1>User Management</h1>");
    out.println("<table border='1'>");
    
    // Imagine 100+ lines of HTML mixed with Java logic
    for(User user : userList) {
        out.println("<tr>");
        out.println("<td>" + user.getName() + "</td>");
        out.println("<td>" + user.getEmail() + "</td>");
        out.println("</tr>");
    }
    
    out.println("</table>");
    out.println("</body>");
    out.println("</html>");
}
```

#### JSP Solution
```jsp
<!-- users.jsp -->
<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
</head>
<body>
    <h1>User Management</h1>
    <table border="1">
        <% for(User user : userList) { %>
            <tr>
                <td><%= user.getName() %></td>
                <td><%= user.getEmail() %></td>
            </tr>
        <% } %>
    </table>
</body>
</html>
```

### JSP Advantages
- **Separation of Concerns**: HTML designers and Java developers can work independently
- **Easier Maintenance**: HTML and Java code are clearly separated
- **Reusability**: JSP pages can be reused across different servlets
- **Less Code**: No need for multiple println statements

---

## JSP to Servlet Conversion

### How JSP Works Internally
JSP pages are translated into servlets by the container.

#### Original JSP File
```jsp
<!-- hello.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Hello JSP</title>
</head>
<body>
    <h1>Welcome to JSP!</h1>
    <% String name = "John"; %>
    <p>Hello <%= name %></p>
    <p>Current time: <%= new java.util.Date() %></p>
</body>
</html>
```

#### Generated Servlet (Simplified)
```java
public class hello_jsp extends HttpJspBase {
    
    public void _jspService(HttpServletRequest request, 
                           HttpServletResponse response)
            throws java.io.IOException, ServletException {
        
        JspWriter out = response.getWriter();
        
        out.write("<!DOCTYPE html>\n");
        out.write("<html>\n");
        out.write("<head>\n");
        out.write("    <title>Hello JSP</title>\n");
        out.write("</head>\n");
        out.write("<body>\n");
        out.write("    <h1>Welcome to JSP!</h1>\n");
        
        String name = "John";
        
        out.write("    <p>Hello ");
        out.print(name);
        out.write("</p>\n");
        out.write("    <p>Current time: ");
        out.print(new java.util.Date());
        out.write("</p>\n");
        out.write("</body>\n");
        out.write("</html>\n");
    }
}
```

### JSP Translation Process
1. **First Request**: JSP is translated to servlet and compiled
2. **Subsequent Requests**: Compiled servlet is used directly
3. **File Modified**: JSP is re-translated and recompiled

### Finding Generated Servlets
In Tomcat, generated servlets are typically found at:
```
$TOMCAT_HOME/work/Catalina/localhost/[app-name]/org/apache/jsp/
```

---

## JSP Tags and Syntax

### Types of JSP Tags

#### 1. Scriptlet Tags `<% %>`
Contains Java code executed during request processing.

```jsp
<%
    String username = request.getParameter("username");
    int count = 0;
    for(int i = 1; i <= 5; i++) {
        count += i;
    }
    Date currentDate = new Date();
%>
```

#### 2. Expression Tags `<%= %>`
Evaluates Java expressions and outputs the result.

```jsp
<p>Username: <%= username %></p>
<p>Count: <%= count %></p>
<p>Current Date: <%= currentDate %></p>
<p>Random Number: <%= Math.random() %></p>
```

#### 3. Declaration Tags `<%! %>`
Declares variables and methods at class level.

```jsp
<%!
    // Instance variables (shared across all requests)
    private int visitCount = 0;
    
    // Method declaration
    public String formatDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(date);
    }
    
    // Static variables
    private static final String COMPANY_NAME = "TechCorp";
%>

<p>Visit Count: <%= ++visitCount %></p>
<p>Formatted Date: <%= formatDate(new Date()) %></p>
<p>Company: <%= COMPANY_NAME %></p>
```

#### 4. Directive Tags `<%@ %>`
Provides metadata about the JSP page.

```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" 
         pageEncoding="UTF-8" import="java.util.*,java.text.*" %>
<%@ include file="header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

### Complete Example
```jsp
<%@ page language="java" contentType="text/html; charset=UTF-8" 
         import="java.util.Date" %>

<%!
    private String getGreeting(int hour) {
        if(hour < 12) return "Good Morning";
        else if(hour < 18) return "Good Afternoon";
        else return "Good Evening";
    }
%>

<!DOCTYPE html>
<html>
<head>
    <title>JSP Tags Demo</title>
</head>
<body>
    <%
        Date now = new Date();
        int hour = now.getHours();
        String name = request.getParameter("name");
        if(name == null) name = "Guest";
    %>
    
    <h1><%= getGreeting(hour) %>, <%= name %>!</h1>
    <p>Current time: <%= now %></p>
    
    <h2>Number Table</h2>
    <table border="1">
        <% for(int i = 1; i <= 5; i++) { %>
            <tr>
                <td><%= i %></td>
                <td><%= i * i %></td>
                <td><%= i * i * i %></td>
            </tr>
        <% } %>
    </table>
</body>
</html>
```

---

## JSP Directives

### 1. Page Directive
Controls page-wide settings.

```jsp
<%@ page 
    language="java"
    contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.*,java.sql.*,com.example.model.*"
    session="true"
    buffer="8kb"
    autoFlush="true"
    isThreadSafe="true"
    info="User Management Page"
    errorPage="error.jsp"
    isErrorPage="false"
%>
```

#### Key Attributes
- **language**: Scripting language (always "java")
- **contentType**: MIME type and character encoding
- **import**: Java packages to import
- **session**: Whether to create HTTP session
- **buffer**: Output buffer size
- **autoFlush**: Auto-flush buffer when full
- **errorPage**: Page to redirect on errors
- **isErrorPage**: Whether this page handles errors

### 2. Include Directive
Includes content from another file at translation time.

```jsp
<!-- header.jsp -->
<div id="header">
    <h1>My Website</h1>
    <nav>
        <a href="home.jsp">Home</a>
        <a href="about.jsp">About</a>
        <a href="contact.jsp">Contact</a>
    </nav>
</div>

<!-- main.jsp -->
<%@ page language="java" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Main Page</title>
</head>
<body>
    <%@ include file="header.jsp" %>
    
    <div id="content">
        <h2>Welcome to Main Page</h2>
        <p>This is the main content area.</p>
    </div>
    
    <%@ include file="footer.jsp" %>
</body>
</html>
```

### 3. Taglib Directive
Declares custom tag libraries.

```jsp
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="sql" uri="http://java.sun.com/jsp/jstl/sql" %>
```


---

## JSP Implicit Objects

JSP provides built-in objects that can be used directly without declaration.

### 1. request (HttpServletRequest)
Represents the client request.

```jsp
<%
    String method = request.getMethod();
    String uri = request.getRequestURI();
    String userAgent = request.getHeader("User-Agent");
    String name = request.getParameter("name");
    
    // Setting attributes
    request.setAttribute("timestamp", new Date());
%>

<p>Request Method: <%= method %></p>
<p>Request URI: <%= uri %></p>
<p>User Agent: <%= userAgent %></p>
<p>Name Parameter: <%= name %></p>
```

### 2. response (HttpServletResponse)
Represents the response to client.

```jsp
<%
    response.setContentType("text/html");
    response.setHeader("Cache-Control", "no-cache");
    
    // Redirecting (should be before any output)
    // response.sendRedirect("login.jsp");
    
    // Adding cookies
    Cookie cookie = new Cookie("lastVisit", new Date().toString());
    response.addCookie(cookie);
%>
```


### 3. out Object (JspWriter)
Used to write content to output stream.

```jsp
<%
    out.println("<h1>Dynamic Content</h1>");
    out.print("Current time: " + new Date());
    
    // Buffer management
    if (out.getBufferSize() > 0) {
        out.flush();
    }
%>
```

### 4. session Object
Manages user session data.

```jsp
<%
    // Storing data in session
    session.setAttribute("username", "john");
    session.setAttribute("loginTime", new Date());
    
    // Retrieving data
    String username = (String) session.getAttribute("username");
    Date loginTime = (Date) session.getAttribute("loginTime");
    
    // Session info
    String sessionId = session.getId();
    boolean isNew = session.isNew();
%>

<p>Welcome <%= username %>!</p>
<p>Session ID: <%= sessionId %></p>
<p>Login Time: <%= loginTime %></p>
```

### 5. application Object (ServletContext)
Application-wide data sharing.

```jsp
<%
    // Setting application-wide data
    application.setAttribute("visitCount", 100);
    
    // Getting init parameters
    String appName = application.getInitParameter("appName");
    
    // Application info
    String serverInfo = application.getServerInfo();
    String contextPath = application.getContextPath();
%>

<p>Application: <%= appName %></p>
<p>Server: <%= serverInfo %></p>
```

### 6. config Object (ServletConfig)
Servlet-specific configuration.

```jsp
<%
    // Getting init parameters
    String driver = config.getInitParameter("driver");
    String url = config.getInitParameter("url");
    
    // Servlet info
    String servletName = config.getServletName();
%>

<p>Driver: <%= driver %></p>
<p>URL: <%= url %></p>
```

### 7. pageContext Object (PageContext)
Page-specific context and scope management.

```jsp
<%
    // Setting attributes in different scopes
    pageContext.setAttribute("name", "John");  // Page scope
    pageContext.setAttribute("age", 25, PageContext.SESSION_SCOPE);
    pageContext.setAttribute("role", "admin", PageContext.APPLICATION_SCOPE);
    
    // Getting attributes
    String name = (String) pageContext.getAttribute("name");
    Integer age = (Integer) pageContext.getAttribute("age", PageContext.SESSION_SCOPE);
    
    // Finding attributes in all scopes
    String role = (String) pageContext.findAttribute("role");
%>
```

### JSP Scopes
Different scopes for storing and retrieving data:

| Scope | Object | Duration | Access |
|-------|---------|----------|---------|
| Page | pageContext | Current page only | Single JSP page |
| Request | request | Current request | Forward/include chain |
| Session | session | User session | All pages in session |
| Application | application | Application lifetime | All users, all pages |

### Scope Usage Example
```jsp
<%
    // Page scope - available only in current page
    pageContext.setAttribute("pageData", "Page specific data");
    
    // Request scope - available in current request chain
    request.setAttribute("requestData", "Request specific data");
    
    // Session scope - available throughout user session
    session.setAttribute("sessionData", "Session specific data");
    
    // Application scope - available to all users
    application.setAttribute("appData", "Application wide data");
%>

<!-- Accessing scoped data -->
<p>Page Data: <%= pageContext.getAttribute("pageData") %></p>
<p>Request Data: <%= request.getAttribute("requestData") %></p>
<p>Session Data: <%= session.getAttribute("sessionData") %></p>
<p>App Data: <%= application.getAttribute("appData") %></p>
```

### Key Benefits of Implicit Objects
- **No Declaration Needed**: Objects are automatically available
- **Container Managed**: Created and managed by servlet container
- **Standard Interface**: Same objects available across all JSP pages
- **Scope Management**: Easy data sharing across different scopes

---

## Quick Reference Summary

### JSP Tag Types
- **`<% %>`** - Scriptlet (Java code in service method)
- **`<%! %>`** - Declaration (class-level variables/methods)
- **`<%= %>`** - Expression (output expressions)
- **`<%@ %>`** - Directive (page metadata)

### Common Directives
```jsp
<%@ page import="java.util.*" %>
<%@ include file="header.jsp" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
```

### Implicit Objects
- **request, response, out, session**
- **application, config, pageContext**
- All automatically available without declaration

---
