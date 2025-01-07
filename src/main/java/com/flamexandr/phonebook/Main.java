package com.flamexandr.phonebook;

import com.flamexandr.phonebook.model.Contact;
import com.flamexandr.phonebook.model.User;
import com.flamexandr.phonebook.repository.ContactRepository;
import com.flamexandr.phonebook.repository.UserRepository;
import com.flamexandr.phonebook.util.SchemaInitializer;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SchemaInitializer.executeSchema();

        UserRepository userRepository = new UserRepository();
        ContactRepository contactRepository = new ContactRepository();
        Scanner scanner = new Scanner(System.in);

        User currentUser = null;

        while (currentUser == null) {
            System.out.println("Выберите действие:");
            System.out.println("1. Регистрация");
            System.out.println("2. Вход");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Попробуйте снова.");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    System.out.println("Введите email:");
                    String email = scanner.nextLine();
                    if (userRepository.existsByEmail(email)) {
                        System.out.println("Пользователь с таким email уже существует.");
                        break;
                    }

                    System.out.println("Введите пароль:");
                    String password = scanner.nextLine();
                    userRepository.save(new User(email, password));
                    System.out.println("Регистрация успешна!");
                }
                case 2 -> {
                    System.out.println("Введите email:");
                    String email = scanner.nextLine();
                    User user = userRepository.findByEmail(email);
                    if (user == null) {
                        System.out.println("Пользователь не найден.");
                        break;
                    }

                    System.out.println("Введите пароль:");
                    String password = scanner.nextLine();
                    if (user.checkPassword(password)) {
                        currentUser = user;
                        System.out.println("Вход успешен. Добро пожаловать, " + user.getEmail() + "!");
                    } else {
                        System.out.println("Неверный пароль.");
                    }
                }
                default -> System.out.println("Неверный выбор.");
            }
        }

        System.out.println("Добро пожаловать в телефонную книгу!");

        try {
            Contact contact = new Contact(
                    0,
                    "Петров",
                    "Петр",
                    currentUser.getId() // Передаем ID авторизованного пользователя
            );

            contactRepository.addContact(contact);

            List<Contact> contacts = contactRepository.getContactsByUserId(currentUser.getId());
            contacts.forEach(c -> System.out.println(c.getFirstName() + " " + c.getLastName()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
