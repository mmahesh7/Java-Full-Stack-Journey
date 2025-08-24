# MySQL SELECT Statement


---
## Introduction

The `SELECT` statement is the most fundamental SQL command used to **retrieve data** from MySQL databases. It allows you to:
- Fetch specific columns or all columns from tables
- Filter data based on conditions
- Sort and organize results
- Perform calculations and use functions
- Combine data from multiple queries

---

## Basic SELECT Syntax

### Definition
The basic SELECT statement retrieves data from one or more tables in a database.

### Syntax
```sql
SELECT column1, column2, ... 
FROM table_name;
```

### Examples

**Select All Columns:**
```sql
SELECT * FROM employees;
```
- `*` means "all columns"
- Returns every column and row from the employees table

**Select Single Column:**
```sql
SELECT first_name FROM employees;
```

**Select Multiple Columns:**
```sql
SELECT first_name, email FROM employees;
```

---

## Selecting Specific Columns

### Use Case
When you don't need all data from a table, selecting specific columns:
- Improves query performance
- Reduces network traffic
- Makes results more readable

### Syntax
```sql
SELECT column1, column2, column3 FROM table_name;
```

### Example
```sql
SELECT first_name, last_name, department FROM employees;
```

---

## Column Aliases

### Definition
Aliases provide alternative names for columns in the result set, improving readability without changing the original table structure.

### Syntax
```sql
SELECT column_name AS 'alias_name' FROM table_name;
-- OR
SELECT column_name AS alias_name FROM table_name;
```

### Use Cases
- Remove underscores from column names
- Make column names more descriptive
- Provide meaningful names for calculated fields

### Examples
```sql
-- Basic alias
SELECT first_name AS 'First Name', last_name AS 'Last Name' 
FROM employees;

-- Alias for calculated fields
SELECT salary * 1.1 AS 'Salary After Raise' 
FROM employees;
```

---

## WHERE Clause - Filtering Data

### Definition
The WHERE clause filters records based on specified conditions, returning only rows that meet the criteria.

### Syntax
```sql
SELECT columns FROM table_name 
WHERE condition;
```

### Common Operators
- `=` : Equal to
- `>` : Greater than
- `<` : Less than
- `>=` : Greater than or equal
- `<=` : Less than or equal
- `!=` or `<>` : Not equal

### Examples
```sql
-- Filter by department
SELECT * FROM employees 
WHERE department = "IT";

-- Filter by salary range
SELECT * FROM employees 
WHERE salary > 70000;

-- Multiple conditions can be added with AND, OR
SELECT * FROM employees 
WHERE department = "IT" AND salary > 70000;
```

---

## ORDER BY - Sorting Results

### Definition
ORDER BY sorts the result set based on one or more columns in ascending or descending order.

### Syntax
```sql
SELECT columns FROM table_name 
ORDER BY column_name [ASC|DESC];
```

### Sort Options
- `ASC` : Ascending order (default)
- `DESC` : Descending order

### Examples
```sql
-- Sort by salary (ascending - default)
SELECT * FROM employees 
ORDER BY salary;

-- Sort by salary (descending)
SELECT * FROM employees 
ORDER BY salary DESC;

-- Sort by multiple columns
SELECT * FROM employees 
ORDER BY department, salary DESC;
```

### Common Use Case
**Find highest paid employee in IT department:**
```sql
SELECT * FROM employees 
WHERE department = "IT" 
ORDER BY salary DESC 
LIMIT 1;
```

---

## LIMIT - Restricting Results

### Definition
LIMIT restricts the number of rows returned by a query.

### Syntax
```sql
SELECT columns FROM table_name 
LIMIT number_of_rows;
```

### Use Cases
- Get top N results
- Implement pagination
- Test queries with large datasets
- Performance optimization

### Examples
```sql
-- Get first 2 employees
SELECT * FROM employees 
LIMIT 2;

-- Get top 3 highest paid employees
SELECT * FROM employees 
ORDER BY salary DESC 
LIMIT 3;
```

**Important Note:** Without ORDER BY, the order of results is not guaranteed, even though it might appear to follow insertion order.

