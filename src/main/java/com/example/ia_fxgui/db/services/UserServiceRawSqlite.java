package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.DBManager;

public class UserServiceRawSqlite implements UserService {
    private static final String TABLE_NAME = "datasets";
    private static final String CREATION_STATEMENT = String.format(
            "(" +
                    "    name TEXT PRIMARY KEY," +
                    "    id   INTEGER" +
                    ");",
            TABLE_NAME
    );

    public UserServiceRawSqlite() {
        DBManager.executeStatement(CREATION_STATEMENT);
    }
}
