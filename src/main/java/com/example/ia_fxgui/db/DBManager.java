package com.example.ia_fxgui.db;


import com.example.ia_fxgui.db.services.*;
import lombok.Getter;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Getter
public class DBManager {
    private static final String DATABASE_URL = "jdbc:sqlite:ia_fxgui.db";

    private static volatile DBManager instance;

    private final DatasetService datasetService;
    private final UserService userService;
    private final AuthService authService;

    public DBManager() {
        userService = new UserServiceRawSqlite();
        datasetService = new DatasetServiceRawSqlite();
        authService = new AuthServiceImpl();
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
}
