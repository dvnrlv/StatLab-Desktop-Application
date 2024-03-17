package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.db.models.Point;

import java.sql.*;

public class DatasetServiceRawSqlite implements DatasetService {
    private static final String POINT_TABLE_CREATION_STATEMENT =
            ;

    public DatasetServiceRawSqlite() {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS datasets" +
                    "(" +
                    "    name PRIMARY KEY" +
                    ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveDataset(Dataset dataset) {
        DBManager.executeStatement(String.format(POINT_TABLE_DELETE_STATEMENT, dataset.getName()));
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO datasets values (?)")) {
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        savePoints(dataset);
    }

    private static void savePoints(Dataset dataset) {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(String.format("CREATE TABLE IF NOT EXISTS %s" +
                    "(" +
                    "    x INTEGER PRIMARY KEY," +
                    "    y INTEGER" +
                    ");", dataset.getName()));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String values = createRawDbValuesList(dataset);
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(String.format("INSERT into %s values (%s)", dataset.getName(), values));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT * from %s", name));
            parseDatasetFromResultSet(resultSet, dataset);
            resultSet.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return dataset;
    }

    private static void parseDatasetFromResultSet(ResultSet resultSet, Dataset dataset) throws SQLException {
        while (resultSet.next()) {
            dataset.getPoints().add(
                    new Point(
                            resultSet.getLong("x"),
                            resultSet.getLong("y")
                    )
            );
        }
    }

    @Override
    public boolean checkIfDatasetWithNameExist(String name) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM datasets WHERE name=?");
        ) {
            statement.setString(0, name);
            ResultSet resultSet = statement.executeQuery();
            boolean doesExist = resultSet.next();
            resultSet.close();
            return doesExist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
