package com.flamexandr.phonebook.model;

import java.util.Objects;

public class ContactCategory {

    private int contactId;
    private int categoryId;

    public ContactCategory() {
    }

    public ContactCategory(int contactId, int categoryId) {
        this.contactId = contactId;
        this.categoryId = categoryId;
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
        return contactId == that.contactId && categoryId == that.categoryId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(contactId, categoryId);
    }

    @Override
    public String toString() {
        return "ContactCategory{" +
                "contactId=" + contactId +
                ", categoryId=" + categoryId +
                '}';
    }
}
