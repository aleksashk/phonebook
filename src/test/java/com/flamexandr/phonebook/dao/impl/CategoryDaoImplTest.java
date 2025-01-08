package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.dao.CategoryDao;
import com.flamexandr.phonebook.model.Category;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CategoryDaoImplTest {
    private CategoryDao categoryDao;

    @BeforeEach
    void setUp() throws Exception {
        categoryDao = new CategoryDaoImpl();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {

            // Очищаем таблицу перед каждым тестом
            statement.execute("TRUNCATE TABLE category RESTART IDENTITY CASCADE;");

            // Добавляем тестовые данные
            statement.execute("INSERT INTO category (\"name\") VALUES ('Friends'), ('Work'), ('Family');");
        }
    }

    @Test
    void testFindById() throws Exception {
        Category category = categoryDao.findById(1);

        assertNotNull(category);
        assertEquals(1, category.getId());
        assertEquals("Friends", category.getName());
    }

    @Test
    void testFindAll() throws Exception {
        List<Category> categories = categoryDao.findAll();

        assertNotNull(categories);
        assertEquals(3, categories.size());

        assertEquals("Friends", categories.get(0).getName());
        assertEquals("Work", categories.get(1).getName());
        assertEquals("Family", categories.get(2).getName());
    }

    @Test
    void testAddCategory() throws Exception {
        Category newCategory = new Category(0, "Hobby");
        categoryDao.addCategory(newCategory);

        assertNotEquals(0, newCategory.getId());
        Category addedCategory = categoryDao.findById(newCategory.getId());
        assertNotNull(addedCategory);
        assertEquals("Hobby", addedCategory.getName());
    }

    @Test
    void testUpdateCategory() throws Exception {
        Category category = categoryDao.findById(1);
        assertNotNull(category);

        category.setName("Best Friends");
        categoryDao.updateCategory(category);

        Category updatedCategory = categoryDao.findById(1);
        assertNotNull(updatedCategory);
        assertEquals("Best Friends", updatedCategory.getName());
    }

    @Test
    void testDeleteCategory() throws Exception {
        boolean isDeleted = categoryDao.deleteCategory(1);

        assertTrue(isDeleted);

        Category deletedCategory = categoryDao.findById(1);
        assertNull(deletedCategory);

        // Убедимся, что другие категории остались
        List<Category> remainingCategories = categoryDao.findAll();
        assertEquals(2, remainingCategories.size());
    }
}