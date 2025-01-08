package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.PhoneNumberDao;
import com.flamexandr.phonebook.model.PhoneNumber;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhoneNumberDaoImplTest {

    private PhoneNumberDao phoneNumberDao;

    @BeforeAll
    public void setUp() throws Exception {
        phoneNumberDao = new PhoneNumberDaoImpl();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Очистка таблицы перед тестами
            statement.execute("TRUNCATE TABLE phone_number RESTART IDENTITY CASCADE;");

            // Добавление тестовых данных
            statement.execute("INSERT INTO phone_number (contact_id, phone_number, type_id) VALUES (1, '1234567890', 1);");
            statement.execute("INSERT INTO phone_number (contact_id, phone_number, type_id) VALUES (2, '9876543210', 2);");
        }
    }

    @Test
    public void testFindById() throws Exception {
        PhoneNumber phoneNumber = phoneNumberDao.findById(1);
        assertNotNull(phoneNumber);
        assertEquals(1, phoneNumber.getContactId());
        assertEquals("1234567890", phoneNumber.getPhoneNumber());
        assertEquals(1, phoneNumber.getTypeId());
    }

    @Test
    public void testFindAll() throws Exception {
        List<PhoneNumber> phoneNumbers = phoneNumberDao.findAll();
        assertNotNull(phoneNumbers);
        assertEquals(2, phoneNumbers.size());
    }

    @Test
    public void testAddPhoneNumber() throws Exception {
        PhoneNumber phoneNumber = new PhoneNumber(0, 3, "5555555555", 3);
        phoneNumberDao.addPhoneNumber(phoneNumber);

        assertNotEquals(0, phoneNumber.getId());

        PhoneNumber insertedPhoneNumber = phoneNumberDao.findById(phoneNumber.getId());
        assertNotNull(insertedPhoneNumber);
        assertEquals("5555555555", insertedPhoneNumber.getPhoneNumber());
        assertEquals(3, insertedPhoneNumber.getTypeId());
    }

    @Test
    public void testUpdatePhoneNumber() throws Exception {
        PhoneNumber phoneNumber = phoneNumberDao.findById(1);
        assertNotNull(phoneNumber);

        phoneNumber.setPhoneNumber("1111111111");
        phoneNumberDao.updatePhoneNumber(phoneNumber);

        PhoneNumber updatedPhoneNumber = phoneNumberDao.findById(1);
        assertEquals("1111111111", updatedPhoneNumber.getPhoneNumber());
    }

    @Test
    public void testDeletePhoneNumber() throws Exception {
        boolean deleted = phoneNumberDao.deletePhoneNumber(1);
        assertTrue(deleted);

        PhoneNumber deletedPhoneNumber = phoneNumberDao.findById(1);
        assertNull(deletedPhoneNumber);
    }

    @Test
    public void testDeleteAllPhoneNumbers() throws Exception {
        phoneNumberDao.deleteAllPhoneNumbers();
        List<PhoneNumber> phoneNumbers = phoneNumberDao.findAll();
        assertTrue(phoneNumbers.isEmpty());
    }

    @AfterAll
    public void tearDown() throws Exception {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE phone_number RESTART IDENTITY CASCADE;");
        }
    }
}
