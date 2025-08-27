# MySQL UNION & UNION ALL

## 📚 Overview
UNION and UNION ALL are SQL operators used to combine result sets from multiple SELECT queries into a single result set. Think of them as "stacking" query results vertically, one on top of another.

---

## 🎯 Key Definitions

### UNION
- **Definition**: Combines rows from multiple SELECT queries and **removes duplicate rows**
- **Purpose**: Get a combined result set without duplicates
- **Performance**: Slower (due to duplicate removal processing)

### UNION ALL
- **Definition**: Combines rows from multiple SELECT queries and **keeps all rows including duplicates**
- **Purpose**: Get a combined result set with all rows
- **Performance**: Faster (no duplicate removal processing)

---

## 🔧 Basic Syntax

```sql
-- UNION (removes duplicates)
SELECT column1, column2, ... FROM table1
UNION
SELECT column1, column2, ... FROM table2;

-- UNION ALL (keeps duplicates)
SELECT column1, column2, ... FROM table1
UNION ALL
SELECT column1, column2, ... FROM table2;
```

---

## ⚡ Essential Rules

### 1. **Same Number of Columns** ✅
```sql
-- ✅ CORRECT - Both queries have 3 columns
SELECT first_name, last_name, email FROM employees
UNION
SELECT first_name, last_name, email FROM customers;

-- ❌ ERROR - Different column counts
SELECT first_name, last_name FROM employees
UNION
SELECT first_name, last_name, email FROM customers;
```

### 2. **Compatible Data Types** ✅
```sql
-- ✅ CORRECT - Compatible types (VARCHAR with VARCHAR)
SELECT name, age FROM table1
UNION
SELECT name, age FROM table2;

-- ✅ CORRECT - Auto-conversion (INT to DECIMAL)
SELECT name, salary_int FROM table1
UNION
SELECT name, salary_decimal FROM table2;

-- ❌ ERROR - Incompatible types (DATE with BOOLEAN)
SELECT name, hire_date FROM table1
UNION
SELECT name, is_active FROM table2;
```

### 3. **Column Names from First Query** 📝
```sql
-- Result will have column names: first_name, last_name
SELECT first_name, last_name FROM headquarters_employees
UNION
SELECT last_name, first_name FROM branch_employees; -- Column names ignored
```

---

## 💡 Simple Use Cases

### Use Case 1: Combining Employee Lists
```sql
-- Get all employees from different locations
SELECT first_name, last_name, 'Headquarters' AS location
FROM headquarters_employees
UNION ALL
SELECT first_name, last_name, 'Branch' AS location  
FROM branch_employees;
```

### Use Case 2: Creating Contact Lists
```sql
-- Combine employees and customers into one contact list
SELECT name, email, 'Employee' AS type FROM employees
UNION
SELECT name, email, 'Customer' AS type FROM customers;
```

### Use Case 3: Merging Similar Data
```sql
-- Combine current and archived orders
SELECT order_id, customer_id, order_date FROM current_orders
UNION ALL
SELECT order_id, customer_id, order_date FROM archived_orders;
```

---

## 🔍 Detailed Examples

### Example 1: Basic UNION vs UNION ALL
```sql
-- Sample Tables
-- employees: John Smith, Mary Johnson, John Smith (duplicate)
-- contractors: David Wilson, John Smith, Lisa Brown

-- UNION (removes duplicates)
SELECT name FROM employees
UNION
SELECT name FROM contractors;
-- Result: John Smith (once), Mary Johnson, David Wilson, Lisa Brown

-- UNION ALL (keeps duplicates)  
SELECT name FROM employees
UNION ALL
SELECT name FROM contractors;
-- Result: John Smith (twice), Mary Johnson, John Smith, David Wilson, Lisa Brown
```

### Example 2: Adding Identifier Columns
```sql
-- Add source identification to combined results
SELECT 
    employee_id AS id,
    first_name, 
    last_name, 
    'HQ Employee' AS source
FROM headquarters_employees
UNION ALL
SELECT 
    customer_id AS id,
    first_name, 
    last_name, 
    'Customer' AS source
FROM customers
ORDER BY last_name;
```

