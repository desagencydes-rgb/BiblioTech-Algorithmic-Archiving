package services;

import models.Activity;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ActivityService {
    private static final ObservableList<Activity> activities = FXCollections.observableArrayList();
    private static final String FILE_PATH = "data/activities.csv";
    private static final DateTimeFormatter FILE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private static final int MAX_ACTIVITIES = 50;

    static {
        loadData();
    }

    public ActivityService() {
        // Empty constructor
    }

    public static void addActivity(String description) {
        Activity activity = new Activity(description, LocalDateTime.now());
        activities.add(0, activity); // Add to top

        // Keep only last MAX_ACTIVITIES
        if (activities.size() > MAX_ACTIVITIES) {
            activities.remove(MAX_ACTIVITIES, activities.size());
        }

        saveData();
    }

    public ObservableList<Activity> getActivities() {
        return activities;
    }

    private static void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            activities.clear();
            List<Activity> loadedActivities = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";", 2); // Split into date and description
                if (parts.length >= 2) {
                    try {
                        LocalDateTime timestamp = LocalDateTime.parse(parts[0], FILE_FORMATTER);
                        String description = parts[1];
                        loadedActivities.add(new Activity(description, timestamp));
                    } catch (Exception e) {
                        System.err.println("Skipping malformed activity line: " + line);
                    }
                }
            }
            // Add all at once to avoid triggering listeners unnecessarily or multiple saves
            activities.addAll(loadedActivities);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveData() {
        File file = new File(FILE_PATH);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Activity activity : activities) {
                writer.write(activity.getTimestamp().format(FILE_FORMATTER) + ";" + activity.getDescription());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
