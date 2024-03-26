package com.example.ia_fxgui;

import com.example.ia_fxgui.db.DBManager;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

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


    public void switchToSceneMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("main-menu.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setFullScreen(!stage.isFullScreen());
        stage.show();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (DBManager.getInstance().getAuthService().login(username, password)) {
            // Successful login, navigate to the main application screen
            // You can replace this with your application logic.
            System.out.println("Login Successful");
        } else {
            // Display an error message or handle unsuccessful login
            System.out.println("Login Failed");
            WarningPopup.openPopup("Login Failed");
        }
    }

    @FXML
    public void showRegisterScreen(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("RegisterScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(!stage.isFullScreen());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistration(ActionEvent event) {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();

        DBManager.getInstance().getAuthService().register(username, password);

        // After successful registration, you can navigate back to the login screen
        showLoginScreen(event);
    }

    @FXML
    public void showLoginScreen(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setFullScreen(!stage.isFullScreen());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Stage currentStage;
    private static Scene currentScene;

    // Setter methods to set the current Stage and Scene
    public static void setCurrentStage(Stage stage) {
        currentStage = stage;
    }

    public static void setCurrentScene(Scene scene) {
        currentScene = scene;
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
    public static void exitApplication(ActionEvent event){
        Platform.exit();
    }

    public static void exitApplication() {
        Platform.exit();
    }



    // Method to exit the application





}