---

## DISTINCT - Removing Duplicates

### Definition
DISTINCT returns only unique values, eliminating duplicate rows from the result set.

### Syntax
```sql
SELECT DISTINCT column_name FROM table_name;
```

### Use Cases
- Find unique values in a column
- Get list of all departments
- Remove duplicate entries

### Examples
```sql
-- Get all unique departments
SELECT DISTINCT department FROM employees;

-- This would show: HR, IT, Finance, Marketing (no duplicates)
```

**Without DISTINCT:**
```sql
SELECT department FROM employees;
-- Might show: HR, IT, Finance, IT, Marketing (with duplicates)
```

---

## Mathematical Expressions

### Definition
You can perform mathematical calculations directly in SELECT statements.

### Supported Operations
- `+` : Addition
- `-` : Subtraction
- `*` : Multiplication
- `/` : Division

### Use Cases
- Calculate salary increases
- Compute totals
- Convert units
- Generate derived values

### Examples
```sql
-- Calculate 10% salary raise
SELECT first_name, last_name, 
       salary * 1.1 AS 'Salary After Raise' 
FROM employees;

-- Calculate annual salary from monthly
SELECT first_name, 
       monthly_salary * 12 AS 'Annual Salary' 
FROM employees;
```

---

## Built-in Functions

### String Functions

#### CONCAT()
**Purpose:** Combines two or more strings
```sql
SELECT CONCAT(first_name, ' ', last_name) AS 'Full Name' 
FROM employees;
```

#### LENGTH()
**Purpose:** Returns the length of a string
```sql
SELECT LENGTH('hello');  -- Returns: 5
```

### Date Functions

#### YEAR()
**Purpose:** Extracts year from a date
```sql
SELECT first_name, YEAR(hire_date) AS 'Hire Year' 
FROM employees;
```

#### NOW()
**Purpose:** Returns current date and time
```sql
SELECT NOW() AS 'Current Time';
```

### Numeric Functions

#### ROUND()
**Purpose:** Rounds a number to specified decimal places
```sql
-- Round to 1 decimal place
SELECT ROUND(salary, 1) FROM employees;

-- Round to whole number
SELECT ROUND(salary) FROM employees;
```

#### AVG()
**Purpose:** Calculates average value
```sql
SELECT AVG(salary) FROM employees;
```

#### COUNT()
**Purpose:** Counts number of rows
```sql
SELECT COUNT(*) FROM employees;
```

---

## Subqueries

### Definition
A subquery is a query nested inside another query, used to provide data for the main query.

### Syntax
```sql
SELECT columns FROM table_name 
WHERE column operator (SELECT column FROM table_name WHERE condition);
```

### Use Cases
- Compare values against calculated results
- Filter based on aggregate functions
- Create dynamic conditions

### Example
**Find employees with salary above average:**
```sql
-- Method 1: Manual calculation
SELECT AVG(salary) FROM employees;  -- Returns average
SELECT * FROM employees WHERE salary > 68800;  -- Use the result

-- Method 2: Using subquery (dynamic)
SELECT * FROM employees 
WHERE salary > (SELECT AVG(salary) FROM employees);
```

### Benefits of Subqueries
- Dynamic conditions
- Single query execution
- Always uses current data

---

## UNION - Combining Results

### Definition
UNION combines results from two or more SELECT statements into a single result set.

### Syntax
```sql
SELECT columns FROM table1 WHERE condition1
UNION
SELECT columns FROM table2 WHERE condition2;
```

### Rules
- Same number of columns in all SELECT statements
- Columns must have compatible data types
- Column names come from the first SELECT
- Automatically removes duplicates (use UNION ALL to keep duplicates)

### Example
```sql
-- Get employees from IT and HR departments
SELECT first_name, last_name FROM employees WHERE department = 'IT'
UNION
SELECT first_name, last_name FROM employees WHERE department = 'HR';
```

---

## GROUP BY - Data Aggregation

### Definition
GROUP BY groups rows with same values and is typically used with aggregate functions.

