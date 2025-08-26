# Database Normalization - Complete Study Notes

## 📚 What is Database Normalization?

**Definition**: Normalization is a process to organize data in a database to reduce redundancy and improve data integrity.

**Purpose**:
- Eliminate redundant data storage
- Reduce data inconsistency
- Improve database structure
- Make updates easier and more efficient

---

## 🚫 Problems with Denormalized Tables

### Example: Bookstore Orders (Before Normalization)

```sql
CREATE TABLE book_orders (
    order_id INT,
    customer_name VARCHAR(100),
    customer_email VARCHAR(100),
    customer_address VARCHAR(255),
    book_isbn VARCHAR(20),
    book_title VARCHAR(200),
    book_author VARCHAR(100),
    book_price DECIMAL(10, 2),
    order_date DATE,
    quantity INT,
    total_price DECIMAL(10, 2)
);
```

### Issues with This Structure:

1. **Data Redundancy**: Customer details repeat for every order
2. **Storage Waste**: Same book information stored multiple times
3. **Update Anomalies**: Need to update multiple rows when customer changes address
4. **Insertion Anomalies**: Cannot add a book without associating it with an order
5. **Deletion Anomalies**: Deleting customer might lose book information

---

## 🔢 First Normal Form (1NF)

### Definition
A table is in 1NF if it meets these requirements:

### Requirements:
1. ✅ **Atomic Values**: Each column contains indivisible values
2. ✅ **Same Data Types**: Each column contains values of the same type
3. ✅ **Unique Rows**: Each row is unique (ensured by primary key)
4. ✅ **No Repeating Groups**: No repeating groups of columns

### Examples:

#### ❌ Violates 1NF (Non-atomic values):
```sql
-- BAD: Multiple phone numbers in one column
customer_phones VARCHAR(255) -- "123-456-7890, 987-654-3210"
```

#### ✅ Follows 1NF:
```sql
-- GOOD: Separate columns or separate table for phones
primary_phone VARCHAR(15)
secondary_phone VARCHAR(15)
```

#### ❌ Violates 1NF (Repeating groups):
```sql
-- BAD: Repeating column groups
phone1 VARCHAR(15)
phone2 VARCHAR(15)  
phone3 VARCHAR(15)
phone_type1 VARCHAR(20)
phone_type2 VARCHAR(20)
phone_type3 VARCHAR(20)
```

#### ✅ Better Approach:
```sql
-- Create separate table for phone numbers
CREATE TABLE customer_phones (
    phone_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    phone_number VARCHAR(15),
    phone_type VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
```

### Converting to 1NF:
```sql
CREATE TABLE book_orders_1nf (
    order_id INT,
    book_isbn VARCHAR(20),
    customer_name VARCHAR(100),
    customer_email VARCHAR(100),
    customer_address VARCHAR(255),
    book_title VARCHAR(200),
    book_author VARCHAR(100),
    book_price DECIMAL(10, 2),
    order_date DATE,
    quantity INT,
    total_price DECIMAL(10, 2),
    PRIMARY KEY (order_id, book_isbn)  -- Composite primary key
);
```

**Key Point**: Composite primary key is needed because one order can have multiple books.

---

## 🔢 Second Normal Form (2NF)

### Definition
A table is in 2NF if:
1. ✅ It's in 1NF
2. ✅ **No Partial Dependencies**: All non-key attributes must depend on the ENTIRE primary key, not just part of it

### Understanding Partial Dependencies:

In our 1NF table with composite key `(order_id, book_isbn)`:

#### Partial Dependencies Found:
- `customer_name` depends only on `order_id` (not on `book_isbn`)
- `customer_email` depends only on `order_id` (not on `book_isbn`)
- `customer_address` depends only on `order_id` (not on `book_isbn`)
- `order_date` depends only on `order_id` (not on `book_isbn`)
- `book_title` depends only on `book_isbn` (not on `order_id`)
- `book_author` depends only on `book_isbn` (not on `order_id`)
- `book_price` depends only on `book_isbn` (not on `order_id`)

#### Full Dependencies (These are OK):
- `quantity` depends on both `order_id` AND `book_isbn`
- `total_price` depends on both `order_id` AND `book_isbn`

