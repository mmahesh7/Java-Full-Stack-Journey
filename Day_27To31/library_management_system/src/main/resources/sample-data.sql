USE library_db;

-- Sample Authors
INSERT IGNORE INTO authors (name, email, birth_year, biography) VALUES
('J.K. Rowling', 'jk.rowling@example.com', 1965, 'British author, best known for the Harry Potter fantasy series.'),
('George Orwell', 'george.orwell@example.com', 1903, 'English novelist and social critic.'),
('Jane Austen', 'jane.austen@example.com', 1775, 'English novelist known for social commentary.'),
('Stephen King', 'stephen.king@example.com', 1947, 'American author known for horror and supernatural fiction.'),
('Agatha Christie', 'agatha.christie@example.com', 1890, 'English writer known for detective novels.');

-- Sample Books
INSERT IGNORE INTO books (title, isbn, publication_year, price, copies_available, total_copies, author_id) VALUES
('Harry Potter and the Philosopher\'s Stone', '978-0-7475-3269-9', 1997, 12.99, 5, 5, 1),
('Harry Potter and the Chamber of Secrets', '978-0-7475-3849-3', 1998, 12.99, 3, 3, 1),
('1984', '978-0-452-28423-4', 1949, 13.99, 4, 4, 2),
('Animal Farm', '978-0-452-28424-1', 1945, 10.99, 6, 6, 2),
('Pride and Prejudice', '978-0-14-143951-8', 1813, 11.99, 4, 4, 3),
('Sense and Sensibility', '978-0-14-143962-4', 1811, 11.99, 2, 2, 3),
('The Shining', '978-0-385-12167-5', 1977, 14.99, 3, 3, 4),
('It', '978-0-670-81302-6', 1986, 16.99, 2, 2, 4),
('Murder on the Orient Express', '978-0-00-711932-7', 1934, 9.99, 5, 5, 5),
('The ABC Murders', '978-0-00-711933-4', 1936, 9.99, 3, 3, 5);

-- Sample Members
INSERT IGNORE INTO members (name, email, phone, address, membership_type, status) VALUES
('Alice Johnson', 'alice.johnson@email.com', '555-0101', '123 Main St, Anytown, AN 12345', 'PREMIUM', 'ACTIVE'),
('Bob Smith', 'bob.smith@email.com', '555-0102', '456 Oak Ave, Somewhere, SW 67890', 'BASIC', 'ACTIVE'),
('Carol Davis', 'carol.davis@email.com', '555-0103', '789 Pine Rd, Elsewhere, EW 54321', 'BASIC', 'ACTIVE'),
('David Wilson', 'david.wilson@email.com', '555-0104', '321 Elm St, Nowhere, NW 09876', 'PREMIUM', 'ACTIVE'),
('Eva Brown', 'eva.brown@email.com', '555-0105', '654 Maple Dr, Anywhere, AW 13579', 'BASIC', 'ACTIVE');

-- Sample Book Loans (some active, some returned)
INSERT IGNORE INTO book_loans (book_id, member_id, loan_date, due_date, return_date, fine_amount, status) VALUES
(1, 1, '2024-01-15', '2024-02-05', '2024-02-03', 0.00, 'RETURNED'),
(2, 1, '2024-02-01', '2024-02-22', NULL, 0.00, 'ACTIVE'),
(3, 2, '2024-01-20', '2024-02-03', '2024-02-10', 7.00, 'RETURNED'),
(4, 3, '2024-02-05', '2024-02-19', NULL, 0.00, 'ACTIVE'),
(5, 4, '2024-01-10', '2024-01-31', NULL, 12.00, 'OVERDUE'),
(6, 5, '2024-02-08', '2024-02-22', NULL, 0.00, 'ACTIVE');

-- Update some loan statuses and book availability (simulating real usage)
UPDATE book_loans SET status = 'OVERDUE' WHERE due_date < CURDATE() AND status = 'ACTIVE';