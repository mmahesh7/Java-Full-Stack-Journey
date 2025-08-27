# SQL -  FULL, CROSS & SELF JOINs

---

## FULL JOIN

### 📖 Definition
FULL JOIN returns **all records** from both tables, including:
- All matching rows where join condition is met
- All non-matching rows from left table (with NULL for right table columns)
- All non-matching rows from right table (with NULL for left table columns)

### 💡 Key Concept
**FULL JOIN = LEFT JOIN + RIGHT JOIN**
- It combines results of both LEFT and RIGHT JOINs
- Shows complete picture of data from both tables

### 🔧 Syntax

#### Standard SQL (PostgreSQL, SQL Server)
```sql
SELECT columns
FROM table1
FULL JOIN table2 ON table1.column = table2.column;
```

#### MySQL Implementation (using UNION)
```sql
SELECT columns
FROM table1
LEFT JOIN table2 ON table1.column = table2.column
UNION
SELECT columns
FROM table1
RIGHT JOIN table2 ON table1.column = table2.column;
```

### 📝 Example - Friends Theme
```sql
-- Setup
CREATE TABLE characters (
    character_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    occupation VARCHAR(100)
);

CREATE TABLE apartments (
    apartment_id INT PRIMARY KEY,
    building_address VARCHAR(100),
    apartment_number VARCHAR(10),
    monthly_rent DECIMAL(8,2),
    current_tenant_id INT
);

-- Sample Data
INSERT INTO characters VALUES
(1, 'Ross', 'Geller', 'Paleontologist'),
(2, 'Rachel', 'Green', 'Fashion Executive'),
(3, 'Chandler', 'Bing', 'IT Manager'),
(4, 'Monica', 'Geller', 'Chef');

INSERT INTO apartments VALUES
(101, '90 Bedford Street', '20', 3500.00, 3),
(102, '90 Bedford Street', '19', 3500.00, 4),
(103, '5 Morton Street', '14', 2800.00, NULL);

-- FULL JOIN Example (MySQL)
SELECT c.first_name, c.last_name, a.building_address, a.apartment_number
FROM characters c
LEFT JOIN apartments a ON c.character_id = a.current_tenant_id
UNION
SELECT c.first_name, c.last_name, a.building_address, a.apartment_number
FROM characters c  
RIGHT JOIN apartments a ON c.character_id = a.current_tenant_id;
```

### 🎯 Use Cases
- **Complete data analysis**: Show all customers and all orders
- **Data quality checks**: Find unmatched records in both tables
- **Reporting**: Generate comprehensive reports including all data
- **Master-detail relationships**: Display all masters and all details

### ⚡ Performance Tips
- MySQL uses UNION to emulate FULL JOIN (removes duplicates automatically)
- Use WHERE clauses to filter results after JOIN
- Consider indexing JOIN columns for better performance

---

## CROSS JOIN

### 📖 Definition
CROSS JOIN produces the **Cartesian product** of two tables:
- Combines each row from first table with every row from second table
- Results in all possible combinations
- No JOIN condition needed

### 🔢 Formula
**Result rows = Table1 rows × Table2 rows × Table3 rows...**

### 🔧 Syntax
```sql
SELECT columns
FROM table1
CROSS JOIN table2;

-- Alternative syntax
SELECT columns  
FROM table1, table2;
```

