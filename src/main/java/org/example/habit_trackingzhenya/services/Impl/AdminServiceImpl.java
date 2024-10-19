package org.example.habit_trackingzhenya.services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.AdminServices;
import org.example.habit_trackingzhenya.services.HabitService;
import org.example.habit_trackingzhenya.services.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

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
    public Optional<List<User>> getAllUsers() throws SQLException {
        return userService.getAllUsers();
    }

    @Override
    public List<Habit> getAllHabits() throws SQLException {
        return habitService.getAllHabits();
    }

    @Override
    public boolean blockUser(String email) throws SQLException {
        return userService.blockUser(email);
    }

    @Override
    public boolean unblockUser(String email) throws SQLException {
        return userService.unblockUser(email);
    }

    @Override
    public boolean deleteUser(String email) throws SQLException {
        return userService.deleteUserForAdmin(email);
    }
}
