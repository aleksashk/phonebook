package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.PhoneNumberTypeDao;
import com.flamexandr.phonebook.model.PhoneNumberType;
import com.flamexandr.phonebook.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PhoneNumberTypeDaoImpl implements PhoneNumberTypeDao {

    @Override
    public void save(PhoneNumberType phoneNumberType) throws SQLException {
        String sql = "INSERT INTO phone_number_type (name) VALUES (?) RETURNING id";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, phoneNumberType.getName());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                phoneNumberType.setId(resultSet.getInt(1));
            }
        }
    }

    @Override
    public PhoneNumberType findById(int id) throws SQLException {
        String sql = "SELECT * FROM phone_number_type WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                String name = resultSet.getString("name");
                System.out.println("Retrieved PhoneNumberType: " + name);
                return new PhoneNumberType(id, name);
            }
        }
        return null;
    }

    @Override
    public List<PhoneNumberType> findAll() throws SQLException {
        List<PhoneNumberType> phoneNumberTypes = new ArrayList<>();
        String sql = "SELECT * FROM phone_number_type";
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                phoneNumberTypes.add(new PhoneNumberType(resultSet.getInt("id"), resultSet.getString("name")));
            }
        }
        return phoneNumberTypes;
    }

    @Override
    public boolean existsById(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM phone_number_type WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next() && resultSet.getInt(1) > 0;
        }
    }

    @Override
    public void deleteById(int id) throws SQLException {
        String sql = "DELETE FROM phone_number_type WHERE id = ?";
        try (Connection connection = DatabaseUtil.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            statement.executeUpdate();
        }
    }
}
