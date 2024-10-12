package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

import java.time.LocalDate;
import java.util.List;

public interface HabitService {
    boolean createHabit(User user, String name, String description, Frequency frequency);

    List<Habit> getHabit(User user);

    boolean updateHabit(Habit habit, String name, String description, Frequency frequency);

    boolean deleteHabit(Habit habit);
    List<Habit> getFilteredHabits(User user, LocalDate startDate, LocalDate endDate, Boolean completed);
}
