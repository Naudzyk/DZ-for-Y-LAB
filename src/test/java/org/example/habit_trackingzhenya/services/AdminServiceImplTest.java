package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.Impl.AdminServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class AdminServiceImplTest {
    private AdminServiceImpl adminService;
    private UserRepository userRepository;
    private HabitRepository habitRepository;

    private User adminUser;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        habitRepository = Mockito.mock(HabitRepository.class);
        adminService = new AdminServiceImpl(userRepository, habitRepository);
    }

    @Test
    public void testIsAdmin_AdminUser() {
        adminUser = new User("Admin", "admin@example.com", "password", Role.ADMIN);
        assertThat(adminService.isAdmin(adminUser)).isTrue();
    }

    @Test
    public void testIsAdmin_RegularUser() {
        User regularUser = new User("User", "user@example.com", "password", Role.USER);
        assertThat(adminService.isAdmin(regularUser)).isFalse();
    }

    @Test
    public void testGetAllUsers() {
        List<User> users = Arrays.asList(
                new User("User1", "user1@example.com", "password", Role.USER),
                new User("User2", "user2@example.com", "password", Role.USER)
        );
        when(userRepository.getAllUsers()).thenReturn(users);

        List<User> result = adminService.getAllUsers();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(users);
        verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    public void testGetAllHabits() {
        List<Habit> habits = Arrays.asList(
                new Habit("Habit1", "Description1", Frequency.DAILY,adminUser),
                new Habit("Habit2", "Description2", Frequency.WEEKLY,adminUser)
        );
        when(habitRepository.getAllHabits()).thenReturn(habits);

        List<Habit> result = adminService.getAllHabits();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(habits);
        verify(habitRepository, times(1)).getAllHabits();
    }

    @Test
    public void testBlockUser_Success() {
        User user = new User("User", "user@example.com", "password", Role.USER);
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        boolean result = adminService.blockUser("user@example.com");

        assertThat(result).isTrue();
        assertThat(user.isBlocked()).isTrue();
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testBlockUser_UserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(null);

        boolean result = adminService.blockUser("user@example.com");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testUnblockUser_Success() {
        User user = new User("User", "user@example.com", "password", Role.USER);
        user.setBlocked(true);
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        boolean result = adminService.unblockUser("user@example.com");

        assertThat(result).isTrue();
        assertThat(user.isBlocked()).isFalse();
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testUnblockUser_UserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(null);

        boolean result = adminService.unblockUser("user@example.com");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByEmail("user@example.com");
    }

    @Test
    public void testDeleteUser_Success() {
        User user = new User("User", "user@example.com", "password", Role.USER);
        when(userRepository.findByEmail("user@example.com")).thenReturn(user);

        boolean result = adminService.deleteUser("user@example.com");

        assertThat(result).isTrue();
        verify(userRepository, times(1)).findByEmail("user@example.com");
        verify(userRepository, times(1)).deleteUser("user@example.com");
    }

    @Test
    public void testDeleteUser_UserNotFound() {
        when(userRepository.findByEmail("user@example.com")).thenReturn(null);

        boolean result = adminService.deleteUser("user@example.com");

        assertThat(result).isFalse();
        verify(userRepository, times(1)).findByEmail("user@example.com");
        verify(userRepository, never()).deleteUser(anyString());
    }
}

