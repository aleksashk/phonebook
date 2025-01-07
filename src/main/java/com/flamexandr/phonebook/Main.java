package com.flamexandr.phonebook;

import com.flamexandr.phonebook.model.User;
import com.flamexandr.phonebook.repository.UserRepository;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        UserRepository userRepository = new UserRepository();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("1. Регистрация");
            System.out.println("2. Вход");
            int choice = Integer.parseInt(scanner.nextLine());

            if (choice == 1) {
                System.out.println("Введите email:");
                String email = scanner.nextLine();
                System.out.println("Введите пароль:");
                String password = scanner.nextLine();

                if (userRepository.existsByEmail(email)) {
                    System.out.println("Пользователь с таким email уже существует.");
                } else {
                    userRepository.save(new User(email, password));
                    System.out.println("Регистрация успешна!");
                }
            } else if (choice == 2) {
                System.out.println("Введите email:");
                String email = scanner.nextLine();
                System.out.println("Введите пароль:");
                String password = scanner.nextLine();

                User user = userRepository.findByEmail(email);
                if (user != null && user.checkPassword(password)) {
                    System.out.println("Добро пожаловать, " + user.getEmail() + "!");
                } else {
                    System.out.println("Неверные email или пароль.");
                }
            } else {
                System.out.println("Неверный выбор.");
            }
        }
    }
}
