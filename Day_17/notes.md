# DAY 17 - MySQL Data Types, Database-Level CRUD Operations & Table Commands

## 1) Big Picture of Data Types

Most-used categories:

* **Numeric**: integers, floating-point, fixed-point (DECIMAL)
* **String/Text**: `CHAR`, `VARCHAR`, `TEXT` family, `ENUM`, `SET`
* **Date & Time**: `DATE`, `DATETIME`, `TIMESTAMP`, `TIME`, `YEAR`
* **Binary**: `BINARY`, `VARBINARY`, `BLOB` family


---

## 2) Numeric Types

### 2.1 Integers (exact, no decimals)

| Type        | Storage | Signed Range (approx) | Unsigned Range (approx) | Notes                     |
| ----------- | ------- | --------------------- | ----------------------- | ------------------------- |
| `TINYINT`   | 1 byte  | −128 to 127           | 0 to 255                | Very small counters/flags |
| `SMALLINT`  | 2 bytes | −32k to 32k           | 0 to 65k                | Small IDs                 |
| `MEDIUMINT` | 3 bytes | −8M to 8M             | 0 to 16M                | Medium ranges             |
| `INT`       | 4 bytes | −2.1B to 2.1B         | 0 to 4.2B               | Default for many IDs      |
| `BIGINT`    | 8 bytes | \~−9e18 to 9e18       | 0 to \~1.8e19           | Very large counts         |

* **Signed vs Unsigned**: if you don’t need negatives, `UNSIGNED` roughly doubles the positive range.
* **Phone numbers**: **store as `VARCHAR`** (keep `+91`, leading zeros, formatting); don’t use numeric types.

### 2.2 Floating-Point (approximate)

| Type     | Storage | Precision (rough) | Range (magnitude)   | Use for                  |
| -------- | ------- | ----------------- | ------------------- | ------------------------ |
| `FLOAT`  | 4 bytes | \~7 digits        | \~1e−38 to \~1e38   | Scientific/approx values |
| `DOUBLE` | 8 bytes | \~15–16 digits    | \~1e−308 to \~1e308 | Higher-precision approx  |

* Floating types **round**; **do not** use for money.
* Scientific notation like `1.23e+6` is normal.

### 2.3 Fixed-Point (exact)

**`DECIMAL(p, s)`**

* `p` = total digits, `s` = digits after decimal.
* Max: **`p ≤ 65`**, **`s ≤ 30`**.
* Default if omitted: `DECIMAL(10,0)`.
* **Exact** arithmetic, supports rounding to `s` digits when inserting.
* **Use for money, prices** (e.g., `DECIMAL(9,2)`).

**Examples**

* `DECIMAL(5,4)` stores up to 5 total digits with 4 after decimal (e.g., `1.2345`).
* Inserting `1.23456` into `DECIMAL(5,4)` → stored as `1.2346` (rounded).
* If total digits exceed `p`, insert **error** (not just a warning).

---

## 3) String & Text Types

### 3.1 `CHAR` vs `VARCHAR`

| Type         | Length Spec | Storage                                      | Max Length                                | Use When                                                    |
| ------------ | ----------- | -------------------------------------------- | ----------------------------------------- | ----------------------------------------------------------- |
| `CHAR(n)`    | required    | **Fixed** `n` bytes (pads right with spaces) | up to 255 chars                           | Fixed-length codes (e.g., state codes, fixed product codes) |
| `VARCHAR(n)` | required    | **Variable** (actual length + 1 or 2 bytes)  | up to 65,535 **bytes** (row limits apply) | Most names, addresses, variable strings                     |

* **Prefer `VARCHAR`** for most text; `CHAR` wastes space if data is shorter.
* `VARCHAR` limit is in **bytes**, not chars (multi-byte UTF-8 matters).

### 3.2 `TEXT` family (stored off-row; pointer kept in the row)

| Type         | Max Size     | Notes                                       |
| ------------ | ------------ | ------------------------------------------- |
| `TINYTEXT`   | 255 bytes    | Small text; rarely needed if `VARCHAR` fits |
| `TEXT`       | 65,535 bytes | Common large text                           |
| `MEDIUMTEXT` | 16 MB        | Bigger articles/blobs of text               |
| `LONGTEXT`   | 4 GB         | Very large text                             |

Key differences vs `VARCHAR`:

* `TEXT` columns are **stored separately** from the row → can be **slower** to query.
* **Indexing**: `VARCHAR` can be fully indexed (subject to length limits); `TEXT` typically requires **prefix indexes** (e.g., `INDEX(col(200))`).

### 3.3 `ENUM` (single choice from a predefined list)

* Store constrained categorical values, e.g., `ENUM('active','inactive','pending')`, `ENUM('male','female')`.
* MySQL stores an internal **integer index** for the chosen value (efficient storage, typically 1–2 bytes).
* Useful for **small, stable** sets of labels.

### 3.4 `SET` (zero or more choices from a predefined list)

* Example: `SET('red','green','blue','black','white')` to tag available colors.
* A single row can store **multiple** selected members (including none).

---

## 4) Date & Time Types

