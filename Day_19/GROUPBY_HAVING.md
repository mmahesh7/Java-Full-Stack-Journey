# SQL GROUP BY and HAVING Clauses
---

##  What is GROUP BY?

**Definition:** GROUP BY is used to group rows that have the same values in specified columns. It's commonly used with aggregate functions to perform calculations on each group.

**Purpose:** 
- Summarize large datasets
- Calculate statistics for different categories
- Group similar records into buckets for analysis

---

##  Basic Syntax

```sql
SELECT column_name(s), aggregate_function(column)
FROM table_name
WHERE condition
GROUP BY column_name(s)
HAVING group_condition
ORDER BY column_name(s);
```

**Key Points:**
- You can only SELECT columns that are in GROUP BY or aggregate functions
- Cannot use SELECT * with GROUP BY

---

##  Key Rules & Restrictions

###  What We CAN Do:
```sql
-- Select grouped columns
SELECT department FROM employees GROUP BY department;

-- Use aggregate functions
SELECT department, COUNT(*) FROM employees GROUP BY department;

-- Group by multiple columns
SELECT department, YEAR(joining_date) FROM employees 
GROUP BY department, YEAR(joining_date);
```

###  What We CANNOT Do:
```sql
-- This will cause an error
SELECT * FROM employees GROUP BY department;

-- This will also cause an error (name is not in GROUP BY)
SELECT department, name FROM employees GROUP BY department;
```

---

##  Common Use Cases

### 1. **Count Records in Each Group**
```sql
-- Count employees per department
SELECT department, COUNT(*) AS employee_count
FROM employees 
GROUP BY department;
```

### 2. **Calculate Averages**
```sql
-- Average salary per department
SELECT department, AVG(salary) AS avg_salary
FROM employees 
GROUP BY department;
```

### 3. **Find Min/Max Values**
```sql
-- Highest and lowest salary per department
SELECT department, 
       MIN(salary) AS min_salary,
       MAX(salary) AS max_salary
FROM employees 
GROUP BY department;
```

### 4. **Sum Calculations**
```sql
-- Total salary expense per department
SELECT department, SUM(salary) AS total_expense
FROM employees 
GROUP BY department;
```

---

##  Working with Aggregate Functions

| Function | Purpose | Example |
|----------|---------|---------|
| `COUNT()` | Count rows | `COUNT(*)` or `COUNT(column)` |
| `SUM()` | Sum values | `SUM(salary)` |
| `AVG()` | Average values | `AVG(salary)` |
| `MIN()` | Minimum value | `MIN(salary)` |
| `MAX()` | Maximum value | `MAX(salary)` |

### Example with Multiple Aggregates:
```sql
SELECT department,
       COUNT(*) AS employee_count,
       AVG(salary) AS avg_salary,
       MIN(salary) AS min_salary,
       MAX(salary) AS max_salary,
       SUM(salary) AS total_expense
FROM employees 
GROUP BY department;
```

---

##  Multiple Column Grouping

When you group by multiple columns, it creates sub-groups within groups.

```sql
-- Group by department, then by joining year
SELECT department, 
       YEAR(joining_date) AS joining_year,
       COUNT(*) AS employee_count
FROM employees 
GROUP BY department, YEAR(joining_date)
ORDER BY department, joining_year;
```

**Result Example:**
```
department | joining_year | employee_count
-----------|--------------|---------------
Finance    | 2019         | 1
Finance    | 2020         | 1
Finance    | 2021         | 1
HR         | 2019         | 2
HR         | 2020         | 1
IT         | 2017         | 1
IT         | 2018         | 1
IT         | 2021         | 2
```

---

##  HAVING Clause

**Purpose:** Filter groups after they've been created (similar to WHERE but for groups)

### Basic HAVING Example:
```sql
-- Find departments with more than 2 employees
SELECT department, COUNT(*) AS employee_count
FROM employees 
GROUP BY department
HAVING COUNT(*) > 2;
```

### Complex HAVING Example:
```sql
-- Departments with more than 2 employees AND average salary > 60000
SELECT department, 
       COUNT(*) AS employee_count,
       AVG(salary) AS avg_salary
FROM employees 
GROUP BY department
HAVING COUNT(*) > 2 AND AVG(salary) > 60000;
```

---

##  WHERE vs HAVING

| Aspect | WHERE | HAVING |
|--------|--------|---------|
| **When Applied** | Before grouping | After grouping |
| **Works With** | Individual rows | Groups |
| **Can Use Aggregates** | No | Yes |
| **Position in Query** | Before GROUP BY | After GROUP BY |

