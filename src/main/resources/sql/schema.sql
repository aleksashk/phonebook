DROP TABLE IF EXISTS phone_number CASCADE;
DROP TABLE IF EXISTS contact CASCADE;
DROP TABLE IF EXISTS phone_number_type CASCADE;


CREATE TABLE IF NOT EXISTS contact (
                                       id SERIAL PRIMARY KEY,
                                       last_name VARCHAR(255) NOT NULL DEFAULT '',
                                       first_name VARCHAR(255) NOT NULL DEFAULT ''
);

CREATE TABLE IF NOT EXISTS category (
                                        id SERIAL PRIMARY KEY,
                                        name VARCHAR(255) NOT NULL UNIQUE DEFAULT ''
);

CREATE TABLE IF NOT EXISTS phone_number_type (
                                                 id SERIAL PRIMARY KEY,
                                                 name VARCHAR(255) NOT NULL UNIQUE DEFAULT ''
);

CREATE TABLE IF NOT EXISTS phone_number (
                                            id SERIAL PRIMARY KEY, -- SERIAL автоматически создаёт последовательность и привязывает её к столбцу
                                            contact_id INT NOT NULL REFERENCES contact(id) ON DELETE CASCADE,
                                            phone_number VARCHAR(15) NOT NULL UNIQUE,
                                            type_id INT NOT NULL REFERENCES phone_number_type(id)
);

CREATE TABLE contact_category (
                                  contact_id INT NOT NULL REFERENCES contact(id) ON DELETE CASCADE,
                                  category_id INT NOT NULL REFERENCES category(id),
                                  PRIMARY KEY (contact_id, category_id)
);


INSERT INTO phone_number_type (name) VALUES ('Личный'), ('Рабочий'), ('Домашний');

INSERT INTO contact (first_name, last_name) VALUES ('Павел', 'Титов'), ('Анастасия', 'Иванова');

INSERT INTO category (name) VALUES ('Друзья'), ('Одноклассники'), ('Родственники');

INSERT INTO phone_number (contact_id, phone_number, type_id)
VALUES (1, '46237648234', 1), (2, '324234234234', 2);

INSERT INTO contact_category (contact_id, category_id) VALUES (1, 1), (2, 2);
