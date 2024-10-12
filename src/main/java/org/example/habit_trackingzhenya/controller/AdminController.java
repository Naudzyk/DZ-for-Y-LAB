package org.example.habit_trackingzhenya.controller;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.exception.AdminException;
import org.example.habit_trackingzhenya.exception.DeleteException;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.AdminServices;
import org.example.habit_trackingzhenya.services.Impl.AdminServiceImpl;
import org.example.habit_trackingzhenya.utils.InputReader;
import org.example.habit_trackingzhenya.utils.Utils;

import java.util.List;

@RequiredArgsConstructor
public class AdminController {
    private AdminServices adminServices;
    private InputReader inputReader;

    public AdminController(AdminServiceImpl adminServices, InputReader inputReader) {
        this.adminServices = adminServices;
        this.inputReader = inputReader;
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
        }catch (Exception e) {
            System.out.println("Ошибка");
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
        }catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    public void blockUser() {
        try {
            String email = inputReader.read("Введите email пользователя для блокировки: ");
            if (adminServices.blockUser(email)) {
                System.out.println("Пользователь заблокирован.");
            } else {
                throw new AdminException("Пользователь не найден.");
            }
        }catch (AdminException e){
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    public void unblockUser() {
        try {
            String email = inputReader.read("Введите email пользователя для разблокировки: ");
            if (adminServices.unblockUser(email)) {
                System.out.println("Пользователь разблокирован.");
            } else {
                throw new AdminException("Пользователь не найден.");
            }
        }catch (AdminException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
        }
    }

    public void deleteUser() {
        try {
            String email = inputReader.read("Введите email пользователя для удаления: ");
            if (adminServices.deleteUser(email)) {
                System.out.println("Пользователь удален.");
            } else {
                throw new DeleteException("Пользователь не найден.");
            }
        }catch (DeleteException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
        }
    }
}
