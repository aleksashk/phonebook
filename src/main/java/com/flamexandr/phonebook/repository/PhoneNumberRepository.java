package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.PhoneNumber;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneNumberRepository {

    public void addPhoneNumber(PhoneNumber phoneNumber) throws SQLException {
        String query = "INSERT INTO phone_number (contact_id, phone_number, type_id) VALUES (?, ?, ?)";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setInt(1, phoneNumber.getContactId());
            preparedStatement.setString(2, phoneNumber.getPhoneNumber());
            preparedStatement.setInt(3, phoneNumber.getTypeId());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating phone number failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    phoneNumber.setId(generatedKeys.getInt(1));
                    System.out.println("Inserted PhoneNumber with ID: " + phoneNumber.getId());
                } else {
                    throw new SQLException("Creating phone number failed, no ID obtained.");
                }
            }
        }
    }

    public PhoneNumber getPhoneNumber(int id) throws SQLException {
        String query = "SELECT * FROM phone_number WHERE id = ?";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new PhoneNumber(
                            resultSet.getInt("id"),
                            resultSet.getInt("contact_id"),
                            resultSet.getString("phone_number"),
                            resultSet.getInt("type_id")
                    );
                }
            }
        }
        return null;
    }

    public List<PhoneNumber> getAllPhoneNumbers() throws SQLException {
        String query = "SELECT * FROM phone_number";
        System.out.println("Executing query: " + query);

        List<PhoneNumber> phoneNumbers = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                phoneNumbers.add(new PhoneNumber(
                        resultSet.getInt("id"),
                        resultSet.getInt("contact_id"),
                        resultSet.getString("phone_number"),
                        resultSet.getInt("type_id")
                ));
            }
        }
        return phoneNumbers;
    }

    public void updatePhoneNumber(PhoneNumber phoneNumber) throws SQLException {
        String query = "UPDATE phone_number SET contact_id = ?, phone_number = ?, type_id = ? WHERE id = ?";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, phoneNumber.getContactId());
            preparedStatement.setString(2, phoneNumber.getPhoneNumber());
            preparedStatement.setInt(3, phoneNumber.getTypeId());
            preparedStatement.setInt(4, phoneNumber.getId());

            preparedStatement.executeUpdate();
        }
    }

    public void deletePhoneNumber(int id) throws SQLException {
        String query = "DELETE FROM phone_number WHERE id = ?";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }

    public void deleteAllPhoneNumbers() throws SQLException {
        String query = "DELETE FROM phone_number";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }
}
