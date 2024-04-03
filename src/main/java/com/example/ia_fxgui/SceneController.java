package com.example.ia_fxgui;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.services.Grapher;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class SceneController {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private BorderPane registerPane;
    @FXML
    private Label welcomeText;

    @FXML
    private BorderPane loginPane;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField registerUsernameField;
    @FXML
    private PasswordField registerPasswordField;

    @FXML
    private Button popupButton;

    @FXML
    private AnchorPane scenePane;

    @FXML
    private void initialize() {
        DBManager.getInstance();

    }


    private static Stage currentStage = null;

    public static void showWindowFromFxml(String fxmlFile, boolean fullDisplay, boolean closePrevious) throws IOException {
        try {
            // Load FXML file
            Parent root = FXMLLoader.load(SceneController.class.getResource(fxmlFile));
            showWindowWithScene(new Scene(root), fullDisplay, closePrevious);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void showWindowWithScene(Scene scene, boolean fullDisplay, boolean closePrevious) {
        Stage newStage = new Stage();
        newStage.setScene(scene);

        // Close the current stage if it's open
        if (currentStage != null && closePrevious) {
            currentStage.close();
        }

        // Set fullscreen or resizable based on the parameter
        if (fullDisplay) {
            newStage.setFullScreen(!newStage.isFullScreen());
        } else {
            newStage.setResizable(false);
        }

        // Set the new stage as the current stage and show it
        currentStage = newStage;
        currentStage.show();
    }


    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (DBManager.getInstance().getAuthService().login(username, password)) {
            // Successful login, navigate to the main application screen
            // You can replace this with your application logic.
            ((Stage) usernameField.getScene().getWindow()).close();
            System.out.println("Login Successful");
            System.out.println(SceneController.class.getResource("MainMenu.fxml"));
            showWindowFromFxml("MainMenu.fxml", false, true);
            Main.WarningPopup.openPopup("Login Successful");

        } else {
            // Display an error message or handle unsuccessful login
            System.out.println("Login Failed");
            Main.WarningPopup.openPopup("Login Failed");
        }
    }

    @FXML
    public void showRegisterScreen(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("RegisterScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            //stage.setFullScreen(!stage.isFullScreen());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistration(ActionEvent event) {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();

        if (DBManager.getInstance().getAuthService().register(username, password)) {
            showLoginScreen(event);
        }
    }

    @FXML
    public void showLoginScreen(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Scene currentScene;

    // Setter methods to set the current Stage and Scene
    public static void setCurrentStage(Stage stage) {
        currentStage = stage;
    }



    public static void closeCurrentStage() {
        if (currentStage != null) {
            currentStage.close();
        }
    }

    // Method to close the current Scene
    public static void closeCurrentScene() {
        if (currentScene != null && currentStage != null) {
            currentStage.setScene(null);
        }
    }

    @FXML
    public void userLogout(ActionEvent event) {

        stage = (Stage) scenePane.getScene().getWindow();
        System.out.println("You successfully logged out");
        stage.close();
    }

    @FXML
    private TextArea textArea;

    public static void downloadAndDisplayGuide() {
        try {
            URL resourceUrl = SceneController.class.getResource("guide.txt");
            if (resourceUrl != null) {
                InputStream inputStream = resourceUrl.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                reader.close();

                // Create a new stage to display the guide text
                Stage guideStage = new Stage();
                TextArea guideTextArea = new TextArea();
                guideTextArea.setEditable(false);
                guideTextArea.setText(stringBuilder.toString());

                VBox root = new VBox(guideTextArea);
                VBox.setVgrow(guideTextArea, javafx.scene.layout.Priority.ALWAYS); // Ensure TextArea grows vertically
                Scene scene = new Scene(root, 600, 400);
                guideStage.setScene(scene);
                guideStage.setTitle("Guide");
                guideStage.show();
            } else {
                System.err.println("Resource not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public static void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    public static void exitApplication() {
        Platform.exit();
    }


    // Method to exit the application


}



