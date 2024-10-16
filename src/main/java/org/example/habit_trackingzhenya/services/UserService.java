package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.User;

public interface UserService {
    boolean addUser(User user);

    boolean updateEmail(String oldEmail, String newEmail);

    boolean updateName(String email, String newName);

    boolean updatePassword(String email, String newPassword,String oldPassword);

    User login(String email, String password);

    boolean deleteUser(String email, String password);

    boolean resetPassword(String email, String newPassword);

}
