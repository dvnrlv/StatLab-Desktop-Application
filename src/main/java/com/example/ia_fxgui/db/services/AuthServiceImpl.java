package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.Main;
import com.example.ia_fxgui.db.DBManager;

public class AuthServiceImpl implements AuthService {
    private String loggedUser = null;

    @Override
    public boolean register(String login, String password) {
        if (password.length() >= 6) {
            Main.WarningPopup.openPopup("Registration Successful");
            return DBManager.getInstance().getUserService().register(login, password);
        } else {
            Main.WarningPopup.openPopup("Registration Unsuccessful");
            return false;
        }
    }

    @Override
    public boolean login(String login, String password) {
        boolean logged = DBManager.getInstance().getUserService().login(login, password);
        if (logged) {
            loggedUser = login;
        }

        return logged;
    }

    @Override
    public boolean logout() {
        if (loggedUser == null) {
            return false;
        }
        loggedUser = null;
        return true;
    }

    @Override
    public String getLoggedUserName() {
        return loggedUser;
    }
}
