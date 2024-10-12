package org.example.habit_trackingzhenya.services;

import org.example.habit_trackingzhenya.exception.InputInvalidException;
import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.services.Impl.HabitServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class HabitServiceImplTest {
    private HabitServiceImpl habitService;
    private HabitRepository habitRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        habitRepository = Mockito.mock(HabitRepository.class);
        habitService = new HabitServiceImpl(habitRepository);
        user = new User("User", "user@example.com", "password", Role.USER);
    }

    @Test
    public void testCreateHabit_Success() {
        boolean result = habitService.createHabit(user, "Habit1", "Description1", Frequency.DAILY);

        assertThat(result).isTrue();
        assertThat(user.getHabits()).hasSize(1);
        assertThat(user.getHabits().get(0).getName()).isEqualTo("Habit1");
    }

    @Test
    public void testCreateHabit_NullUser() {
        boolean result = habitService.createHabit(null, "Habit1", "Description1", Frequency.DAILY);

        assertThat(result).isFalse();
    }

    @Test
    public void testGetHabit_Success() {
        Habit habit1 = new Habit("Habit1", "Description1", Frequency.DAILY, user);
        Habit habit2 = new Habit("Habit2", "Description2", Frequency.WEEKLY, user);
        user.addHabit(habit1);
        user.addHabit(habit2);

        List<Habit> result = habitService.getHabit(user);

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(habit1, habit2);
    }

    @Test
    public void testGetHabit_NullUser() {
        List<Habit> result = habitService.getHabit(null);

        assertThat(result).isNull();
    }

    @Test
    public void testUpdateHabit_Success() {
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, user);
        when(habitRepository.updateHabit(habit, "NewHabit", "NewDescription", Frequency.WEEKLY)).thenReturn(true);

        boolean result = habitService.updateHabit(habit, "NewHabit", "NewDescription", Frequency.WEEKLY);

        assertThat(result).isTrue();
        verify(habitRepository, times(1)).updateHabit(habit, "NewHabit", "NewDescription", Frequency.WEEKLY);
    }

    @Test
    public void testUpdateHabit_NullHabit() {
        assertThatThrownBy(() -> habitService.updateHabit(null, "NewHabit", "NewDescription", Frequency.WEEKLY))
                .isInstanceOf(InputInvalidException.class)
                .hasMessage("Habit, name, description, or frequency cannot be null");
    }

    @Test
    public void testUpdateHabit_NullName() {
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, user);
        assertThatThrownBy(() -> habitService.updateHabit(habit, null, "NewDescription", Frequency.WEEKLY))
                .isInstanceOf(InputInvalidException.class)
                .hasMessage("Habit, name, description, or frequency cannot be null");
    }

    @Test
    public void testUpdateHabit_NullDescription() {
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, user);
        assertThatThrownBy(() -> habitService.updateHabit(habit, "NewHabit", null, Frequency.WEEKLY))
                .isInstanceOf(InputInvalidException.class)
                .hasMessage("Habit, name, description, or frequency cannot be null");
    }

    @Test
    public void testUpdateHabit_NullFrequency() {
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, user);
        assertThatThrownBy(() -> habitService.updateHabit(habit, "NewHabit", "NewDescription", null))
                .isInstanceOf(InputInvalidException.class)
                .hasMessage("Habit, name, description, or frequency cannot be null");
    }

    @Test
    public void testDeleteHabit_Success() {
        Habit habit = new Habit("Habit1", "Description1", Frequency.DAILY, user);
        when(habitRepository.deleteHabit(habit)).thenReturn(true);

        boolean result = habitService.deleteHabit(habit);

        assertThat(result).isTrue();
        verify(habitRepository, times(1)).deleteHabit(habit);
    }

    @Test
    public void testDeleteHabit_NullHabit() {
        assertThatThrownBy(() -> habitService.deleteHabit(null))
                .isInstanceOf(InputInvalidException.class)
                .hasMessage("Habit не удалился");
    }
}
