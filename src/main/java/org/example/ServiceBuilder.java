package org.example;

import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ServiceBuilder {
    JsonWriter writer;
    ArgsParser argsParser;

    public ServiceBuilder(ArgsParser argsParser, String dbDriver, String dbConnection, String dbUser, String dbPassword) throws IOException {
        writer = new JsonWriter(argsParser.getOutputFile());
        try {
            MyJsonParser parser = new MyJsonParser(
                    (new JsonReader(argsParser.getInputFile())).getJsonObject(),
                    new MyDBConnection(dbDriver, dbConnection, dbUser, dbPassword));
            if (argsParser.isSearchOrStat()) {
                parser.searchParse();
            } else {
                parser.statParse();
            }
            writer.writeJson(parser.getJsonObjectOut());
        } catch (Exception e) {
            // | IOException | ParseException
            writer.writeError(e.getMessage());
            System.out.println(e.getMessage());
        }

    }
}
