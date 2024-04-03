package com.example.ia_fxgui;


import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.services.CSVFileParser;
import com.example.ia_fxgui.services.DatasetStorage;
import com.example.ia_fxgui.services.Grapher;
import com.example.ia_fxgui.services.StatFunctions;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

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


    private TableColumn<StatFunctions.StatFunctionRow, String> nameColumn;


    private TableColumn<StatFunctions.StatFunctionRow, String> valueColumn;

    @FXML
    private TableView<StatFunctions.StatFunctionRow> resultStatArrayTable;

    @FXML
    private ChoiceBox<Integer> polynomialChoiceBox;
    private Integer[] polynomialChoiceBoxOptions = {1, 2, 3, 4, 5, 6};

    @FXML
    private Button statCalc;

    @FXML
    private Button statSave;

    @FXML
    private TextField filenameDisplay;

    @FXML
    private Button exitToMenu;

    @FXML
    private LineChart<Number, Number> resultLineChart;

    @FXML
    private Button showLargerChart;

    @FXML
    private Button plotDesmos;

    @FXML
    private Button producePDF;

    @FXML
    private ListView<String> resultListView;

    @FXML
    private TextField modelInput;

    @FXML
    private CheckBox statYMean;

    @FXML
    private CheckBox statYMedian;

    @FXML
    private CheckBox statYMax;

    @FXML
    private CheckBox statYMin;

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
        filenameDisplay.setText(DatasetStorage.getDataset().getName());

        statPearson.setOnAction(event -> handleStatPearson());
        statKendall.setOnAction(event -> handleStatKendall());
        statSpearman.setOnAction(event -> handleStatSpearman());
        statCovariance.setOnAction(event -> handleStatCovariance());
        statYMean.setOnAction(event -> handleStatYMean());
        statYMedian.setOnAction(event -> handleStatYMedian());
        statYMax.setOnAction(event -> handleStatYMax());
        statYMin.setOnAction(event -> handleStatYMin());

    }


    // Event handlers

    private void handleStatPearson() {
        System.out.println("Pearson's Correlation selected");
        StatFunctions.calculatePearsonsCorrelationCoefficient(DatasetStorage.getDataset().getPointsArray());
        // Implement your logic here
    }

    private void handleStatKendall() {
        System.out.println("Kendall's Correlation selected");
        StatFunctions.calculateKendallsCorrelationCoefficient(DatasetStorage.getDataset().getPointsArray());
        // Implement your logic here
    }

    private void handleStatSpearman() {
        System.out.println("Spearman's Correlation selected");
        StatFunctions.calculateSpearmansCorrelationCoefficient(DatasetStorage.getDataset().getPointsArray());
        // Implement your logic here
    }

    private void handleStatCovariance() {
        System.out.println("Covariance selected");
        StatFunctions.calculateCovarianceXY(DatasetStorage.getDataset().getPointsArray());
        // Implement your logic here
    }

    private void handleStatYMean() {
        System.out.println("Dependent Variable Mean selected");
        StatFunctions.calculateYMean(DatasetStorage.getDataset().getPointsArray());
        // Implement your logic here
    }

    private void handleStatYMedian() {
        System.out.println("Dependent Variable Median selected");
        StatFunctions.calculateYMedian(DatasetStorage.getDataset().getPointsArray());
        // Implement your logic here
    }

    private void handleStatYMax() {
        System.out.println("Dependent Variable Max selected");
        StatFunctions.calculateMaxY(DatasetStorage.getDataset().getPointsArray());
        // Implement your logic here
    }

    private void handleStatYMin() {
        System.out.println("Dependent Variable Min selected");
        StatFunctions.calculateMinY(DatasetStorage.getDataset().getPointsArray());
        // Implement your logic here
    }


    private static int selectedPolynomialValue = 0;

    public void getPolynomialDegree(ActionEvent event) {
        selectedPolynomialValue = polynomialChoiceBox.getValue(); // This will automatically unbox to int
        System.out.println("Selected value: " + selectedPolynomialValue);
    }


    private void calculateAndGraph() {
        System.out.println("Calculate and Graph button clicked.");
        // Add logic to calculate and graph data
        String bestFit = StatFunctions.fitPolynomial(selectedPolynomialValue, DatasetStorage.getDataset().getPointsArray());
        String model;
        if (modelInput.toString() != "" && modelInput.toString() != null) {
            model = modelInput.toString();
        } else {
            model = null;
        }
//        resultLineChart = Grapher.displayDataSet(DatasetStorage.getDataset().getPointsArray(), "x", "y", bestFit, model, DatasetStorage.getDataset().getName());
        //display resultStatArray and LineChart

        populateTable(StatFunctions.createResultArray(DatasetStorage.getDataset().getName()));
    }

    private void saveDataSet() {
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
            System.out.println("Show Larger Chart");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("largerChart.fxml"));
            Parent root = loader.load();


            String bestFit = StatFunctions.fitPolynomial(selectedPolynomialValue, DatasetStorage.getDataset().getPointsArray());
            String model;
            if (modelInput.toString() != "" && modelInput.toString() != null) {
                model = modelInput.toString();
            } else {
                model = null;
            }
            resultLineChart = Grapher.displayDataSet(DatasetStorage.getDataset().getPointsArray(), "x", "y", bestFit, model, DatasetStorage.getDataset().getName());


            LineChartController controller = loader.getController();
            controller.setLineChart(resultLineChart);

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static class LineChartController {
        @FXML
        private LineChart<Number, Number> lineChart;

        public void setLineChart(LineChart<Number, Number> chart) {
            lineChart.getData().addAll(chart.getData());
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

    public void populateTable(List<StatFunctions.StatFunctionRow> data) {
        resultStatArrayTable.getItems().clear(); // Clear existing items
        resultStatArrayTable.getItems().addAll(FXCollections.observableArrayList(data)); // Add new items
        nameColumn = new TableColumn<>("Statistical Function");
        nameColumn.setStyle("-fx-pref-width: 331");
        valueColumn = new TableColumn<>("Calculated Value");
        valueColumn.setStyle("-fx-pref-width: 301");

        // Set cell value factories after populating the table
        resultStatArrayTable.getColumns().clear();
        resultStatArrayTable.getColumns().add(nameColumn);
        resultStatArrayTable.getColumns().add(valueColumn);

        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        valueColumn.setCellValueFactory(new PropertyValueFactory<>("value"));
    }
}

