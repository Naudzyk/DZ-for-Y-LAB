package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.HabitCompletion;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HabitCompletionRepository {
    private List<HabitCompletion> completions = new ArrayList<>();

    public void addCompletion(Habit habit, LocalDate completionDate) {
        completions.add(new HabitCompletion(habit, completionDate));
    }

    public List<HabitCompletion> getCompletionsForHabit(Habit habit) {
        return completions.stream()
                .filter(completion -> completion.getHabit().equals(habit))
                .collect(Collectors.toList());
    }

    public List<HabitCompletion> getCompletionsForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate) {
        return completions.stream()
                .filter(completion -> completion.getHabit().equals(habit) &&
                        !completion.getCompletionDate().isBefore(startDate) &&
                        !completion.getCompletionDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
}
