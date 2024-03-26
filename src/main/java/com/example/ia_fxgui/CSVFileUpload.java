package com.example.ia_fxgui;


import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.db.models.Point;
import com.example.ia_fxgui.services.CSVFileParser;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;


public class CSVFileUpload extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("CSV File Upload");

        // Create a FileChooser for CSV files
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new ExtensionFilter("CSV Files", "*.csv"));

        Button uploadButton = new Button("Upload CSV File");
        Label statusLabel = new Label();

        uploadButton.setOnAction(e -> {
            File selectedFile = fileChooser.showOpenDialog(primaryStage);
            if (selectedFile != null) {
                String fileName = selectedFile.getName();
                if (fileName.endsWith(".csv")) {


                    statusLabel.setText("Selected File: " + selectedFile.getName());

                    //CSV Parsing:

                    System.out.println(selectedFile.getName());

                    System.out.println(selectedFile.getPath());

                    try {
                        Double[][] dataSetArray = CSVFileParser.parseCSV(selectedFile.getPath());


                        Dataset dataset = new Dataset(fileName);
                        for (int i = 0; i < dataSetArray.length; i++) {
                            dataset.getPoints().add(new Point(dataSetArray[i][0], dataSetArray[i][1]));
                        }
                        DBManager.getInstance().getDatasetService().saveDataset(dataset); //saving dataSet to DB


                        // Print the contents of the array in a double[][] format
                        System.out.print("[");
                        for (int i = 0; i < dataSetArray.length; i++) {
                            System.out.print("[");
                            for (int j = 0; j < dataSetArray[i].length; j++) {
                                System.out.print(dataSetArray[i][j]);
                                if (j < dataSetArray[i].length - 1) {
                                    System.out.print(", ");
                                }
                            }
                            System.out.print("]");
                            if (i < dataSetArray.length - 1) {
                                System.out.print(", ");
                            }
                        }
                        System.out.println("]");

                    } catch (IOException | IllegalArgumentException f) {
                        f.printStackTrace();
                    }




                    try {
                        if (CSVFileParser.parseCSV(selectedFile.getPath()) != null) {
                            openNextWindow();
                        } else {
                            statusLabel.setText("Incorrect file type. Refer to manual for formatting");
                        }
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                } else {
                    statusLabel.setText("Incorrect file type. Please select a CSV file.");
                }
            }
        });

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(uploadButton, statusLabel);

        Scene scene = new Scene(layout, 300, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openNextWindow() {
        // Create a new stage for the next window
        Stage nextStage = new Stage();
        nextStage.setTitle("Next Window");

        // Create the content for the next window
        Label nextLabel = new Label("Next window content goes here.");

        // Layout for the next window
        VBox nextLayout = new VBox(10);
        nextLayout.getChildren().add(nextLabel);
        nextLayout.setAlignment(Pos.CENTER);

        Scene nextScene = new Scene(nextLayout, 400, 200);
        nextStage.setScene(nextScene);
        nextStage.show();
    }
}
