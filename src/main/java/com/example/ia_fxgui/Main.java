package com.example.ia_fxgui;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.db.models.Point;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


    private static Stage stg;

    public static void main(String[] args) {
        launch();
        /*DBManager.getInstance().getAuthService().register("admin", "agile7");
        DBManager.getInstance().getAuthService().login("admin", "agile7");
        Dataset dataset = new Dataset("graph", "admin");
        dataset.getPoints().add(new Point(1, 1));
        DBManager.getInstance().getDatasetService().saveDataset(dataset);
        try {
            DBManager.getInstance().getDatasetService().findDatasetByName("graph");
        } catch (SqlRowNotFoundException e) {
            System.out.println(e.getMessage());
        }
        DBManager.getInstance().getDatasetService().findLoggedUserDatasetsNames().forEach(System.out::println);*/
    }

    @Override
    public void start(Stage primaryStage) {

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