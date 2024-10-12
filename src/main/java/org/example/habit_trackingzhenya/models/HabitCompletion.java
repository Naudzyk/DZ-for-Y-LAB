package org.example.habit_trackingzhenya.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitCompletion {
    private Habit habit;
    private LocalDate completionDate;
}
