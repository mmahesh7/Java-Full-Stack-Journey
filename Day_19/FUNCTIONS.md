#  Complete MySQL Functions

---

##  1. Introduction to MySQL Functions

### What are MySQL Functions?

**Definition**: Functions in MySQL are pre-built programs that take input values (parameters), perform specific operations, and return results. They help manipulate and transform data efficiently.

**Categories**:
- **String Functions** → Manipulate text data
- **Numeric Functions** → Perform mathematical operations
- **Date/Time Functions** → Handle date and time values
- **Conditional Functions** → Make decisions based on conditions
- **Aggregate Functions** → Perform calculations on groups of rows

**Basic Syntax Pattern**:
```sql
SELECT FUNCTION_NAME(parameters) FROM table_name;
```

---

##  2. String Functions

### 2.1 CONCAT() - String Concatenation

**Definition**: Combines multiple strings into a single string.

**Syntax**:
```sql
CONCAT(string1, string2, string3, ...)
```

**Use Cases**:
- Creating full names from first and last names
- Building complete addresses
- Generating custom messages or labels

**Sample Code**:
```sql
-- Example 1: Basic concatenation
SELECT CONCAT('Hello', ' ', 'World') AS greeting;
-- Output: "Hello World"

-- Example 2: Real-world scenario with table
SELECT 
    CONCAT(first_name, ' ', last_name) AS full_name,
    CONCAT('Welcome, ', first_name, '!') AS welcome_message
FROM employees;
-- Output: "John Doe", "Welcome, John!"

-- Example 3: Building addresses
SELECT CONCAT(street, ', ', city, ', ', state, ' ', zip_code) AS full_address
FROM addresses;
-- Output: "123 Main St, New York, NY 10001"
```

**Important Notes**:
- Returns NULL if any parameter is NULL
- Use CONCAT_WS() for separator-based concatenation

---

### 2.2 LENGTH() & CHAR_LENGTH() - String Length

**Definition**: 
- `LENGTH()`: Returns number of bytes in a string
- `CHAR_LENGTH()`: Returns number of characters in a string

**Syntax**:
```sql
LENGTH(string)
CHAR_LENGTH(string)
```

**Use Cases**:
- Validating input length (passwords, usernames)
- Database optimization and indexing decisions
- Handling multi-byte character sets

**Sample Code**:
```sql
-- Example 1: Basic length check
SELECT 
    LENGTH('Hello World') AS byte_length,
    CHAR_LENGTH('Hello World') AS char_length;
-- Output: 11, 11

-- Example 2: Multi-byte characters
SELECT 
    LENGTH('café') AS byte_length,
    CHAR_LENGTH('café') AS char_length;
-- Output: 5, 4 (é uses 2 bytes)

-- Example 3: Validation query
SELECT username 
FROM users 
WHERE CHAR_LENGTH(username) BETWEEN 3 AND 20;

-- Example 4: Finding long descriptions
SELECT product_name, description
FROM products
WHERE LENGTH(description) > 500;
```

---

### 2.3 UPPER() & LOWER() - Case Conversion

**Definition**: Converts string to uppercase or lowercase.

**Syntax**:
```sql
UPPER(string)
LOWER(string)
```

**Use Cases**:
- Case-insensitive searches and comparisons
- Data standardization
- Display formatting

**Sample Code**:
```sql
-- Example 1: Basic conversion
SELECT 
    UPPER('hello world') AS uppercase,
    LOWER('HELLO WORLD') AS lowercase;
-- Output: "HELLO WORLD", "hello world"

-- Example 2: Case-insensitive search
SELECT * FROM customers 
WHERE UPPER(customer_name) = UPPER('john doe');

-- Example 3: Data standardization
UPDATE products 
SET product_code = UPPER(product_code);

-- Example 4: Mixed case formatting
SELECT 
    CONCAT(UPPER(LEFT(first_name, 1)), LOWER(SUBSTRING(first_name, 2))) AS formatted_name
FROM employees;
-- Output: "John" from "JOHN" or "john"
```

---

### 2.4 TRIM(), LTRIM(), RTRIM() - Whitespace Removal

**Definition**: Removes whitespace characters from strings.

**Syntax**:
```sql
TRIM([LEADING | TRAILING | BOTH] [characters FROM] string)
LTRIM(string)  -- Left trim
RTRIM(string)  -- Right trim
```

**Use Cases**:
- Cleaning user input data
- Data migration and cleanup
- Preparing data for comparison

