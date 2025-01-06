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

            statement.execute("TRUNCATE TABLE contact_category, phone_number, contact, category, phone_number_type RESTART IDENTITY CASCADE");

            // Инициализация справочных данных
            statement.execute("INSERT INTO phone_number_type (name) VALUES ('Личный'), ('Рабочий'), ('Домашний');");
        }
    }

    @Test
    void testAddContact() throws Exception {
        // Добавляем контакт
        Contact contact = new Contact(0, "Петров", "Петр");
        contactRepository.addContact(contact);

        // Проверяем количество записей и данные
        List<Contact> contacts = contactRepository.getAllContacts();
        assertEquals(1, contacts.size());
        assertEquals("Петров", contacts.get(0).getLastName());
        assertEquals("Петр", contacts.get(0).getFirstName());
    }

    @Test
    void testGetAllContacts() throws Exception {
        // Добавляем два контакта
        contactRepository.addContact(new Contact(0, "Иванов", "Иван"));
        contactRepository.addContact(new Contact(0, "Петров", "Петр"));

        // Проверяем их количество
        List<Contact> contacts = contactRepository.getAllContacts();
        assertEquals(2, contacts.size());
    }

    @Test
    void testGetContactById() throws Exception {
        // Добавляем новый контакт
        Contact contact = new Contact(0, "Сидоров", "Семен");
        contactRepository.addContact(contact);

        // Проверяем, что он существует и данные соответствуют
        Contact retrievedContact = contactRepository.getContactById(contact.getId());
        assertNotNull(retrievedContact, "Contact should not be null");
        assertEquals("Сидоров", retrievedContact.getLastName());
        assertEquals("Семен", retrievedContact.getFirstName());
    }

    @Test
    void testGetContactById_NotFound() throws Exception {
        Contact contact = contactRepository.getContactById(999);
        assertNull(contact);
    }

    @Test
    void testExistsById() throws Exception {
        Contact contact = new Contact(0, "Иванов", "Иван");
        contactRepository.addContact(contact);
        assertTrue(contactRepository.existsById(1), "Contact with ID 1 should exist");
    }

    @Test
    void testGetContactsByLastName() throws Exception {
        contactRepository.addContact(new Contact(0, "Иванов", "Иван"));
        contactRepository.addContact(new Contact(0, "Иванов", "Пётр"));

        List<Contact> contacts = contactRepository.getContactsByLastName("Иванов");
        assertEquals(2, contacts.size());
        assertEquals("Иван", contacts.get(0).getFirstName());
        assertEquals("Пётр", contacts.get(1).getFirstName());
    }

    @Test
    void testGetContactsByLastName_NotFound() throws Exception {
        List<Contact> contacts = contactRepository.getContactsByLastName("Неизвестный");
        assertTrue(contacts.isEmpty());
    }

    @Test
    void testUpdateContact() throws Exception {
        Contact contact = new Contact(0, "Иванов", "Иван");
        contactRepository.addContact(contact);

        Contact updatedContact = new Contact(1, "Смирнов", "Иван");
        contactRepository.updateContact(updatedContact);

        Contact retrievedContact = contactRepository.getContactById(1);
        assertNotNull(retrievedContact, "Updated contact should not be null");
        assertEquals("Смирнов", retrievedContact.getLastName());
    }

    @Test
    void testUpdateContact_NotFound() throws Exception {
        Contact updatedContact = new Contact(999, "Неизвестный", "Имя");
        contactRepository.updateContact(updatedContact);

        Contact contact = contactRepository.getContactById(999);
        assertNull(contact);
    }

    @Test
    void testDeleteContact() throws Exception {
        Contact contact = new Contact(0, "Иванов", "Иван");
        contactRepository.addContact(contact);
        assertTrue(contactRepository.existsById(1), "Contact with ID 1 should exist before deletion");

        contactRepository.deleteContact(1);
        assertFalse(contactRepository.existsById(1), "Contact with ID 1 should not exist after deletion");
    }

    @Test
    void testDeleteContact_NotFound() throws Exception {
        contactRepository.deleteContact(999);
        assertFalse(contactRepository.existsById(999));
    }
}