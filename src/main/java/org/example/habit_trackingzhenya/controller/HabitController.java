package org.example.habit_trackingzhenya.controller;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.exception.*;
import org.example.habit_trackingzhenya.models.*;
import org.example.habit_trackingzhenya.services.HabitCompletionService;
import org.example.habit_trackingzhenya.services.HabitService;
import org.example.habit_trackingzhenya.services.Impl.HabitCompletionServiceImpl;
import org.example.habit_trackingzhenya.services.Impl.HabitServiceImpl;
import org.example.habit_trackingzhenya.services.Impl.NotificationServiceImpl;
import org.example.habit_trackingzhenya.services.NotificationService;
import org.example.habit_trackingzhenya.utils.ConsoleInputReader;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@RequiredArgsConstructor
public class HabitController {
    private HabitService habitService;
    private HabitCompletionService habitCompletionService;
    private NotificationService notificationService;
    private ConsoleInputReader consoleInputReader;
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    public HabitController(HabitServiceImpl habitService, HabitCompletionServiceImpl completionService, NotificationServiceImpl notificationService,ConsoleInputReader consoleInputReader) {
        this.habitService = habitService;
        this.habitCompletionService = completionService;
        this.notificationService = notificationService;
        this.consoleInputReader = consoleInputReader;
    }
    private void checkUserBlocked(User user) {
            if (user.isBlocked()) {
                throw new BlockedUserException("Пользователь заблокирован и не может выполнять эту операцию.");
            }

    }

    public void createHabit(User user) {
        try {
            checkUserBlocked(user);
            String name = consoleInputReader.read("Введите название привычки: ");
            String description = consoleInputReader.read("Введите описание привычки: ");
            String frequencyStr = consoleInputReader.read("Введите частоту (DAILY/WEEKLY): ");
            Frequency frequency = Frequency.valueOf(frequencyStr.toUpperCase());

            boolean success = habitService.createHabit(user, name, description, frequency);

            if (success) {
                System.out.println("Привычка успешно создана.");
            } else {
                throw new HabitException("Ошибка при создании привычки.");
            }
        } catch (HabitException e) {
            logger.log(Level.WARNING, "Ошибка создания привычки: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка создания привычки");
        }
    }

