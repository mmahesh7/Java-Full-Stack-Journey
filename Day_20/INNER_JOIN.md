# SQL JOINS 
## What are SQL JOINs?

### Definition
**JOIN** is a SQL operation that combines rows from two or more tables based on a related column between them. It allows you to retrieve data that is spread across multiple tables.

### Why Use JOINs?
- **Data Normalization**: Avoid data duplication by storing related data in separate tables
- **Relationship Management**: Connect related information (like books and their authors)
- **Efficient Storage**: Reduce database size and maintain data integrity

---

## INNER JOIN

### Definition
**INNER JOIN** returns only the rows where there is a match in both tables based on the specified join condition. If there's no match, rows from both tables are excluded.

### Basic Syntax
```sql
SELECT columns
FROM table1
INNER JOIN table2
ON table1.column = table2.column;
```

### Alternative Syntax (Same Result)
```sql
SELECT columns
FROM table1
JOIN table2
ON table1.column = table2.column;
```

### Simple Example
```sql
-- Get books with their author information
SELECT b.title, a.first_name, a.last_name
FROM books b
INNER JOIN authors a
ON b.author_id = a.author_id;
```

### Key Points
- ✅ Only returns matching records
- ❌ Excludes rows with NULL values in join columns
- 🔄 Order of tables doesn't matter for results (just column order changes)

---

## Basic JOIN Syntax

### Table Setup
```sql
-- Authors table
CREATE TABLE authors (
    author_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    birth_year INT
);

-- Books table  
CREATE TABLE books (
    book_id INT PRIMARY KEY,
    title VARCHAR(100),
    author_id INT,  -- Foreign key reference
    publication_year INT,
    price DECIMAL(6,2)
);
```

### Using Table Aliases
```sql
-- With aliases (recommended for readability)
SELECT b.title, a.first_name, a.last_name
FROM books AS b
JOIN authors AS a
ON b.author_id = a.author_id;

-- Short form (without AS keyword)
SELECT b.title, a.first_name, a.last_name
FROM books b
JOIN authors a
ON b.author_id = a.author_id;
```

### Adding Conditions
```sql
-- JOIN with WHERE clause (filtering after join)
SELECT b.title, a.first_name, a.last_name
FROM books b
JOIN authors a ON b.author_id = a.author_id
WHERE b.publication_year > 1940;

-- JOIN with conditions in ON clause (filtering during join)
SELECT b.title, a.first_name, a.last_name  
FROM books b
JOIN authors a ON b.author_id = a.author_id 
                AND b.publication_year > 1940
                AND a.birth_year < 1900;
```

### Difference: ON vs WHERE
- **ON clause**: Filters during the join operation (more efficient)
- **WHERE clause**: Filters after the join is complete
- For INNER JOIN, results are often the same, but performance can differ

---

## Working with Multiple Tables

### Three Table JOIN
```sql
-- Adding categories to books
SELECT b.title, a.first_name, a.last_name, c.category_name
FROM books b
JOIN authors a ON b.author_id = a.author_id
JOIN book_categories bc ON b.book_id = bc.book_id
JOIN categories c ON bc.category_id = c.category_id;
```

### Many-to-Many Relationships
```sql
-- Junction table for book-category relationship
CREATE TABLE book_categories (
    book_id INT,
    category_id INT,
    PRIMARY KEY (book_id, category_id)
);

-- Get books with all their categories concatenated
SELECT b.title, 
       a.first_name, 
       a.last_name,
       GROUP_CONCAT(c.category_name SEPARATOR ', ') AS categories
FROM books b
JOIN authors a ON b.author_id = a.author_id  
JOIN book_categories bc ON b.book_id = bc.book_id
JOIN categories c ON bc.category_id = c.category_id
GROUP BY b.book_id;
```

### GROUP_CONCAT Function
- **Purpose**: Combines multiple values into a single string
- **Syntax**: `GROUP_CONCAT(column SEPARATOR 'delimiter')`
- **Use with**: GROUP BY clause to handle one-to-many relationships

---

## Advanced JOIN Techniques

### Aggregating Data with JOINs
```sql
-- Count books per author
SELECT a.first_name, 
       a.last_name, 
       COUNT(b.book_id) AS book_count
FROM authors a
JOIN books b ON a.author_id = b.author_id
GROUP BY a.author_id, a.first_name, a.last_name;

-- Find authors with more than one book
SELECT a.first_name, 
       a.last_name, 
       COUNT(b.book_id) AS book_count
FROM authors a
JOIN books b ON a.author_id = b.author_id
GROUP BY a.author_id, a.first_name, a.last_name
HAVING COUNT(b.book_id) > 1;
```

