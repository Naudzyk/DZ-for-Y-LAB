package org.example.habit_trackingzhenya.repository;

import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HabitCompletionRepositoryTest {
    private HabitCompletionRepository habitCompletionRepository;
    private Habit habit1;
    private Habit habit2;
    private User user1;

    @BeforeEach
    public void setUp() {
        user1 = new User("Zhenya","zhenya@mail.ru","password", Role.USER);
        habitCompletionRepository = new HabitCompletionRepository();
        habit1 = new Habit("Habit1", "Description1", Frequency.DAILY, user1);
        habit2 = new Habit("Habit2", "Description2", Frequency.WEEKLY, user1);
    }

    @Test
    public void testAddCompletion() {
        habitCompletionRepository.addCompletion(habit1, LocalDate.of(2023, 10, 1));
        habitCompletionRepository.addCompletion(habit1, LocalDate.of(2023, 10, 2));

        List<HabitCompletion> completions = habitCompletionRepository.getCompletionsForHabit(habit1);

        assertThat(completions).hasSize(2);
        assertThat(completions.get(0).getCompletionDate()).isEqualTo(LocalDate.of(2023, 10, 1));
        assertThat(completions.get(1).getCompletionDate()).isEqualTo(LocalDate.of(2023, 10, 2));
    }

    @Test
    public void testGetCompletionsForHabit() {
        habitCompletionRepository.addCompletion(habit1, LocalDate.of(2023, 10, 1));
        habitCompletionRepository.addCompletion(habit1, LocalDate.of(2023, 10, 2));
        habitCompletionRepository.addCompletion(habit2, LocalDate.of(2023, 10, 3));

        List<HabitCompletion> completionsForHabit1 = habitCompletionRepository.getCompletionsForHabit(habit1);
        List<HabitCompletion> completionsForHabit2 = habitCompletionRepository.getCompletionsForHabit(habit2);

        assertThat(completionsForHabit1).hasSize(2);
        assertThat(completionsForHabit1.get(0).getCompletionDate()).isEqualTo(LocalDate.of(2023, 10, 1));
        assertThat(completionsForHabit1.get(1).getCompletionDate()).isEqualTo(LocalDate.of(2023, 10, 2));

        assertThat(completionsForHabit2).hasSize(1);
        assertThat(completionsForHabit2.get(0).getCompletionDate()).isEqualTo(LocalDate.of(2023, 10, 3));
    }

    @Test
    public void testGetCompletionsForHabitInPeriod() {
        habitCompletionRepository.addCompletion(habit1, LocalDate.of(2023, 10, 1));
        habitCompletionRepository.addCompletion(habit1, LocalDate.of(2023, 10, 2));
        habitCompletionRepository.addCompletion(habit1, LocalDate.of(2023, 10, 4));
        habitCompletionRepository.addCompletion(habit2, LocalDate.of(2023, 10, 3));

        LocalDate startDate = LocalDate.of(2023, 10, 2);
        LocalDate endDate = LocalDate.of(2023, 10, 3);

        List<HabitCompletion> completionsForHabit1 = habitCompletionRepository.getCompletionsForHabitInPeriod(habit1, startDate, endDate);
        List<HabitCompletion> completionsForHabit2 = habitCompletionRepository.getCompletionsForHabitInPeriod(habit2, startDate, endDate);

        assertThat(completionsForHabit1).hasSize(1);
        assertThat(completionsForHabit1.get(0).getCompletionDate()).isEqualTo(LocalDate.of(2023, 10, 2));

        assertThat(completionsForHabit2).hasSize(1);
        assertThat(completionsForHabit2.get(0).getCompletionDate()).isEqualTo(LocalDate.of(2023, 10, 3));
    }
}