### Converting to 2NF:

Break the table into three tables:

```sql
-- Table 1: Orders (attributes depending only on order_id)
CREATE TABLE orders_2nf (
    order_id INT PRIMARY KEY,
    customer_name VARCHAR(100),
    customer_email VARCHAR(100),
    customer_address VARCHAR(255),
    order_date DATE
);

-- Table 2: Books (attributes depending only on book_isbn)
CREATE TABLE books_2nf (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(200),
    author VARCHAR(100),
    price DECIMAL(10, 2)
);

-- Table 3: Order Items (attributes depending on both keys)
CREATE TABLE order_items_2nf (
    order_id INT,
    book_isbn VARCHAR(20),
    quantity INT,
    total_price DECIMAL(10, 2),
    PRIMARY KEY (order_id, book_isbn),
    FOREIGN KEY (order_id) REFERENCES orders_2nf(order_id),
    FOREIGN KEY (book_isbn) REFERENCES books_2nf(isbn)
);
```

### Sample Data:
```sql
-- Orders data
INSERT INTO orders_2nf VALUES
(1, 'John Smith', 'john@example.com', '123 Main St', '2023-01-15'),
(2, 'Mary Johnson', 'mary@example.com', '456 Oak Ave', '2023-01-20');

-- Books data
INSERT INTO books_2nf VALUES
('978-0141439518', 'Pride and Prejudice', 'Jane Austen', 9.99),
('978-0451524935', '1984', 'George Orwell', 12.99);

-- Order items data
INSERT INTO order_items_2nf VALUES
(1, '978-0141439518', 1, 9.99),
(1, '978-0451524935', 2, 25.98);
```

---

## 🔢 Third Normal Form (3NF)

### Definition
A table is in 3NF if:
1. ✅ It's in 2NF
2. ✅ **No Transitive Dependencies**: Non-key attributes cannot depend on other non-key attributes

### Understanding Transitive Dependencies:

In our `orders_2nf` table:
- `customer_email` and `customer_address` depend on `customer_name`
- `customer_name` depends on `order_id`
- Therefore: `customer_email` and `customer_address` **transitively depend** on `order_id` through `customer_name`

This is a **transitive dependency** and violates 3NF.

### Converting to 3NF:

```sql
-- Table 1: Customers (separate customer information)
CREATE TABLE customers_3nf (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    email VARCHAR(100),
    address VARCHAR(255)
);

-- Table 2: Orders (now references customer_id instead of storing customer details)
CREATE TABLE orders_3nf (
    order_id INT PRIMARY KEY,
    customer_id INT,
    order_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers_3nf(customer_id)
);

-- Table 3: Books (no changes needed - already in 3NF)
CREATE TABLE books_3nf (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(200),
    author VARCHAR(100),
    price DECIMAL(10, 2)
);

-- Table 4: Order Items (removed total_price as it's derived)
CREATE TABLE order_items_3nf (
    order_id INT,
    book_isbn VARCHAR(20),
    quantity INT,
    -- total_price removed (can be calculated: quantity * book_price)
    PRIMARY KEY (order_id, book_isbn),
    FOREIGN KEY (order_id) REFERENCES orders_3nf(order_id),
    FOREIGN KEY (book_isbn) REFERENCES books_3nf(isbn)
);
```

### Why Remove total_price?
`total_price` can be calculated from `quantity * book_price`, making it a derived/computed value. Storing it creates a transitive dependency.

---

## 📊 Complete 3NF Example with Data

