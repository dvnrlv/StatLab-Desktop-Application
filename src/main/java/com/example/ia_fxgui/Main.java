package com.example.ia_fxgui;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.db.models.Point;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;



public class Main extends Application {


    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }



    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInScene.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Login and Registration");
            //primaryStage.setMaximized(true);
            //primaryStage.setFullScreen(!primaryStage.isFullScreen());
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace(); // Print the exception details for debugging
            // Handle the exception or display an error message
        }
    }

    public static void main(String[] args) {
        launch();
    }
}

    class WarningPopup {

        public static void openPopup(String customText) {
            Stage primaryStage = Main.getPrimaryStage(); // Access the main stage

            // Create a new stage for the popup window
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.WINDOW_MODAL);
            popupStage.initOwner(primaryStage);


            // Create components for the popup window
            Label label = new Label(customText);
            Button closeButton = new Button("Close");
            closeButton.setOnAction(e -> popupStage.close());

            // Layout for the popup window
            VBox vBox = new VBox(10, label, closeButton);
            vBox.setAlignment(Pos.CENTER);

            // Set scene and show the popup window
            popupStage.setScene(new Scene(vBox, 250, 150));
            popupStage.setTitle("Warning");

            try {
                Scene scene = popupStage.getScene();
                scene.getStylesheets().add(WarningPopup.class.getResource("popup-style.css").toExternalForm());
                scene.getRoot().getStyleClass().add("popup-scene");
            } catch (Exception e) {
                e.printStackTrace();
            }

            popupStage.show();
        }
    }
