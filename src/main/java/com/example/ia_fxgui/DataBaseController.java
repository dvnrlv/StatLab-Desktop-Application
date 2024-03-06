package com.example.ia_fxgui;

import java.sql.*;
import java.util.Scanner;

public class DataBaseController {
    public static void main(String[] args) {
        // Database connection parameters
        String jdbcUrl = "jdbc:mysql://localhost:3306/IA_Schema";
        String username = "root";
        String password = "Dreamworks2705";

        try {
            // Step 1: Connect to the database
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connected to the database!");

            // Step 2: Create a prepared statement
            String sqlQuery = "SELECT * FROM your_table_name WHERE column_name = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);

            // Step 3: Take user input for the parameter
            Scanner scanner = new Scanner(System.in);
            System.out.print("Enter a value for column_name: ");
            String userInput = scanner.nextLine();

            // Step 4: Set the parameter value in the prepared statement
            preparedStatement.setString(1, userInput);

            // Step 5: Execute the SQL query
            ResultSet resultSet = preparedStatement.executeQuery();

            // Step 6: Process the results
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String columnValue = resultSet.getString("column_name");

                System.out.println("ID: " + id + ", Column Name Value: " + columnValue);
            }

            // Step 7: Close the resources
            resultSet.close();
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}