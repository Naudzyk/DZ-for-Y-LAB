package org.example.habit_trackingzhenya.services.Impl;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.AdminServices;

import java.util.ArrayList;
import java.util.List;
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminServices {

    private UserRepository userRepository;
    private HabitRepository habitRepository;

    public AdminServiceImpl(UserRepository userRepository, HabitRepository habitRepository) {
        this.userRepository = userRepository;
        this.habitRepository = habitRepository;
    }

    public boolean isAdmin(User user) {
        return user.getRole() == Role.ADMIN;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.getAllUsers();
    }

    @Override
    public List<Habit> getAllHabits() {
        return new ArrayList<>(habitRepository.getAllHabits());
    }

    @Override
    public boolean blockUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null && user.getRole() != Role.ADMIN) {
            user.setBlocked(true);
            return true;
        }
        return false;
    }

    @Override
    public boolean unblockUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setBlocked(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteUser(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            userRepository.deleteUser(email);
            return true;
        }
        return false;
    }
}
