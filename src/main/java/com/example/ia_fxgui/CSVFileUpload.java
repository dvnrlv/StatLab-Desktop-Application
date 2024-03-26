package com.example.ia_fxgui;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class CSVFileUpload {

    public static String chooseCSVFile(Stage stage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose CSV File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            return selectedFile.getAbsolutePath(); // Return absolute path
        } else {
            return null;
        }
    }
}