package com.example.ia_fxgui.services;

import com.example.ia_fxgui.SceneController;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class Grapher {


    //with model and best fit

    public static void displayDataSet(LineChart<Number, Number> lineChart, Double[][] dataset, String xAxisName, String yAxisName, String model, String fileName) {

        fileName = fileName.replace(".csv", "");

        lineChart.getData().clear();

        lineChart.getXAxis().setLabel(xAxisName);
        lineChart.getYAxis().setLabel(yAxisName);

        lineChart.setTitle(fileName);




        try {
            String cssResourcePath = SceneController.class.getResource("chartStyle.css").toExternalForm();
            if (cssResourcePath != null) {
                lineChart.getStylesheets().add(cssResourcePath);
            } else {
                System.err.println("Resource not found: chartStyle.css");
            }
        } catch (NullPointerException e) {
            System.err.println("NullPointerException occurred: " + e.getMessage());
            e.printStackTrace();
        }

        //        dataSeries.getNode().setId("dataSeries");
        if (model != null) {

            XYChart.Series<Number, Number> modelSeries = new XYChart.Series<>();
            modelSeries.setName("Model: " + model);

//            modelSeries.getNode().setId("modelSeries");


            double minX = Double.POSITIVE_INFINITY;
            double maxX = Double.NEGATIVE_INFINITY;

            for (int i = 0; i < dataset.length; i++) {
                double x = dataset[i][0]; // X variable
                minX = Math.min(minX, x);
                maxX = Math.max(maxX, x);
            }
            System.out.println(minX+ ", "+ maxX);

            for (Double x = Math.floor(minX); x <= Math.ceil(maxX); x = x + (Math.ceil(maxX) - Math.floor(minX)) / 1000) {
                modelSeries.getData().add(new XYChart.Data<>(x, EquationReader.evaluate(model, x)));//need to write expression with postorder tree traversal
            }


            lineChart.getData().add(modelSeries);
            modelSeries.getNode().setId("modelSeries");
            modelSeries.getNode().setStyle("-fx-background: transparent;");

        }


        XYChart.Series<Number, Number> dataSeries = new XYChart.Series<>();

        for (int i = 0; i < dataset.length; i++) {
            dataSeries.getData().add(new XYChart.Data<>(dataset[i][0], dataset[i][1]));
            System.out.println(dataset[i][0]); // X variable
            System.out.println(dataset[i][1]); // Y variable
        }
        lineChart.getData().add(dataSeries);
        dataSeries.setName("Experimental Data");
        dataSeries.getNode().setStyle("-fx-stroke: transparent;" +
                "-fx-background: red;");



        //if (bestFit != null) {
          //  XYChart.Series<Number, Number> bestFitSeries = new XYChart.Series<>();
            //bestFitSeries.setName("Polynomial Fit: " + bestFit);
            //bestFitSeries.getNode().setId("bestFitSeries");

        //Double minX = Double.MAX_VALUE;
        //Double maxX = Double.MIN_VALUE;

            // find the minimum and maximum x values
        //for (Double[] data : dataset) {
        // for (Double x : data) {
        // if (x < minX) {
        //  minX = x;
        //  }
        //  if (x > maxX) {
        //  maxX = x;
        //  }
        // }
        // }

        // for (Double x = Math.floor(minX); x <= Math.ceil(maxX); x = x + (Math.ceil(maxX) - Math.floor(minX)) / 1000) {
        //  bestFitSeries.getData().add(new XYChart.Data<>(x, EquationReader.evaluate(model, x))); //need to write expression with postorder tree traversal
                //  }

        // lineChart.getData().add(bestFitSeries);
        //}
    }


    public static void saveChartAsImage(LineChart<Number, Number> chart, String fileName) {
        try {
            fileName = fileName.replace(".csv", "");
            fileName = fileName+".png";
            // Convert the chart to a buffered image
            javafx.scene.image.WritableImage image = chart.snapshot(null, null);
            java.awt.image.BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image, null);

            // Save the buffered image to a file
            File file = new File("src/main/resources/" + fileName); // Modify this path as needed
            ImageIO.write(bufferedImage, "png", file);

            System.out.println("Chart saved to: " + file.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to save chart: " + e.getMessage());
        }
    }


}
