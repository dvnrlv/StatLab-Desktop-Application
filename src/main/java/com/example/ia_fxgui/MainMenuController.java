package com.example.ia_fxgui;
import com.example.ia_fxgui.db.DBManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.time.LocalDate;


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
            showGuide.setOnAction(event -> showGuide());
            logOutButton.setOnAction(event -> logOut());
        }
    }

    // Action methods for buttons
    private void showEvaluateMenu() {
        // Code to show the evaluate menu stage or window
        System.out.println("Show Evaluate Menu");
    }

    private void showDB() {
        // Code to show the DB stage or window
        System.out.println("Show DB");
    }

    private void showGuide() {
        // Code to show the guide stage or window
        System.out.println("Show Guide");
    }

    private void logOut() {
        // Code to log out the user
        System.out.println("Log Out");
        Stage stage = (Stage) logOutButton.getScene().getWindow();
        DBManager.getInstance().getAuthService().logout();

        stage.close(); // Close the current stage (window)
    }



}
