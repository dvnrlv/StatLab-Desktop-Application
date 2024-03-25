package com.example.ia_fxgui.services;


        import com.example.ia_fxgui.PopupManager;
        import org.apache.commons.csv.CSVFormat;
        import org.apache.commons.csv.CSVParser;
        import org.apache.commons.csv.CSVRecord;

        import java.io.BufferedReader;
        import java.io.FileReader;
        import java.io.IOException;
        import java.util.ArrayList;

public class CSVFileParser {

    public static Double[][] parseCSV(String fileName) {
        ArrayList<ArrayList<Double>> data = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.withDelimiter(',').withDelimiter(';');
            CSVParser csvParser = new CSVParser(br, CSVFormat.DEFAULT);
            int numberOfColumns = -1;
            for (CSVRecord record : csvParser) {
                ArrayList<Double> row = new ArrayList<>();
                for (String value : record) {
                    try {
                        row.add(Double.parseDouble(value.trim()));
                    } catch (NumberFormatException e) {
                        PopupManager.getInstance().openPopup("Non-numeric value found: " + value);
                        System.err.println("Non-numeric value found: " + value);
                        row.add(null); // Add a placeholder for non-numeric value
                    }
                }
                if (numberOfColumns == -1) {
                    numberOfColumns = row.size();
                } else if (row.size() != numberOfColumns) {
                    PopupManager.getInstance().openPopup("Row length does not match.");
                    throw new IllegalArgumentException("Row length does not match.");
                }
                data.add(row);
            }
        } catch (IOException e) {
            PopupManager.getInstance().openPopup("Error reading file");
            System.err.println("Error reading file: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        Double[][] dataSet = new Double[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            ArrayList<Double> innerList = data.get(i);
            dataSet[i] = new Double[innerList.size()];
            for (int j = 0; j < innerList.size(); j++) {
                dataSet[i][j] = innerList.get(j);
            }
        }

        return dataSet;
    }
}








