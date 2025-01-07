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

        User currentUser = authenticateUser(userRepository, scanner);

        System.out.println("Добро пожаловать в телефонную книгу!");
        boolean running = true;

        while (running) {
            System.out.println("\nВыберите действие:");
            System.out.println("1. Добавить контакт");
            System.out.println("2. Показать все контакты");
            System.out.println("3. Выйти");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Неверный ввод. Попробуйте снова.");
                continue;
            }

            switch (choice) {
                case 1 -> addContact(contactRepository, currentUser, scanner);
                case 2 -> listContacts(contactRepository, currentUser);
                case 3 -> running = false;
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
    }

    private static User authenticateUser(UserRepository userRepository, Scanner scanner) {
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
                default -> System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }

        return currentUser;
    }

    private static void addContact(ContactRepository contactRepository, User user, Scanner scanner) {
        System.out.println("Введите фамилию:");
        String lastName = scanner.nextLine();
        System.out.println("Введите имя:");
        String firstName = scanner.nextLine();

        Contact contact = new Contact(0, lastName, firstName, user.getEmail());
        contactRepository.addContact(contact);
        System.out.println("Контакт успешно добавлен!");
    }

    private static void listContacts(ContactRepository contactRepository, User user) {
        List<Contact> contacts = contactRepository.getContactsByUserEmail(user.getEmail());
        if (contacts.isEmpty()) {
            System.out.println("Нет контактов.");
            return;
        }

        System.out.println("Список контактов:");
        for (Contact contact : contacts) {
            System.out.println("ID: " + contact.getId() + ", Фамилия: " + contact.getLastName() + ", Имя: " + contact.getFirstName());
        }
    }
}
