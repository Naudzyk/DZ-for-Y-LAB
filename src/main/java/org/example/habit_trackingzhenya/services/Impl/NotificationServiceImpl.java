package org.example.habit_trackingzhenya.services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Notification;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;
import org.example.habit_trackingzhenya.services.NotificationService;

import java.time.LocalDateTime;
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public void sendNotification(User user, Habit habit, String message) {
        Notification notification = new Notification(user, habit, message, LocalDateTime.now());
        notificationRepository.add(notification);
        // TODO: Здесь можно добавить логику для отправки уведомления через внешний сервис
        System.out.println("Уведомление отправлено: " + message);
    }
}
