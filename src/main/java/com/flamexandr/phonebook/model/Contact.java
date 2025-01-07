package com.flamexandr.phonebook.model;

public class Contact {

    private int id;
    private String lastName;
    private String firstName;
    private int userId;

    public Contact(int id, String lastName, String firstName, int userId) {
        this.id = id;
        this.lastName = lastName;
        this.firstName = firstName;
        this.userId = userId;
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
