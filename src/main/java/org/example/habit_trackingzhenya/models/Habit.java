package org.example.habit_trackingzhenya.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Habit {
    private String name;
    private String description;
    private Frequency frequency;
    private LocalDate creationDate;
    private User user;

    public Habit(String name, String description, Frequency frequency, User user) {
        this.name = name;
        this.description = description;
        this.frequency = frequency;
        this.creationDate = LocalDate.now();
        this.user = user;
    }

}
