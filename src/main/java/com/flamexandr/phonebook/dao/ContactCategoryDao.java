package com.flamexandr.phonebook.dao;

import com.flamexandr.phonebook.model.ContactCategory;
import java.sql.SQLException;
import java.util.List;

public interface ContactCategoryDao {
    ContactCategory findById(int id) throws SQLException;
    List<ContactCategory> findAll() throws SQLException;
    void addContactCategory(ContactCategory contactCategory) throws SQLException;
    void updateContactCategory(ContactCategory contactCategory) throws SQLException;
    boolean deleteContactCategory(int id) throws SQLException;
}
