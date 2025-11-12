package me.code.services;

import me.code.models.Todo;
import me.code.models.TodoStatus;
import me.code.repositories.ITodoRepository;

import java.util.*;
import java.util.stream.Stream;

/**
 * Standardimplementation av ITodoService som innehåller affärslogiken för todos.
 *
 * Service-lagret är hjärnan i applikationen! Här hanterar vi all logik kring
 * hur todos ska skapas, uppdateras, raderas och sökas.
 *
 * Vi följer "Single Responsibility Principle" - denna klass har ett enda ansvar:
 * att hantera todo-relaterad affärslogik. Den vet inget om FIL-hantering
 * (det sköter repository) eller UI (det sköter commands).
 */
public class DefaultTodoService implements ITodoService {

    // Dependency - vi är beroende av ett repository för att spara/hämta data
    private final ITodoRepository todoRepository;

    /**
     * Konstruktor med Dependency Injection.
     *
     * Istället för att skapa ett repository själv (new FileTodoRepository())
     * får vi det skickat in utifrån. Detta gör koden lättare att testa och
     * mer flexibel - vi kan byta till en annan repository-implementation
     * utan att ändra denna klass!
     *
     * @param todoRepository Repository som ska användas för datalagring
     */
    public DefaultTodoService(ITodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    /**
     * Skapar och sparar en ny todo.
     *
     * Enkel delegering till repository - vi lägger ingen extra logik här,
     * men vi SKULLE kunna lägga till validering (t.ex. att titel inte är tom).
     */
    @Override
    public void createTodo(Todo todo) throws Exception {
        todoRepository.save(todo);
    }

    /**
     * Raderar en todo och returnerar den raderade todon.
     *
     * Vi hämtar först todon så vi kan returnera den (användaren vill se vad som raderades),
     * sedan raderar vi den från repository.
     */
    @Override
    public Todo deleteTodoById(UUID id) throws Exception {
        Todo todo = todoRepository.findById(id);
        todoRepository.delete(id);
        return todo;
    }

    /**
     * Uppdaterar statusen på en befintlig todo.
     *
     * Vi hämtar todon, ändrar dess status och sparar den igen.
     * Detta är ett exempel på "read-modify-write"-mönstret.
     *
     * @return Den uppdaterade todon, eller null om den inte hittades
     */
    @Override
    public Todo updateTodoStatusById(UUID todoId, TodoStatus status) throws Exception {
        Todo todo = getTodoById(todoId);
        if (todo == null) {
            return null;  // Todon fanns inte
        } else {
            todo.setStatus(status);      // Uppdatera status
            todoRepository.save(todo);   // Spara tillbaka
            return todo;
        }
    }

    /**
     * Hämtar en specifik todo baserat på ID.
     *
     * Direkt delegering till repository.
     */
    @Override
    public Todo getTodoById(UUID id) throws Exception {
        return todoRepository.findById(id);
    }

    /**
     * Hämtar alla todos som en Stream.
     *
     * Vi konverterar listan från repository till en Stream för att
     * ge användaren möjlighet att använda Stream API:et (filter, map, etc).
     */
    @Override
    public Stream<Todo> getTodos() throws Exception {
        return todoRepository.findAll().stream();
    }

    /**
     * Söker efter todos vars titel innehåller söktermen.
     *
     * Detta är ett exempel på hur bra Stream API:et är!
     * Vi kedjar ihop operationer:
     * 1. Hämta alla todos
     * 2. Filtrera ut de som innehåller söktermen (case-insensitive)
     * 3. Sortera alfabetiskt
     *
     * Allt på ett funktionellt och läsbart sätt!
     *
     * @param query Söktermen (case-insensitive)
     * @return Stream med matchande todos, sorterade alfabetiskt
     */
    @Override
    public Stream<Todo> searchTodos(String query) throws Exception {
        return getTodos()
                // Filtrera: behåll bara todos vars titel innehåller söktermen
                .filter((todo) -> todo.getTitle().toLowerCase().contains(query.toLowerCase()))
                // Sortera alfabetiskt (case-insensitive)
                .sorted((a, b) -> a.getTitle().compareToIgnoreCase(b.getTitle()));
    }

    /**
     * Räknar antalet todos för en specifik status.
     *
     * Vi använder Stream API:et för att filtrera och räkna todos.
     */
    @Override
    public int countTodosByStatus(TodoStatus status) throws Exception {
        return (int) getTodos()
                .filter(todo -> todo.getStatus() == status)
                .count();
    }

    /**
     * Hämtar alla unika kategorier som finns bland todos.
     *
     * Vi använder Stream API:et för att extrahera alla kategorier och
     * sedan filtrera bort duplicates.
     */
    @Override
    public List<String> getAllCategories() throws Exception {
        return getTodos()
                .map(todo -> todo.getCategory())
                .distinct()
                .sorted()
                .toList();
    }
}
