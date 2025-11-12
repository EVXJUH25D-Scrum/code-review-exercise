package me.code;

import com.sun.jdi.event.ExceptionEvent;
import me.code.commands.*;
import me.code.models.Todo;
import me.code.repositories.FileTodoRepository;
import me.code.services.ICommandService;
import me.code.services.ITodoService;
import me.code.services.TerminalCommandService;
import me.code.services.DefaultTodoService;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    // LÅTSAS ÄNDRING!
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
        ICommandService commandService = new TerminalCommandService();
        ITodoService todoService = new DefaultTodoService(
                new FileTodoRepository()
        );

        try {
            List<Command> commands = getApplicationCommands(todoService);
            for (Command command : commands) {
                commandService.registerCommand(command);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            System.out.println("Something went wrong! Contact an administrator.");
            return;
        }

        if (commandService instanceof TerminalCommandService service) {
            service.start();
        }
    }

    private static List<Command> getApplicationCommands(ITodoService todoService) throws IOException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        List<Class<?>> commandClasses = Arrays.stream(getClasses("me.code.commands"))
                .filter(clazz -> clazz.getSuperclass() == Command.class)
                .sorted((classA, classB) -> {
                    boolean aHasAnnotation = classA.isAnnotationPresent(CommandInfo.class);
                    boolean bHasAnnotation = classB.isAnnotationPresent(CommandInfo.class);
                    if (!aHasAnnotation && !bHasAnnotation) {
                        return 0;
                    } else if (aHasAnnotation && !bHasAnnotation) {
                        return 1;
                    } else if (!aHasAnnotation && bHasAnnotation) {
                        return -1;
                    }

                    CommandInfo infoA = classA.getAnnotation(CommandInfo.class);
                    CommandInfo infoB = classB.getAnnotation(CommandInfo.class);

                    return Integer.compare(infoA.order(), infoB.order());
                }).toList();

        List<Command> commands = new ArrayList<>();
        for (Class<?> clazz : commandClasses) {
            Constructor<?> constructor = clazz.getConstructor(ITodoService.class);
            Command command = (Command) constructor.newInstance(todoService);
            commands.add(command);
        }

        return commands;
    }

    private static Class<?>[] getClasses(String packageName)
            throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        assert classLoader != null;
        String path = packageName.replace('.', '/');
        Enumeration<URL> resources = classLoader.getResources(path);
        List<File> dirs = new ArrayList<>();
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            dirs.add(new File(resource.getFile()));
        }
        ArrayList<Class<?>> classes = new ArrayList<>();
        for (File directory : dirs) {
            classes.addAll(findClasses(directory, packageName));
        }
        return classes.toArray(new Class[classes.size()]);
    }

    /**
     * Recursive method used to find all classes in a given directory and subdirs.
     *
     * @param directory   The base directory
     * @param packageName The package name for classes found inside the base directory
     * @return The classes
     */
    private static List<Class<?>> findClasses(File directory, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null) {
            return classes;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                assert !file.getName().contains(".");
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' + file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}
