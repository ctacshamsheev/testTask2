package org.example;

import java.io.IOException;

public class Main {
    public static final String DB_DRIVER = "org.postgresql.Driver";
    public static final String DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/postgres";
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "postgres";

    public static void main(String[] args) {
        try {
            ServiceBuilder build = new ServiceBuilder(new ArgsParser(args), DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
//            ServiceBuilder build = new ServiceBuilder(new ArgsParser(new String[]{"search", "in1.json", "out1.json"}), DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
//            ServiceBuilder build2 = new ServiceBuilder(new ArgsParser(new String[]{"stat", "in2.json", "out2.json"}), DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
