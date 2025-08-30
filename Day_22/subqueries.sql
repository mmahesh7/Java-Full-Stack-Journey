-- Create database and set it as the active database
CREATE DATABASE online_store;
USE online_store;

-- =============================================
-- TABLE CREATION
-- =============================================

-- Create customers table
CREATE TABLE customers (
    customer_id INT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    city VARCHAR(50),
    state VARCHAR(2),
    signup_date DATE
);

-- Create products table
CREATE TABLE products (
    product_id INT PRIMARY KEY AUTO_INCREMENT,
    product_name VARCHAR(100) NOT NULL,
    category VARCHAR(50) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    stock_quantity INT NOT NULL
);

-- Create orders table
CREATE TABLE orders (
    order_id INT PRIMARY KEY AUTO_INCREMENT,
    customer_id INT NOT NULL,
    order_date DATETIME NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- Create order_items table
CREATE TABLE order_items (
    item_id INT PRIMARY KEY AUTO_INCREMENT,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    item_price DECIMAL(10, 2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id),
    FOREIGN KEY (product_id) REFERENCES products(product_id)
);

-- =============================================
-- SAMPLE DATA INSERTION
-- =============================================

-- Insert data into customers
INSERT INTO customers (first_name, last_name, email, city, state, signup_date) VALUES
('John', 'Smith', 'john.smith@example.com', 'New York', 'NY', '2023-01-15'),
('Sarah', 'Johnson', 'sarah.j@example.com', 'Los Angeles', 'CA', '2023-02-20'),
('Michael', 'Brown', 'michael.b@example.com', 'Chicago', 'IL', '2023-03-05'),
('Emily', 'Davis', 'emily.d@example.com', 'Houston', 'TX', '2023-01-30'),
('Robert', 'Wilson', 'robert.w@example.com', 'Phoenix', 'AZ', '2023-02-10'),
('Jennifer', 'Martinez', 'jennifer.m@example.com', 'Philadelphia', 'PA', '2023-03-15'),
('David', 'Anderson', 'david.a@example.com', 'San Antonio', 'TX', '2023-01-25'),
('Lisa', 'Thomas', 'lisa.t@example.com', 'San Diego', 'CA', '2023-02-28'),
('James', 'Jackson', 'james.j@example.com', 'Dallas', 'TX', '2023-03-12'),
('Mary', 'White', 'mary.w@example.com', 'San Jose', 'CA', '2023-01-18');

-- Insert data into products
INSERT INTO products (product_name, category, price, stock_quantity) VALUES
('Laptop Pro', 'Electronics', 1299.99, 25),
('Smartphone X', 'Electronics', 899.99, 50),
('Wireless Headphones', 'Electronics', 199.99, 100),
('Coffee Maker', 'Home Appliances', 79.99, 30),
('Blender', 'Home Appliances', 49.99, 40),
('Running Shoes', 'Sports', 129.99, 75),
('Yoga Mat', 'Sports', 29.99, 120),
('Mystery Novel', 'Books', 14.99, 200),
('Cookbook', 'Books', 24.99, 150),
('Desk Chair', 'Furniture', 149.99, 15);

-- Insert data into orders
INSERT INTO orders (customer_id, order_date, total_amount) VALUES
(1, '2023-04-10 14:30:00', 1499.98),
(2, '2023-04-11 10:15:00', 249.98),
(3, '2023-04-12 16:45:00', 899.99),
(4, '2023-04-13 13:20:00', 1329.98),
(2, '2023-04-14 09:30:00', 49.99),
(5, '2023-04-15 15:10:00', 179.98),
(6, '2023-04-16 11:05:00', 159.98),
(7, '2023-04-17 14:55:00', 39.98),
(8, '2023-04-18 12:40:00', 899.99),
(9, '2023-04-19 16:25:00', 229.98),
(10, '2023-04-20 10:50:00', 279.97),
(1, '2023-04-21 13:35:00', 24.99),
(3, '2023-04-22 15:15:00', 129.99);

SELECT * FROM orders;

-- Insert data into order_items
INSERT INTO order_items (order_id, product_id, quantity, item_price) VALUES
(1, 1, 1, 1299.99),
(1, 3, 1, 199.99),
(2, 5, 1, 49.99),
(2, 7, 1, 29.99),
(2, 9, 1, 24.99),
(3, 2, 1, 899.99),
(4, 1, 1, 1299.99),
(4, 6, 1, 129.99),
(5, 5, 1, 49.99),
(6, 4, 1, 79.99),
(6, 8, 1, 14.99),
(6, 9, 1, 24.99),
(7, 6, 1, 129.99),
(7, 8, 2, 14.99),
(8, 8, 1, 14.99),
(8, 9, 1, 24.99),
(9, 2, 1, 899.99),
(10, 3, 1, 199.99),
(10, 6, 1, 129.99),
(11, 5, 1, 49.99),
(11, 7, 1, 29.99),
(11, 8, 1, 14.99),
(12, 9, 1, 24.99),
(13, 6, 1, 129.99);

-- Find all customers who have placed at least one order
SELECT * FROM customers where customer_id IN
(SELECT DISTINCT customer_id FROM orders);

-- Find all customers who haven't placed at least one order
SELECT * FROM customers where customer_id NOT IN
(SELECT DISTINCT customer_id FROM orders);


-- Find products with a price higher than average
SELECT * FROM products where price > (SELECT avg(price) from products);


-- Find categories that have more than 2 products
(select category, count(*) from products GROUP BY category having count(*) > 2);

-- Find all orders made by customers from Texas
select * from orders where customer_id in
(select customer_id from customers where state = 'tx');

-- Find all customers who ordered electronics products
-- Using JOINs
select * from customers c join orders o on c.customer_id = o.customer_id 
join order_items ot on o.order_id = ot.order_id
join products p on p.product_id = ot.product_id where category = 'electronics';

select c.customer_id, c.first_name, o.order_id, p.product_id from customers c join orders o on c.customer_id = o.customer_id 
join order_items ot on o.order_id = ot.order_id
join products p on p.product_id = ot.product_id where category = 'electronics';

-- Using subquery
select * from customers c 
join orders o on c.customer_id = o.customer_id 
join order_items ot on o.order_id = ot.order_id
where ot.product_id in (SELECT product_id FROM products where category = 'electronics');

-- Customers who spent more than average
-- 1. Calculate the total amount spent by each customer
-- 2. Calculate the average total spending across all customers
-- -- 3. Identify customers whose total spending exceeds this average
-- select *,
-- (SELECT SUM(total_amount) FROM orders WHERE customer_id = customers.customer_id) AS total_spent from customers 
-- where
-- (select sum(total_amount) from orders where customer_id = customers.customer_id) > 
-- (select AVG(total_spent) AS average_customer_spending from 
-- (select customer_id, sum(total_amount) as total_spent from orders group by customer_id) as customer_total);

-- to find avg
(SELECT AVG(total_spent) AS average_customer_spending FROM 
        (SELECT customer_id, SUM(total_amount) AS total_spent FROM orders GROUP BY customer_id) AS customer_total);


SELECT *, 
    (SELECT SUM(total_amount) FROM orders WHERE customer_id = customers.customer_id) AS total_spent 
FROM customers 
WHERE
    (SELECT SUM(total_amount) FROM orders WHERE customer_id = customers.customer_id) > 
    (SELECT AVG(total_spent) AS average_customer_spending FROM 
        (SELECT customer_id, SUM(total_amount) AS total_spent FROM orders GROUP BY customer_id) AS customer_total);

-- Find customers who have ordered all products in the 'Electronics' category
SELECT c.email
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN products p ON oi.product_id = p.product_id
WHERE p.category = "Electronics"
GROUP BY c.customer_id
HAVING COUNT(DISTINCT p.product_id) = (SELECT COUNT(*) FROM products WHERE category="Electronics");

-- Find all customers who are not from California but have purchased the same product-quantity combinations as California customers

--finding of california's......
(SELECT c.email, c.state, oi.product_id, oi.quantity  from customers c 
join orders o on c.customer_id = o.customer_id
join order_items oi on oi.order_id = o.order_id
where c.state = "ca");


SELECT c.email, c.state, oi.product_id, oi.quantity from customers c
join orders o on c.customer_id = o.customer_id
join order_items oi on o.order_id = oi.order_id
where c.state != 'ca' and (oi.product_id, oi.quantity) in 
(SELECT oi.product_id, oi.quantity from customers c 
join orders o on c.customer_id = o.customer_id
join order_items oi on oi.order_id = o.order_id
where c.state = "ca"); 

--  Show customer details with their total spending
SELECT c.*, 
       (SELECT SUM(total_amount) FROM orders WHERE customer_id = c.customer_id) AS total_spent
FROM customers c
WHERE (SELECT SUM(total_amount) FROM orders WHERE customer_id = c.customer_id) > 500;