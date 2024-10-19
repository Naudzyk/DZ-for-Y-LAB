package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserRepository {
    void insertUser(User user,String sql) throws SQLException;

    void addUser (User user) throws SQLException;

    Optional<User> getUserByEmail(String email) throws SQLException;

    boolean existsByEmail(String email) throws SQLException;

    boolean deleteUser(String email) throws SQLException;

    Optional<User> findByEmail(String email) throws SQLException;

    void updateEmail(String oldEmail, String newEmail) throws SQLException;

    boolean updateName(String email, String newName) throws SQLException;

    boolean updatePassword(String email, String newPassword) throws SQLException;

    Optional<List<User>> getAllUsers() throws SQLException;
}
