package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.HabitCompletion;

import java.time.LocalDate;
import java.util.List;

public interface HabitCompletionService {
    void markHabitCompleted(Habit habit);

    List<HabitCompletion> getCompletionsForHabit(Habit habit);

    List<HabitCompletion> getCompletionsForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate);

    int getCompletionCountForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate);

    int getCurrentStreak(Habit habit);

    double getCompletionPercentageForPeriod(Habit habit, LocalDate startDate, LocalDate endDate);

    String generateProgressReport(Habit habit, LocalDate startDate, LocalDate endDate);
}
