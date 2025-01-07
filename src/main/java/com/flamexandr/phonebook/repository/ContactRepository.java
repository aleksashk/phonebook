package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.Contact;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository {

    public void addContact(Contact contact) {
        String sql = "INSERT INTO contact (last_name, first_name, user_email) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, contact.getLastName());
            statement.setString(2, contact.getFirstName());
            statement.setString(3, contact.getUserEmail());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contact.setId(generatedKeys.getInt(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error adding contact", e);
        }
    }

    public List<Contact> getContactsByUserEmail(String userEmail) {
        String sql = "SELECT * FROM contact WHERE user_email = ?";
        List<Contact> contacts = new ArrayList<>();
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, userEmail);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    contacts.add(new Contact(
                            resultSet.getInt("id"),
                            resultSet.getString("last_name"),
                            resultSet.getString("first_name"),
                            resultSet.getString("user_email")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching contacts", e);
        }
        return contacts;
    }

    public List<Contact> searchContacts(String userEmail, String query) {
        List<Contact> contacts = new ArrayList<>();
        String sql = "SELECT * FROM contact WHERE user_email = ? AND (last_name ILIKE ? OR first_name ILIKE ?)";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, userEmail);
            statement.setString(2, "%" + query + "%");
            statement.setString(3, "%" + query + "%");

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    contacts.add(new Contact(
                            resultSet.getInt("id"),
                            resultSet.getString("last_name"),
                            resultSet.getString("first_name"),
                            resultSet.getString("user_email")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error while searching contacts", e);
        }

        return contacts;
    }

    public Contact getContactById(int id) {
        String sql = "SELECT * FROM contact WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return new Contact(
                            resultSet.getInt("id"),
                            resultSet.getString("last_name"),
                            resultSet.getString("first_name"),
                            resultSet.getString("user_email")
                    );
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error fetching contact by ID", e);
        }
        return null;
    }

    public void updateContact(Contact contact) {
        String sql = "UPDATE contact SET last_name = ?, first_name = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, contact.getLastName());
            statement.setString(2, contact.getFirstName());
            statement.setInt(3, contact.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating contact", e);
        }
    }

    public void deleteContact(int id) {
        String sql = "DELETE FROM contact WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting contact", e);
        }
    }
}
