package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.User;
import org.apache.commons.codec.digest.DigestUtils;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private final Map<String, User> users = new HashMap<>();

    public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }

    public void save(User user) {
        user.setPassword(hashPassword(user.getPassword())); // Хэшируем пароль перед сохранением
        users.put(user.getEmail(), user);
    }

    public User findByEmail(String email) {
        return users.get(email);
    }

    private String hashPassword(String password) {
        return DigestUtils.md5Hex(password);
    }
}
