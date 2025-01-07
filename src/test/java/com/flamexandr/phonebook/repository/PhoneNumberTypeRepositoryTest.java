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
    private PhoneNumberRepository phoneNumberRepository;

    @BeforeEach
    void setUp() throws Exception {
        phoneNumberTypeRepository = new PhoneNumberTypeRepository();
        phoneNumberRepository = new PhoneNumberRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Очистка таблиц
            statement.execute("TRUNCATE TABLE phone_number RESTART IDENTITY CASCADE;");
            statement.execute("TRUNCATE TABLE phone_number_type RESTART IDENTITY CASCADE;");

            // Добавление тестовых данных
            statement.execute("INSERT INTO phone_number_type (name) VALUES ('Личный'), ('Рабочий'), ('Домашний');");
            statement.execute("INSERT INTO phone_number (contact_id, phone_number, type_id) VALUES (1, '1234567890', 1);");
        }
    }

    @Test
    public void testAddPhoneNumberType() throws SQLException {
        PhoneNumberType type = new PhoneNumberType(0, "Мобильный");
        phoneNumberTypeRepository.addPhoneNumberType(type);

        assertNotEquals(0, type.getId(), "ID должен быть сгенерирован");
        PhoneNumberType retrieved = phoneNumberTypeRepository.getPhoneNumberType(type.getId());
        assertNotNull(retrieved, "Добавленный тип должен быть найден");
        assertEquals("Мобильный", retrieved.getName());
    }

    @Test
    public void testGetPhoneNumberType() throws SQLException {
        PhoneNumberType type = phoneNumberTypeRepository.getPhoneNumberType(1);
        assertNotNull(type, "Тип телефонного номера с ID 1 должен существовать");
        assertEquals("Личный", type.getName());
    }

    @Test
    public void testGetAllPhoneNumberTypes() throws SQLException {
        List<PhoneNumberType> types = phoneNumberTypeRepository.getAllPhoneNumberTypes();
        assertNotNull(types, "Список типов не должен быть null");
        assertEquals(3, types.size(), "Должно быть 3 типа телефонных номеров");
    }

    @Test
    public void testUpdatePhoneNumberType() throws SQLException {
        PhoneNumberType type = phoneNumberTypeRepository.getPhoneNumberType(1);
        assertNotNull(type, "Тип телефонного номера с ID 1 должен существовать");

        type.setName("Обновленный тип");
        phoneNumberTypeRepository.updatePhoneNumberType(type);

        PhoneNumberType updated = phoneNumberTypeRepository.getPhoneNumberType(1);
        assertEquals("Обновленный тип", updated.getName(), "Имя должно обновиться");
    }

    @Test
    public void testDeletePhoneNumberType() throws SQLException {
        // Удаляем зависимые записи из phone_number
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("DELETE FROM phone_number WHERE type_id = 1;");
        }

        phoneNumberTypeRepository.deletePhoneNumberType(1);
        PhoneNumberType deleted = phoneNumberTypeRepository.getPhoneNumberType(1);
        assertNull(deleted, "Тип телефонного номера с ID 1 должен быть удален");
    }

    @AfterEach
    void tearDown() throws Exception {
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Удаление всех записей
            statement.execute("TRUNCATE TABLE phone_number RESTART IDENTITY CASCADE;");
            statement.execute("TRUNCATE TABLE phone_number_type RESTART IDENTITY CASCADE;");
        }
    }
}
