package org.example.habit_trackingzhenya.repositories;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

import java.sql.SQLException;
import java.util.List;

public interface HabitRepository {
    boolean addHabit(Habit habit) throws SQLException;

    boolean updateHabit(Habit habit, String name, String description, Frequency frequency) throws SQLException;

    boolean deleteHabit(Habit habit) throws SQLException;

    List<Habit> getUserHabits(User user) throws SQLException;

    List<Habit> getAllHabits() throws SQLException;

}
