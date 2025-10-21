package me.code.services;

import me.code.models.Todo;
import me.code.models.TodoStatus;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Stream;

public class TodoService {

    private static List<Todo> todos = new ArrayList<>();

    public static void loadTodos(ArrayList<Todo> loadFrom) {
        todos.addAll(loadFrom);
    }

    public static void addTodo(Todo todo) throws Exception {
        todos.add(todo);
        TodoRepository.saveTodosToFile(todos);
    }

    public static Todo removeTodoById(UUID id) throws Exception {
        Optional<Todo> removedTodo = todos.stream()
                .filter((todo) -> todo.getId().equals(id))
                .findFirst();

        if (removedTodo.isEmpty()) {
            return null;
        }

        todos = todos.stream()
                .filter((todo) -> !todo.getId().equals(id))
                .toList();

        TodoRepository.deleteTodoFile(id);

        return removedTodo.get();
    }

    public static Todo updateTodoStatusById(UUID todoId, TodoStatus status) throws Exception {
        Todo todo = getTodoById(todoId);
        if (todo == null) {
            return null;
        } else {
            todo.setStatus(status);
            TodoRepository.saveTodosToFile(todos);
            return todo;
        }
    }

    public static Todo getTodoById(UUID id) {
        Optional<Todo> potentialTodo = todos.stream()
                .filter((todo) -> todo.getId().equals(id))
                .findFirst();

        return potentialTodo.orElse(null);
    }

    public static Stream<Todo> getTodos() {
        return todos.stream();
    }

    public static Stream<Todo> searchTodos(String query) {
        return todos.stream()
                .filter((todo) -> todo.getTitle().toLowerCase().contains(query.toLowerCase()))
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
    }
}
