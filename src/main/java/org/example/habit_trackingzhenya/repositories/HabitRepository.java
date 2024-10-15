package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

import java.util.List;

public interface HabitRepository {
    boolean addHabit(Habit habit);

    boolean updateHabit(Habit habit, String name, String description, Frequency frequency);

    boolean deleteHabit(Habit habit);

    List<Habit> getUserHabits(User user);

    List<Habit> getAllHabits();

}
