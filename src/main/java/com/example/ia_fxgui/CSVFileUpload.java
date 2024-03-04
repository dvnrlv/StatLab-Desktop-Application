package com.example.ia_fxgui;



import javafx.application.Application;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import java.io.File;
import javafx.geometry.*;
import javafx.stage.FileChooser.ExtensionFilter;



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

                                   System.out.println(CSVFIleParser.parseCSV(selectedFile.getPath()));

                                   if(CSVFIleParser.parseCSV(selectedFile.getPath())!=null){
                                          openNextWindow();
                                   }
                                   else{
                                          statusLabel.setText("Incorrect file type. Refer to manual for formatting");
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
