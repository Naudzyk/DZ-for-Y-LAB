package org.example.habit_trackingzhenya.controller;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.Impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDate.now;
import static org.mockito.Mockito.*;

public class AdminControllerTest {

    @Mock
    private AdminServiceImpl adminServices;

    @Mock
    private ConsoleInputReader consoleInputReader;

    @InjectMocks
    private AdminController adminController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testViewAllUsers() throws SQLException {
        // Arrange
        User user1 = User.builder()
                .email("user1@example.com")
                .name("User1")
                .blocked(false)
                .build();

        User user2 = User.builder()
                .email("user2@example.com")
                .name("User2")
                .blocked(true)
                .build();

        List<User> users = Arrays.asList(user1, user2);
        when(adminServices.getAllUsers()).thenReturn(Optional.of(users));

        // Act
        adminController.viewAllUsers();

        // Assert
        verify(adminServices, times(1)).getAllUsers();
    }

    @Test
    public void testViewAllHabits() throws SQLException {
        // Arrange
        Habit habit1 = Habit.builder()
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .build();

        Habit habit2 = Habit.builder()
                .name("Habit2")
                .description("Description2")
                .frequency(Frequency.WEEKLY)
                .build();

        List<Habit> habits = Arrays.asList(habit1, habit2);
        when(adminServices.getAllHabits()).thenReturn(habits);

        // Act
        adminController.viewAllHabits();

        // Assert
        verify(adminServices, times(1)).getAllHabits();
    }

    @Test
    public void testBlockUser() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите email пользователя для блокировки: ")).thenReturn("user1@example.com");
        when(adminServices.blockUser("user1@example.com")).thenReturn(true);

        // Act
        adminController.blockUser();

        // Assert
        verify(consoleInputReader, times(1)).read("Введите email пользователя для блокировки: ");
        verify(adminServices, times(1)).blockUser("user1@example.com");
    }

    @Test
    public void testBlockUserNotFound() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите email пользователя для блокировки: ")).thenReturn("user1@example.com");
        when(adminServices.blockUser("user1@example.com")).thenReturn(false);

        // Act
        adminController.blockUser();

        // Assert
        verify(consoleInputReader, times(1)).read("Введите email пользователя для блокировки: ");
        verify(adminServices, times(1)).blockUser("user1@example.com");
    }

    @Test
    public void testUnblockUser() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите email пользователя для разблокировки: ")).thenReturn("user1@example.com");
        when(adminServices.unblockUser("user1@example.com")).thenReturn(true);

        // Act
        adminController.unblockUser();

        // Assert
        verify(consoleInputReader, times(1)).read("Введите email пользователя для разблокировки: ");
        verify(adminServices, times(1)).unblockUser("user1@example.com");
    }

    @Test
    public void testUnblockUserNotFound() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите email пользователя для разблокировки: ")).thenReturn("user1@example.com");
        when(adminServices.unblockUser("user1@example.com")).thenReturn(false);

        // Act
        adminController.unblockUser();

        // Assert
        verify(consoleInputReader, times(1)).read("Введите email пользователя для разблокировки: ");
        verify(adminServices, times(1)).unblockUser("user1@example.com");
    }

    @Test
    public void testDeleteUser() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите email пользователя для удаления: ")).thenReturn("user1@example.com");
        when(adminServices.deleteUser("user1@example.com")).thenReturn(true);

        // Act
        adminController.deleteUser();

        // Assert
        verify(consoleInputReader, times(1)).read("Введите email пользователя для удаления: ");
        verify(adminServices, times(1)).deleteUser("user1@example.com");
    }

    @Test
    public void testDeleteUserNotFound() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите email пользователя для удаления: ")).thenReturn("user1@example.com");
        when(adminServices.deleteUser("user1@example.com")).thenReturn(false);

        // Act
        adminController.deleteUser();

        // Assert
        verify(consoleInputReader, times(1)).read("Введите email пользователя для удаления: ");
        verify(adminServices, times(1)).deleteUser("user1@example.com");
    }
}