package org.example.habit_trackingzhenya.controller;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.exception.DeleteException;
import org.example.habit_trackingzhenya.exception.EntityNotFoundException;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.AdminServices;
import org.example.habit_trackingzhenya.services.Impl.AdminServiceImpl;

import org.example.habit_trackingzhenya.utils.ConsoleInputReader;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class AdminController {
    private AdminServices adminServices;
    private ConsoleInputReader consoleInputReader;
     private final Logger logger = Logger.getLogger(this.getClass().getName());

    public AdminController(AdminServiceImpl adminServices, ConsoleInputReader consoleInputReader) {
        this.adminServices = adminServices;
        this.consoleInputReader = consoleInputReader;
    }

     public void viewAllUsers() {
        try {
            List<User> users = adminServices.getAllUsers();
            if (users.isEmpty()) {
                System.out.println("Нет пользователей.");
                return;
            }

            System.out.println("Список пользователей:");
            for (User user : users) {
                System.out.println(user.getEmail() + " - " + user.getName() + (user.isBlocked() ? " (заблокирован)" : ""));
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка при просмотре пользователей: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка при просмотре пользователей");
        }
    }

    public void viewAllHabits() {
        try {
            List<Habit> habits = adminServices.getAllHabits();
            if (habits.isEmpty()) {
                System.out.println("Нет привычек.");
                return;
            }

            System.out.println("Список привычек:");
            for (Habit habit : habits) {
                System.out.println(habit.getName() + " - " + habit.getDescription() + " (" + habit.getFrequency() + ")");
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка при просмотре привычек: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка при просмотре привычек");
        }
    }

    public void blockUser() {
        try {
            String email = consoleInputReader.read("Введите email пользователя для блокировки: ");
            if (adminServices.blockUser(email)) {
                System.out.println("Пользователь заблокирован.");
            } else {
                throw new EntityNotFoundException("Пользователь не найден.");
            }
        } catch (EntityNotFoundException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка при блокировке пользователя: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка при блокировке пользователя");
        }
    }

    public void unblockUser() {
        try {
            String email = consoleInputReader.read("Введите email пользователя для разблокировки: ");
            if (adminServices.unblockUser(email)) {
                System.out.println("Пользователь разблокирован.");
            } else {
                throw new EntityNotFoundException("Пользователь не найден.");
            }
        } catch (EntityNotFoundException e) {
            logger.log(Level.WARNING, "Ошибка разблокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка при разблокировке пользователя: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка при разблокировке пользователя");
        }
    }

    public void deleteUser() {
        try {
            String email = consoleInputReader.read("Введите email пользователя для удаления: ");
            if (adminServices.deleteUser(email)) {
                System.out.println("Пользователь удален.");
            } else {
                throw new DeleteException("Пользователь не найден.");
            }
        } catch (DeleteException e) {
            logger.log(Level.WARNING, "Ошибка удаления пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка при удалении пользователя: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка удаления пользователя");
        }
    }
}
