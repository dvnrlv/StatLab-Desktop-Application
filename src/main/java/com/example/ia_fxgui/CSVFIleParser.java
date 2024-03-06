package com.example.ia_fxgui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


// GPT generated. Check & fix
public class CSVFIleParser {

    public static ArrayList<ArrayList<Integer>> parseCSV(String fileName) {
        ArrayList<ArrayList<Integer>> data = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int numberOfColumns = -1;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(";");
                ArrayList<Integer> row = new ArrayList<>();
                for (String value : values) {
                    row.add(Integer.parseInt(value.trim()));
                }
                if (numberOfColumns == -1) {
                    numberOfColumns = row.size();
                } else if (row.size() != numberOfColumns) {
                    throw new IllegalArgumentException("Row length does not match.");
                }
                data.add(row);
            }

            while ((line = br.readLine()) != null) {
                // Assuming semicolon as delimiter
                String[] values = line.split(";");
                ArrayList<Integer> row = new ArrayList<>();
                for (String value : values) {
                    // Trim to remove leading and trailing whitespace
                    value = value.trim();
                    // Check if value is a numeric
                    if (!value.matches("-?\\d+(\\.\\d+)?")) {
                        throw new NumberFormatException("Non-numeric value found: " + value);
                    }
                    row.add(Integer.parseInt(value));
                }
                if (numberOfColumns == -1) {
                    numberOfColumns = row.size();
                } else if (row.size() != numberOfColumns) {
                    throw new IllegalArgumentException("Row length does not match.");
                }
                data.add(row);
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Incorrect file: " + e.getMessage());
            e.printStackTrace();
            return null;

        }
        return data;
    }

}