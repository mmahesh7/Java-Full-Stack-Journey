# LEFT JOIN and RIGHT JOIN 
---

## LEFT JOIN (LEFT OUTER JOIN)

### Definition
LEFT JOIN returns **ALL records from the left table** and **only matching records from the right table**. If no match exists in the right table, **NULL values** will be returned for the right table's columns.

### Key Points
- **All rows from LEFT table** are always included
- **Matching rows from RIGHT table** are included
- **Non-matching right table data** shows as NULL
- `LEFT JOIN` and `LEFT OUTER JOIN` are the same thing
- There's no such thing as "LEFT INNER JOIN" (doesn't make sense)

### Basic Syntax
```sql
SELECT columns
FROM table1
LEFT JOIN table2 ON table1.column = table2.column;
```

### Visual Understanding
```
LEFT TABLE (customers)    RIGHT TABLE (orders)    RESULT
John Smith               Order 101               John Smith + Order 101
Jane Doe                 Order 102               Jane Doe + Order 102  
Emily Davis              (no orders)             Emily Davis + NULL
Michael Brown            (no orders)             Michael Brown + NULL
```

---

## Setting Up Example Tables

```sql
-- Create database
CREATE DATABASE left_join_tutorial;
USE left_join_tutorial;

-- Create customers table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY,
    customer_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    city VARCHAR(50)
);

-- Create orders table
CREATE TABLE orders (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_date DATE NOT NULL,
    total_amount DECIMAL(10, 2),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Insert sample data
INSERT INTO customers (customer_id, customer_name, email, city) VALUES
(1, 'John Smith', 'john@example.com', 'New York'),
(2, 'Jane Doe', 'jane@example.com', 'Los Angeles'),
(3, 'Robert Johnson', 'robert@example.com', 'Chicago'),
(4, 'Emily Davis', 'emily@example.com', 'Houston'),
(5, 'Michael Brown', 'michael@example.com', 'Phoenix');

INSERT INTO orders (order_id, customer_id, order_date, total_amount) VALUES
(101, 1, '2023-01-15', 150.75),
(102, 3, '2023-01-16', 89.50),
(103, 1, '2023-01-20', 45.25),
(104, 2, '2023-01-25', 210.30);
-- Note: Emily Davis (ID: 4) and Michael Brown (ID: 5) have no orders
```

---

## LEFT JOIN Examples

### Example 1: Basic LEFT JOIN
```sql
-- Get ALL customers and their orders (if any)
SELECT 
    c.customer_id,
    c.customer_name,
    o.order_id,
    o.order_date,
    o.total_amount
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id;
```

**Result:**
- Shows all 5 customers
- Emily and Michael have NULL values for order columns
- John appears twice (has 2 orders)

### Example 2: Finding Customers Without Orders
```sql
-- Find customers who haven't placed any orders
SELECT 
    c.customer_id,
    c.customer_name
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE o.order_id IS NULL;
```

**Result:**
- Only Emily Davis and Michael Brown
- Perfect for finding inactive customers

### Example 3: Using Aggregate Functions with LEFT JOIN
```sql
-- Count orders and total spending per customer (including 0 orders)
SELECT 
    c.customer_id,
    c.customer_name,
    COUNT(o.order_id) AS order_count,
    IFNULL(SUM(o.total_amount), 0) AS total_spent
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name;
```

**Key Points:**
- `COUNT(o.order_id)` counts actual orders (NULL values not counted)
- `IFNULL(SUM(o.total_amount), 0)` replaces NULL with 0 for customers without orders

### Example 4: Multiple LEFT JOINs
```sql
-- Create shipping table first
CREATE TABLE shipping (
    shipping_id INT PRIMARY KEY,
    order_id INT,
    shipping_date DATE,
    carrier VARCHAR(50),
    tracking_number VARCHAR(50),
    FOREIGN KEY (order_id) REFERENCES orders(order_id)
);

INSERT INTO shipping VALUES
(1001, 101, '2023-01-16', 'FedEx', 'FDX123456789'),
(1002, 104, '2023-01-26', 'UPS', 'UPS987654321');

-- Get customers, their orders, and shipping information
SELECT 
    c.customer_name,
    o.order_id,
    o.order_date,
    s.carrier,
    s.tracking_number
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
LEFT JOIN shipping s ON o.order_id = s.order_id;
```

**Result:**
- All customers shown
- Some orders without shipping info show NULL
- Customers without orders show NULL for everything

---

## Filtering with LEFT JOIN

### Method 1: WHERE Clause (Filter After Join)
```sql
-- Only show New York customers and their orders
SELECT 
    c.customer_name,
    o.order_id,
    o.total_amount
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE c.city = 'New York';
```
**Result:** Only New York customers appear in output

