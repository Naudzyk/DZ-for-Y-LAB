package org.example.habit_trackingzhenya.services.Impl;

import lombok.RequiredArgsConstructor;

import org.example.habit_trackingzhenya.exception.InputInvalidException;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Notification;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;
import org.example.habit_trackingzhenya.services.NotificationService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public boolean sendNotification(User user, Habit habit, String message) {
        if (user == null || habit == null || message == null) {
            throw new InputInvalidException("User, habit, or message cannot be null");
        }
        LocalDateTime sendTime = LocalDateTime.now();
        Notification notification = Notification.builder()
                .user(user)
                .habit(habit)
                .message(message)
                .sendTime(sendTime)
                .build();
        try {
            return notificationRepository.add(notification);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при отправке уведомления: " + e.getMessage(), e);
            return false;
        }
    }
}
