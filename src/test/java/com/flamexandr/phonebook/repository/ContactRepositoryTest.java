package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.Contact;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactRepositoryTest {

    private ContactRepository contactRepository;

    @BeforeEach
    void setUp() throws Exception {
        contactRepository = new ContactRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Полная очистка всех таблиц
            statement.execute("TRUNCATE TABLE contact_category, phone_number, contact, category, phone_number_type, users RESTART IDENTITY CASCADE");

            // Добавление тестового пользователя
            statement.execute("INSERT INTO users (email, password) VALUES ('test_user@example.com', 'password123')");
        }
    }

    @Test
    void testAddContact() {
        Contact contact = new Contact(0, "Иванов", "Иван", "test_user@example.com");
        contactRepository.addContact(contact);

        List<Contact> contacts = contactRepository.getContactsByUserEmail("test_user@example.com");
        assertEquals(1, contacts.size());
        assertEquals("Иванов", contacts.get(0).getLastName());
        assertEquals("Иван", contacts.get(0).getFirstName());
    }

    @Test
    void testGetContactsByUserEmail() {
        contactRepository.addContact(new Contact(0, "Иванов", "Иван", "test_user@example.com"));
        contactRepository.addContact(new Contact(0, "Петров", "Петр", "test_user@example.com"));

        List<Contact> contacts = contactRepository.getContactsByUserEmail("test_user@example.com");
        assertEquals(2, contacts.size());
    }

    @Test
    void testSearchContacts() {
        contactRepository.addContact(new Contact(0, "Иванов", "Иван", "test_user@example.com"));
        contactRepository.addContact(new Contact(0, "Петров", "Петр", "test_user@example.com"));

        // Убедитесь, что метод `searchContacts` принимает правильные параметры.
        List<Contact> results = contactRepository.searchContacts("test_user@example.com", "Иван");
        assertEquals(1, results.size());
        assertEquals("Иванов", results.get(0).getLastName());
    }

    @Test
    void testUpdateContact() {
        Contact contact = new Contact(0, "Иванов", "Иван", "test_user@example.com");
        contactRepository.addContact(contact);

        contact.setLastName("Сидоров");
        contact.setFirstName("Сергей");
        contactRepository.updateContact(contact);

        Contact updatedContact = contactRepository.getContactsByUserEmail("test_user@example.com").get(0);
        assertEquals("Сидоров", updatedContact.getLastName());
        assertEquals("Сергей", updatedContact.getFirstName());
    }

    @Test
    void testDeleteContact() {
        Contact contact = new Contact(0, "Иванов", "Иван", "test_user@example.com");
        contactRepository.addContact(contact);

        assertFalse(contactRepository.getContactsByUserEmail("test_user@example.com").isEmpty());
        contactRepository.deleteContact(contact.getId());
        assertTrue(contactRepository.getContactsByUserEmail("test_user@example.com").isEmpty());
    }
}
