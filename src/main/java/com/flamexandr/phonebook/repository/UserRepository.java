package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.User;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserRepository {

    public boolean existsByEmail(String email) {
        String query = "SELECT 1 FROM users WHERE email = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to check if email exists: " + e.getMessage(), e);
        }
    }

    public void save(User user) {
        String query = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to save user: " + e.getMessage(), e);
        }
    }

    public User findByEmail(String email) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new User(
                            resultSet.getInt("id"),
                            resultSet.getString("email"),
                            resultSet.getString("password")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to find user by email: " + e.getMessage(), e);
        }
        return null;
    }
}
