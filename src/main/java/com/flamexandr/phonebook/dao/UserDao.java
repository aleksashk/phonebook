package com.flamexandr.phonebook.dao;

import com.flamexandr.phonebook.model.User;

import java.sql.SQLException;

public interface UserDao {
    void save(User user) throws SQLException;

    boolean existsByEmail(String email) throws SQLException;

    User findByEmail(String email) throws SQLException;

    String hashPassword(String password);
}
