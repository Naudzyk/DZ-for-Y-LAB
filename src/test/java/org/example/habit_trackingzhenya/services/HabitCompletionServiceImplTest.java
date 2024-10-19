package org.example.habit_trackingzhenya.services;
import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.services.Impl.HabitCompletionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.now;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class HabitCompletionServiceImplTest {

    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @InjectMocks
    private HabitCompletionServiceImpl habitCompletionService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testMarkHabitCompleted() throws SQLException {
        // Arrange
        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .build();

        // Act
        habitCompletionService.markHabitCompleted(habit);

        // Assert
        verify(habitCompletionRepository, times(1)).addCompletion(habit, LocalDate.now());
    }

    @Test
    public void testGetCompletionsForHabit() throws SQLException {
        // Arrange
        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .build();

        HabitCompletion completion1 = new HabitCompletion(habit, LocalDate.now().minusDays(1));
        HabitCompletion completion2 = new HabitCompletion(habit, LocalDate.now());
        List<HabitCompletion> completions = Arrays.asList(completion1, completion2);

        when(habitCompletionRepository.getCompletionsForHabit(habit)).thenReturn(completions);

        // Act
        List<HabitCompletion> result = habitCompletionService.getCompletionsForHabit(habit);

        // Assert
        assertEquals(2, result.size());
        verify(habitCompletionRepository, times(1)).getCompletionsForHabit(habit);
    }

    @Test
    public void testGetCompletionsForHabitInPeriod() throws SQLException {
        // Arrange
        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .build();

        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();

        HabitCompletion completion1 = new HabitCompletion(habit, LocalDate.now().minusDays(1));
        HabitCompletion completion2 = new HabitCompletion(habit, LocalDate.now());
        List<HabitCompletion> completions = Arrays.asList(completion1, completion2);

        when(habitCompletionRepository.getCompletionsForHabitInPeriod(habit, startDate, endDate)).thenReturn(completions);

        // Act
        List<HabitCompletion> result = habitCompletionService.getCompletionsForHabitInPeriod(habit, startDate, endDate);

        // Assert
        assertEquals(2, result.size());
        verify(habitCompletionRepository, times(1)).getCompletionsForHabitInPeriod(habit, startDate, endDate);
    }

    @Test
    public void testGetCompletionCountForHabitInPeriod() throws SQLException {
        // Arrange
        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .build();

        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();

        HabitCompletion completion1 = new HabitCompletion(habit, LocalDate.now().minusDays(1));
        HabitCompletion completion2 = new HabitCompletion(habit, LocalDate.now());
        List<HabitCompletion> completions = Arrays.asList(completion1, completion2);

        when(habitCompletionRepository.getCompletionsForHabitInPeriod(habit, startDate, endDate)).thenReturn(completions);

        // Act
        int result = habitCompletionService.getCompletionCountForHabitInPeriod(habit, startDate, endDate);

        // Assert
        assertEquals(2, result);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabitInPeriod(habit, startDate, endDate);
    }

    @Test
    public void testGetCurrentStreak() throws SQLException {
        // Arrange
        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .build();

        HabitCompletion completion1 = new HabitCompletion(habit, LocalDate.now().minusDays(2));
        HabitCompletion completion2 = new HabitCompletion(habit, LocalDate.now().minusDays(1));
        HabitCompletion completion3 = new HabitCompletion(habit, LocalDate.now());
        List<HabitCompletion> completions = Arrays.asList(completion1, completion2, completion3);

        when(habitCompletionRepository.getCompletionsForHabit(habit)).thenReturn(completions);

        // Act
        int result = habitCompletionService.getCurrentStreak(habit);

        // Assert
        assertEquals(3, result);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabit(habit);
    }

    @Test
    public void testGetCompletionPercentageForPeriod() throws SQLException {
        // Arrange
        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .build();

        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();

        HabitCompletion completion1 = new HabitCompletion(habit, LocalDate.now().minusDays(1));
        HabitCompletion completion2 = new HabitCompletion(habit, LocalDate.now());
        List<HabitCompletion> completions = Arrays.asList(completion1, completion2);

        when(habitCompletionRepository.getCompletionsForHabitInPeriod(habit, startDate, endDate)).thenReturn(completions);

        // Act
        double result = habitCompletionService.getCompletionPercentageForPeriod(habit, startDate, endDate);

        // Assert
        assertEquals(66.67, result, 0.01);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabitInPeriod(habit, startDate, endDate);
    }

    @Test
    public void testGenerateProgressReport() throws SQLException {
        // Arrange
        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .build();

        LocalDate startDate = LocalDate.now().minusDays(2);
        LocalDate endDate = LocalDate.now();

        HabitCompletion completion1 = new HabitCompletion(habit, LocalDate.now().minusDays(1));
        HabitCompletion completion2 = new HabitCompletion(habit, LocalDate.now());
        List<HabitCompletion> completions = Arrays.asList(completion1, completion2);

        when(habitCompletionRepository.getCompletionsForHabitInPeriod(habit, startDate, endDate)).thenReturn(completions);

        // Act
        String result = habitCompletionService.generateProgressReport(habit, startDate, endDate);

        // Assert
        String expectedReport = "Отчет по прогрессу выполнения привычки Test Habit за период с " +
                startDate + " по " + endDate + ":\n" +
                "Количество выполнений: 2\n" +
                "Текущая серия выполнений: 0\n" +
                "Процент успешного выполнения: 66.66666666666666%";
        assertEquals(expectedReport, result);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabitInPeriod(habit, startDate, endDate);
    }
}