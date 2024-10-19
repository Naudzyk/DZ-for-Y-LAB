package org.example.habit_trackingzhenya.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Habit {
    private Long id;
    private String name;
    private String description;
    private Frequency frequency;
    private LocalDate creationDate;
    private User user;

}
