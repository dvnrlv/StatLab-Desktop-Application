package com.example.ia_fxgui;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class PopupManager {
    private static PopupManager instance;
    private Stage primaryStage;
    private Stage popupStage;

    private PopupManager() {}

    public static synchronized PopupManager getInstance() {
        if (instance == null) {
            instance = new PopupManager();
        }
        return instance;
    }

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public void openPopup(String text) {
        if (popupStage == null) {
            popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.setTitle("Popup Window");

            Label label = new Label(text);
            Button closeButton = new Button("Close Popup");
            closeButton.setOnAction(e -> closePopup());

            StackPane layout = new StackPane();
            layout.getChildren().addAll(label, closeButton);

            popupStage.setScene(new Scene(layout, 300, 150));
        } else {
            Label label = (Label) ((StackPane) popupStage.getScene().getRoot()).getChildren().get(0);
            label.setText(text);
        }

        popupStage.showAndWait();
    }

    private void closePopup() {
        if (popupStage != null) {
            popupStage.close();
        }
    }
}
