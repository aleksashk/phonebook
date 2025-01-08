package com.flamexandr.phonebook.model;

import org.apache.commons.codec.digest.DigestUtils;

public class User {
    private String email;
    private String password;

    // Конструктор для создания нового пользователя (хэширование пароля)
    public User(String email, String password) {
        this.email = email;
        this.password = DigestUtils.md5Hex(password); // Хэширование пароля
    }

    // Конструктор для создания пользователя с уже хэшированным паролем
    public User(String email, String password, boolean hashed) {
        this.email = email;
        this.password = hashed ? password : DigestUtils.md5Hex(password);
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public boolean checkPassword(String plainPassword) {
        return DigestUtils.md5Hex(plainPassword).equals(this.password);
    }
}
