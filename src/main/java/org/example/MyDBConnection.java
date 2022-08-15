package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;

public class MyDBConnection {
    Connection connection = null;

    MyDBConnection(String DB_DRIVER, String DB_CONNECTION, String DB_USER, String DB_PASSWORD) throws Exception {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        try {
            connection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        System.out.println("Connection to: " + DB_CONNECTION + " successful");
    }

    // поиск и упаковка покупателей в json по запросу SQL_SELECT
    public JSONArray getPreparedStatementFirsNameLastName(String SQL_SELECT) throws SQLException {
        JSONArray listOutResult = new JSONArray();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            JSONObject objOut = new JSONObject();
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            objOut.put("lastName", lastname);
            objOut.put("firstName", firstname);
            listOutResult.add(objOut);
        }
        return listOutResult;
    }

    // 1.	Фамилия — поиск покупателей с этой фамилией
    public JSONArray getCriteriaLastName(String lastName) throws SQLException {
        String SQL_SELECT = "SELECT firstname, lastname\n" + "FROM customers\n" + "WHERE lastname = '" + lastName + "';";
        return getPreparedStatementFirsNameLastName(SQL_SELECT);
    }

    // 2.	Название товара и число раз — поиск покупателей, купивших этот товар не менее, чем указанное число раз
    public JSONArray getCriteriaProductNameminTimes(String productName, long minTimes) throws SQLException {
        String SQL_SELECT = "WITH res AS (SELECT c.id as customer_id, count(c.id) as countsale\n" + "             FROM sales\n" + "             JOIN customers c on c.id = sales.customer\n" + "             JOIN products p on p.id = sales.product\n" + "             WHERE p.productName = '" + productName + "'\n" + "             GROUP BY c.id)\n" + "SELECT firstname, lastname, countsale as quantity\n" + "FROM res\n" + "JOIN customers c on customer_id = c.id\n" + "WHERE countsale >= " + minTimes + ";";
        return getPreparedStatementFirsNameLastName(SQL_SELECT);
    }

    // 3.	Минимальная и максимальная стоимость всех покупок — поиск покупателей,
    // у которых общая стоимость всех покупок за всё время попадает в интервал
    public JSONArray getCriteriaminmaxExpenses(long minExpenses, long maxExpenses) throws SQLException {
        String SQL_SELECT = "WITH customer_expenses AS (SELECT c.id, sum(expenses) as exp\n" + "                           FROM sales\n" + "                           JOIN products p on p.id = sales.product\n" + "                           JOIN customers c on c.id = sales.customer\n" + "                           group by c.id\n" + "                           ORDER BY c.id)\n" + "SELECT firstname, lastname, exp\n" + "FROM customer_expenses\n" + "JOIN customers c ON customer_expenses.id = c.id\n" + "WHERE exp > " + minExpenses + "  AND exp < " + maxExpenses + "ORDER BY exp DESC;";
        return getPreparedStatementFirsNameLastName(SQL_SELECT);
    }

    // 4.	Число пассивных покупателей — поиск покупателей, купивших меньше всего товаров.
    // Возвращается не более, чем указанное число покупателей.
    public JSONArray getCriteriaBadCustomers(long badCustomers) throws SQLException {
        String SQL_SELECT = "WITH customer_occurencies AS (SELECT c.id as customer_id, count(c.id) as occurencies\n" + "                              FROM sales\n" + "                              JOIN customers c on c.id = sales.customer\n" + "                              JOIN products p on p.id = sales.product\n" + "                              GROUP BY c.id)\n" + "SELECT firstname, lastname, occurencies\n" + "FROM customer_occurencies\n" + "JOIN customers c on customer_id = c.id\n" + "ORDER BY occurencies LIMIT " + badCustomers + ";";
        return getPreparedStatementFirsNameLastName(SQL_SELECT);
    }

    // Сумма покупок всех покупателей за период
    public long getStatTotalExpenses(String startDateStr, String endDateStr) throws SQLException {
        String SQL_SELECT = "SELECT SUM (expenses) FROM sales\n" + "JOIN products p on p.id = sales.product\n" + "WHERE sale_date >= DATE '" + startDateStr + "' AND sale_date <= DATE '" + endDateStr + "';";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        long result = resultSet.getLong("SUM");
        return result;
    }

