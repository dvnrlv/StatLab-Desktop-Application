package com.example.ia_fxgui.services;

import com.example.ia_fxgui.db.models.Dataset;
import lombok.Getter;

public class DatasetStorage {
    @Getter
    private static volatile Dataset dataset;

    public static void setDataset(Dataset newSet) {
        dataset = newSet;
    }

    public static void setDataset(Double[][] points, String name) {
        dataset = new Dataset(name, points);
    }
}
