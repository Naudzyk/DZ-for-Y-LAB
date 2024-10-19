package org.example.habit_trackingzhenya.repository;

import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.Impl.NotificationRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@Testcontainers
public class NotificationRepositoryImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    private static Connection connection;
    private static NotificationRepository notificationRepository;

    @BeforeAll
    public static void setUp() throws SQLException {
        postgresContainer.start();
        String jdbcUrl = postgresContainer.getJdbcUrl();
        String username = postgresContainer.getUsername();
        String password = postgresContainer.getPassword();
        connection = DriverManager.getConnection(jdbcUrl, username, password);

        // Создание схемы и таблиц
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("CREATE SCHEMA habit_schema");
            stmt.execute("CREATE TABLE habit_schema.habits (" +
                    "id BIGSERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "description TEXT NOT NULL," +
                    "frequency VARCHAR(50) NOT NULL," +
                    "user_id BIGINT NOT NULL," +
                    "creation_date DATE NOT NULL" +
                    ")");
            stmt.execute("CREATE TABLE habit_schema.notifications (" +
                    "id BIGSERIAL PRIMARY KEY," +
                    "user_id BIGINT NOT NULL," +
                    "habit_id BIGINT NOT NULL," +
                    "message TEXT NOT NULL," +
                    "send_time TIMESTAMP NOT NULL" +
                    ")");
        }

        notificationRepository = new NotificationRepositoryImpl(connection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Очистка таблиц после каждого теста
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM habit_schema.notifications");
            stmt.execute("DELETE FROM habit_schema.habits");
        }
    }

    @Test
    public void testAddNotification() throws SQLException {
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
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        Notification notification = Notification.builder()
                .user(user)
                .habit(habit)
                .message("Test Notification")
                .sendTime(LocalDateTime.now())
                .build();

        // Act
        boolean added = notificationRepository.add(notification);
        List<Notification> notifications = notificationRepository.getNotifications(user);

        // Assert
        assertTrue(added);
        assertFalse(notifications.isEmpty());
        assertEquals(1, notifications.size());
        assertEquals("Test Notification", notifications.get(0).getMessage());
    }

    @Test
    public void testGetNotifications() throws SQLException {
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
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        Notification notification1 = Notification.builder()
                .user(user)
                .habit(habit)
                .message("Test Notification 1")
                .sendTime(LocalDateTime.now())
                .build();

        Notification notification2 = Notification.builder()
                .user(user)
                .habit(habit)
                .message("Test Notification 2")
                .sendTime(LocalDateTime.now())
                .build();

        notificationRepository.add(notification1);
        notificationRepository.add(notification2);

        // Act
        List<Notification> notifications = notificationRepository.getNotifications(user);

        // Assert
        assertFalse(notifications.isEmpty());
        assertEquals(2, notifications.size());
        assertEquals("Test Notification 1", notifications.get(0).getMessage());
        assertEquals("Test Notification 2", notifications.get(1).getMessage());
    }
}

