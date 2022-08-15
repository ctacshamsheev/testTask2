package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.*;

public class MyDBConnection {
    Connection connection = null;

    public Connection getDBConnection() {
        return connection;
    }

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

    public JSONArray getCriteriaLastName(String lastName) throws SQLException {
        String SQL_SELECT = "SELECT firstname, lastname\n" +
                "FROM customers\n" +
                "WHERE lastname = \'" + lastName + "\';";
        return getPreparedStatementFirsNameLastName(SQL_SELECT);
    }

    public JSONArray getCriteriaProductNameminTimes(String productName, long minTimes) throws SQLException {
        String SQL_SELECT =
                "WITH res AS (SELECT c.id as customer_id, count(c.id) as countsale\n" +
                        "             FROM sales\n" +
                        "             JOIN customers c on c.id = sales.customer\n" +
                        "             JOIN products p on p.id = sales.product\n" +
                        "             WHERE p.productName = \'" + productName + "\'\n" +
                        "             GROUP BY c.id)\n" +
                        "SELECT firstname, lastname, countsale as quantity\n" +
                        "FROM res\n" +
                        "JOIN customers c on customer_id = c.id\n" +
                        "WHERE countsale >= " + minTimes + ";";
        return getPreparedStatementFirsNameLastName(SQL_SELECT);
    }

    public JSONArray getCriteriaminmaxExpenses(long minExpenses, long maxExpenses) throws SQLException {
        String SQL_SELECT =
                "WITH customer_expenses AS (SELECT c.id, sum(expenses) as exp\n" +
                        "                           FROM sales\n" +
                        "                           JOIN products p on p.id = sales.product\n" +
                        "                           JOIN customers c on c.id = sales.customer\n" +
                        "                           group by c.id\n" +
                        "                           ORDER BY c.id)\n" +
                        "SELECT firstname, lastname, exp\n" +
                        "FROM customer_expenses\n" +
                        "JOIN customers c ON customer_expenses.id = c.id\n" +
                        "WHERE exp > " + minExpenses +
                        "  AND exp < " + maxExpenses +
                        "ORDER BY exp DESC;";
        return getPreparedStatementFirsNameLastName(SQL_SELECT);
    }

    public JSONArray getCriteriaBadCustomers(long badCustomers) throws SQLException {
        String SQL_SELECT =
                "WITH customer_occurencies AS (SELECT c.id as customer_id, count(c.id) as occurencies\n" +
                        "                              FROM sales\n" +
                        "                              JOIN customers c on c.id = sales.customer\n" +
                        "                              JOIN products p on p.id = sales.product\n" +
                        "                              GROUP BY c.id)\n" +
                        "SELECT firstname, lastname, occurencies\n" +
                        "FROM customer_occurencies\n" +
                        "JOIN customers c on customer_id = c.id\n" +
                        "ORDER BY occurencies LIMIT " + badCustomers + ";";
        return getPreparedStatementFirsNameLastName(SQL_SELECT);
    }

    public long getStatTotalExpenses(String startDateStr, String endDateStr) throws SQLException {
        String SQL_SELECT = "SELECT SUM (expenses) FROM sales\n" +
                "JOIN products p on p.id = sales.product\n" +
                "WHERE sale_date >= DATE \'" + startDateStr + "\' AND sale_date <= DATE \'" + endDateStr + "\';";
        PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        long result = resultSet.getLong("SUM");
        return result;
    }

}
