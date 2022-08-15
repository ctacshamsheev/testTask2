package org.example;


import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;


public class JsonReader {

    JSONObject jsonObject = null;


    JsonReader(String inputFile) throws IOException, ParseException {
        // считывание файла JSON
        FileReader reader = new FileReader(inputFile);
        JSONParser jsonParser = new JSONParser();
        jsonObject = (JSONObject) jsonParser.parse(reader);
    }

    public JSONObject getJsonObject() {
        return jsonObject;
    }
}
