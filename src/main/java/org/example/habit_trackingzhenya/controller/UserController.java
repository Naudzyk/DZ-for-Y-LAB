package org.example.habit_trackingzhenya.controller;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.exception.*;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.Impl.UserServiceImpl;
import org.example.habit_trackingzhenya.services.UserService;
import org.example.habit_trackingzhenya.utils.InputReader;
import org.example.habit_trackingzhenya.utils.Utils;

@RequiredArgsConstructor
public class UserController {
    private UserService userService;
    private InputReader inputReader;
    private UserRepository userRepository;

    public UserController(UserServiceImpl userService,InputReader inputReader) {
        this.userService = userService;
        this.inputReader = inputReader;
    }



    public void registerUser() {
        try {
            String name = inputReader.read("Введите ваше имя: ");
            String email = inputReader.read("Введите ваш email: ");
            String password = inputReader.read("Введите ваш пароль: ");
            String roleStr = inputReader.read("Введите роль (USER/ADMIN): ");
            Role role = Role.valueOf(roleStr.toUpperCase());

            User user = new User(name, email, password, role);
            boolean success = userService.addUser(user);
            if (success) {
                System.out.println("Регистрация прошла успешно.");
            } else {
                throw new RegistrationException("Пользователь с таким email уже существует.");
            }
        } catch (RegistrationException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    public User loginUser() {
        try {
            String email = inputReader.read("Введите ваш Email: ");
            String password = inputReader.read("Введите ваш Password: ");

            User user = userService.login(email, password);

            if (user != null) {
                System.out.println("Вы вошли! Привет " + user.getName() + ".");
                return user;
            } else {
                throw new InputInvalidException("Ошибка входа! Возможно, неверный email или пароль.");
            }

        } catch (InputInvalidException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
        return null;
    }

    public void editProfile(String email) {
        System.out.println("Что вы хотите редактировать?");
        System.out.println("1. Редактировать Email");
        System.out.println("2. Редактировать Имя");
        System.out.println("3. Редактировать Пароль");
        int choice = Integer.parseInt(inputReader.read("Введите ваш выбор: "));
        try {
            switch (choice) {
                case 1:
                    String newEmail = inputReader.read("Введите новый Email: ");
                    if (userService.updateEmail(email, newEmail)) {
                        System.out.println("Ваш новый Email: " + newEmail);
                    } else {
                        throw new EditException("Email не обновлен. Возможно, такой Email уже зарегистрирован.");
                    }
                    break;
                case 2:
                    String newName = inputReader.read("Введите новое Имя: ");
                    if (userService.updateName(email, newName)) {
                        System.out.println("Ваше новое Имя: " + newName);
                    } else {
                        throw new EditException("Имя не обновлено. Ошибка!");
                    }
                    break;
                case 3:
                    String oldPassword = inputReader.read("Введите старый Пароль: ");
                    String newPassword = inputReader.read("Введите новый Пароль: ");

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
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка!");
        }
    }

    public void resetPassword(String email) {
        try {
            String newPassword = inputReader.read("Введите новый пароль: ");

            if (userService.resetPassword(email, newPassword)) {
                System.out.println("Пароль успешно сброшен!");
            } else {
                throw new UpdateException("Не удалось сбросить пароль");
            }

        } catch (UpdateException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    public void deleteUser(String email) {
        try {
            String password = inputReader.read("Введите пароль: ");

            if (userService.deleteUser(email, password)) {
                System.out.println("Аккаунт удалён");
            } else {
                throw new DeleteException("Ошибка! Возможно, неверный пароль.");
            }

        } catch (DeleteException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }
}
