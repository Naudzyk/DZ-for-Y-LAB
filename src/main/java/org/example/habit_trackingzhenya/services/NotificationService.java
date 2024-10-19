package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

import java.sql.SQLException;

public interface NotificationService {
    boolean sendNotification(User user, Habit habit, String message) throws SQLException;
}
