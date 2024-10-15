package org.example.habit_trackingzhenya.services.Impl;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.AdminServices;
import org.example.habit_trackingzhenya.services.HabitService;
import org.example.habit_trackingzhenya.services.UserService;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminServices {

    private UserService userService;
    private HabitService habitService;

    public AdminServiceImpl(HabitService habitService, UserService userService) {
        this.habitService = habitService;
        this.userService = userService;
    }

    public boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    @Override
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @Override
    public List<Habit> getAllHabits() {
        return habitService.getAllHabits();
    }

    @Override
    public boolean blockUser(String email) {
        return userService.blockUser(email);
    }

    @Override
    public boolean unblockUser(String email) {
        return userService.unblockUser(email);
    }

    @Override
    public boolean deleteUser(String email) {
        return userService.deleteUserForAdmin(email);
    }
}
