package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.exception.InputInvalidException;
import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.services.Impl.HabitServiceImpl;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

public class HabitServiceImplTest {

    @Mock
    private HabitRepository habitRepository;

    @Mock
    private HabitCompletionRepository habitCompletionRepository;

    @InjectMocks
    private HabitServiceImpl habitService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateHabit() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        when(habitRepository.addHabit(any(Habit.class))).thenReturn(true);

        // Act
        boolean result = habitService.createHabit(user, "Test Habit", "Test Description", Frequency.DAILY);

        // Assert
        assertTrue(result);
        verify(habitRepository, times(1)).addHabit(any(Habit.class));
    }

    @Test
    public void testGetHabitsByUser() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit1 = Habit.builder()
                .id(1L)
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        Habit habit2 = Habit.builder()
                .id(2L)
                .name("Habit2")
                .description("Description2")
                .frequency(Frequency.WEEKLY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit1, habit2);
        when(habitRepository.getUserHabits(user)).thenReturn(habits);

        // Act
        List<Habit> result = habitService.getHabitsByUser(user);

        // Assert
        assertEquals(2, result.size());
        verify(habitRepository, times(1)).getUserHabits(user);
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
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        when(habitRepository.updateHabit(habit, "New Name", "New Description", Frequency.WEEKLY)).thenReturn(true);

        // Act
        boolean result = habitService.updateHabit(habit, "New Name", "New Description", Frequency.WEEKLY);

        // Assert
        assertTrue(result);
        verify(habitRepository, times(1)).updateHabit(habit, "New Name", "New Description", Frequency.WEEKLY);
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
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        when(habitRepository.deleteHabit(habit)).thenReturn(true);

        // Act
        boolean result = habitService.deleteHabit(habit);

        // Assert
        assertTrue(result);
        verify(habitRepository, times(1)).deleteHabit(habit);
    }

    @Test
    public void testGetFilteredHabits() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit1 = Habit.builder()
                .id(1L)
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now().minusDays(1))
                .build();

        Habit habit2 = Habit.builder()
                .id(2L)
                .name("Habit2")
                .description("Description2")
                .frequency(Frequency.WEEKLY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit1, habit2);
        when(habitRepository.getUserHabits(user)).thenReturn(habits);
        when(habitCompletionRepository.getCompletionsForHabit(habit1)).thenReturn(Arrays.asList(new HabitCompletion(habit1, LocalDate.now())));
        when(habitCompletionRepository.getCompletionsForHabit(habit2)).thenReturn(Arrays.asList(new HabitCompletion(habit2, LocalDate.now())));

        // Act
        List<Habit> result = habitService.getFilteredHabits(user, LocalDate.now().minusDays(2), LocalDate.now(), true);

        // Assert
        assertEquals(2, result.size());
        verify(habitRepository, times(1)).getUserHabits(user);
        verify(habitCompletionRepository, times(2)).getCompletionsForHabit(any(Habit.class));
    }

    @Test
    public void testGetAllHabits() throws SQLException {
        // Arrange
        User user = User.builder()
                .id(1L)
                .name("User1")
                .email("user1@example.com")
                .password("password1")
                .blocked(false)
                .role(Role.USER)
                .build();

        Habit habit1 = Habit.builder()
                .id(1L)
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        Habit habit2 = Habit.builder()
                .id(2L)
                .name("Habit2")
                .description("Description2")
                .frequency(Frequency.WEEKLY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        List<Habit> habits = Arrays.asList(habit1, habit2);
        when(habitRepository.getAllHabits()).thenReturn(habits);

        // Act
        List<Habit> result = habitService.getAllHabits();

        // Assert
        assertEquals(2, result.size());
        verify(habitRepository, times(1)).getAllHabits();
    }

    @Test
    public void testIsHabitCompleted() throws SQLException {
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
                .name("Habit1")
                .description("Description1")
                .frequency(Frequency.DAILY)
                .user(user)
                .creationDate(LocalDate.now())
                .build();

        HabitCompletion completion = new HabitCompletion(habit, LocalDate.now());
        when(habitCompletionRepository.getCompletionsForHabit(habit)).thenReturn(Arrays.asList(completion));

        // Act
        boolean result = habitService.isHabitCompleted(habit, LocalDate.now());

        // Assert
        assertTrue(result);
        verify(habitCompletionRepository, times(1)).getCompletionsForHabit(habit);
    }
}