package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.UserDao;
import com.flamexandr.phonebook.model.User;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    @Override
    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users (email, password) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            System.out.println("Сохраняемый email: " + user.getEmail());
            System.out.println("Сохраняемый пароль (хэш): " + user.getPassword());
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword()); // Хэшированный пароль
            statement.executeUpdate();
        }
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }

    @Override
    public User findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String retrievedEmail = resultSet.getString("email");
                String retrievedPassword = resultSet.getString("password");
                System.out.println("Извлеченный email: " + retrievedEmail);
                System.out.println("Извлеченный пароль (хэш): " + retrievedPassword);
                // Используем новый конструктор для хэшированного пароля
                return new User(retrievedEmail, retrievedPassword, true);
            }
        }
        return null;
    }

    @Override
    public String hashPassword(String password) {
        throw new UnsupportedOperationException("Хэширование пароля должно выполняться на уровне модели.");
    }
}
