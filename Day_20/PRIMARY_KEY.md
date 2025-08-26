# SQL Primary Keys
---

## What is a Primary Key?

**Definition**: A primary key is a column or set of columns that uniquely identifies each record (row) in a database table.

### Key Benefits:
- **Unique Identification**: Every row can be uniquely identified
- **Data Integrity**: Prevents duplicate records
- **Performance**: Faster data retrieval and indexing
- **Relationships**: Reference point for connecting multiple tables

---

## Primary Key Characteristics

A primary key must have these three essential characteristics:

### 1. **UNIQUE** 🔑
- No two rows can have the same primary key value
- Each value must be distinct across the entire table

### 2. **NOT NULL** ❌
- Primary key columns cannot contain NULL values
- Every row must have a valid primary key value

### 3. **STABLE** 🔒
- Primary key values should not change frequently
- Provides consistent reference for relationships

---

## Creating Primary Keys

### Method 1: Inline Definition
```sql
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100)
);
```

### Method 2: Separate Definition
```sql
CREATE TABLE orders (
    order_id INT,
    customer_id INT,
    order_date DATE NOT NULL,
    total_amount DECIMAL(10, 2),
    PRIMARY KEY (order_id)
);
```

### Example with Sample Data:
```sql
-- Insert valid records
INSERT INTO students (student_id, first_name, last_name, email)
VALUES 
    (1, 'John', 'Smith', 'john@example.com'),
    (2, 'Maria', 'Garcia', 'maria@example.com'),
    (3, 'Ahmed', 'Khan', 'ahmed@example.com');

-- This will FAIL - duplicate primary key
INSERT INTO students (student_id, first_name, last_name, email)
VALUES (1, 'Jane', 'Doe', 'jane@example.com');
-- Error: Duplicate entry '1' for key 'PRIMARY'
```

---

## Auto-Increment Primary Keys

### Definition:
Auto-increment automatically generates sequential numeric values for primary keys, eliminating the need to manually specify values.

### Syntax:
```sql
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT
);
```

### Usage Example:
```sql
-- No need to specify product_id - it's auto-generated
INSERT INTO products (product_name, price, description)
VALUES 
    ('Laptop', 1299.99, 'High-performance laptop'),
    ('Smartphone', 799.99, 'Latest model smartphone'),
    ('Headphones', 199.99, 'Noise-cancelling headphones');

-- View auto-generated IDs
SELECT * FROM products;
-- Result:
-- product_id | product_name | price   | description
-- 1          | Laptop       | 1299.99 | High-performance laptop
-- 2          | Smartphone   | 799.99  | Latest model smartphone
-- 3          | Headphones   | 199.99  | Noise-cancelling headphones
```

### Benefits of Auto-Increment:
- **No Manual Management**: Database handles ID generation
- **Guaranteed Uniqueness**: No duplicate key errors
- **Sequential Order**: IDs are generated in order
- **Performance**: Optimized for insertions

---

## Adding Primary Keys to Existing Tables

### Scenario: Table Created Without Primary Key
```sql
-- Create table without primary key
CREATE TABLE suppliers (
    supplier_id INT,
    supplier_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100)
);
```

### Adding Primary Key Later:
```sql
-- Add primary key to existing table
ALTER TABLE suppliers
ADD PRIMARY KEY (supplier_id);
```

### Complete Example:
```sql
-- Insert some data first
INSERT INTO suppliers (supplier_id, supplier_name, contact_person)
VALUES 
    (1, 'Tech Supply Co', 'John Manager'),
    (2, 'Office Depot', 'Sarah Director');

-- Now add the primary key
ALTER TABLE suppliers
ADD PRIMARY KEY (supplier_id);
```

---

## Composite Primary Keys

### Definition:
A composite primary key uses **multiple columns combined** to create a unique identifier for each row.

### When to Use:
- When no single column can uniquely identify a record
- Many-to-many relationship tables
- Junction/bridge tables

### Syntax:
```sql
CREATE TABLE enrollments (
    student_id INT,
    course_id INT,
    enrollment_date DATE NOT NULL,
    grade VARCHAR(2),
    PRIMARY KEY (student_id, course_id)  -- Composite key
);
```

### Example Usage:
```sql
-- Valid insertions - each combination is unique
INSERT INTO enrollments (student_id, course_id, enrollment_date, grade)
VALUES 
    (1, 101, '2023-01-15', 'A'),    -- Student 1, Course 101
    (1, 102, '2023-01-15', 'B+'),   -- Student 1, Course 102 ✅
    (2, 101, '2023-01-16', 'A-'),   -- Student 2, Course 101 ✅
    (3, 103, '2023-01-17', 'B');    -- Student 3, Course 103 ✅

-- This will FAIL - duplicate combination
INSERT INTO enrollments (student_id, course_id, enrollment_date, grade)
VALUES (1, 101, '2023-02-01', 'C');  -- Student 1, Course 101 already exists
-- Error: Duplicate entry '1-101' for key 'PRIMARY'
```

### Composite Key Rules:
- The **combination** of all columns must be unique
- Individual columns can have duplicate values
- All columns in the composite key are NOT NULL

---

## How Primary Keys Work Internally

### Database Storage Structure:

#### Clustered Index
When you create a primary key, MySQL automatically creates a **clustered index**:

