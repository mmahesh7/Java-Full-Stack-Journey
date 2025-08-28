# MySQL Subqueries

## Introduction to Subqueries

### What are Subqueries?
- **Definition**: Subqueries (also called nested queries or inner queries) are SQL queries embedded inside another SQL statement
- **Purpose**: Allow you to use the result of one query as input for another query
- **Alternative Names**: Nested queries, Inner queries

### Key Concepts
- The **inner query** (subquery) executes first
- Its result is used by the **outer query** (main query)
- Subqueries can be used in SELECT, WHERE, FROM, and HAVING clauses
- They create a temporary result set that acts like a table

---

## Basic Syntax and Structure

### General Structure
```sql
SELECT columns
FROM table1
WHERE column_name OPERATOR (
    SELECT column_name 
    FROM table2 
    WHERE condition
);
```

### Key Components
- **Outer Query**: The main SQL statement
- **Inner Query**: The subquery enclosed in parentheses
- **Operator**: IN, NOT IN, =, >, <, >=, <=, EXISTS, etc.

---

## Types of Subqueries

### 1. Single Row Subqueries
Return exactly one row and one column
```sql
-- Example: Products with price higher than average
SELECT * FROM products 
WHERE price > (SELECT AVG(price) FROM products);
```

### 2. Multiple Row Subqueries
Return multiple rows (but single column)
```sql
-- Example: Customers who have placed orders
SELECT * FROM customers 
WHERE customer_id IN (SELECT DISTINCT customer_id FROM orders);
```

### 3. Multiple Column Subqueries
Return multiple columns and rows
```sql
-- Example: Product-quantity combinations
WHERE (product_id, quantity) IN (SELECT product_id, quantity FROM other_table);
```



---

## Basic Subquery Examples

### Example 1: Find Customers Who Placed Orders
```sql
-- Using subquery with IN operator
SELECT * FROM customers 
WHERE customer_id IN (
    SELECT DISTINCT customer_id FROM orders
);
```
**Explanation**: 
- Inner query gets all customer IDs from orders table
- Outer query returns customer details for those IDs

### Example 2: Find Customers Who Haven't Placed Orders
```sql
-- Using subquery with NOT IN operator
SELECT * FROM customers 
WHERE customer_id NOT IN (
    SELECT DISTINCT customer_id FROM orders
);
```
**Explanation**: 
- Same logic as above but with NOT IN
- Returns customers whose IDs are NOT in the orders table

### Example 3: Products Above Average Price
```sql
-- Using subquery with comparison operator
SELECT * FROM products 
WHERE price > (
    SELECT AVG(price) FROM products
);
```
**Explanation**: 
- Inner query calculates average price of all products
- Outer query returns products with price higher than this average

### Example 4: Orders from Specific State
```sql
-- Find all orders made by customers from Texas
SELECT * FROM orders 
WHERE customer_id IN (
    SELECT customer_id FROM customers WHERE state = 'TX'
);
```
**Explanation**: 
- Inner query finds all customer IDs from Texas
- Outer query returns orders placed by these customers

---

## Subqueries vs JOINs

### When to Use Subqueries
- When you need a filtered subset of data
- For aggregate comparisons (like average)
- When the relationship is simple and you don't need all columns

### When to Use JOINs
- When you need columns from multiple tables in the result
- For better performance in complex queries
- When you need to see related data side by side

### Comparison Example
```sql
-- Using SUBQUERY (returns only order information)
SELECT * FROM orders 
WHERE customer_id IN (
    SELECT customer_id FROM customers WHERE state = 'TX'
);

-- Using JOIN (returns both customer and order information)
SELECT * FROM customers c 
JOIN orders o ON c.customer_id = o.customer_id 
WHERE c.state = 'TX';
```

---

## Advanced Subquery Patterns

### Pattern 1: Subquery in FROM Clause (Derived Tables)
```sql
-- Calculate average spending per customer
SELECT AVG(total_spent) AS average_customer_spending 
FROM (
    SELECT customer_id, SUM(total_amount) AS total_spent 
    FROM orders 
    GROUP BY customer_id
) AS customer_totals;
```

