package org.example.habit_trackingzhenya;

import org.example.habit_trackingzhenya.controller.AdminController;
import org.example.habit_trackingzhenya.controller.HabitController;
import org.example.habit_trackingzhenya.controller.UserController;
import org.example.habit_trackingzhenya.models.Role;
import org.example.habit_trackingzhenya.models.User;
import org.example.habit_trackingzhenya.repositories.HabitCompletionRepository;
import org.example.habit_trackingzhenya.repositories.HabitRepository;
import org.example.habit_trackingzhenya.repositories.Impl.HabitCompletionRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.Impl.HabitRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.Impl.NotificationRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.Impl.UserRepositoryImpl;
import org.example.habit_trackingzhenya.repositories.NotificationRepository;
import org.example.habit_trackingzhenya.repositories.UserRepository;
import org.example.habit_trackingzhenya.services.Impl.*;
import org.example.habit_trackingzhenya.utils.ConsoleInputReader;
import org.example.habit_trackingzhenya.utils.ConsoleReader;
public class HelloApplication {
    private UserController userController;
    private HabitController habitController;
    private AdminController adminController;
    private User currentUser;
    private ConsoleInputReader consoleinputReader;

    public HelloApplication() {
        UserRepository userRepository = new UserRepositoryImpl();
        HabitRepository habitRepository = new HabitRepositoryImpl();
        HabitCompletionRepository completionRepository = new HabitCompletionRepositoryImpl();
        UserServiceImpl userService = new UserServiceImpl(userRepository);
        HabitServiceImpl habitService = new HabitServiceImpl(habitRepository,completionRepository);
        HabitCompletionServiceImpl completionService = new HabitCompletionServiceImpl(completionRepository);
        NotificationRepository notificationRepository = new NotificationRepositoryImpl();
        NotificationServiceImpl notificationService = new NotificationServiceImpl(notificationRepository);
        AdminServiceImpl adminServices = new AdminServiceImpl(habitService,userService);

        this.consoleinputReader= new ConsoleReader();


        this.userController = new UserController(userService,consoleinputReader);
        this.habitController = new HabitController(habitService, completionService,notificationService,consoleinputReader);
        this.adminController = new AdminController(adminServices,consoleinputReader);

    }

    public void start() {
        boolean running = true;

        while (running) {
            if (currentUser == null) {
                showMainMenu();
                int choice = getUserChoice(3);
                switch (choice) {
                    case 1:
                        userController.registerUser();
                        break;
                    case 2:
                        currentUser = userController.loginUser();
                        break;
                    case 3:
                        running = false;
                        break;
                    default:
                        System.out.println("Неверный выбор");
                }
            } else {
                if (currentUser.getRole() == Role.ADMIN) {
                    showAdminMenu();
                    int choice = getUserChoice(6);
                    handleAdminChoice(choice);
                } else {
                    showUserMenu();
                    int choice = getUserChoice(12);
                    handleUserChoice(choice);
                }
            }
        }
    }

    private void showMainMenu() {
        System.out.println("1. Регистрация");
        System.out.println("2. Вход");
        System.out.println("3. Выход");
    }

    private void showAdminMenu() {
        System.out.println("1. Просмотреть всех пользователей");
        System.out.println("2. Просмотреть все привычки");
        System.out.println("3. Заблокировать пользователя");
        System.out.println("4. Разблокировать пользователя");
        System.out.println("5. Удалить пользователя");
        System.out.println("6. Выход");
    }

    private void showUserMenu() {
        System.out.println("1. Редактировать профиль");
        System.out.println("2. Удалить аккаунт");
        System.out.println("3. Создать привычку");
        System.out.println("4. Редактировать привычку");
        System.out.println("5. Удалить привычку");
        System.out.println("6. Просмотреть привычки");
        System.out.println("7. Отметить выполнение привычки");
        System.out.println("8. Просмотреть историю выполнения привычки");
        System.out.println("9. Просмотреть статистику по привычке");
        System.out.println("10. Сформировать отчет по прогрессу выполнения привычки");
        System.out.println("11. Отправить уведомление");
        System.out.println("12. Фильтр привычек");
        System.out.println("13. Выход");
    }

    private int getUserChoice(int maxOption) {
        int choice = -1;
        while (choice < 1 || choice > maxOption) {
            try {
                choice = Integer.parseInt(consoleinputReader.read("Введите ваш выбор: "));
                if (choice < 1 || choice > maxOption) {
                    System.out.println("Неверный выбор, попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Вводите только числа.");
            }
        }
        return choice;
    }

    private void handleAdminChoice(int choice) {
        switch (choice) {
            case 1:
                adminController.viewAllUsers();
                break;
            case 2:
                adminController.viewAllHabits();
                break;
            case 3:
                adminController.blockUser();
                break;
            case 4:
                adminController.unblockUser();
                break;
            case 5:
                adminController.deleteUser();
                break;
            case 6:
                currentUser = null;
                break;
            default:
                System.out.println("Неверный выбор");
        }
    }
    private void handleUserChoice ( int choice){
        switch (choice) {
            case 1:
                userController.editProfile(currentUser.getEmail());
                break;
                case 2:
                    userController.deleteUser(currentUser.getEmail());
                    currentUser = null;
                    break;
                case 3:
                    habitController.createHabit(currentUser);
                    break;
                case 4:
                    habitController.updateHabit(currentUser);
                    break;
                case 5:
                    habitController.deleteHabit(currentUser);
                    break;
                case 6:
                    habitController.viewHabits(currentUser);
                    break;
                case 7:
                    habitController.markHabitCompleted(currentUser);
                    break;
                case 8:
                    habitController.viewHabitCompletions(currentUser);
                    break;
                case 9:
                    habitController.viewHabitStatistics(currentUser);
                    break;
                case 10:
                    habitController.generateProgressReport(currentUser);
                    break;
                case 11:
                    habitController.sendNotification(currentUser);
                    break;
                case 12:
                    habitController.viewFilteredHabits(currentUser);
                    break;
                case 13:
                    currentUser = null;
                    break;
                default:
                    System.out.println("Неверный выбор");
            }
        }


    public static void main(String[] args) {
        HelloApplication app = new HelloApplication();
        app.start();
    }
}