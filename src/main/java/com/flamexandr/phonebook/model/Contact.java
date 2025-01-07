package com.flamexandr.phonebook.model;

public class Contact {
    private int id;
    private String lastName;
    private String firstName;
    private String userEmail; // Используем email вместо userId

    public Contact(int id, String lastName, String firstName, String userEmail) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userEmail = userEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
