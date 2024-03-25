package com.example.ia_fxgui.db.services;

public interface AuthService {
    boolean register(String login, String password);

    boolean login(String login, String password);
    boolean logout();

    String getLoggedUserName();
}
