package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Notification;
import org.example.habit_trackingzhenya.models.User;

import java.sql.SQLException;
import java.util.List;

public interface NotificationRepository {
    boolean add(Notification notification) throws SQLException;;

    List<Notification> getNotifications(User user) throws SQLException;;
}