| Type        | Stores                  | Example               | Notes                                                                              |
| ----------- | ----------------------- | --------------------- | ---------------------------------------------------------------------------------- |
| `DATE`      | Date only               | `2025-01-16`          | Range roughly `1000-01-01` to `9999-12-31`                                         |
| `DATETIME`  | Date + time (no TZ)     | `2025-01-16 14:00:00` | Stores exactly what you insert (no time zone conversion)                           |
| `TIMESTAMP` | Date + time (UTC-based) | `2025-01-16 14:00:00` | **Stored in UTC**; on read/write it’s converted using the client/session time zone |
| `TIME`      | Time of day / interval  | `14:00:00`            | Hours, minutes, seconds                                                            |
| `YEAR`      | Year                    | `2025`                | Can insert as number or quoted string                                              |

**`DATETIME` vs `TIMESTAMP`**

* Use **`TIMESTAMP`** when you want time zone–aware behavior (store in UTC; auto-convert for clients).
* Use **`DATETIME`** for absolute timestamps that should not change with time zone.

**Why use date/time types (not strings)?**

* You get date math, comparisons, validation, and for `TIMESTAMP`, time zone handling.

---

## 5) Binary Types

Two groups: raw binary strings and large binary objects.

### 5.1 Binary strings

| Type           | Storage                | Max Length   | Behavior                               |
| -------------- | ---------------------- | ------------ | -------------------------------------- |
| `BINARY(n)`    | Fixed-length binary    | up to 255    | Pads with `\0` (null bytes) if shorter |
| `VARBINARY(n)` | Variable-length binary | up to 65,535 | No padding                             |

Use cases:

* Keys, hashes, tokens where **exact bytes** matter (no encoding/rounding issues).
* Example: **SHA-256** output is **32 bytes** → store as `BINARY(32)` (more compact and exact than hex in `VARCHAR(64)`).

### 5.2 BLOB family (binary large objects)

| Type         | Max Size  | Use for                                 |
| ------------ | --------- | --------------------------------------- |
| `TINYBLOB`   | 255 bytes | Tiny files/snippets                     |
| `BLOB`       | 64 KB     | Small files                             |
| `MEDIUMBLOB` | 16 MB     | Medium files                            |
| `LONGBLOB`   | 4 GB      | Very large files (images, videos, PDFs) |

* Store **files** (images, PDFs) as raw bytes.
* App typically sends/reads the bytes; database stores them as-is.

---

## 6) Best Practices & Rules of Thumb

* **Money**: `DECIMAL(precision, scale)` (e.g., `DECIMAL(12,2)`), **not** `FLOAT/DOUBLE`.
* **Phone numbers, IDs with formatting**: `VARCHAR`, not numeric.
* **Choose the smallest integer** type that fits your **max range** (`TINYINT` → `BIGINT`).
* **Prefer `VARCHAR` over `CHAR`** unless the value is truly fixed-length.
* **`TEXT` vs `VARCHAR`**:

  * `VARCHAR` is row-stored and fully indexable (up to practical limits).
  * `TEXT` is off-row; consider only when you need long text; use **prefix indexes**.
* **`ENUM`** for small, stable categorical sets; **`SET`** when multiple tags per row make sense.
* **`TIMESTAMP`** for UTC-aware times; **`DATETIME`** for absolute times that shouldn’t shift.
* **Binary**:

  * Exact cryptographic material → `BINARY/VARBINARY`.
  * Files → `BLOB` family.
* **Rounding**:

  * `FLOAT/DOUBLE` are approximate → expect rounding.
  * `DECIMAL` is exact; will round to `s` digits on insert.
* **Defaults**: `DECIMAL` defaults to `DECIMAL(10,0)` if you omit `(p,s)`.

---

## 7) Quick Examples

### 7.1 Product table (strings, ENUM, SET)

```sql
CREATE TABLE product (
  product_id     INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  product_code   CHAR(10) NOT NULL,                  -- fixed-length code
  name           VARCHAR(200) NOT NULL,              -- variable-length
  short_desc     VARCHAR(500),                       -- prefer VARCHAR over TINYTEXT
  long_desc      TEXT,                               -- longer text
  price          DECIMAL(9,2) NOT NULL,              -- exact money
  size_label     ENUM('small','medium','large'),     -- one of a list
  available_colors SET('black','white','red','blue','green')  -- multiple tags
);
```

### 7.2 People & timestamps (date/time types)

```sql
CREATE TABLE person_event (
  id          BIGINT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
  full_name   VARCHAR(150) NOT NULL,
  phone       VARCHAR(20),                 -- keep +, leading zeros
  dob         DATE,                        -- date only
  created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,        -- UTC-aware timestamp
  fixed_time  DATETIME                      -- absolute time, no TZ conversion
);
```

### 7.3 Security & files (binary types)

```sql
CREATE TABLE user_file (
  user_id        BIGINT UNSIGNED NOT NULL,
  security_key   BINARY(32),               -- exact 32-byte key (e.g., SHA-256)
  session_token  VARBINARY(128),           -- variable-length token
  profile_pic    TINYBLOB,                 -- small image
  document_pdf   MEDIUMBLOB,               -- medium file
  large_media    LONGBLOB,                 -- up to ~4GB
  PRIMARY KEY (user_id)
);
```

