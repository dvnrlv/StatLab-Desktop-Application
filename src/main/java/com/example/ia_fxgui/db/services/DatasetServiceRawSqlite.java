package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.db.models.Point;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DatasetServiceRawSqlite implements DatasetService {
    private static final String TABLE_NAME = "datasets";
    private static final String CREATION_STATEMENT = String.format(
            "CREATE TABLE IF NOT EXISTS %s" +
                    "(" +
                    "    name PRIMARY KEY" +
                    ");",
            TABLE_NAME
    );

    private static final String POINT_TABLE_CREATION_STATEMENT =
            "CREATE TABLE IF NOT EXISTS %s" +
                    "(" +
                    "    x INTEGER PRIMARY KEY," +
                    "    y INTEGER" +
                    ");";

    private static final String POINT_TABLE_INSERTION_STATEMENT =
            "INSERT INTO %s values %s";

    private static final String POINT_TABLE_DELETE_STATEMENT =
            "DELETE FROM %s";
    private static final String SELECT_POINTS_STATEMENT = "SELECT * from %s";
    private static final String SELECT_NAME_STATEMENT = String.format("SELECT * from %s", TABLE_NAME) + " WHERE name=%s";
    private static final String INSERT_NAME_STATEMENT = String.format("INSERT INTO %s", TABLE_NAME) + " values(%s)";


    public DatasetServiceRawSqlite() {
        DBManager.executeStatement(CREATION_STATEMENT);
    }

    @Override
    public void saveExistingDataset(Dataset dataset) throws SqlRowNotFoundException {
        if (checkIfDatasetWithNameExist(dataset.getName())) {
            throw new SqlRowNotFoundException("Dataset with provided name not found");
        }

        DBManager.executeStatement(String.format(POINT_TABLE_DELETE_STATEMENT, dataset.getName()));
        savePoints(dataset);
    }

    @Override
    public void saveNewDataset(Dataset dataset) {
        DBManager.executeStatement(String.format(INSERT_NAME_STATEMENT, dataset.getName()));
        savePoints(dataset);
    }

    private void savePoints(Dataset dataset) {
        DBManager.executeStatement(String.format(POINT_TABLE_CREATION_STATEMENT, dataset.getName()));
        String values = createRawDbValuesList(dataset);
        DBManager.executeStatement(String.format(POINT_TABLE_INSERTION_STATEMENT, dataset.getName(), values));
    }

    private static String createRawDbValuesList(Dataset dataset) {
        StringBuilder pointsForStatement = new StringBuilder();
        int pointsNumber = dataset.getPoints().size();
        for (int i = 0; i < pointsNumber; i++) {
            pointsForStatement.append('(')
                    .append(dataset.getPoints().get(i).toDbRaw())
                    .append(')');

            if (i < pointsNumber - 1) {
                pointsForStatement.append(',');
            }
        }

        return pointsForStatement.toString();
    }

    @Override
    public Dataset findDatasetByName(String name) throws SqlRowNotFoundException {
        if (!checkIfDatasetWithNameExist(name)) {
            throw new SqlRowNotFoundException("No dataset with provided");
        }

        Dataset dataset = new Dataset(name);
        try (ResultSet resultSet = DBManager.executeQuery(String.format(SELECT_POINTS_STATEMENT, name))) {
            while (resultSet.next()) {
                dataset.getPoints().add(
                        new Point(
                                resultSet.getLong("x"),
                                resultSet.getLong("y")
                        )
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dataset;
    }

    @Override
    public boolean checkIfDatasetWithNameExist(String name) {
        try (ResultSet resultSet = DBManager.executeQuery(String.format(SELECT_NAME_STATEMENT, name))) {
            return resultSet.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
