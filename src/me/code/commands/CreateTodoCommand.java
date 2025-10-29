package me.code.commands;

import me.code.models.Todo;
import me.code.services.DefaultTodoService;
import me.code.services.ITodoService;

import java.text.ParseException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

@CommandInfo(order = 1)
public class CreateTodoCommand extends Command {

    public CreateTodoCommand(ITodoService todoService) {
        super("create-todo", "Create and save a new todo", todoService);
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Create a todo.");
        System.out.print("Enter a title: ");
        String title = scanner.nextLine();

        String category = "";
        while (true) {
            System.out.print("Enter a category: ");
            category = scanner.nextLine();
            if (category.isBlank()) {
                System.out.println("Category may not be empty or blank.");
            } else {
                break;
            }
        }

        int priority = 0;
        while (true) {
            System.out.print("Enter a priority (higher number is higher priority): ");
            try {
                priority = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException ignored) {
                System.out.println("You must provide a valid number.");
            }
        }

        Date deadline;
        while (true) {
            System.out.print("Enter a deadline date (year-month-day): ");
            String deadlineDateString = scanner.nextLine();

            try {
                deadline = Todo.DATE_FORMAT.parse(deadlineDateString);
                break;
            } catch (ParseException ignored) {
                System.out.println("Invalid date format, try again.");
            }
        }

        Todo todo = new Todo(title, deadline, category, priority);
        try {
            todoService.createTodo(todo);
            System.out.println("Todo '" + title + "' has been created!");
        } catch (Exception exception) {
            System.out.println("An error occurred, message: " + exception.getMessage());
        }
    }
}
