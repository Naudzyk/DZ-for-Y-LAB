package org.example.habit_trackingzhenya.repositories.Impl;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HabitRepositoryImpl implements HabitRepository {
    private List<Habit> habits = new ArrayList<>();
    @Override
    public boolean addHabit(Habit habit) {
        if (habit == null) {
            return false;
        }
        habits.add(habit);
        return true;
    }
    @Override
    public boolean updateHabit(Habit habit, String name, String description, Frequency frequency) {
        if (habit == null) {
            return false;
        }
        habit.setName(name);
        habit.setDescription(description);
        habit.setFrequency(frequency);
        return true;
    }
    @Override
    public boolean deleteHabit(Habit habit) {
        if (habit == null) {
            return false;
        }
        return habits.remove(habit);
    }
    @Override
    public List<Habit> getUserHabits(User user) {
        if (user == null) {
            return null;
        }
        return habits.stream()
                .filter(habit -> habit.getUser().equals(user))
                .collect(Collectors.toList());
    }
    @Override
    public List<Habit> getAllHabits() {
        return habits;
    }
}
