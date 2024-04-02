package com.example.ia_fxgui;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

public class ChartController {


    @FXML
    private LineChart<String, Number> chart;

    public void initializeChart(LineChart<?, ?> lineChart) {
        this.chart.setData(lineChart.getData());
        this.chart.setTitle(lineChart.getTitle());
        // You can set other properties as needed
    }

    // Your other methods

}