### Example 3: Handling Different Table Structures
```sql
-- Tables with different columns - use NULL for missing columns
SELECT 
    employee_id, 
    first_name, 
    last_name, 
    department, 
    salary, 
    NULL AS status  -- customers don't have salary
FROM employees
UNION ALL
SELECT 
    customer_id, 
    first_name, 
    last_name, 
    NULL AS department,  -- customers don't have department
    NULL AS salary,      -- customers don't have salary
    status
FROM customers;
```

---

## 🎯 Advanced Techniques

### 1. Filtering Before UNION
```sql
-- Apply WHERE clause to each query separately
SELECT name, salary FROM employees WHERE salary > 50000
UNION
SELECT name, salary FROM contractors WHERE salary > 50000
ORDER BY salary DESC;
```

### 2. Using Subqueries with UNION
```sql
-- Wrap UNION in subquery for further processing
SELECT department FROM (
    SELECT DISTINCT department FROM headquarters_employees
    UNION ALL
    SELECT DISTINCT department FROM branch_employees
) AS all_departments
GROUP BY department
HAVING COUNT(*) = 2;  -- Departments in both locations
```

### 3. Complex Data Transformation
```sql
-- Transform and combine data from different sources
SELECT 
    CONCAT(first_name, ' ', last_name) AS full_name,
    email,
    hire_date AS important_date,
    'Employee' AS person_type
FROM employees
UNION ALL
SELECT 
    CONCAT(first_name, ' ', last_name) AS full_name,
    email,
    signup_date AS important_date,
    'Customer' AS person_type
FROM customers
ORDER BY important_date DESC;
```

---

## ⚠️ Common Mistakes & Solutions

### Mistake 1: Column Count Mismatch
```sql
-- ❌ WRONG
SELECT first_name, last_name FROM table1
UNION
SELECT first_name FROM table2;  -- Error: Different column counts

-- ✅ CORRECT
SELECT first_name, last_name FROM table1
UNION
SELECT first_name, NULL AS last_name FROM table2;
```

### Mistake 2: Incompatible Data Types
```sql
-- ❌ WRONG
SELECT name, age FROM table1        -- age is INT
UNION
SELECT name, status FROM table2;    -- status is VARCHAR

-- ✅ CORRECT
SELECT name, CAST(age AS VARCHAR) FROM table1
UNION
SELECT name, status FROM table2;
```

### Mistake 3: Expecting Wrong Column Names
```sql
-- Result will have columns: name, info (from first query)
SELECT name, age AS info FROM table1
UNION
SELECT name, salary AS monthly_pay FROM table2;  -- monthly_pay ignored
```

---

## 🚀 Performance Tips

### 1. Use UNION ALL When Possible
```sql
-- Faster - no duplicate checking
SELECT * FROM table1
UNION ALL
SELECT * FROM table2;
```

### 2. Filter Early
```sql
-- Apply filters before UNION for better performance
SELECT name FROM large_table WHERE active = 1
UNION ALL
SELECT name FROM another_table WHERE status = 'approved';
```

### 3. Use Indexes
```sql
-- Ensure filtered columns are indexed
CREATE INDEX idx_active ON large_table(active);
CREATE INDEX idx_status ON another_table(status);
```

---

## 📝 Quick Reference

| Aspect | UNION | UNION ALL |
|--------|-------|-----------|
| **Duplicates** | Removes duplicates | Keeps all rows |
| **Performance** | Slower | Faster |
| **Use When** | Need unique results | Need all data |
| **Processing** | Extra duplicate removal | Direct combination |

### Essential Checklist ✅
- [ ] Same number of columns in all SELECT statements
- [ ] Compatible data types across corresponding columns
- [ ] Column names taken from first SELECT statement
- [ ] Use UNION ALL for better performance when duplicates don't matter
- [ ] Apply ORDER BY to entire result set (not individual queries)
- [ ] Use NULL for missing columns when combining different table structures

---

## 🔄 Practice Scenarios

1. **Scenario**: Combine active and inactive users
   - Use UNION ALL if you need count totals
   - Use UNION if you want unique users only

2. **Scenario**: Merge data from multiple years
   - UNION ALL for complete historical data
   - Add year identifier column for clarity

3. **Scenario**: Create comprehensive contact list
   - Combine employees, customers, suppliers
   - Add source type column
   - Handle different column structures with NULL

---
