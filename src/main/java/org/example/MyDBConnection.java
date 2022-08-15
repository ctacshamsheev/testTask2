package org.example;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDBConnection {
    Connection dbConnection = null;

    public Connection getDBConnection() {
        return dbConnection;
    }

    MyDBConnection(String DB_DRIVER, String DB_CONNECTION, String DB_USER, String DB_PASSWORD) throws Exception {
        try {
            Class.forName(DB_DRIVER);
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        try {
            dbConnection = DriverManager.getConnection(DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            throw new Exception(e.getMessage());
        }
        System.out.println("Connection to: " + DB_CONNECTION + " successful");
    }
}