```
📖 Analogy: Dictionary vs Book Index
┌─────────────────────────────────────┐
│ CLUSTERED INDEX (Primary Key)       │
│ Like a Dictionary:                  │
│ - Words stored in alphabetical order│
│ - Data physically organized by key  │
│ - Only ONE per table                │
└─────────────────────────────────────┘

┌─────────────────────────────────────┐
│ NON-CLUSTERED INDEX (Regular Index) │
│ Like Book Index:                    │
│ - Separate section with page numbers│
│ - Points to actual data location    │
│ - Multiple allowed per table        │
└─────────────────────────────────────┘
```

#### B+ Tree Structure
Primary keys are stored using **B+ Tree** data structure:

```
Example Employee Table:
employee_id: 5, 12, 24, 37, 43, 65, 78, 85

B+ Tree Structure:
         [37, 78]           ← Root Node (separators)
        /    |    \
   [5,12,24] [37,43,65] [78,85]  ← Leaf Nodes (actual data)
```

#### Search Process:
```sql
-- Searching for employee_id = 43
-- 1. Start at root: [37, 78]
-- 2. 43 > 37 and 43 < 78, so go to middle branch
-- 3. Find in leaf node: [37, 43, 65]
-- 4. Return complete employee record
```

---

## Best Practices

### ✅ DO's

1. **Always Use Primary Keys**
   ```sql
   -- Every table should have a primary key
   CREATE TABLE users (
       user_id INT AUTO_INCREMENT PRIMARY KEY,
       username VARCHAR(50) NOT NULL
   );
   ```

2. **Use Auto-Increment for Numeric IDs**
   ```sql
   -- Preferred approach
   CREATE TABLE orders (
       order_id BIGINT AUTO_INCREMENT PRIMARY KEY,  -- Use BIGINT for large datasets
       customer_name VARCHAR(100)
   );
   ```

3. **Keep Primary Keys Simple**
   ```sql
   -- Good: Simple integer
   user_id INT PRIMARY KEY
   
   -- Avoid: Complex composite unless necessary
   PRIMARY KEY (first_name, last_name, birth_date)
   ```

4. **Choose Appropriate Data Types**
   ```sql
   -- For small datasets (< 2 million records)
   id INT AUTO_INCREMENT PRIMARY KEY
   
   -- For large datasets (> 2 million records)
   id BIGINT AUTO_INCREMENT PRIMARY KEY
   ```

### ❌ DON'Ts

1. **Don't Use Frequently Changing Values**
   ```sql
   -- BAD: Email might change
   CREATE TABLE users (
       email VARCHAR(100) PRIMARY KEY,  -- Avoid this
       name VARCHAR(50)
   );
   
   -- GOOD: Stable numeric ID
   CREATE TABLE users (
       user_id INT AUTO_INCREMENT PRIMARY KEY,
       email VARCHAR(100),
       name VARCHAR(50)
   );
   ```

2. **Don't Skip Primary Keys**
   ```sql
   -- BAD: No primary key
   CREATE TABLE logs (
       message TEXT,
       created_at TIMESTAMP
   );
   
   -- GOOD: Always include primary key
   CREATE TABLE logs (
       log_id BIGINT AUTO_INCREMENT PRIMARY KEY,
       message TEXT,
       created_at TIMESTAMP
   );
   ```

---

## Common Errors

### 1. Duplicate Entry Error
```sql
-- Error Code: 1062
-- Duplicate entry '1' for key 'PRIMARY'

-- Cause: Trying to insert existing primary key value
INSERT INTO students (student_id, name) VALUES (1, 'John');
INSERT INTO students (student_id, name) VALUES (1, 'Jane');  -- This fails
```

### 2. NULL Value Error
```sql
-- Error: Primary key cannot be NULL
INSERT INTO students (student_id, name) VALUES (NULL, 'John');  -- Fails
```

### 3. Composite Key Violation
```sql
-- Error: Duplicate entry '1-101' for key 'PRIMARY'
INSERT INTO enrollments (student_id, course_id) VALUES (1, 101);
INSERT INTO enrollments (student_id, course_id) VALUES (1, 101);  -- Fails
```

---

## Quick Reference Commands

### Creating Primary Keys:
```sql
-- Method 1: Inline
CREATE TABLE table_name (
    id INT PRIMARY KEY,
    column_name DATA_TYPE
);

-- Method 2: Separate definition
CREATE TABLE table_name (
    id INT,
    column_name DATA_TYPE,
    PRIMARY KEY (id)
);

-- Method 3: Auto-increment
CREATE TABLE table_name (
    id INT AUTO_INCREMENT PRIMARY KEY,
    column_name DATA_TYPE
);

-- Method 4: Composite key
CREATE TABLE table_name (
    col1 INT,
    col2 INT,
    data_column DATA_TYPE,
    PRIMARY KEY (col1, col2)
);
```

### Adding to Existing Table:
```sql
ALTER TABLE table_name
ADD PRIMARY KEY (column_name);
```

### Viewing Primary Key Information:
```sql
-- Show table structure
DESCRIBE table_name;

-- Show indexes (including primary key)
SHOW INDEX FROM table_name;
```

---

## Summary Checklist

When working with primary keys, remember:

- Every table needs a primary key
- Use AUTO_INCREMENT for numeric IDs
- Keep primary keys simple (INT or BIGINT)
- Primary key values must be unique and not null
- Don't change primary key values frequently  
- Use composite keys only when necessary
- Primary keys automatically create clustered indexes
- Primary keys improve query performance significantly
