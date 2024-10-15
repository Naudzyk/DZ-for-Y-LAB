package org.example.habit_trackingzhenya.controller;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.AdminServices;
import org.example.habit_trackingzhenya.services.Impl.AdminServiceImpl;
import org.example.habit_trackingzhenya.utils.ConsoleInputReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

public class AdminControllerTest {
    private AdminController adminController;
    private AdminServiceImpl adminServices;
    private ConsoleInputReader consoleInputReader;
    private User user;

    @BeforeEach
    public void setUp() {
        adminServices = Mockito.mock(AdminServiceImpl.class);
        consoleInputReader = Mockito.mock(ConsoleInputReader.class);
        adminController = new AdminController(adminServices, consoleInputReader);
        user = new User("User", "user@example.com", "password", Role.USER);
    }

    @Test
    public void testViewAllUsers_Success() {
        List<User> users = Arrays.asList(
                new User("User1", "user1@example.com", "password", Role.USER),
                new User("User2", "user2@example.com", "password", Role.USER)
        );
        when(adminServices.getAllUsers()).thenReturn(users);

        adminController.viewAllUsers();

        verify(adminServices, times(1)).getAllUsers();
    }

    @Test
    public void testViewAllUsers_NoUsers() {
        when(adminServices.getAllUsers()).thenReturn(Arrays.asList());

        adminController.viewAllUsers();

        verify(adminServices, times(1)).getAllUsers();
    }

    @Test
    public void testViewAllHabits_Success() {
        List<Habit> habits = Arrays.asList(
                new Habit("Habit1", "Description1", Frequency.DAILY,user),
                new Habit("Habit2", "Description2", Frequency.WEEKLY,user)
        );
        when(adminServices.getAllHabits()).thenReturn(habits);

        adminController.viewAllHabits();

        verify(adminServices, times(1)).getAllHabits();
    }

    @Test
    public void testViewAllHabits_NoHabits() {
        when(adminServices.getAllHabits()).thenReturn(Arrays.asList());

        adminController.viewAllHabits();

        verify(adminServices, times(1)).getAllHabits();
    }

    @Test
    public void testBlockUser_Success() {
        when(consoleInputReader
.read("Введите email пользователя для блокировки: ")).thenReturn("user1@example.com");
        when(adminServices.blockUser("user1@example.com")).thenReturn(true);

        adminController.blockUser();

        verify(adminServices, times(1)).blockUser("user1@example.com");
    }

    @Test
    public void testBlockUser_Failure() {
        when(consoleInputReader
.read("Введите email пользователя для блокировки: ")).thenReturn("user1@example.com");
        when(adminServices.blockUser("user1@example.com")).thenReturn(false);

        adminController.blockUser();

        verify(adminServices, times(1)).blockUser("user1@example.com");
    }

    @Test
    public void testUnblockUser_Success() {
        when(consoleInputReader
.read("Введите email пользователя для разблокировки: ")).thenReturn("user1@example.com");
        when(adminServices.unblockUser("user1@example.com")).thenReturn(true);

        adminController.unblockUser();

        verify(adminServices, times(1)).unblockUser("user1@example.com");
    }

    @Test
    public void testUnblockUser_Failure() {
        when(consoleInputReader
.read("Введите email пользователя для разблокировки: ")).thenReturn("user1@example.com");
        when(adminServices.unblockUser("user1@example.com")).thenReturn(false);

        adminController.unblockUser();

        verify(adminServices, times(1)).unblockUser("user1@example.com");
    }

    @Test
    public void testDeleteUser_Success() {
        when(consoleInputReader
.read("Введите email пользователя для удаления: ")).thenReturn("user1@example.com");
        when(adminServices.deleteUser("user1@example.com")).thenReturn(true);

        adminController.deleteUser();

        verify(adminServices, times(1)).deleteUser("user1@example.com");
    }

    @Test
    public void testDeleteUser_Failure() {
        when(consoleInputReader
.read("Введите email пользователя для удаления: ")).thenReturn("user1@example.com");
        when(adminServices.deleteUser("user1@example.com")).thenReturn(false);

        adminController.deleteUser();

        verify(adminServices, times(1)).deleteUser("user1@example.com");
    }
}
