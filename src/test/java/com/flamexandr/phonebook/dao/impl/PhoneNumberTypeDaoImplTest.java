package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.PhoneNumberTypeDao;
import com.flamexandr.phonebook.model.PhoneNumberType;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhoneNumberTypeDaoImplTest {

    private PhoneNumberTypeDao phoneNumberTypeDao;

    @BeforeAll
    void setup() throws Exception {
        phoneNumberTypeDao = new PhoneNumberTypeDaoImpl();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE phone_number_type RESTART IDENTITY CASCADE;");
        }
    }

    @Test
    void testSaveAndFindById() throws Exception {
        PhoneNumberType phoneNumberType = new PhoneNumberType("Личный");
        phoneNumberTypeDao.save(phoneNumberType);

        PhoneNumberType retrievedType = phoneNumberTypeDao.findById(phoneNumberType.getId());
        assertNotNull(retrievedType, "Retrieved PhoneNumberType should not be null");
        assertEquals("Личный", retrievedType.getName());
    }

    @Test
    void testFindAll() throws Exception {
        phoneNumberTypeDao.save(new PhoneNumberType("Рабочий"));
        phoneNumberTypeDao.save(new PhoneNumberType("Домашний"));

        List<PhoneNumberType> types = phoneNumberTypeDao.findAll();
        assertEquals(2, types.size());
    }

    @Test
    void testExistsById() throws Exception {
        phoneNumberTypeDao.save(new PhoneNumberType("Тестовый"));

        assertTrue(phoneNumberTypeDao.existsById(1));
        assertFalse(phoneNumberTypeDao.existsById(99));
    }

    @Test
    void testDeleteById() throws Exception {
        phoneNumberTypeDao.save(new PhoneNumberType("Удаляемый"));
        phoneNumberTypeDao.deleteById(1);

        assertFalse(phoneNumberTypeDao.existsById(1));
    }
}