```sql
-- Create database
CREATE DATABASE bookstore_3nf;
USE bookstore_3nf;

-- 1. Customers table
CREATE TABLE customers (
    customer_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    address VARCHAR(255)
);

-- 2. Books table  
CREATE TABLE books (
    isbn VARCHAR(20) PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    author VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2) NOT NULL
);

-- 3. Orders table
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT NOT NULL,
    order_date DATE NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);

-- 4. Order Items table
CREATE TABLE order_items (
    order_id INT,
    book_isbn VARCHAR(20),
    quantity INT NOT NULL DEFAULT 1,
    PRIMARY KEY (order_id, book_isbn),
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (book_isbn) REFERENCES books(isbn) ON DELETE CASCADE
);

-- Insert sample data
INSERT INTO customers (name, email, address) VALUES
('John Smith', 'john@example.com', '123 Main St, Anytown'),
('Mary Johnson', 'mary@example.com', '456 Oak Ave, Somewhere'),
('Robert Brown', 'robert@example.com', '789 Pine Rd, Nowhere');

INSERT INTO books VALUES
('978-0141439518', 'Pride and Prejudice', 'Jane Austen', 9.99),
('978-0451524935', '1984', 'George Orwell', 12.99),
('978-0061120084', 'To Kill a Mockingbird', 'Harper Lee', 14.99);

INSERT INTO orders (customer_id, order_date) VALUES
(1, '2023-01-15'),
(2, '2023-01-20'),
(3, '2023-01-25');

INSERT INTO order_items VALUES
(1, '978-0141439518', 1),
(1, '978-0451524935', 2),
(2, '978-0061120084', 1),
(3, '978-0141439518', 1);
```

---

## 🔍 Querying Normalized Data

### Get Order Details with Customer and Book Information:
```sql
SELECT 
    o.order_id,
    c.name AS customer_name,
    c.email AS customer_email,
    b.title AS book_title,
    b.author AS book_author,
    b.price AS unit_price,
    oi.quantity,
    (b.price * oi.quantity) AS total_price,
    o.order_date
FROM orders o
JOIN customers c ON o.customer_id = c.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN books b ON oi.book_isbn = b.isbn
ORDER BY o.order_id, b.title;
```

### Get Customer's Total Spending:
```sql
SELECT 
    c.name,
    c.email,
    COUNT(DISTINCT o.order_id) AS total_orders,
    SUM(b.price * oi.quantity) AS total_spent
FROM customers c
JOIN orders o ON c.customer_id = o.customer_id
JOIN order_items oi ON o.order_id = oi.order_id
JOIN books b ON oi.book_isbn = b.isbn
GROUP BY c.customer_id, c.name, c.email
ORDER BY total_spent DESC;
```

---

## ⚖️ Normalization Trade-offs

### ✅ Benefits:
- **Eliminates redundancy**: No duplicate data
- **Improves consistency**: Single source of truth
- **Easier updates**: Change data in one place
- **Better data integrity**: Reduced chance of inconsistencies
- **Storage efficiency**: Less disk space used

### ❌ Drawbacks:
- **Complex queries**: Need JOINs to retrieve related data
- **Performance impact**: JOINs can be slower than single table queries
- **Increased complexity**: More tables to manage

### 🎯 When to Denormalize:
Sometimes it's acceptable to denormalize for performance:
```sql
-- Example: Keep calculated total_price for faster reporting
CREATE TABLE order_summary (
    order_id INT PRIMARY KEY,
    customer_id INT,
    total_amount DECIMAL(10, 2),  -- Denormalized for performance
    order_date DATE,
    FOREIGN KEY (customer_id) REFERENCES customers(customer_id)
);
```

---

## 🎓 Key Takeaways

1. **1NF**: Atomic values, unique rows, no repeating groups
2. **2NF**: 1NF + no partial dependencies on composite keys
3. **3NF**: 2NF + no transitive dependencies
4. **Most databases aim for 3NF** as it provides the best balance
5. **Higher normal forms exist** (BCNF, 4NF, 5NF) but 3NF is usually sufficient
6. **Consider performance** - sometimes controlled denormalization is acceptable

---

## 🧠 Memory Tips

**1NF**: "**A**tomic values, **U**nique rows" (AU)
**2NF**: "**P**artial dependencies are **P**rohibited" (PP)  
**3NF**: "**T**ransitive dependencies are **T**aboo" (TT)

**Rule of thumb**: Every non-key column should depend on "the key, the whole key, and nothing but the key!"

---

## 🔧 Practice Exercises

1. **Identify violations**: Look for redundancy in existing tables
2. **Create normalized versions**: Practice breaking down denormalized tables
3. **Write JOIN queries**: Practice retrieving data from normalized tables
4. **Design from scratch**: Create 3NF designs for new requirements

Remember: Normalization is about organizing data logically, not just following rules mechanically!