### 📝 Example - Product Variations
```sql
-- Setup
CREATE TABLE products (
    product_id INT PRIMARY KEY,
    product_name VARCHAR(50)
);

CREATE TABLE colors (
    color_id INT PRIMARY KEY, 
    color_name VARCHAR(30)
);

CREATE TABLE sizes (
    size_id INT PRIMARY KEY,
    size_name VARCHAR(10)
);

-- Sample Data
INSERT INTO products VALUES
(1, 'T-shirt'), (2, 'Jeans'), (3, 'Sweater'), (4, 'Jacket');

INSERT INTO colors VALUES  
(1, 'Red'), (2, 'Blue'), (3, 'Green'), (4, 'Black'), (5, 'White');

INSERT INTO sizes VALUES
(1, 'S'), (2, 'M'), (3, 'L'), (4, 'XL');

-- CROSS JOIN Example
SELECT p.product_name, c.color_name, s.size_name,
       CONCAT(p.product_name, ' - ', c.color_name, ' - Size ', s.size_name) AS full_description
FROM products p
CROSS JOIN colors c  
CROSS JOIN sizes s;

-- Result: 4 × 5 × 4 = 80 combinations
```

### 🎯 Use Cases
- **Product catalogs**: Generate all product variations (color, size, style)
- **Test data generation**: Create comprehensive test scenarios
- **Scheduling**: Generate all possible time slot combinations
- **Report templates**: Create all possible report parameter combinations

### ⚠️ Performance Warnings
- **Exponential growth**: Be cautious with large tables
- **Memory intensive**: Can consume significant resources
- **Use filters**: Add WHERE clauses to limit results
- **Production caution**: Avoid unfiltered CROSS JOINs in production

### 💡 Filtering Example
```sql
-- Get only T-shirt variations
SELECT p.product_name, c.color_name, s.size_name
FROM products p
CROSS JOIN colors c
CROSS JOIN sizes s  
WHERE p.product_name = 'T-shirt';
-- Result: 1 × 5 × 4 = 20 combinations
```

---

## SELF JOIN

### 📖 Definition
SELF JOIN is a regular JOIN where a table is joined with **itself**:
- Use different aliases to distinguish table instances
- Useful for hierarchical or related data within same table
- Can use any JOIN type (INNER, LEFT, RIGHT)

### 🔧 Syntax
```sql
SELECT columns
FROM table1 alias1
JOIN table1 alias2 ON alias1.column = alias2.column;
```

### 📝 Example - Employee Hierarchy
```sql
-- Setup
CREATE TABLE employees (
    employee_id INT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50), 
    job_title VARCHAR(100),
    salary DECIMAL(10,2),
    department VARCHAR(50),
    manager_id INT,  -- References employee_id
    hire_date DATE
);

-- Sample Data
INSERT INTO employees VALUES
(1, 'James', 'Smith', 'CEO', 150000, 'Executive', NULL, '2010-01-15'),
(2, 'Sarah', 'Johnson', 'CTO', 140000, 'Technology', 1, '2011-03-10'),
(3, 'Michael', 'Williams', 'CFO', 140000, 'Finance', 1, '2012-07-22'),
(5, 'David', 'Miller', 'Senior Developer', 95000, 'Technology', 2, '2014-11-05'),
(6, 'Emily', 'Davis', 'Developer', 80000, 'Technology', 5, '2016-08-12');
```

### 🎯 Common Use Cases

#### 1️⃣ Employee-Manager Relationships
```sql
-- Show employees with their managers
SELECT emp.first_name + ' ' + emp.last_name AS employee,
       mgr.first_name + ' ' + mgr.last_name AS manager,
       emp.job_title
FROM employees emp
JOIN employees mgr ON emp.manager_id = mgr.employee_id;
```

#### 2️⃣ Include All Employees (even without managers)
```sql
-- LEFT JOIN to include CEO (who has no manager)
SELECT emp.first_name + ' ' + emp.last_name AS employee,
       COALESCE(mgr.first_name + ' ' + mgr.last_name, 'No Manager') AS manager
FROM employees emp  
LEFT JOIN employees mgr ON emp.manager_id = mgr.employee_id;
```

#### 3️⃣ Find Employees in Same Department
```sql
-- Find colleagues in same department (avoid duplicates)
SELECT e1.first_name + ' ' + e1.last_name AS employee1,
       e2.first_name + ' ' + e2.last_name AS employee2,
       e1.department
FROM employees e1
JOIN employees e2 ON e1.department = e2.department  
WHERE e1.employee_id < e2.employee_id;  -- Prevents duplicates
```

