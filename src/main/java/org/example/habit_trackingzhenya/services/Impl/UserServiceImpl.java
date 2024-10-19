package org.example.habit_trackingzhenya.services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.exception.DeleteException;
import org.example.habit_trackingzhenya.exception.EntityNotFoundException;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private HabitRepository habitRepository;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public boolean addUser(User user) throws SQLException {
        if(userRepository.existsByEmail(user.getEmail())) {
            return false;
        }
        userRepository.addUser(user);
        return true;
    }

    @Override
    public boolean updateEmail(String oldEmail, String newEmail) throws SQLException {
        if (userRepository.existsByEmail(newEmail)) {
            return false;
        }
        userRepository.updateEmail(oldEmail, newEmail);
        return true;
    }

    @Override
    public boolean updateName(String email, String newName) throws SQLException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setName(newName);
            return true;
        }
        return false;
    }

    @Override
    public boolean updatePassword(String email, String newPassword, String oldPassword) throws SQLException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getPassword().equals(oldPassword)) {
                user.get().setPassword(newPassword);
                return true;
        }
        return false;
    }

    @Override
    public User login(String email, String password) throws SQLException {
        Optional<User> user = userRepository.getUserByEmail(email);
        if(user.isPresent() && user.get().getPassword().equals(password)) {
            return user.orElse(null);
        }
        return null;
    }

    @Override
    public boolean deleteUser(String email, String password) {
        try {
            User user = userRepository.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
            if (!user.getPassword().equals(password)) {
                throw new DeleteException("Неверный пароль");
            }
            deleteUserHabits(user);
            return userRepository.deleteUser(email);
        } catch (SQLException | EntityNotFoundException | DeleteException e) {
            logger.log(Level.SEVERE, "Ошибка при удалении пользователя: " + e.getMessage(), e);
            return false;
        }
    }
    private void deleteUserHabits(User user) throws SQLException {
        List<Habit> habits = habitRepository.getUserHabits(user);
        for (Habit habit : habits) {
            habitRepository.deleteHabit(habit);
        }
    }

    @Override
    public boolean resetPassword(String email, String newPassword) throws SQLException {
        Optional<User> user = userRepository.getUserByEmail(email);
        if(user.isPresent() && user.get().getPassword().equals(newPassword)) {
            user.get().setPassword(newPassword);
            return true;
        }
        return false;
    }

    @Override
    public Optional<List<User>> getAllUsers() throws SQLException {
        return userRepository.getAllUsers();
    }

    @Override
    public boolean deleteUserForAdmin(String email) throws SQLException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            userRepository.deleteUser(email);
            return true;
        }
        return false;
    }

    @Override
    public boolean unblockUser(String email) throws SQLException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            user.get().setBlocked(false);
            return true;
        }
        return false;
    }

    @Override
    public boolean blockUser(String email) throws SQLException {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent() && user.get().getRole() != Role.ADMIN) {
            user.get().setBlocked(true);
            return true;
        }
        return false;
    }


}
