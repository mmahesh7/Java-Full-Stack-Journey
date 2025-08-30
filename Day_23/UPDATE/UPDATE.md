# MySQL UPDATE Operations
## Basic UPDATE Syntax

### Definition
The UPDATE statement is used to modify existing records in a MySQL table.

### Syntax
```sql
UPDATE table_name 
SET column_name = new_value 
WHERE condition;
```

### Key Components
- **UPDATE**: SQL keyword to modify existing data
- **table_name**: Name of the table to update
- **SET**: Specifies which columns to update and their new values
- **WHERE**: Condition to specify which rows to update (CRUCIAL for safety)

### Basic Example
```sql
-- Update a single product's price
UPDATE products 
SET price = 999.99 
WHERE product_id = 1;
```

---

## Safe Update Mode

### Definition
MySQL Workbench has a safety feature that prevents UPDATE statements without WHERE clauses to avoid accidentally modifying all rows.

### The Problem
```sql
-- This will cause an error in Safe Update Mode
UPDATE products 
SET price = price * 0.9;
```

### Error Message
```
Error Code: 1175. You are using safe update mode and you tried to update a table without a WHERE that uses a KEY column.
```

### Solution
Always include a WHERE clause, even if you want to update all rows:
```sql
-- Safe way to update all rows
UPDATE products 
SET price = price * 0.9 
WHERE product_id > 0;
```

### Disabling Safe Mode (Not Recommended)
```sql
SET SQL_SAFE_UPDATES = 0;  -- Disable safe mode
-- Your UPDATE statement here
SET SQL_SAFE_UPDATES = 1;  -- Re-enable safe mode
```

---

## Single Column Updates

### Definition
Updating one column at a time in selected rows.

### Syntax
```sql
UPDATE table_name 
SET column_name = new_value 
WHERE condition;
```

### Examples
```sql
-- Update laptop price
UPDATE products 
SET price = 899.99 
WHERE product_id = 1;

-- Update all electronics category prices
UPDATE products 
SET price = price + 50 
WHERE category = 'Electronics';

-- Update stock for out-of-stock items
UPDATE products 
SET stock_quantity = 10 
WHERE stock_quantity = 0;
```

---

## Multiple Column Updates

### Definition
Updating multiple columns in a single UPDATE statement using comma-separated column assignments.

### Syntax
```sql
UPDATE table_name 
SET column1 = value1, 
    column2 = value2, 
    column3 = value3 
WHERE condition;
```

### Examples
```sql
-- Update both price and stock quantity
UPDATE products 
SET price = 89.99, 
    stock_quantity = 20 
WHERE product_id = 3;

-- Update multiple product details
UPDATE products 
SET price = 199.99, 
    stock_quantity = 5, 
    category = 'Electronics' 
WHERE product_id = 1;
```

---

## Mathematical Operations in Updates

### Definition
Performing calculations during updates using arithmetic operators.

### Common Operations
- **Addition**: `column = column + value`
- **Subtraction**: `column = column - value`
- **Multiplication**: `column = column * value`
- **Division**: `column = column / value`
- **Percentage calculations**: `column = column * 0.9` (10% discount)

### Examples
```sql
-- Apply 10% discount (multiply by 0.9)
UPDATE products 
SET price = price * 0.9 
WHERE product_id > 0;

-- Apply 90% discount (multiply by 0.1)
UPDATE products 
SET price = price * 0.1 
WHERE category = 'Furniture';

-- Increase stock by 25 units
UPDATE products 
SET stock_quantity = stock_quantity + 25 
WHERE stock_quantity < 10;

-- Round prices to nearest dollar
UPDATE products 
SET price = ROUND(price, 0) 
WHERE product_id > 0;
```

### Data Type Considerations
```sql
-- When multiplying DECIMAL values, precision may increase
-- Original: DECIMAL(10,2) = 99.99
-- After 0.9 multiplication: 89.991
-- MySQL will round to fit: 89.99 (with warning about data truncation)
```

---

## Automatic Timestamp Updates

### Definition
Setting up columns to automatically update their timestamp values when rows are modified.

### Table Creation with Auto-Update
```sql
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(50),
    price DECIMAL(10,2),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
```

### Modifying Existing Tables
```sql
-- Add auto-update to existing timestamp column
ALTER TABLE products 
MODIFY last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;
```

### Understanding the Options
- **DEFAULT CURRENT_TIMESTAMP**: Sets current time when row is inserted
- **ON UPDATE CURRENT_TIMESTAMP**: Updates to current time when row is modified
- Both can be used together for complete tracking

### Example
```sql
-- This update will automatically update the last_updated column
UPDATE products 
SET price = 199, 
    stock_quantity = 1 
WHERE product_id = 1;

-- Check the automatic timestamp update
SELECT product_id, price, stock_quantity, last_updated 
FROM products 
WHERE product_id = 1;
```

---

## Using LIMIT with UPDATE

### Definition
Restricting the number of rows that can be updated in a single UPDATE operation.

### Syntax
```sql
UPDATE table_name 
SET column = value 
WHERE condition 
LIMIT number;
```

