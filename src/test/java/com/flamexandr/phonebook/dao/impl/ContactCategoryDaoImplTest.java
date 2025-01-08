package com.flamexandr.phonebook.dao.impl;

import com.flamexandr.phonebook.model.ContactCategory;
import com.flamexandr.phonebook.util.DatabaseUtil;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ContactCategoryDaoImplTest {
    private ContactCategoryDaoImpl contactCategoryDao;

    @BeforeAll
    void setup() throws Exception {
        contactCategoryDao = new ContactCategoryDaoImpl();

        try (Connection connection = DatabaseUtil.getConnection();
             Statement statement = connection.createStatement()) {
            statement.execute("TRUNCATE TABLE contact_category RESTART IDENTITY CASCADE;");
            statement.execute("INSERT INTO contact_category (contact_id, category_id) VALUES (1, 1), (2, 2);");
        }
    }

    @Test
    void testFindById() throws Exception {
        ContactCategory contactCategory = contactCategoryDao.findById(1);
        assertNotNull(contactCategory);
        assertEquals(1, contactCategory.getContactId());
        assertEquals(1, contactCategory.getCategoryId());
    }

    @Test
    void testFindAll() throws Exception {
        List<ContactCategory> contactCategories = contactCategoryDao.findAll();
        assertEquals(2, contactCategories.size());
    }

    @Test
    void testAddContactCategory() throws Exception {
        ContactCategory contactCategory = new ContactCategory(0, 3, 3);
        contactCategoryDao.addContactCategory(contactCategory);
        assertNotEquals(0, contactCategory.getId());
    }

    @Test
    void testUpdateContactCategory() throws Exception {
        ContactCategory contactCategory = contactCategoryDao.findById(1);
        contactCategory.setCategoryId(5);
        contactCategoryDao.updateContactCategory(contactCategory);

        ContactCategory updated = contactCategoryDao.findById(1);
        assertEquals(5, updated.getCategoryId());
    }

    @Test
    void testDeleteContactCategory() throws Exception {
        boolean isDeleted = contactCategoryDao.deleteContactCategory(1);
        assertTrue(isDeleted);
        assertNull(contactCategoryDao.findById(1));
    }
}
