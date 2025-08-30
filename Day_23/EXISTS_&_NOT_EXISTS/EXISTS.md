# Types of SQL Subqueries


## What are Subqueries?

A **subquery** (also called a nested query or inner query) is a SQL query nested inside another SQL query. The subquery is executed first, and its result is used by the outer query.

### Basic Syntax
```sql
SELECT column_name
FROM table_name
WHERE column_name operator (SELECT column_name FROM table_name WHERE condition);
```

### Simple Example
```sql
-- Find customers who spent more than the average order amount
SELECT customer_id, first_name, last_name
FROM customers
WHERE customer_id IN (
    SELECT customer_id 
    FROM orders 
    WHERE total_amount > (SELECT AVG(total_amount) FROM orders)
);
```

---

## Types of Subqueries

### 1. Based on Location
- **SELECT Clause Subqueries**: Return a single value for each row
- **WHERE Clause Subqueries**: Filter rows based on subquery results
- **FROM Clause Subqueries**: Treat subquery result as a temporary table
- **HAVING Clause Subqueries**: Filter grouped results

### 2. Based on Result Type
- **Single Row Subqueries**: Return exactly one row
- **Multiple Row Subqueries**: Return multiple rows
- **Multiple Column Subqueries**: Return multiple columns

### 3. Based on Dependency
- **Correlated Subqueries**: Depend on outer query
- **Non-Correlated Subqueries**: Independent of outer query

---

## Correlated vs Non-Correlated Subqueries

### Non-Correlated Subqueries
- **Definition**: Can be executed independently of the outer query
- **Execution**: Runs once and returns result to outer query
- **Performance**: Generally faster for simple cases

```sql
-- Example: Find products more expensive than average
SELECT product_name, price
FROM products
WHERE price > (SELECT AVG(price) FROM products);
```

### Correlated Subqueries
- **Definition**: References columns from the outer query
- **Execution**: Runs once for each row in the outer query
- **Performance**: Can be slower but more flexible

```sql
-- Example: Find customers with above-average spending
SELECT *,
    (SELECT SUM(total_amount) FROM orders WHERE customer_id = customers.customer_id) AS total_spent
FROM customers
WHERE 
    (SELECT SUM(total_amount) FROM orders WHERE customer_id = customers.customer_id) > 
    (SELECT AVG(total_spent) FROM 
        (SELECT customer_id, SUM(total_amount) AS total_spent FROM orders GROUP BY customer_id) AS customer_totals);
```

**Key Point**: Notice how `customers.customer_id` is referenced inside the subquery - this makes it correlated.

---

## Scalar Subqueries

### Definition
A **scalar subquery** returns exactly one value (one row, one column). It can be used wherever a single value is expected.

### Characteristics
- Always returns one value
- Can be independent or correlated
- Can appear in SELECT, WHERE, or HAVING clauses

### Examples

#### In SELECT Clause
```sql
-- Add total spent by each customer
SELECT customer_id, first_name, last_name,
    (SELECT SUM(total_amount) FROM orders WHERE customer_id = customers.customer_id) AS total_spent
FROM customers;
```

#### In WHERE Clause
```sql
-- Find customers who spent more than average
SELECT *
FROM customers
WHERE (SELECT SUM(total_amount) FROM orders WHERE customer_id = customers.customer_id) > 
      (SELECT AVG(total_amount) FROM orders);
```

### Important Note
If a scalar subquery returns more than one row, SQL will throw an error. Always ensure your scalar subqueries return exactly one value.

---

## EXISTS and NOT EXISTS

### EXISTS Operator
- **Purpose**: Tests whether a subquery returns any rows
- **Returns**: TRUE if subquery returns at least one row, FALSE otherwise
- **Performance**: Stops as soon as first match is found (short-circuit evaluation)

### Basic Syntax
```sql
SELECT columns
FROM table1
WHERE EXISTS (SELECT 1 FROM table2 WHERE condition);
```

### Key Points
1. Use `SELECT 1` instead of `SELECT *` for better performance
2. The actual data returned by subquery doesn't matter, only existence
3. More efficient than JOINs when you only need to check existence

### Examples

#### Find Customers with Orders
```sql
-- Using EXISTS
SELECT * 
FROM customers c
WHERE EXISTS (
    SELECT 1 FROM orders o 
    WHERE c.customer_id = o.customer_id
);

-- Equivalent with JOIN
SELECT DISTINCT c.*
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id;
```

#### Find Customers without Orders
```sql
SELECT * 
FROM customers c
WHERE NOT EXISTS (
    SELECT 1 FROM orders o 
    WHERE c.customer_id = o.customer_id
);
```

#### Find Products Never Ordered
```sql
SELECT * 
FROM products p
WHERE NOT EXISTS (
    SELECT 1 FROM order_items oi 
    WHERE oi.product_id = p.product_id
);
```

#### Complex EXISTS Example
```sql
-- Find customers who ordered electronics products
SELECT * 
FROM customers c
WHERE EXISTS (
    SELECT 1 
    FROM orders o
    JOIN order_items oi ON o.order_id = oi.order_id
    JOIN products p ON oi.product_id = p.product_id
    WHERE c.customer_id = o.customer_id
    AND p.category = 'Electronics'
);
```

---

## Subqueries vs JOINs

### When to Use Subqueries
1. **Existence Checks**: Use EXISTS when you only need to know if related data exists
2. **Aggregation**: When you need to compare with aggregate values
3. **Readability**: Sometimes more intuitive for complex logical conditions
4. **Performance**: Can be faster for existence checks on large datasets

