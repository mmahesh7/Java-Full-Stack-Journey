# Foreign Keys in SQL

---

## 📖 Definition & Core Concepts

### What is a Foreign Key?
A **Foreign Key** is a column (or group of columns) in one table that refers to the Primary Key in another table. It creates a link between two tables, establishing a parent-child relationship.

### Key Terms:
- **Parent Table**: Contains the Primary Key that is referenced
- **Child Table**: Contains the Foreign Key that references the parent table
- **Referential Integrity**: Ensures relationships between tables remain consistent

### Visual Representation:
```
Parent Table (departments)           Child Table (employees)
┌─────────────────────┐             ┌──────────────────────┐
│ department_id (PK)  │◄────────────│ department_id (FK)   │
│ department_name     │             │ employee_id (PK)     │
│ location            │             │ name                 │
└─────────────────────┘             └──────────────────────┘
```

---

## 🎯 Purpose of Foreign Keys

### 1. **Referential Integrity**
- Maintains consistent relationships between tables
- Prevents orphaned records
- Ensures data accuracy across related tables

### 2. **Data Validation**
- Validates that referenced values exist in the parent table
- Prevents insertion of invalid foreign key values
- Maintains database consistency

### 3. **Structured Relationships**
- Defines clear and logical relationships between tables
- Mirrors real-world relationships in database design
- Makes database structure more understandable

---

## 🔗 Types of Relationships

### 1. One-to-One (1:1)
**Definition**: One record in Table A relates to exactly one record in Table B

**Use Case**: Employee and Employee Details
```sql
-- Employee table (Parent)
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50)
);

-- Employee Details table (Child)
CREATE TABLE employee_details (
    employee_id INT PRIMARY KEY,
    passport_number VARCHAR(20),
    emergency_contact VARCHAR(100),
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);
```

### 2. One-to-Many (1:N)
**Definition**: One record in Table A can relate to multiple records in Table B

**Use Case**: Department and Employees
```sql
-- Departments table (Parent)
CREATE TABLE departments (
    department_id INT PRIMARY KEY,
    department_name VARCHAR(100)
);

-- Employees table (Child)
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    department_id INT,
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);
```

### 3. Many-to-Many (N:M)
**Definition**: Multiple records in Table A can relate to multiple records in Table B

**Use Case**: Students and Courses (requires Junction Table)
```sql
-- Students table
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50)
);

-- Courses table
CREATE TABLE courses (
    course_id INT PRIMARY KEY,
    course_name VARCHAR(100),
    instructor VARCHAR(100)
);

-- Junction/Bridge table
CREATE TABLE enrollments (
    enroll_id INT PRIMARY KEY,
    student_id INT,
    course_id INT,
    grade VARCHAR(5),
    FOREIGN KEY (student_id) REFERENCES students(student_id),
    FOREIGN KEY (course_id) REFERENCES courses(course_id)
);
```

---

## 💻 Syntax & Implementation

### Basic Foreign Key Syntax
```sql
-- During table creation
CREATE TABLE child_table (
    column1 datatype,
    column2 datatype,
    foreign_key_column datatype,
    PRIMARY KEY (column1),
    FOREIGN KEY (foreign_key_column) REFERENCES parent_table(primary_key_column)
);
```

### Named Constraint Syntax
```sql
CREATE TABLE child_table (
    column1 datatype,
    foreign_key_column datatype,
    PRIMARY KEY (column1),
    CONSTRAINT fk_constraint_name 
    FOREIGN KEY (foreign_key_column) REFERENCES parent_table(primary_key_column)
);
```

### Adding Foreign Key After Table Creation
```sql
ALTER TABLE table_name
ADD FOREIGN KEY (column_name) REFERENCES parent_table(parent_column);

-- With named constraint
ALTER TABLE table_name
ADD CONSTRAINT constraint_name
FOREIGN KEY (column_name) REFERENCES parent_table(parent_column);
```

### Removing Foreign Key
```sql
ALTER TABLE table_name DROP FOREIGN KEY constraint_name;
```

---

## 🛠️ Practical Examples

### Complete Implementation Example

```sql
-- Create database
CREATE DATABASE company_db;
USE company_db;

-- 1. Create Parent Table
CREATE TABLE departments (
    department_id INT NOT NULL,
    department_name VARCHAR(100) NOT NULL,
    location VARCHAR(100),
    PRIMARY KEY (department_id)
);

-- 2. Create Child Table with Foreign Key
CREATE TABLE employees (
    employee_id INT NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100),
    hire_date DATE,
    salary DECIMAL(10,2),
    department_id INT,
    PRIMARY KEY (employee_id),
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);

-- 3. Insert Data into Parent Table First
INSERT INTO departments VALUES
(1, 'Human Resources', 'Floor 1'),
(2, 'Marketing', 'Floor 2'),
(3, 'Engineering', 'Floor 3'),
(4, 'Finance', 'Floor 1');

-- 4. Insert Data into Child Table
INSERT INTO employees VALUES
(101, 'John', 'Smith', 'john@company.com', '2018-06-20', 55000.00, 1),
(102, 'Sarah', 'Johnson', 'sarah@company.com', '2019-03-15', 62000.00, 2),
(103, 'Michael', 'Williams', 'michael@company.com', '2020-01-10', 75000.00, 3);
```

