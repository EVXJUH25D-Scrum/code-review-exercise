package me.code.commands;

import me.code.models.Todo;
import me.code.models.TodoStatus;
import me.code.services.DefaultTodoService;
import me.code.services.ITodoService;
import me.code.utility.CommandHelper;

import java.util.UUID;

@CommandInfo(order = 5)
public class CompleteTodoCommand extends Command {

    public CompleteTodoCommand(ITodoService todoService) {
        super("complete-todo", "Mark a todo as completed", todoService);
    }

    @Override
    public void execute() {
        UUID todoId = CommandHelper.queryTodoId();
        if (todoId == null) {
            return;
        }

        Todo todo;
        try {
            todo = todoService.updateTodoStatusById(todoId, TodoStatus.COMPLETED);
        } catch (Exception exception) {
            System.out.println("An error occurred, message: " + exception.getMessage());
            return;
        }

        if (todo != null) {
            System.out.println("Todo '" + todo.getTitle() + "' is now completed.");
        } else {
            System.out.println("No such todo was found.");
        }
    }
}
