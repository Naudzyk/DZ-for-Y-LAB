package org.example.habit_trackingzhenya.repository;

import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.repositories.Impl.HabitCompletionRepositoryImpl;
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


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@Testcontainers
public class HabitCompletionRepositoryImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpassword");

    private static Connection connection;
    private static HabitCompletionRepository habitCompletionRepository;

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
            stmt.execute("CREATE TABLE habit_schema.habit_completions (" +
                    "id BIGSERIAL PRIMARY KEY," +
                    "habit_id BIGINT NOT NULL," +
                    "completion_date DATE NOT NULL" +
                    ")");
        }

        habitCompletionRepository = new HabitCompletionRepositoryImpl(connection);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        // Очистка таблиц после каждого теста
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM habit_schema.habit_completions");
            stmt.execute("DELETE FROM habit_schema.habits");
            stmt.execute("DELETE FROM habit_schema.users");
        }
    }

    @Test
    public void testAddCompletion() throws SQLException {
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
        habitCompletionRepository.save(habit);
        habitCompletionRepository.addCompletion(habit, LocalDate.now());
        List<HabitCompletion> completions = habitCompletionRepository.getCompletionsForHabit(habit);

        // Assert
        assertFalse(completions.isEmpty());
        assertEquals(1, completions.size());
        assertEquals(LocalDate.now(), completions.get(0).getCompletionDate());
    }

    @Test
    public void testGetCompletionsForHabitInPeriod() throws SQLException {
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
        habitCompletionRepository.save(habit);
        habitCompletionRepository.addCompletion(habit, LocalDate.now());
        habitCompletionRepository.addCompletion(habit, LocalDate.now().plusDays(1));
        List<HabitCompletion> completions = habitCompletionRepository.getCompletionsForHabitInPeriod(habit, LocalDate.now(), LocalDate.now().plusDays(1));

        // Assert
        assertFalse(completions.isEmpty());
        assertEquals(2, completions.size());
    }
}