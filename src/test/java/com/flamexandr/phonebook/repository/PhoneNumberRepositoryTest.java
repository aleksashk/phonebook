package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.PhoneNumber;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhoneNumberRepositoryTest {

    private PhoneNumberRepository phoneNumberRepository;

    @BeforeEach
    void setUp() throws Exception {
        phoneNumberRepository = new PhoneNumberRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Создание таблиц, если их нет
            statement.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id SERIAL PRIMARY KEY,
                    email VARCHAR(255) NOT NULL UNIQUE,
                    password VARCHAR(255) NOT NULL
                );
                CREATE TABLE IF NOT EXISTS contact (
                    id SERIAL PRIMARY KEY,
                    last_name VARCHAR(255) NOT NULL DEFAULT '',
                    first_name VARCHAR(255) NOT NULL DEFAULT '',
                    user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
                );
                CREATE TABLE IF NOT EXISTS phone_number_type (
                    id SERIAL PRIMARY KEY,
                    name VARCHAR(255) NOT NULL UNIQUE
                );
                CREATE TABLE IF NOT EXISTS phone_number (
                    id SERIAL PRIMARY KEY,
                    contact_id INT NOT NULL REFERENCES contact(id) ON DELETE CASCADE,
                    phone_number VARCHAR(15) NOT NULL UNIQUE,
                    type_id INT NOT NULL REFERENCES phone_number_type(id)
                );
            """);

            // Очистка данных перед каждым тестом
            statement.execute("TRUNCATE TABLE phone_number, contact, phone_number_type, users RESTART IDENTITY CASCADE;");

            // Вставка тестовых данных
            statement.execute("INSERT INTO users (email, password) VALUES ('user1@example.com', 'password1');");
            statement.execute("INSERT INTO contact (last_name, first_name, user_id) VALUES ('Иванов', 'Иван', 1);");
            statement.execute("INSERT INTO phone_number_type (name) VALUES ('Личный'), ('Рабочий'), ('Домашний');");
            statement.execute("INSERT INTO phone_number (contact_id, phone_number, type_id) VALUES (1, '1234567890', 1);");
        }
    }

    @Test
    public void testAddPhoneNumber() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(0, 1, "333444555", 1);
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        assertNotEquals(0, phoneNumber.getId());
        PhoneNumber retrieved = phoneNumberRepository.getPhoneNumber(phoneNumber.getId());
        assertNotNull(retrieved);
        assertEquals("333444555", retrieved.getPhoneNumber());
    }

    @Test
    public void testGetPhoneNumber() throws Exception {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);
        assertNotNull(phoneNumber);
        assertEquals("1234567890", phoneNumber.getPhoneNumber());
    }

    @Test
    public void testGetAllPhoneNumbers() throws Exception {
        List<PhoneNumber> phoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        assertNotNull(phoneNumbers);
        assertEquals(1, phoneNumbers.size());
    }

    @Test
    public void testUpdatePhoneNumber() throws Exception {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);
        assertNotNull(phoneNumber);

        phoneNumber.setPhoneNumber("222333444");
        phoneNumberRepository.updatePhoneNumber(phoneNumber);

        PhoneNumber updated = phoneNumberRepository.getPhoneNumber(1);
        assertEquals("222333444", updated.getPhoneNumber());
    }

    @Test
    public void testDeletePhoneNumber() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(0, 1, "555666777", 2);
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        int id = phoneNumber.getId();
        phoneNumberRepository.deletePhoneNumber(id);

        PhoneNumber deleted = phoneNumberRepository.getPhoneNumber(id);
        assertNull(deleted);
    }

    @Test
    public void testDeleteAllPhoneNumbers() throws Exception {
        phoneNumberRepository.deleteAllPhoneNumbers();
        List<PhoneNumber> phoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        assertTrue(phoneNumbers.isEmpty());
    }

    @AfterAll
    public void tearDown() throws Exception {
        phoneNumberRepository.deleteAllPhoneNumbers();
    }
}
