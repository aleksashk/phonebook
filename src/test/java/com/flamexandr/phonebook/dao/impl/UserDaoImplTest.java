package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.UserDao;
import com.flamexandr.phonebook.model.User;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserDaoImplTest {

    private UserDao userDao;

    @BeforeAll
    void setup() throws Exception {
        userDao = new UserDaoImpl();
        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            // Очистка таблицы пользователей перед запуском тестов
            statement.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE;");
        }
    }

    @Test
    void testSaveAndFindByEmail() throws Exception {
        // Создаем пользователя
        User user = new User("test@example.com", "password123");
        userDao.save(user);

        User retrievedUser = userDao.findByEmail("test@example.com");
        assertNotNull(retrievedUser);
        assertEquals("test@example.com", retrievedUser.getEmail());

        System.out.println("Хеш из базы данных: " + retrievedUser.getPassword());
        System.out.println("Ожидаемый хеш: " + user.getPassword());

        assertEquals(user.getPassword(), retrievedUser.getPassword(), "Хэш пароля должен совпадать");
    }

    @Test
    void testExistsByEmail() throws Exception {
        // Создаем пользователя
        User user = new User("exists@example.com", "password123");
        userDao.save(user);

        // Проверяем существование пользователя
        assertTrue(userDao.existsByEmail("exists@example.com"), "Email должен существовать");
        assertFalse(userDao.existsByEmail("unknown@example.com"), "Неизвестный email не должен существовать");
    }

    @Test
    void testUserPasswordHashing() {
        // Проверяем, что хэширование пароля работает правильно
        User user = new User("test@example.com", "password123");
        String expectedHash = "482c811da5d5b4bc6d497ffa98491e38"; // MD5-хэш для "password123"
        String actualHash = user.getPassword();

        System.out.println("Ожидаемый хеш: " + expectedHash);
        System.out.println("Фактический хеш: " + actualHash);

        assertEquals(expectedHash, actualHash, "Хэш пароля должен быть корректным");
    }
}
