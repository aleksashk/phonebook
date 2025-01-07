package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.PhoneNumberType;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneNumberTypeRepository {

    public void addPhoneNumberType(PhoneNumberType phoneNumberType) throws SQLException {
        String query = "INSERT INTO phone_number_type (name) VALUES (?)";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, phoneNumberType.getName());

            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating phone number type failed, no rows affected.");
            }

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    phoneNumberType.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating phone number type failed, no ID obtained.");
                }
            }
        }
    }

    public PhoneNumberType getPhoneNumberType(int id) throws SQLException {
        String query = "SELECT * FROM phone_number_type WHERE id = ?";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new PhoneNumberType(
                            resultSet.getInt("id"),
                            resultSet.getString("name")
                    );
                }
            }
        }
        return null;
    }

    public List<PhoneNumberType> getAllPhoneNumberTypes() throws SQLException {
        String query = "SELECT * FROM phone_number_type";
        System.out.println("Executing query: " + query);

        List<PhoneNumberType> phoneNumberTypes = new ArrayList<>();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                phoneNumberTypes.add(new PhoneNumberType(
                        resultSet.getInt("id"),
                        resultSet.getString("name")
                ));
            }
        }
        return phoneNumberTypes;
    }

    public void updatePhoneNumberType(PhoneNumberType phoneNumberType) throws SQLException {
        String query = "UPDATE phone_number_type SET name = ? WHERE id = ?";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, phoneNumberType.getName());
            preparedStatement.setInt(2, phoneNumberType.getId());

            preparedStatement.executeUpdate();
        }
    }

    public void deletePhoneNumberType(int id) throws SQLException {
        String query = "DELETE FROM phone_number_type WHERE id = ?";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }

    public void deleteAllPhoneNumberTypes() throws SQLException {
        String query = "DELETE FROM phone_number_type";
        System.out.println("Executing query: " + query);

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }
}
