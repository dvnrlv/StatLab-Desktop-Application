package com.example.ia_fxgui.login;

import com.example.ia_fxgui.SceneController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;


public class Login {


    @FXML
    private Button logInButton;

    @FXML
    private Label wrongLogIn;

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;


    private Stage stage;
    private Scene scene;

    public void userLogin(ActionEvent event) throws IOException {
        SceneController m = new SceneController();
        if ((username.getText().equals("dvnrlv")) && password.getText().equals("123")) {

            m.switchToSceneMain(event);
        } else if (username.getText().isEmpty() && password.getText().isEmpty()) {
            wrongLogIn.setText("Please enter your Password and Password");
        } else {
            wrongLogIn.setText("Wrong username or password");
        }

    }


}
