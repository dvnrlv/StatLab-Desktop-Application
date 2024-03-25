package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.Dataset;

import java.util.List;

public interface DatasetService {
    void saveDataset(Dataset dataset);

    List<String> findLoggedUserDatasetsNames();

    Dataset findDatasetByName(String name) throws SqlRowNotFoundException;

    boolean checkIfDatasetWithNameExist(String name);
}
