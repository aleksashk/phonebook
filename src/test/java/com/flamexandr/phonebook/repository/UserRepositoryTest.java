package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepository();
    }

    @Test
    void testSaveUser() {
        User user = new User("test@example.com", "password123");
        userRepository.save(user);

        User retrievedUser = userRepository.findByEmail("test@example.com");
        assertNotNull(retrievedUser, "User should be saved and retrievable.");
        assertEquals("test@example.com", retrievedUser.getEmail());
        assertTrue(retrievedUser.checkPassword("password123"), "Password should match.");
    }

    @Test
    void testExistsByEmail() {
        userRepository.save(new User("test@example.com", "password123"));

        assertTrue(userRepository.existsByEmail("test@example.com"), "User with this email should exist.");
        assertFalse(userRepository.existsByEmail("nonexistent@example.com"), "Nonexistent user should not exist.");
    }

    @Test
    void testFindByEmail() {
        userRepository.save(new User("test@example.com", "password123"));

        User user = userRepository.findByEmail("test@example.com");
        assertNotNull(user, "User should be found.");
        assertEquals("test@example.com", user.getEmail(), "Email should match.");
    }

    @Test
    void testFindByEmail_NotFound() {
        User user = userRepository.findByEmail("nonexistent@example.com");
        assertNull(user, "User should not be found for nonexistent email.");
    }

    @Test
    void testSaveMultipleUsers() {
        userRepository.save(new User("user1@example.com", "password1"));
        userRepository.save(new User("user2@example.com", "password2"));

        assertTrue(userRepository.existsByEmail("user1@example.com"), "User1 should exist.");
        assertTrue(userRepository.existsByEmail("user2@example.com"), "User2 should exist.");

        User user1 = userRepository.findByEmail("user1@example.com");
        User user2 = userRepository.findByEmail("user2@example.com");

        assertNotNull(user1, "User1 should be found.");
        assertNotNull(user2, "User2 should be found.");

        assertTrue(user1.checkPassword("password1"), "Password1 should match.");
        assertTrue(user2.checkPassword("password2"), "Password2 should match.");
    }
}
