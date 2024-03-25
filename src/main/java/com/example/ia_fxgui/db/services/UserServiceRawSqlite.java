package com.example.ia_fxgui.db.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.example.ia_fxgui.db.DBManager;
import com.example.ia_fxgui.db.SqlRowNotFoundException;
import com.example.ia_fxgui.db.models.User;

import java.sql.*;


public class UserServiceRawSqlite implements UserService {

    public UserServiceRawSqlite() {
        try (Connection connection = DBManager.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("CREATE TABLE IF NOT EXISTS users(" +
                    "                       login TEXT PRIMARY KEY," +
                    "                       password TEXT" +
                    "                    )");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
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
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login=?");
        ) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            if (!resultSet.next()) {
                throw new SqlRowNotFoundException("No user with provided login");
            }

            User user = new User(resultSet.getString("login"), resultSet.getString("password"));
            resultSet.close();
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean checkIfUserWithLoginExists(String login) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE login=?");
        ) {
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            boolean doesExist = resultSet.next();
            resultSet.close();
            return doesExist;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static void saveNewUser(User user) {
        try (Connection connection = DBManager.getConnection();
             PreparedStatement statement = connection.prepareStatement("INSERT INTO users values (?, ?)")) {
            statement.setString(1, user.getLogin());
            statement.setString(2, user.getPassword());
            statement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
