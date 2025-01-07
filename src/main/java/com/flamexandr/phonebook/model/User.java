package com.flamexandr.phonebook.model;

import org.apache.commons.codec.digest.DigestUtils;

public class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean checkPassword(String rawPassword) {
        // Здесь мы хэшируем введенный пароль и сравниваем с хэшированным из БД
        return DigestUtils.md5Hex(rawPassword).equals(password);
    }
}
