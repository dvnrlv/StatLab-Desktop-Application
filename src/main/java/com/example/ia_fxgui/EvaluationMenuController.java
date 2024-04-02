package com.example.ia_fxgui;


import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.services.CSVFileParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.ia_fxgui.SceneController.showWindowFromFxml;

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
    private TableColumn resultStatArrayString;

    @FXML
    private TableColumn resultStatArrayDouble;

    @FXML
    private TableView resultStatArrayTable;

    @FXML
    private ChoiceBox<Integer> polynomialChoiceBox;
    private Integer[] polynomialChoiceBoxOptions = {1,2,3,4,5,6};

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

        // Add event handlers
        statCalc.setOnAction(event -> calculateAndGraph());
        statSave.setOnAction(event -> saveDataSet());
        exitToMenu.setOnAction(event -> exitToMainMenu());
        showLargerChart.setOnAction(event -> showLargerChart());
        plotDesmos.setOnAction(event -> plotInDesmos());
        producePDF.setOnAction(event -> saveAsPDF());
        polynomialChoiceBox.getItems().addAll(polynomialChoiceBoxOptions);
        polynomialChoiceBox.setOnAction(this::getPolynomialDegree);

    }


    // Event handlers


    public void getPolynomialDegree(ActionEvent event) {
        int selectedValue = polynomialChoiceBox.getValue(); // This will automatically unbox to int
        System.out.println("Selected value: " + selectedValue);
    }


    private void calculateAndGraph() {
        System.out.println("Calculate and Graph button clicked.");
        // Add logic to calculate and graph data

        //display resultStatArray and LineChart


    }

    private void saveDataSet()  {
        System.out.println("Save Data Set button clicked.");
        DBManager.getInstance().getDatasetService().saveDataset(
                new Dataset(CSVFileParser.getDatasetNameWOExtension(), CSVFileParser.parsedDataSet));
        Main.WarningPopup.openPopup("Data Set successfully saved");

    }


    private void exitToMainMenu() {
        System.out.println("Exit to Main Menu button clicked.");
        try {
            showWindowFromFxml("MainMenu.fxml", false, true);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Add logic to exit to main menu
    }

    private void showLargerChart() {
        System.out.println("Show Larger Chart button clicked.");
        // Add logic to show a larger chart

        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("largerChart.fxml"));
            Parent root = loader.load();

            // Get the controller instance
            ChartController chartController = loader.getController();

            // Pass the LineChart object to the controller
            chartController.initializeChart(resultLineChart);

            // Create a new stage
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Chart Window");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void plotInDesmos() {
        System.out.println("Plot in Desmos button clicked.");
        // Add logic to plot data in Desmos

        new DesmosController().launchDesmos(CSVFileParser.parsedDataSet);
    }

    private void saveAsPDF() {
        System.out.println("Save as PDF button clicked.");
        // Add logic to save data as PDF
    }




}

