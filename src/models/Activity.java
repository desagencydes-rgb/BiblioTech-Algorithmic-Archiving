package models;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Activity {
    private String description;
    private LocalDateTime timestamp;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public Activity(String description, LocalDateTime timestamp) {
        this.description = description;
        this.timestamp = timestamp;
    }

    public Activity(String description) {
        this(description, LocalDateTime.now());
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getFormattedTimestamp() {
        return timestamp.format(formatter);
    }

    @Override
    public String toString() {
        return description + " (" + getFormattedTimestamp() + ")";
    }
}
