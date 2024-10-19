package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface AdminServices {
    Optional<List<User>> getAllUsers() throws SQLException;

    List<Habit> getAllHabits() throws SQLException;

    boolean blockUser(String email) throws SQLException;

    boolean unblockUser(String email) throws SQLException;

    boolean deleteUser(String email) throws SQLException;
}