---

## 🔧 Managing Foreign Keys

### Viewing Table Structure
```sql
-- Show complete table creation statement including foreign keys
SHOW CREATE TABLE table_name;

-- Describe table structure
DESCRIBE table_name;
```

### Foreign Key Constraint Violations

#### Example 1: Invalid Foreign Key Value
```sql
-- This will FAIL - department_id 99 doesn't exist
INSERT INTO employees VALUES
(999, 'Invalid', 'Employee', 'invalid@company.com', '2023-01-01', 50000.00, 99);
-- Error: Cannot add or update a child row: foreign key constraint fails
```

#### Example 2: NULL Values (Allowed)
```sql
-- This will SUCCEED - NULL is allowed for foreign keys (optional relationship)
INSERT INTO employees VALUES
(108, 'Thomas', 'Wilson', 'thomas@company.com', '2022-04-10', 65000.00, NULL);
```

#### Example 3: Cannot Delete Referenced Parent
```sql
-- This will FAIL if employees reference this department
DELETE FROM departments WHERE department_id = 1;
-- Error: Cannot delete or update a parent row: foreign key constraint fails
```

---

## 🎯 Common Scenarios

### Scenario 1: Employee Skills (One-to-Many)
```sql
CREATE TABLE employee_skills (
    skill_id INT PRIMARY KEY,
    employee_id INT NOT NULL,
    skill_name VARCHAR(50) NOT NULL,
    proficiency_level ENUM('Beginner', 'Intermediate', 'Advanced', 'Expert'),
    CONSTRAINT fk_employee_skill 
    FOREIGN KEY (employee_id) REFERENCES employees(employee_id)
);

-- Insert skills
INSERT INTO employee_skills VALUES
(1, 103, 'Python', 'Expert'),
(2, 103, 'SQL', 'Advanced'),
(3, 104, 'Java', 'Intermediate');
```

### Scenario 2: Project Management
```sql
CREATE TABLE projects (
    project_id INT PRIMARY KEY,
    project_name VARCHAR(100) NOT NULL,
    start_date DATE,
    end_date DATE,
    manager_id INT,
    FOREIGN KEY (manager_id) REFERENCES employees(employee_id)
);
```

### Scenario 3: Order Management System
```sql
-- Customers table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(100),
    email VARCHAR(100)
);

-- Orders table
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    order_date DATE,
    customer_id INT,
    total_amount DECIMAL(10,2),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Order Items table
CREATE TABLE order_items (
    item_id INT PRIMARY KEY,
    order_id INT,
    product_name VARCHAR(100),
    quantity INT,
    price DECIMAL(10,2),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);
```

---

## 📝 Best Practices

### 1. **Naming Conventions**
```sql
-- Use descriptive constraint names
CONSTRAINT fk_employee_department 
FOREIGN KEY (department_id) REFERENCES departments(department_id)
```

### 2. **Data Type Consistency**
```sql
-- Foreign key and referenced primary key must have same data type
-- Parent: department_id INT
-- Child: department_id INT (same type)
```

### 3. **Order of Operations**
```sql
-- Always create parent table first
-- Insert data into parent table before child table
-- Delete from child table before parent table
```

### 4. **NULL Considerations**
```sql
-- Use NOT NULL if relationship is mandatory
department_id INT NOT NULL,
FOREIGN KEY (department_id) REFERENCES departments(department_id)

-- Allow NULL for optional relationships
manager_id INT, -- Can be NULL
FOREIGN KEY (manager_id) REFERENCES employees(employee_id)
```

### 5. **Documentation**
```sql
-- Add comments to explain relationships
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    department_id INT, -- References departments.department_id
    FOREIGN KEY (department_id) REFERENCES departments(department_id)
);
```

---

## 🎓 Quick Reference

### Essential Commands
```sql
-- Create with foreign key
FOREIGN KEY (column_name) REFERENCES parent_table(parent_column)

-- Add foreign key
ALTER TABLE table_name ADD FOREIGN KEY (column_name) REFERENCES parent_table(parent_column);

-- Drop foreign key
ALTER TABLE table_name DROP FOREIGN KEY constraint_name;

-- View table structure
SHOW CREATE TABLE table_name;
```

### Remember:
- ✅ Parent table must exist before child table
- ✅ Referenced column must be PRIMARY KEY or UNIQUE
- ✅ Data types must match exactly
- ✅ Insert parent records before child records
- ✅ Delete child records before parent records
- ✅ NULL values are allowed in foreign key columns (unless NOT NULL specified)

---

## 💡 Key Takeaways

1. **Foreign Keys enforce relationships** between tables
2. **Referential Integrity** prevents data inconsistencies
3. **Parent-Child relationship** is fundamental to understanding foreign keys
4. **Junction tables** are used for many-to-many relationships
5. **Proper planning** of relationships is crucial for database design
6. **Order matters** - create parents before children, delete children before parents