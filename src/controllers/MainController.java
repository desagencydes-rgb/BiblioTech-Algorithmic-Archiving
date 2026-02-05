package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.control.Label;
import services.ConfigService;

public class MainController implements Initializable {

    @FXML
    private BorderPane mainPane;
    @FXML
    private ScrollPane contentScrollPane;
    @FXML
    private Label appTitleLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (appTitleLabel != null) {
            appTitleLabel.textProperty().bind(ConfigService.appTitleProperty());
        }
        showDashboard();
    }

    @FXML
    public void showDashboard() {
        showDashboardAndOpenSearch(false);
    }

    @FXML
    public void showSearch() {
        showDashboardAndOpenSearch(true);
    }

    private void showDashboardAndOpenSearch(boolean openSearch) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
            Parent view = loader.load();

            DashboardController controller = loader.getController();
            controller.setMainController(this);

            if (openSearch) {
                controller.openSearch();
            }

            contentScrollPane.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading dashboard view");
        }
    }

    public void showLivres() {
        // Just do the loading here since we are here.
        try {
            FXMLLoader loaderLocal = new FXMLLoader(getClass().getResource("/views/Livres.fxml"));
            Parent view = loaderLocal.load();
            LivresController controller = loaderLocal.getController();
            controller.setMainController(this);
            contentScrollPane.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showMembres() {
        loadView("/views/Membres.fxml");
    }

    @FXML
    private void showEmprunts() {
        loadView("/views/Emprunts.fxml");
    }

    @FXML
    private void showAbout() {
        loadView("/views/About.fxml");
    }

    @FXML
    private void showDocumentation() {
        loadView("/views/Documentation.fxml");
    }

    @FXML
    public void showSettings() {
        loadView("/views/Settings.fxml");
    }

    @FXML
    private void handleExit() {
        javafx.application.Platform.exit();
    }

    @FXML
    public void showAiAssistant() {
        loadView("/views/AiAssistant.fxml");
    }

    private void loadView(String fxmlPath) {
        try {
            URL resource = getClass().getResource(fxmlPath);
            if (resource == null) {
                System.err.println("Error: FXML file not found: " + fxmlPath);
                return;
            }

            FXMLLoader loader = new FXMLLoader(resource);
            Parent view = loader.load();
            contentScrollPane.setContent(view);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error loading view: " + fxmlPath);
        }
    }
}
