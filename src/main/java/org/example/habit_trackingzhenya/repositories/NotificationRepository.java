package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Notification;
import org.example.habit_trackingzhenya.models.User;

import java.util.List;

public interface NotificationRepository {
    void add(Notification notification);

    List<Notification> getNotifications(User user);
}
