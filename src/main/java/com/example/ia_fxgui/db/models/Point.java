package com.example.ia_fxgui.db.models;

public record Point(double x, double y) {

    public String toDbRaw() {
        return x + "," + y;
    }
}
