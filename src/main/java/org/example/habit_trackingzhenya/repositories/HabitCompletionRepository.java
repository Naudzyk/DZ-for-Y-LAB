package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.HabitCompletion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface HabitCompletionRepository {
    void save(Habit habit) throws SQLException;

    void addCompletion(Habit habit, LocalDate completionDate) throws SQLException;

    List<HabitCompletion> getCompletionsForHabit(Habit habit) throws SQLException;

    List<HabitCompletion> getCompletionsForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate) throws SQLException;


}
