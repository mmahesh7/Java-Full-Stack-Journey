# Day 15 - Introduction to Databases and SQL Command Categories

## What is a Database?
A database is an organized collection of structured information, or data, typically stored electronically in a computer system. Databases allow for data to be easily accessed, managed, modified, updated, and deleted. They serve as the backbone of various applications—everything from social media platforms to e-commerce websites and financial systems rely on databases to store user profiles, products, transactions, and more.

### Key Characteristics of Databases
- **Persistent Storage**: Data is stored over the long term, surviving application restarts and system reboots.
- **Structured and Organized**: Data is systematically arranged to avoid duplication and inconsistency.
- **Easily Retrievable**: Efficient methods exist for querying, filtering, and retrieving stored data quickly.
- **Concurrent Access**: Multiple users and applications can use the database simultaneously without corrupting data.
- **Security and Integrity**: Access can be controlled and data can be protected against unauthorized use or corruption.

### Why Use a Database?
- To maintain a permanent record of information.
- To ensure data integrity and reduce redundancy.
- To efficiently handle large volumes of data and ensure fast retrieval.
- To allow multiple users and applications to access and work with the data safely and concurrently.
- To back up and recover data in case of hardware failures or data corruption.

---

## Database Management Systems (DBMS)
It is software that manages databases, handling data storage, retrieval, updates, and security. It acts as an interface between databases and users/applications.

### Examples of DBMS:
- **Relational DBMS (RDBMS)**: MySQL, PostgreSQL, Oracle, SQL Server  
- **NoSQL DBMS**: MongoDB, Cassandra, DynamoDB  
- **In-memory DBMS**: Redis, Memcached  

👉 Here, we will focus on **MySQL**, which is a popular open-source RDBMS.

---

## Introduction to the Relational Data Model
The relational data model organizes data into one or more tables (also known as relations) with rows and columns. The idea, introduced by **E.F. Codd in 1970**, revolutionized how databases are structured and queried.

### Key Concepts in the Relational Model
- **Tables (Relations)**: Represents an entity or concept (e.g., Employees, Customers, Products).  
- **Columns (Attributes)**: Define the type of data stored (e.g., employee_id, first_name, hire_date).  
- **Rows (Records)**: Each row represents a single instance/record (e.g., one employee).  
- **Keys**: Primary Key & Foreign Key  
- **Relationships**: One-to-One (1:1), One-to-Many (1:N), Many-to-Many (M:N)  

### Primary Key
A column or set of columns that uniquely identify each row in a table.  
👉 Example: `employee_id` uniquely identifies every employee.

### Foreign Key
A column in one table that refers to the primary key in another table.  
👉 Example: `department_id` in Employees table referencing Departments table.

### Relationships
- **One-to-One (1:1)**: One employee → one unique company car.  
- **One-to-Many (1:N)**: One department → many employees.  
- **Many-to-Many (M:N)**: Employees ↔ Projects (many employees work on many projects).  

### Why the Relational Model?
- **Data Integrity**: Enforces referential integrity (no orphaned records).  
- **Reduced Redundancy**: Normalization minimizes duplication.  
- **Flexibility in Querying**: SQL enables powerful querying and manipulation.  

---

# SQL Command Categories

SQL commands are grouped into categories based on their functionality.

## 1. DDL (Data Definition Language)
Commands that define and manage database **structure**.  
Examples:  
- `CREATE` → Create objects (database, table, view).  
- `ALTER` → Modify an existing object.  
- `DROP` → Delete database objects.  
- `TRUNCATE` → Remove all rows from a table but keep structure.  

---

## 2. DML (Data Manipulation Language)
Commands to manage **data inside tables**.  
Examples:  
- `INSERT` → Add new records.  
- `UPDATE` → Modify existing records.  
- `DELETE` → Remove specific records.  
- `SELECT` → Retrieve data from tables.  

---

## 3. DCL (Data Control Language)
Commands to control **permissions and access rights**.  
Examples:  
- `GRANT` → Give access privileges.  
- `REVOKE` → Remove access privileges.  

---

## 4. TCL (Transaction Control Language)
Commands to manage **transactions** (a set of SQL operations).  
Examples:  
- `COMMIT` → Save changes permanently.  
- `ROLLBACK` → Undo changes since last commit.  
- `SAVEPOINT` → Mark a point within a transaction to rollback partially.  

---

