# MySQL WHERE Clause



---

## 1. Introduction to WHERE Clause 

The **WHERE clause** is used to filter records in SQL queries. It allows you to specify conditions that must be met for rows to be included in the result set.

### Key Points:
- Filters data based on specified conditions
- Only returns rows that satisfy the condition(s)
- Can be used with SELECT, UPDATE, DELETE statements
- Conditions evaluate to TRUE, FALSE, or UNKNOWN

---

## 2. Basic Syntax

```sql
SELECT column1, column2, ...
FROM table_name
WHERE condition;
```

### Example Database Setup:
```sql
CREATE DATABASE bookstore;
USE bookstore;

CREATE TABLE books (
    book_id INT PRIMARY KEY,
    title VARCHAR(100),
    author VARCHAR(50),
    price DECIMAL(10,2),
    publication_date DATE,
    category VARCHAR(30),
    in_stock INT
);

INSERT INTO books VALUES
(1, 'The MySQL Guide', 'John Smith', 29.99, '2023-01-15', 'Technology', 50),
(2, 'Data Science Basics', 'Sarah Johnson', 34.99, '2023-03-20', 'Technology', 30),
(3, 'Mystery at Midnight', 'Michael Brown', 19.99, '2023-02-10', 'Mystery', 100),
(4, 'Cooking Essentials', 'Lisa Anderson', 24.99, '2023-04-05', 'Cooking', 75);
```

---

## 3. Comparison Operators 

### Equality Check
```sql
-- Find books in Technology category
SELECT * FROM books WHERE category = 'Technology';
```

### Numeric Comparisons
| Operator | Description | Example |
|----------|-------------|---------|
| `=` | Equal to | `price = 29.99` |
| `!=` or `<>` | Not equal to | `price != 30.00` |
| `<` | Less than | `price < 30.00` |
| `<=` | Less than or equal | `price <= 30.00` |
| `>` | Greater than | `price > 25.00` |
| `>=` | Greater than or equal | `price >= 25.00` |

### Examples:
```sql
-- Books priced less than $30
SELECT title, price FROM books WHERE price < 30.00;

-- Books published on or after March 1, 2023
SELECT title, publication_date FROM books 
WHERE publication_date >= '2023-03-01';
```

---

## 4. Logical Operators

### AND Operator
Both conditions must be TRUE.

```sql
-- Technology books under $30
SELECT * FROM books 
WHERE category = 'Technology' AND price < 30;
```

### OR Operator
Either condition can be TRUE.

```sql
-- Technology books OR books under $30
SELECT * FROM books 
WHERE category = 'Technology' OR price < 30;
```

### NOT Operator
Negates a condition.

```sql
-- Books that are NOT in Technology category
SELECT * FROM books 
WHERE NOT category = 'Technology';

-- Alternative syntax
SELECT * FROM books 
WHERE category != 'Technology';
```

### Combining Multiple Conditions
Use parentheses to group conditions:

```sql
-- (Technology OR Mystery) AND price < $25
SELECT * FROM books 
WHERE (category = 'Technology' OR category = 'Mystery') 
AND price < 25;
```

---

## 5. Handling NULL Values

### Important Notes:
- **Cannot** use `=` or `!=` with NULL values
- Comparisons with NULL return UNKNOWN (not TRUE/FALSE)
- WHERE clause only returns rows where condition is TRUE

### IS NULL
Find rows with NULL values:

```sql
-- Find books with no author specified
SELECT * FROM books WHERE author IS NULL;
```

### IS NOT NULL
Find rows without NULL values:

```sql
-- Find books with author specified
SELECT * FROM books WHERE author IS NOT NULL;
```

### Why `= NULL` doesn't work:
```sql
-- ❌ This won't return any results
SELECT * FROM books WHERE author = NULL;

-- ✅ Correct way
SELECT * FROM books WHERE author IS NULL;
```

---

## 6. Pattern Matching 

### LIKE Operator
Used for pattern matching with wildcards.

#### Wildcards:
- `%` - Matches zero or more characters
- `_` - Matches exactly one character

### Examples:

#### Contains Pattern
```sql
-- Books with 'SQL' anywhere in title
SELECT * FROM books WHERE title LIKE '%SQL%';
```

#### Starts With Pattern
```sql
-- Books starting with 'The'
SELECT * FROM books WHERE title LIKE 'The%';
```

#### Single Character Wildcard
```sql
-- Authors with 4-letter first name ending in 'ohn'
SELECT * FROM books WHERE author LIKE '_ohn%';
```

### Case Sensitivity
- **Default**: Case-insensitive
- **Case-sensitive**: Use `BINARY` keyword

```sql
-- Case-insensitive (default)
SELECT * FROM books WHERE title LIKE '%sql%';

-- Case-sensitive
SELECT * FROM books WHERE title LIKE BINARY '%SQL%';
```

---

## 7. Range Operators 

### BETWEEN Operator
Check if value falls within a range (inclusive).

```sql
-- Books priced between $20 and $30
SELECT * FROM books WHERE price BETWEEN 20 AND 30;
```

### IN Operator
Check if value matches any in a list.

```sql
-- Books in specific categories
SELECT * FROM books 
WHERE category IN ('Technology', 'Mystery', 'Science');
```

### NOT IN Operator
```sql
-- Books NOT in specified categories
SELECT * FROM books 
WHERE category NOT IN ('Cooking', 'Fiction');
```

### Combining Range Operators
```sql
-- Complex condition example
SELECT * FROM books 
WHERE price BETWEEN 20.00 AND 40.00 
AND publication_date >= '2023-01-01';
```

---

## 8. Subqueries with WHERE 

### Definition
A subquery is a query nested inside another query.

### Examples:

#### Scalar Subquery
```sql
-- Books priced above average
SELECT * FROM books 
WHERE price > (
    SELECT AVG(price) FROM books
);
```

#### Subquery with IN
```sql
-- Books in categories that have more than 20 in stock
SELECT * FROM books 
WHERE category IN (
    SELECT category FROM books WHERE in_stock > 20
);
```

---

## 9. Practice Examples 

### Example 1: Year Function with Subquery
```sql
-- Books published in 2023 costing less than average
SELECT title, price, publication_date
FROM books
WHERE YEAR(publication_date) = 2023
AND price < (SELECT AVG(price) FROM books);
```

### Example 2: Multiple Pattern Matching
```sql
-- Technology books with "data" in title and high stock
SELECT title, category, in_stock
FROM books
WHERE category = 'Technology'
AND title LIKE '%data%'
AND in_stock > 50;
```

### Example 3: Complex OR Conditions
```sql
-- Expensive tech books OR cheap mystery books
SELECT title, category, price
FROM books
WHERE (category = 'Technology' AND price > 30.00)
OR (category = 'Mystery' AND price < 20.00);
```

### Example 4: Multiple LIKE with Date Comparison
```sql
-- Authors with specific patterns, published after March 2023
SELECT title, author, publication_date
FROM books
WHERE (author LIKE '%son%' OR author LIKE '%th%')
AND publication_date > '2023-03-31';
```

---

## Key Takeaways

1. **WHERE clause filters data** - only returns rows meeting conditions
2. **Use proper operators** - `=`, `<`, `>`, etc. for comparisons
3. **Combine conditions** - use AND, OR, NOT with parentheses
4. **Handle NULL carefully** - use IS NULL/IS NOT NULL, never = NULL
5. **Pattern matching** - use LIKE with % and _ wildcards
6. **Range queries** - use BETWEEN and IN for multiple values
7. **Subqueries** - nest queries for dynamic filtering
8. **Case sensitivity** - use BINARY for case-sensitive matching

---
