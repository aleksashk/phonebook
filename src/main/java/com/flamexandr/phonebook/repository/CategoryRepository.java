package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.Category;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryRepository {

    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO category (\"name\") VALUES (?)";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) { // Добавлено Statement.RETURN_GENERATED_KEYS

            preparedStatement.setString(1, category.getName());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    category.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public void deleteCategory(int id) throws SQLException {
        String query = "DELETE FROM category WHERE id = ?";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        }
    }

    public Category getCategoryById(int id) throws SQLException {
        String query = "SELECT * FROM category WHERE id = ?";

        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                );
            }
        }
        return null;
    }

    public List<Category> getAllCategories() throws SQLException {
        String query = "SELECT * FROM category";

        System.out.println("Executing query: " + query);

        List<Category> categories = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                categories.add(new Category(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
        }
        return categories;
    }

    public void updateCategory(Category category) throws SQLException {
        String query = "UPDATE category SET name = ? WHERE id = ?";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, category.getName());
            preparedStatement.setInt(2, category.getId());
            preparedStatement.executeUpdate();

        }
    }
}
