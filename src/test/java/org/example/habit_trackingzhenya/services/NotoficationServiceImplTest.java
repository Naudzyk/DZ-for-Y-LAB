package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;
import org.example.habit_trackingzhenya.services.Impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NotoficationServiceImplTest {
    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    private User user;
    private Habit habit;
    private Notification notification;

    @BeforeEach
    public void setUp() {
        user = new User("John Doe", "john@example.com", "password", Role.USER);
        habit = new Habit("Exercise", "Daily exercise", Frequency.DAILY, user);
        notification = new Notification(user, habit, "Test notification", LocalDateTime.now());
    }

    @Test
    public void testSendNotification() {
        notificationService.sendNotification(user, habit, "Test notification");
        verify(notificationRepository, times(1)).add(any(Notification.class));
    }


}