### Example Showing the Difference:
```sql
-- WHERE: Filter rows before grouping
SELECT department, COUNT(*) AS employee_count
FROM employees 
WHERE salary > 60000  -- Filter individuals first
GROUP BY department;

-- HAVING: Filter groups after grouping  
SELECT department, COUNT(*) AS employee_count
FROM employees 
GROUP BY department
HAVING COUNT(*) > 2;  -- Filter departments with >2 employees

-- Combined: Both WHERE and HAVING
SELECT department, 
       COUNT(*) AS employee_count,
       AVG(salary) AS avg_salary
FROM employees 
WHERE joining_date > '2018-01-01'  -- First: filter individuals
GROUP BY department                 -- Then: group them
HAVING COUNT(*) > 1                -- Finally: filter groups
   AND AVG(salary) > 55000;
```

---

## Complete Query Order

**Correct Order of SQL Clauses:**
```sql
SELECT column_list
FROM table_name
WHERE individual_row_conditions
GROUP BY grouping_columns
HAVING group_conditions  
ORDER BY sorting_columns
LIMIT number_of_rows;
```

**Execution Order:**
1. **FROM** - Get the table
2. **WHERE** - Filter individual rows
3. **GROUP BY** - Group the remaining rows
4. **HAVING** - Filter the groups
5. **SELECT** - Choose what to display
6. **ORDER BY** - Sort the results
7. **LIMIT** - Restrict number of results

---

## Practical Examples

### Example 1: Sales Analysis
```sql
-- Find top-selling product categories
SELECT category, 
       COUNT(*) AS total_orders,
       SUM(amount) AS total_revenue,
       AVG(amount) AS avg_order_value
FROM sales 
GROUP BY category
ORDER BY total_revenue DESC
LIMIT 5;
```

### Example 2: Employee Analysis
```sql
-- Department performance analysis
SELECT department,
       COUNT(*) AS employee_count,
       AVG(salary) AS avg_salary,
       MIN(salary) AS min_salary,
       MAX(salary) AS max_salary
FROM employees 
GROUP BY department
HAVING COUNT(*) >= 2  -- Only departments with 2+ employees
ORDER BY avg_salary DESC;
```

### Example 3: Salary Range Analysis
```sql
-- Group employees by salary ranges
SELECT 
    CASE 
        WHEN salary < 60000 THEN 'Low Salary'
        WHEN salary BETWEEN 60000 AND 70000 THEN 'Medium Salary'
        ELSE 'High Salary'
    END AS salary_range,
    COUNT(*) AS employee_count,
    AVG(salary) AS avg_salary_in_range
FROM employees 
GROUP BY salary_range
ORDER BY avg_salary_in_range;
```

### Example 4: Time-based Analysis
```sql
-- Monthly hiring trends
SELECT YEAR(joining_date) AS year,
       MONTH(joining_date) AS month,
       COUNT(*) AS hires_count,
       AVG(salary) AS avg_starting_salary
FROM employees 
GROUP BY YEAR(joining_date), MONTH(joining_date)
ORDER BY year, month;
```

---

##  Internal Working

**How GROUP BY Works Internally:**

1. **Scan Phase:** MySQL reads all records from the table
2. **Key Extraction:** For each row, extracts values from GROUP BY columns
3. **Memory Table Creation:** Creates a temporary in-memory table with:
   - **Key:** Combination of GROUP BY column values
   - **Value:** Aggregated data (count, sum, etc.)
4. **Grouping Process:** 
   - First occurrence: Creates new group
   - Subsequent occurrences: Updates existing group's aggregate values
5. **Result Generation:** Returns the final grouped results

**Example Process:**
```
Original Data:
IT, John, 70000
IT, Jane, 75000  
HR, Bob, 50000

Internal Memory Table:
Key      | Count | Sum    | Avg
---------|-------|--------|-------
IT       | 2     | 145000 | 72500
HR       | 1     | 50000  | 50000
```

---

##  Quick Reference

### Most Common GROUP BY Patterns:
```sql
-- Basic counting
SELECT column, COUNT(*) FROM table GROUP BY column;

-- With ordering
SELECT column, COUNT(*) FROM table 
GROUP BY column ORDER BY COUNT(*) DESC;

-- With filtering
SELECT column, COUNT(*) FROM table 
GROUP BY column HAVING COUNT(*) > threshold;

-- Multiple aggregates
SELECT column, COUNT(*), AVG(numeric_column), SUM(numeric_column)
FROM table GROUP BY column;

-- Multiple grouping columns
SELECT col1, col2, COUNT(*) FROM table 
GROUP BY col1, col2;
```

### Remember:
- ✅ Always use aggregate functions with GROUP BY
- ✅ Only SELECT columns that are in GROUP BY or aggregated
- ✅ Use HAVING for filtering groups, WHERE for filtering rows
- ✅ Follow the correct clause order: SELECT → FROM → WHERE → GROUP BY → HAVING → ORDER BY → LIMIT

---

