package org.example.habit_trackingzhenya.repository;

import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.Impl.NotificationRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class NotificationRepositoryTest {

    private NotificationRepository notificationRepository;
    private User user1;
    private User user2;
    private Habit habit1;
    private Habit habit2;

    @BeforeEach
    public void setUp() {
        notificationRepository = new NotificationRepositoryImpl();
        user1 = new User("User1", "user1@example.com", "password", Role.USER);
        user2 = new User("User2", "user2@example.com", "password", Role.USER);
        habit1 = new Habit("Habit1", "Description1", Frequency.DAILY,user1);
        habit2 = new Habit("Habit2", "Description2", Frequency.WEEKLY,user2);
    }

    @Test
    public void testAddNotification() {
        Notification notification = new Notification(user1, habit1, "Test notification");
        notificationRepository.add(notification);

        List<Notification> notifications = notificationRepository.getNotifications(user1);
        assertThat(notifications).hasSize(1);
        assertThat(notifications.get(0).getMessage()).isEqualTo("Test notification");
        assertThat(notifications.get(0).getHabit()).isEqualTo(habit1);
    }

    @Test
    public void testGetNotificationsForUser() {
        Notification notification1 = new Notification(user1, habit1, "Notification 1");
        Notification notification2 = new Notification(user1, habit2, "Notification 2");
        Notification notification3 = new Notification(user2, habit1, "Notification 3");

        notificationRepository.add(notification1);
        notificationRepository.add(notification2);
        notificationRepository.add(notification3);

        List<Notification> user1Notifications = notificationRepository.getNotifications(user1);
        List<Notification> user2Notifications = notificationRepository.getNotifications(user2);

        assertThat(user1Notifications).hasSize(2);
        assertThat(user1Notifications).containsExactlyInAnyOrder(notification1, notification2);

        assertThat(user2Notifications).hasSize(1);
        assertThat(user2Notifications).containsExactly(notification3);
    }

    @Test
    public void testGetNotificationsForNonExistentUser() {
        User nonExistentUser = new User("NonExistent", "nonexistent@example.com", "password", Role.USER);

        List<Notification> notifications = notificationRepository.getNotifications(nonExistentUser);
        assertThat(notifications).isEmpty();
    }

    @Test
    public void testNotificationConstructorWithoutHabit() {
        Notification notification = new Notification(user1, "Test notification");

        assertThat(notification.getUser()).isEqualTo(user1);
        assertThat(notification.getHabit()).isNull();
        assertThat(notification.getMessage()).isEqualTo("Test notification");
    }

    @Test
    public void testNotificationConstructorWithHabit() {
        Notification notification = new Notification(user1, habit1, "Test notification");

        assertThat(notification.getUser()).isEqualTo(user1);
        assertThat(notification.getHabit()).isEqualTo(habit1);
        assertThat(notification.getMessage()).isEqualTo("Test notification");
        assertThat(notification.getSendTime()).isNotNull();
    }

    @Test
    public void testNotificationEqualsAndHashCode() {
        Notification notification1 = new Notification(user1, habit1, "Test notification");
        Notification notification2 = new Notification(user1, habit1, "Test notification");
        Notification notification3 = new Notification(user2, habit2, "Different notification");

        assertThat(notification1).isEqualTo(notification2);
        assertThat(notification1).isNotEqualTo(notification3);

        assertThat(notification1.hashCode()).isEqualTo(notification2.hashCode());
        assertThat(notification1.hashCode()).isNotEqualTo(notification3.hashCode());
    }

}
