package org.example.habit_trackingzhenya.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HabitCompletion {
    private Habit habit;
    private LocalDate completionDate;
}