### When to Use JOINs
1. **Data Retrieval**: When you need data from multiple tables in the result
2. **Simple Relationships**: For straightforward table relationships
3. **Performance**: Often faster for retrieving actual data
4. **Standards**: More widely understood and maintained

### Performance Comparison

#### EXISTS vs JOIN for Existence Check
```sql
-- EXISTS - Stops at first match (faster for existence)
SELECT * FROM customers c
WHERE EXISTS (SELECT 1 FROM orders o WHERE c.customer_id = o.customer_id);

-- JOIN - May process all matches (slower for existence)
SELECT DISTINCT c.*
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id;
```

#### When You Need Data from Both Tables
```sql
-- JOIN is better here
SELECT c.first_name, c.last_name, p.product_name
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id
WHERE p.category = 'Electronics';

-- Subquery can't return product data in this context
SELECT c.*
FROM customers c
WHERE EXISTS (
    SELECT 1 FROM orders o
    JOIN order_items oi ON o.order_id = oi.order_id
    JOIN products p ON oi.product_id = p.product_id
    WHERE c.customer_id = o.customer_id AND p.category = 'Electronics'
);
-- Note: This only returns customer data, not product data
```

---

## Common Use Cases

### 1. Find Records Above/Below Average
```sql
-- Products priced above average
SELECT product_name, price
FROM products
WHERE price > (SELECT AVG(price) FROM products);
```

### 2. Find Top N Records
```sql
-- Top 3 most expensive products
SELECT product_name, price
FROM products
WHERE price >= (
    SELECT MIN(price) FROM (
        SELECT DISTINCT price 
        FROM products 
        ORDER BY price DESC 
        LIMIT 3
    ) AS top_prices
);
```

### 3. Conditional Aggregation
```sql
-- Customers with above-average order count
SELECT customer_id, 
       (SELECT COUNT(*) FROM orders WHERE customer_id = customers.customer_id) as order_count
FROM customers
WHERE (SELECT COUNT(*) FROM orders WHERE customer_id = customers.customer_id) > 
      (SELECT AVG(order_count) FROM 
          (SELECT COUNT(*) as order_count FROM orders GROUP BY customer_id) AS avg_orders);
```

### 4. Data Validation
```sql
-- Find orders without corresponding order items (data integrity check)
SELECT *
FROM orders o
WHERE NOT EXISTS (
    SELECT 1 FROM order_items oi 
    WHERE oi.order_id = o.order_id
);
```

### 5. Complex Filtering
```sql
-- Customers who have ordered ALL electronics products
SELECT c.customer_id, c.first_name, c.last_name
FROM customers c
WHERE NOT EXISTS (
    SELECT p.product_id
    FROM products p
    WHERE p.category = 'Electronics'
    AND NOT EXISTS (
        SELECT 1
        FROM orders o
        JOIN order_items oi ON o.order_id = oi.order_id
        WHERE o.customer_id = c.customer_id
        AND oi.product_id = p.product_id
    )
);
```

---

## Best Practices

### 1. Performance Optimization
- Use `SELECT 1` instead of `SELECT *` in EXISTS subqueries
- Consider indexing columns used in subquery conditions
- For large datasets, test both subqueries and JOINs to see which performs better

### 2. Readability
- Use meaningful aliases for tables
- Format complex subqueries with proper indentation
- Comment complex logic

### 3. Common Pitfalls to Avoid
- **Missing Aliases**: Every derived table must have an alias
```sql
-- Wrong
SELECT AVG(total_spent) FROM
(SELECT customer_id, SUM(total_amount) AS total_spent FROM orders GROUP BY customer_id);

-- Correct
SELECT AVG(total_spent) FROM
(SELECT customer_id, SUM(total_amount) AS total_spent FROM orders GROUP BY customer_id) AS customer_totals;
```

- **Scalar Subquery Returning Multiple Rows**: Ensure scalar subqueries return exactly one value
- **NULL Handling**: Be aware that NULL values can affect subquery results

### 4. Debugging Tips
- Test subqueries independently first
- Use EXPLAIN to understand query execution plans
- Start simple and build complexity gradually

### 5. Code Organization
```sql
-- Good formatting for complex subqueries
SELECT c.customer_id, 
       c.first_name, 
       c.last_name,
       (SELECT SUM(total_amount) 
        FROM orders o 
        WHERE o.customer_id = c.customer_id) AS total_spent
FROM customers c
WHERE EXISTS (
    SELECT 1 
    FROM orders o
    JOIN order_items oi ON o.order_id = oi.order_id
    JOIN products p ON oi.product_id = p.product_id
    WHERE o.customer_id = c.customer_id
    AND p.category = 'Electronics'
)
ORDER BY total_spent DESC;
```

---

## Quick Reference

### Operators Used with Subqueries
- `=`, `!=`, `<`, `>`, `<=`, `>=` (with scalar subqueries)
- `IN`, `NOT IN` (with multiple row subqueries)
- `EXISTS`, `NOT EXISTS` (for existence checks)
- `ALL`, `ANY`, `SOME` (comparison with multiple values)

### Performance Tips Summary
1. Use EXISTS for existence checks
2. Use JOINs when you need data from multiple tables
3. Use `SELECT 1` in EXISTS subqueries
4. Index columns used in subquery conditions
5. Test both approaches with your actual data volume

### Common Patterns to Remember
1. **Existence**: `WHERE EXISTS (SELECT 1 FROM...)`
2. **Above Average**: `WHERE column > (SELECT AVG(column) FROM...)`
3. **Not in Category**: `WHERE NOT EXISTS (SELECT 1 FROM...)`
4. **Correlated Calculation**: `SELECT (SELECT ... WHERE outer.id = inner.id) FROM outer`

---
