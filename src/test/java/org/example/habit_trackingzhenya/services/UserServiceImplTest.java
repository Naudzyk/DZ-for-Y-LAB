package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

     @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddUser_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);

        boolean result = userService.addUser(user);

        assertTrue(result);
        verify(userRepository).addUser(user);
    }

    @Test
    void testAddUser_EmailExists() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);

        boolean result = userService.addUser(user);

        assertFalse(result);
        verify(userRepository, never()).addUser(user);
    }

    @Test
    void testUpdateEmail_Success() {
        String oldEmail = "john.doe@example.com";
        String newEmail = "jane.doe@example.com";
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);

        boolean result = userService.updateEmail(oldEmail, newEmail);

        assertTrue(result);
        verify(userRepository).updateEmail(oldEmail, newEmail);
    }

    @Test
    void testUpdateEmail_EmailExists() {
        String oldEmail = "john.doe@example.com";
        String newEmail = "jane.doe@example.com";
        when(userRepository.existsByEmail(newEmail)).thenReturn(true);

        boolean result = userService.updateEmail(oldEmail, newEmail);

        assertFalse(result);
        verify(userRepository, never()).updateEmail(oldEmail, newEmail);
    }

    @Test
    void testUpdateName_Success() {
        String email = "john.doe@example.com";
        String newName = "Jane Doe";
        User user = new User("John Doe", email, "password", Role.USER, false);
        when(userRepository.findByEmail(email)).thenReturn(user);

        boolean result = userService.updateName(email, newName);

        assertTrue(result);
        assertEquals(newName, user.getName());
    }

    @Test
    void testUpdateName_UserNotFound() {
        String email = "john.doe@example.com";
        String newName = "Jane Doe";
        when(userRepository.findByEmail(email)).thenReturn(null);

        boolean result = userService.updateName(email, newName);

        assertFalse(result);
    }

    @Test
    void testUpdatePassword_Success() {
        String email = "john.doe@example.com";
        String oldPassword = "password";
        String newPassword = "newpassword";
        User user = new User("John Doe", email, oldPassword, Role.USER, false);
        when(userRepository.findByEmail(email)).thenReturn(user);

        boolean result = userService.updatePassword(email, newPassword, oldPassword);

        assertTrue(result);
        assertEquals(newPassword, user.getPassword());
    }

    @Test
    void testUpdatePassword_WrongOldPassword() {
        String email = "john.doe@example.com";
        String oldPassword = "wrongpassword";
        String newPassword = "newpassword";
        User user = new User("John Doe", email, "password", Role.USER, false);
        when(userRepository.findByEmail(email)).thenReturn(user);

        boolean result = userService.updatePassword(email, newPassword, oldPassword);

        assertFalse(result);
        assertNotEquals(newPassword, user.getPassword());
    }

    @Test
    void testUpdatePassword_UserNotFound() {
        String email = "john.doe@example.com";
        String oldPassword = "password";
        String newPassword = "newpassword";
        when(userRepository.findByEmail(email)).thenReturn(null);

        boolean result = userService.updatePassword(email, newPassword, oldPassword);

        assertFalse(result);
    }

    @Test
    void testLogin_Success() {
        String email = "john.doe@example.com";
        String password = "password";
        User user = new User("John Doe", email, password, Role.USER, false);
        when(userRepository.getUserByEmail(email)).thenReturn(user);

        User result = userService.login(email, password);

        assertNotNull(result);
        assertEquals(user, result);
    }

    @Test
    void testLogin_WrongPassword() {
        String email = "john.doe@example.com";
        String password = "wrongpassword";
        User user = new User("John Doe", email, "password", Role.USER, false);
        when(userRepository.getUserByEmail(email)).thenReturn(user);

        User result = userService.login(email, password);

        assertNull(result);
    }

    @Test
    void testLogin_UserNotFound() {
        String email = "john.doe@example.com";
        String password = "password";
        when(userRepository.getUserByEmail(email)).thenReturn(null);

        User result = userService.login(email, password);

        assertNull(result);
    }

    @Test
    void testDeleteUser_Success() {
        String email = "john.doe@example.com";
        String password = "password";
        User user = new User("John Doe", email, password, Role.USER, false);
        when(userRepository.getUserByEmail(email)).thenReturn(user);
        when(userRepository.existsByEmail(email)).thenReturn(true);

        boolean result = userService.deleteUser(email, password);

        assertTrue(result);
        verify(userRepository).deleteUser(email);
    }

    @Test
    void testDeleteUser_WrongPassword() {
        String email = "john.doe@example.com";
        String password = "wrongpassword";
        User user = new User("John Doe", email, "password", Role.USER, false);
        when(userRepository.getUserByEmail(email)).thenReturn(user);

        boolean result = userService.deleteUser(email, password);

        assertFalse(result);
        verify(userRepository, never()).deleteUser(email);
    }

    @Test
    void testDeleteUser_UserNotFound() {
        String email = "john.doe@example.com";
        String password = "password";
        when(userRepository.getUserByEmail(email)).thenReturn(null);

        boolean result = userService.deleteUser(email, password);

        assertFalse(result);
        verify(userRepository, never()).deleteUser(email);
    }


    @Test
    void testResetPassword_UserNotFound() {
        String email = "john.doe@example.com";
        String newPassword = "newpassword";
        when(userRepository.getUserByEmail(email)).thenReturn(null);

        boolean result = userService.resetPassword(email, newPassword);

        assertFalse(result);
    }
}
