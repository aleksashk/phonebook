package com.flamexandr.phonebook.model;

public class PhoneNumber {

    private int id;
    private int contactId;
    private String phoneNumber;
    private int typeId;

    public PhoneNumber(int id, int contactId, String phoneNumber, int typeId) {
        this.id = id;
        this.contactId = contactId;
        this.phoneNumber = phoneNumber;
        this.typeId = typeId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getContactId() {
        return contactId;
    }

    public void setContactId(int contactId) {
        this.contactId = contactId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }
}
