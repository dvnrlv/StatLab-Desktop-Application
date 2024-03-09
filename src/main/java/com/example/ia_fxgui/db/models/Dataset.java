package com.example.ia_fxgui.db.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
public class Dataset {
    private String name;
    private List<Point> points;

    public Dataset(String name) {
        this.name = name;
        points = new ArrayList<>();
    }
}
