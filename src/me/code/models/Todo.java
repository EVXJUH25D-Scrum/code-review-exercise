package me.code.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class Todo {

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final UUID id;
    private String title;
    private Date deadline;
    private String category;
    private TodoStatus status;
    private int priority;

    public Todo(String title, Date deadline, String category, int priority) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.deadline = deadline;
        this.category = category;
        this.priority = priority;
        this.status = TodoStatus.PENDING;
    }

    public Todo(UUID id, String title, Date deadline, String category, int priority, TodoStatus status) {
        this.id = id;
        this.title = title;
        this.deadline = deadline;
        this.category = category;
        this.priority = priority;
        this.status = status;
    }

    @Override
    public String toString() {
        return this.title + "\n    " +
                "Id: " + this.id + "\n    " + 
                "Status: " + this.status.getDisplayName() + "\n    " +
                "Category: " + this.category + "\n    " +
                "Deadline: " + DATE_FORMAT.format(this.deadline) + "\n    " +
                "Priority: " + this.priority;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getDeadline() {
        return deadline;
    }

    public void setDeadline(Date deadline) {
        this.deadline = deadline;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public TodoStatus getStatus() {
        return status;
    }

    public void setStatus(TodoStatus status) {
        this.status = status;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public UUID getId() {
        return id;
    }
}
