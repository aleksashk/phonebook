package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.ContactDao;
import com.flamexandr.phonebook.model.Contact;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactDaoImpl implements ContactDao {
    @Override
    public Contact findById(int id) throws SQLException {
        String query = "SELECT * FROM contact WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Contact(
                        resultSet.getInt("id"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name"),
                        resultSet.getString("user_email")
                );
            }
        }
        return null;
    }

    @Override
    public List<Contact> findAllByUserEmail(String email) throws SQLException {
        String query = "SELECT * FROM contact WHERE user_email = ?";
        List<Contact> contacts = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                contacts.add(new Contact(
                        resultSet.getInt("id"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name"),
                        resultSet.getString("user_email")
                ));
            }
        }
        return contacts;
    }

    @Override
    public void addContact(Contact contact) throws SQLException {
        String query = "INSERT INTO contact (last_name, first_name, user_email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, contact.getLastName());
            preparedStatement.setString(2, contact.getFirstName());
            preparedStatement.setString(3, contact.getUserEmail());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void updateContact(Contact contact) throws SQLException {
        String query = "UPDATE contact SET last_name = ?, first_name = ?, user_email = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, contact.getLastName());
            preparedStatement.setString(2, contact.getFirstName());
            preparedStatement.setString(3, contact.getUserEmail());
            preparedStatement.setInt(4, contact.getId());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deleteContact(int id) throws SQLException {
        String query = "DELETE FROM contact WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public int insert(Contact contact) throws SQLException {
        String query = "INSERT INTO contact (last_name, first_name, user_email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setString(1, contact.getLastName());
            preparedStatement.setString(2, contact.getFirstName());
            preparedStatement.setString(3, contact.getUserEmail());
            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1; // Если вставка не удалась
    }

    @Override
    public List<Contact> findByUserEmail(String email) throws SQLException {
        String query = "SELECT * FROM contact WHERE user_email = ?";
        List<Contact> contacts = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, email);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                contacts.add(new Contact(
                        resultSet.getInt("id"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name"),
                        resultSet.getString("user_email")
                ));
            }
        }
        return contacts;
    }
}
