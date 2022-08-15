package org.example;

import java.io.IOException;

public class Main {
    public static final String DB_DRIVER = "org.postgresql.Driver";
    public static final String DB_CONNECTION = "jdbc:postgresql://127.0.0.1:5432/postgres";
    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "1";

    public static void main(String[] args) {
        try {
            // ServiceBuilder build = new ServiceBuilder(new ArgsParser(args), DB_DRIVER, DB_CONNECTION,DB_USER, DB_PASSWORD );
            ServiceBuilder build = new ServiceBuilder(new ArgsParser(new String[]{"search", "in1.json", "out1.json"}), DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);
            ServiceBuilder build2 = new ServiceBuilder(new ArgsParser(new String[]{"stat", "in2.json", "out2.json"}), DB_DRIVER, DB_CONNECTION, DB_USER, DB_PASSWORD);

        } catch (IllegalArgumentException | IOException e) {
            System.out.println(e.getMessage());
        }
    }
}


//        FileWriter writer = new FileWriter("out1.json");
//        writer.write("{\"hello\":1}");
//        writer.close();

//        try {
//            JsonReader jsonIn = new JsonReader("in2.json");
//            jsonIn.statParse();
//
//        } catch (ParseException | java.text.ParseException e) {
//            System.out.println(e);
//        }

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