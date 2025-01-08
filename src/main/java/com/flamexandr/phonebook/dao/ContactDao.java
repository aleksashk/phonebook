package com.flamexandr.phonebook.dao;

import com.flamexandr.phonebook.model.Contact;

import java.sql.SQLException;
import java.util.List;

public interface ContactDao {
    Contact findById(int id) throws SQLException;

    List<Contact> findAllByUserEmail(String email) throws SQLException;

    void addContact(Contact contact) throws SQLException;

    void updateContact(Contact contact) throws SQLException;

    boolean deleteContact(int id) throws SQLException;
}
