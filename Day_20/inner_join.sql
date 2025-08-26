create DATABASE db_inner_join;
use db_inner_join;

create Table authors (
    author_id int AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    birth_year int
);

create table books(
    book_id int primary key,
    title VARCHAR(100),
    author_id int,
    publication_year int,
    price decimal(10,2)
);

insert into authors (author_id, first_name, last_name, birth_year) 
VALUES
    (1, 'Jane', 'Austen', 1775),
    (2, 'George', 'Orwell', 1903),
    (3, 'Ernest', 'Hemingway', 1899),
    (4, 'Agatha', 'Christie', 1890),
    (5, 'J.K.', 'Rowling', 1965);

INSERT into books (book_id, title, author_id, publication_year, price)
VALUES
    (101, 'Pride and Prejudice', 1, 1813, 12.99),
    (102, '1984', 2, 1949, 14.50),
    (103, 'Animal Farm', 2, 1945, 11.75),
    (104, 'The Old Man and the Sea', 3, 1952, 10.99),
    (105, 'Murder on the Orient Express', 4, 1934, 13.25),
    (106, 'Death on the Nile', 4, 1937, 12.50),
    (107, 'Emma', 1, 1815, 11.99),
    (108, 'For Whom the Bell Tolls', 3, 1940, 15.75);

select *from authors;
select *from books;

SELECT * from authors a join books b on a.author_id = b.author_id;

-- Retrieve books with their author's information with conditions and ordering
select title, a.author_id, a.first_name, a.last_name, a.birth_year from books b join authors a on a.author_id = b.author_id;

select title, a.author_id, a.first_name, a.last_name, a.birth_year from books b join authors a on a.author_id = b.author_id
    where b.publication_year > 1940
    ORDER BY birth_year;

-- Count how many books each author has written
select CONCAT(first_name, " ", last_name), count(*) from books b join authors a on a.author_id = b.author_id
GROUP BY a.author_id;

-- Create junction table for book-category many-to-many relationship
CREATE TABLE book_categories (
    book_id INT,
    category_id INT,
    PRIMARY KEY (book_id, category_id)
);

-- Create categories table for many-to-many relationship example
CREATE TABLE categories (
    category_id INT PRIMARY KEY,
    category_name VARCHAR(50)
);
INSERT INTO categories (category_id, category_name)
VALUES 
    (1, 'Fiction'),
    (2, 'Classic'),
    (3, 'Romance'),
    (4, 'Political'),
    (5, 'Mystery'),
    (6, 'Adventure');

-- Create junction table for book-category many-to-many relationship
CREATE TABLE book_categories (
    book_id INT,
    category_id INT,
    PRIMARY KEY (book_id, category_id)
);

INSERT INTO book_categories (book_id, category_id)
VALUES 
    (101, 1), (101, 2), (101, 3), -- Pride and Prejudice: Fiction, Classic, Romance
    (102, 1), (102, 2), (102, 4), -- 1984: Fiction, Classic, Political
    (103, 1), (103, 2), (103, 4), -- Animal Farm: Fiction, Classic, Political
    (104, 1), (104, 2), (104, 6), -- The Old Man and the Sea: Fiction, Classic, Adventure
    (105, 1), (105, 5), -- Murder on the Orient Express: Fiction, Mystery
    (106, 1), (106, 5), -- Death on the Nile: Fiction, Mystery
    (107, 1), (107, 2), (107, 3), -- Emma: Fiction, Classic, Romance
    (108, 1), (108, 2), (108, 6); -- For Whom the Bell Tolls: Fiction, Classic, Adventure

-- Get books with their authors and categories using GROUP_CONCAT
select b.title, CONCAT(a.first_name," ",a.last_name) as author_name, GROUP_CONCAT(c.category_name SEPARATOR ", ") as categories
     from books b 
    join authors a on a.author_id = b.author_id
    join book_categories bc on b.book_id = bc.book_id
    join categories c on bc.category_id = c.category_id
    GROUP BY b.book_id;

-- Example with join condition in ON clause
select b.title, a.last_name from books b inner join authors a on b.author_id = a.author_id
    and b.publication_year > 1970
    and a.birth_year < 1900;

SELECT b.title, a.last_name from books b inner join authors a
    on b.author_id = a.author_id where b.publication_year< 1950;

-- Example with date functions - books published more than 70 years ago
select b.title, a.last_name, b.publication_year from books b join authors a on b.author_id = a.author_id
    where (YEAR(CURRENT_TIME) - b.publication_year) > 70;

/*
Note: INNER JOIN excludes rows with NULL values in the join columns. If you want to include rows with NULL values,
you would need to use LEFT JOIN or RIGHT JOIN (which we'll cover in later sections).
*/
 
-- Find authors who have written more than one book using HAVING clause
SELECT a.last_name, count(b.book_id) as no_of_books from authors a join books b on a.author_id = b.author_id GROUP BY a.author_id, a.first_name, a.last_name HAVING count(b.book_id)> 1;