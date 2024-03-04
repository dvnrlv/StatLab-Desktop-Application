package com.example.ia_fxgui.login;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserManager {
    private static final String DATABASE_URL = "jdbc:sqlite:users.db";

    public static void registerUser(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL);
            String insertSQL = "INSERT INTO users (username, password) VALUES (?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(insertSQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashAndSaltPassword(password));
            preparedStatement.executeUpdate();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean loginUser(String username, String password) {
        try {
            Connection connection = DriverManager.getConnection(DATABASE_URL);
            String selectSQL = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, hashAndSaltPassword(password));
            ResultSet resultSet = preparedStatement.executeQuery();
            boolean loggedIn = resultSet.next();
            resultSet.close();
            preparedStatement.close();
            connection.close();
            return loggedIn;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String hashAndSaltPassword(String password) {
        // Implement password hashing and salting here (e.g., using bcrypt or another secure hashing algorithm)
        return password;
    }
}
