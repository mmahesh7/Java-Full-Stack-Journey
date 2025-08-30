# SQL DELETE & TRUNCATE

## DELETE Statement

### Definition
The DELETE statement is used to remove existing records from a database table. It's a DML (Data Manipulation Language) command that allows selective removal of rows based on conditions.

### Basic Syntax
```sql
DELETE FROM table_name WHERE condition;
```

### Key Points
- **Selective Deletion**: Can delete specific rows using WHERE clause
- **Row-by-Row**: Processes each row individually
- **Logged Operation**: Each row deletion is logged
- **Rollback Possible**: Can be rolled back in transactions
- **Triggers**: Activates DELETE triggers

### Use Cases

#### 1. Delete Specific Record by ID
```sql
-- Delete a product with specific ID
DELETE FROM products WHERE product_id = 10;
```

#### 2. Delete Records Based on Condition
```sql
-- Delete all products with price less than 50
DELETE FROM products WHERE price < 50.00;

-- Delete products with multiple conditions
DELETE FROM products WHERE price > 300 AND stock_quantity < 5;
```

#### 3. Delete All Records (Keep Table Structure)
```sql
-- Delete all records from table
DELETE FROM products;
```

### Important Safety Tips
⚠️ **Always use SELECT first to verify what you're deleting:**
```sql
-- Step 1: Check what will be deleted
SELECT * FROM products WHERE price > 300;

-- Step 2: If results look correct, then delete
DELETE FROM products WHERE price > 300;
```

### Sample Code Example
```sql
-- Create sample table
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(100),
    price DECIMAL(10, 2),
    stock_quantity INT
);

-- Insert sample data
INSERT INTO products VALUES
(1, 'Laptop', 999.99, 10),
(2, 'Smartphone', 499.99, 25),
(3, 'Headphones', 89.99, 50);

-- Delete specific product
DELETE FROM products WHERE product_id = 1;

-- Verify deletion
SELECT * FROM products;
```

---

## TRUNCATE Statement

### Definition
TRUNCATE is a DDL (Data Definition Language) command that removes ALL rows from a table by dropping and recreating the table structure. It's faster than DELETE for removing all records.

### Basic Syntax
```sql
TRUNCATE TABLE table_name;
-- OR
TRUNCATE table_name;
```

### Key Points
- **All Records Only**: Cannot use WHERE clause - removes ALL rows
- **Fast Operation**: Drops and recreates table (not row-by-row)
- **Minimal Logging**: Only logs table deallocation
- **Auto-Increment Reset**: Resets auto-increment counters
- **No Triggers**: Does not fire DELETE triggers
- **Limited Rollback**: Generally cannot be rolled back

### Use Cases

#### 1. Clear All Data from Table
```sql
-- Remove all employees but keep table structure
TRUNCATE TABLE employees;
```

#### 2. Reset Table for Fresh Data
```sql
-- Clear products table before importing new data
TRUNCATE products;
```

### Sample Code Example
```sql
-- Create sample table
CREATE TABLE employees (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    salary DECIMAL(10,2)
);

-- Insert data
INSERT INTO employees (name, email, salary) VALUES
('John Doe', 'john@example.com', 65000),
('Jane Smith', 'jane@example.com', 72000);

-- Clear all data
TRUNCATE TABLE employees;

-- Table is now empty but structure remains
SELECT * FROM employees;
```

---

## DELETE vs TRUNCATE Comparison

| Feature | DELETE | TRUNCATE |
|---------|--------|----------|
| **Speed** | Slower (row-by-row) | Faster (drop & recreate) |
| **WHERE Clause** | ✅ Supported | ❌ Not supported |
| **Logging** | Individual row logging | Minimal logging |
| **Auto-Increment** | Preserves counter | Resets to initial value |
| **Triggers** | ✅ Fires DELETE triggers | ❌ No triggers fired |
| **Rollback** | ✅ Can be rolled back | ❌ Limited rollback |
| **Command Type** | DML | DDL |
| **Memory Usage** | Higher (logs each row) | Lower (minimal logging) |

### When to Use Which?

**Use DELETE when:**
- You need to remove specific rows (with conditions)
- You want to preserve auto-increment values
- You need trigger execution
- You're working within a transaction that might need rollback

**Use TRUNCATE when:**
- You want to remove ALL rows quickly
- You want to reset auto-increment counters
- Performance is critical
- You don't need triggers to fire

---

## Foreign Key Constraints

### Problem: Cannot Delete Referenced Records
```sql
-- This will fail if product_id is referenced in orders table
DELETE FROM products WHERE product_id = 2;
-- ERROR: Cannot delete referenced row
```

