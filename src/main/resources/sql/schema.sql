-- Удаление таблиц, если они существуют
DROP TABLE IF EXISTS contact_category CASCADE;
DROP TABLE IF EXISTS phone_number CASCADE;
DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS contact CASCADE;
DROP TABLE IF EXISTS phone_number_type CASCADE;
DROP TABLE IF EXISTS users CASCADE;

-- Создание таблицы пользователей
CREATE TABLE IF NOT EXISTS users (
                                     email VARCHAR(255) PRIMARY KEY,
                                     password VARCHAR(255) NOT NULL
);

-- Создание таблицы контактов
CREATE TABLE IF NOT EXISTS contact (
                                       id SERIAL PRIMARY KEY,
                                       last_name VARCHAR(255) NOT NULL,
                                       first_name VARCHAR(255) NOT NULL,
                                       user_email VARCHAR(255) NOT NULL REFERENCES users(email) ON DELETE CASCADE
);

-- Создание таблицы категорий
CREATE TABLE IF NOT EXISTS category (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL UNIQUE
);

-- Создание таблицы типов телефонных номеров
CREATE TABLE IF NOT EXISTS phone_number_type (
                                                 id SERIAL PRIMARY KEY,
                                                 name VARCHAR(255) NOT NULL UNIQUE
);

-- Создание таблицы телефонных номеров
CREATE TABLE IF NOT EXISTS phone_number (
                                            id SERIAL PRIMARY KEY,
                                            contact_id INT NOT NULL REFERENCES contact(id) ON DELETE CASCADE,
                                            phone_number VARCHAR(20) NOT NULL UNIQUE,
                                            type_id INT NOT NULL REFERENCES phone_number_type(id) ON DELETE CASCADE
);

-- Создание таблицы связи контактов и категорий
CREATE TABLE IF NOT EXISTS contact_category (
                                                id SERIAL PRIMARY KEY,
                                                contact_id INT NOT NULL REFERENCES contact(id) ON DELETE CASCADE,
                                                category_id INT NOT NULL REFERENCES category(id)
);

-- Вставка тестовых данных
INSERT INTO users (email, password) VALUES ('pavel@example.com', md5('password123')), ('nastya@example.com', md5('password456'));

INSERT INTO contact (first_name, last_name, user_email) VALUES ('Павел', 'Титов', 'pavel@example.com'), ('Анастасия', 'Иванова', 'nastya@example.com');

INSERT INTO category (name) VALUES ('Друзья'), ('Одноклассники'), ('Родственники');

INSERT INTO phone_number_type (name) VALUES ('Личный'), ('Рабочий'), ('Домашний');

INSERT INTO phone_number (contact_id, phone_number, type_id) VALUES (1, '46237648234', 1), (2, '324234234234', 2);

INSERT INTO contact_category (contact_id, category_id) VALUES (1, 1), (2, 2);
