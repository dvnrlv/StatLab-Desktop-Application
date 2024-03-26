package com.example.ia_fxgui;

import javafx.concurrent.Worker;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class DesmosController {
    private final Stage stage;
    private WebEngine webEngine;

    public DesmosController() {
        stage = new Stage();
        setUpStage(setUpRoot());
    }

    private void setUpStage(VBox root) {
        Scene scene = new Scene(root);
        stage.setTitle("Desmos");
        stage.setScene(scene);
        stage.setWidth(800);
        stage.setHeight(590);
    }

    private VBox setUpRoot() {
        final WebView browser = new WebView();
        webEngine = browser.getEngine();

        VBox root = new VBox();
        root.setPadding(new Insets(5));
        root.setSpacing(5);
        root.getChildren().add(browser);
        return root;
    }


    public void launchDesmos() {
        stage.show();
        File file = new File(getClass().getResource("desmos.html").getFile());
        URL url;
        try {
            url = file.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Local URL: " + url);
        webEngine.load(url.toString());
    }

    public void launchDesmos(Double[][] points) {
        launchDesmos();
        addPointsWhenLoad(points);
    }

    public void addPointsWhenLoad(Double[][] points) {
        webEngine.getLoadWorker().stateProperty().addListener((ov, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                addPointsToLoadedView(points);
            }
        });
    }

    private void addPointsToLoadedView(Double[][] points) {
        webEngine.executeScript(
                String.format("""
                        calculator.setExpression({
                          type: 'table',
                          columns: [
                            {
                              latex: 'x',
                              values: %s
                            },
                            {
                              latex: 'y',
                              values: %s,
                              dragMode: Desmos.DragModes.XY,
                              columnMode: Desmos.ColumnModes.POINTS
                            }
                          ]
                        });""", arrayToDesmosString(points, 0), arrayToDesmosString(points, 1))
        );
    }

    private String arrayToDesmosString(Double[][] points, int dim) {
        StringBuilder result = new StringBuilder();
        result.append('[');
        for (Double[] point : points) {
            result.append(point[dim]).append(", ");
        }
        result.append(']');
        return result.toString();
    }
}
