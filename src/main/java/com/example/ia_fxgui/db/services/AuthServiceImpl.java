package com.example.ia_fxgui.db.services;

import com.example.ia_fxgui.Main;
import com.example.ia_fxgui.db.DBManager;

public class AuthServiceImpl implements AuthService {
    private String loggedUser = null;

    @Override
    public boolean register(String login, String password) {
        if (password.length() >= 6) {
            if (DBManager.getInstance().getUserService().register(login, password)) {
                Main.WarningPopup.openPopup("Registration Successful");
                return true;
            } else {
                Main.WarningPopup.openPopup("Duplicate user login");
                return false;
            }
        } else {
            Main.WarningPopup.openPopup("Registration Unsuccessful. Password is too weak");
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
