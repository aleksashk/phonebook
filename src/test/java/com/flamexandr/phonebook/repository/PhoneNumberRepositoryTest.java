package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.PhoneNumber;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class PhoneNumberRepositoryTest {

    private PhoneNumberRepository phoneNumberRepository;

    @BeforeEach
    void setUp() throws Exception {
        phoneNumberRepository = new PhoneNumberRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Очистка таблиц
            statement.execute("TRUNCATE TABLE phone_number, contact, phone_number_type, users RESTART IDENTITY CASCADE;");

            // Добавление тестовых данных
            statement.execute("INSERT INTO users (email, password) VALUES ('user1@example.com', 'password1');");
            statement.execute("INSERT INTO contact (last_name, first_name, user_email) VALUES ('Иванов', 'Иван', 'user1@example.com');");

            // Добавляем типы номеров
            statement.execute("INSERT INTO phone_number_type (name) VALUES ('Личный'), ('Рабочий'), ('Домашний');");

            // Добавляем номера телефонов
            statement.execute("INSERT INTO phone_number (contact_id, phone_number, type_id) VALUES (1, '1234567890', 1);");
        }
    }

    @Test
    void testAddPhoneNumber() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(0, 1, "9876543210", 1);
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        assertNotNull(phoneNumber.getId());
        assertEquals(2, phoneNumberRepository.getAllPhoneNumbers().size());
    }

    @Test
    void testGetPhoneNumber() throws Exception {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);

        assertNotNull(phoneNumber);
        assertEquals("1234567890", phoneNumber.getPhoneNumber());
    }

    @Test
    void testUpdatePhoneNumber() throws Exception {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);
        phoneNumber.setPhoneNumber("1112223333");
        phoneNumberRepository.updatePhoneNumber(phoneNumber);

        PhoneNumber updatedPhoneNumber = phoneNumberRepository.getPhoneNumber(1);
        assertEquals("1112223333", updatedPhoneNumber.getPhoneNumber());
    }

    @Test
    void testDeletePhoneNumber() throws Exception {
        phoneNumberRepository.deletePhoneNumber(1);

        assertNull(phoneNumberRepository.getPhoneNumber(1));
    }
}
