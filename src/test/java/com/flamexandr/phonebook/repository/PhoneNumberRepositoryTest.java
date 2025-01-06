package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.PhoneNumber;
import org.junit.jupiter.api.*;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhoneNumberRepositoryTest {

    private PhoneNumberRepository phoneNumberRepository;

    @BeforeAll
    public void setUp() throws SQLException {
        phoneNumberRepository = new PhoneNumberRepository();

        // Очистка таблицы перед тестами
        phoneNumberRepository.deleteAllPhoneNumbers();

        // Добавление тестовых данных
        phoneNumberRepository.addPhoneNumber(new PhoneNumber(0, 1, "1234567890", 1));
        phoneNumberRepository.addPhoneNumber(new PhoneNumber(0, 2, "0987654321", 2));
    }

    @Test
    public void testAddPhoneNumber() throws SQLException {
        PhoneNumber phoneNumber = new PhoneNumber(0, 3, "111222333", 1);
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        assertNotEquals(0, phoneNumber.getId());

        PhoneNumber retrieved = phoneNumberRepository.getPhoneNumber(phoneNumber.getId());
        assertNotNull(retrieved);
        assertEquals("111222333", retrieved.getPhoneNumber());
    }

    @Test
    public void testGetPhoneNumber() throws SQLException {
        PhoneNumber phoneNumber = phoneNumberRepository.getPhoneNumber(1);
        assertNotNull(phoneNumber);
        assertEquals("1234567890", phoneNumber.getPhoneNumber());
    }

    @Test
    public void testGetAllPhoneNumbers() throws SQLException {
        List<PhoneNumber> phoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        assertNotNull(phoneNumbers);
        assertTrue(phoneNumbers.size() >= 2);
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
        PhoneNumber phoneNumber = new PhoneNumber(0, 4, "555666777", 2);
        phoneNumberRepository.addPhoneNumber(phoneNumber);

        int id = phoneNumber.getId();
        phoneNumberRepository.deletePhoneNumber(id);

        PhoneNumber deleted = phoneNumberRepository.getPhoneNumber(id);
        assertNull(deleted);
    }

    @Test
    public void testDeleteAllPhoneNumbers() throws SQLException {
        phoneNumberRepository.addPhoneNumber(new PhoneNumber(0, 1, "1234567890", 1));
        phoneNumberRepository.addPhoneNumber(new PhoneNumber(0, 2, "0987654321", 2));

        phoneNumberRepository.deleteAllPhoneNumbers();
        List<PhoneNumber> allPhoneNumbers = phoneNumberRepository.getAllPhoneNumbers();
        Assertions.assertTrue(allPhoneNumbers.isEmpty());
    }


    @AfterAll
    public void tearDown() throws SQLException {
        phoneNumberRepository.deleteAllPhoneNumbers();
    }
}
