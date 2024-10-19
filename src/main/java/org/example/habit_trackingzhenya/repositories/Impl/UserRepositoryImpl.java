package org.example.habit_trackingzhenya.repositories.Impl;

import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.UserRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Logger;

public class UserRepositoryImpl implements UserRepository {
    private static final Logger logger = Logger.getLogger(UserRepositoryImpl.class.getName());
    private final Connection connection;

    public UserRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insertUser(User user, String sql) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getName());
            statement.setString(2, user.getEmail());
            statement.setString(3, user.getPassword());
            statement.setBoolean(4, user.isBlocked());
            statement.setString(5, user.getRole().name());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
        }
    }

    @Override
    public void addUser(User user) throws SQLException {
        String sqlForUser = "INSERT INTO habit_schema.users (name, email, password, blocked, role) VALUES (?, ?, ?, ?, ?)";
        String sqlForAdmin = "INSERT INTO utility_schema.utility (name, email, password, blocked, role) VALUES (?, ?, ?, ?, ?)";

        try {
            if (user.getRole().equals(Role.ADMIN)) {
                insertUser(user, sqlForAdmin);
            } else {
                insertUser(user, sqlForUser);
            }
            logger.info("Пользователь успешно добавлен: " + user.getName() + " с ролью " + user.getRole());
        } catch (SQLException e) {
            logger.severe("Ошибка при добавлении пользователя: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<User> getUserByEmail(String email) throws SQLException {
        String sqlHabitSchema = "SELECT * FROM habit_schema.users WHERE email = ?";
        String sqlUtilitySchema = "SELECT * FROM utility_schema.utility WHERE email = ?";

        Optional<User> userFromHabitSchema = getUserByEmailFromSchema(sqlHabitSchema, email);
        if (userFromHabitSchema.isPresent()) {
            return userFromHabitSchema;
        }

        return getUserByEmailFromSchema(sqlUtilitySchema, email);
    }

    private Optional<User> getUserByEmailFromSchema(String sql, String email) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = User.builder()
                            .id(resultSet.getLong("id"))
                            .name(resultSet.getString("name"))
                            .email(resultSet.getString("email"))
                            .password(resultSet.getString("password"))
                            .blocked(resultSet.getBoolean("blocked"))
                            .role(Role.valueOf(resultSet.getString("role")))
                            .build();
                    logger.info("Пользователь найден по email: " + email);
                    return Optional.of(user);
                }
            }
        } catch (SQLException e) {
            logger.severe("Ошибка при получении пользователя по email: " + e.getMessage());
            throw e;
        }
        logger.info("Пользователь не найден по email: " + email);
        return Optional.empty();
    }

    @Override
    public boolean existsByEmail(String email) throws SQLException {
        String sqlHabitSchema = "SELECT COUNT(*) FROM habit_schema.users WHERE email = ?";
        String sqlUtilitySchema = "SELECT COUNT(*) FROM utility_schema.utility WHERE email = ?";

        boolean existsInHabitSchema = checkExists(sqlHabitSchema, email);
        boolean existsInUtilitySchema = checkExists(sqlUtilitySchema, email);

        return existsInHabitSchema || existsInUtilitySchema;
    }

    private boolean checkExists(String sql, String email) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    boolean exists = resultSet.getInt(1) > 0;
                    logger.info("Пользователь существует по email: " + email + " - " + exists);
                    return exists;
                }
            }
        } catch (SQLException e) {
            logger.severe("Ошибка при проверке существования пользователя по email: " + e.getMessage());
            throw e;
        }
        logger.info("Пользователь не найден по email: " + email);
        return false;
    }

@Override
    public boolean deleteUser(String email) throws SQLException {
        String sql = "DELETE FROM habit_schema.users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            int rowsDeleted = statement.executeUpdate();
            if (rowsDeleted > 0) {
                logger.info("Пользователь удален по email: " + email);
                return true;
            } else {
                logger.warning("Пользователь не найден по email: " + email);
                return false;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка при удалении пользователя по email: " + e.getMessage());
            throw e;
        }
    }


    @Override
    public Optional<User> findByEmail(String email) throws SQLException {
        return getUserByEmail(email);
    }

    @Override
    public void updateEmail(String oldEmail, String newEmail) throws SQLException {
        String sql = "UPDATE habit_schema.users SET email = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newEmail);
            statement.setString(2, oldEmail);
            int rowsUpdated = statement.executeUpdate();
            logger.info("Email обновлен с " + oldEmail + " на " + newEmail + " - Обновлено строк: " + rowsUpdated);
        } catch (SQLException e) {
            logger.severe("Ошибка при обновлении email: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean updateName(String email, String newName) throws SQLException {
        String sql = "UPDATE habit_schema.users SET name = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newName);
            statement.setString(2, email);
            int rowsUpdated = statement.executeUpdate();
            logger.info("Имя обновлено для email " + email + " на " + newName + " - Обновлено строк: " + rowsUpdated);
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.severe("Ошибка при обновлении имени: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public boolean updatePassword(String email, String newPassword) throws SQLException {
        String sql = "UPDATE habit_schema.users SET password = ? WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, newPassword);
            statement.setString(2, email);
            int rowsUpdated = statement.executeUpdate();
            logger.info("Пароль обновлен для email " + email + " - Обновлено строк: " + rowsUpdated);
            return rowsUpdated > 0;
        } catch (SQLException e) {
            logger.severe("Ошибка при обновлении пароля: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public Optional<List<User>> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM habit_schema.users";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .email(resultSet.getString("email"))
                        .password(resultSet.getString("password"))
                        .blocked(resultSet.getBoolean("blocked"))
                        .role(Role.valueOf(resultSet.getString("role")))
                        .build();
                users.add(user);
            }
            logger.info("Получены все пользователи - Количество: " + users.size());
        } catch (SQLException e) {
            logger.severe("Ошибка при получении всех пользователей: " + e.getMessage());
            throw e;
        }
        return Optional.of(users);
    }
}
