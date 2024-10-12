package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HabitRepository {
    private List<Habit> habits = new ArrayList<>();

    public boolean addHabit(Habit habit) {
        if (habit == null) {
            return false;
        }
        habits.add(habit);
        return true;
    }

    public boolean updateHabit(Habit habit, String name, String description, Frequency frequency) {
        if (habit == null) {
            return false;
        }
        habit.setName(name);
        habit.setDescription(description);
        habit.setFrequency(frequency);
        return true;
    }

    public boolean deleteHabit(Habit habit) {
        if (habit == null) {
            return false;
        }
        return habits.remove(habit);
    }

    public List<Habit> getUserHabits(User user) {
        if (user == null) {
            return null;
        }
        return habits.stream()
                .filter(habit -> habit.getUser().equals(user))
                .collect(Collectors.toList());
    }
    public List<Habit> getAllHabits() {
        return habits;
    }
}
