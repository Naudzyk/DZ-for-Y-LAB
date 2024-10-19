package org.example.habit_trackingzhenya.controller;

import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.services.Impl.HabitCompletionServiceImpl;
import org.example.habit_trackingzhenya.services.Impl.HabitServiceImpl;
import org.example.habit_trackingzhenya.services.Impl.NotificationServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static java.time.LocalDate.now;
import static org.mockito.Mockito.*;

public class HabitControllerTest {

    @Mock
    private HabitServiceImpl habitService;

    @Mock
    private HabitCompletionServiceImpl habitCompletionService;

    @Mock
    private NotificationServiceImpl notificationService;

    @Mock
    private ConsoleInputReader consoleInputReader;

    @InjectMocks
    private HabitController habitController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateHabit() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        when(consoleInputReader.read("Введите имя привычки: ")).thenReturn("Test Habit");
        when(consoleInputReader.read("Введите описание привычки: ")).thenReturn("Test Description");
        when(consoleInputReader.read("Введите частоту привычки (DAILY, WEEKLY): ")).thenReturn("DAILY");
        when(habitService.createHabit(user, "Test Habit", "Test Description", Frequency.DAILY)).thenReturn(true);

        // Act
        habitController.createHabit(user);

        // Assert
        verify(consoleInputReader, times(1)).read("Введите имя привычки: ");
        verify(consoleInputReader, times(1)).read("Введите описание привычки: ");
        verify(consoleInputReader, times(1)).read("Введите частоту привычки (DAILY, WEEKLY): ");
        verify(habitService, times(1)).createHabit(user, "Test Habit", "Test Description", Frequency.DAILY);
    }

    @Test
    public void testUpdateHabit() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        when(habitService.getHabitsByUser(user)).thenReturn(habits);
        when(consoleInputReader.read("Введите номер привычки: ")).thenReturn("1");
        when(consoleInputReader.read("Введите новое имя привычки: ")).thenReturn("Updated Habit");
        when(consoleInputReader.read("Введите новое описание привычки: ")).thenReturn("Updated Description");
        when(consoleInputReader.read("Введите новую частоту привычки (DAILY, WEEKLY): ")).thenReturn("WEEKLY");
        when(habitService.updateHabit(habit, "Updated Habit", "Updated Description", Frequency.WEEKLY)).thenReturn(true);

        // Act
        habitController.updateHabit(user);

        // Assert
        verify(habitService, times(1)).getHabitsByUser(user);
        verify(consoleInputReader, times(1)).read("Введите номер привычки: ");
        verify(consoleInputReader, times(1)).read("Введите новое имя привычки: ");
        verify(consoleInputReader, times(1)).read("Введите новое описание привычки: ");
        verify(consoleInputReader, times(1)).read("Введите новую частоту привычки (DAILY, WEEKLY): ");
        verify(habitService, times(1)).updateHabit(habit, "Updated Habit", "Updated Description", Frequency.WEEKLY);
    }

    @Test
    public void testDeleteHabit() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        when(habitService.getHabitsByUser(user)).thenReturn(habits);
        when(consoleInputReader.read("Введите номер привычки: ")).thenReturn("1");
        when(habitService.deleteHabit(habit)).thenReturn(true);

        // Act
        habitController.deleteHabit(user);

        // Assert
        verify(habitService, times(1)).getHabitsByUser(user);
        verify(consoleInputReader, times(1)).read("Введите номер привычки: ");
        verify(habitService, times(1)).deleteHabit(habit);
    }

    @Test
    public void testViewHabits() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        when(habitService.getHabitsByUser(user)).thenReturn(habits);

        // Act
        habitController.viewHabits(user);

        // Assert
        verify(habitService, times(1)).getHabitsByUser(user);
    }

    @Test
    public void testMarkHabitCompleted() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        when(habitService.getHabitsByUser(user)).thenReturn(habits);
        when(consoleInputReader.read("Введите номер привычки: ")).thenReturn("1");

        // Act
        habitController.markHabitCompleted(user);

        // Assert
        verify(habitService, times(1)).getHabitsByUser(user);
        verify(consoleInputReader, times(1)).read("Введите номер привычки: ");
        verify(habitCompletionService, times(1)).markHabitCompleted(habit);
    }

    @Test
    public void testViewHabitCompletions() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        List<HabitCompletion> completions = Arrays.asList(new HabitCompletion(habit, LocalDate.now()));
        when(habitService.getHabitsByUser(user)).thenReturn(habits);
        when(consoleInputReader.read("Введите номер привычки: ")).thenReturn("1");
        when(habitCompletionService.getCompletionsForHabit(habit)).thenReturn(completions);

        // Act
        habitController.viewHabitCompletions(user);

        // Assert
        verify(habitService, times(1)).getHabitsByUser(user);
        verify(consoleInputReader, times(1)).read("Введите номер привычки: ");
        verify(habitCompletionService, times(1)).getCompletionsForHabit(habit);
    }

    @Test
    public void testViewHabitStatistics() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        when(habitService.getHabitsByUser(user)).thenReturn(habits);
        when(consoleInputReader.read("Введите номер привычки: ")).thenReturn("1");
        when(consoleInputReader.read("Введите период (DAY/WEEK/MONTH): ")).thenReturn("WEEK");
        when(habitCompletionService.getCompletionCountForHabitInPeriod(habit, LocalDate.now().minusWeeks(1), LocalDate.now())).thenReturn(5);
        when(habitCompletionService.getCurrentStreak(habit)).thenReturn(3);
        when(habitCompletionService.getCompletionPercentageForPeriod(habit, LocalDate.now().minusWeeks(1), LocalDate.now())).thenReturn(75.0);

        // Act
        habitController.viewHabitStatistics(user);

        // Assert
        verify(habitService, times(1)).getHabitsByUser(user);
        verify(consoleInputReader, times(1)).read("Введите номер привычки: ");
        verify(consoleInputReader, times(1)).read("Введите период (DAY/WEEK/MONTH): ");
        verify(habitCompletionService, times(1)).getCompletionCountForHabitInPeriod(habit, LocalDate.now().minusWeeks(1), LocalDate.now());
        verify(habitCompletionService, times(1)).getCurrentStreak(habit);
        verify(habitCompletionService, times(1)).getCompletionPercentageForPeriod(habit, LocalDate.now().minusWeeks(1), LocalDate.now());
    }

    @Test
    public void testGenerateProgressReport() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        when(habitService.getHabitsByUser(user)).thenReturn(habits);
        when(consoleInputReader.read("Введите номер привычки: ")).thenReturn("1");
        when(consoleInputReader.read("Введите период (DAY/WEEK/MONTH): ")).thenReturn("WEEK");
        when(habitCompletionService.generateProgressReport(habit, LocalDate.now().minusWeeks(1), LocalDate.now())).thenReturn("Test Report");

        // Act
        habitController.generateProgressReport(user);

        // Assert
        verify(habitService, times(1)).getHabitsByUser(user);
        verify(consoleInputReader, times(1)).read("Введите номер привычки: ");
        verify(consoleInputReader, times(1)).read("Введите период (DAY/WEEK/MONTH): ");
        verify(habitCompletionService, times(1)).generateProgressReport(habit, LocalDate.now().minusWeeks(1), LocalDate.now());
    }

    @Test
    public void testSendNotification() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        when(habitService.getHabitsByUser(user)).thenReturn(habits);
        when(consoleInputReader.read("Введите номер привычки: ")).thenReturn("1");
        when(consoleInputReader.read("Введите сообщение для уведомления: ")).thenReturn("Test Message");

        // Act
        habitController.sendNotification(user);

        // Assert
        verify(habitService, times(1)).getHabitsByUser(user);
        verify(consoleInputReader, times(1)).read("Введите номер привычки: ");
        verify(consoleInputReader, times(1)).read("Введите сообщение для уведомления: ");
    }

    @Test
    public void testViewFilteredHabits() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit = Habit.builder()
                .id(1L)
                .name("Test Habit")
                .description("Test Description")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit);
        when(consoleInputReader.read("Введите дату начала отчета (гггг-мм-дд) или оставьте пустым: ")).thenReturn("");
        when(consoleInputReader.read("Введите дату конца отчета (гггг-мм-дд) или оставьте пустым: ")).thenReturn("");
        when(consoleInputReader.read("Введите статус выполнения (true/false) или оставьте пустым: ")).thenReturn("");
        when(habitService.getFilteredHabits(user, null, null, null)).thenReturn(habits);

        // Act
        habitController.viewFilteredHabits(user);

        // Assert
        verify(consoleInputReader, times(1)).read("Введите дату начала отчета (гггг-мм-дд) или оставьте пустым: ");
        verify(consoleInputReader, times(1)).read("Введите дату конца отчета (гггг-мм-дд) или оставьте пустым: ");
        verify(consoleInputReader, times(1)).read("Введите статус выполнения (true/false) или оставьте пустым: ");
        verify(habitService, times(1)).getFilteredHabits(user, null, null, null);
    }
}