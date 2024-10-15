package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.HabitCompletion;

import java.time.LocalDate;
import java.util.List;

public interface HabitCompletionRepository {
    void addCompletion(Habit habit, LocalDate completionDate);

    List<HabitCompletion> getCompletionsForHabit(Habit habit);

    List<HabitCompletion> getCompletionsForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate);


}
