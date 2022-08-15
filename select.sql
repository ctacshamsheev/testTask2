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
WITH res AS (SELECT c.id as customer_id, count(c.id) as countsale
             FROM sales
                      JOIN customers c on c.id = sales.customer
                      JOIN products p on p.id = sales.product
             WHERE p.productName = 'Хлеб'
             GROUP BY c.id)
SELECT firstname, lastname, countsale as quantity
FROM res
         JOIN customers c on customer_id = c.id
WHERE countsale >= 3;

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
ORDER BY occurencies LIMIT 3;


-- // сумма покупок за период
SELECT SUM(expenses)
FROM sales
         JOIN customers c on c.id = sales.customer
         JOIN products p on p.id = sales.product
WHERE sale_date >= DATE '2022-01-15'
  AND sale_date <= DATE '2022-02-15';


-- все продажи за период
SELECT p.id as pid, productname, expenses, c.id as cid, firstname, lastname, sale_date
FROM sales
         JOIN customers c on c.id = sales.customer
         JOIN products p on p.id = sales.product
WHERE sale_date >= DATE '2022-01-15'
  AND sale_date <= DATE '2022-01-17'
ORDER BY sale_date, cid;


-- кто сколько купил за период
SELECT c.id, firstname, lastname, SUM(p.expenses) as sum_exp
FROM sales
         JOIN customers c on c.id = sales.customer
         JOIN products p on p.id = sales.product
WHERE sale_date >= DATE '2022-01-15'
  AND sale_date <= DATE '2022-01-17'
GROUP BY c.id
ORDER BY sum_exp DESC;

-- сумма по покупкам человека для каждого товара за период
WITH customer_occurencies AS (SELECT p.id as pid, expenses, c.id as cid
                              FROM sales
                                       JOIN customers c on c.id = sales.customer
                                       JOIN products p on p.id = sales.product
                              WHERE sale_date >= DATE '2022-01-15'
                                AND sale_date <= DATE '2022-01-17'
                              ORDER BY sale_date, cid)
SELECT pr.productname, sum(customer_occurencies.expenses) as sum_exp
FROM customer_occurencies
         JOIN products pr on pr.id = pid
WHERE cid = 2
GROUP BY pr.productname
ORDER BY sum_exp DESC;

-- среднее
WITH sum_sale AS (SELECT SUM(p.expenses) as sum_exp
                  FROM sales
                           JOIN customers c on c.id = sales.customer
                           JOIN products p on p.id = sales.product
                  WHERE sale_date >= DATE '2022-01-15'
                    AND sale_date <= DATE '2022-01-17'
                  GROUP BY c.id)
SELECT AVG(sum_exp)
FROM sum_sale;

