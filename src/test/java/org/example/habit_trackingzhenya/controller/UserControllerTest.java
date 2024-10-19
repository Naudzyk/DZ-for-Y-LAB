package org.example.habit_trackingzhenya.controller;

import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;


import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private ConsoleInputReader consoleInputReader;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите ваше имя: ")).thenReturn("Test User");
        when(consoleInputReader.read("Введите ваш email: ")).thenReturn("test@example.com");
        when(consoleInputReader.read("Введите ваш пароль: ")).thenReturn("password");
        when(consoleInputReader.read("Введите роль (USER/ADMIN): ")).thenReturn("USER");
        when(userService.addUser(any(User.class))).thenReturn(true);

        // Act
        userController.registerUser();

        // Assert
        verify(consoleInputReader, times(1)).read("Введите ваше имя: ");
        verify(consoleInputReader, times(1)).read("Введите ваш email: ");
        verify(consoleInputReader, times(1)).read("Введите ваш пароль: ");
        verify(consoleInputReader, times(1)).read("Введите роль (USER/ADMIN): ");
        verify(userService, times(1)).addUser(any(User.class));
    }

    @Test
    public void testLoginUser() throws SQLException {
        // Arrange
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .role(Role.USER)
                .build();

        when(consoleInputReader.read("Введите ваш Email: ")).thenReturn("test@example.com");
        when(consoleInputReader.read("Введите ваш Password: ")).thenReturn("password");
        when(userService.login("test@example.com", "password")).thenReturn(user);

        // Act
        User loggedInUser = userController.loginUser();

        // Assert
        verify(consoleInputReader, times(1)).read("Введите ваш Email: ");
        verify(consoleInputReader, times(1)).read("Введите ваш Password: ");
        verify(userService, times(1)).login("test@example.com", "password");
        assertNotNull(loggedInUser);
        assertEquals("Test User", loggedInUser.getName());
    }

    @Test
    public void testEditProfileEmail() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите ваш выбор: ")).thenReturn("1");
        when(consoleInputReader.read("Введите новый Email: ")).thenReturn("newemail@example.com");
        when(userService.updateEmail("test@example.com", "newemail@example.com")).thenReturn(true);

        // Act
        userController.editProfile("test@example.com");

        // Assert
        verify(consoleInputReader, times(1)).read("Введите ваш выбор: ");
        verify(consoleInputReader, times(1)).read("Введите новый Email: ");
        verify(userService, times(1)).updateEmail("test@example.com", "newemail@example.com");
    }

    @Test
    public void testEditProfileName() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите ваш выбор: ")).thenReturn("2");
        when(consoleInputReader.read("Введите новое Имя: ")).thenReturn("New Name");
        when(userService.updateName("test@example.com", "New Name")).thenReturn(true);

        // Act
        userController.editProfile("test@example.com");

        // Assert
        verify(consoleInputReader, times(1)).read("Введите ваш выбор: ");
        verify(consoleInputReader, times(1)).read("Введите новое Имя: ");
        verify(userService, times(1)).updateName("test@example.com", "New Name");
    }

    @Test
    public void testEditProfilePassword() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите ваш выбор: ")).thenReturn("3");
        when(consoleInputReader.read("Введите старый Пароль: ")).thenReturn("oldpassword");
        when(consoleInputReader.read("Введите новый Пароль: ")).thenReturn("newpassword");
        when(userService.updatePassword("test@example.com", "newpassword", "oldpassword")).thenReturn(true);

        // Act
        userController.editProfile("test@example.com");

        // Assert
        verify(consoleInputReader, times(1)).read("Введите ваш выбор: ");
        verify(consoleInputReader, times(1)).read("Введите старый Пароль: ");
        verify(consoleInputReader, times(1)).read("Введите новый Пароль: ");
        verify(userService, times(1)).updatePassword("test@example.com", "newpassword", "oldpassword");
    }

    @Test
    public void testResetPassword() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите новый пароль: ")).thenReturn("newpassword");
        when(userService.resetPassword("test@example.com", "newpassword")).thenReturn(true);

        // Act
        userController.resetPassword("test@example.com");

        // Assert
        verify(consoleInputReader, times(1)).read("Введите новый пароль: ");
        verify(userService, times(1)).resetPassword("test@example.com", "newpassword");
    }

    @Test
    public void testDeleteUser() throws SQLException {
        // Arrange
        when(consoleInputReader.read("Введите пароль: ")).thenReturn("password");
        when(userService.deleteUser("test@example.com", "password")).thenReturn(true);

        // Act
        userController.deleteUser("test@example.com");

        // Assert
        verify(consoleInputReader, times(1)).read("Введите пароль: ");
        verify(userService, times(1)).deleteUser("test@example.com", "password");
    }
}