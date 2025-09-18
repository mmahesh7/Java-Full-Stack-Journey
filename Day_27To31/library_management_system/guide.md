# 📚 Library Management System (JDBC)

A complete **console-based Library Management System** built in **Java** using **JDBC**.
This project demonstrates **enterprise-style layered architecture**, core database operations, and advanced JDBC concepts like **transactions, batch processing, and complex joins**.

---

## 🚀 Features

### ✅ Core Functionalities

* **Author Management** – Add, update, view, delete authors
* **Book Management** – Track availability, assign authors, manage inventory
* **Member Management** – Differentiate between *Basic* and *Premium* members
* **Loan System** – Issue, return, and track book loans

### ✅ Business Rules Implemented

* **Inventory Management** – Dynamic book availability updates
* **Loan Limits** – Rules vary for Basic vs Premium members
* **Fine Calculation** – Automatic overdue fee computation
* **Data Integrity** – Prevents orphaned/invalid records

---

## 🏗️ Project Architecture

The system is built with **3 distinct layers** following enterprise best practices:

* **DAO Layer (Data Access Layer)**

    * `AuthorDAO`, `BookDAO`, `MemberDAO`, `BookLoanDAO`
    * Handles direct database operations (CRUD, queries)

* **Service Layer (Business Logic Layer)**

    * `LibraryService`
    * Implements core logic, manages transactions, applies rules

* **Presentation Layer (UI Layer)**

    * Console-based user interface
    * Provides interaction for admin and members

---

## 🛠️ JDBC Concepts Used

* **Connection Management** – Proper handling and cleanup
* **PreparedStatements** – SQL injection prevention, parameterized queries
* **ResultSet Processing** – Data mapping into objects
* **Transaction Management** – Multi-table operations with rollback
* **Batch Processing** – Bulk updates (e.g., overdue loan processing)
* **Auto-generated Keys** – Retrieve DB-generated IDs
* **Complex JOIN Queries** – Multi-table relationships and retrieval

---

## 📂 Database Design

* **Author ↔ Book** → One-to-Many relationship
* **Member ↔ Loan** → One-to-Many relationship
* **Foreign Key Constraints** → Ensures referential integrity

---

## ⚡ Tech Stack

* **Java** (Core + JDBC)
* **MySQL / PostgreSQL** (any relational DB works)
* **JDBC Driver**
* **Maven** (optional, for dependency management)

---

## ▶️ How to Run

1. **Clone the Repository**

   ```bash
   git clone https://github.com/mmahesh7/Java-Full-Stack-Journey
   cd library-management-system
   ```

2. **Set Up Database**

    * Import the SQL schema (provided in `/db` folder)
    * Update DB credentials in `DBConnection.java`

3. **Compile and Run**

   ```bash
   javac -d bin src/**/*.java
   java -cp bin Main
   ```

---

## 📸 Sample Console Output

```
========= LIBRARY MANAGEMENT =========
1. Add New Book
2. Issue Book
3. Return Book
4. View Members
5. Exit
======================================
Enter choice:
```

---

## 🏆 Learning Outcomes

* Designed **layered architecture** (DAO → Service → UI)
* Implemented **production-style error handling** with rollbacks
* Applied **business logic** with real-world rules
* Learned **database performance optimizations** (batch, joins, indexes)

---

## 📌 Future Enhancements

* Add **Servlets/JSP** for a web-based interface
* Integrate with **Spring Boot + JPA**
* Add **REST APIs** for modern applications
* Enhance reporting & analytics features

---

## 👨‍💻 Author

**Mahesh M**

* 🚀 Full Stack Java Developer (in progress)
* 📖 Currently on a **Java Full Stack Learning Journey**
* 🔗 [LinkedIn]() | [GitHub](https://github.com/mmahesh7)

---
