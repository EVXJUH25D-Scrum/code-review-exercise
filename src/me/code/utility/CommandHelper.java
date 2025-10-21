package me.code.utility;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.UUID;

public class CommandHelper {

    public static UUID queryTodoId() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter ID of todo: ");

        try {
            String id = scanner.nextLine();
            return UUID.fromString(id);
        } catch (IllegalArgumentException exception) {
            System.out.println("The id must be a valid UUID.");
            return null;
        }
    }

}
