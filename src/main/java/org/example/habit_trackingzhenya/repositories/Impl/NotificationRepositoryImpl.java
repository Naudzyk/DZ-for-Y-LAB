package org.example.habit_trackingzhenya.repositories.Impl;

import org.example.habit_trackingzhenya.exception.InputInvalidException;
import org.example.habit_trackingzhenya.models.Frequency;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.Notification;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class NotificationRepositoryImpl implements NotificationRepository {
   private static final Logger logger = Logger.getLogger(NotificationRepositoryImpl.class.getName());
    private final Connection connection;

    public NotificationRepositoryImpl(Connection connection) {
        this.connection = connection;
    }



    @Override
    public boolean add(Notification notification) throws SQLException {
        if (notification == null || notification.getSendTime() == null) {
            throw new InputInvalidException("Notification or send time cannot be null");
        }
        String sql = "INSERT INTO habit_schema.notifications (user_id, habit_id, message, send_time) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, notification.getUser().getId());
            statement.setLong(2, notification.getHabit().getId());
            statement.setString(3, notification.getMessage());
            statement.setTimestamp(4, java.sql.Timestamp.valueOf(notification.getSendTime()));
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                logger.info("Уведомление успешно отправлено: " + notification.getMessage());
                return true;
            }
        } catch (SQLException e) {
            logger.severe("Ошибка при отправке уведомления: " + e.getMessage());
            throw e;
        }
        return false;
    }


    @Override
    public List<Notification> getNotifications(User user) throws SQLException {
        List<Notification> notifications = new ArrayList<>();
        String sql = "SELECT * FROM habit_schema.notifications WHERE user_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Habit habit = null;
                    if (resultSet.getLong("habit_id") != 0) {
                        habit = getHabitById(resultSet.getLong("habit_id"));
                    }

                    Notification notification = Notification.builder()
                            .id(resultSet.getLong("id"))
                            .user(user)
                            .habit(habit)
                            .message(resultSet.getString("message"))
                            .sendTime(resultSet.getTimestamp("send_time").toLocalDateTime())
                            .build();
                    notifications.add(notification);
                }
            }
            logger.info("Получены уведомления для пользователя: " + user.getId() + " - Количество: " + notifications.size());
        } catch (SQLException e) {
            logger.severe("Ошибка при получении уведомлений для пользователя: " + e.getMessage());
            throw e;
        }
        return notifications;
    }
    private Habit getHabitById(Long habitId) throws SQLException {
            String sql = "SELECT * FROM habit_schema.habits WHERE id = ?";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setLong(1, habitId);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        User user = User.builder()
                                .id(resultSet.getLong("user_id"))
                                .build();

                        return Habit.builder()
                                .id(resultSet.getLong("id"))
                                .name(resultSet.getString("name"))
                                .description(resultSet.getString("description"))
                                .frequency(Frequency.valueOf(resultSet.getString("frequency")))
                                .creationDate(resultSet.getDate("creation_date").toLocalDate())
                                .user(user)
                                .build();
                    }
                }
            }
            return null;
    }
}
