package org.example;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

public class MyJsonParser {
    JSONObject jsonObjectIn;
    JSONObject jsonObjectOut = null;
    MyDBConnection dbConnection;

    public MyJsonParser(JSONObject jsonObjectIn, MyDBConnection connection) {
        this.jsonObjectIn = jsonObjectIn;
        this.dbConnection = connection;
        this.jsonObjectOut = new JSONObject();
    }


    public void searchParse() throws SQLException {
        // получение массива
        jsonObjectOut.put("type", "search");
        JSONArray listOut = new JSONArray();

        JSONArray searchArray = (JSONArray) jsonObjectIn.get("criterias");
        Iterator i = searchArray.iterator();
        while (i.hasNext()) {
            JSONObject innerObj = (JSONObject) i.next();
            JSONObject objOut = new JSONObject();
            objOut.put("criteria", innerObj);
            JSONArray listOutResult;
            if (innerObj.containsKey("lastName")) {
                String lastName = (String) innerObj.get("lastName");
                listOutResult = dbConnection.getCriteriaLastName(lastName);
            } else if (innerObj.containsKey("minTimes")) {
                String productName = (String) innerObj.get("productName");
                long minTimes = (long) innerObj.get("minTimes");
                listOutResult = dbConnection.getCriteriaProductNameminTimes(productName, minTimes);
            } else if (innerObj.containsKey("minExpenses")) {
                long minExpenses = (long) innerObj.get("minExpenses");
                long maxExpenses = (long) innerObj.get("maxExpenses");
                listOutResult = dbConnection.getCriteriaminmaxExpenses(minExpenses, maxExpenses);
            } else if (innerObj.containsKey("badCustomers")) {
                long badCustomers = (long) innerObj.get("badCustomers");
                listOutResult = dbConnection.getCriteriaBadCustomers(badCustomers);
            } else {
                throw new IllegalArgumentException("unknown criterias: "+ innerObj );
            }
            objOut.put("results", listOutResult);
            listOut.add(objOut);
        }
        jsonObjectOut.put("results", listOut);
    }

    public void statParse() throws ParseException, SQLException {
        jsonObjectOut.put("type", "stat");
        String startDateStr = (String) jsonObjectIn.get("startDate");
        String endDateStr = (String) jsonObjectIn.get("endDate");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat1.parse(startDateStr);
        Date endDate = simpleDateFormat1.parse(endDateStr);
        long totalDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) +1;
        if (totalDays<=0 ) {
            throw new IllegalArgumentException("Error date: startDate > endDate");
        }

        jsonObjectOut.put("totalDays", totalDays);

        JSONArray listOut = dbConnection.getStatCustomers(startDateStr ,endDateStr);

        jsonObjectOut.put("customers", listOut);
        jsonObjectOut.put("totalExpenses", dbConnection.getStatTotalExpenses( startDateStr ,endDateStr));
        jsonObjectOut.put("avgExpenses", dbConnection.getStatAvgExpenses(startDateStr ,endDateStr));

    }

    public JSONObject getJsonObjectOut() {
        return jsonObjectOut;
    }

}