### Method 2: Subquery Pre-filtering (Most Efficient)
```sql
-- Filter customers first, then join (better performance)
SELECT 
    c.customer_name,
    o.order_id,
    o.total_amount
FROM (SELECT * FROM customers WHERE city = 'New York') c
LEFT JOIN orders o ON c.customer_id = o.customer_id;
```
**Benefits:**
- Reduces number of rows to join
- Better performance on large tables
- Join operation becomes less expensive

---

## Real-World LEFT JOIN Use Case

### Finding Inactive Customers (Last 30 Days)
```sql
-- Find customers who haven't ordered in the past 30 days or never ordered
SELECT 
    c.customer_id,
    c.customer_name,
    MAX(o.order_date) AS last_order_date
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id, c.customer_name
HAVING 
    MAX(o.order_date) IS NULL 
    OR MAX(o.order_date) < DATE_SUB(CURDATE(), INTERVAL 30 DAY);
```

**Why HAVING instead of WHERE?**
- `HAVING` filters AFTER grouping
- `WHERE` would filter BEFORE grouping
- If we used WHERE, we might exclude recent orders and get wrong results

---

## RIGHT JOIN (RIGHT OUTER JOIN)

### Definition
RIGHT JOIN returns **ALL records from the right table** and **only matching records from the left table**. If no match exists in the left table, **NULL values** will be returned for the left table's columns.

### Key Points
- **All rows from RIGHT table** are always included
- **Matching rows from LEFT table** are included  
- **Non-matching left table data** shows as NULL
- `RIGHT JOIN` and `RIGHT OUTER JOIN` are the same thing
- Less commonly used than LEFT JOIN

### Basic Syntax
```sql
SELECT columns
FROM table1
RIGHT JOIN table2 ON table1.column = table2.column;
```

---

## RIGHT JOIN Examples

### Setting Up Apartment Example
```sql
-- Create apartment database
CREATE DATABASE gokuldham_society;
USE gokuldham_society;

-- Create apartments table
CREATE TABLE apartments (
    apartment_id INT PRIMARY KEY,
    apartment_number VARCHAR(10) NOT NULL,
    floor_number INT NOT NULL,
    wing_name CHAR(1) NOT NULL
);

-- Create residents table
CREATE TABLE residents (
    resident_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    occupation VARCHAR(100),
    apartment_id INT,
    FOREIGN KEY (apartment_id) REFERENCES apartments(apartment_id)
);

-- Insert apartments (some will be empty)
INSERT INTO apartments VALUES
(1, '101', 1, 'A'), (2, '102', 1, 'A'), (3, '201', 2, 'A'),
(4, '202', 2, 'A'), (5, '301', 3, 'A'), (6, '302', 3, 'A'),
(7, '401', 4, 'A'), (8, '402', 4, 'A'), (9, '501', 5, 'B'), (10, '502', 5, 'B');

-- Insert residents (only for some apartments)
INSERT INTO residents VALUES
(1, 'Jethalal', 'Gada', 'Electronics Shop Owner', 1),
(2, 'Daya', 'Gada', 'Housewife', 1),
(3, 'Taarak', 'Mehta', 'Writer', 2),
(4, 'Anjali', 'Mehta', 'Teacher', 2),
(5, 'Popatlal', 'Pandey', 'Reporter', 3);
-- Note: Apartments 6, 7, 8, 9, 10 have no residents
```

### Example 1: Basic RIGHT JOIN
```sql
-- Get all apartments and their residents (if any)
SELECT 
    a.apartment_number,
    a.floor_number,
    a.wing_name,
    r.first_name,
    r.last_name
FROM residents r
RIGHT JOIN apartments a ON r.apartment_id = a.apartment_id;
```

**Result:**
- Shows all 10 apartments
- Empty apartments show NULL for resident information
- Some apartments show multiple residents

---

## LEFT JOIN vs RIGHT JOIN Equivalence

These two queries produce **exactly the same result**:

### Using LEFT JOIN
```sql
SELECT 
    a.apartment_number,
    r.first_name,
    r.last_name
FROM apartments a
LEFT JOIN residents r ON a.apartment_id = r.apartment_id;
```

### Using RIGHT JOIN (Equivalent)
```sql
SELECT 
    a.apartment_number,
    r.first_name,
    r.last_name  
FROM residents r
RIGHT JOIN apartments a ON r.apartment_id = a.apartment_id;
```

**Key Point:** RIGHT JOIN can always be rewritten as LEFT JOIN by switching table order.

---

## Key Differences and When to Use

### LEFT JOIN
**Use When:**
- You want ALL records from the main/primary table
- Finding missing relationships (customers without orders)
- Reporting where you need complete list from main table
- More intuitive and commonly used

**Common Scenarios:**
- All customers with their orders (if any)
- All products with their sales (if any)  
- All employees with their departments (if any)

### RIGHT JOIN
**Use When:**
- You want ALL records from the secondary table
- Less common in practice
- Can be converted to LEFT JOIN easily

