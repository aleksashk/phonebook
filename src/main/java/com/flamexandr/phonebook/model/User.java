package com.flamexandr.phonebook.model;

import org.apache.commons.codec.digest.DigestUtils;

public class User {
    private String email;
    private String password;

    public User(String email, String password) {
        this.email = email;
        this.password = DigestUtils.md5Hex(password);
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
