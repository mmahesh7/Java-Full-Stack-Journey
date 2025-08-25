# MySQL Aliases



## 🎯 What are Aliases?

### Definition
**Aliases are temporary names** assigned to database tables, columns, or expressions to make them:
- More readable
- Easier to reference
- Shorter to write
- More meaningful in output

### Key Points
- ✅ **Temporary**: Only exist for the duration of the query
- ✅ **Flexible**: Can be used with columns, tables, and expressions
- ✅ **Optional**: The `AS` keyword is optional in most cases
- ✅ **Case-sensitive**: Follow your database's case sensitivity rules

---

## 📊 Column Aliases

### Basic Syntax
```sql
SELECT column_name AS alias_name
FROM table_name;

-- AS keyword is optional
SELECT column_name alias_name
FROM table_name;
```

### Simple Example
```sql
-- Original column name appears in output
SELECT emp_id FROM employees;
```

```sql
-- Using alias for better readability
SELECT emp_id AS id FROM employees;
```

### Practical Examples
```sql
-- Multiple column aliases
SELECT 
    emp_id AS employee_id,
    first_name AS fname,
    last_name AS lname,
    salary AS monthly_salary
FROM employees;
```

---

##  Expression Aliases

### Definition
Aliases can be applied to **calculated expressions** and **function results** to make output more meaningful.

### Mathematical Expressions
```sql
-- Salary with 10% increment
SELECT 
    salary,
    salary * 1.1 AS new_salary
FROM employees;
```


### String Functions
```sql
-- Concatenating first and last name
SELECT 
    CONCAT(first_name, ' ', last_name) AS full_name
FROM employees;
```


### Aggregate Functions
```sql
-- Using aliases with aggregate functions
SELECT 
    COUNT(*) AS total_employees,
    AVG(salary) AS average_salary,
    MAX(salary) AS highest_salary,
    MIN(salary) AS lowest_salary
FROM employees;
```

---

##  Table Aliases

### Basic Syntax
```sql
SELECT column_name
FROM table_name AS alias_name;

-- AS keyword is optional
SELECT column_name
FROM table_name alias_name;
```

### Simple Example
```sql
-- Using table alias for shorter reference
SELECT e.emp_id, e.salary
FROM employees AS e;

-- Same result without AS keyword
SELECT e.emp_id, e.salary
FROM employees e;
```

---

##  Advanced Alias Usage

### Subquery Aliases
```sql
-- Subquery must have an alias
SELECT avg_sal.average_salary
FROM (
    SELECT AVG(salary) AS average_salary 
    FROM employees
) AS avg_sal;
```

### CASE Statement Aliases
```sql
-- Using aliases with CASE statements
SELECT 
    first_name,
    last_name,
    salary,
    CASE 
        WHEN salary > 60000 THEN 'High'
        WHEN salary > 50000 THEN 'Medium'
        ELSE 'Low'
    END AS salary_category
FROM employees;
```
---

## 💡 Best Practices

### ✅ DO's

#### 1. Use Meaningful Names
```sql
-- ✅ Good: Descriptive aliases
SELECT 
    COUNT(*) AS total_orders,
    SUM(amount) AS total_revenue
FROM orders;

-- ❌ Bad: Meaningless aliases  
SELECT 
    COUNT(*) AS c,
    SUM(amount) AS s
FROM orders;
```

#### 2. Use Aliases for Complex Expressions
```sql
-- ✅ Good: Complex calculation with alias
SELECT 
    product_name,
    (price * quantity * (1 - discount_rate)) AS final_amount
FROM order_items;
```

#### 3. Consistent Naming Convention
```sql
-- ✅ Good: Consistent snake_case
SELECT 
    emp_id AS employee_id,
    dept_id AS department_id
FROM employees;
```

#### 4. Use Table Aliases in JOINs
```sql
-- ✅ Always use table aliases in JOINs
SELECT e.name, d.department_name
FROM employees e
JOIN departments d ON e.dept_id = d.id;
```

### ❌ DON'Ts

#### 1. Don't Use Reserved Keywords
```sql
-- ❌ Bad: Using reserved words
SELECT salary AS select FROM employees;  -- Error!

-- ✅ Good: Use descriptive, non-reserved names
SELECT salary AS monthly_salary FROM employees;
```

#### 2. Don't Make Aliases Too Long
```sql
-- ❌ Bad: Overly long alias
SELECT emp_id AS employee_identification_number FROM employees;

-- ✅ Good: Concise but clear
SELECT emp_id AS employee_id FROM employees;
```

---

## 🎯 Common Use Cases

### 1. Reporting and Analytics
```sql
-- Clear column names for reports
SELECT 
    YEAR(hire_date) AS hire_year,
    COUNT(*) AS employees_hired,
    AVG(salary) AS average_starting_salary
FROM employees
GROUP BY YEAR(hire_date);
```

### 2. Data Export
```sql
-- User-friendly column names for export
SELECT 
    emp_id AS "Employee ID",
    CONCAT(first_name, ' ', last_name) AS "Full Name",
    FORMAT(salary, 2) AS "Annual Salary"
FROM employees;
```

### 3. Complex Calculations
```sql
-- Multiple related calculations
SELECT 
    product_name,
    price AS original_price,
    price * 0.9 AS discounted_price,
    price * 0.1 AS discount_amount
FROM products;
```

### 4. Self-Joins
```sql
-- Using aliases to distinguish same table
SELECT 
    e1.first_name AS employee_name,
    e2.first_name AS manager_name
FROM employees e1
LEFT JOIN employees e2 ON e1.manager_id = e2.emp_id;
```

---

## 🔍 Quick Reference

### Syntax Summary
```sql
-- Column alias
SELECT column AS alias_name FROM table;

-- Table alias  
SELECT t.column FROM table AS t;

-- Expression alias
SELECT (expression) AS alias_name FROM table;

-- Subquery alias
SELECT * FROM (subquery) AS alias_name;
```

### Common Patterns

| Use Case | Example | Purpose |
|----------|---------|---------|
| Rename Column | `emp_id AS id` | Cleaner output |
| Calculate Value | `salary * 1.1 AS new_salary` | Show calculations |
| Combine Columns | `CONCAT(fname, lname) AS full_name` | Merge data |
| Shorten Table Name | `employees e` | Easier typing |
| Aggregate Function | `COUNT(*) AS total` | Meaningful labels |

### Keywords
- `AS` - Optional keyword for creating aliases
- Can be omitted in most cases
- Required for subquery aliases in some databases

---

## 🎯 Key Takeaways

1. **Aliases improve readability** - Make your queries more understandable
2. **`AS` keyword is optional** - But using it makes intention clearer  
3. **Essential for JOINs** - Table aliases prevent ambiguity
4. **Required for subqueries** - Derived tables must have aliases
5. **Case expressions benefit** - Complex logic needs meaningful names
6. **Consistent naming** - Follow your team's conventions
7. **Avoid reserved words** - Don't use SQL keywords as aliases

---

## 📝 Practice Examples

### Basic Practice
```sql
-- Try these exercises:

-- 1. Create full address from separate columns
SELECT CONCAT(street, ', ', city, ', ', state) AS full_address
FROM addresses;

-- 2.  Format currency values
SELECT 
    product_name,
    CONCAT('$', FORMAT(price, 2)) AS formatted_price
FROM products;
```

---