**Sample Code**:
```sql
-- Example 1: Basic trimming
SELECT 
    TRIM('  Hello World  ') AS trimmed,
    LTRIM('  Hello World  ') AS left_trimmed,
    RTRIM('  Hello World  ') AS right_trimmed;
-- Output: "Hello World", "Hello World  ", "  Hello World"

-- Example 2: Custom character trimming
SELECT TRIM(BOTH '.' FROM '...Hello World...') AS custom_trim;
-- Output: "Hello World"

-- Example 3: Data cleanup
UPDATE customers 
SET customer_name = TRIM(customer_name),
    email = TRIM(LOWER(email));

-- Example 4: Removing specific characters
SELECT TRIM(LEADING '0' FROM '000123') AS number_clean;
-- Output: "123"
```

---

### 2.5 SUBSTRING() - String Extraction

**Definition**: Extracts a portion of a string starting from a specified position.

**Syntax**:
```sql
SUBSTRING(string, start_position, length)
SUBSTRING(string FROM start_position FOR length)
```

**Use Cases**:
- Extracting parts of codes or IDs
- Data parsing and formatting
- Creating abbreviations

**Sample Code**:
```sql
-- Example 1: Basic substring
SELECT SUBSTRING('Hello World', 1, 5) AS first_part;
-- Output: "Hello"

-- Example 2: Extract year from date string
SELECT SUBSTRING('2024-03-15', 1, 4) AS year;
-- Output: "2024"

-- Example 3: Get file extension
SELECT 
    filename,
    SUBSTRING(filename, LENGTH(filename) - 2) AS extension
FROM files;

-- Example 4: Create initials
SELECT 
    CONCAT(
        SUBSTRING(first_name, 1, 1), 
        '.', 
        SUBSTRING(last_name, 1, 1), 
        '.'
    ) AS initials
FROM employees;
-- Output: "J.D." from "John Doe"
```

---

### 2.6 LOCATE() - String Search

**Definition**: Finds the position of a substring within a string.

**Syntax**:
```sql
LOCATE(substring, string)
```

**Use Cases**:
- Validating email formats
- Parsing structured data
- Finding specific patterns

**Sample Code**:
```sql
-- Example : Basic position finding
SELECT LOCATE('@', 'user@example.com') AS at_position;
-- Output: 5
```

---

### 2.7 REPLACE() - String Replacement

**Definition**: Replaces all occurrences of a substring with another string.

**Syntax**:
```sql
REPLACE(string, search_string, replacement_string)
```

**Use Cases**:
- Data cleaning and standardization
- Format conversion
- Updating legacy data

**Sample Code**:
```sql
-- Example : Basic replacement
SELECT REPLACE('Hello World', 'World', 'MySQL') AS replaced;
-- Output: "Hello MySQL"

```

---

### 2.8 LEFT() & RIGHT() - String Extraction

**Definition**: Extracts specified number of characters from left or right side.

**Syntax**:
```sql
LEFT(string, number_of_characters)
RIGHT(string, number_of_characters)
```

**Use Cases**:
- Extracting prefixes or suffixes
- Getting area codes from phone numbers
- Creating abbreviations

**Sample Code**:
```sql
-- Example 1: Basic extraction
SELECT 
    LEFT('Database', 4) AS prefix,
    RIGHT('Database', 4) AS suffix;
-- Output: "Data", "base"



-- Example 2: Get file name without extension
SELECT 
    filename,
    LEFT(filename, LENGTH(filename) - 4) AS name_only
FROM files 
WHERE RIGHT(filename, 4) = '.txt';

-- Example 3: Create short codes
SELECT 
    company_name,
    UPPER(LEFT(company_name, 3)) AS company_code
FROM companies;
```

---

### 2.9 Additional String Functions

#### REVERSE() - String Reversal
```sql
-- Example: Palindrome check
SELECT 
    word,
    REVERSE(word) AS reversed,
    CASE 
        WHEN word = REVERSE(word) THEN 'Palindrome'
        ELSE 'Not Palindrome'
    END AS check_result
FROM words;
```

#### ASCII() & CHAR() - Character Conversion
```sql
-- Example: Character code operations
SELECT 
    ASCII('A') AS ascii_value,  -- Output: 65
    CHAR(65) AS character;      -- Output: 'A'
```

#### SOUNDEX() - Phonetic Matching
```sql
-- Example: Find similar sounding names
SELECT * FROM customers 
WHERE SOUNDEX(last_name) = SOUNDEX('Smith');
-- Matches: Smith, Smyth, etc.
```

