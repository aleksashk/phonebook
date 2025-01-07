-- Удаление таблиц, если они существуют
DROP TABLE IF EXISTS contact_category CASCADE;
DROP TABLE IF EXISTS phone_number CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS contact CASCADE;
DROP TABLE IF EXISTS phone_number_type CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     password VARCHAR(255) NOT NULL
);

-- Создание таблицы контактов
CREATE TABLE IF NOT EXISTS contact (
                                       id SERIAL PRIMARY KEY,
                                       last_name VARCHAR(255) NOT NULL DEFAULT '',
                                       first_name VARCHAR(255) NOT NULL DEFAULT '',
                                       user_id INT NOT NULL REFERENCES users(id) ON DELETE CASCADE
);

-- Создание таблицы категорий
CREATE TABLE IF NOT EXISTS category (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL UNIQUE DEFAULT ''
);

-- Создание таблицы типов телефонных номеров
CREATE TABLE IF NOT EXISTS phone_number_type (
                                                 id SERIAL PRIMARY KEY,
                                                 name VARCHAR(255) NOT NULL UNIQUE DEFAULT ''
);

-- Создание таблицы телефонных номеров
CREATE TABLE IF NOT EXISTS phone_number (
                                            id SERIAL PRIMARY KEY,
                                            contact_id INT NOT NULL REFERENCES contact(id) ON DELETE CASCADE,
                                            phone_number VARCHAR(15) NOT NULL UNIQUE,
                                            type_id INT NOT NULL REFERENCES phone_number_type(id)
);

-- Создание таблицы связи контактов и категорий
CREATE TABLE IF NOT EXISTS contact_category (
                                                id SERIAL PRIMARY KEY,
                                                contact_id INT NOT NULL REFERENCES contact(id) ON DELETE CASCADE,
                                                category_id INT NOT NULL REFERENCES category(id)
);

-- Вставка тестовых данных
-- Добавление пользователей
INSERT INTO users (email, password) VALUES ('pavel@example.com', 'password123'), ('nastya@example.com', 'password456');

-- Добавление контактов
INSERT INTO contact (first_name, last_name, user_id)
VALUES ('Павел', 'Титов', 1), ('Анастасия', 'Иванова', 2);

-- Добавление категорий
INSERT INTO category (name) VALUES ('Друзья'), ('Одноклассники'), ('Родственники');

-- Добавление типов телефонных номеров
INSERT INTO phone_number_type (name) VALUES ('Личный'), ('Рабочий'), ('Домашний');

-- Добавление телефонных номеров
INSERT INTO phone_number (contact_id, phone_number, type_id)
VALUES (1, '46237648234', 1), (2, '324234234234', 2);

-- Добавление связей контактов и категорий
INSERT INTO contact_category (contact_id, category_id) VALUES (1, 1), (2, 2);
