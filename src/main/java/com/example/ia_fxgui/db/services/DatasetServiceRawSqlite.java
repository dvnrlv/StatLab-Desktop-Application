package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.db.models.Point;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatasetServiceRawSqlite implements DatasetService {
    public DatasetServiceRawSqlite() {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS datasets" +
                    "(" +
                    "    name PRIMARY KEY," +
                    "    owner TEXT" +
                    ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveDataset(Dataset dataset) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO datasets values (?, ?)")) {
            statement.setString(1, dataset.getName());
            statement.setString(2, DBManager.getInstance().getAuthService().getLoggedUser());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        savePoints(dataset);
    }

    @Override
    public List<String> findLoggedUserDatasetsNames() {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT name from datasets WHERE owner=?")) {
            statement.setString(1, DBManager.getInstance().getAuthService().getLoggedUser());
            ResultSet resultSet = statement.executeQuery();
            List<String> datasetName = new ArrayList<>();
            while (resultSet.next()) {
                datasetName.add(resultSet.getString("name"));
            }
            return datasetName;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void savePoints(Dataset dataset) {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            String statementText = String.format("DROP TABLE %s;", dataset.getName());
            statement.execute(statementText);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            String statementText = String.format("CREATE TABLE IF NOT EXISTS %s" +
                    "(" +
                    "    x INTEGER PRIMARY KEY," +
                    "    y INTEGER" +
                    ");", dataset.getName());
            statement.execute(statementText);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String values = createRawDbValuesList(dataset);
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            String statementText = String.format("INSERT into %s values %s", dataset.getName(), values);
            statement.execute(statementText);

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

        Dataset dataset = new Dataset(name, DBManager.getInstance().getAuthService().getLoggedUser());
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
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM datasets WHERE name=? and owner=?"
             );
        ) {
            statement.setString(1, name);
            statement.setString(2, DBManager.getInstance().getAuthService().getLoggedUser());
            ResultSet resultSet = statement.executeQuery();
            boolean doesExist = resultSet.next();
            resultSet.close();
            return doesExist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
