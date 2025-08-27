-- Database Setup - Friends Theme
-- ====================================================================
CREATE DATABASE friends_db;
USE friends_db;

-- Create tables for our demonstration
CREATE TABLE characters (
    character_id INT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    occupation VARCHAR(100)
);

CREATE TABLE apartments (
    apartment_id INT PRIMARY KEY,
    building_address VARCHAR(100) NOT NULL,
    apartment_number VARCHAR(10) NOT NULL,
    monthly_rent DECIMAL(8, 2),
    current_tenant_id INT
);

-- Sample Data
-- ====================================================================
-- Insert data into characters
INSERT INTO characters (character_id, first_name, last_name, occupation) VALUES
(1, 'Ross', 'Geller', 'Paleontologist'),
(2, 'Rachel', 'Green', 'Fashion Executive'),
(3, 'Chandler', 'Bing', 'IT Procurement Manager'),
(4, 'Monica', 'Geller', 'Chef'),
(5, 'Joey', 'Tribbiani', 'Actor'),
(6, 'Phoebe', 'Buffay', 'Massage Therapist'),
(7, 'Gunther', 'Smith', 'Coffee Shop Manager'),
(8, 'Janice', 'Hosenstein', 'Unknown');

-- Insert data into apartments
INSERT INTO apartments (apartment_id, building_address, apartment_number, monthly_rent, current_tenant_id) VALUES
(101, '90 Bedford Street', '20', 3500.00, 3),
(102, '90 Bedford Street', '19', 3500.00, 4),
(103, '5 Morton Street', '14', 2800.00, 6),
(104, '17 Grove Street', '3B', 2200.00, NULL),
(105, '15 Yemen Road', 'Yemen', 900.00, NULL),
(106, '495 Grove Street', '7', 2400.00, 1);

-- View table data
-- ====================================================================
SELECT * FROM characters;
SELECT * FROM apartments;

-- FULL JOIN Example (MySQL implementation using UNION)
-- All characters and all apartments, with matches where they exist
select c.character_id, c.first_name, c.last_name, c.occupation,
       a.apartment_id, a.building_address, a.apartment_number, a.monthly_rent
    from characters c left join apartments a on c.character_id = a.current_tenant_id
union 
select c.character_id, c.first_name, c.last_name, c.occupation,
       a.apartment_id, a.building_address, a.apartment_number, a.monthly_rent
    from characters c right join apartments a on c.character_id = a.current_tenant_id;

-- Finding only characters without apartments
SELECT c.character_id, c.first_name, c.last_name
    from characters c left join apartments a on c.character_id = a.current_tenant_id
    where a.apartment_id is null;

-- Finding only apartments without tenants
select apartment_id, building_address, apartment_number from apartments where current_tenant_id is null;

-- Finding only apartments without tenants - correct way
SELECT a.apartment_id, a.building_address, a.apartment_number
FROM apartments a
LEFT JOIN characters c ON c.character_id = a.current_tenant_id
WHERE c.character_id IS NULL;


-- Using the FULL JOIN result to find both unmatched cases
select c.character_id, c.first_name, c.last_name, c.occupation,
       a.apartment_id, a.building_address, a.apartment_number, a.monthly_rent
    from characters c left join apartments a on c.character_id = a.current_tenant_id where current_tenant_id is null
union 
select c.character_id, c.first_name, c.last_name, c.occupation,
       a.apartment_id, a.building_address, a.apartment_number, a.monthly_rent
    from characters c right join apartments a on c.character_id = a.current_tenant_id where character_id is null;

-- Using the FULL JOIN result to find both unmatched cases
SELECT c.character_id, c.first_name, c.last_name, 
       a.apartment_id, a.building_address, a.apartment_number
FROM characters c
LEFT JOIN apartments a ON c.character_id = a.current_tenant_id
WHERE a.apartment_id IS NULL
UNION
SELECT c.character_id, c.first_name, c.last_name, 
       a.apartment_id, a.building_address, a.apartment_number
FROM characters c
RIGHT JOIN apartments a ON c.character_id = a.current_tenant_id
WHERE c.character_id IS NULL;