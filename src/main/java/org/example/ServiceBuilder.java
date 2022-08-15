package org.example;

import java.io.IOException;

public class ServiceBuilder {

    public ServiceBuilder(ArgsParser argsParser, String dbDriver, String dbConnection, String dbUser, String dbPassword) throws IOException {
        JsonWriter writer = new JsonWriter(argsParser.getOutputFile()); // open output file
        try {
            MyJsonParser parser = new MyJsonParser( // open input file and open DB
                    (new JsonReader(argsParser.getInputFile())).getJsonObject(),
                    new MyDBConnection(dbDriver, dbConnection, dbUser, dbPassword));
            if (argsParser.isSearchOrStat()) {
                parser.searchParse(); // search mode
            } else {
                parser.statParse(); // stat mode
            }
            writer.writeJson(parser.getJsonObjectOut()); // save result
        } catch (Exception e) {
            writer.writeError(e.getMessage()); // if error - write in output file
            System.out.println(e.getMessage());
        }
    }
}
