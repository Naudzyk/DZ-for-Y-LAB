package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

public interface NotificationService {
    void sendNotification(User user, Habit habit, String message);
}
