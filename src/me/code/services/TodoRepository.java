package me.code.services;

import me.code.models.Todo;
import me.code.models.TodoStatus;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class TodoRepository {

    private static final String EXTENSION = ".txt";

    public static void saveTodosToFile(List<Todo> todos) throws Exception {
        for (Todo todo : todos) {
            saveTodoToFile(todo);
        }
    }

    public static ArrayList<Todo> loadTodosFromFile() {
        ArrayList<Todo> todos = new ArrayList<>();

        File directory = new File("./");
        File[] todoFiles = directory.listFiles();
        if (todoFiles == null) {
            return todos;
        }

        for (File todoFile : todoFiles) {
            String name = todoFile.getName();
            if (!name.endsWith(EXTENSION)) {
                continue;
            }

            String fileName = name.substring(0, name.length() - EXTENSION.length());
            UUID todoId;
            try {
                todoId = UUID.fromString(fileName);
            } catch (IllegalArgumentException ignored) {
                continue;
            }

            try {
                Todo todo = loadTodoFromFile(todoId);
                todos.add(todo);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return todos;
    }

    private static void saveTodoToFile(Todo todo) throws Exception {
        String fileName = getFileName(todo.getId());

        try (BufferedWriter stream = new BufferedWriter(new FileWriter(fileName))) {
            String priority = todo.getPriority() + "";
            String deadline = todo.getDeadline().getTime() + "";
            stream
                    .append(todo.getId().toString())
                    .append("\n")
                    .append(todo.getTitle())
                    .append("\n")
                    .append(todo.getCategory())
                    .append("\n")
                    .append(todo.getStatus().toString())
                    .append("\n")
                    .append(priority)
                    .append("\n")
                    .append(deadline);
        }
    }

    public static Todo loadTodoFromFile(UUID id) throws Exception {
        String fileName = getFileName(id);

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            reader.readLine(); // Skip id
            String title = reader.readLine();
            String category = reader.readLine();
            String statusString = reader.readLine();
            String priorityString = reader.readLine();
            String deadlineString = reader.readLine();

            TodoStatus status = TodoStatus.valueOf(statusString);
            int priority = Integer.parseInt(priorityString);
            long deadlineTime = Long.parseLong(deadlineString);
            Date deadline = new Date(deadlineTime);

            return new Todo(id, title, deadline, category, priority, status);
        }
    }

    public static void deleteTodoFile(UUID id) throws Exception {
        String fileName = getFileName(id);

        File file = new File(fileName);
        boolean ignored = file.delete();
    }

    private static String getFileName(UUID todoId) {
        return todoId.toString() + EXTENSION;
    }
}
