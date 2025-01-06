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
            statement.executeUpdate("DELETE FROM phone_number");
            statement.executeUpdate("DELETE FROM contact");
            statement.executeUpdate("DELETE FROM phone_number_type");

            // Insert test data
            statement.executeUpdate("INSERT INTO contact (id, first_name, last_name) VALUES " +
                    "(1, 'Иван', 'Иванов'), (2, 'Мария', 'Петрова')");
            statement.executeUpdate("INSERT INTO phone_number_type (id, name) VALUES " +
                    "(1, 'Личный'), (2, 'Рабочий')");
        }
    }

    @BeforeEach
    public void resetPhoneNumbers() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("DELETE FROM phone_number");
            statement.executeUpdate("ALTER SEQUENCE phone_number_id_seq RESTART WITH 1");

            phoneNumberRepository.addPhoneNumber(new PhoneNumber(0, 1, "1234567891", 1));
            phoneNumberRepository.addPhoneNumber(new PhoneNumber(0, 2, "0987654322", 2));
        }
    }

    @Test
    public void testAddPhoneNumber() throws SQLException {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("INSERT INTO contact (id, first_name, last_name) VALUES (3, 'Сергей', 'Сергеев')");
        }

        PhoneNumber phoneNumber = new PhoneNumber(0, 3, "333444555", 1);
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        assertNotEquals(0, phoneNumber.getId());
        PhoneNumber retrieved = phoneNumberRepository.getPhoneNumber(phoneNumber.getId());
        assertNotNull(retrieved);
        assertEquals("333444555", retrieved.getPhoneNumber());
    }

    @Test
    public void testGetPhoneNumber() throws SQLException {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);
        assertNotNull(phoneNumber);
        assertEquals("1234567891", phoneNumber.getPhoneNumber());
    }

    @Test
    public void testGetAllPhoneNumbers() throws SQLException {
        List<PhoneNumber> phoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        assertNotNull(phoneNumbers);
        assertEquals(2, phoneNumbers.size());
    }

    @Test
    public void testUpdatePhoneNumber() throws SQLException {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);
        assertNotNull(phoneNumber);

        phoneNumber.setPhoneNumber("222333444");
        phoneNumberRepository.updatePhoneNumber(phoneNumber);

        PhoneNumber updated = phoneNumberRepository.getPhoneNumber(1);
        assertEquals("222333444", updated.getPhoneNumber());
    }

    @Test
    public void testDeletePhoneNumber() throws SQLException {
        PhoneNumber phoneNumber = new PhoneNumber(0, 1, "555666777", 2);
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        int id = phoneNumber.getId();
        phoneNumberRepository.deletePhoneNumber(id);

        PhoneNumber deleted = phoneNumberRepository.getPhoneNumber(id);
        assertNull(deleted);
    }

    @Test
    public void testDeleteAllPhoneNumbers() throws SQLException {
        phoneNumberRepository.deleteAllPhoneNumbers();
        List<PhoneNumber> phoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        assertTrue(phoneNumbers.isEmpty());
    }

    @AfterAll
    public void tearDown() throws SQLException {
        phoneNumberRepository.deleteAllPhoneNumbers();
    }
}
