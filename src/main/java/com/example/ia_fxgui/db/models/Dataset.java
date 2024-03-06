package com.example.ia_fxgui.db.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Data
@Getter
@Setter
public class Dataset {
    private String name;
    private ArrayList<Point> points;
}
