package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.ContactCategoryDao;
import com.flamexandr.phonebook.model.ContactCategory;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactCategoryDaoImpl implements ContactCategoryDao {
    @Override
    public ContactCategory findById(int id) throws SQLException {
        String query = "SELECT * FROM contact_category WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new ContactCategory(
                            resultSet.getInt("id"),
                            resultSet.getInt("contact_id"),
                            resultSet.getInt("category_id")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<ContactCategory> findAll() throws SQLException {
        String query = "SELECT * FROM contact_category";
        List<ContactCategory> contactCategories = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                contactCategories.add(new ContactCategory(
                        resultSet.getInt("id"),
                        resultSet.getInt("contact_id"),
                        resultSet.getInt("category_id")
                ));
            }
        }
        return contactCategories;
    }

    @Override
    public void addContactCategory(ContactCategory contactCategory) throws SQLException {
        String query = "INSERT INTO contact_category (contact_id, category_id) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, contactCategory.getContactId());
            preparedStatement.setInt(2, contactCategory.getCategoryId());
            preparedStatement.executeUpdate();
            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contactCategory.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateContactCategory(ContactCategory contactCategory) throws SQLException {
        String query = "UPDATE contact_category SET contact_id = ?, category_id = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, contactCategory.getContactId());
            preparedStatement.setInt(2, contactCategory.getCategoryId());
            preparedStatement.setInt(3, contactCategory.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deleteContactCategory(int id) throws SQLException {
        String query = "DELETE FROM contact_category WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }
}
