package com.example.ia_fxgui.db.models;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class User {
    private String login;
    private String password;

    public String toDbRaw() {
        return login + ", " + password;
    }
}
