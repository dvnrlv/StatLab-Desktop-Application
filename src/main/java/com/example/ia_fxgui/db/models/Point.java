package com.example.ia_fxgui.db.models;

public record Point(long x, long y) {

    public String toDbRaw() {
        return x + "," + y;
    }
}
