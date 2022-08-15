package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.Connection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class MyJsonParser {
    JSONObject jsonObjectIn;
    JSONObject jsonObjectOut = null;
    Connection connection;

    public MyJsonParser(JSONObject jsonObjectIn, Connection connection) {
        this.jsonObjectIn = jsonObjectIn;
        this.connection = connection;
        this.jsonObjectOut = new JSONObject();
    }


    public void searchParse() throws NullPointerException {
        // получение массива
        jsonObjectOut.put("type", "search");
        JSONArray listOut = new JSONArray();

        JSONArray searchArray = (JSONArray) jsonObjectIn.get("criterias");
        Iterator i = searchArray.iterator();
        while (i.hasNext()) {
            JSONObject innerObj = (JSONObject) i.next();
            JSONObject objOut = new JSONObject();
            objOut.put("criteria", innerObj);
            JSONArray listOutResult = new JSONArray();

            if (innerObj.containsKey("lastName")) {
                String lastName = (String) innerObj.get("lastName");
                System.out.println(lastName);

            } else if (innerObj.containsKey("minTimes")) {
                String productName = (String) innerObj.get("productName");
                System.out.println(productName);
                long minTimes = (long) innerObj.get("minTimes");
                System.out.println(minTimes);

            } else if (innerObj.containsKey("minExpenses")) {
                long minExpenses = (long) innerObj.get("minExpenses");
                System.out.println(minExpenses);
                long maxExpenses = (long) innerObj.get("maxExpenses");
                System.out.println(maxExpenses);
            } else if (innerObj.containsKey("badCustomers")) {
                long badCustomers = (long) innerObj.get("badCustomers");
                System.out.println(badCustomers);
            } else {
                throw new NullPointerException("unknown criterias");
            }
            objOut.put("results", listOutResult);
            listOut.add(objOut);
        }
        jsonObjectOut.put("results", listOut);
    }

    public void statParse() throws ParseException {
        String startDateStr = (String) jsonObjectIn.get("startDate");
        String endDateStr = (String) jsonObjectIn.get("endDate");

        System.out.println(startDateStr);
        System.out.println(endDateStr);

        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");

        Date startDate = simpleDateFormat1.parse(startDateStr);
        Date endDate = simpleDateFormat1.parse(endDateStr);
        System.out.println(startDate);
        System.out.println(endDate);

//        "startDate": "2020-01-14", // Начальная дата
//        "endDate": "2020-01-26" // Конечная дата

    }
    //

//            // получение строки из объекта
//            String firstName = (String) jsonObject.get("firstname");
//            System.out.println("The first name is: " + firstName);

//            // получение номера из объекта
//            long id =  (long) jsonObject.get("id");
//            System.out.println("The id is: " + id);
//
//
//            // берем элементы массива
//            for(int i=0; i<lang.size(); i++){
//                System.out.println("The " + i + " element of the array: "+lang.get(i));
//            }
//            // берем каждое значение из массива json отдельно

//            // обрабатываем структуру в объекте
//            JSONObject structure = (JSONObject) jsonObject.get("job");
//            System.out.println("Into job structure, name: " + structure.get("name"));

//    } catch(
//    FileNotFoundException ex)
//
//    {
//        ex.printStackTrace();
//    } catch(
//    IOException ex)
//
//    {
//        ex.printStackTrace();
//    } catch(
//    ParseException ex)
//
//    {
//        ex.printStackTrace();
////        } catch (NullPointerException ex) {
////            ex.printStackTrace();
//    }


    // reader = new JsonReader(argsParser.getInputFile());

//            try {
//                String SQL_SELECT = "Select * from customers";
//                PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT);
//                ResultSet resultSet = preparedStatement.executeQuery();
//                while (resultSet.next()) {
//                    int id = resultSet.getInt("id");
//                    String name = resultSet.getString("firstName");
//                    System.out.println(id + " " + name);
//                }
//            } catch (SQLException e) {
//                System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }


    public JSONObject getJsonObjectOut() {
        return jsonObjectOut;
    }

}
