CREATE DATABASE db_for_exists;

use db_for_exists;

SELECT DATABASE();

-- =============================================
-- CORRELATED SUBQUERIES AND EXISTS
-- =============================================

-- Correlated Subqueries
-- A correlated subquery is a subquery that uses values from the outer query. 
-- Unlike regular subqueries which can be executed independently, correlated subqueries are dependent on the outer query and
-- must be re-evaluated for each row processed by the outer query.
-- Can Appear in Various SQL Clauses (SELECT / WHERE / HAVING)

-- Scalar subquery
-- Always Returns Exactly One Value
-- Can Be Independent or Correlated
-- Can Appear in Various SQL Clauses (SELECT / WHERE / HAVING)

-- Find customers who have placed at least one order
-- Using JOIN
SELECT DISTINCT c.customer_id, c.email 
FROM customers c 
JOIN orders o ON c.customer_id = o.customer_id 
ORDER BY c.customer_id;

-- Using EXISTS
SELECT * FROM customers c 
WHERE EXISTS (
    SELECT 1 FROM orders o WHERE c.customer_id = o.customer_id
);

-- Find customers who haven't placed any orders
SELECT * FROM customers c 
WHERE NOT EXISTS (
    SELECT 1 FROM orders o WHERE c.customer_id = o.customer_id
);

-- Products that have never been ordered
SELECT * FROM products p 
WHERE NOT EXISTS (
    SELECT 1 FROM order_items oi WHERE oi.product_id = p.product_id
);

-- Find customers who have ordered electronics products
-- Using JOINs
SELECT DISTINCT c.customer_id, c.first_name, c.last_name
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id
WHERE p.category = 'Electronics';

-- Using EXISTS
SELECT * FROM customers c 
WHERE EXISTS (
    SELECT 1 FROM orders o 
    JOIN order_items oi ON o.order_id = oi.order_id
    JOIN products p ON oi.product_id = p.product_id
    WHERE c.customer_id = o.customer_id
    AND p.category = 'Electronics'
);