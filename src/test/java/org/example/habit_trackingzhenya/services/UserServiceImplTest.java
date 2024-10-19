package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.Impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private HabitRepository habitRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddUser() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);


        // Act
        boolean result = userService.addUser(user);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(user.getEmail());
        verify(userRepository, times(1)).addUser(user);
    }

    @Test
    public void testUpdateEmail() throws SQLException {
        // Arrange
        String oldEmail = "user1@example.com";
        String newEmail = "newuser1@example.com";

        when(userRepository.existsByEmail(newEmail)).thenReturn(false);


        // Act
        boolean result = userService.updateEmail(oldEmail, newEmail);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).existsByEmail(newEmail);
        verify(userRepository, times(1)).updateEmail(oldEmail, newEmail);
    }

    @Test
    public void testUpdateName() throws SQLException {
        // Arrange
        String email = "user1@example.com";
        String newName = "New Name";

        User user = User.builder()
                .id(1L)
                .name("User1")
                .email(email)
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.updateName(email, newName);

        // Assert
        assertTrue(result);
        assertEquals(newName, user.getName());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testUpdatePassword() throws SQLException {
        // Arrange
        String email = "user1@example.com";
        String oldPassword = "password1";
        String newPassword = "newpassword1";

        User user = User.builder()
                .id(1L)
                .name("User1")
                .email(email)
                .password(oldPassword)
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.updatePassword(email, newPassword, oldPassword);

        // Assert
        assertTrue(result);
        assertEquals(newPassword, user.getPassword());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testLogin() throws SQLException {
        // Arrange
        String email = "user1@example.com";
        String password = "password1";

        User user = User.builder()
                .id(1L)
                .name("User1")
                .email(email)
                .password(password)
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));

        // Act
        User result = userService.login(email, password);

        // Assert
        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).getUserByEmail(email);
    }

    @Test
    public void testDeleteUser() throws SQLException {
        // Arrange
        String email = "user1@example.com";
        String password = "password1";

        User user = User.builder()
                .id(1L)
                .name("User1")
                .email(email)
                .password(password)
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.deleteUser(email)).thenReturn(true);

        // Act
        boolean result = userService.deleteUser(email, password);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).deleteUser(email);
    }

    @Test
    public void testResetPassword() throws SQLException {
        // Arrange
        String email = "user1@example.com";
        String newPassword = "newpassword1";

        User user = User.builder()
                .id(1L)
                .name("User1")
                .email(email)
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.getUserByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.resetPassword(email, newPassword);

        // Assert
        assertTrue(result);
        assertEquals(newPassword, user.getPassword());
        verify(userRepository, times(1)).getUserByEmail(email);
    }

    @Test
    public void testGetAllUsers() throws SQLException {
        // Arrange
        User user1 = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("User2")
                .email("user2@example.com")
                .password("password2")
                .blocked(false)
                .role(Role.USER)
                .build();

        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.getAllUsers()).thenReturn(Optional.of(users));

        // Act
        Optional<List<User>> result = userService.getAllUsers();

        // Assert
        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        verify(userRepository, times(1)).getAllUsers();
    }

    @Test
    public void testDeleteUserForAdmin() throws SQLException {
        // Arrange
        String email = "user1@example.com";

        User user = User.builder()
                .id(1L)
                .name("User1")
                .email(email)
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(userRepository.deleteUser(email)).thenReturn(true);

        // Act
        boolean result = userService.deleteUserForAdmin(email);

        // Assert
        assertTrue(result);
        verify(userRepository, times(1)).findByEmail(email);
        verify(userRepository, times(1)).deleteUser(email);
    }

    @Test
    public void testUnblockUser() throws SQLException {
        // Arrange
        String email = "user1@example.com";

        User user = User.builder()
                .id(1L)
                .name("User1")
                .email(email)
                .password("password1")
                .blocked(true)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.unblockUser(email);

        // Assert
        assertTrue(result);
        assertFalse(user.isBlocked());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    public void testBlockUser() throws SQLException {
        // Arrange
        String email = "user1@example.com";

        User user = User.builder()
                .id(1L)
                .name("User1")
                .email(email)
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        // Act
        boolean result = userService.blockUser(email);

        // Assert
        assertTrue(result);
        assertTrue(user.isBlocked());
        verify(userRepository, times(1)).findByEmail(email);
    }
}
