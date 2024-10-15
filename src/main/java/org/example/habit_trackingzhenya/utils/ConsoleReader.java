package org.example.habit_trackingzhenya.utils;

import java.util.Scanner;

public class ConsoleReader implements ConsoleInputReader {
    private static final Scanner scanner = new Scanner(System.in);

    @Override
    public String read(String value) {
        System.out.print(value);
        return scanner.nextLine();
    }
}
