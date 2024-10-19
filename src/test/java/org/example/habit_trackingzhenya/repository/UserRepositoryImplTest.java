package org.example.habit_trackingzhenya.repository;

import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.Impl.UserRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.UserRepository;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;


@Testcontainers
public class UserRepositoryImplTest {

    @Container
    private static final PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("y_lab_db")
            .withUsername("zhenya")
            .withPassword("qwerty123");

    private static Connection connection;
    private static UserRepository userRepository;

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

            stmt.execute("CREATE SCHEMA utility_schema");
            stmt.execute("CREATE TABLE utility_schema.utility (" +
                    "id BIGSERIAL PRIMARY KEY," +
                    "name VARCHAR(255) NOT NULL," +
                    "email VARCHAR(255) NOT NULL UNIQUE," +
                    "password VARCHAR(255) NOT NULL," +
                    "blocked BOOLEAN NOT NULL," +
                    "role VARCHAR(50) NOT NULL" +
                    ")");

        }



        userRepository = new UserRepositoryImpl(connection);
    }
    @AfterEach
    public void tearDown() throws SQLException {
        // Очистка таблицы после каждого теста
        try (Statement stmt = connection.createStatement()) {
            stmt.execute("DELETE FROM habit_schema.users");
        }
    }

    @Test
    public void testAddUser() throws SQLException {
        // Arrange
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .blocked(false)
                .role(Role.USER)
                .build();

        // Act
        userRepository.addUser(user);
        Optional<User> foundUser = userRepository.getUserByEmail("test@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    public void testGetUserByEmail() throws SQLException {
        // Arrange
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .blocked(false)
                .role(Role.USER)
                .build();

        userRepository.addUser(user);

        // Act
        Optional<User> foundUser = userRepository.getUserByEmail("test@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
    }

    @Test
    public void testExistsByEmail() throws SQLException {
        // Arrange
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .blocked(false)
                .role(Role.USER)
                .build();

        userRepository.addUser(user);

        // Act
        boolean exists = userRepository.existsByEmail("test@example.com");

        // Assert
        assertTrue(exists);
    }

    @Test
    public void testDeleteUser() throws SQLException {
        // Arrange
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .blocked(false)
                .role(Role.USER)
                .build();

        userRepository.addUser(user);

        // Act
        boolean deleted = userRepository.deleteUser("test@example.com");
        Optional<User> foundUser = userRepository.getUserByEmail("test@example.com");

        // Assert
        assertTrue(deleted);
        assertFalse(foundUser.isPresent());
    }

    @Test
    public void testUpdateEmail() throws SQLException {
        // Arrange
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .blocked(false)
                .role(Role.USER)
                .build();

        userRepository.addUser(user);

        // Act
        userRepository.updateEmail("test@example.com", "newemail@example.com");
        Optional<User> foundUser = userRepository.getUserByEmail("newemail@example.com");

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals("newemail@example.com", foundUser.get().getEmail());
    }

    @Test
    public void testUpdateName() throws SQLException {
        // Arrange
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .blocked(false)
                .role(Role.USER)
                .build();

        userRepository.addUser(user);

        // Act
        boolean updated = userRepository.updateName("test@example.com", "New Name");
        Optional<User> foundUser = userRepository.getUserByEmail("test@example.com");

        // Assert
        assertTrue(updated);
        assertTrue(foundUser.isPresent());
        assertEquals("New Name", foundUser.get().getName());
    }

    @Test
    public void testUpdatePassword() throws SQLException {
        // Arrange
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .blocked(false)
                .role(Role.USER)
                .build();

        userRepository.addUser(user);

        // Act
        boolean updated = userRepository.updatePassword("test@example.com", "newpassword");
        Optional<User> foundUser = userRepository.getUserByEmail("test@example.com");

        // Assert
        assertTrue(updated);
        assertTrue(foundUser.isPresent());
        assertEquals("newpassword", foundUser.get().getPassword());
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        // Arrange
        User user1 = User.builder()
                .name("Test User 1")
                .email("test1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .name("Test User 2")
                .email("test2@example.com")
                .password("password2")
                .blocked(false)
                .role(Role.USER)
                .build();

        userRepository.addUser(user1);
        userRepository.addUser(user2);

        // Act
        Optional<List<User>> users = userRepository.getAllUsers();

        // Assert
        assertTrue(users.isPresent());
        assertEquals(2, users.get().size());
        assertEquals("Test User 1", users.get().get(0).getName());
        assertEquals("Test User 2", users.get().get(1).getName());
    }
}

