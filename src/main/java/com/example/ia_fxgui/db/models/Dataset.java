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
    private String owner;
    private List<Point> points;

    public Dataset(String name, String owner) {
        this.name = name;
        this.owner = owner;
        points = new ArrayList<>();
    }
}
