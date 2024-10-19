package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface HabitService {
    boolean createHabit(User user, String name, String description, Frequency frequency);

    List<Habit> getHabitsByUser(User user) throws SQLException;

    boolean updateHabit(Habit habit, String name, String description, Frequency frequency) throws SQLException;

    boolean deleteHabit(Habit habit) throws SQLException;
    List<Habit> getFilteredHabits(User user, LocalDate startDate, LocalDate endDate, Boolean completed);

    List<Habit > getAllHabits() throws SQLException;
}
