package org.example.habit_trackingzhenya.controller;

import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.Impl.UserServiceImpl;
import org.example.habit_trackingzhenya.utils.ConsoleInputReader;
import org.example.habit_trackingzhenya.utils.Utils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserControllerTest {

    private UserController userController;
    private UserServiceImpl userService;
    private ConsoleInputReader consoleInputReader;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        consoleInputReader = Mockito.mock(Utils.class);
        userController = new UserController(userService, consoleInputReader);
    }

    @Test
    void testRegisterUser_Success() {
        when(consoleInputReader.read("Введите ваше имя: ")).thenReturn("John Doe");
        when(consoleInputReader.read("Введите ваш email: ")).thenReturn("john.doe@example.com");
        when(consoleInputReader.read("Введите ваш пароль: ")).thenReturn("password");
        when(consoleInputReader.read("Введите роль (USER/ADMIN): ")).thenReturn("USER");
        when(userService.addUser(any(User.class))).thenReturn(true);

        userController.registerUser();

        verify(userService).addUser(any(User.class));
        verify(consoleInputReader, times(4)).read(anyString());
    }

    @Test
    void testRegisterUser_Failure() {
        when(consoleInputReader.read("Введите ваше имя: ")).thenReturn("John Doe");
        when(consoleInputReader.read("Введите ваш email: ")).thenReturn("john.doe@example.com");
        when(consoleInputReader.read("Введите ваш пароль: ")).thenReturn("password");
        when(consoleInputReader.read("Введите роль (USER/ADMIN): ")).thenReturn("USER");
        when(userService.addUser(any(User.class))).thenReturn(false);

        userController.registerUser();

        verify(userService).addUser(any(User.class));
        verify(consoleInputReader
, times(4)).read(anyString());
    }

    @Test
    void testLoginUser_Success() {
        when(consoleInputReader
.read("Введите ваш Email: ")).thenReturn("john.doe@example.com");
        when(consoleInputReader
.read("Введите ваш Password: ")).thenReturn("password");
        when(userService.login("john.doe@example.com", "password")).thenReturn(new User("John Doe", "john.doe@example.com", "password", Role.USER));

        User user = userController.loginUser();

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        verify(userService).login("john.doe@example.com", "password");
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testLoginUser_Failure() {
        when(consoleInputReader
.read("Введите ваш Email: ")).thenReturn("john.doe@example.com");
        when(consoleInputReader
.read("Введите ваш Password: ")).thenReturn("wrongpassword");
        when(userService.login("john.doe@example.com", "wrongpassword")).thenReturn(null);

        User user = userController.loginUser();

        assertNull(user);
        verify(userService).login("john.doe@example.com", "wrongpassword");
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdateEmail_Success() {
        when(consoleInputReader
.read("Введите ваш выбор: ")).thenReturn("1");
        when(consoleInputReader
.read("Введите новый Email: ")).thenReturn("new.email@example.com");
        when(userService.updateEmail("john.doe@example.com", "new.email@example.com")).thenReturn(true);

        userController.editProfile("john.doe@example.com");

        verify(userService).updateEmail("john.doe@example.com", "new.email@example.com");
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdateEmail_Failure() {
        when(consoleInputReader
.read("Введите ваш выбор: ")).thenReturn("1");
        when(consoleInputReader
.read("Введите новый Email: ")).thenReturn("new.email@example.com");
        when(userService.updateEmail("john.doe@example.com", "new.email@example.com")).thenReturn(false);

        userController.editProfile("john.doe@example.com");

        verify(userService).updateEmail("john.doe@example.com", "new.email@example.com");
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdateName_Success() {
        when(consoleInputReader
.read("Введите ваш выбор: ")).thenReturn("2");
        when(consoleInputReader
.read("Введите новое Имя: ")).thenReturn("New Name");
        when(userService.updateName("john.doe@example.com", "New Name")).thenReturn(true);

        userController.editProfile("john.doe@example.com");

        verify(userService).updateName("john.doe@example.com", "New Name");
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdateName_Failure() {
        when(consoleInputReader
.read("Введите ваш выбор: ")).thenReturn("2");
        when(consoleInputReader
.read("Введите новое Имя: ")).thenReturn("New Name");
        when(userService.updateName("john.doe@example.com", "New Name")).thenReturn(false);

        userController.editProfile("john.doe@example.com");

        verify(userService).updateName("john.doe@example.com", "New Name");
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdatePassword_Success() {
        when(consoleInputReader
.read("Введите ваш выбор: ")).thenReturn("3");
        when(consoleInputReader
.read("Введите старый Пароль: ")).thenReturn("oldpassword");
        when(consoleInputReader
.read("Введите новый Пароль: ")).thenReturn("newpassword");
        when(userService.updatePassword("john.doe@example.com", "newpassword", "oldpassword")).thenReturn(true);

        userController.editProfile("john.doe@example.com");

        verify(userService).updatePassword("john.doe@example.com", "newpassword", "oldpassword");
        verify(consoleInputReader
, times(3)).read(anyString());
    }

    @Test
    void testEditProfile_UpdatePassword_Failure() {
        when(consoleInputReader
.read("Введите ваш выбор: ")).thenReturn("3");
        when(consoleInputReader
.read("Введите старый Пароль: ")).thenReturn("wrongpassword");
        when(consoleInputReader
.read("Введите новый Пароль: ")).thenReturn("newpassword");
        when(userService.updatePassword("john.doe@example.com", "newpassword", "wrongpassword")).thenReturn(false);

        userController.editProfile("john.doe@example.com");

        verify(userService).updatePassword("john.doe@example.com", "newpassword", "wrongpassword");
        verify(consoleInputReader
, times(3)).read(anyString());
    }

    @Test
    void testResetPassword_Success() {
        when(consoleInputReader
.read("Введите новый пароль: ")).thenReturn("newpassword");
        when(userService.resetPassword("john.doe@example.com", "newpassword")).thenReturn(true);

        userController.resetPassword("john.doe@example.com");

        verify(userService).resetPassword("john.doe@example.com", "newpassword");
        verify(consoleInputReader
).read("Введите новый пароль: ");
    }

    @Test
    void testResetPassword_Failure() {
        when(consoleInputReader
.read("Введите новый пароль: ")).thenReturn("newpassword");
        when(userService.resetPassword("john.doe@example.com", "newpassword")).thenReturn(false);

        userController.resetPassword("john.doe@example.com");

        verify(userService).resetPassword("john.doe@example.com", "newpassword");
        verify(consoleInputReader
).read("Введите новый пароль: ");
    }

    @Test
    void testDeleteUser_Success() {
        when(consoleInputReader
.read("Введите пароль: ")).thenReturn("password");
        when(userService.deleteUser("john.doe@example.com", "password")).thenReturn(true);

        userController.deleteUser("john.doe@example.com");

        verify(userService).deleteUser("john.doe@example.com", "password");
        verify(consoleInputReader
).read("Введите пароль: ");
    }

    @Test
    void testDeleteUser_Failure() {
        when(consoleInputReader
.read("Введите пароль: ")).thenReturn("wrongpassword");
        when(userService.deleteUser("john.doe@example.com", "wrongpassword")).thenReturn(false);

        userController.deleteUser("john.doe@example.com");

        verify(userService).deleteUser("john.doe@example.com", "wrongpassword");
        verify(consoleInputReader
).read("Введите пароль: ");
    }
}
