package com.flamexandr.phonebook.dao;

import com.flamexandr.phonebook.model.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {

    Category findById(int id) throws SQLException;

    List<Category> findAll() throws SQLException;

    void addCategory(Category category) throws SQLException;

    void updateCategory(Category category) throws SQLException;

    boolean deleteCategory(int id) throws SQLException;

}
