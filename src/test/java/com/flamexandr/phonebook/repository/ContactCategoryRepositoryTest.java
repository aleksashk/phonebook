package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.ContactCategory;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ContactCategoryRepositoryTest {

    private ContactCategoryRepository contactCategoryRepository;

    @BeforeEach
    void setUp() throws Exception {
        contactCategoryRepository = new ContactCategoryRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Создайте таблицы, если их нет
            statement.execute("""
            CREATE TABLE IF NOT EXISTS users (
                id SERIAL PRIMARY KEY,
                email VARCHAR(255) NOT NULL UNIQUE,
                password VARCHAR(255) NOT NULL
            );
            CREATE TABLE IF NOT EXISTS contact (
                id SERIAL PRIMARY KEY,
                last_name VARCHAR(255) NOT NULL DEFAULT '',
                first_name VARCHAR(255) NOT NULL DEFAULT '',
                user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
            );
            CREATE TABLE IF NOT EXISTS category (
                id SERIAL PRIMARY KEY,
                name VARCHAR(255) NOT NULL UNIQUE
            );
            CREATE TABLE IF NOT EXISTS contact_category (
                id SERIAL PRIMARY KEY,
                contact_id INT NOT NULL REFERENCES contact(id) ON DELETE CASCADE,
                category_id INT NOT NULL REFERENCES category(id)
            );
        """);

            // Очистите и заполните таблицы тестовыми данными
            statement.execute("TRUNCATE TABLE contact_category, contact, category, users RESTART IDENTITY CASCADE;");
            statement.execute("INSERT INTO users (email, password) VALUES ('user1@example.com', 'password1');");

            // Добавляем контакты с корректным user_id
            statement.execute("INSERT INTO contact (last_name, first_name, user_id) VALUES ('Иванов', 'Иван', 1);");
            statement.execute("INSERT INTO contact (last_name, first_name, user_id) VALUES ('Петров', 'Петр', 1);");

            // Добавляем категории
            statement.execute("INSERT INTO category (name) VALUES ('Друзья'), ('Работа');");

            // Добавляем связь между контактами и категориями
            statement.execute("INSERT INTO contact_category (contact_id, category_id) VALUES (1, 1), (2, 2);");
        }
    }

    @Test
    void testAddContactCategory() throws Exception {
        ContactCategory contactCategory = new ContactCategory(0, 1, 2);
        contactCategoryRepository.addContactCategory(contactCategory);

        List<ContactCategory> contactCategories = contactCategoryRepository.getAllContactCategories();
        assertEquals(3, contactCategories.size());
        assertTrue(contactCategories.stream().anyMatch(cc -> cc.getContactId() == 1 && cc.getCategoryId() == 2));
    }

    @Test
    void testGetContactCategory() throws Exception {
        ContactCategory contactCategory = contactCategoryRepository.getContactCategory(1);
        assertNotNull(contactCategory);
        assertEquals(1, contactCategory.getContactId());
        assertEquals(1, contactCategory.getCategoryId());
    }

    @Test
    void testGetAllContactCategories() throws Exception {
        List<ContactCategory> contactCategories = contactCategoryRepository.getAllContactCategories();
        assertEquals(2, contactCategories.size());
    }

    @Test
    void testUpdateContactCategory() throws Exception {
        ContactCategory contactCategory = new ContactCategory(1, 1, 2);
        contactCategoryRepository.updateContactCategory(contactCategory);

        ContactCategory updated = contactCategoryRepository.getContactCategory(1);
        assertEquals(2, updated.getCategoryId());
    }

    @Test
    void testDeleteContactCategory() throws Exception {
        contactCategoryRepository.deleteContactCategory(1);

        List<ContactCategory> contactCategories = contactCategoryRepository.getAllContactCategories();
        assertEquals(1, contactCategories.size());
    }
}