---

## 3. Numeric Functions 

### 3.1 ABS() - Absolute Value

**Definition**: Returns the absolute (non-negative) value of a number.

**Syntax**:
```sql
ABS(number)
```

**Use Cases**:
- Calculating distances and differences
- Financial calculations (avoiding negative balances display)
- Mathematical operations requiring positive values

**Sample Code**:
```sql
-- Example 1: Basic absolute value
SELECT ABS(-25) AS positive_value;
-- Output: 25


---

### 3.2 CEIL() & FLOOR() - Rounding Functions

**Definition**: 
- `CEIL()`: Rounds up to nearest integer
- `FLOOR()`: Rounds down to nearest integer

**Syntax**:
```sql
CEIL(number)    -- or CEILING(number)
FLOOR(number)
```

**Use Cases**:
- Inventory calculations (full units only)
- Page numbering and pagination
- Resource allocation

**Sample Code**:
```sql
-- Example 1: Basic rounding
SELECT 
    CEIL(4.2) AS rounded_up,    -- Output: 5
    FLOOR(4.8) AS rounded_down; -- Output: 4

-- Example 2: Calculate required packages
SELECT 
    product_id,
    quantity_needed,
    package_size,
    CEIL(quantity_needed / package_size) AS packages_required
FROM inventory_needs;

-- Example 3: Warehouse capacity
SELECT 
    warehouse_id,
    FLOOR(available_space / item_size) AS max_items_fit
FROM warehouse_capacity;
```

---

### 3.3 ROUND() - Precision Rounding

**Definition**: Rounds a number to specified decimal places.

**Syntax**:
```sql
ROUND(number, decimal_places)
```

**Use Cases**:
- Financial calculations and currency formatting
- Statistical reporting
- Display formatting for user interfaces

**Sample Code**:
```sql
-- Example 1: Basic rounding
SELECT 
    ROUND(15.678, 2) AS two_decimals,    -- Output: 15.68
    ROUND(15.678, 0) AS no_decimals,     -- Output: 16
    ROUND(15.678, -1) AS tens_place;     -- Output: 20

-- Example 2: Grade calculations
SELECT 
    student_id,
    ROUND((exam1 + exam2 + exam3) / 3, 1) AS final_grade
FROM student_scores;
```

---

### 3.4 MOD() - Modulus Operation

**Definition**: Returns the remainder after division.

**Syntax**:
```sql
MOD(dividend, divisor)
-- Alternative: dividend % divisor
```

**Use Cases**:
- Determining even/odd numbers
- Cycling through values
- Data partitioning and sharding

**Sample Code**:
```sql
-- Example 1: Check even/odd numbers
SELECT 
    number,
    CASE 
        WHEN MOD(number, 2) = 0 THEN 'Even'
        ELSE 'Odd'
    END AS number_type
FROM number_list;
```

---

### 3.5 POWER() & SQRT() - Exponential Functions

**Definition**: 
- `POWER()`: Raises a number to specified power
- `SQRT()`: Returns square root of a number

**Syntax**:
```sql
POWER(base, exponent)
SQRT(number)
```

**Use Cases**:
- Mathematical calculations
- Statistical formulas
- Geometric calculations

**Sample Code**:
```sql
-- Example 1: Basic exponential operations
SELECT 
    POWER(2, 3) AS two_cubed,     -- Output: 8
    SQRT(25) AS square_root;      -- Output: 5

```

---

### 3.6 Additional Numeric Functions

#### SIGN() - Number Sign
```sql
-- Example: Determine if values are positive, negative, or zero
SELECT 
    value,
    SIGN(value) AS sign_indicator  -- Returns -1, 0, or 1
FROM financial_data;
```

#### TRUNCATE() - Value Truncation
```sql
-- Example: Remove decimal places without rounding
SELECT 
    price,
    TRUNCATE(price, 2) AS truncated_price
FROM products;
```

#### GREATEST() & LEAST() - Value Comparison
```sql
-- Example: Find highest and lowest values
SELECT 
    GREATEST(score1, score2, score3) AS highest_score,
    LEAST(score1, score2, score3) AS lowest_score
