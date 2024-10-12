package org.example.habit_trackingzhenya.services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.UserService;
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean addUser(User user) {
        if(userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        userRepository.addUser(user);
        return true;
    }

    @Override
    public boolean updateEmail(String oldEmail, String newEmail) {
        if (userRepository.existsByEmail(newEmail)) {
            return false;
        }
        userRepository.updateEmail(oldEmail, newEmail);
        return true;
    }

    @Override
    public boolean updateName(String email, String newName) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setName(newName);
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePassword(String email, String newPassword, String oldPassword) {
        User user = userRepository.findByEmail(email);
        if (user!= null && user.getPassword().equals(oldPassword)) {
                user.setPassword(newPassword);
                return true;
        }
        return false;
    }

    @Override
    public User login(String email, String password) {
        User user = userRepository.getUserByEmail(email);
        if(user != null && user.getPassword().equals(password)) {
            return user;
        }
        return null;
    }

    @Override
    public boolean deleteUser(String email, String password) {
        User user = userRepository.getUserByEmail(email);
        if(user != null && user.getPassword().equals(password)) {
            if (userRepository.existsByEmail(email)) {
                userRepository.deleteUser(email);
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean resetPassword(String email, String newPassword) {
        User user = userRepository.getUserByEmail(email);
        if(user != null && user.getPassword().equals(newPassword)) {
            user.setPassword(newPassword);
            return true;
        }
        return false;
    }


}