### Use Cases
- **Testing updates**: Update only a few rows first
- **Batch processing**: Update records in chunks
- **Performance**: Avoid updating too many rows at once

### Examples
```sql
-- Update only the first 2 products
UPDATE products 
SET price = price * 0.1 
WHERE product_id > 0 
LIMIT 2;

-- Update top 5 most expensive products
UPDATE products 
SET price = price * 0.8 
WHERE price > 100 
ORDER BY price DESC 
LIMIT 5;
```

### Best Practice with ORDER BY
```sql
-- Always use ORDER BY with LIMIT to ensure predictable results
UPDATE products 
SET stock_quantity = stock_quantity + 10 
WHERE category = 'Electronics' 
ORDER BY product_id 
LIMIT 3;
```

---

## Common Constraints and Errors

### Primary Key Constraint Violation

#### The Problem
```sql
-- This will fail - trying to create duplicate primary key
UPDATE products 
SET product_id = 1 
WHERE product_id = 2;
```

#### Error Message
```
Error Code: 1062. Duplicate entry '1' for key 'PRIMARY'
```

#### Why It Happens
- Primary keys must be unique
- Cannot update a primary key to a value that already exists
- Generally, primary keys should not be updated

### Other Common Constraints
```sql
-- Foreign key constraint violation
UPDATE orders 
SET product_id = 999 
WHERE order_id = 1;  -- If product_id 999 doesn't exist

-- Check constraint violation (MySQL 8.0+)
UPDATE products 
SET price = -50 
WHERE product_id = 1;  -- If CHECK (price > 0) exists

-- Data type constraint
UPDATE products 
SET product_name = NULL 
WHERE product_id = 1;  -- If column is NOT NULL
```

---

## Best Practices

### 1. Always Use WHERE Clauses
```sql
-- Good
UPDATE products SET price = 99.99 WHERE product_id = 1;

-- Dangerous
UPDATE products SET price = 99.99;  -- Updates ALL rows!
```

### 2. Test with SELECT First
```sql
-- Test your condition with SELECT
SELECT * FROM products WHERE category = 'Electronics';

-- Then apply the UPDATE
UPDATE products SET price = price * 0.9 WHERE category = 'Electronics';
```

### 3. Use Appropriate Data Types
```sql
-- For prices, use DECIMAL not FLOAT
price DECIMAL(10,2)  -- Good for currency
price FLOAT          -- Can cause rounding issues
```

---

## Practice Examples

### Setup Table
```sql
CREATE DATABASE practice_db;
USE practice_db;

CREATE TABLE employees (
    emp_id INT PRIMARY KEY,
    name VARCHAR(50),
    department VARCHAR(30),
    salary DECIMAL(10,2),
    bonus DECIMAL(10,2) DEFAULT 0,
    last_updated TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO employees VALUES
(1, 'Alice', 'Engineering', 75000, 5000, NOW()),
(2, 'Bob', 'Marketing', 65000, 3000, NOW()),
(3, 'Charlie', 'Engineering', 80000, 4000, NOW()),
(4, 'Diana', 'Sales', 60000, 2000, NOW()),
(5, 'Eve', 'Marketing', 70000, 3500, NOW());
```

### Exercise 1: Basic Updates
```sql
-- 1. Give Alice a raise to $85,000
UPDATE employees SET salary = 85000 WHERE emp_id = 1;

-- 2. Update all Marketing employees' bonuses to $4,000
UPDATE employees SET bonus = 4000 WHERE department = 'Marketing';

-- 3. Give all Engineering employees a 10% salary increase
UPDATE employees SET salary = salary * 1.10 WHERE department = 'Engineering';
```

### Exercise 2: Complex Updates
```sql
-- 1. Update salary and bonus for employee with highest current salary
UPDATE employees 
SET salary = salary + 5000, bonus = bonus + 1000 
WHERE salary = (SELECT MAX(salary) FROM (SELECT * FROM employees) AS temp);

-- 2. Standardize department names
UPDATE employees SET department = 'Engineering Dept' WHERE department = 'Engineering';
UPDATE employees SET department = 'Marketing Dept' WHERE department = 'Marketing';
UPDATE employees SET department = 'Sales Dept' WHERE department = 'Sales';
```

### Exercise 3: Conditional Updates
```sql
-- Give bonus increases based on salary levels
UPDATE employees 
SET bonus = CASE 
    WHEN salary >= 80000 THEN bonus * 1.20
    WHEN salary >= 70000 THEN bonus * 1.15
    ELSE bonus * 1.10
END
WHERE emp_id > 0;
```

---

## Quick Reference

### Common UPDATE Patterns
```sql
-- Single column
UPDATE table SET column = value WHERE condition;

-- Multiple columns
UPDATE table SET col1 = val1, col2 = val2 WHERE condition;

-- Mathematical operation
UPDATE table SET price = price * 0.9 WHERE condition;

-- With LIMIT
UPDATE table SET column = value WHERE condition LIMIT 5;

-- Using subquery
UPDATE table1 SET column = (SELECT column FROM table2 WHERE condition) WHERE condition;
```