FROM test_results;
```

---

## 4. Date and Time Functions

### 4.1 NOW(), CURDATE(), CURTIME() - Current Date/Time

**Definition**: Returns current date and time information.

**Syntax**:
```sql
NOW()        -- Current date and time
CURDATE()    -- Current date only
CURTIME()    -- Current time only
```

**Use Cases**:
- Timestamping records
- Calculating ages and durations
- Scheduling and time-based queries

**Sample Code**:
```sql
-- Example 1: Basic current date/time
SELECT 
    NOW() AS current_datetime,      -- 2024-03-15 14:30:25
    CURDATE() AS current_date,      -- 2024-03-15
    CURTIME() AS current_time;      -- 14:30:25

-- Example 2: Insert with timestamp
INSERT INTO user_logs (user_id, action, created_at)
VALUES (1, 'login', NOW());

-- Example 3: Find recent records
SELECT * FROM orders 
WHERE order_date >= CURDATE() - INTERVAL 30 DAY;

-- Example 4: Calculate business hours
SELECT 
    employee_id,
    CASE 
        WHEN CURTIME() BETWEEN '09:00:00' AND '17:00:00' THEN 'Business Hours'
        ELSE 'After Hours'
    END AS work_status
FROM employees;
```

---

### 4.2 DATE_ADD() & DATE_SUB() - Date Arithmetic

**Definition**: Adds or subtracts time intervals from dates.

**Syntax**:
```sql
DATE_ADD(date, INTERVAL value unit)
DATE_SUB(date, INTERVAL value unit)
```

**Use Cases**:
- Due date calculations
- Subscription expiry dates
- Historical data analysis

**Sample Code**:
```sql
-- Example 1: Basic date arithmetic
SELECT 
    CURDATE() AS today,
    DATE_ADD(CURDATE(), INTERVAL 30 DAY) AS thirty_days_later,
    DATE_SUB(CURDATE(), INTERVAL 1 YEAR) AS one_year_ago;

-- Example 2: Age calculation
SELECT 
    customer_id,
    birth_date,
    TIMESTAMPDIFF(YEAR, birth_date, CURDATE()) AS age
FROM customers;
```

---

### 4.3 DATEDIFF() & TIMESTAMPDIFF() - Date Differences

**Definition**: Calculates differences between dates.

**Syntax**:
```sql
DATEDIFF(date1, date2)                    -- Returns days difference
TIMESTAMPDIFF(unit, date1, date2)         -- Returns difference in specified unit
```

**Use Cases**:
- Calculate durations
- Performance metrics
- Age and tenure calculations

**Sample Code**:
```sql
-- Example 1: Basic date differences
SELECT 
    DATEDIFF('2024-03-15', '2024-03-01') AS days_difference,  -- 14
    TIMESTAMPDIFF(MONTH, '2024-01-01', '2024-03-15') AS months_difference;  -- 2

```

---

### 4.4 DATE_FORMAT() - Date Formatting

**Definition**: Formats dates according to specified patterns.

**Syntax**:
```sql
DATE_FORMAT(date, format_string)
```

**Use Cases**:
- User-friendly date displays
- Report formatting
- Internationalization

**Sample Code**:
```sql
-- Example 1: Various date formats
SELECT 
    DATE_FORMAT(NOW(), '%Y-%m-%d') AS iso_date,           -- 2024-03-15
    DATE_FORMAT(NOW(), '%M %d, %Y') AS readable_date,     -- March 15, 2024
    DATE_FORMAT(NOW(), '%W, %M %e, %Y') AS full_date;     -- Friday, March 15, 2024

-- Example 2: Monthly reports
SELECT 
    DATE_FORMAT(order_date, '%Y-%m') AS month_year,
    COUNT(*) AS total_orders
FROM orders
GROUP BY DATE_FORMAT(order_date, '%Y-%m');

-- Example 3: Time formatting
SELECT 
    employee_id,
    DATE_FORMAT(clock_in, '%h:%i %p') AS formatted_time
FROM time_clock;

```

---

### 4.5 EXTRACT() - Date Component Extraction

**Definition**: Extracts specific parts from date/time values.

**Syntax**:
```sql
EXTRACT(unit FROM date)
```

**Use Cases**:
- Grouping by time periods
- Conditional logic based on date parts
- Statistical analysis by time components

**Sample Code**:
```sql
-- Example 1: Extract date components
SELECT 
    order_date,
    EXTRACT(YEAR FROM order_date) AS order_year,
    EXTRACT(MONTH FROM order_date) AS order_month,
    EXTRACT(DAY FROM order_date) AS order_day
FROM orders;

