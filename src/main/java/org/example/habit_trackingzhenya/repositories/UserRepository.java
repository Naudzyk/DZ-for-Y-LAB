package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepository {
    private Map<String, User> users = new HashMap<>();

    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }
    public User getUserByEmail(String email) {
        return users.get(email);
    }
     public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }

    public void deleteUser(String email) {
        users.remove(email);
    }

    public User findByEmail(String email) {
        return users.get(email);
    }

    public void updateEmail(String oldEmail, String newEmail) {
        User user = users.remove(oldEmail);
        if (user != null) {
            user.setEmail(newEmail);
            users.put(newEmail, user);
        }
    }
    public boolean updateName(String email, String newName) {
        User user = users.get(email);
        if (user != null) {
            user.setName(newName);
            return true;
        }
        return false;
    }

    public boolean updatePassword(String email, String newPassword) {
        User user = users.get(email);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }
}
