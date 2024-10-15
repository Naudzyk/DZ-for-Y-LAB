package org.example.habit_trackingzhenya.controller;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.exception.*;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.Impl.UserServiceImpl;
import org.example.habit_trackingzhenya.services.UserService;
import org.example.habit_trackingzhenya.utils.ConsoleInputReader;

import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class UserController {
    private UserService userService;
    private ConsoleInputReader consoleInputReader;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public UserController(UserServiceImpl userService,ConsoleInputReader consoleinputReader) {
        this.userService = userService;
        this.consoleInputReader = consoleinputReader;
    }


    public void registerUser() {
        try {
            String name = consoleInputReader.read("Введите ваше имя: ");
            String email = consoleInputReader.read("Введите ваш email: ");
            String password = consoleInputReader.read("Введите ваш пароль: ");
            String roleStr = consoleInputReader.read("Введите роль (USER/ADMIN): ");
            Role role = Role.valueOf(roleStr.toUpperCase());

            User user = new User(name, email, password, role);
            boolean success = userService.addUser(user);
            if (success) {
                System.out.println("Регистрация прошла успешно.");
            } else {
                throw new RegistrationException("Пользователь с таким email уже существует.");
            }
        } catch (RegistrationException e) {
            logger.log(Level.WARNING, "Ошибка регистрации: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.log(Level.WARNING, "Ошибка ввода роли: " + e.getMessage(), e);
            System.out.println("Неверная роль. Допустимые значения: USER, ADMIN.");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Ошибка");
        }
    }

    public User loginUser() {
        try {
            String email = consoleInputReader.read("Введите ваш Email: ");
            String password = consoleInputReader.read("Введите ваш Password: ");

            User user = userService.login(email, password);

            if (user != null) {
                System.out.println("Вы вошли! Привет " + user.getName() + ".");
                return user;
            } else {
                throw new InputInvalidException("Ошибка входа! Возможно, неверный email или пароль.");
            }

        } catch (InputInvalidException e) {
            logger.log(Level.WARNING, "Ошибка входа: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Ошибка");
        }
        return null;
    }

    public void editProfile(String email) {
        System.out.println("Что вы хотите редактировать?");
        System.out.println("1. Редактировать Email");
        System.out.println("2. Редактировать Имя");
        System.out.println("3. Редактировать Пароль");
        int choice = Integer.parseInt(consoleInputReader.read("Введите ваш выбор: "));
        try {
            switch (choice) {
                case 1:
                    String newEmail = consoleInputReader.read("Введите новый Email: ");
                    if (userService.updateEmail(email, newEmail)) {
                        System.out.println("Ваш новый Email: " + newEmail);
                    } else {
                        throw new EditException("Email не обновлен. Возможно, такой Email уже зарегистрирован.");
                    }
                    break;
                case 2:
                    String newName = consoleInputReader.read("Введите новое Имя: ");
                    if (userService.updateName(email, newName)) {
                        System.out.println("Ваше новое Имя: " + newName);
                    } else {
                        throw new EditException("Имя не обновлено. Ошибка!");
                    }
                    break;
                case 3:
                    String oldPassword = consoleInputReader.read("Введите старый Пароль: ");
                    String newPassword = consoleInputReader.read("Введите новый Пароль: ");

                    if (userService.updatePassword(email, newPassword, oldPassword)) {
                        System.out.println("Пароль обновлен");
                    } else {
                        throw new EditException("Пароль не обновлен. Ошибка! Возможно, неверный старый Пароль.");
                    }
                    break;
                default:
                    System.out.println("Неверный выбор.");
                    break;
            }
        } catch (EditException e) {
            logger.log(Level.WARNING, "Ошибка редактирования профиля: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Ошибка!");
        }
    }

    public void resetPassword(String email) {
        try {
            String newPassword = consoleInputReader.read("Введите новый пароль: ");

            if (userService.resetPassword(email, newPassword)) {
                System.out.println("Пароль успешно сброшен!");
            } else {
                throw new UpdateException("Не удалось сбросить пароль");
            }

        } catch (UpdateException e) {
            logger.log(Level.WARNING, "Ошибка сброса пароля: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Ошибка");
        }
    }

    public void deleteUser(String email) {
        try {
            String password = consoleInputReader.read("Введите пароль: ");

            if (userService.deleteUser(email, password)) {
                System.out.println("Аккаунт удалён");
            } else {
                throw new DeleteException("Ошибка! Возможно, неверный пароль.");
            }

        } catch (DeleteException e) {
            logger.log(Level.WARNING, "Ошибка удаления пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Ошибка");
        }
    }
}
