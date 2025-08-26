-- SECTION 1: Basic Primary Key Implementation
-- Creating a table with a simple primary key
CREATE TABLE students (
    student_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100)
);

-- Inserting records with valid primary keys
INSERT INTO students (student_id, first_name, last_name, email)
VALUES 
(1, 'John', 'Smith', 'john.smith@example.com'),
(2, 'Maria', 'Garcia', 'maria.garcia@example.com'),
(3, 'Ahmed', 'Khan', 'ahmed.khan@example.com');

-- Demonstrating primary key constraint - This will fail
INSERT INTO students (student_id, first_name, last_name, email)
VALUES (1, 'Jane', 'Doe', 'jane.doe@example.com');
-- Error Code: 1062. Duplicate entry '1' for key 'PRIMARY'

-- SECTION 2: Auto-increment Primary Keys
-- Creating a table with an auto-increment primary key
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    product_name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    description TEXT
);

-- With auto-increment, we don't need to specify the primary key value
INSERT INTO products (product_name, price, description)
VALUES 
('Laptop', 1299.99, 'High-performance laptop'),
('Smartphone', 799.99, 'Latest model smartphone'),
('Headphones', 199.99, 'Noise-cancelling headphones');

-- View the auto-generated IDs
SELECT * FROM products;

-- SECTION 3: Adding Primary Keys to Existing Tables
-- Creating a table with a primary key defined separately
CREATE TABLE orders (
    order_id INT,
    customer_id INT,
    order_date DATE NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    PRIMARY KEY (order_id)
);

-- Create table without primary key
CREATE TABLE suppliers (
    supplier_id INT,
    supplier_name VARCHAR(100) NOT NULL,
    contact_person VARCHAR(100)
);

-- Adding a primary key to an existing table
ALTER TABLE suppliers
ADD PRIMARY KEY (supplier_id);

-- SECTION 4: Composite Primary Keys
-- Creating a table with a composite primary key (multiple columns)
CREATE TABLE enrollments (
    student_id INT,
    course_id INT,
    enrollment_date DATE NOT NULL,
    grade VARCHAR(2),
    PRIMARY KEY (student_id, course_id)
);

-- Insert records with unique combinations of the composite key
INSERT INTO enrollments (student_id, course_id, enrollment_date, grade)
VALUES 
(1, 101, '2023-01-15', 'A'),
(1, 102, '2023-01-15', 'B+'),  -- Same student, different course - OK
(2, 101, '2023-01-16', 'A-'),  -- Different student, same course - OK
(3, 103, '2023-01-17', 'B');

-- This will fail - duplicate composite key (student_id + course_id)
INSERT INTO enrollments (student_id, course_id, enrollment_date, grade)
VALUES (1, 101, '2023-02-01', 'C');
-- Error: Duplicate entry '1-101' for key 'PRIMARY'