package me.code.commands;

import me.code.models.Todo;
import me.code.services.TodoService;

import java.util.ArrayList;
import java.util.stream.Stream;

public class ListTodosCommand extends Command {

    public ListTodosCommand() {
        super("list-todos", "List all created todos");
    }

    @Override
    public void execute() {
        Stream<Todo> todos = TodoService.getTodos();

        System.out.println("Created todos:");
        todos.forEach(todo -> {
            System.out.println(" - " + todo.toString());
        });
    }
}