-- Example 2: Quarterly analysis
SELECT 
    CASE 
        WHEN EXTRACT(MONTH FROM order_date) IN (1,2,3) THEN 'Q1'
        WHEN EXTRACT(MONTH FROM order_date) IN (4,5,6) THEN 'Q2'
        WHEN EXTRACT(MONTH FROM order_date) IN (7,8,9) THEN 'Q3'
        ELSE 'Q4'
    END AS quarter,
    COUNT(*) AS orders_count
FROM orders
GROUP BY quarter;

-- Example 3: Weekend identification
SELECT 
    order_date,
    CASE 
        WHEN EXTRACT(DOW FROM order_date) IN (0,6) THEN 'Weekend'
        ELSE 'Weekday'
    END AS day_type
FROM orders;
```

---

## 5. Conditional Functions 

### 5.1 IF() - Simple Conditional

**Definition**: Returns one of two values based on a condition.

**Syntax**:
```sql
IF(condition, value_if_true, value_if_false)
```

**Use Cases**:
- Simple data transformations
- Status indicators
- Binary classifications

**Sample Code**:
```sql
-- Example 1: Basic conditional
SELECT 
    product_id,
    stock_quantity,
    IF(stock_quantity > 0, 'In Stock', 'Out of Stock') AS availability
FROM products;

```

---

### 5.2 CASE - Complex Conditional Logic

**Definition**: Evaluates multiple conditions and returns corresponding values.

**Syntax**:
```sql
CASE 
    WHEN condition1 THEN result1
    WHEN condition2 THEN result2
    ELSE default_result
END
```

**Use Cases**:
- Complex business logic
- Multi-tier categorization
- Grade calculations

**Sample Code**:
```sql
-- Example 1: Grade assignment
SELECT 
    student_id,
    score,
    CASE 
        WHEN score >= 90 THEN 'A'
        WHEN score >= 80 THEN 'B'
        WHEN score >= 70 THEN 'C'
        WHEN score >= 60 THEN 'D'
        ELSE 'F'
    END AS letter_grade
FROM student_scores;

-- Example 2:  Seasonal pricing
SELECT 
    product_id,
    base_price,
    CASE 
        WHEN MONTH(CURDATE()) IN (12,1,2) THEN base_price * 1.2  -- Winter
        WHEN MONTH(CURDATE()) IN (6,7,8) THEN base_price * 0.9   -- Summer
        ELSE base_price
    END AS seasonal_price
FROM products;
```

---

## 6. Quick Reference Summary

### String Functions Quick Reference
| Function | Purpose | Example |
|----------|---------|---------|
| `CONCAT()` | Join strings | `CONCAT('Hello', ' World')` |
| `LENGTH()` | String length | `LENGTH('Hello')` = 5 |
| `UPPER()/LOWER()` | Case conversion | `UPPER('hello')` = 'HELLO' |
| `TRIM()` | Remove spaces | `TRIM('  Hello  ')` = 'Hello' |
| `SUBSTRING()` | Extract portion | `SUBSTRING('Hello', 1, 3)` = 'Hel' |
| `REPLACE()` | Replace text | `REPLACE('Hi John', 'John', 'Jane')` |

### Numeric Functions Quick Reference
| Function | Purpose | Example |
|----------|---------|---------|
| `ABS()` | Absolute value | `ABS(-5)` = 5 |
| `ROUND()` | Round to decimals | `ROUND(15.678, 2)` = 15.68 |
| `CEIL()` | Round up | `CEIL(4.2)` = 5 |
| `FLOOR()` | Round down | `FLOOR(4.8)` = 4 |
| `MOD()` | Remainder | `MOD(10, 3)` = 1 |
| `POWER()` | Exponentiation | `POWER(2, 3)` = 8 |

### Date Functions Quick Reference
| Function | Purpose | Example |
|----------|---------|---------|
| `NOW()` | Current datetime | `2024-03-15 14:30:25` |
| `CURDATE()` | Current date | `2024-03-15` |
| `DATE_ADD()` | Add interval | `DATE_ADD(CURDATE(), INTERVAL 1 MONTH)` |
| `DATEDIFF()` | Days between | `DATEDIFF('2024-03-15', '2024-03-01')` = 14 |
| `DATE_FORMAT()` | Format date | `DATE_FORMAT(NOW(), '%M %d, %Y')` |

### Conditional Functions Quick Reference
| Function | Purpose | Example |
|----------|---------|---------|
| `IF()` | Simple condition | `IF(score > 80, 'Pass', 'Fail')` |
| `CASE` | Multiple conditions | `CASE WHEN score > 90 THEN 'A' ... END` |
