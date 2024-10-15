package org.example.habit_trackingzhenya.repositories.Impl;

import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.UserRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserRepositoryImpl implements UserRepository {
    private Map<String, User> users = new HashMap<>();
    @Override
    public void addUser(User user) {
        users.put(user.getEmail(), user);
    }
    @Override
    public User getUserByEmail(String email) {
        return users.get(email);
    }
    @Override
     public boolean existsByEmail(String email) {
        return users.containsKey(email);
    }
    @Override
    public void deleteUser(String email) {
        users.remove(email);
    }
    @Override
    public User findByEmail(String email) {
        return users.get(email);
    }
    @Override
    public void updateEmail(String oldEmail, String newEmail) {
        User user = users.remove(oldEmail);
        if (user != null) {
            user.setEmail(newEmail);
            users.put(newEmail, user);
        }
    }
    @Override
    public boolean updateName(String email, String newName) {
        User user = users.get(email);
        if (user != null) {
            user.setName(newName);
            return true;
        }
        return false;
    }
    @Override
    public boolean updatePassword(String email, String newPassword) {
        User user = users.get(email);
        if (user != null) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }
    @Override
    public List<User> getAllUsers() {
        return new ArrayList<User>(users.values());
    }
}
