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
        this.jsonObjectIn = jsonObjectIn; // input file in json object
        this.dbConnection = connection; // DB, connect and select
        this.jsonObjectOut = new JSONObject(); // result for output file
    }

    // search mode
    public void searchParse() throws SQLException {
        // получение массива
        jsonObjectOut.put("type", "search");
        JSONArray listOut = new JSONArray();
        JSONArray searchArray = (JSONArray) jsonObjectIn.get("criterias");
        Iterator i = searchArray.iterator();
        while (i.hasNext()) { // for each line - parse criteria
            JSONObject innerObj = (JSONObject) i.next();
            JSONObject objOut = new JSONObject();
            objOut.put("criteria", innerObj);
            JSONArray listOutResult;
            if (innerObj.containsKey("lastName")) {
                // 1.	Фамилия — поиск покупателей с этой фамилией
                String lastName = (String) innerObj.get("lastName");
                listOutResult = dbConnection.getCriteriaLastName(lastName);
            } else if (innerObj.containsKey("minTimes")) {
                // 2.	Название товара и число раз —
                // поиск покупателей, купивших этот товар не менее, чем указанное число раз
                String productName = (String) innerObj.get("productName");
                long minTimes = (long) innerObj.get("minTimes");
                listOutResult = dbConnection.getCriteriaProductNameminTimes(productName, minTimes);
            } else if (innerObj.containsKey("minExpenses")) {
                // 3.	Минимальная и максимальная стоимость всех покупок —
                // поиск покупателей, у которых общая стоимость всех покупок за всё время попадает в интервал
                long minExpenses = (long) innerObj.get("minExpenses");
                long maxExpenses = (long) innerObj.get("maxExpenses");
                listOutResult = dbConnection.getCriteriaminmaxExpenses(minExpenses, maxExpenses);
            } else if (innerObj.containsKey("badCustomers")) {
                // 4.	Число пассивных покупателей —
                // поиск покупателей, купивших меньше всего товаров. Возвращается не более, чем указанное число покупателей
                long badCustomers = (long) innerObj.get("badCustomers");
                listOutResult = dbConnection.getCriteriaBadCustomers(badCustomers);
            } else {
                // error
                throw new IllegalArgumentException("unknown criterias: " + innerObj);
            }
            objOut.put("results", listOutResult);
            listOut.add(objOut);
        }
        jsonObjectOut.put("results", listOut);
    }

    // stat mode
    public void statParse() throws ParseException, SQLException {
        jsonObjectOut.put("type", "stat");
        String startDateStr = (String) jsonObjectIn.get("startDate");
        String endDateStr = (String) jsonObjectIn.get("endDate");
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = simpleDateFormat1.parse(startDateStr);
        Date endDate = simpleDateFormat1.parse(endDateStr);
        long totalDays = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) + 1;
        // Общее число дней за период из двух дат, включительно, без выходных
        if (totalDays <= 0) {
            throw new IllegalArgumentException("Error date: startDate > endDate");
        }
        jsonObjectOut.put("totalDays", totalDays);
        JSONArray listOut = dbConnection.getStatCustomers(startDateStr, endDateStr);
        // Данные по покупателям за этот период, упорядоченные по общей стоимости покупок по убыванию
        // Общая стоимость покупок этого покупателя за период (то есть сумма всех стоимостей покупок всех товаров)
        jsonObjectOut.put("customers", listOut);
        jsonObjectOut.put("totalExpenses", dbConnection.getStatTotalExpenses(startDateStr, endDateStr));
        // Сумма покупок всех покупателей за период
        jsonObjectOut.put("avgExpenses", dbConnection.getStatAvgExpenses(startDateStr, endDateStr));
        // Средние затраты всех покупателей за период

    }

    public JSONObject getJsonObjectOut() {
        return jsonObjectOut;
    }

}
