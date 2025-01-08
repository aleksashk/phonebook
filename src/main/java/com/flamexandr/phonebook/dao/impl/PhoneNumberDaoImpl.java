package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.PhoneNumberDao;
import com.flamexandr.phonebook.model.PhoneNumber;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneNumberDaoImpl implements PhoneNumberDao {

    @Override
    public PhoneNumber findById(int id) throws SQLException {
        String query = "SELECT * FROM phone_number WHERE id = ?";
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

    @Override
    public List<PhoneNumber> findAll() throws SQLException {
        String query = "SELECT * FROM phone_number";
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

    @Override
    public void addPhoneNumber(PhoneNumber phoneNumber) throws SQLException {
        String query = "INSERT INTO phone_number (contact_id, phone_number, type_id) VALUES (?, ?, ?)";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            preparedStatement.setInt(1, phoneNumber.getContactId());
            preparedStatement.setString(2, phoneNumber.getPhoneNumber());
            preparedStatement.setInt(3, phoneNumber.getTypeId());

            preparedStatement.executeUpdate();

            try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    phoneNumber.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    @Override
    public void updatePhoneNumber(PhoneNumber phoneNumber) throws SQLException {
        String query = "UPDATE phone_number SET contact_id = ?, phone_number = ?, type_id = ? WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, phoneNumber.getContactId());
            preparedStatement.setString(2, phoneNumber.getPhoneNumber());
            preparedStatement.setInt(3, phoneNumber.getTypeId());
            preparedStatement.setInt(4, phoneNumber.getId());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean deletePhoneNumber(int id) throws SQLException {
        String query = "DELETE FROM phone_number WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);
            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;
        }
    }

    @Override
    public void deleteAllPhoneNumbers() throws SQLException {
        String query = "DELETE FROM phone_number";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(query);
        }
    }
}
