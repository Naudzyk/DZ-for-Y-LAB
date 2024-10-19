package org.example.habit_trackingzhenya.repositories.Impl;

import org.example.habit_trackingzhenya.exception.InputInvalidException;
import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.time.LocalDate.now;

public class HabitRepositoryImpl implements HabitRepository {
    private final Connection connection;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public HabitRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

@Override
    public boolean addHabit(Habit habit) throws SQLException {
        if (habit == null) {
            return false;
        }
        String sql = "INSERT INTO habit_schema.habits (name, description, frequency, creation_date, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, habit.getName());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequency().name());
            statement.setDate(4, java.sql.Date.valueOf(habit.getCreationDate()));
            statement.setLong(5, habit.getUser().getId());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        habit.setId(generatedKeys.getLong(1));
                    }
                }
                logger.info("Привычка успешно сохранена: " + habit.getName());
                return true;
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при сохранении привычки: " + e.getMessage(), e);
            throw e;
        }
        return false;
    }

    @Override
    public boolean updateHabit(Habit habit, String name, String description, Frequency frequency) throws SQLException {
        if (habit == null || name == null || description == null || frequency == null) {
            throw new InputInvalidException("Habit, name, description, or frequency cannot be null");
        }
        if (habit.getId() == null) {
            throw new InputInvalidException("Habit ID cannot be null");
        }
        String sql = "UPDATE habit_schema.habits SET name = ?, description = ?, frequency = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setString(3, frequency.name());
            statement.setLong(4, habit.getId());
            int rowsUpdated = statement.executeUpdate();
            if (rowsUpdated > 0) {
                habit.setName(name);
                habit.setDescription(description);
                habit.setFrequency(frequency);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean deleteHabit(Habit habit) throws SQLException {
        if (habit == null) {
            return false;
        }
        String sql = "DELETE FROM habit_schema.habits WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, habit.getId());
            int rowsDeleted = statement.executeUpdate();
            return rowsDeleted > 0;
        }
    }

@Override
    public List<Habit> getUserHabits(User user) throws SQLException {
        if (user == null) {
            return null;
        }
        List<Habit> habits = new ArrayList<>();
        String sql = "SELECT * FROM habit_schema.habits WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Habit habit = Habit.builder()
                            .id(resultSet.getLong("id"))
                            .name(resultSet.getString("name"))
                            .description(resultSet.getString("description"))
                            .frequency(Frequency.valueOf(resultSet.getString("frequency")))
                            .creationDate(resultSet.getDate("creation_date").toLocalDate())
                            .user(user)
                            .build();
                    habits.add(habit);
                }
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при извлечении привычек пользователя: " + e.getMessage(), e);
            throw e;
        }
        return habits;
    }

    @Override
    public List<Habit> getAllHabits() throws SQLException {
        List<Habit> habits = new ArrayList<>();
        String sql = "SELECT * FROM habit_schema.habits";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = User.builder()
                        .id(resultSet.getLong("user_id"))
                        .build();

                Habit habit = Habit.builder()
                        .id(resultSet.getLong("id"))
                        .name(resultSet.getString("name"))
                        .description(resultSet.getString("description"))
                        .frequency(Frequency.valueOf(resultSet.getString("frequency")))
                        .creationDate(resultSet.getDate("creation_date").toLocalDate())
                        .user(user)
                        .build();
                habits.add(habit);
            }
        }
        return habits;
    }
}