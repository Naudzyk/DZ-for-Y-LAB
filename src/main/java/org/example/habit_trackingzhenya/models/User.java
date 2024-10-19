package org.example.habit_trackingzhenya.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private boolean blocked;
    private Role role;
    private List<Habit> habits;

    // Инициализация списка привычек в конструкторе
    public User(Long id, String name, String email, String password, boolean blocked, Role role, List<Habit> habits) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.blocked = blocked;
        this.role = role;
        this.habits = habits != null ? habits : new ArrayList<>();
    }

    // Метод для добавления привычки
    public void addHabit(Habit habit) {
        if (habits == null) {
            habits = new ArrayList<>();
        }
        habits.add(habit);
    }
}
