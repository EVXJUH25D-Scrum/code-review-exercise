package me.code.services;

import me.code.models.Todo;
import me.code.models.TodoStatus;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Interface som definierar affärslogiken för todo-hantering.
 *
 * Service-lagret sitter mellan användargränssnittet (commands) och datalagret (repository).
 * Här lägger vi all "affärslogik" - regler för hur todos ska hanteras, filtrering,
 * sökning, etc.
 *
 * Vi använder interface här också för att kunna byta implementation om vi vill
 * (t.ex. en MockTodoService för testning).
 */
public interface ITodoService {

    /**
     * Skapar och sparar en ny todo.
     *
     * @param todo Den nya todon som ska skapas
     * @throws Exception Om något går fel vid sparning
     */
    void createTodo(Todo todo) throws Exception;

    /**
     * Raderar en todo och returnerar den raderade todon.
     *
     * Vi returnerar den raderade todon så användaren kan se vad som togs bort!
     *
     * @param id ID för todon som ska raderas
     * @return Den raderade todon
     * @throws Exception Om todon inte finns eller om något går fel
     */
    Todo deleteTodoById(UUID id) throws Exception;

    /**
     * Uppdaterar statusen på en todo (t.ex. från PENDING till IN_PROGRESS).
     *
     * @param todoId ID för todon som ska uppdateras
     * @param status Ny status
     * @return Den uppdaterade todon (eller null om den inte hittades)
     * @throws Exception Om något går fel vid uppdatering
     */
    Todo updateTodoStatusById(UUID todoId, TodoStatus status) throws Exception;

    /**
     * Hämtar en specifik todo baserat på ID.
     *
     * @param id ID för todon vi söker efter
     * @return Den hittade todon
     * @throws Exception Om todon inte finns
     */
    Todo getTodoById(UUID id) throws Exception;

    /**
     * Hämtar alla todos som en Stream.
     *
     * Vi använder Stream istället för List eftersom det ger oss möjlighet att
     * kedja ihop operationer som filter, map, sorted etc. på ett effektivt sätt!
     *
     * @return En Stream med alla todos
     * @throws Exception Om något går fel vid hämtning
     */
    Stream<Todo> getTodos() throws Exception;

    /**
     * Söker efter todos vars titel innehåller söktermen.
     *
     * Sökningen är case-insensitive (bryr sig inte om stora/små bokstäver)
     * och resultatet returneras sorterat alfabetiskt.
     *
     * @param query Sökterm att leta efter i titlar
     * @return En Stream med matchande todos, sorterade alfabetiskt
     * @throws Exception Om något går fel vid sökning
     */
    Stream<Todo> searchTodos(String query) throws Exception;

    /**
     * Räknar antalet todos för en specifik status.
     *
     * @param status Status att räkna (PENDING, IN_PROGRESS, eller COMPLETED)
     * @return Antal todos med given status
     * @throws Exception Om något går fel vid räkning
     */
    int countTodosByStatus(TodoStatus status) throws Exception;

    /**
     * Hämtar alla unika kategorier som finns bland todos.
     *
     * @return En lista med unika kategorinamn
     * @throws Exception Om något går fel vid hämtning
     */
    List<String> getAllCategories() throws Exception;
}
