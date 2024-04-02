package com.example.ia_fxgui.db.models;

import com.example.ia_fxgui.db.DBManager;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
public class Dataset {
    private String name;
    private String owner;
    private Date date;
    private List<Point> points;

    public Dataset(String name) {
        this.name = name;
        this.owner = DBManager.getInstance().getAuthService().getLoggedUserName();
        points = new ArrayList<>();
    }

    public Dataset(String name, String owner) {
        this.name = name;
        this.owner = owner;
        points = new ArrayList<>();
    }

    public Dataset(String name, List<Point> points) {
        this.name = name;
        this.owner = DBManager.getInstance().getAuthService().getLoggedUserName();
        this.points = points;
    }

    public Dataset(String name, Double[][] points) {
        this.name = name;
        this.owner = DBManager.getInstance().getAuthService().getLoggedUserName();
        this.points = new ArrayList<>();
        addPoints(points);
    }

    public void addPoints(Double[][] points) {
        for (Double[] point : points) {
            this.points.add(new Point(point[0], point[1]));
        }
    }

    public void setPoints(Double[][] points) {
        this.points.clear();
        addPoints(points);
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public double[][] getPointsArray() {
        Double[][] result = new Double[points.size()][2];
        for (int i = 0; i < result.length; i++) {
            result[i][0] = points.get(0).x();
            result[i][1] = points.get(0).y();
        }
        return result;
    }
}
