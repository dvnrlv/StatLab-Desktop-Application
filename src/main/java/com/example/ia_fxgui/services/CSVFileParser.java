package com.example.ia_fxgui.services;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVFileParser {

    public static Double[][] parseCSV(String fileName) throws IOException, IllegalArgumentException {
        List<List<Double>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int expectedColumns = -1;
            int lineNumber = 0;
            while ((line = br.readLine()) != null) {
                lineNumber++;
                if (!line.trim().isEmpty()) { // Skip empty lines
                    List<Double> row = parseCSVLine(line, expectedColumns, lineNumber);
                    if (row != null) {
                        if (expectedColumns == -1) {
                            expectedColumns = row.size(); // Set the expected number of columns
                        }
                        data.add(row);
                    }
                }
            }
        }


        Double[][] dataSet = new Double[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            List<Double> row = data.get(i);
            dataSet[i] = new Double[row.size()];
            for (int j = 0; j < row.size(); j++) {
                if (row.get(j) != null) {
                    dataSet[i][j] = row.get(j);
                } else {
                    dataSet[i][j] = Double.NaN; // Set null values to Double.NaN
                }
            }
        }

        return dataSet;

    }

    private static List<Double> parseCSVLine(String line, int expectedColumns, int lineNumber) throws IllegalArgumentException {
        List<Double> row = new ArrayList<>();
        StringBuilder value = new StringBuilder();
        boolean insideQuotes = false;
        int columns = 0; // Counter for the number of columns

        for (char c : line.toCharArray()) {
            if (c == '"') {
                insideQuotes = !insideQuotes;
            } else if ((c == ',' || c == ';') && !insideQuotes) {
                try {
                    row.add(Double.parseDouble(value.toString().trim()));
                    columns++;
                } catch (NumberFormatException e) {
                    System.out.println("Non-numeric value found at line " + lineNumber + ": " + value.toString().trim());
                    row.add(null); // Add null if non-numeric value is found
                }
                value.setLength(0); // clear StringBuilder for next value
            } else {
                value.append(c);
            }
        }

        // Add the last value after the loop
        try {
            row.add(Double.parseDouble(value.toString().trim()));
            columns++;
        } catch (NumberFormatException e) {
            System.out.println("Non-numeric value found at line " + lineNumber + ": " + value.toString().trim());
            row.add(null); // Add null if non-numeric value is found
        }

        // Check if the number of columns matches the expected number
        if (expectedColumns != -1 && columns != expectedColumns) {
            // Handle cases where the number of columns doesn't match
            System.out.println("Number of columns does not match at line " + lineNumber);
            if (columns < expectedColumns) {
                // Add null values to match the expected number of columns
                for (int i = columns; i < expectedColumns; i++) {
                    row.add(null);
                }
            } else {
                // Trim the row to match the expected number of columns
                while (row.size() > expectedColumns) {
                    row.remove(row.size() - 1);
                }
            }
        }

        return row;
    }
}