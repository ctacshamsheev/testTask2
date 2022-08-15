package org.example;

import java.io.IOException;

public class ServiceBuilder {
    JsonWriter writer;

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
