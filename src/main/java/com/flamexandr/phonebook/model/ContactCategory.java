package com.flamexandr.phonebook.model;

import java.util.Objects;

public class ContactCategory {

    private int id;
    private int contactId;
    private int categoryId;

    public ContactCategory() {
    }


    public ContactCategory(int id, int contactId, int categoryId) {
        this.id = id;
        this.contactId = contactId;
        this.categoryId = categoryId;
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

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactCategory that = (ContactCategory) o;
        return id == that.id && contactId == that.contactId && categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, contactId, categoryId);
    }

    @Override
    public String toString() {
        return "ContactCategory{" +
                "id=" + id +
                ", contactId=" + contactId +
                ", categoryId=" + categoryId +
                '}';
    }
}
