package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.Dataset;
import com.example.ia_fxgui.db.models.Point;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DatasetServiceRawSqlite implements DatasetService {
    public DatasetServiceRawSqlite() {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS datasets" +
                    "(" +
                    "    name PRIMARY KEY," +
                    "    owner TEXT," +
                    "    date TIMESTAMP" +
                    ");");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void saveDataset(Dataset dataset) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT OR IGNORE INTO datasets values (?, ?, ?)")) {
            statement.setString(1, dataset.getName());
            statement.setString(2, DBManager.getInstance().getAuthService().getLoggedUserName());
            statement.setString(3, LocalDate.now().toString());
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
            statement.setString(1, DBManager.getInstance().getAuthService().getLoggedUserName());
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
        dropPointsTable(dataset.getName());
        createPointsTable(dataset.getName());
        insertPointsIntoTable(dataset);
    }

    private static void insertPointsIntoTable(Dataset dataset) {
        String values = createRawDbValuesList(dataset);
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            String statementText = String.format("INSERT into %s values %s", dataset.getName(), values);
            statement.execute(statementText);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void createPointsTable(String name) {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            String statementText = String.format("CREATE TABLE IF NOT EXISTS %s" +
                    "(" +
                    "    id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    x REAL," +
                    "    y REAL" +
                    ");", name);
            statement.execute(statementText);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void dropPointsTable(String name) {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            String statementText = String.format("DROP TABLE IF EXISTS %s;", name);
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
                    .append("NULL, ")
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
            throw new SqlRowNotFoundException("No dataset with provided name");
        }

        String owner = DBManager.getInstance().getAuthService().getLoggedUserName();
        Dataset dataset = new Dataset(name, owner);
        dataset.setPoints(getDatasetPoints(name));
        dataset.setDate(getDatasetDate(name, owner));

        return dataset;
    }

    private static List<Point> getDatasetPoints(String name) {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(String.format("SELECT x, y from %s", name));
            List<Point> points = parseDatasetFromResultSet(resultSet);
            resultSet.close();
            return points;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Date getDatasetDate(String name, String owner) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT date from datasets WHERE name=? and owner=?")) {
            statement.setString(1, name);
            statement.setString(2, owner);

            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                return null;
            }

            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            return formatter.parse(resultSet.getString("date"));
        } catch (SQLException | ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Point> parseDatasetFromResultSet(ResultSet resultSet) throws SQLException {
        List<Point> points = new ArrayList<>();
        while (resultSet.next()) {
            points.add(
                    new Point(
                            resultSet.getLong("x"),
                            resultSet.getLong("y")
                    )
            );
        }
        return points;
    }

    @Override
    public boolean checkIfDatasetWithNameExist(String name) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM datasets WHERE name=? and owner=?"
             )
        ) {
            statement.setString(1, name);
            statement.setString(2, DBManager.getInstance().getAuthService().getLoggedUserName());
            ResultSet resultSet = statement.executeQuery();
            boolean doesExist = resultSet.next();
            resultSet.close();
            return doesExist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void removeDatasetByName(String name) throws SqlRowNotFoundException {
        if (!checkIfDatasetWithNameExist(name)) {
            throw new SqlRowNotFoundException("No dataset with provided name");
        }

        dropPointsTable(name);
        removeDatasetFromUser(name);
    }

    private void removeDatasetFromUser(String datasetName) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "DELETE FROM datasets WHERE name=? and owner=?"
             );
        ) {
            statement.setString(1, datasetName);
            statement.setString(2, DBManager.getInstance().getAuthService().getLoggedUserName());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
