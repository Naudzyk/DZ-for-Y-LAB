package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.User;

import java.util.List;

public interface UserRepository {
    void addUser (User user);

    User getUserByEmail(String email);

    boolean existsByEmail(String email);

    void deleteUser(String email);

    User findByEmail(String email);

    void updateEmail(String oldEmail, String newEmail);

    boolean updateName(String email, String newName);

    boolean updatePassword(String email, String newPassword);

    List<User> getAllUsers();
}
