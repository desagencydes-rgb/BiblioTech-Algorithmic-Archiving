package controllers;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import models.Emprunt;
import models.Livre;
import models.Membre;
import services.EmpruntService;
import services.LivreService;
import services.MembreService;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmpruntsController implements Initializable {

    @FXML
    private TableView<Emprunt> tableEmprunts;
    @FXML
    private TableColumn<Emprunt, Integer> colId;
    @FXML
    private TableColumn<Emprunt, String> colLivre;
    @FXML
    private TableColumn<Emprunt, String> colMembre;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateEmprunt;
    @FXML
    private TableColumn<Emprunt, LocalDate> colDateRetour;
    @FXML
    private TableColumn<Emprunt, String> colStatut;

    @FXML
    private ComboBox<Livre> comboLivre;
    @FXML
    private ComboBox<Membre> comboMembre;
    @FXML
    private DatePicker dateRetourPicker;
    @FXML
    private Button btnAjouter;
    @FXML
    private Button btnRetourner;

    private EmpruntService empruntService;
    private LivreService livreService;
    private MembreService membreService;

    public EmpruntsController() {
        this.empruntService = new EmpruntService();
        this.livreService = new LivreService();
        this.membreService = new MembreService();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupTable();
        loadData();
        setupForm();
    }

    private void setupTable() {
        colId.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getId()));
        colLivre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLivre().getTitre()));
        colMembre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getMembre().getNom()));
        colDateEmprunt
                .setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateEmprunt()));
        colDateRetour.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getDateRetour()));

        colStatut.setCellValueFactory(cellData -> {
            boolean returned = cellData.getValue().isEstRetourne();
            if (returned)
                return new SimpleStringProperty("Retourné");

            if (cellData.getValue().getDateRetour().isBefore(LocalDate.now())) {
                return new SimpleStringProperty("En retard");
            }
            return new SimpleStringProperty("En cours");
        });

        // Custom rendering for Status column to add colors? (Optional, maybe later)
    }

    private void loadData() {
        tableEmprunts.setItems(FXCollections.observableArrayList(empruntService.getAllEmprunts()));
        refreshCombos();
    }

    private void refreshCombos() {
        // Only available books
        comboLivre.setItems(FXCollections.observableArrayList(
                livreService.getAllLivres().stream()
                        .filter(Livre::isDisponible)
                        .collect(Collectors.toList())));

        comboMembre.setItems(FXCollections.observableArrayList(membreService.getAllMembres()));
    }

    private void setupForm() {
        // Converters for ComboBox to display names properly
        comboLivre.setConverter(new StringConverter<Livre>() {
            @Override
            public String toString(Livre object) {
                return object == null ? "" : object.getTitre() + " (" + object.getAuteur() + ")";
            }

            @Override
            public Livre fromString(String string) {
                return null; // Not needed for selection
            }
        });

        comboMembre.setConverter(new StringConverter<Membre>() {
            @Override
            public String toString(Membre object) {
                return object == null ? "" : object.getNom() + " (" + object.getId() + ")";
            }

            @Override
            public Membre fromString(String string) {
                return null;
            }
        });

        dateRetourPicker.setValue(LocalDate.now().plusWeeks(2)); // Default 2 weeks
    }

    @FXML
    private void handleAjouter() {
        Livre selectedLivre = comboLivre.getValue();
        Membre selectedMembre = comboMembre.getValue();
        LocalDate dateRetour = dateRetourPicker.getValue();

        if (selectedLivre == null || selectedMembre == null || dateRetour == null) {
            showAlert(Alert.AlertType.WARNING, "Champs manquants",
                    "Veuillez sélectionner un livre, un membre et une date de retour.");
            return;
        }

        if (dateRetour.isBefore(LocalDate.now())) {
            showAlert(Alert.AlertType.WARNING, "Date invalide",
                    "La date de retour ne peut pas être antérieure à aujourd'hui.");
            return;
        }

        empruntService.ajouterEmprunt(selectedLivre, selectedMembre, dateRetour);

        // Clear form and refresh
        comboLivre.setValue(null);
        comboMembre.setValue(null);
        dateRetourPicker.setValue(LocalDate.now().plusWeeks(2));

        loadData();
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Emprunt enregistré avec succès.");
    }

    @FXML
    private void handleRetourner() {
        Emprunt selected = tableEmprunts.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert(Alert.AlertType.WARNING, "Aucune sélection", "Veuillez sélectionner un emprunt à retourner.");
            return;
        }

        if (selected.isEstRetourne()) {
            showAlert(Alert.AlertType.INFORMATION, "Information", "Cet emprunt est déjà retourné.");
            return;
        }

        empruntService.retournerEmprunt(selected);
        loadData(); // Refresh table and combos (book becomes available)
        showAlert(Alert.AlertType.INFORMATION, "Succès", "Livre retourné avec succès.");
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
