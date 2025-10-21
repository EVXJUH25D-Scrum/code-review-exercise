package me.code.models;

public enum TodoStatus {
    PENDING("pending"),
    IN_PROGRESS("in-progress"),
    COMPLETED("completed");

    private final String displayName;

    TodoStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
