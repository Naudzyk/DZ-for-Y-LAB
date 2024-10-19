package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.HabitCompletion;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface HabitCompletionService {
    void markHabitCompleted(Habit habit) throws SQLException;

    List<HabitCompletion> getCompletionsForHabit(Habit habit) throws SQLException;

    List<HabitCompletion> getCompletionsForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate) throws SQLException;

    int getCompletionCountForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate) throws SQLException;

    int getCurrentStreak(Habit habit) throws SQLException;

    double getCompletionPercentageForPeriod(Habit habit, LocalDate startDate, LocalDate endDate) throws SQLException;

    String generateProgressReport(Habit habit, LocalDate startDate, LocalDate endDate) throws SQLException;
}
