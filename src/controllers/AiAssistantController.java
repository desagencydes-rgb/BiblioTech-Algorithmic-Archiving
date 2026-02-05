package controllers;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import services.AIService;
import services.ConfigService;

import java.net.URL;
import java.util.ResourceBundle;

public class AiAssistantController implements Initializable {

    @FXML
    private VBox chatContainer;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private TextArea inputField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addSystemMessage("Bonjour ! Je suis votre assistant de bibliothèque. Comment puis-je vous aider ?");

        // Auto-scroll to bottom
        chatContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            scrollPane.setVvalue(1.0);
        });
    }

    @FXML
    private void handleSendMessage() {
        String message = inputField.getText().trim();
        if (message.isEmpty())
            return;

        addUserMessage(message);
        inputField.clear();

        // Call AI Service
        AIService.sendMessage(message)
                .thenAccept(response -> {
                    Platform.runLater(() -> addAiMessage(response));
                })
                .exceptionally(ex -> {
                    Platform.runLater(() -> addSystemMessage("Erreur : " + ex.getMessage()));
                    return null;
                });
    }

    private void addUserMessage(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-background-color: #3498db; -fx-text-fill: white; -fx-padding: 10; -fx-background-radius: 10; -fx-max-width: 400;");
        label.setWrapText(true);

        VBox container = new VBox(label);
        container.setStyle("-fx-alignment: CENTER_RIGHT; -fx-padding: 5;");
        // Hack for alignment since VBox alignment property affects children not self in
        // parent
        // Actually we need the VBox to be wide and align the label inside right.
        // Or simpler: use HBox with spacer.

        chatContainer.getChildren().add(container);
    }

    private void addAiMessage(String text) {
        Label label = new Label(text);
        label.setStyle(
                "-fx-background-color: #ecf0f1; -fx-text-fill: black; -fx-padding: 10; -fx-background-radius: 10; -fx-max-width: 400;");
        label.setWrapText(true);

        VBox container = new VBox(label);
        container.setStyle("-fx-alignment: CENTER_LEFT; -fx-padding: 5;");

        chatContainer.getChildren().add(container);
    }

    private void addSystemMessage(String text) {
        Label label = new Label(text);
        label.setStyle("-fx-text-fill: #7f8c8d; -fx-font-style: italic; -fx-padding: 5;");
        label.setWrapText(true);

        VBox container = new VBox(label);
        container.setStyle("-fx-alignment: CENTER; -fx-padding: 5;");

        chatContainer.getChildren().add(container);
    }
}
