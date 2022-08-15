package org.example;

public class ArgsParser {
    private String inputFile = null;
    private String outputFile = null;
    private boolean isSearchOrStat = true; // search = true; stat = false;

    ArgsParser(String[] args) throws IllegalArgumentException {
        if ((args != null) && (3 == args.length) && (args[0].equals("search") || args[0].equals("stat"))) {
            if (args[0].equals("stat")) {
                isSearchOrStat = false;
            }
            inputFile = args[1];
            outputFile = args[2];
        } else {
            throw new IllegalArgumentException("Error argument, use: <search|stat> <input-file> <output-file>");
        }
    }

    public String getInputFile() {
        return inputFile;
    }

    public String getOutputFile() {
        return outputFile;
    }

    public boolean isSearchOrStat() {
        return isSearchOrStat;
    }

}
