package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.models.Dataset;

public class DatasetServiceRawSqlite implements DatasetService {
    private static final String TABLE_NAME = "datasets";
    private static final String CREATION_STATEMENT = String.format(
            "CREATE TABLE IF NOT EXISTS %s" +
                    "(" +
                    "    id       INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "    name     TEXT UNIQUE," +
                    "    password TEXT" +
                    ");",
            TABLE_NAME
    );


    public DatasetServiceRawSqlite() {
        DBManager.executeStatement(CREATION_STATEMENT);
    }

    @Override
    public void saveDataset(Dataset dataset) {

    }

    @Override
    public Dataset findDatasetByName() {
        return null;
    }
}
