package com.flamexandr.phonebook.model;

import java.util.HashMap;
import java.util.Map;

public class User {
    private int id;
    private String email;
    private String password;
    private Map<String, String> phoneBook;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.phoneBook = new HashMap<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { // Добавлен сеттер
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(password);
    }

    public Map<String, String> getPhoneBook() {
        return phoneBook;
    }
}
