package com.example.ia_fxgui.db.models;

public record Point(Double x, Double y) {

    public String toDbRaw() {
        return x + "," + y;
    }
}
