-- DROP TABLE customers, products, sales;

CREATE TABLE customers
(
    id        int PRIMARY KEY NOT NULL,
    firstName varchar(20),
    lastName  varchar(20)
);

CREATE TABLE products
(
    id          int PRIMARY KEY NOT NULL,
    productName varchar(20),
    expenses    int
);

CREATE TABLE sales
(
    id        int PRIMARY KEY NOT NULL,
    sale_date date,
    customer  int,
    product   int,
    CONSTRAINT fk_customer_id FOREIGN KEY (customer) REFERENCES customers (id),
    CONSTRAINT fk_products_id FOREIGN KEY (product) REFERENCES products (id)
);

INSERT INTO customers (id, firstName, lastName)
VALUES (1, 'Иван', 'Иванов');
INSERT INTO customers (id, firstName, lastName)
VALUES (2, 'Степан', 'Иванов');
INSERT INTO customers (id, firstName, lastName)
VALUES (3, 'Николай', 'Иванов');
INSERT INTO customers (id, firstName, lastName)
VALUES (4, 'Сидр', 'Сидоров');
INSERT INTO customers (id, firstName, lastName)
VALUES (5, 'Петр', 'Петров');
INSERT INTO customers (id, firstName, lastName)
VALUES (6, 'Иван', 'Петров');
INSERT INTO customers (id, firstName, lastName)
VALUES (7, 'Андрей', 'Андреев');
INSERT INTO customers (id, firstName, lastName)
VALUES (8, 'Павел', 'Павлов');

INSERT INTO products (id, productName, expenses)
VALUES (1, 'Хлеб', 25);
INSERT INTO products (id, productName, expenses)
VALUES (2, 'Минеральная вода', 36);
INSERT INTO products (id, productName, expenses)
VALUES (3, 'Сметана', 86);
INSERT INTO products (id, productName, expenses)
VALUES (4, 'Колбаса', 290);
INSERT INTO products (id, productName, expenses)
VALUES (5, 'Сыр', 189);
INSERT INTO products (id, productName, expenses)
VALUES (6, 'Яйца', 250);
INSERT INTO products (id, productName, expenses)
VALUES (7, 'Молоко', 79);
INSERT INTO products (id, productName, expenses)
VALUES (8, 'Помидоры', 90);
INSERT INTO products (id, productName, expenses)
VALUES (9, 'Булочка', 10);

INSERT INTO sales (id, sale_date, customer, product)
VALUES (1, '2022-01-14', 1, 1);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (2, '2022-01-14', 1, 2);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (3, '2022-01-14', 1, 3);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (4, '2022-01-14', 2, 4);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (5, '2022-01-14', 2, 5);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (6, '2022-01-15', 2, 6);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (7, '2022-01-15', 2, 7);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (8, '2022-01-15', 3, 8);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (9, '2022-01-15', 4, 9);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (10, '2022-01-16', 4, 1);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (11, '2022-01-16', 5, 2);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (12, '2022-01-16', 5, 3);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (13, '2022-01-16', 6, 4);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (14, '2022-01-17', 7, 5);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (15, '2022-01-17', 7, 6);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (16, '2022-01-17', 7, 7);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (17, '2022-01-18', 8, 8);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (18, '2022-01-18', 1, 9);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (19, '2022-01-18', 1, 1);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (20, '2022-01-18', 2, 2);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (21, '2022-02-14', 3, 3);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (22, '2022-02-14', 4, 4);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (23, '2022-02-15', 8, 5);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (24, '2022-02-15', 6, 6);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (25, '2022-02-15', 7, 7);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (26, '2022-02-16', 1, 8);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (27, '2022-02-17', 1, 9);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (28, '2022-02-17', 2, 1);
INSERT INTO sales (id, sale_date, customer, product)
VALUES (29, '2022-02-17', 2, 2);

-- SELECT *
-- FROM customers;

-- SELECT *
-- FROM products;

-- SELECT *
-- FROM sales;

SELECT productname, expenses, firstname, lastname, sale_date
FROM sales
        JOIN customers c on c.id = sales.customer
        JOIN products p on p.id = sales.product;
-- WHERE p.productName = 'Хлеб';


-- 1)))
-- — поиск покупателей с этой фамилией 
SELECT firstname, lastname
FROM customers
WHERE lastname = 'Иванов';


-- 2)))
-- Название товара и число раз 
-- поиск покупателей, купивших этот товар не менее, чем указанное число раз
WITH ggg AS (SELECT c.id as customer_id, count(c.id) as kolvo
             FROM sales
                      JOIN customers c on c.id = sales.customer
                      JOIN products p on p.id = sales.product
             WHERE p.productName = 'Хлеб'
             GROUP BY c.id)
SELECT firstname, lastname, kolvo as quantity
FROM ggg
         JOIN customers c on customer_id = c.id;

-- 3)))
--	Минимальная и максимальная стоимость всех покупок
-- поиск покупателей, у которых общая стоимость всех покупок за всё время попадает в интервал
WITH customer_expenses AS (SELECT c.id, sum(expenses) as exp
                           FROM sales
                                    JOIN products p on p.id = sales.product
                                    JOIN customers c on c.id = sales.customer
                           group by c.id
                           ORDER BY c.id)
SELECT firstname, lastname, exp
FROM customer_expenses
         JOIN customers c ON customer_expenses.id = c.id
WHERE exp > 100
  AND exp < 500
ORDER BY exp DESC;


-- 4)))))
-- Число пассивных покупателей
-- поиск покупателей, купивших меньше всего товаров. Возвращается не более, чем указанное число покупателей.

WITH customer_occurencies AS (SELECT c.id as customer_id, count(c.id) as occurencies
                              FROM sales
                                       JOIN customers c on c.id = sales.customer
                                       JOIN products p on p.id = sales.product
                              GROUP BY c.id)
SELECT firstname, lastname, occurencies
FROM customer_occurencies
         JOIN customers c on customer_id = c.id
ORDER BY occurencies
LIMIT 3;




-- [ // Данные по покупателям за этот период, упорядоченные по общей стоимости покупок по убыванию
-- // Фамилия и имя покупателя
-- // Список всех уникальных товаров, купленных покупателем за этот период, упорядоченных по суммарной стоимости по убыванию
