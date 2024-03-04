package com.example.ia_fxgui;

import com.example.ia_fxgui.login.SQLiteDataBaseController;
import com.example.ia_fxgui.login.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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



    public void switchToSceneMain(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.show();
    }




    public void initialize() {
        SQLiteDataBaseController.initializeDatabase();
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (UserManager.loginUser(username, password)) {
            // Successful login, navigate to the main application screen
            // You can replace this with your application logic.
            System.out.println("Login Successful");
        } else {
            // Display an error message or handle unsuccessful login
            System.out.println("Login Failed");
        }
    }


    public void showRegisterScreen (ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("RegisterScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRegistration(ActionEvent event) {
        String username = registerUsernameField.getText();
        String password = registerPasswordField.getText();

        UserManager.registerUser(username, password);

        // After successful registration, you can navigate back to the login screen
        showLoginScreen(event);
    }

    public void showLoginScreen (ActionEvent event){
        try{
            Parent root = FXMLLoader.load(getClass().getResource("LoginScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.setMaximized(true);
            stage.show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private AnchorPane scenePane;

    public void userLogout(ActionEvent event){

        stage = (Stage) scenePane.getScene().getWindow();
        System.out.println("You successfully logged out");
        stage.close();
    }





}



