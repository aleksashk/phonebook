package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.ContactDao;
import com.flamexandr.phonebook.model.Contact;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactDaoImplTest {

    private ContactDao contactDao;

    @BeforeAll
    void setUpDatabase() throws Exception {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            // Создание таблиц и тестового пользователя
            statement.execute("TRUNCATE TABLE contact RESTART IDENTITY CASCADE;");
            statement.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE;");
            statement.execute("INSERT INTO users (email, password) VALUES ('test@example.com', 'password123');");
        }
    }

    @BeforeEach
    void setUp() {
        contactDao = new ContactDaoImpl();
    }

    @Test
    void testAddContact() throws Exception {
        Contact contact = new Contact(0, "Иванов", "Иван", "test@example.com");
        contactDao.addContact(contact);

        assertNotEquals(0, contact.getId());
        Contact retrieved = contactDao.findById(contact.getId());
        assertNotNull(retrieved);
        assertEquals("Иванов", retrieved.getLastName());
        assertEquals("Иван", retrieved.getFirstName());
        assertEquals("test@example.com", retrieved.getUserEmail());
    }

    @Test
    void testFindById() throws Exception {
        Contact contact = new Contact(0, "Петров", "Петр", "test@example.com");
        contactDao.addContact(contact);

        Contact retrieved = contactDao.findById(contact.getId());
        assertNotNull(retrieved);
        assertEquals("Петров", retrieved.getLastName());
        assertEquals("Петр", retrieved.getFirstName());
    }

    @Test
    void testFindAllByUserEmail() throws Exception {
        contactDao.addContact(new Contact(0, "Сидоров", "Сергей", "test@example.com"));
        contactDao.addContact(new Contact(0, "Кузнецов", "Алексей", "test@example.com"));

        List<Contact> contacts = contactDao.findAllByUserEmail("test@example.com");
        assertEquals(2, contacts.size());
    }

    @Test
    void testUpdateContact() throws Exception {
        Contact contact = new Contact(0, "Иванов", "Иван", "test@example.com");
        contactDao.addContact(contact);

        contact.setLastName("Сидоров");
        contact.setFirstName("Сергей");
        contactDao.updateContact(contact);

        Contact updated = contactDao.findById(contact.getId());
        assertEquals("Сидоров", updated.getLastName());
        assertEquals("Сергей", updated.getFirstName());
    }

    @Test
    void testDeleteContact() throws Exception {
        Contact contact = new Contact(0, "Иванов", "Иван", "test@example.com");
        contactDao.addContact(contact);

        assertTrue(contactDao.deleteContact(contact.getId()));
        assertNull(contactDao.findById(contact.getId()));
    }

    @AfterEach
    void cleanUp() throws Exception {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE contact RESTART IDENTITY CASCADE;");
        }
    }
}
