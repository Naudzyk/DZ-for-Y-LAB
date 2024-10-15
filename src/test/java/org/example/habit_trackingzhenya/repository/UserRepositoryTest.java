package org.example.habit_trackingzhenya.repository;

import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.Impl.UserRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserRepositoryTest {

    private UserRepository userRepository;

    @BeforeEach
    public void setUp() {
        userRepository = new UserRepositoryImpl();
    }

    @Test
    public void testAddUser() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER);
        userRepository.addUser(user);

        User retrievedUser = userRepository.getUserByEmail("john.doe@example.com");
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getName()).isEqualTo("John Doe");
        assertThat(retrievedUser.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(retrievedUser.getPassword()).isEqualTo("password");
        assertThat(retrievedUser.getRole()).isEqualTo(Role.USER);
    }

    @Test
    public void testExistsByEmail() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER);
        userRepository.addUser(user);

        boolean exists = userRepository.existsByEmail("john.doe@example.com");
        assertThat(exists).isTrue();

        boolean doesNotExist = userRepository.existsByEmail("nonexistent@example.com");
        assertThat(doesNotExist).isFalse();
    }

    @Test
    public void testDeleteUser() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER);
        userRepository.addUser(user);

        userRepository.deleteUser("john.doe@example.com");

        User retrievedUser = userRepository.getUserByEmail("john.doe@example.com");
        assertThat(retrievedUser).isNull();
    }

    @Test
    public void testFindByEmail() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER);
        userRepository.addUser(user);

        User retrievedUser = userRepository.findByEmail("john.doe@example.com");
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getName()).isEqualTo("John Doe");
        assertThat(retrievedUser.getEmail()).isEqualTo("john.doe@example.com");
        assertThat(retrievedUser.getPassword()).isEqualTo("password");
        assertThat(retrievedUser.getRole()).isEqualTo(Role.USER);
    }

    @Test
    public void testUpdateEmail() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER);
        userRepository.addUser(user);

        userRepository.updateEmail("john.doe@example.com", "john.smith@example.com");

        User retrievedUser = userRepository.getUserByEmail("john.smith@example.com");
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getName()).isEqualTo("John Doe");
        assertThat(retrievedUser.getEmail()).isEqualTo("john.smith@example.com");
        assertThat(retrievedUser.getPassword()).isEqualTo("password");
        assertThat(retrievedUser.getRole()).isEqualTo(Role.USER);

        User oldEmailUser = userRepository.getUserByEmail("john.doe@example.com");
        assertThat(oldEmailUser).isNull();
    }

    @Test
    public void testUpdateName() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER);
        userRepository.addUser(user);

        boolean updated = userRepository.updateName("john.doe@example.com", "John Smith");
        assertThat(updated).isTrue();

        User retrievedUser = userRepository.getUserByEmail("john.doe@example.com");
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getName()).isEqualTo("John Smith");
    }

    @Test
    public void testUpdatePassword() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER);
        userRepository.addUser(user);

        boolean updated = userRepository.updatePassword("john.doe@example.com", "newPassword");
        assertThat(updated).isTrue();

        User retrievedUser = userRepository.getUserByEmail("john.doe@example.com");
        assertThat(retrievedUser).isNotNull();
        assertThat(retrievedUser.getPassword()).isEqualTo("newPassword");
    }

    @Test
    public void testGetAllUsers() {
        User user1 = new User("John Doe", "john.doe@example.com", "password", Role.USER);
        User user2 = new User("Jane Smith", "jane.smith@example.com", "password", Role.USER);
        userRepository.addUser(user1);
        userRepository.addUser(user2);

        List<User> allUsers = userRepository.getAllUsers();
        assertThat(allUsers).hasSize(2);
        assertThat(allUsers).containsExactlyInAnyOrder(user1, user2);
    }
}
