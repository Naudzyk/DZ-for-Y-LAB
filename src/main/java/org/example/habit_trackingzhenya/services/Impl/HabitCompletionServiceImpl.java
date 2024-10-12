package org.example.habit_trackingzhenya.services.Impl;

import lombok.RequiredArgsConstructor;
import org.example.habit_trackingzhenya.models.Habit;
import org.example.habit_trackingzhenya.models.HabitCompletion;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.services.HabitCompletionService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
@RequiredArgsConstructor
public class HabitCompletionServiceImpl implements HabitCompletionService {

    private HabitCompletionRepository habitCompletionRepository;

    public HabitCompletionServiceImpl(HabitCompletionRepository completionRepository) {
        this.habitCompletionRepository = completionRepository;
    }

    @Override
    public void markHabitCompleted(Habit habit) {
         habitCompletionRepository.addCompletion(habit, LocalDate.now());
    }

    @Override
    public List<HabitCompletion> getCompletionsForHabit(Habit habit) {
        return habitCompletionRepository.getCompletionsForHabit(habit);
    }

    @Override
    public List<HabitCompletion> getCompletionsForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate) {
        return habitCompletionRepository.getCompletionsForHabitInPeriod(habit, startDate, endDate);
    }

    @Override
    public int getCompletionCountForHabitInPeriod(Habit habit, LocalDate startDate, LocalDate endDate) {
        return getCompletionsForHabitInPeriod(habit, startDate, endDate).size();
    }

    @Override
    public int getCurrentStreak(Habit habit) {
        List<HabitCompletion> completions = getCompletionsForHabit(habit);
        if (completions.isEmpty()) {
            return 0;
        }

        completions.sort((c1, c2) -> c2.getCompletionDate().compareTo(c1.getCompletionDate()));

        int streak = 0;
        LocalDate lastDate = completions.get(0).getCompletionDate();

        for (HabitCompletion completion : completions) {
            if (completion.getCompletionDate().isEqual(lastDate.minusDays(streak))) {
                streak++;
            } else {
                break;
            }
        }

        return streak;
    }

    @Override
    public double getCompletionPercentageForPeriod(Habit habit, LocalDate startDate, LocalDate endDate) {
        long totalDays = ChronoUnit.DAYS.between(startDate, endDate) + 1;
        int completionCount = getCompletionCountForHabitInPeriod(habit, startDate, endDate);
        return (double) completionCount / totalDays * 100;
    }

    @Override
    public String generateProgressReport(Habit habit, LocalDate startDate, LocalDate endDate) {
        int completionCount = getCompletionCountForHabitInPeriod(habit, startDate, endDate);
        int currentStreak = getCurrentStreak(habit);
        double completionPercentage = getCompletionPercentageForPeriod(habit, startDate, endDate);

        return "Отчет по прогрессу выполнения привычки " + habit.getName() + " за период с " +
                startDate + " по " + endDate + ":\n" +
                "Количество выполнений: " + completionCount + "\n" +
                "Текущая серия выполнений: " + currentStreak + "\n" +
                "Процент успешного выполнения: " + completionPercentage + "%";
    }
}
