package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.User;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() throws Exception {
        userRepository = new UserRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            // Очистка таблицы users
            statement.execute("TRUNCATE TABLE users RESTART IDENTITY CASCADE;");
        }
    }

    @Test
    void testSaveUser() {
        User user = new User("unique@example.com", "password123");
        userRepository.save(user);
        assertNotNull(user.getId());
    }

    @Test
    void testSaveMultipleUsers() {
        userRepository.save(new User("user1@example.com", "password1"));
        userRepository.save(new User("user2@example.com", "password2"));
        assertNotNull(userRepository.findByEmail("user1@example.com"));
        assertNotNull(userRepository.findByEmail("user2@example.com"));
    }

    @Test
    void testFindByEmail() {
        User user = new User("test@example.com", "password");
        userRepository.save(user);
        User found = userRepository.findByEmail("test@example.com");
        assertNotNull(found);
        assertEquals("test@example.com", found.getEmail());
    }

    @Test
    void testExistsByEmail() {
        userRepository.save(new User("exists@example.com", "password"));
        assertTrue(userRepository.existsByEmail("exists@example.com"));
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"));
    }
}
