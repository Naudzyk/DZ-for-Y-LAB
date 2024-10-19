package org.example.habit_trackingzhenya.repositories.Impl;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.HabitCompletion;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HabitCompletionRepositoryImpl implements HabitCompletionRepository {

    private final Connection connection;

    public HabitCompletionRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void save(Habit habit) throws SQLException {
        String sql = "INSERT INTO habit_schema.habits (name, description, frequency, creation_date, user_id) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, habit.getName());
            statement.setString(2, habit.getDescription());
            statement.setString(3, habit.getFrequency().name());
            statement.setDate(4, java.sql.Date.valueOf(habit.getCreationDate()));
            statement.setLong(5, habit.getUser().getId());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    habit.setId(generatedKeys.getLong(1));
                }
            }
        }

    }

    @Override
    public void addCompletion(Habit habit, LocalDate completionDate) throws SQLException {
        String sql = "INSERT INTO habit_schema.habit_completions (habit_id, completion_date) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, habit.getId());
            statement.setDate(2, java.sql.Date.valueOf(completionDate));
            statement.executeUpdate();
        }
    }

    @Override
    public List<HabitCompletion> getCompletionsForHabit(Habit habit) throws SQLException {
        List<HabitCompletion> completions = new ArrayList<>();
        String sql = "SELECT * FROM habit_schema.habit_completions WHERE habit_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, habit.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    HabitCompletion completion = new HabitCompletion(
                            habit,
                            resultSet.getDate("completion_date").toLocalDate()
                    );
                    completions.add(completion);
                }
            }
        }
        return completions;
    }

    @Override
    public List<HabitCompletion> getCompletionsForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate) throws SQLException {
        List<HabitCompletion> completions = new ArrayList<>();
        String sql = "SELECT * FROM habit_schema.habit_completions WHERE habit_id = ? AND completion_date BETWEEN ? AND ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, habit.getId());
            statement.setDate(2, java.sql.Date.valueOf(startDate));
            statement.setDate(3, java.sql.Date.valueOf(endDate));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    HabitCompletion completion = new HabitCompletion(
                            habit,
                            resultSet.getDate("completion_date").toLocalDate()
                    );
                    completions.add(completion);
                }
            }
        }
        return completions;
    }
}
