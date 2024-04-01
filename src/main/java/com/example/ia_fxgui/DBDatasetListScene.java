package com.example.ia_fxgui;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.services.DatasetStorage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

import static com.example.ia_fxgui.SceneController.showWindow;

public class DBDatasetListScene {
    static class XCell extends ListCell<String> {
        String datasetName;
        HBox hbox = new HBox();
        Label label = new Label("(empty)");
        Pane pane = new Pane();
        Button button = new Button("Evaluate");
        String lastItem;

        public XCell() {
            super();
            hbox.getChildren().addAll(label, pane, button);
            HBox.setHgrow(pane, Priority.ALWAYS);
            button.setOnAction(event -> {
                try {
                    DatasetStorage.setDataset(DBManager.getInstance().getDatasetService().findDatasetByName(datasetName));
                } catch (SqlRowNotFoundException e) {
                    throw new RuntimeException(e);
                }

                try {
                    showWindow("EvaluationMenu.fxml", false, true);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        }

        @Override
        protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            datasetName = item;
            setText(null);  // No text in label of super class
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

    public void start(Stage primaryStage) {
        StackPane pane = new StackPane();
        Scene scene = new Scene(pane, 600, 400);

        primaryStage.setScene(scene);

        ObservableList<String> datasetsNames = FXCollections.observableArrayList(
                DBManager.getInstance().getDatasetService().findLoggedUserDatasetsNames()
        );

        ListView<String> lv = new ListView<>(datasetsNames);
        lv.setCellFactory(param -> new XCell());

        pane.getChildren().add(lv);
        primaryStage.show();
    }
}
