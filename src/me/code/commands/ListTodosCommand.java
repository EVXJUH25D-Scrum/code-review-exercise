package me.code.commands;

import me.code.models.Todo;
import me.code.services.DefaultTodoService;
import me.code.services.ITodoService;

import java.util.stream.Stream;

@CommandInfo(order = 2)
public class ListTodosCommand extends Command {

    public ListTodosCommand(ITodoService todoService) {
        super("list-todos", "List all created todos", todoService);
    }

    @Override
    public void execute() {
        Stream<Todo> todos;
        try {
            todos = todoService.getTodos();
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Something went wrong!");
            return;
        }

        System.out.println("Created todos:");
        todos.forEach(todo -> {
            System.out.println(" - " + todo.toString());
        });
    }
}