### Solution 1: CASCADE DELETE
Automatically delete related records in child tables.

```sql
-- Drop existing constraint
ALTER TABLE orders DROP FOREIGN KEY orders_ibfk_1;

-- Add CASCADE constraint
ALTER TABLE orders 
ADD CONSTRAINT orders_ibfk_1 
FOREIGN KEY (product_id) REFERENCES products(product_id) 
ON DELETE CASCADE;

-- Now deleting product will also delete related orders
DELETE FROM products WHERE product_id = 2;
```

### Solution 2: SET NULL
Set foreign key to NULL when parent record is deleted.

```sql
-- Drop existing constraint
ALTER TABLE orders DROP FOREIGN KEY orders_ibfk_1;

-- Add SET NULL constraint
ALTER TABLE orders 
ADD CONSTRAINT orders_ibfk_1 
FOREIGN KEY (product_id) REFERENCES products(product_id) 
ON DELETE SET NULL;

-- Now deleting product will set product_id to NULL in orders
DELETE FROM products WHERE product_id = 2;
```

### Sample Code with Foreign Keys
```sql
-- Create parent table
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(100),
    price DECIMAL(10, 2)
);

-- Create child table with foreign key
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    product_id INT,
    quantity INT,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
);

-- Insert data
INSERT INTO products VALUES (1, 'Laptop', 999.99);
INSERT INTO orders VALUES (1, 1, 2);

-- Delete product (will also delete related order due to CASCADE)
DELETE FROM products WHERE product_id = 1;
```

---

## Auto-Increment Behavior

### DELETE Behavior
Auto-increment counter continues from where it left off.

```sql
CREATE TABLE auto_example (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50)
);

-- Insert records (IDs will be 1, 2, 3)
INSERT INTO auto_example (name) VALUES ('Item 1'), ('Item 2'), ('Item 3');

-- Delete all records
DELETE FROM auto_example;

-- Insert new record (ID will be 4, not 1!)
INSERT INTO auto_example (name) VALUES ('New Item');
SELECT * FROM auto_example; -- Shows ID = 4
```

### TRUNCATE Behavior
Auto-increment counter resets to initial value.

```sql
-- Using same table from above
TRUNCATE TABLE auto_example;

-- Insert new record (ID will be 1)
INSERT INTO auto_example (name) VALUES ('New Item');
SELECT * FROM auto_example; -- Shows ID = 1
```

---

## Best Practices


### ⚡ Performance Tips
1. **Use TRUNCATE for clearing entire tables**
2. **Delete in batches for large datasets**
3. **Consider indexes on WHERE clause columns**

```sql
-- Batch deletion for large tables
DELETE FROM large_table WHERE condition LIMIT 1000;
-- Repeat until all target rows are deleted
```

### 🔍 Monitoring and Verification
```sql
-- Check row count before and after
SELECT COUNT(*) FROM products; -- Before deletion
DELETE FROM products WHERE price > 1000;
SELECT COUNT(*) FROM products; -- After deletion
```

### 📋 Common Patterns
```sql
-- Delete duplicates (keep one record)
DELETE p1 FROM products p1
INNER JOIN products p2 
WHERE p1.product_id > p2.product_id 
AND p1.product_name = p2.product_name;

-- Delete old records
DELETE FROM logs WHERE created_date < DATE_SUB(NOW(), INTERVAL 30 DAY);

-- Conditional truncate alternative
DELETE FROM temp_table; -- Instead of TRUNCATE when you need logging
```

---

## Quick Reference Commands

```sql
-- Basic DELETE operations
DELETE FROM table_name WHERE condition;
DELETE FROM table_name; -- All rows

-- TRUNCATE operations  
TRUNCATE TABLE table_name;
TRUNCATE table_name;

-- Foreign key management
ALTER TABLE child_table DROP FOREIGN KEY constraint_name;
ALTER TABLE child_table ADD CONSTRAINT constraint_name 
FOREIGN KEY (column) REFERENCES parent_table(column) ON DELETE CASCADE;

-- Safety checks
SELECT * FROM table_name WHERE condition; -- Preview
SELECT COUNT(*) FROM table_name; -- Count rows
```

## Summary
- **DELETE**: Selective, logged, slower, triggers fire, preserves auto-increment
- **TRUNCATE**: All rows, fast, minimal logging, resets auto-increment, no triggers
- **Foreign Keys**: Use CASCADE or SET NULL for handling relationships
- **Safety**: Always preview with SELECT before DELETE operations