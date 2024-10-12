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
import org.example.habit_trackingzhenya.utils.InputReader;
import org.example.habit_trackingzhenya.utils.Utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
public class HabitController {
    private HabitService habitService;
    private HabitCompletionService habitCompletionService;
    private NotificationService notificationService;
    private InputReader inputReader;

    public HabitController(HabitServiceImpl habitService, HabitCompletionServiceImpl completionService, NotificationServiceImpl notificationService,InputReader inputReader) {
        this.habitService = habitService;
        this.habitCompletionService = completionService;
        this.notificationService = notificationService;
        this.inputReader = inputReader;
    }
    private void checkUserBlocked(User user) {
            if (user.isBlocked()) {
                throw new BlockedUserException("Пользователь заблокирован и не может выполнять эту операцию.");
            }

    }

    public void createHabit(User user) {
        try {
            checkUserBlocked(user);
            String name = inputReader.read("Введите название привычки: ");
            String description = inputReader.read("Введите описание привычки: ");
            String frequencyStr = inputReader.read("Введите частоту (DAILY/WEEKLY): ");
            Frequency frequency = Frequency.valueOf(frequencyStr.toUpperCase());

            boolean success = habitService.createHabit(user, name, description, frequency);

            if (success) {
                System.out.println("Привычка успешно создана.");
            } else {
                throw new HabitException("Ошибка при создании привычки.");
            }
        } catch (HabitException e) {
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
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

            int choice = Integer.parseInt(inputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            String name = inputReader.read("Введите новое название привычки: ");
            String description = inputReader.read("Введите новое описание привычки: ");
            String frequencyStr = inputReader.read("Введите новую частоту (DAILY/WEEKLY): ");
            Frequency frequency = Frequency.valueOf(frequencyStr.toUpperCase());

            boolean success = habitService.updateHabit(habit, name, description, frequency);
            if (success) {
                System.out.println("Привычка успешно обновлена.");
            } else {
                throw new UpdateException("Ошибка при обновлении привычки.");
            }
        } catch (UpdateException e) {
            System.out.println(e.getMessage());
        }catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
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

            int choice = Integer.parseInt(inputReader.read("Введите номер привычки: ")) - 1;
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
            System.out.println(e.getMessage());
        }catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
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
            System.out.println(e.getMessage());
        }catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
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

            int choice = Integer.parseInt(inputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                throw new MarkHabitException("Неверный выбор.");
            }

            Habit habit = habits.get(choice);
            habitCompletionService.markHabitCompleted(habit);
            System.out.println("Привычка отмечена как выполненная.");
        } catch (MarkHabitException e) {
            System.out.println(e.getMessage());
        }catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
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

            int choice = Integer.parseInt(inputReader.read("Введите номер привычки: ")) - 1;
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
            System.out.println(e.getMessage());
        }catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
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

            int choice = Integer.parseInt(inputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            String period = inputReader.read("Введите период (DAY/WEEK/MONTH): ");
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
        }catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
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

            int choice = Integer.parseInt(inputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            String period = inputReader.read("Введите период (DAY/WEEK/MONTH): ");
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
        }catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
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

            int choice = Integer.parseInt(inputReader.read("Введите номер привычки: ")) - 1;
            if (choice < 0 || choice >= habits.size()) {
                System.out.println("Неверный выбор.");
                return;
            }

            Habit habit = habits.get(choice);
            String message = inputReader.read("Введите сообщение для уведомления: ");
            notificationService.sendNotification(user, habit, message);
        } catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        }catch (Exception e) {
            System.out.println("Ошибка");
        }
    }
    public void viewFilteredHabits(User user) {
        try {
            checkUserBlocked(user);
            String startDateStr = inputReader.read("Введите дату начала отчета (гггг-мм-дд) или оставьте пустым: ");
            LocalDate startDate = startDateStr.isEmpty() ? null : LocalDate.parse(startDateStr);

            String endDateStr = inputReader.read("Введите дату конца отчета (гггг-мм-дд) или оставьте пустым: ");
            LocalDate endDate = endDateStr.isEmpty() ? null : LocalDate.parse(endDateStr);

            String completedStr = inputReader.read("Введите статус выполнения (true/false) или оставьте пустым: ");
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
            System.out.println(e.getMessage());
        } catch (BlockedUserException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка");
        }
    }
}
