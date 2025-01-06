package com.flamexandr.phonebook.repository;

import com.flamexandr.phonebook.model.Category;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryRepositoryTest {

    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() throws Exception {
        categoryRepository = new CategoryRepository();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Полная очистка всех таблиц
            statement.execute("TRUNCATE TABLE contact_category, contact, category RESTART IDENTITY CASCADE");

            // Инициализация тестовых данных
            statement.execute("INSERT INTO category (name) VALUES ('Друзья'), ('Работа'), ('Семья')");
        }
    }

    @Test
    void testAddCategory() throws Exception {
        Category category = new Category(0, "Хобби");
        categoryRepository.addCategory(category);

        List<Category> categories = categoryRepository.getAllCategories();
        assertEquals(4, categories.size());
        assertTrue(categories.stream().anyMatch(cat -> "Хобби".equals(cat.getName())));
    }

    @Test
    void testGetCategoryById() throws Exception {
        Category category = categoryRepository.getCategoryById(1);
        assertNotNull(category);
        assertEquals("Друзья", category.getName());
    }

    @Test
    void testGetCategoryById_NotFound() throws Exception {
        Category category = categoryRepository.getCategoryById(999);
        assertNull(category);
    }

    @Test
    void testGetAllCategories() throws Exception {
        List<Category> categories = categoryRepository.getAllCategories();
        assertEquals(3, categories.size());
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category updatedCategory = new Category(1, "Коллеги");
        categoryRepository.updateCategory(updatedCategory);

        Category category = categoryRepository.getCategoryById(1);
        assertNotNull(category);
        assertEquals("Коллеги", category.getName());
    }

    @Test
    void testUpdateCategory_NotFound() throws Exception {
        Category updatedCategory = new Category(999, "Неизвестная");
        categoryRepository.updateCategory(updatedCategory);

        Category category = categoryRepository.getCategoryById(999);
        assertNull(category);
    }

    @Test
    void testDeleteCategory() throws Exception {
        categoryRepository.deleteCategory(1);
        Category category = categoryRepository.getCategoryById(1);
        assertNull(category);

        List<Category> categories = categoryRepository.getAllCategories();
        assertEquals(2, categories.size());
    }

    @Test
    void testDeleteCategory_NotFound() throws Exception {
        categoryRepository.deleteCategory(999); // Should not throw an exception
    }
}
