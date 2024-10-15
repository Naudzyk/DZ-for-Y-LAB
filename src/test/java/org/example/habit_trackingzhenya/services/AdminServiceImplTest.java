package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.Impl.AdminServiceImpl;
import org.example.habit_trackingzhenya.services.Impl.HabitServiceImpl;
import org.example.habit_trackingzhenya.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private UserService userService;

    @Mock
    private HabitService habitService;

    @InjectMocks
    private AdminServiceImpl adminService;

    private User regularUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        regularUser = new User("User", "user@example.com", "password", Role.USER);
    }

    @Test
    void testIsAdmin() {
        User adminUser = new User("Admin", "admin@example.com", "password", Role.ADMIN);


        assertTrue(adminService.isAdmin(adminUser));
        assertFalse(adminService.isAdmin(regularUser));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User("User1", "user1@example.com", "password", Role.USER),
                new User("User2", "user2@example.com", "password", Role.USER)
        );

        when(userService.getAllUsers()).thenReturn(users);

        List<User> result = adminService.getAllUsers();

        assertEquals(users, result);
        verify(userService, times(1)).getAllUsers();
    }

    @Test
    void testGetAllHabits() {
        List<Habit> habits = Arrays.asList(
                new Habit("Habit1", "Description1", Frequency.DAILY,regularUser),
                new Habit("Habit2", "Description2", Frequency.WEEKLY,regularUser)
        );

        when(habitService.getAllHabits()).thenReturn(habits);

        List<Habit> result = adminService.getAllHabits();

        assertEquals(habits, result);
        verify(habitService, times(1)).getAllHabits();
    }

    @Test
    void testBlockUser() {
        String email = "user@example.com";

        when(userService.blockUser(email)).thenReturn(true);

        boolean result = adminService.blockUser(email);

        assertTrue(result);
        verify(userService, times(1)).blockUser(email);
    }

    @Test
    void testUnblockUser() {
        String email = "user@example.com";

        when(userService.unblockUser(email)).thenReturn(true);

        boolean result = adminService.unblockUser(email);

        assertTrue(result);
        verify(userService, times(1)).unblockUser(email);
    }

    @Test
    void testDeleteUser() {
        String email = "user@example.com";

        when(userService.deleteUserForAdmin(email)).thenReturn(true);

        boolean result = adminService.deleteUser(email);

        assertTrue(result);
        verify(userService, times(1)).deleteUserForAdmin(email);
    }
}
