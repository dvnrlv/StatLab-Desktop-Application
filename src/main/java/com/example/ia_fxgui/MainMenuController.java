package com.example.ia_fxgui;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.services.CSVFileParser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDate;

import static com.example.ia_fxgui.SceneController.showWindowFromFxml;


public class MainMenuController {

    @FXML
    private Label welcomeText1;

    @FXML
    private Label dateDisplay;

    @FXML
    private Label usernameDisplay;

    @FXML
    private Button showEvaluateMenu;

    @FXML
    private Button showDB;

    @FXML
    private Button showGuide;

    @FXML
    private Button logOutButton;

    // Initialize method called after FXML fields are injected
    public void initialize() {
        if (dateDisplay == null) {
            System.out.println("dateDisplay is null");
        } else {
            // Set dateDisplay and usernameDisplay (You might want to get values dynamically)
            dateDisplay.setText("Date: " + String.valueOf(LocalDate.now()));
            usernameDisplay.setText(DBManager.getInstance().getAuthService().getLoggedUserName());

            // Wire up event handlers for buttons
            showEvaluateMenu.setOnMouseClicked(event -> showEvaluateMenu());
            showDB.setOnAction(event -> showDB());
            showGuide.setOnAction(event -> {
                try {
                    showGuide();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            logOutButton.setOnAction(event -> {
                try {
                    logOut();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    // Action methods for buttons
    private void showEvaluateMenu() {
        // Code to show the evaluate menu stage or window

        Stage stage = new Stage(); // Create a new stage (you can pass your existing stage if you have access to it)
        String filePath = CSVFileUpload.chooseCSVFile(stage);
        // Process the selected CSV file
        if (filePath != null) {
            System.out.println("Selected CSV file path: " + filePath);
            try {
                Double[][] dataSet = CSVFileParser.parseCSV(filePath);
                // Now you have the parsed data set, you can use it as needed
                // For example, you can pass it to another method or display it in your application
                // Example:

                for (Double[] row : dataSet) {
                    for (Double value : row) {
                        System.out.print(value + " ");
                    }
                    System.out.println();
                }

            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
                // Handle the exception as needed
            }
            System.out.println("Show Evaluate Menu");
            try {
                showWindowFromFxml("EvaluationMenu.fxml", false, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        else {
            System.out.println("No file selected.");
            try {
                showWindowFromFxml("MainMenu.fxml", false, true);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Main.WarningPopup.openPopup("No file selected.");

        }
    }

    private void showDB() {
        Stage stage = new Stage(); // Create a new stage (you can pass your existing stage if you have access to it)
        SceneController.closeCurrentStage();
        try {
            SceneController.showWindowWithScene(new DBDatasetListScene(), false, true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void showGuide() throws IOException {
        // Code to show the guide stage or window
        System.out.println("Show Guide");
        // Download and display the guide text
        SceneController.downloadAndDisplayGuide();
    }

    private void logOut() throws IOException {
        // Code to log out the user
        System.out.println("Log Out");
        showWindowFromFxml("LogInScene.fxml", false, true);
        // Close the current stage (window)
    }


}
