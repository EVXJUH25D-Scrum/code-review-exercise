package me.code.commands;

import me.code.models.Todo;
import me.code.services.DefaultTodoService;
import me.code.services.ITodoService;
import me.code.utility.CommandHelper;

import java.util.UUID;

@CommandInfo(order = 6)
public class DeleteTodoCommand extends Command {

    public DeleteTodoCommand(ITodoService todoService) {
        super("delete-todo", "Remove todos", todoService);
    }

    @Override
    public void execute() {
        UUID todoId = CommandHelper.queryTodoId();
        if (todoId == null) {
            return;
        }

        Todo todo;
        try {
            todo = todoService.deleteTodoById(todoId);
        } catch (Exception exception) {
            System.out.println("An error occurred, message: " + exception.getMessage());
            return;
        }

        if (todo == null) {
            System.out.println("No such todo was found.");
        } else {
            System.out.println("Deleted todo with title '" + todo.getTitle() + "'");
        }
    }
}
