package org.example.habit_trackingzhenya.services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.exception.InputInvalidException;
import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.services.HabitService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {

    private HabitRepository habitRepository;
    private HabitCompletionRepository habitCompletionRepository;

    public HabitServiceImpl(HabitRepository habitRepository, HabitCompletionRepository habitCompletionRepository) {
        this.habitRepository = habitRepository;
        this.habitCompletionRepository = habitCompletionRepository;
    }

    @Override
    public boolean createHabit(User user, String name, String description, Frequency frequency) {
        if (user == null) {
            return false;
        }
        Habit habit = new Habit(name, description, frequency, user);
        user.addHabit(habit);
        return true;
    }

    @Override
    public List<Habit> getHabit(User user) {
        if (user == null) {
            return null;
        }
        return user.getHabits();
    }

    @Override
    public boolean updateHabit(Habit habit, String name, String description, Frequency frequency) {
        if (habit == null || name == null || description == null || frequency == null) {
            throw new InputInvalidException("Habit, name, description, or frequency cannot be null");
        }
        return habitRepository.updateHabit(habit, name, description, frequency);
    }

    @Override
    public boolean deleteHabit(Habit habit) {
        if ( habit == null) {
            throw new InputInvalidException("Habit не удалился");
        }
        return habitRepository.deleteHabit(habit);
    }

    @Override
    public List<Habit> getFilteredHabits(User user, LocalDate startDate, LocalDate endDate, Boolean completed) {
        List<Habit> habits = getHabit(user);

        return habits.stream()
                .filter(habit -> startDate == null || habit.getCreationDate().isAfter(startDate.minusDays(1)))
                .filter(habit -> endDate == null || habit.getCreationDate().isBefore(endDate.plusDays(1)))
                .filter(habit -> completed == null || isHabitCompleted(habit,LocalDate.now()) == completed)
                .collect(Collectors.toList());
    }
    private boolean isHabitCompleted(Habit habit, LocalDate date) {
        return habitCompletionRepository.getCompletionsForHabit(habit).stream()
                .anyMatch(completion -> completion.getCompletionDate().equals(date));
    }
}
