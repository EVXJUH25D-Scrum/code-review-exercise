package me.code.commands;

import me.code.models.Todo;
import me.code.services.DefaultTodoService;
import me.code.services.ITodoService;

import java.util.Scanner;
import java.util.stream.Stream;

@CommandInfo(order = 3)
public class SearchTodosCommand extends Command {

    public SearchTodosCommand(ITodoService todoService) {
        super("search-todos", "Search for todos", todoService);
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Search for todos.");
        System.out.print("Enter a search query: ");
        String query = scanner.nextLine();

        Stream<Todo> stream;
        try {
            stream = todoService.searchTodos(query);
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Something went wrong!");
            return;
        }

        stream.forEach(todo -> {
            System.out.println(" - " + todo.toString());
        });
    }
}
