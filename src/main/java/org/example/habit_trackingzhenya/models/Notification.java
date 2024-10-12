package org.example.habit_trackingzhenya.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notification {
    private User user;
    private Habit habit;
    private String message;
    private LocalDateTime sendTime;

    public Notification(User user, Habit habit, String message) {
        this.user = user;
        this.habit = habit;
        this.message = message;
        this.sendTime = LocalDateTime.now();
    }

    public Notification(User user, String message) {
        this.user = user;
        this.message = message;
    }
}
