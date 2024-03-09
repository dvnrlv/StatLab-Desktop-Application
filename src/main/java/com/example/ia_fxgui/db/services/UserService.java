package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.User;

public interface UserService {
    boolean register(String login, String password);

    boolean login(String login, String password);

    User findByLogin(String login) throws SqlRowNotFoundException;

    boolean checkIfUserWithLoginExists(String login);
}
