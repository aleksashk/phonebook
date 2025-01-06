package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.Contact;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactRepository {


    public List<Contact> getAllContacts() throws SQLException {

        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM contact";

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {
                contacts.add(new Contact(
                        resultSet.getInt("id"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name")
                ));
            }
            return contacts;
        }
    }

    public void addContact(Contact contact) throws SQLException {

        String query = "INSERT INTO contact (last_name, first_name) VALUES (?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, contact.getLastName());
            preparedStatement.setString(2, contact.getFirstName());

            preparedStatement.executeUpdate();
        }
    }

    public Contact getContactById(int id) throws SQLException {

        String query = "SELECT * FROM contact WHERE id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                return new Contact(
                        resultSet.getInt("id"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name")
                );
            }
        }
        return null;
    }

    public boolean existsById(int id) throws SQLException {
        String query = "SELECT 1 FROM contact WHERE id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();

            return resultSet.next();
        }
    }

    public List<Contact> getContactsByFirstName(String firstName) throws SQLException {
        List<Contact> contacts = new ArrayList<>();
        String query = "SELECT * FROM contact WHERE first_name = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, firstName);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                contacts.add(new Contact(
                        resultSet.getInt("id"),
                        resultSet.getString("last_name"),
                        resultSet.getString("first_name")
                ));
            }
        }
        return contacts;
    }

    public void updateContact(Contact contact) throws SQLException {
        String query = "UPDATE contact SET last_name = ?, first_name = ? WHERE id = ?";

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setString(1, contact.getLastName());
            preparedStatement.setString(2, contact.getFirstName());
            preparedStatement.setInt(3, contact.getId());

            preparedStatement.executeUpdate();
        }
    }

}