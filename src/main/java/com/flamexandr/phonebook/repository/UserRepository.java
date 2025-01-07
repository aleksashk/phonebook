package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.User;

import java.util.HashMap;
import java.util.Map;

public class UserRepository {
    private int currentId = 1;
    private final Map<String, User> users = new HashMap<>();

    public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }

    public void save(User user) {
        user.setId(currentId++); // Генерируем уникальный ID
        users.put(user.getEmail(), user);
    }

    public User findByEmail(String email) {
        return users.get(email);
    }

}
