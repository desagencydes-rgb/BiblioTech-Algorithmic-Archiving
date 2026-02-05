package services;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.DoubleProperty;

import java.io.*;
import java.util.Properties;

public class ConfigService {
    private static final String FILE_PATH = "data/config.properties";

    private static final StringProperty appTitle = new SimpleStringProperty("Gestion de Bibliothèque");
    private static final IntegerProperty loanDays = new SimpleIntegerProperty(14);
    private static final DoubleProperty finePerDay = new SimpleDoubleProperty(0.50);
    private static final StringProperty contactEmail = new SimpleStringProperty("contact@library.com");
    private static final StringProperty aiApiKey = new SimpleStringProperty("");
    private static final StringProperty aiModel = new SimpleStringProperty("gpt-3.5-turbo");
    private static final StringProperty aiSystemPrompt = new SimpleStringProperty(
            "You are a helpful library assistant.");
    private static final IntegerProperty aiMaxTokens = new SimpleIntegerProperty(150);

    static {
        loadConfig();
    }

    private ConfigService() {
        // Private constructor
    }

    // ... Existing getters/setters ...
    public static StringProperty appTitleProperty() {
        return appTitle;
    }

    public static String getAppTitle() {
        return appTitle.get();
    }

    public static void setAppTitle(String title) {
        appTitle.set(title);
        saveConfig();
    }

    public static IntegerProperty loanDaysProperty() {
        return loanDays;
    }

    public static int getLoanDays() {
        return loanDays.get();
    }

    public static void setLoanDays(int days) {
        loanDays.set(days);
        saveConfig();
    }

    public static DoubleProperty finePerDayProperty() {
        return finePerDay;
    }

    public static double getFinePerDay() {
        return finePerDay.get();
    }

    public static void setFinePerDay(double fine) {
        finePerDay.set(fine);
        saveConfig();
    }

    public static StringProperty contactEmailProperty() {
        return contactEmail;
    }

    public static String getContactEmail() {
        return contactEmail.get();
    }

    public static void setContactEmail(String email) {
        contactEmail.set(email);
        saveConfig();
    }

    // AI Getters/Setters
    public static StringProperty aiApiKeyProperty() {
        return aiApiKey;
    }

    public static String getAiApiKey() {
        return aiApiKey.get();
    }

    public static void setAiApiKey(String key) {
        aiApiKey.set(key);
        saveConfig();
    }

    public static StringProperty aiModelProperty() {
        return aiModel;
    }

    public static String getAiModel() {
        return aiModel.get();
    }

    public static void setAiModel(String model) {
        aiModel.set(model);
        saveConfig();
    }

    public static StringProperty aiSystemPromptProperty() {
        return aiSystemPrompt;
    }

    public static String getAiSystemPrompt() {
        return aiSystemPrompt.get();
    }

    public static void setAiSystemPrompt(String prompt) {
        aiSystemPrompt.set(prompt);
        saveConfig();
    }

    public static IntegerProperty aiMaxTokensProperty() {
        return aiMaxTokens;
    }

    public static int getAiMaxTokens() {
        return aiMaxTokens.get();
    }

    public static void setAiMaxTokens(int tokens) {
        aiMaxTokens.set(tokens);
        saveConfig();
    }

    private static void loadConfig() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        Properties props = new Properties();
        try (InputStream input = new FileInputStream(file)) {
            props.load(input);
            appTitle.set(props.getProperty("appTitle", "Gestion de Bibliothèque"));
            loanDays.set(Integer.parseInt(props.getProperty("loanDays", "14")));
            finePerDay.set(Double.parseDouble(props.getProperty("finePerDay", "0.50")));
            contactEmail.set(props.getProperty("contactEmail", "contact@library.com"));

            // Load AI Config
            aiApiKey.set(props.getProperty("aiApiKey", ""));
            aiModel.set(props.getProperty("aiModel", "gpt-3.5-turbo"));
            aiSystemPrompt.set(props.getProperty("aiSystemPrompt", "You are a helpful library assistant."));
            aiMaxTokens.set(Integer.parseInt(props.getProperty("aiMaxTokens", "150")));

        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    public static void saveConfig() {
        File file = new File(FILE_PATH);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        Properties props = new Properties();
        props.setProperty("appTitle", appTitle.get());
        props.setProperty("loanDays", String.valueOf(loanDays.get()));
        props.setProperty("finePerDay", String.valueOf(finePerDay.get()));
        props.setProperty("contactEmail", contactEmail.get());

        // Save AI Config
        props.setProperty("aiApiKey", aiApiKey.get());
        props.setProperty("aiModel", aiModel.get());
        props.setProperty("aiSystemPrompt", aiSystemPrompt.get());
        props.setProperty("aiMaxTokens", String.valueOf(aiMaxTokens.get()));

        try (OutputStream output = new FileOutputStream(file)) {
            props.store(output, "Library Manager Configuration");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
