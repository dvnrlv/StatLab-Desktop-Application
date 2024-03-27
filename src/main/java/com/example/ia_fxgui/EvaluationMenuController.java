package com.example.ia_fxgui;


import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.services.CSVFileParser;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;

import java.io.IOException;

import static com.example.ia_fxgui.SceneController.showWindow;

public class EvaluationMenuController {

    @FXML
    private CheckBox statPearson;

    @FXML
    private CheckBox statKendall;

    @FXML
    private CheckBox statSpearman;

    @FXML
    private CheckBox statCovariance;

    @FXML
    private ChoiceBox<Integer> polynomialChoiceBox;

    @FXML
    private Button statCalc;

    @FXML
    private Button statSave;

    @FXML
    private TextField filenameDisplay;

    @FXML
    private Button exitToMenu;

    @FXML
    private LineChart<String, Number> resultLineChart;

    @FXML
    private Button showLargerChart;

    @FXML
    private Button plotDesmos;

    @FXML
    private Button producePDF;

    @FXML
    private ListView<String> resultListView;

    @FXML
    public void initialize() {
        // Initialize ChoiceBox
        polynomialChoiceBox.getItems().addAll(1, 2, 3, 4, 5);

        // Add event handlers
        statCalc.setOnAction(event -> calculateAndGraph());
        statSave.setOnAction(event -> saveDataSet());
        exitToMenu.setOnAction(event -> exitToMainMenu());
        showLargerChart.setOnAction(event -> showLargerChart());
        plotDesmos.setOnAction(event -> plotInDesmos());
        producePDF.setOnAction(event -> saveAsPDF());
    }

    // Event handlers
    private void calculateAndGraph() {
        System.out.println("Calculate and Graph button clicked.");
        // Add logic to calculate and graph data
    }

    private void saveDataSet() {
        System.out.println("Save Data Set button clicked.");

        DBManager.getInstance().getDatasetService().saveDataset(
                new Dataset(CSVFileParser.getDatasetNameWOExtension(), CSVFileParser.parsedDataSet)
        );
    }

    private void exitToMainMenu() {
        System.out.println("Exit to Main Menu button clicked.");
        try {
            showWindow("MainMenu.fxml", false, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Add logic to exit to main menu
    }

    private void showLargerChart() {
        System.out.println("Show Larger Chart button clicked.");
        // Add logic to show a larger chart
    }

    private void plotInDesmos() {
        System.out.println("Plot in Desmos button clicked.");
        // Add logic to plot data in Desmos

        new DesmosController().launchDesmos(new Double[][]{{1.0, 2.0}, {1.0, 2.0}, {2.0, 6.0}});
    }

    private void saveAsPDF() {
        System.out.println("Save as PDF button clicked.");
        // Add logic to save data as PDF
    }
}

