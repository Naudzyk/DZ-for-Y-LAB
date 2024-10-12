package org.example.habit_trackingzhenya.services;
import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.services.Impl.HabitCompletionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.*;

public class HabitCompletionServiceImplTest {
    private HabitCompletionServiceImpl habitCompletionService;
    private HabitCompletionRepository habitCompletionRepository;
    private Habit habit;

    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("Zhenya","zhenya@mail.ru","password", Role.USER);
        habitCompletionRepository = Mockito.mock(HabitCompletionRepository.class);
        habitCompletionService = new HabitCompletionServiceImpl(habitCompletionRepository);
        habit = new Habit("Habit1", "Description1", Frequency.DAILY,user);
    }

    @Test
    public void testMarkHabitCompleted() {
        habitCompletionService.markHabitCompleted(habit);

        verify(habitCompletionRepository, times(1)).addCompletion(habit, LocalDate.now());
    }

    @Test
    public void testGetCompletionsForHabit() {
        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(habit, LocalDate.of(2023, 10, 1)),
                new HabitCompletion(habit, LocalDate.of(2023, 10, 2))
        );
        when(habitCompletionRepository.getCompletionsForHabit(habit)).thenReturn(completions);

        List<HabitCompletion> result = habitCompletionService.getCompletionsForHabit(habit);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(completions);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabit(habit);
    }

    @Test
    public void testGetCompletionsForHabitInPeriod() {
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = LocalDate.of(2023, 10, 3);
        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(habit, LocalDate.of(2023, 10, 1)),
                new HabitCompletion(habit, LocalDate.of(2023, 10, 2))
        );
        when(habitCompletionRepository.getCompletionsForHabitInPeriod(habit, startDate, endDate)).thenReturn(completions);

        List<HabitCompletion> result = habitCompletionService.getCompletionsForHabitInPeriod(habit, startDate, endDate);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyElementsOf(completions);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabitInPeriod(habit, startDate, endDate);
    }

    @Test
    public void testGetCompletionCountForHabitInPeriod() {
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = LocalDate.of(2023, 10, 3);
        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(habit, LocalDate.of(2023, 10, 1)),
                new HabitCompletion(habit, LocalDate.of(2023, 10, 2))
        );
        when(habitCompletionRepository.getCompletionsForHabitInPeriod(habit, startDate, endDate)).thenReturn(completions);

        int result = habitCompletionService.getCompletionCountForHabitInPeriod(habit, startDate, endDate);

        assertThat(result).isEqualTo(2);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabitInPeriod(habit, startDate, endDate);
    }

    @Test
    public void testGetCurrentStreak() {
        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(habit, LocalDate.of(2023, 10, 1)),
                new HabitCompletion(habit, LocalDate.of(2023, 10, 2)),
                new HabitCompletion(habit, LocalDate.of(2023, 10, 4))
        );
        when(habitCompletionRepository.getCompletionsForHabit(habit)).thenReturn(completions);

        int result = habitCompletionService.getCurrentStreak(habit);

        assertThat(result).isEqualTo(1);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabit(habit);
    }

    @Test
    public void testGetCompletionPercentageForPeriod() {
        LocalDate startDate = LocalDate.of(2023, 10, 1);
        LocalDate endDate = LocalDate.of(2023, 10, 3);
        List<HabitCompletion> completions = Arrays.asList(
                new HabitCompletion(habit, LocalDate.of(2023, 10, 1)),
                new HabitCompletion(habit, LocalDate.of(2023, 10, 2))
        );
        when(habitCompletionRepository.getCompletionsForHabitInPeriod(habit, startDate, endDate)).thenReturn(completions);

        double result = habitCompletionService.getCompletionPercentageForPeriod(habit, startDate, endDate);

        assertThat(result).isEqualTo(66.67, offset(0.01));
        verify(habitCompletionRepository, times(1)).getCompletionsForHabitInPeriod(habit, startDate, endDate);
    }

}
