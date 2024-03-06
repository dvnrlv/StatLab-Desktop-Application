package com.example.ia_fxgui.db;


import com.example.ia_fxgui.db.services.DatasetService;
import com.example.ia_fxgui.db.services.DatasetServiceRawSqlite;
import com.example.ia_fxgui.db.services.UserService;
import com.example.ia_fxgui.db.services.UserServiceRawSqlite;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DBManager {
    private static final String DATABASE_URL = "jdbc:sqlite:ia_fxgui.db";

    private static volatile DBManager instance;

    @Getter
    private final DatasetService datasetService;
    @Getter
    private final UserService userService;

    public DBManager() {
        userService = new UserServiceRawSqlite();
        datasetService = new DatasetServiceRawSqlite();
    }

    public static DBManager getInstance() {
        DBManager localInstance = instance;
        if (localInstance == null) {
            synchronized (DBManager.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = localInstance = new DBManager();
                }
            }
        }

        return localInstance;
    }

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DATABASE_URL);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void executeStatement(String statement) {
        try (Connection connection = DBManager.getConnection();
             Statement dbStatement = connection.createStatement()) {
            dbStatement.execute(statement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