---

## 8) When to Use What?

* **Exact decimals (money, rates)** → `DECIMAL(p,s)`
* **Approximate scientific numbers** → `FLOAT`/`DOUBLE`
* **IDs/counters** → smallest suitable integer (`TINYINT` → `BIGINT`, consider `UNSIGNED`)
* **Human-readable identifiers (phones, SKUs with +/leading zeros)** → `VARCHAR`
* **Fixed-length codes** → `CHAR(n)`
* **Very long text** → `TEXT`/`MEDIUMTEXT`/`LONGTEXT` (mind indexing)
* **Small categorical** → `ENUM`; **multi-tag** → `SET`
* **Timezone-aware timestamps** → `TIMESTAMP`
* **Absolute timestamps (no TZ changes)** → `DATETIME`
* **Exact binary (keys, hashes)** → `BINARY/VARBINARY`
* **Binary files** → `BLOB` family

---



#  Database-Level Commands(CREATE / SHOW / USE / DROP)


## 1 Creating a database

* SQL command: `CREATE DATABASE database_name;`
* Example: `CREATE DATABASE school_db;`
* Database names can include letters, numbers, and underscores. MySQL keywords are case-insensitive.
* On Windows, database names are case-insensitive by default; in general, treat names without relying on case.

## 2 Listing databases

* Command: `SHOW DATABASES;`
* It returns all databases available on the current MySQL server (including system databases like `mysql`, `information_schema`, `performance_schema`).
* After creating a DB, run `SHOW DATABASES;` to verify it appears.

## 3 Running multiple commands & semicolons

* SQL statements are terminated with a semicolon `;` when executing multiple statements in the same execution block.
* If you run single statements interactively, some tools allow executing without `;`, but using `;` is safer when batching commands.
* Example problem: running `CREATE DATABASE books` followed immediately by another command without `;` can cause a syntax error or unexpected parsing.

## 4 Selecting (switching) a database

* Command: `USE database_name;` — this makes `database_name` the current default for subsequent commands.
* SQL functions: `SELECT DATABASE();` returns the current default database name (or `NULL` if none selected).
* Example flow:

  ```sql
  CREATE DATABASE school_db;
  USE school_db;
  -- now CREATE TABLE will create tables inside school_db
  ```

## 5 Case sensitivity & keywords

* MySQL keywords (`CREATE`, `SHOW`, `USE`, etc.) are case-insensitive.
* Database names are typically case-insensitive on Windows, but on Unix-like systems they may be case-sensitive depending on filesystem settings. Best practice: be consistent with naming.

## 6 Dropping a database

* Command: `DROP DATABASE database_name;`
* Warning: this **deletes the entire database** and all objects inside it (tables, data). It also removes related metadata from MySQL internal tables.
* Example: `DROP DATABASE test;` — the server will report how many objects (rows) were affected; this confirms deletion.
* Dropping a database is destructive and usually irreversible; ensure you have backups if needed.

## 7 Typical workflow summarized

1. `CREATE DATABASE dbname;`
2. `SHOW DATABASES;` - confirm creation
3. `USE dbname;` - switch to the database
4. `CREATE TABLE ...;` - create tables inside `dbname`
5. `SELECT DATABASE();` - confirm which DB is currently selected
6. `DROP DATABASE dbname;` - remove if you no longer need it (with caution)

---

# Table Commands - CREATE / ALTER / DROP / INSERT

## 1. CREATE TABLE

* **Definition:** Used to create a new table in the database with specified columns and their data types.
* **Syntax:**

```sql
CREATE TABLE table_name (
    column1 datatype constraints,
    column2 datatype constraints,
    ...
);
```


---

## 2. ALTER TABLE

* **Definition:** Used to modify an existing table structure (add, delete, or modify columns, or manage constraints).
* **Syntax:**

```sql
ALTER TABLE table_name ADD column_name datatype;
ALTER TABLE table_name DROP COLUMN column_name;
ALTER TABLE table_name MODIFY COLUMN column_name datatype;
ALTER TABLE table_name RENAME COLUMN old_name TO new_name;
ALTER TABLE table_name RENAME TO new_table_name;
```



---

## 3. DROP TABLE

* **Definition:** Deletes an entire table and all its data permanently.
* **Syntax:**

```sql
DROP TABLE table_name;
```

* **Examples:**

```sql
-- Drop a single table
DROP TABLE Student;

-- Drop multiple tables
DROP TABLE Student, Employee;

-- Drop if exists (avoids error if table does not exist)
DROP TABLE IF EXISTS Users;
```

 *Once dropped, the table and its data cannot be recovered.*

---

## 4. INSERT INTO

* **Definition:** Used to insert new records (rows) into a table.
* **Syntax:**

```sql
INSERT INTO table_name (column1, column2, ...)
VALUES (value1, value2, ...);

-- Insert multiple rows
INSERT INTO table_name (column1, column2, ...)
VALUES (value1a, value2a, ...),
       (value1b, value2b, ...);
```



---