### Pattern 2: Complex WHERE Conditions
```sql
-- Customers who spent more than average (using simple subquery approach)
SELECT * FROM customers 
WHERE customer_id IN (
    SELECT customer_id FROM (
        SELECT customer_id, SUM(total_amount) AS customer_total
        FROM orders 
        GROUP BY customer_id
        HAVING SUM(total_amount) > (
            SELECT AVG(total_spent) FROM 
            (SELECT SUM(total_amount) AS total_spent FROM orders GROUP BY customer_id) AS customer_totals
        )
    ) AS high_spenders
);
```

### Pattern 3: Multiple Conditions with Subqueries
```sql
-- Customers who ordered Electronics products
SELECT * FROM customers c 
JOIN orders o ON c.customer_id = o.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
WHERE oi.product_id IN (
    SELECT product_id FROM products WHERE category = 'Electronics'
);
```

---

## Common Errors and Solutions

### Error 1: Missing Table Alias
```sql
-- WRONG: This will cause an error
SELECT AVG(total_spent) FROM
(SELECT SUM(total_amount) AS total_spent FROM orders GROUP BY customer_id);

-- CORRECT: Every derived table must have an alias
SELECT AVG(total_spent) FROM
(SELECT SUM(total_amount) AS total_spent FROM orders GROUP BY customer_id) AS customer_totals;
```

### Error 2: NULL Values with NOT IN
```sql
-- PROBLEMATIC: If subquery returns NULL, NOT IN might not work as expected
SELECT * FROM customers 
WHERE customer_id NOT IN (SELECT customer_id FROM orders);

-- SAFER: Handle NULLs explicitly
SELECT * FROM customers 
WHERE customer_id NOT IN (SELECT customer_id FROM orders WHERE customer_id IS NOT NULL);
```

### Error 3: Multiple Values with Single Row Operators
```sql
-- WRONG: Using = with multiple row subquery
SELECT * FROM products 
WHERE price = (SELECT price FROM products WHERE category = 'Electronics');

-- CORRECT: Use IN for multiple values
SELECT * FROM products 
WHERE price IN (SELECT price FROM products WHERE category = 'Electronics');
```

---

## Performance Considerations

### Optimization Tips

1. **Use DISTINCT in Subqueries**: Reduces duplicate processing
```sql
SELECT * FROM customers 
WHERE customer_id IN (SELECT DISTINCT customer_id FROM orders);
```

2. **Consider JOINs for Better Performance**: Often faster than subqueries
```sql
-- Subquery version
SELECT * FROM products WHERE category IN (SELECT category FROM categories WHERE active = 1);

-- JOIN version (often faster)
SELECT p.* FROM products p JOIN categories c ON p.category = c.category WHERE c.active = 1;
```

3. **Use Appropriate Operators**: Choose the right operator for your use case
```sql
-- For single values
WHERE column = (SELECT column FROM table WHERE condition);

-- For multiple values  
WHERE column IN (SELECT column FROM table WHERE condition);
```

---

## Practice Database Schema

### Tables Structure
```sql
-- Customers table
customers (customer_id, first_name, last_name, email, city, state, signup_date)

-- Products table  
products (product_id, product_name, category, price, stock_quantity)

-- Orders table
orders (order_id, customer_id, order_date, total_amount)

-- Order Items table
order_items (item_id, order_id, product_id, quantity, item_price)
```

### Sample Data Relationships
- One customer can have multiple orders
- One order can have multiple order items
- Each order item references one product
- Products are categorized (Electronics, Home Appliances, Sports, Books, Furniture)

---

## Quick Reference - Common Subquery Patterns

### 1. Find records that exist in another table
```sql
WHERE column IN (SELECT column FROM other_table)
```

### 2. Find records that don't exist in another table
```sql
WHERE column NOT IN (SELECT column FROM other_table WHERE column IS NOT NULL)
```

### 3. Compare with aggregate functions
```sql
WHERE column > (SELECT AVG(column) FROM table)
```

### 4. Multiple column comparison
```sql
WHERE (col1, col2) IN (SELECT col1, col2 FROM other_table)
```

---

## Practice Exercises

Try these queries with the sample database:

1. **Basic**: Find products that cost more than $100
2. **Intermediate**: Find customers who have spent more than $500 total
3. **Advanced**: Find customers who have ordered every product in the Electronics category
4. **Expert**: Find the top 3 customers by spending in each state

