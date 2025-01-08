package com.flamexandr.phonebook.dao;

import com.flamexandr.phonebook.model.PhoneNumberType;

import java.sql.SQLException;
import java.util.List;

public interface PhoneNumberTypeDao {
    void save(PhoneNumberType phoneNumberType) throws SQLException;

    PhoneNumberType findById(int id) throws SQLException;

    List<PhoneNumberType> findAll() throws SQLException;

    boolean existsById(int id) throws SQLException;

    void deleteById(int id) throws SQLException;
}
