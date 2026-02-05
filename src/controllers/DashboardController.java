package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import models.Livre;
import models.Activity;
import services.LivreService;
import services.MembreService;
import services.ActivityService;
import javafx.scene.control.ListCell;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController implements Initializable {

    private MainController mainController;
    private LivreService livreService;

    @FXML
    private VBox searchOverlay;
    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> searchResultsList;

    @FXML
    private ListView<Activity> activitiesList;

    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label activeMembersLabel;
    @FXML
    private Label activeLoansLabel;
    @FXML
    private Label availableBooksLabel;

    private MembreService membreService;
    private ActivityService activityService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        livreService = new LivreService();
        membreService = new MembreService();
        activityService = new ActivityService();

        // Setup search listener
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            updateSearchResults(newValue);
        });

        // Hide overlay when clicking on the background (empty space in VBox)
        searchOverlay.setOnMouseClicked(event -> {
            if (event.getTarget() == searchOverlay) {
                closeSearch();
            }
        });

        // Bind activities
        activitiesList.setItems(activityService.getActivities());
        activitiesList.setCellFactory(param -> new ListCell<Activity>() {
            @Override
            protected void updateItem(Activity item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    setText("●  " + item.getDescription() + "  [" + item.getFormattedTimestamp() + "]");
                    setStyle("-fx-padding: 5 10; -fx-text-fill: #555;"); // basic styling
                }
            }
        });

        updateStats();
    }

    private void updateStats() {
        int totalBooks = livreService.getAllLivres().size();
        long availableBooks = livreService.getAllLivres().stream().filter(Livre::isDisponible).count();
        long activeLoans = livreService.getAllLivres().stream().filter(l -> !l.isDisponible()).count();
        long activeMembers = membreService.getActiveMembersCount();

        if (totalBooksLabel != null)
            totalBooksLabel.setText(String.valueOf(totalBooks));
        if (availableBooksLabel != null)
            availableBooksLabel.setText(String.valueOf(availableBooks));
        if (activeMembersLabel != null)
            activeMembersLabel.setText(String.valueOf(activeMembers));
        if (activeLoansLabel != null)
            activeLoansLabel.setText(String.valueOf(activeLoans));
    }

    public void setMainController(MainController mainController) {
        this.mainController = mainController;
        // Also refresh stats when view happens if needed, checking periodically or on
        // event?
        // for now initialize is fine for first load, maybe add a refresh method calls
        // from MainController
    }

    @FXML
    private void handleAjouter() {
        if (mainController != null) {
            mainController.showLivres();
        }
    }

    @FXML
    private void handleInscrire() {
        if (mainController != null) {
            mainController.showMembres();
        }
    }

    @FXML
    private void handleRechercher() {
        searchOverlay.setVisible(true);
        searchField.requestFocus();
        updateSearchResults(""); // Show all or clear
    }

    @FXML
    public void closeSearch() {
        searchOverlay.setVisible(false);
        searchField.clear();
    }

    public void openSearch() {
        searchOverlay.setVisible(true);
        searchField.requestFocus();
        updateSearchResults("");
    }

    private void updateSearchResults(String query) {
        List<Livre> results;
        if (query == null || query.trim().isEmpty()) {
            results = livreService.getAllLivres();
        } else {
            results = livreService.rechercherLivre(query);
            // If empty, maybe show "No results"?
        }

        // Convert to display strings for ListView
        List<String> displayList = results.stream()
                .map(l -> l.getTitre() + " - " + l.getAuteur() + (l.isDisponible() ? " (Disponible)" : " (Emprunté)"))
                .collect(Collectors.toList());

        searchResultsList.setItems(FXCollections.observableArrayList(displayList));
    }
}
