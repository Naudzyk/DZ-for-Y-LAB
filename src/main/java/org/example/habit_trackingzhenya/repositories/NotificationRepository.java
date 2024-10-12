package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Notification;
import org.example.habit_trackingzhenya.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationRepository {
    private List<Notification> notifications = new ArrayList<Notification>();

    public void add(Notification notification) {
        notifications.add(notification);
    }

    public List<Notification> getNotifications(User user) {
        return notifications.stream()
                .filter(notification -> notification.getUser().equals(user))
                .collect(Collectors.toList());
    }
}
