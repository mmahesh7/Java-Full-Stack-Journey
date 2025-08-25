use db_for_groupby;

SELECT DATABASE();

create table employees(
    id int AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(30),
    department varchar(50),
    salary decimal(10,2),
    joining_date DATE
);

insert into employees(name, department, salary, joining_date) VALUES
('Alice', 'HR', 50000, '2020-06-15'),
('Bob', 'HR', 55000, '2019-08-20'),
('Charlie', 'IT', 70000, '2018-03-25'),
('David', 'IT', 72000, '2017-07-10'),
('Eve', 'IT', 73000, '2021-02-15'),
('Frank', 'Finance', 60000, '2020-11-05'),
('Grace', 'Finance', 65000, '2019-05-30'),
('Hannah', 'Finance', 62000, '2021-01-12');

SELECT * from employees;


select department, count(*) from  employees GROUP BY department;

SELECT department, avg(salary) from employees GROUP BY department;

SELECT department, MIN(salary) as lowest_salary, MAX(salary) as highest_salary from employees GROUP BY department;

INSERT INTO employees (name, department, salary, joining_date) VALUES
('Tim', 'HR', 65000, '2019-05-30'),
('Tom', 'IT', 62000, '2021-01-12');

SELECT department, year(joining_date) as joining_year, COUNT(*) from employees GROUP BY year(joining_date),department;

SELECT department, avg(salary) as avg_salary from employees GROUP BY department ORDER BY avg(salary) DESC ;

SELECT 
CASE 
    WHEN salary<60000 THEN  "low"
    when salary between 60000 and 70000 then "medium"
    ELSE "high"  
END as salary_range,
count(*)
from employees 
GROUP BY salary_range;


select department, count(*) from employees GROUP BY department;

SELECT department, count(*) as no_of_employees from employees GROUP BY department order by no_of_employees desc limit 1;

SELECT department, count(*) as total_employees, avg(salary) as average_salary from employees
    where joining_date > '2017-01-31' GROUP BY department HAVING total_employees > 2 and average_salary > 60000;