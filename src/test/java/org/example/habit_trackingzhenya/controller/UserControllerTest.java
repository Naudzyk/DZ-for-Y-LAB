package org.example.habit_trackingzhenya.controller;

import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.services.Impl.UserServiceImpl;
import org.example.habit_trackingzhenya.services.UserService;
import org.example.habit_trackingzhenya.utils.InputReader;
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
    private InputReader inputReader;

    @BeforeEach
    public void setUp() {
        userService = Mockito.mock(UserServiceImpl.class);
        inputReader = Mockito.mock(Utils.class);
        userController = new UserController(userService, inputReader);
    }

    @Test
    void testRegisterUser_Success() {
        when(inputReader.read("Введите ваше имя: ")).thenReturn("John Doe");
        when(inputReader.read("Введите ваш email: ")).thenReturn("john.doe@example.com");
        when(inputReader.read("Введите ваш пароль: ")).thenReturn("password");
        when(inputReader.read("Введите роль (USER/ADMIN): ")).thenReturn("USER");
        when(userService.addUser(any(User.class))).thenReturn(true);

        userController.registerUser();

        verify(userService).addUser(any(User.class));
        verify(inputReader, times(4)).read(anyString());
    }

    @Test
    void testRegisterUser_Failure() {
        when(inputReader.read("Введите ваше имя: ")).thenReturn("John Doe");
        when(inputReader.read("Введите ваш email: ")).thenReturn("john.doe@example.com");
        when(inputReader.read("Введите ваш пароль: ")).thenReturn("password");
        when(inputReader.read("Введите роль (USER/ADMIN): ")).thenReturn("USER");
        when(userService.addUser(any(User.class))).thenReturn(false);

        userController.registerUser();

        verify(userService).addUser(any(User.class));
        verify(inputReader, times(4)).read(anyString());
    }

    @Test
    void testLoginUser_Success() {
        when(inputReader.read("Введите ваш Email: ")).thenReturn("john.doe@example.com");
        when(inputReader.read("Введите ваш Password: ")).thenReturn("password");
        when(userService.login("john.doe@example.com", "password")).thenReturn(new User("John Doe", "john.doe@example.com", "password", Role.USER));

        User user = userController.loginUser();

        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        verify(userService).login("john.doe@example.com", "password");
        verify(inputReader, times(2)).read(anyString());
    }

    @Test
    void testLoginUser_Failure() {
        when(inputReader.read("Введите ваш Email: ")).thenReturn("john.doe@example.com");
        when(inputReader.read("Введите ваш Password: ")).thenReturn("wrongpassword");
        when(userService.login("john.doe@example.com", "wrongpassword")).thenReturn(null);

        User user = userController.loginUser();

        assertNull(user);
        verify(userService).login("john.doe@example.com", "wrongpassword");
        verify(inputReader, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdateEmail_Success() {
        when(inputReader.read("Введите ваш выбор: ")).thenReturn("1");
        when(inputReader.read("Введите новый Email: ")).thenReturn("new.email@example.com");
        when(userService.updateEmail("john.doe@example.com", "new.email@example.com")).thenReturn(true);

        userController.editProfile("john.doe@example.com");

        verify(userService).updateEmail("john.doe@example.com", "new.email@example.com");
        verify(inputReader, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdateEmail_Failure() {
        when(inputReader.read("Введите ваш выбор: ")).thenReturn("1");
        when(inputReader.read("Введите новый Email: ")).thenReturn("new.email@example.com");
        when(userService.updateEmail("john.doe@example.com", "new.email@example.com")).thenReturn(false);

        userController.editProfile("john.doe@example.com");

        verify(userService).updateEmail("john.doe@example.com", "new.email@example.com");
        verify(inputReader, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdateName_Success() {
        when(inputReader.read("Введите ваш выбор: ")).thenReturn("2");
        when(inputReader.read("Введите новое Имя: ")).thenReturn("New Name");
        when(userService.updateName("john.doe@example.com", "New Name")).thenReturn(true);

        userController.editProfile("john.doe@example.com");

        verify(userService).updateName("john.doe@example.com", "New Name");
        verify(inputReader, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdateName_Failure() {
        when(inputReader.read("Введите ваш выбор: ")).thenReturn("2");
        when(inputReader.read("Введите новое Имя: ")).thenReturn("New Name");
        when(userService.updateName("john.doe@example.com", "New Name")).thenReturn(false);

        userController.editProfile("john.doe@example.com");

        verify(userService).updateName("john.doe@example.com", "New Name");
        verify(inputReader, times(2)).read(anyString());
    }

    @Test
    void testEditProfile_UpdatePassword_Success() {
        when(inputReader.read("Введите ваш выбор: ")).thenReturn("3");
        when(inputReader.read("Введите старый Пароль: ")).thenReturn("oldpassword");
        when(inputReader.read("Введите новый Пароль: ")).thenReturn("newpassword");
        when(userService.updatePassword("john.doe@example.com", "newpassword", "oldpassword")).thenReturn(true);

        userController.editProfile("john.doe@example.com");

        verify(userService).updatePassword("john.doe@example.com", "newpassword", "oldpassword");
        verify(inputReader, times(3)).read(anyString());
    }

    @Test
    void testEditProfile_UpdatePassword_Failure() {
        when(inputReader.read("Введите ваш выбор: ")).thenReturn("3");
        when(inputReader.read("Введите старый Пароль: ")).thenReturn("wrongpassword");
        when(inputReader.read("Введите новый Пароль: ")).thenReturn("newpassword");
        when(userService.updatePassword("john.doe@example.com", "newpassword", "wrongpassword")).thenReturn(false);

        userController.editProfile("john.doe@example.com");

        verify(userService).updatePassword("john.doe@example.com", "newpassword", "wrongpassword");
        verify(inputReader, times(3)).read(anyString());
    }

    @Test
    void testResetPassword_Success() {
        when(inputReader.read("Введите новый пароль: ")).thenReturn("newpassword");
        when(userService.resetPassword("john.doe@example.com", "newpassword")).thenReturn(true);

        userController.resetPassword("john.doe@example.com");

        verify(userService).resetPassword("john.doe@example.com", "newpassword");
        verify(inputReader).read("Введите новый пароль: ");
    }

    @Test
    void testResetPassword_Failure() {
        when(inputReader.read("Введите новый пароль: ")).thenReturn("newpassword");
        when(userService.resetPassword("john.doe@example.com", "newpassword")).thenReturn(false);

        userController.resetPassword("john.doe@example.com");

        verify(userService).resetPassword("john.doe@example.com", "newpassword");
        verify(inputReader).read("Введите новый пароль: ");
    }

    @Test
    void testDeleteUser_Success() {
        when(inputReader.read("Введите пароль: ")).thenReturn("password");
        when(userService.deleteUser("john.doe@example.com", "password")).thenReturn(true);

        userController.deleteUser("john.doe@example.com");

        verify(userService).deleteUser("john.doe@example.com", "password");
        verify(inputReader).read("Введите пароль: ");
    }

    @Test
    void testDeleteUser_Failure() {
        when(inputReader.read("Введите пароль: ")).thenReturn("wrongpassword");
        when(userService.deleteUser("john.doe@example.com", "wrongpassword")).thenReturn(false);

        userController.deleteUser("john.doe@example.com");

        verify(userService).deleteUser("john.doe@example.com", "wrongpassword");
        verify(inputReader).read("Введите пароль: ");
    }
}
