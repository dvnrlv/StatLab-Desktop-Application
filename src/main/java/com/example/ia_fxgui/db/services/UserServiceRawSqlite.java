package com.example.ia_fxgui.db.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;


public class UserServiceRawSqlite implements UserService {
    private static final String TABLE_NAME = "users";
    private static final String CREATION_STATEMENT = String.format(
            "CREATE TABLE IF NOT EXISTS %s(" +
                    "    login TEXT PRIMARY KEY," +
                    "    password TEXT" +
                    ")",
            TABLE_NAME
    );

    private static final String SELECT_STATEMENT = String.format(
            "SELECT * from %s",
            TABLE_NAME
    ) + " WHERE login='%s'";

    private static final String INSERT_STATEMENT_PATTERN = String.format(
            "INSERT INTO %s",
            TABLE_NAME
    ) + " values (%s)";

    public UserServiceRawSqlite() {
        DBManager.executeStatement(CREATION_STATEMENT);
    }

    @Override
    public boolean register(String login, String password) {
        if (checkIfUserWithLoginExists(login)) {
            return false;
        }

        saveNewUser(new User(login, BCrypt.withDefaults().hashToString(12, password.toCharArray())));
        return true;
    }

    @Override
    public boolean login(String login, String password) {
        User userByLogin;
        try {
            userByLogin = findByLogin(login);
        } catch (SqlRowNotFoundException e) {
            return false;
        }

        return BCrypt.verifyer().verify(password.toCharArray(), userByLogin.getPassword()).verified;
    }

    @Override
    public User findByLogin(String login) throws SqlRowNotFoundException {
        String statement = String.format(SELECT_STATEMENT, login);
        try (ResultSet results = DBManager.executeQuery(statement)) {
            if (!results.next()) {
                throw new SqlRowNotFoundException("No user with provided login");
            }
            return new User(results.getString("name"), results.getString("password"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkIfUserWithLoginExists(String login) {
        String statement = String.format(SELECT_STATEMENT, login);
        try (ResultSet results = DBManager.executeQuery(statement)) {
            return results.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveNewUser(User user) {
        DBManager.executeStatement(String.format(INSERT_STATEMENT_PATTERN, user.toDbRaw()));
    }
}
