package com.flamexandr.phonebook.dao;

import com.flamexandr.phonebook.model.PhoneNumber;

import java.sql.SQLException;
import java.util.List;

public interface PhoneNumberDao {

    PhoneNumber findById(int id) throws SQLException;

    List<PhoneNumber> findAll() throws SQLException;

    void addPhoneNumber(PhoneNumber phoneNumber) throws SQLException;

    void updatePhoneNumber(PhoneNumber phoneNumber) throws SQLException;

    boolean deletePhoneNumber(int id) throws SQLException;

    void deleteAllPhoneNumbers() throws SQLException;
}
