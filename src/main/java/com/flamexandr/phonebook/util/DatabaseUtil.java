package com.flamexandr.phonebook.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseUtil {

    private static final String URL = "jdbc:postgresql://localhost:5432/phone_book";
    private static final String USER = "postgres";
    private static final String PASSWORD = "password";

    // Метод для подключения к базе данных
    public static Connection getConnection() {
        try {
            System.out.println("Connecting to database: " + URL);
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            connection.setAutoCommit(true);
            System.out.println("Connection established successfully!");
            return connection;
        } catch (SQLException e) {
            System.err.println("Failed to connect to the database: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Метод для тестирования подключения
    public static void testConnection() {
        try (Connection connection = getConnection()) {
            System.out.println("Database connection test successful!");
        } catch (SQLException e) {
            System.err.println("Database connection test failed: " + e.getMessage());
        }
    }
}