#### 4️⃣ Salary Comparisons
```sql
-- Find employees earning more than their manager
SELECT emp.first_name + ' ' + emp.last_name AS employee,
       emp.salary AS emp_salary,
       mgr.first_name + ' ' + mgr.last_name AS manager,
       mgr.salary AS mgr_salary
FROM employees emp
JOIN employees mgr ON emp.manager_id = mgr.employee_id
WHERE emp.salary > mgr.salary;
```

#### 5️⃣ Aggregate Analysis
```sql
-- Average salary difference by department
SELECT emp.department,
       COUNT(*) AS num_employees,
       ROUND(AVG(mgr.salary), 2) AS avg_manager_salary,
       ROUND(AVG(emp.salary), 2) AS avg_employee_salary,
       ROUND(AVG(mgr.salary - emp.salary), 2) AS avg_salary_difference
FROM employees emp
JOIN employees mgr ON emp.manager_id = mgr.employee_id
GROUP BY emp.department
ORDER BY avg_salary_difference DESC;
```

### 🎯 Use Cases
- **Organizational charts**: Employee-manager relationships
- **Social networks**: Friend connections, followers
- **Product relationships**: Related products, bundles
- **Geographic data**: City-state-country hierarchies
- **Category trees**: Parent-child categories

---

## Quick Reference

### 🔄 JOIN Types Comparison
| JOIN Type | Returns | Use Case |
|-----------|---------|----------|
| **INNER** | Only matching rows | Common data between tables |
| **LEFT** | All from left + matching from right | Keep all primary records |
| **RIGHT** | All from right + matching from left | Keep all secondary records |
| **FULL** | All from both tables | Complete data picture |
| **CROSS** | All possible combinations | Generate variations |
| **SELF** | Related data within same table | Hierarchical relationships |

### 📊 Result Set Sizes
- **INNER JOIN**: ≤ min(table1, table2)
- **LEFT JOIN**: = table1 rows  
- **RIGHT JOIN**: = table2 rows
- **FULL JOIN**: ≥ max(table1, table2)
- **CROSS JOIN**: = table1 × table2
- **SELF JOIN**: Depends on relationships

### 🎨 Visual Memory Aid
```
INNER: ∩ (intersection)
LEFT:  ⊂ (left includes all)  
RIGHT: ⊃ (right includes all)
FULL:  ∪ (union of all)
CROSS: × (multiplication)
SELF:  ↻ (circular reference)
```

---

## Best Practices

### 🚀 Performance
1. **Index JOIN columns** for better performance
2. **Filter early** with WHERE clauses
3. **Avoid CROSS JOIN** on large tables without filters
4. **Use EXISTS** instead of IN with subqueries
5. **Consider table order** in JOINs (smaller table first)

### 📝 Code Quality  
1. **Always use aliases** for readability
2. **Qualify column names** to avoid ambiguity
3. **Use consistent naming** conventions
4. **Comment complex JOINs** for maintenance
5. **Test with sample data** before production

### ⚠️ Common Pitfalls
1. **Cartesian products**: Missing JOIN conditions
2. **Duplicate data**: Not handling many-to-many properly  
3. **NULL handling**: Forgetting about NULL values
4. **Performance**: Not considering table sizes
5. **Logic errors**: Wrong JOIN type for requirements

### 🔍 Debugging Tips
1. **Start simple**: Test with basic SELECT first
2. **Check row counts**: Verify expected results  
3. **Use EXPLAIN**: Analyze query execution
4. **Sample data**: Test with known datasets
5. **Step by step**: Build complex queries incrementally

### 💡 MySQL Specific Notes
- **No native FULL JOIN**: Use UNION of LEFT and RIGHT
- **UNION removes duplicates** automatically
- **Use UNION ALL** if duplicates needed
- **Consider performance** of UNION operations

---


