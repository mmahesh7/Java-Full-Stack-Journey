-- Create database
CREATE DATABASE IF NOT EXISTS library_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

USE library_db;

-- Create user (optional - for security)
-- CREATE USER IF NOT EXISTS 'library_user'@'localhost' IDENTIFIED BY 'library_pass';
-- GRANT ALL PRIVILEGES ON library_db.* TO 'library_user'@'localhost';
-- FLUSH PRIVILEGES;

-- Authors table
CREATE TABLE IF NOT EXISTS authors (
    author_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    birth_year INT,
    biography TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_author_name (name),
    INDEX idx_author_email (email)
);

-- Books table
CREATE TABLE IF NOT EXISTS books (
    book_id INT PRIMARY KEY AUTO_INCREMENT,
    title VARCHAR(200) NOT NULL,
    isbn VARCHAR(20) UNIQUE,
    publication_year INT,
    price DECIMAL(10,2),
    copies_available INT DEFAULT 1,
    total_copies INT DEFAULT 1,
    author_id INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (author_id) REFERENCES authors(author_id) ON DELETE RESTRICT,
    INDEX idx_book_title (title),
    INDEX idx_book_isbn (isbn),
    INDEX idx_book_author (author_id),
    CONSTRAINT chk_copies CHECK (copies_available >= 0),
    CONSTRAINT chk_total_copies CHECK (total_copies >= copies_available)
);

-- Members table
CREATE TABLE IF NOT EXISTS members (
    member_id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    phone VARCHAR(15),
    address TEXT,
    join_date DATE DEFAULT (CURRENT_DATE),
    membership_type ENUM('BASIC', 'PREMIUM') DEFAULT 'BASIC',
    status ENUM('ACTIVE', 'SUSPENDED', 'INACTIVE') DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    INDEX idx_member_name (name),
    INDEX idx_member_email (email),
    INDEX idx_member_type (membership_type)
);

-- Book lending table
CREATE TABLE IF NOT EXISTS book_loans (
    loan_id INT PRIMARY KEY AUTO_INCREMENT,
    book_id INT NOT NULL,
    member_id INT NOT NULL,
    loan_date DATE DEFAULT (CURRENT_DATE),
    due_date DATE NOT NULL,
    return_date DATE,
    fine_amount DECIMAL(10,2) DEFAULT 0,
    status ENUM('ACTIVE', 'RETURNED', 'OVERDUE') DEFAULT 'ACTIVE',
    notes TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (book_id) REFERENCES books(book_id) ON DELETE RESTRICT,
    FOREIGN KEY (member_id) REFERENCES members(member_id) ON DELETE RESTRICT,
    INDEX idx_loan_book (book_id),
    INDEX idx_loan_member (member_id),
    INDEX idx_loan_status (status),
    INDEX idx_loan_due_date (due_date),
    CONSTRAINT chk_fine_amount CHECK (fine_amount >= 0),
    CONSTRAINT chk_dates CHECK (due_date >= loan_date),
    CONSTRAINT chk_return_date CHECK (return_date IS NULL OR return_date >= loan_date)
);

-- Create triggers for automatic updates
DELIMITER //

CREATE TRIGGER IF NOT EXISTS update_book_copies_after_loan
    AFTER INSERT ON book_loans
    FOR EACH ROW
BEGIN
    IF NEW.status = 'ACTIVE' THEN
        UPDATE books
        SET copies_available = copies_available - 1
        WHERE book_id = NEW.book_id;
    END IF;
END//

CREATE TRIGGER IF NOT EXISTS update_book_copies_after_return
    AFTER UPDATE ON book_loans
    FOR EACH ROW
BEGIN
    IF OLD.status = 'ACTIVE' AND NEW.status = 'RETURNED' THEN
        UPDATE books
        SET copies_available = copies_available + 1
        WHERE book_id = NEW.book_id;
    END IF;
END//

CREATE TRIGGER IF NOT EXISTS update_overdue_status
    BEFORE UPDATE ON book_loans
    FOR EACH ROW
BEGIN
    IF NEW.status = 'ACTIVE' AND NEW.due_date < CURDATE() THEN
        SET NEW.status = 'OVERDUE';
    END IF;
END//

DELIMITER ;

-- Views for common queries
CREATE VIEW IF NOT EXISTS active_loans AS
SELECT
    bl.loan_id,
    bl.loan_date,
    bl.due_date,
    bl.fine_amount,
    bl.status,
    b.title as book_title,
    b.isbn,
    a.name as author_name,
    m.name as member_name,
    m.email as member_email,
    DATEDIFF(CURDATE(), bl.due_date) as days_overdue
FROM book_loans bl
JOIN books b ON bl.book_id = b.book_id
JOIN authors a ON b.author_id = a.author_id
JOIN members m ON bl.member_id = m.member_id
WHERE bl.status IN ('ACTIVE', 'OVERDUE');

CREATE VIEW IF NOT EXISTS library_statistics AS
SELECT
    (SELECT COUNT(*) FROM authors) as total_authors,
    (SELECT COUNT(*) FROM books) as total_books,
    (SELECT SUM(total_copies) FROM books) as total_copies,
    (SELECT SUM(copies_available) FROM books) as available_copies,
    (SELECT COUNT(*) FROM members WHERE status = 'ACTIVE') as active_members,
    (SELECT COUNT(*) FROM book_loans WHERE status = 'ACTIVE') as active_loans,
    (SELECT COUNT(*) FROM book_loans WHERE status = 'OVERDUE') as overdue_loans,
    (SELECT SUM(fine_amount) FROM book_loans WHERE status IN ('OVERDUE', 'RETURNED')) as total_fines;