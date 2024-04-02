package com.example.ia_fxgui;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.services.DatasetStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.ia_fxgui.SceneController.showWindowFromFxml;

public class DBDatasetListScene extends Scene {

    private static final double WIDTH = 600;
    private static final double HEIGHT = 400;

    private Button exitToMenuButton = new Button("Exit to Main Menu");
    TableView<TableRow<String>> tb;

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
        Label dateLabel = new Label("_Date_");
        Button evalButton = new Button("Evaluate");
        Button deleteButton = new Button("Delete");
        String lastItem;

        public DatasetCell() {
            super();
            hbox.getChildren().addAll(label, dateLabel, pane, evalButton, deleteButton);
            hbox.setAlignment(Pos.CENTER);
            HBox.setHgrow(pane, Priority.ALWAYS);
            HBox.setMargin(label, new Insets(0, 10, 0, 0));
            HBox.setMargin(dateLabel, new Insets(0, 10, 0, 0));
            HBox.setMargin(evalButton, new Insets(5, 10, 5, 10));
            HBox.setMargin(deleteButton, new Insets(5, 0, 5, 10));
        }

        private void setupOrUpdateChildren() {
            setupDateLabel();
            setupEvalBtn();
            setupDeleteBtn();
        }

        private void setupDateLabel() {
            try {
                Date date = DBManager.getInstance().getDatasetService().findDatasetByName(datasetName).getDate();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                dateLabel.setText(
                        formatter.format(date)
                );
            } catch (SqlRowNotFoundException e) {
                throw new RuntimeException(e);
            }
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
                getListView().getItems().remove(getItem());
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
                setupOrUpdateChildren();
                lastItem = item;
                label.setText(item != null ? item : "<null>");
                setGraphic(hbox);
            }
        }
    }
}
