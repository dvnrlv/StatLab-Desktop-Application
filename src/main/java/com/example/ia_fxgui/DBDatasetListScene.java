package com.example.ia_fxgui;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.services.DatasetStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.io.IOException;

import static com.example.ia_fxgui.SceneController.showWindowFromFxml;

public class DBDatasetListScene extends Scene {

    private static final double WIDTH = 600;
    private static final double HEIGHT = 400;

    private Button exitToMenuButton = new Button("Exit to Main Menu");

    public DBDatasetListScene() {
        super(new BorderPane(), WIDTH, HEIGHT);

        BorderPane root = (BorderPane) getRoot();
        ObservableList<String> datasetsNames = FXCollections.observableArrayList(
                DBManager.getInstance().getDatasetService().findLoggedUserDatasetsNames()
        );

        ListView<String> datasetsCellList = new ListView<>(datasetsNames);
        datasetsCellList.setCellFactory(param -> new DatasetCell());

        // Place the ListView in the center of the BorderPane
        root.setCenter(datasetsCellList);

        // Move the exitToMenuButton to the bottom-right corner
        root.setBottom(exitToMenuButton);
        BorderPane.setMargin(exitToMenuButton, new Insets(10));
        BorderPane.setAlignment(exitToMenuButton, Pos.BOTTOM_RIGHT);

        exitToMenuButton.setStyle("-fx-background-color: #5698db");
        exitToMenuButton.setOnMouseClicked(mouseEvent -> {
                    try {
                        showWindowFromFxml("MainMenu.fxml", false, true);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    static class DatasetCell extends ListCell<String> {
        String datasetName;
        HBox hbox = new HBox();
        Label label = new Label("_Dataset_Name_");
        Pane pane = new Pane();
        Button evalButton = new Button("Evaluate");
        Button deleteButton = new Button("Delete");
        String lastItem;

        public DatasetCell() {
            super();
            hbox.getChildren().addAll(label, pane, evalButton, deleteButton);
            HBox.setHgrow(pane, Priority.ALWAYS);

            setupEvalBtn();
            setupDeleteBtn();
        }

        private void setupEvalBtn() {
            evalButton.setOnAction(event -> {
                try {
                    DatasetStorage.setDataset(DBManager.getInstance().getDatasetService().findDatasetByName(datasetName));
                } catch (SqlRowNotFoundException e) {
                    throw new RuntimeException(e);
                }

                try {
                    showWindowFromFxml("EvaluationMenu.fxml", false, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            evalButton.setStyle("-fx-background-color: #e67a22");
        }

        private void setupDeleteBtn() {
            deleteButton.setOnAction(event -> {
                try {
                    DBManager.getInstance().getDatasetService().removeDatasetByName(datasetName);
                } catch (SqlRowNotFoundException e) {
                    throw new RuntimeException(e);
                }
            });

            deleteButton.setStyle("-fx-background-color: #e67a22");
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            datasetName = item;
            setText(null);
            if (empty) {
                lastItem = null;
                setGraphic(null);
            } else {
                lastItem = item;
                label.setText(item != null ? item : "<null>");
                setGraphic(hbox);
            }
        }
    }
}
