package org.example.habit_trackingzhenya.repositories.Impl;

import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.HabitCompletion;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HabitCompletionRepositoryImpl implements HabitCompletionRepository {
    private List<HabitCompletion> completions = new ArrayList<>();
    @Override
    public void addCompletion(Habit habit, LocalDate completionDate) {
        completions.add(new HabitCompletion(habit, completionDate));
    }
    @Override
    public List<HabitCompletion> getCompletionsForHabit(Habit habit) {
        return completions.stream()
                .filter(completion -> completion.getHabit().equals(habit))
                .collect(Collectors.toList());
    }
    @Override
    public List<HabitCompletion> getCompletionsForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate) {
        return completions.stream()
                .filter(completion -> completion.getHabit().equals(habit) &&
                        !completion.getCompletionDate().isBefore(startDate) &&
                        !completion.getCompletionDate().isAfter(endDate))
                .collect(Collectors.toList());
    }
}
