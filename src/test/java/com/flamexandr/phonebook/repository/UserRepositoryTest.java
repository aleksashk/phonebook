package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.User;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        userRepository = new UserRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            // Очистка таблицы пользователей перед каждым тестом
            statement.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE;");
        }
    }

    @Test
    void testSaveAndFindUser() {
        User user = new User("test@example.com", "password123");
        userRepository.save(user);

        User foundUser = userRepository.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        assertNotEquals("password123", foundUser.getPassword()); // Пароль должен быть хэшированным
    }

    @Test
    void testExistsByEmail() {
        User user = new User("test@example.com", "password123");
        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("unknown@example.com"));
    }

    @Test
    void testCheckPassword() {
        // Создаем пользователя
        User user = new User("test@example.com", "password123");

        // Проверяем пароли
        assertTrue(user.checkPassword("password123")); // Пароль совпадает
        assertFalse(user.checkPassword("wrongpassword")); // Пароль не совпадает
    }
}
