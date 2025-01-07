package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.PhoneNumberType;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PhoneNumberTypeRepositoryTest {

    private PhoneNumberTypeRepository phoneNumberTypeRepository;

    @BeforeAll
    public void setUp() throws SQLException {
        phoneNumberTypeRepository = new PhoneNumberTypeRepository();
    }

    private void logCurrentState() throws SQLException {
        List<PhoneNumberType> types = phoneNumberTypeRepository.getAllPhoneNumberTypes();
        System.out.println("Текущие данные в phone_number_type:");
        types.forEach(type -> System.out.println("ID: " + type.getId() + ", Name: " + type.getName()));
    }

    @BeforeEach
    public void prepareTestData() throws SQLException {
        phoneNumberTypeRepository.deleteAllPhoneNumberTypes();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate("ALTER SEQUENCE phone_number_type_id_seq RESTART WITH 1");
        }

        phoneNumberTypeRepository.addPhoneNumberType(new PhoneNumberType(0, "Личный"));
        phoneNumberTypeRepository.addPhoneNumberType(new PhoneNumberType(0, "Рабочий"));

        logCurrentState();
    }

    @Test
    public void testAddPhoneNumberType() throws SQLException {
        PhoneNumberType type = new PhoneNumberType(0, "Домашний");
        phoneNumberTypeRepository.addPhoneNumberType(type);

        assertNotEquals(0, type.getId());
        PhoneNumberType retrieved = phoneNumberTypeRepository.getPhoneNumberType(type.getId());
        assertNotNull(retrieved);
        assertEquals("Домашний", retrieved.getName());
    }

    @Test
    public void testGetPhoneNumberType() throws SQLException {
        List<PhoneNumberType> types = phoneNumberTypeRepository.getAllPhoneNumberTypes();
        PhoneNumberType type = types.stream()
                .filter(t -> "Личный".equals(t.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(type, "PhoneNumberType с именем 'Личный' должен существовать");
        assertEquals("Личный", type.getName());
    }

    @Test
    public void testGetAllPhoneNumberTypes() throws SQLException {
        List<PhoneNumberType> types = phoneNumberTypeRepository.getAllPhoneNumberTypes();
        assertNotNull(types);
        assertEquals(2, types.size());
    }

    @Test
    public void testUpdatePhoneNumberType() throws SQLException {
        List<PhoneNumberType> types = phoneNumberTypeRepository.getAllPhoneNumberTypes();
        PhoneNumberType type = types.stream()
                .filter(t -> "Личный".equals(t.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(type, "PhoneNumberType с именем 'Личный' должен существовать");

        type.setName("Обновленный тип");
        phoneNumberTypeRepository.updatePhoneNumberType(type);

        PhoneNumberType updated = phoneNumberTypeRepository.getPhoneNumberType(type.getId());
        assertEquals("Обновленный тип", updated.getName());
    }

    @Test
    public void testDeletePhoneNumberType() throws SQLException {
        PhoneNumberType type = new PhoneNumberType(0, "Временный");
        phoneNumberTypeRepository.addPhoneNumberType(type);

        assertNotEquals(0, type.getId());
        phoneNumberTypeRepository.deletePhoneNumberType(type.getId());

        PhoneNumberType deleted = phoneNumberTypeRepository.getPhoneNumberType(type.getId());
        assertNull(deleted);
    }

    @AfterEach
    public void logState() throws SQLException {
        logDatabaseState();
    }

    @AfterAll
    public void tearDown() throws SQLException {
        phoneNumberTypeRepository.deleteAllPhoneNumberTypes();
    }

    private void logDatabaseState() throws SQLException {
        List<PhoneNumberType> types = phoneNumberTypeRepository.getAllPhoneNumberTypes();
        System.out.println("Current PhoneNumberTypes in Database:");
        types.forEach(type -> System.out.println("ID: " + type.getId() + ", Name: " + type.getName()));
    }
}