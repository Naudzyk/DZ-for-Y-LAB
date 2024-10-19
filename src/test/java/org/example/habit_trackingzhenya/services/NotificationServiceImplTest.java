package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.exception.InputInvalidException;
import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;
import org.example.habit_trackingzhenya.services.Impl.NotificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import static java.time.LocalDate.now;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class NotificationServiceImplTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendNotification() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        String message = "Test Message";

        when(notificationRepository.add(any(Notification.class))).thenReturn(true);

        // Act
        boolean result = notificationService.sendNotification(user, habit, message);

        // Assert
        assertTrue(result);
        verify(notificationRepository, times(1)).add(any(Notification.class));
    }

    @Test
    public void testSendNotificationWithNullUser() {
        // Arrange
        Habit habit = Habit.builder()
                .id(1L)
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(null)
                .creationDate(LocalDate.now())
                .build();

        String message = "Test Message";

        // Act & Assert
        assertThrows(InputInvalidException.class, () -> {
            notificationService.sendNotification(null, habit, message);
        });
    }

    @Test
    public void testSendNotificationWithNullHabit() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        String message = "Test Message";

        // Act & Assert
        assertThrows(InputInvalidException.class, () -> {
            notificationService.sendNotification(user, null, message);
        });
    }

    @Test
    public void testSendNotificationWithNullMessage() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        // Act & Assert
        assertThrows(InputInvalidException.class, () -> {
            notificationService.sendNotification(user, habit, null);
        });
    }

    @Test
    public void testSendNotificationWithSQLException() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        String message = "Test Message";

        when(notificationRepository.add(any(Notification.class))).thenThrow(new SQLException("Database error"));

        // Act
        boolean result = notificationService.sendNotification(user, habit, message);

        // Assert
        assertFalse(result);
        verify(notificationRepository, times(1)).add(any(Notification.class));
    }
}