    // Средние затраты всех покупателей за период
    public double getStatAvgExpenses(String startDateStr, String endDateStr) throws SQLException {
        String SQL_SELECT = "WITH sum_sale AS (SELECT SUM(p.expenses) as sum_exp\n" + "                  FROM sales\n" + "                  JOIN customers c on c.id = sales.customer\n" + "                  JOIN products p on p.id = sales.product\n" + "WHERE sale_date >= DATE '" + startDateStr + "' AND sale_date <= DATE '" + endDateStr + "'\n" + "                  GROUP BY c.id)\n" + "SELECT AVG(sum_exp)\n" + "FROM sum_sale;";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        double result = resultSet.getDouble("avg");
        return result;
    }

    // json - Список всех уникальных товаров, купленных покупателем за этот период, упорядоченных по суммарной стоимости по убыванию
    public JSONArray getPreparedStatementProducts(String SQL_SELECT) throws SQLException {
        JSONArray listOutResult = new JSONArray();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            JSONObject objOut = new JSONObject();
            String productname = resultSet.getString("productname");
            long sum_exp = resultSet.getLong("sum_exp");

            objOut.put("name", productname);

            objOut.put("expenses", sum_exp);

            listOutResult.add(objOut);
        }
        return listOutResult;
    }

    // SQL - Список всех уникальных товаров, купленных покупателем за этот период, упорядоченных по суммарной стоимости по убыванию
    // json - Общая стоимость покупок этого покупателя за период (то есть сумма всех стоимостей покупок всех товаров)
    public JSONArray getPreparedStatementFirsNameWithLastName(String SQL_SELECT, String startDateStr, String endDateStr) throws SQLException {
        JSONArray listOutResult = new JSONArray();
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            JSONObject objOut = new JSONObject();
            String firstname = resultSet.getString("firstname");
            String lastname = resultSet.getString("lastname");
            long sum_exp = resultSet.getLong("sum_exp");
            long id = resultSet.getLong("id");
            objOut.put("name", lastname + " " + firstname);
            String SQL_NEW_SELECT = "WITH customer_occurencies AS (SELECT p.id as pid, expenses, c.id as cid\n" + "                              FROM sales\n" + "                                       JOIN customers c on c.id = sales.customer\n" + "                                       JOIN products p on p.id = sales.product\n" + "WHERE sale_date >= DATE '" + startDateStr + "' AND sale_date <= DATE '" + endDateStr + "'\n" + "                              ORDER BY sale_date, cid)\n" + "SELECT pr.productname, sum(customer_occurencies.expenses) as sum_exp\n" + "FROM customer_occurencies\n" + "         JOIN products pr on pr.id = pid\n" + "WHERE cid = " + id + "\n" + "GROUP BY pr.productname\n" + "ORDER BY sum_exp DESC;";
            JSONArray listProducts = getPreparedStatementProducts(SQL_NEW_SELECT);
            objOut.put("purchases", listProducts);
            objOut.put("totalExpenses", sum_exp);

            listOutResult.add(objOut);
        }
        return listOutResult;
    }

    // SQL - Общая стоимость покупок этого покупателя за период (то есть сумма всех стоимостей покупок всех товаров)
    public JSONArray getStatCustomers(String startDateStr, String endDateStr) throws SQLException {
        String SQL_SELECT = "SELECT c.id, firstname, lastname, SUM(p.expenses) as sum_exp\n" + "FROM sales\n" + "         JOIN customers c on c.id = sales.customer\n" + "         JOIN products p on p.id = sales.product\n" + "WHERE sale_date >= DATE '" + startDateStr + "' AND sale_date <= DATE '" + endDateStr + "'\n" + "GROUP BY c.id\n" + "ORDER BY sum_exp DESC;";
        return getPreparedStatementFirsNameWithLastName(SQL_SELECT, startDateStr, endDateStr);
    }
}
