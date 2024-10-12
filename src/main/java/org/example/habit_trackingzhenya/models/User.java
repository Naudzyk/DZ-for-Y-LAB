package org.example.habit_trackingzhenya.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private String name;

    private String email;

    private String password;

    private boolean blocked;

    private Role role;

    private List<Habit> habits;

    public User(String johnDoe, String mail, String password, Role role, boolean b) {
        this.name = johnDoe;
        this.email = mail;
        this.password = password;
        this.role = role;
        this.blocked = b;
    }

    public void addHabit(Habit habit) {
        habits.add(habit);
    }

    public User(String name, String email, String password, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.role = role;
        this.habits = new ArrayList<>();
    }
}
