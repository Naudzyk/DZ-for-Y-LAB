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
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;

    @BeforeEach
    public void setUp() {
        userRepository = Mockito.mock(UserRepository.class);
        user = new User("Zhenya","zhenya@mail.ru","password", Role.USER);

    }

    @Test
    void testaddUser() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(false);
        boolean result = userService.addUser(user);
        assertThat(result).isTrue();
        verify(userRepository, times(1)).addUser(user);

    }

    @Test
    void testaddUser_False() {
        when(userRepository.existsByEmail(user.getEmail())).thenReturn(true);
        boolean result =  userService.addUser(user);
        assertThat(result).isFalse();
        verify(userRepository,never()).addUser(user);
    }
    @Test
    void testLogin() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        assertThat(loggedInUser).isEqualTo(user);
    }

    @Test
    void testLogin_UserNotFound() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(null);
        User loggedInUser = userService.login(user.getEmail(), user.getPassword());
        assertThat(loggedInUser).isNull();
    }

    @Test
    void testLogin_IncorrectPassword() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
        User loggedInUser = userService.login(user.getEmail(), "wrongPassword");
        assertThat(loggedInUser).isNull();
    }

    @Test
    void testUpdateEmail() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(false);
        boolean result = userService.updateEmail(user.getEmail(), "new@example.com");
        assertThat(result).isTrue();
        verify(userRepository, times(1)).updateEmail(user.getEmail(), "new@example.com");
    }

    @Test
    void testUpdateEmail_EmailAlreadyExists() {
        when(userRepository.existsByEmail("new@example.com")).thenReturn(true);
        boolean result = userService.updateEmail(user.getEmail(), "new@example.com");
        assertThat(result).isFalse();
        verify(userRepository, never()).updateEmail(user.getEmail(), "new@example.com");
    }

    @Test
    void testUpdateName() {
        boolean result = userService.updateName(user.getEmail(), "New Name");
        assertThat(result).isTrue();
        verify(userRepository, times(1)).updateName(user.getEmail(), "New Name");
    }

    @Test
    void testUpdatePassword() {
        boolean result = userService.updatePassword(user.getEmail(), "newPassword", user.getPassword());
        assertThat(result).isTrue();
        verify(userRepository, times(1)).updatePassword(user.getEmail(), "newPassword");
    }

    @Test
    void testUpdatePassword_IncorrectOldPassword() {
        boolean result = userService.updatePassword(user.getEmail(), "newPassword", "wrongPassword");
        assertThat(result).isFalse();
        verify(userRepository, never()).updatePassword(user.getEmail(), "newPassword");
    }


    @Test
    void testDeleteUser() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
        boolean result = userService.deleteUser(user.getEmail(), user.getPassword());
        assertThat(result).isTrue();
        verify(userRepository, times(1)).deleteUser(user.getEmail());
    }

    @Test
    void testDeleteUser_IncorrectPassword() {
        when(userRepository.getUserByEmail(user.getEmail())).thenReturn(user);
        boolean result = userService.deleteUser(user.getEmail(), "wrongPassword");
        assertThat(result).isFalse();
        verify(userRepository, never()).deleteUser(user.getEmail());
    }



}
