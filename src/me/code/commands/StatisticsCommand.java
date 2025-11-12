package me.code.commands;

import me.code.models.TodoStatus;
import me.code.services.ITodoService;

import java.util.List;

@CommandInfo(order = 7)
public class StatisticsCommand extends Command {

    public StatisticsCommand(ITodoService todoService) {
        super("statistics", "Display statistics about todos", todoService);
    }

    @Override
    public void execute() {
        try {
            // Räkna todos per status
            int pendingCount = todoService.countTodosByStatus(TodoStatus.PENDING);
            int inProgressCount = todoService.countTodosByStatus(TodoStatus.IN_PROGRESS);
            int completedCount = todoService.countTodosByStatus(TodoStatus.COMPLETED);
            int totalCount = pendingCount + inProgressCount + completedCount;

            // Hämta kategorier
            List<String> categories = todoService.getAllCategories();

            // Skriv ut statistik
            System.out.println("=== Todo Statistics ===");
            System.out.println("Total todos: " + totalCount);
            System.out.println("Pending: " + pendingCount);
            System.out.println("In Progress: " + inProgressCount);
            System.out.println("Completed: " + completedCount);
            System.out.println();
            System.out.println("Categories (" + categories.size() + "):");
            for (String category : categories) {
                System.out.println(" - " + category);
            }

            // Beräkna completion rate
            if (totalCount > 0) {
                double completionRate = (completedCount * 100.0) / totalCount;
                System.out.println();
                System.out.println("Completion rate: " + String.format("%.1f", completionRate) + "%");
            }

        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Something went wrong!");
        }
    }
}
