package org.example.habit_trackingzhenya.repository;

import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.repositories.Impl.HabitCompletionRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.Impl.HabitRepositoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

@Testcontainers
public class HabitRepositoryImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    private static Connection connection;
    private static HabitRepository habitRepository;

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
            stmt.execute("CREATE TABLE habit_schema.users (" +
                    "id BIGSERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL," +
                    "blocked BOOLEAN NOT NULL," +
                    "role VARCHAR(50) NOT NULL" +
                    ")");
            stmt.execute("CREATE TABLE habit_schema.habits (" +
                    "id BIGSERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "description TEXT NOT NULL," +
                    "frequency VARCHAR(50) NOT NULL," +
                    "user_id BIGINT NOT NULL," +
                    "creation_date DATE NOT NULL" +
                    ")");
        }

        habitRepository = new HabitRepositoryImpl(connection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Очистка таблиц после каждого теста
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM habit_schema.habits");
            stmt.execute("DELETE FROM habit_schema.users");
        }
    }

    @Test
    public void testAddHabit() throws SQLException {
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
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        // Act
        boolean added = habitRepository.addHabit(habit);
        List<Habit> habits = habitRepository.getUserHabits(user);

        // Assert
        assertTrue(added);
        assertFalse(habits.isEmpty());
        assertEquals(1, habits.size());
        assertEquals("Test Habit", habits.get(0).getName());
    }

    @Test
    public void testUpdateHabit() throws SQLException {
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
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        habitRepository.addHabit(habit);

        // Act
        boolean updated = habitRepository.updateHabit(habit, "Updated Habit", "Updated Description", Frequency.WEEKLY);
        List<Habit> habits = habitRepository.getUserHabits(user);

        // Assert
        assertTrue(updated);
        assertFalse(habits.isEmpty());
        assertEquals(1, habits.size());
        assertEquals("Updated Habit", habits.get(0).getName());
        assertEquals("Updated Description", habits.get(0).getDescription());
        assertEquals(Frequency.WEEKLY, habits.get(0).getFrequency());
    }

    @Test
    public void testDeleteHabit() throws SQLException {
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
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        habitRepository.addHabit(habit);

        // Act
        boolean deleted = habitRepository.deleteHabit(habit);
        List<Habit> habits = habitRepository.getUserHabits(user);

        // Assert
        assertTrue(deleted);
        assertTrue(habits.isEmpty());
    }

    @Test
    public void testGetUserHabits() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit1 = Habit.builder()
                .name("Test Habit 1")
                .description("Test Description 1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        Habit habit2 = Habit.builder()
                .name("Test Habit 2")
                .description("Test Description 2")
                .frequency(Frequency.WEEKLY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        habitRepository.addHabit(habit1);
        habitRepository.addHabit(habit2);

        // Act
        List<Habit> habits = habitRepository.getUserHabits(user);

        // Assert
        assertFalse(habits.isEmpty());
        assertEquals(2, habits.size());
        assertEquals("Test Habit 1", habits.get(0).getName());
        assertEquals("Test Habit 2", habits.get(1).getName());
    }

    @Test
    public void testGetAllHabits() throws SQLException {
        // Arrange
        User user1 = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("User2")
                .email("user2@example.com")
                .password("password2")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit1 = Habit.builder()
                .name("Test Habit 1")
                .description("Test Description 1")
                .frequency(Frequency.DAILY)
                .user(user1)
                .creationDate(LocalDate.now())
                .build();

        Habit habit2 = Habit.builder()
                .name("Test Habit 2")
                .description("Test Description 2")
                .frequency(Frequency.WEEKLY)
                .user(user2)
                .creationDate(LocalDate.now())
                .build();

        habitRepository.addHabit(habit1);
        habitRepository.addHabit(habit2);

        // Act
        List<Habit> habits = habitRepository.getAllHabits();

        // Assert
        assertFalse(habits.isEmpty());
        assertEquals(2, habits.size());
        assertEquals("Test Habit 1", habits.get(0).getName());
        assertEquals("Test Habit 2", habits.get(1).getName());
    }
}