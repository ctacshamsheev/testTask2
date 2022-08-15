package org.example;

import org.json.simple.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter {
    private FileWriter writer = null;
    private boolean isClose = false;

    public JsonWriter(String outputFileName) throws IOException {
        writer = new FileWriter(outputFileName);
    }

    public void writeJson(JSONObject obj) throws IOException {
        saveAndCloseWriter(obj);
    }

    public void writeError(String message) throws IOException {
        JSONObject obj = new JSONObject();
        obj.put("type", "error");
        obj.put("message", message);
        saveAndCloseWriter(obj);
    }

    private void saveAndCloseWriter(JSONObject obj) throws IOException {
        if (!isClose) {
            System.out.println(obj.toJSONString());
            writer.write(obj.toJSONString());
            writer.close();
            isClose = true;
        } else {
            System.out.println("Error: Writer Closed");
            throw new IOException("Error: Writer Closed");
        }
    }

}
