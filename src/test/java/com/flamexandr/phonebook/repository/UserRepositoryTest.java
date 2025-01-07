package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.User;
import org.apache.commons.codec.digest.DigestUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    @Test
    void testSaveAndFindUser() {
        UserRepository userRepository = new UserRepository();
        User user = new User("test@example.com", "password123");

        userRepository.save(user);
        User foundUser = userRepository.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
        assertNotEquals("password123", foundUser.getPassword()); // Пароль должен быть хэшированным
    }

    @Test
    void testExistsByEmail() {
        UserRepository userRepository = new UserRepository();
        User user = new User("test@example.com", "password123");

        userRepository.save(user);

        assertTrue(userRepository.existsByEmail("test@example.com"));
        assertFalse(userRepository.existsByEmail("unknown@example.com"));
    }

    @Test
    void testCheckPassword() {
        User user = new User("test@example.com", "password123");

        // Хэшируем пароль
        user.setPassword(DigestUtils.md5Hex(user.getPassword()));

        assertTrue(user.checkPassword("password123"));
        assertFalse(user.checkPassword("wrongpassword"));
    }
}