**Why LEFT JOIN is Preferred:**
- More intuitive reading (left-to-right)
- Industry standard approach
- Easier to understand query flow

---

## Advanced Techniques

### 1. Handling NULL Values
```sql
-- Replace NULL with meaningful defaults
SELECT 
    c.customer_name,
    IFNULL(COUNT(o.order_id), 0) AS order_count,
    IFNULL(SUM(o.total_amount), 0) AS total_spent,
    COALESCE(MAX(o.order_date), 'Never ordered') AS last_order
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
GROUP BY c.customer_id;
```

### 2. Complex Filtering Examples
```sql
-- Find apartments on specific floor with no residents
SELECT 
    a.apartment_number,
    a.floor_number
FROM apartments a
LEFT JOIN residents r ON a.apartment_id = r.apartment_id
WHERE a.floor_number = 3 AND r.resident_id IS NULL;

-- Count residents per floor
SELECT 
    a.floor_number,
    COUNT(r.resident_id) AS resident_count,
    COUNT(a.apartment_id) AS total_apartments,
    COUNT(a.apartment_id) - COUNT(r.resident_id) AS empty_apartments
FROM apartments a
LEFT JOIN residents r ON a.apartment_id = r.apartment_id
GROUP BY a.floor_number;
```

### 3. Multiple Table JOINs
```sql
-- Add maintenance requests table
CREATE TABLE maintenance_requests (
    request_id INT PRIMARY KEY,
    apartment_id INT,
    request_date DATE NOT NULL,
    description TEXT NOT NULL,
    status ENUM('Pending', 'In Progress', 'Completed') DEFAULT 'Pending',
    FOREIGN KEY (apartment_id) REFERENCES apartments(apartment_id)
);

-- Get complete apartment information
SELECT 
    a.apartment_number,
    r.first_name,
    m.status
FROM apartments a
LEFT JOIN residents r ON a.apartment_id = r.apartment_id
LEFT JOIN maintenance_requests m ON a.apartment_id = m.apartment_id;
```

---

## Common Mistakes to Avoid

### 1. Wrong NULL Checks
```sql
-- ❌ WRONG: Using = NULL
WHERE o.order_id = NULL

-- ✅ CORRECT: Using IS NULL
WHERE o.order_id IS NULL
```

### 2. Counting NULLs
```sql
-- ❌ WRONG: COUNT(*) counts NULL rows too
COUNT(*) -- This counts customers without orders as 1

-- ✅ CORRECT: COUNT(column) ignores NULL values  
COUNT(o.order_id) -- This gives actual order count
```

### 3. Using WHERE vs HAVING
```sql
-- ❌ WRONG: Using WHERE with aggregates
WHERE MAX(o.order_date) < '2023-01-01'

-- ✅ CORRECT: Using HAVING with aggregates
HAVING MAX(o.order_date) < '2023-01-01'
```

### 4. Forgetting Table Aliases
```sql
-- ❌ WRONG: Ambiguous column names
SELECT customer_id, order_id
FROM customers
LEFT JOIN orders ON customer_id = customer_id

-- ✅ CORRECT: Clear table aliases
SELECT c.customer_id, o.order_id  
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
```

---

## Performance Tips

### 1. Create Proper Indexes
```sql
-- Index on foreign key columns
CREATE INDEX idx_orders_customer_id ON orders(customer_id);
CREATE INDEX idx_residents_apartment_id ON residents(apartment_id);
```

### 2. Filter Before Joining
```sql
-- ✅ GOOD: Filter first, then join
FROM (SELECT * FROM customers WHERE active = 1) c
LEFT JOIN orders o ON c.customer_id = o.customer_id

-- ❌ LESS EFFICIENT: Join everything, then filter
FROM customers c  
LEFT JOIN orders o ON c.customer_id = o.customer_id
WHERE c.active = 1
```

### 3. Use LIMIT When Possible
```sql
SELECT c.customer_name, o.order_id
FROM customers c
LEFT JOIN orders o ON c.customer_id = o.customer_id
LIMIT 100;
```

---

## Quick Reference Summary

| Aspect | LEFT JOIN | RIGHT JOIN |
|--------|-----------|------------|
| **Returns** | ALL from left + matching from right | ALL from right + matching from left |
| **NULL Values** | In right table columns | In left table columns |  
| **Common Use** | Get all main records + related data | Less common, can rewrite as LEFT |
| **Syntax** | `FROM table1 LEFT JOIN table2` | `FROM table1 RIGHT JOIN table2` |
| **Best Practice** | Preferred approach | Convert to LEFT JOIN |

### Essential Functions
- `IS NULL` / `IS NOT NULL` - Check for missing data
- `IFNULL(value, replacement)` - Handle NULL values (MySQL)
- `COUNT(column)` vs `COUNT(*)` - Different behavior with NULL
