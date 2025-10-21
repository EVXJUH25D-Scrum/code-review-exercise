package me.code.commands;

public abstract class Command {

    protected final String name;
    protected final String description;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public abstract void execute();

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return name + " - " + description;
    }
}
