package me.code.commands;

import me.code.models.Todo;
import me.code.services.TodoService;

import java.util.Scanner;
import java.util.stream.Stream;

public class SearchTodosCommand extends Command {

    public SearchTodosCommand() {
        super("search-todos", "Search for todos");
    }

    @Override
    public void execute() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Search for todos.");
        System.out.print("Enter a search query: ");
        String query = scanner.nextLine();

        Stream<Todo> stream = TodoService.searchTodos(query);
        stream.forEach(todo -> {
            System.out.println(" - " + todo.toString());
        });
    }
}