### Syntax
```sql
SELECT column, aggregate_function(column) 
FROM table_name 
GROUP BY column;
```

### Important Rules
- When using GROUP BY, SELECT can only contain:
  - Columns that are in GROUP BY clause
  - Aggregate functions (COUNT, SUM, AVG, MAX, MIN)

### Common Aggregate Functions
- `COUNT(*)` : Count number of rows
- `COUNT(column)` : Count non-null values
- `SUM(column)` : Sum of values
- `AVG(column)` : Average value
- `MAX(column)` : Maximum value
- `MIN(column)` : Minimum value

### Examples
```sql
-- Count employees by department
SELECT COUNT(*), department 
FROM employees 
GROUP BY department;

-- Average salary by department
SELECT department, AVG(salary) AS 'Average Salary' 
FROM employees 
GROUP BY department;
```

### What GROUP BY Does
1. Groups rows with same department values
2. Applies aggregate function to each group
3. Returns one row per group

---

## SELECT Without Tables

### Definition
SELECT can be used as a calculator or to evaluate expressions without referencing any table.

### Use Cases
- Mathematical calculations
- Function testing
- Getting current date/time
- String operations
- Debugging expressions

### Examples
```sql
-- Basic calculation
SELECT 5 * 2;  -- Returns: 10

-- Current timestamp
SELECT NOW() AS 'Current Time';

-- String functions
SELECT LENGTH('hello');  -- Returns: 5

-- Concatenation
SELECT CONCAT('Hello', ' ', 'World');  -- Returns: Hello World

-- Comparison (returns 1 for true, 0 for false)
SELECT 5 > 3;  -- Returns: 1
SELECT 5 < 3;  -- Returns: 0
```

---

## Practice Examples

### Sample Database Setup
```sql
CREATE DATABASE company;
USE company;

CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    department VARCHAR(50),
    salary DECIMAL(10,2),
    hire_date DATE
);

INSERT INTO employees (first_name, last_name, department, salary, hire_date) VALUES
('John', 'Doe', 'HR', 60000.00, '2022-05-10'),
('Jane', 'Smith', 'IT', 75000.00, '2021-08-15'),
('Alice', 'Johnson', 'Finance', 82000.00, '2019-03-20'),
('Bob', 'Williams', 'IT', 72000.00, '2020-11-25'),
('Charlie', 'Brown', 'Marketing', 65000.00, '2023-01-05');
```

### Common Query Patterns

**1. Basic Information Retrieval:**
```sql
-- Get all employee information
SELECT * FROM employees;

-- Get specific columns
SELECT first_name, email FROM employees;
```

**2. Filtering and Sorting:**
```sql
-- IT department employees, sorted by salary
SELECT * FROM employees 
WHERE department = 'IT' 
ORDER BY salary DESC;

-- Top 2 earners
SELECT * FROM employees 
ORDER BY salary DESC 
LIMIT 2;
```

**3. Data Analysis:**
```sql
-- Employee count by department
SELECT department, COUNT(*) AS 'Employee Count' 
FROM employees 
GROUP BY department;

-- Employees hired after 2020
SELECT first_name, last_name, hire_date 
FROM employees 
WHERE YEAR(hire_date) > 2020;
```

**4. Advanced Queries:**
```sql
-- Full name with hire year and rounded salary
SELECT CONCAT(first_name, ' ', last_name) AS 'Full Name',
       YEAR(hire_date) AS 'Hire Year',
       ROUND(salary, 0) AS 'Salary' 
FROM employees 
WHERE salary > 70000;

-- Above average earners
SELECT * FROM employees 
WHERE salary > (SELECT AVG(salary) FROM employees);
```

---

## Key Terms and Concepts

**Clause:** A component of an SQL statement (SELECT, FROM, WHERE, ORDER BY are all clauses)

**Result Set:** The data returned by a SELECT query

**Aggregate Functions:** Functions that perform calculations on multiple rows (COUNT, SUM, AVG, etc.)

**Expression:** A combination of columns, operators, functions, and values that evaluates to a single value

**Alias:** An alternative name given to a column or table in the query result

---

