package org.example.habit_trackingzhenya.controller;

import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.services.HabitCompletionService;
import org.example.habit_trackingzhenya.services.HabitService;
import org.example.habit_trackingzhenya.services.Impl.HabitCompletionServiceImpl;
import org.example.habit_trackingzhenya.services.Impl.HabitServiceImpl;
import org.example.habit_trackingzhenya.services.Impl.NotificationServiceImpl;
import org.example.habit_trackingzhenya.services.NotificationService;

import org.example.habit_trackingzhenya.utils.ConsoleInputReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class HabitControllerTest {
    private HabitController habitController;
    private HabitServiceImpl habitService;
    private HabitCompletionServiceImpl habitCompletionService;
    private NotificationServiceImpl notificationService;
    private ConsoleInputReader consoleInputReader;


    @BeforeEach
    public void setUp() {
        habitService = Mockito.mock(HabitServiceImpl.class);
        habitCompletionService = Mockito.mock(HabitCompletionServiceImpl.class);
        notificationService = Mockito.mock(NotificationServiceImpl.class);
        consoleInputReader = Mockito.mock(ConsoleInputReader.class);
        habitController = new HabitController(habitService, habitCompletionService, notificationService, consoleInputReader);
    }

       @Test
    void testCreateHabit_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        when(consoleInputReader
.read("Введите название привычки: ")).thenReturn("Habit1");
        when(consoleInputReader
.read("Введите описание привычки: ")).thenReturn("Description1");
        when(consoleInputReader
.read("Введите частоту (DAILY/WEEKLY): ")).thenReturn("DAILY");
        when(habitService.createHabit(user, "Habit1", "Description1", Frequency.DAILY)).thenReturn(true);

        habitController.createHabit(user);

        verify(habitService).createHabit(user, "Habit1", "Description1", Frequency.DAILY);
        verify(consoleInputReader
, times(3)).read(anyString());
    }

    @Test
    void testCreateHabit_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.createHabit(user);

        verify(habitService, never()).createHabit(any(), anyString(), anyString(), any());
        verify(consoleInputReader
, never()).read(anyString());
    }

    @Test
    void testUpdateHabit_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, LocalDate.now(), user);
        when(habitService.getHabit(user)).thenReturn(Arrays.asList(habit));
        when(consoleInputReader
.read("Введите номер привычки: ")).thenReturn("1");
        when(consoleInputReader
.read("Введите новое название привычки: ")).thenReturn("NewHabit");
        when(consoleInputReader
.read("Введите новое описание привычки: ")).thenReturn("NewDescription");
        when(consoleInputReader
.read("Введите новую частоту (DAILY/WEEKLY): ")).thenReturn("WEEKLY");
        when(habitService.updateHabit(habit, "NewHabit", "NewDescription", Frequency.WEEKLY)).thenReturn(true);

        habitController.updateHabit(user);

        verify(habitService).updateHabit(habit, "NewHabit", "NewDescription", Frequency.WEEKLY);
        verify(consoleInputReader
, times(4)).read(anyString());
    }

    @Test
    void testUpdateHabit_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.updateHabit(user);

        verify(habitService, never()).updateHabit(any(), anyString(), anyString(), any());
        verify(consoleInputReader
, never()).read(anyString());
    }

    @Test
    void testDeleteHabit_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, LocalDate.now(), user);
        when(habitService.getHabit(user)).thenReturn(Arrays.asList(habit));
        when(consoleInputReader
.read("Введите номер привычки: ")).thenReturn("1");
        when(habitService.deleteHabit(habit)).thenReturn(true);

        habitController.deleteHabit(user);

        verify(habitService).deleteHabit(habit);
        verify(consoleInputReader
).read("Введите номер привычки: ");
    }

    @Test
    void testDeleteHabit_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.deleteHabit(user);

        verify(habitService, never()).deleteHabit(any());
        verify(consoleInputReader
, never()).read(anyString());
    }

    @Test
    void testViewHabits_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        Habit habit1 = new Habit("Habit1", "Description1", Frequency.DAILY, LocalDate.now(), user);
        Habit habit2 = new Habit("Habit2", "Description2", Frequency.WEEKLY, LocalDate.now(), user);
        when(habitService.getHabit(user)).thenReturn(Arrays.asList(habit1, habit2));

        habitController.viewHabits(user);

        verify(habitService).getHabit(user);
    }

    @Test
    void testViewHabits_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.viewHabits(user);

        verify(habitService, never()).getHabit(any());
    }

    @Test
    void testMarkHabitCompleted_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, LocalDate.now(), user);
        when(habitService.getHabit(user)).thenReturn(Arrays.asList(habit));
        when(consoleInputReader
.read("Введите номер привычки: ")).thenReturn("1");

        habitController.markHabitCompleted(user);

        verify(habitCompletionService).markHabitCompleted(habit);
        verify(consoleInputReader
).read("Введите номер привычки: ");
    }

    @Test
    void testMarkHabitCompleted_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.markHabitCompleted(user);

        verify(habitCompletionService, never()).markHabitCompleted(any());
        verify(consoleInputReader
, never()).read(anyString());
    }

    @Test
    void testViewHabitCompletions_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, LocalDate.now(), user);
        HabitCompletion completion = new HabitCompletion(habit, LocalDate.now());
        when(habitService.getHabit(user)).thenReturn(Arrays.asList(habit));
        when(consoleInputReader
.read("Введите номер привычки: ")).thenReturn("1");
        when(habitCompletionService.getCompletionsForHabit(habit)).thenReturn(Arrays.asList(completion));

        habitController.viewHabitCompletions(user);

        verify(habitCompletionService).getCompletionsForHabit(habit);
        verify(consoleInputReader
).read("Введите номер привычки: ");
    }

    @Test
    void testViewHabitCompletions_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.viewHabitCompletions(user);

        verify(habitCompletionService, never()).getCompletionsForHabit(any());
        verify(consoleInputReader
, never()).read(anyString());
    }

    @Test
    void testViewHabitStatistics_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, LocalDate.now(), user);
        when(habitService.getHabit(user)).thenReturn(Arrays.asList(habit));
        when(consoleInputReader
.read("Введите номер привычки: ")).thenReturn("1");
        when(consoleInputReader
.read("Введите период (DAY/WEEK/MONTH): ")).thenReturn("WEEK");
        when(habitCompletionService.getCompletionCountForHabitInPeriod(habit, LocalDate.now().minusWeeks(1), LocalDate.now())).thenReturn(5);
        when(habitCompletionService.getCurrentStreak(habit)).thenReturn(3);
        when(habitCompletionService.getCompletionPercentageForPeriod(habit, LocalDate.now().minusWeeks(1), LocalDate.now())).thenReturn(75.0);

        habitController.viewHabitStatistics(user);

        verify(habitCompletionService).getCompletionCountForHabitInPeriod(habit, LocalDate.now().minusWeeks(1), LocalDate.now());
        verify(habitCompletionService).getCurrentStreak(habit);
        verify(habitCompletionService).getCompletionPercentageForPeriod(habit, LocalDate.now().minusWeeks(1), LocalDate.now());
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testViewHabitStatistics_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.viewHabitStatistics(user);

        verify(habitCompletionService, never()).getCompletionCountForHabitInPeriod(any(), any(), any());
        verify(habitCompletionService, never()).getCurrentStreak(any());
        verify(habitCompletionService, never()).getCompletionPercentageForPeriod(any(), any(), any());
        verify(consoleInputReader
, never()).read(anyString());
    }

    @Test
    void testGenerateProgressReport_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, LocalDate.now(), user);
        when(habitService.getHabit(user)).thenReturn(Arrays.asList(habit));
        when(consoleInputReader
.read("Введите номер привычки: ")).thenReturn("1");
        when(consoleInputReader
.read("Введите период (DAY/WEEK/MONTH): ")).thenReturn("WEEK");
        when(habitCompletionService.generateProgressReport(habit, LocalDate.now().minusWeeks(1), LocalDate.now())).thenReturn("Report");

        habitController.generateProgressReport(user);

        verify(habitCompletionService).generateProgressReport(habit, LocalDate.now().minusWeeks(1), LocalDate.now());
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testGenerateProgressReport_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.generateProgressReport(user);

        verify(habitCompletionService, never()).generateProgressReport(any(), any(), any());
        verify(consoleInputReader
, never()).read(anyString());
    }

    @Test
    void testSendNotification_Success() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, false);
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, LocalDate.now(), user);
        when(habitService.getHabit(user)).thenReturn(Arrays.asList(habit));
        when(consoleInputReader
.read("Введите номер привычки: ")).thenReturn("1");
        when(consoleInputReader
.read("Введите сообщение для уведомления: ")).thenReturn("Message");

        habitController.sendNotification(user);

        verify(notificationService).sendNotification(user, habit, "Message");
        verify(consoleInputReader
, times(2)).read(anyString());
    }

    @Test
    void testSendNotification_UserBlocked() {
        User user = new User("John Doe", "john.doe@example.com", "password", Role.USER, true);

        habitController.sendNotification(user);

        verify(notificationService, never()).sendNotification(any(), any(), anyString());
        verify(consoleInputReader
, never()).read(anyString());
    }
}