package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import services.ConfigService;

import java.net.URL;
import java.util.ResourceBundle;

public class SettingsController implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private TextField emailField;
    @FXML
    private Spinner<Integer> loanDaysSpinner;
    @FXML
    private TextField fineField;
    @FXML
    private Label statusLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Initialize fields with current config values
        titleField.setText(ConfigService.getAppTitle());
        emailField.setText(ConfigService.getContactEmail());

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 365,
                ConfigService.getLoanDays());
        loanDaysSpinner.setValueFactory(valueFactory);

        fineField.setText(String.valueOf(ConfigService.getFinePerDay()));

        // Initialize AI Fields
        aiApiKeyField.setText(ConfigService.getAiApiKey());
        aiModelField.setText(ConfigService.getAiModel());
        aiSystemPromptField.setText(ConfigService.getAiSystemPrompt());

        SpinnerValueFactory<Integer> aiTokenFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 4000,
                ConfigService.getAiMaxTokens());
        aiMaxTokensSpinner.setValueFactory(aiTokenFactory);
    }

    @FXML
    private void handleSave() {
        try {
            ConfigService.setAppTitle(titleField.getText());
            ConfigService.setContactEmail(emailField.getText());
            ConfigService.setLoanDays(loanDaysSpinner.getValue());
            ConfigService.setFinePerDay(Double.parseDouble(fineField.getText())); // Parse manually to be safe or bind

            // Ideally separate save for general vs AI, but one ConfigService save call
            // saves all for now.
            // But we have separate buttons.

            statusLabel.setText("Paramètres généraux enregistrés !");
            statusLabel.setStyle("-fx-text-fill: green;");
        } catch (NumberFormatException e) {
            statusLabel.setText("Erreur : Veuillez vérifier les champs numériques.");
            statusLabel.setStyle("-fx-text-fill: red;");
        } catch (Exception e) {
            statusLabel.setText("Erreur lors de l'enregistrement.");
            statusLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }

    // AI Fields
    @FXML
    private TextField aiApiKeyField;
    @FXML
    private TextField aiModelField;
    @FXML
    private javafx.scene.control.TextArea aiSystemPromptField;
    @FXML
    private Spinner<Integer> aiMaxTokensSpinner;
    @FXML
    private Label aiStatusLabel;

    @FXML
    private void handleSaveAI() {
        try {
            ConfigService.setAiApiKey(aiApiKeyField.getText());
            ConfigService.setAiModel(aiModelField.getText());
            ConfigService.setAiSystemPrompt(aiSystemPromptField.getText());
            ConfigService.setAiMaxTokens(aiMaxTokensSpinner.getValue());

            aiStatusLabel.setText("Configuration IA enregistrée !");
            aiStatusLabel.setStyle("-fx-text-fill: green;");
        } catch (Exception e) {
            aiStatusLabel.setText("Erreur lors de l'enregistrement.");
            aiStatusLabel.setStyle("-fx-text-fill: red;");
            e.printStackTrace();
        }
    }
}
