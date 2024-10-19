package org.example.habit_trackingzhenya.services.Impl;

import lombok.RequiredArgsConstructor;

import org.example.habit_trackingzhenya.exception.InputInvalidException;
import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.services.HabitService;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;


@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private final HabitRepository habitRepository;
    private final HabitCompletionRepository habitCompletionRepository;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    @Override
    public boolean createHabit(User user, String name, String description, Frequency frequency) {
        if (user == null || name == null || description == null || frequency == null) {
            throw new InputInvalidException("User, name, description, or frequency cannot be null");
        }
        Habit habit = Habit.builder()
                .name(name)
                .description(description)
                .frequency(frequency)
                .user(user)
                .creationDate(LocalDate.now())
                .build();
        user.addHabit(habit);

        try {
            return habitRepository.addHabit(habit);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при сохранении привычки: " + e.getMessage(), e);
            return false;
        }
    }


    @Override
    public List<Habit> getHabitsByUser(User user) {
        try {
            return habitRepository.getUserHabits(user);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Ошибка при получении привычек пользователя: " + e.getMessage(), e);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean updateHabit(Habit habit, String name, String description, Frequency frequency) throws SQLException {
        if (habit == null || name == null || description == null || frequency == null) {
            throw new InputInvalidException("Habit, name, description, or frequency cannot be null");
        }
        if (habit.getId() == null) {
            throw new InputInvalidException("Habit ID cannot be null");
        }
        return habitRepository.updateHabit(habit, name, description, frequency);
    }


    @Override
    public boolean deleteHabit(Habit habit) throws SQLException {
        if (habit == null) {
            throw new InputInvalidException("Habit не удалился");
        }
        return habitRepository.deleteHabit(habit);
    }

    @Override
    public List<Habit> getFilteredHabits(User user, LocalDate startDate, LocalDate endDate, Boolean completed) {
        List<Habit> habits = getHabitsByUser(user);

        return habits.stream()
                .filter(habit -> startDate == null || habit.getCreationDate().isAfter(startDate.minusDays(1)))
                .filter(habit -> endDate == null || habit.getCreationDate().isBefore(endDate.plusDays(1)))
                .filter(habit -> {
                    try {
                        return completed == null || isHabitCompleted(habit, LocalDate.now()) == completed;
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<Habit> getAllHabits() throws SQLException {
        return habitRepository.getAllHabits();
    }

    public boolean isHabitCompleted(Habit habit, LocalDate date) throws SQLException {
        return habitCompletionRepository.getCompletionsForHabit(habit).stream()
                .anyMatch(completion -> completion.getCompletionDate().equals(date));
    }
}
