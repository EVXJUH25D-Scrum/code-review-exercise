package me.code;

import me.code.commands.*;
import me.code.models.Todo;
import me.code.services.ICommandService;
import me.code.services.TerminalCommandService;
import me.code.services.TodoRepository;
import me.code.services.TodoService;

import java.util.ArrayList;

public class Main {

    /*

    1. Skapa todos (som sparas)
    2. Lista todos
    3. Avklara todos
    4. Radera todos
    5. Prioritera todos
    6. Uppdatera/redigera todos
    7. Kategorisera todos
    8. Statusmarkera todos (ej påbörjad, in progress, avslutad)

     */

    public static void main(String[] args) {
        ArrayList<Todo> todos = TodoRepository.loadTodosFromFile();
        TodoService.loadTodos(todos);

        ICommandService commandService = new TerminalCommandService();

        commandService.registerCommand(new ListTodosCommand());
        commandService.registerCommand(new CreateTodoCommand());
        commandService.registerCommand(new SearchTodosCommand());
        commandService.registerCommand(new CompleteTodoCommand());
        commandService.registerCommand(new DeleteTodoCommand());
        commandService.registerCommand(new StartTodoCommand());

        if (commandService instanceof TerminalCommandService service) {
            service.start();
        }
    }
}
