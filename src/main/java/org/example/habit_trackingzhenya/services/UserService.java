package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.User;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface UserService {
    boolean addUser(User user) throws SQLException;

    boolean updateEmail(String oldEmail, String newEmail) throws SQLException;

    boolean updateName(String email, String newName) throws SQLException;

    boolean updatePassword(String email, String newPassword,String oldPassword) throws SQLException;

    User login(String email, String password) throws SQLException;

    boolean deleteUser(String email, String password) throws SQLException;

    boolean resetPassword(String email, String newPassword) throws SQLException;

    Optional<List<User>> getAllUsers() throws SQLException;

    boolean deleteUserForAdmin(String email) throws SQLException;

    boolean unblockUser(String email) throws SQLException;

    boolean blockUser(String email) throws SQLException;

}
