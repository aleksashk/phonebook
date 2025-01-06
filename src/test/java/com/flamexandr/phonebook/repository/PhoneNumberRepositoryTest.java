package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.PhoneNumber;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhoneNumberRepositoryTest {

    private PhoneNumberRepository phoneNumberRepository;

    @BeforeAll
    public void setUp() throws SQLException {
        phoneNumberRepository = new PhoneNumberRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            // Очистка таблиц
            statement.executeUpdate("DELETE FROM phone_number");
            statement.executeUpdate("DELETE FROM contact");
            statement.executeUpdate("DELETE FROM phone_number_type");

            // Добавление данных в contact
            statement.executeUpdate("INSERT INTO contact (id, first_name, last_name) VALUES " +
                    "(1, 'Иван', 'Иванов'), " +
                    "(2, 'Мария', 'Петрова')");

            // Добавление данных в phone_number_type
            statement.executeUpdate("INSERT INTO phone_number_type (id, name) VALUES " +
                    "(1, 'Личный'), " +
                    "(2, 'Рабочий')");
        }
    }

    @BeforeEach
    public void resetPhoneNumbers() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            // Очистка таблицы phone_number
            statement.executeUpdate("DELETE FROM phone_number");

            // Сброс автоинкремента для id
            statement.executeUpdate("ALTER SEQUENCE phone_number_id_seq RESTART WITH 1");

            // Добавление тестовых данных
            phoneNumberRepository.addPhoneNumber(new PhoneNumber(0, 1, "1234567891", 1));
            phoneNumberRepository.addPhoneNumber(new PhoneNumber(0, 2, "0987654322", 2));
        }
    }

    @Test
    public void testAddPhoneNumber() throws SQLException {
        // Убедитесь, что contact_id = 3 существует
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO contact (id, first_name, last_name) VALUES (3, 'Сергей', 'Сергеев')");
        }

        PhoneNumber phoneNumber = new PhoneNumber(0, 3, "333444555", 1); // contact_id = 3
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        assertNotEquals(0, phoneNumber.getId());

        PhoneNumber retrieved = phoneNumberRepository.getPhoneNumber(phoneNumber.getId());
        assertNotNull(retrieved);
        assertEquals("333444555", retrieved.getPhoneNumber());
    }

    @Test
    public void testDeletePhoneNumber() throws SQLException {
        PhoneNumber phoneNumber = new PhoneNumber(0, 3, "555666777", 2);
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        int id = phoneNumber.getId();
        phoneNumberRepository.deletePhoneNumber(id);

        PhoneNumber deleted = phoneNumberRepository.getPhoneNumber(id);
        assertNull(deleted);
    }

    @Test
    public void testGetPhoneNumber() throws SQLException {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);
        assertNotNull(phoneNumber, "PhoneNumber with ID 1 should exist");
        assertEquals("1234567891", phoneNumber.getPhoneNumber(), "Phone number should match");
    }

    @Test
    public void testGetAllPhoneNumbers() throws SQLException {
        List<PhoneNumber> phoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        assertNotNull(phoneNumbers, "PhoneNumbers list should not be null");
        assertEquals(2, phoneNumbers.size(), "There should be 2 phone numbers");
    }

    @Test
    public void testUpdatePhoneNumber() throws SQLException {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);
        assertNotNull(phoneNumber, "PhoneNumber with ID 1 should exist before update");

        phoneNumber.setPhoneNumber("222333444");
        phoneNumberRepository.updatePhoneNumber(phoneNumber);

        PhoneNumber updated = phoneNumberRepository.getPhoneNumber(1);
        assertNotNull(updated, "PhoneNumber with ID 1 should exist after update");
        assertEquals("222333444", updated.getPhoneNumber(), "Phone number should be updated");
    }

    @Test
    public void testDeleteAllPhoneNumbers() throws SQLException {
        phoneNumberRepository.deleteAllPhoneNumbers();
        List<PhoneNumber> allPhoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        assertTrue(allPhoneNumbers.isEmpty(), "All phone numbers should be deleted");
    }

    private void logDatabaseState() throws SQLException {
        List<PhoneNumber> phoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        System.out.println("Current Phone Numbers:");
        phoneNumbers.forEach(phoneNumber ->
                System.out.println("ID: " + phoneNumber.getId() + ", ContactID: " + phoneNumber.getContactId() + ", PhoneNumber: " + phoneNumber.getPhoneNumber())
        );
    }

    @AfterAll
    public void tearDown() throws SQLException {
        phoneNumberRepository.deleteAllPhoneNumbers();
    }
}
