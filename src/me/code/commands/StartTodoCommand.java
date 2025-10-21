package me.code.commands;

import me.code.models.Todo;
import me.code.models.TodoStatus;
import me.code.services.TodoService;
import me.code.utility.CommandHelper;

import java.util.UUID;

public class StartTodoCommand extends Command {

    public StartTodoCommand() {
        super("start-todo", "Mark a todo as in-progress");
    }

    @Override
    public void execute() {
        UUID todoId = CommandHelper.queryTodoId();
        if (todoId == null) {
            return;
        }
        Todo todo;
        try {
            todo = TodoService.updateTodoStatusById(todoId, TodoStatus.IN_PROGRESS);
        } catch (Exception exception) {
            System.out.println("An error occurred, message: " + exception.getMessage());
            return;
        }

        if (todo != null) {
            System.out.println("Todo '" + todo.getTitle() + "' is now in-progress.");
        } else {
            System.out.println("No such todo was found.");
        }
    }
}