    public void updateHabit(User user) {
        try {
            checkUserBlocked(user);
            List<Habit> habits = habitService.getHabit(user);
            if (habits == null || habits.isEmpty()) {
                System.out.println("У вас нет привычек для обновления.");
                return;
            }

            System.out.println("Выберите привычку для обновления:");
            for (int i = 0; i < habits.size(); i++) {
                System.out.println((i + 1) + ". " + habits.get(i).getName());
            }

            int choice = Integer.parseInt(consoleInputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            String name = consoleInputReader.read("Введите новое название привычки: ");
            String description = consoleInputReader.read("Введите новое описание привычки: ");
            String frequencyStr = consoleInputReader.read("Введите новую частоту (DAILY/WEEKLY): ");
            Frequency frequency = Frequency.valueOf(frequencyStr.toUpperCase());

            boolean success = habitService.updateHabit(habit, name, description, frequency);
            if (success) {
                System.out.println("Привычка успешно обновлена.");
            } else {
                throw new UpdateException("Ошибка при обновлении привычки.");
            }
        } catch (UpdateException e) {
            logger.log(Level.WARNING, "Ошибка обновления привычки: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка обновления привычки");
        }
    }

    public void deleteHabit(User user) {
        try {
            checkUserBlocked(user);
            List<Habit> habits = habitService.getHabit(user);
            if (habits == null || habits.isEmpty()) {
                System.out.println("У вас нет привычек для удаления.");
                return;
            }

            System.out.println("Выберите привычку для удаления:");
            for (int i = 0; i < habits.size(); i++) {
                System.out.println((i + 1) + ". " + habits.get(i).getName());
            }

            int choice = Integer.parseInt(consoleInputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            boolean success = habitService.deleteHabit(habit);
            if (success) {
                System.out.println("Привычка успешно удалена.");
            } else {
                throw new DeleteException("Ошибка при удалении привычки.");
            }
        } catch (DeleteException e) {
            logger.log(Level.WARNING, "Ошибка удаления привычки: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка удаления привычек");
        }
    }

    public void viewHabits(User user) {
        try {
            checkUserBlocked(user);
            List<Habit> habits = habitService.getHabit(user);
            if (habits == null || habits.isEmpty()) {
                System.out.println("У вас нет привычек.");
                return;
            }

            System.out.println("Ваши привычки:");
            for (int i = 0; i < habits.size(); i++) {
                Habit habit = habits.get(i);
                System.out.println((i + 1) + ". " + habit.getName() + " - " + habit.getDescription() + " (" + habit.getFrequency() + ")");
            }
        } catch (HabitException e) {
            logger.log(Level.WARNING, "Ошибка при просмотре привычек: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка просмотре привычек");
        }
    }

    public void markHabitCompleted(User user) {
        try {
            checkUserBlocked(user);
            List<Habit> habits = habitService.getHabit(user);
            if (habits == null || habits.isEmpty()) {
                System.out.println("У вас нет привычек для отметки выполнения.");
                return;
            }

            System.out.println("Выберите привычку для отметки выполнения:");
            for (int i = 0; i < habits.size(); i++) {
                System.out.println((i + 1) + ". " + habits.get(i).getName());
            }

            int choice = Integer.parseInt(consoleInputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                throw new HabitException("Неверный выбор.");
            }

            Habit habit = habits.get(choice);
            habitCompletionService.markHabitCompleted(habit);
            System.out.println("Привычка отмечена как выполненная.");
        } catch (HabitException e) {
            logger.log(Level.WARNING, "Ошибка отметки привычки: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка отметки выполнения");
        }
    }

    public void viewHabitCompletions(User user) {
        try {
            checkUserBlocked(user);
            List<Habit> habits = habitService.getHabit(user);
            if (habits == null || habits.isEmpty()) {
                System.out.println("У вас нет привычек.");
                return;
            }

            System.out.println("Выберите привычку для просмотра истории выполнения:");
            for (int i = 0; i < habits.size(); i++) {
                System.out.println((i + 1) + ". " + habits.get(i).getName());
            }

            int choice = Integer.parseInt(consoleInputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            List<HabitCompletion> completions = habitCompletionService.getCompletionsForHabit(habit);
            if (completions.isEmpty()) {
                System.out.println("Нет истории выполнения для этой привычки.");
                return;
            }

            System.out.println("История выполнения для привычки " + habit.getName() + ":");
            for (HabitCompletion completion : completions) {
                System.out.println(completion.getCompletionDate());
            }
        } catch (HabitException e) {
            logger.log(Level.WARNING, "Ошибка при просмотре истории выполнения привычки: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка истории выполнения");
        }
    }

    public void viewHabitStatistics(User user) {
        try {
            checkUserBlocked(user);
            List<Habit> habits = habitService.getHabit(user);
            if (habits == null || habits.isEmpty()) {
                System.out.println("У вас нет привычек.");
                return;
            }

            System.out.println("Выберите привычку для просмотра статистики:");
            for (int i = 0; i < habits.size(); i++) {
                System.out.println((i + 1) + ". " + habits.get(i).getName());
            }

            int choice = Integer.parseInt(consoleInputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            String period = consoleInputReader.read("Введите период (DAY/WEEK/MONTH): ");
            LocalDate endDate = LocalDate.now();
            LocalDate startDate;

            switch (period.toUpperCase()) {
                case "DAY":
                    startDate = endDate;
                    break;
                case "WEEK":
                    startDate = endDate.minusWeeks(1);
                    break;
                case "MONTH":
                    startDate = endDate.minusMonths(1);
                    break;
                default:
                    System.out.println("Неверный период.");
                    return;
            }

            int completionCount = habitCompletionService.getCompletionCountForHabitInPeriod(habit, startDate, endDate);
            int currentStreak = habitCompletionService.getCurrentStreak(habit);
            double completionPercentage = habitCompletionService.getCompletionPercentageForPeriod(habit, startDate, endDate);

            System.out.println("Статистика для привычки " + habit.getName() + " за период с " +
                    startDate.format(DateTimeFormatter.ISO_DATE) + " по " +
                    endDate.format(DateTimeFormatter.ISO_DATE) + ":");
            System.out.println("Количество выполнений: " + completionCount);
            System.out.println("Текущая серия выполнений: " + currentStreak);
            System.out.println("Процент успешного выполнения: " + completionPercentage + "%");
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка просмотра статистики");
        }
    }

     public void generateProgressReport(User user) {
        try {
            checkUserBlocked(user);
            List<Habit> habits = habitService.getHabit(user);
            if (habits == null || habits.isEmpty()) {
                System.out.println("У вас нет привычек.");
                return;
            }

            System.out.println("Выберите привычку для формирования отчета:");
            for (int i = 0; i < habits.size(); i++) {
                System.out.println((i + 1) + ". " + habits.get(i).getName());
            }

            int choice = Integer.parseInt(consoleInputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            String period = consoleInputReader.read("Введите период (DAY/WEEK/MONTH): ");
            LocalDate endDate = LocalDate.now();
            LocalDate startDate;

            switch (period.toUpperCase()) {
                case "DAY":
                    startDate = endDate;
                    break;
                case "WEEK":
                    startDate = endDate.minusWeeks(1);
                    break;
                case "MONTH":
                    startDate = endDate.minusMonths(1);
                    break;
                default:
                    System.out.println("Неверный период.");
                    return;
            }

            String report = habitCompletionService.generateProgressReport(habit, startDate, endDate);
            System.out.println(report);
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка формирования отчета");
        }
    }

    public void sendNotification(User user) {
        try {
            checkUserBlocked(user);
            List<Habit> habits = habitService.getHabit(user);
            if (habits == null || habits.isEmpty()) {
                System.out.println("У вас нет привычек.");
                return;
            }

            System.out.println("Выберите привычку для отправки уведомления:");
            for (int i = 0; i < habits.size(); i++) {
                System.out.println((i + 1) + ". " + habits.get(i).getName());
            }

            int choice = Integer.parseInt(consoleInputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            String message = consoleInputReader.read("Введите сообщение для уведомления: ");
            notificationService.sendNotification(user, habit, message);
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка отправки уведомления");
        }
    }

    public void viewFilteredHabits(User user) {
        try {
            checkUserBlocked(user);
            String startDateStr = consoleInputReader.read("Введите дату начала отчета (гггг-мм-дд) или оставьте пустым: ");
            LocalDate startDate = startDateStr.isEmpty() ? null : LocalDate.parse(startDateStr);

            String endDateStr = consoleInputReader.read("Введите дату конца отчета (гггг-мм-дд) или оставьте пустым: ");
            LocalDate endDate = endDateStr.isEmpty() ? null : LocalDate.parse(endDateStr);

            String completedStr = consoleInputReader.read("Введите статус выполнения (true/false) или оставьте пустым: ");
            Boolean completed = completedStr.isEmpty() ? null : Boolean.parseBoolean(completedStr);

            List<Habit> habits = habitService.getFilteredHabits(user, startDate, endDate, completed);
            if (habits == null || habits.isEmpty()) {
                System.out.println("Нет привычек, соответствующих фильтру.");
                return;
            }

            System.out.println("Отфильтрованные привычки:");
            for (int i = 0; i < habits.size(); i++) {
                Habit habit = habits.get(i);
                System.out.println((i + 1) + ". " + habit.getName() + " - " + habit.getDescription() + " (" + habit.getFrequency() + ")");
            }
        } catch (HabitException e) {
            logger.log(Level.WARNING, "Ошибка при фильтрации привычек: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            logger.log(Level.WARNING, "Ошибка блокировки пользователя: " + e.getMessage(), e);
            System.out.println(e.getMessage());
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Непредвиденная ошибка: " + e.getMessage(), e);
            System.out.println("Непредвиденная ошибка фильтрации привычек");
        }
    }
}