### Using Date Functions in JOINs
```sql
-- Books published more than 70 years ago
SELECT b.title, a.last_name
FROM books b
JOIN authors a ON b.author_id = a.author_id
WHERE YEAR(CURDATE()) - b.publication_year > 70;
```

### Self JOIN Preview
```sql
-- Find books published in the same year (self join concept)
SELECT b1.title AS book1, b2.title AS book2, b1.publication_year
FROM books b1
JOIN books b2 ON b1.publication_year = b2.publication_year
                AND b1.book_id < b2.book_id;  -- Avoid duplicates
```

---

## Performance Tips

### Optimization Strategies
1. **Use Indexes**: Create indexes on join columns
```sql
CREATE INDEX idx_books_author_id ON books(author_id);
CREATE INDEX idx_authors_author_id ON authors(author_id);
```

2. **Select Specific Columns**: Avoid `SELECT *`
```sql
-- Good ✅
SELECT b.title, a.first_name FROM books b JOIN authors a...

-- Avoid ❌
SELECT * FROM books b JOIN authors a...
```

3. **Use LIMIT**: Limit results in development/testing
```sql
SELECT b.title, a.first_name 
FROM books b JOIN authors a ON b.author_id = a.author_id
LIMIT 10;
```

4. **Filter Early**: Use WHERE conditions to reduce data before joining
```sql
SELECT b.title, a.first_name
FROM books b
JOIN authors a ON b.author_id = a.author_id
WHERE b.publication_year > 2000  -- Filter reduces join complexity
```

---

## Common Use Cases

### 1. Master-Detail Relationships
```sql
-- Orders with order items
SELECT o.order_id, o.order_date, oi.product_name, oi.quantity
FROM orders o
JOIN order_items oi ON o.order_id = oi.order_id;
```

### 2. User-Role Relationships  
```sql
-- Users with their roles
SELECT u.username, r.role_name
FROM users u
JOIN user_roles ur ON u.user_id = ur.user_id
JOIN roles r ON ur.role_id = r.role_id;
```

### 3. Category-Product Relationships
```sql
-- Products with categories
SELECT p.product_name, c.category_name
FROM products p
JOIN categories c ON p.category_id = c.category_id;
```

### 4. Reporting Queries
```sql
-- Sales report by region
SELECT r.region_name, 
       SUM(s.amount) as total_sales,
       COUNT(s.sale_id) as total_transactions
FROM sales s
JOIN regions r ON s.region_id = r.region_id
GROUP BY r.region_id, r.region_name;
```

---

## Practice Examples

### Setup Tables
```sql
CREATE DATABASE practice_joins;
USE practice_joins;

-- Create and populate tables
CREATE TABLE departments (
    dept_id INT PRIMARY KEY,
    dept_name VARCHAR(50)
);

CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    name VARCHAR(50),
    dept_id INT,
    salary DECIMAL(8,2)
);

INSERT INTO departments VALUES 
(1, 'Engineering'), (2, 'Marketing'), (3, 'HR');

INSERT INTO employees VALUES 
(101, 'Alice', 1, 75000),
(102, 'Bob', 1, 80000), 
(103, 'Carol', 2, 65000),
(104, 'David', NULL, 70000);  -- No department assigned
```

### Practice Queries

#### Basic JOIN
```sql
-- Get employees with their departments
SELECT e.name, d.dept_name
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id;
-- Result: Alice, Bob, Carol (David excluded due to NULL)
```

#### Aggregate with JOIN
```sql
-- Average salary by department
SELECT d.dept_name, AVG(e.salary) as avg_salary
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
GROUP BY d.dept_id, d.dept_name;
```

#### Complex JOIN with Conditions
```sql
-- High-earning employees by department
SELECT d.dept_name, e.name, e.salary
FROM employees e
JOIN departments d ON e.dept_id = d.dept_id
WHERE e.salary > 70000
ORDER BY d.dept_name, e.salary DESC;
```

---

## Quick Reference Cheat Sheet

### Essential INNER JOIN Patterns

| Pattern | Syntax | Use Case |
|---------|--------|----------|
| **Basic JOIN** | `FROM table1 JOIN table2 ON condition` | Link two related tables |
| **Multiple JOINs** | `JOIN table2... JOIN table3...` | Connect 3+ tables |
| **JOIN + WHERE** | `JOIN... WHERE condition` | Filter after joining |
| **JOIN + GROUP BY** | `JOIN... GROUP BY column` | Aggregate joined data |
| **JOIN + HAVING** | `...GROUP BY... HAVING condition` | Filter aggregated results |

### Common Mistakes to Avoid
- ❌ Forgetting table aliases with multiple tables
- ❌ Using SELECT * in production queries
- ❌ Not handling NULL values in join columns
- ❌ Joining without proper indexes on large tables
- ❌ Confusing ON vs WHERE clause usage


---
