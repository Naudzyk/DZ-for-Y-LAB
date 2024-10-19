package org.example.habit_trackingzhenya.services;

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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AdminServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private HabitService habitService;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testIsAdmin() {
        // Arrange
        User adminUser = User.builder()
                .id(1L)
                .name("Admin User")
                .email("admin@example.com")
                .password("password")
                .role(Role.ADMIN)
                .build();

        User regularUser = User.builder()
                .id(2L)
                .name("Regular User")
                .email("user@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        // Act & Assert
        assertTrue(adminService.isAdmin(adminUser));
        assertFalse(adminService.isAdmin(regularUser));
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        // Arrange
        User user1 = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("User2")
                .email("user2@example.com")
                .password("password2")
                .role(Role.USER)
                .build();

        List<User> users = Arrays.asList(user1, user2);
        when(userService.getAllUsers()).thenReturn(Optional.of(users));

        // Act
        Optional<List<User>> result = adminService.getAllUsers();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    public void testGetAllHabits() throws SQLException {
        // Arrange
        Habit habit1 = Habit.builder()
                .id(1L)
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .build();

        Habit habit2 = Habit.builder()
                .id(2L)
                .name("Habit2")
                .description("Description2")
                .frequency(Frequency.WEEKLY)
                .build();

        List<Habit> habits = Arrays.asList(habit1, habit2);
        when(habitService.getAllHabits()).thenReturn(habits);

        // Act
        List<Habit> result = adminService.getAllHabits();

        // Assert
        assertEquals(2, result.size());
        verify(habitService, times(1)).getAllHabits();
    }

    @Test
    public void testBlockUser() throws SQLException {
        // Arrange
        when(userService.blockUser("user1@example.com")).thenReturn(true);

        // Act
        boolean result = adminService.blockUser("user1@example.com");

        // Assert
        assertTrue(result);
        verify(userService, times(1)).blockUser("user1@example.com");
    }

    @Test
    public void testUnblockUser() throws SQLException {
        // Arrange
        when(userService.unblockUser("user1@example.com")).thenReturn(true);

        // Act
        boolean result = adminService.unblockUser("user1@example.com");

        // Assert
        assertTrue(result);
        verify(userService, times(1)).unblockUser("user1@example.com");
    }

    @Test
    public void testDeleteUser() throws SQLException {
        // Arrange
        when(userService.deleteUserForAdmin("user1@example.com")).thenReturn(true);

        // Act
        boolean result = adminService.deleteUser("user1@example.com");

        // Assert
        assertTrue(result);
        verify(userService, times(1)).deleteUserForAdmin("user1@example.com");
    }
}