package com.example.ia_fxgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    private static Stage stg;

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("LogInScene.fxml"));
            primaryStage.setScene(new Scene(loader.load()));
            primaryStage.setTitle("Login and Registration");
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace(); // Print the exception details for debugging
            // Handle the exception or display an error message
        }
    }
}