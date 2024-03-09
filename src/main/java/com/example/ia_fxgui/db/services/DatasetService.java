package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.models.Dataset;

public interface DatasetService {
    void saveExistingDataset(Dataset dataset);
    void saveNewDataset(Dataset dataset);

    Dataset findDatasetByName();
}
