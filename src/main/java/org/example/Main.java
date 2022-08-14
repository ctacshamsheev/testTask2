package org.example;

import org.json.simple.parser.ParseException;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class Main {

    public static void main(String[] args) throws IOException {
//        FileWriter writer = new FileWriter("out1.json");
//        writer.write("{\"hello\":1}");
//        writer.close();

        try {
            JsonReader jsonIn = new JsonReader("in2.json");
            jsonIn.statParse();

        } catch (ParseException | java.text.ParseException e) {
            System.out.println(e);
        }

//        try {
//            String SQL_SELECT = "Select * from tast";
//            Connection conn = getDBConnection();
//            PreparedStatement preparedStatement = conn.prepareStatement(SQL_SELECT);
//            ResultSet resultSet = preparedStatement.executeQuery();
//            while (resultSet.next()) {
//                int id = resultSet.getInt("key_id");
//                String name = resultSet.getString("name");
//                System.out.println(id + " " + name);
//            }
//        } catch (SQLException e) {
//            System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static String DB_DRIVER = "org.postgresql.Driver";
    public static String DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/postgres";
    public static String DB_USER = "postgres";
    public static String DB_PASSWORD = "1";

    public static Connection getDBConnection() {
        Connection dbConnection = null;
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Connection to: " + DB_CONNECTION + " successful");
        return dbConnection;
    }

}