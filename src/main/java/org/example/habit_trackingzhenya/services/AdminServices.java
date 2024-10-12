package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

import java.util.List;

public interface AdminServices {
    List<User> getAllUsers();

    List<Habit> getAllHabits();

    boolean blockUser(String email);

    boolean unblockUser(String email);

    boolean deleteUser(String email);
}
