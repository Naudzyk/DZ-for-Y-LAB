package org.example.habit_trackingzhenya.repositories.Impl;

import org.example.habit_trackingzhenya.models.Notification;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class NotificationRepositoryImpl implements NotificationRepository {
    private List<Notification> notifications = new ArrayList<Notification>();
    @Override
    public void add(Notification notification) {
        notifications.add(notification);
    }
    @Override
    public List<Notification> getNotifications(User user) {
        return notifications.stream()
                .filter(notification -> notification.getUser().equals(user))
                .collect(Collectors.toList());
    }
}
