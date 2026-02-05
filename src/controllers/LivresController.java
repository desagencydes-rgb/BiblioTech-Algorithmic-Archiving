package controllers;

import javafx.collections.FXCollections;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Livre;
import services.LivreService;
import services.MembreService;

import java.net.URL;
import java.util.ResourceBundle;

public class LivresController implements Initializable {

    @FXML
    private TextField titleField;
    @FXML
    private TextField authorField;
    @FXML
    private TextField isbnField;
    @FXML
    private TextField categoryField;
    @FXML
    private DatePicker publishedDate;
    @FXML
    private TextField publisherField;
    @FXML
    private TextField editionField;
    @FXML
    private TextField copiesField;
    @FXML
    private TextArea descriptionArea;

    @FXML
    private TableView<Livre> booksTable;
    @FXML
    private TableColumn<Livre, Integer> colId;
    @FXML
    private TableColumn<Livre, String> colTitre;
    @FXML
    private TableColumn<Livre, String> colAuteur;
    @FXML
    private TableColumn<Livre, String> colDisponible;

    private LivreService livreService;
    private MainController mainController;

    public void setMainController(MainController mc) {
        this.mainController = mc;
    }

    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label activeMembersLabel;
    @FXML
    private Label activeLoansLabel;
    @FXML
    private Label availableBooksLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        livreService = new LivreService();
        setupTable();
        loadBooks();
        updateStats();
    }

    private void updateStats() {
        int total = livreService.getAllLivres().size();
        long available = livreService.getAllLivres().stream().filter(Livre::isDisponible).count();
        long loans = livreService.getAllLivres().stream().filter(l -> !l.isDisponible()).count();

        MembreService membreService = new MembreService();
        long members = membreService.getActiveMembersCount();

        if (totalBooksLabel != null)
            totalBooksLabel.setText(String.valueOf(total));
        if (availableBooksLabel != null)
            availableBooksLabel.setText(String.valueOf(available));
        if (activeMembersLabel != null)
            activeMembersLabel.setText(String.valueOf(members));
        if (activeLoansLabel != null)
            activeLoansLabel.setText(String.valueOf(loans));
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colTitre.setCellValueFactory(new PropertyValueFactory<>("titre"));
        colAuteur.setCellValueFactory(new PropertyValueFactory<>("auteur"));

        colDisponible.setCellValueFactory(cellData -> {
            boolean dispo = cellData.getValue().isDisponible();
            return new SimpleStringProperty(dispo ? "Disponible" : "Emprunté");
        });
    }

    private void loadBooks() {
        if (booksTable != null) {
            booksTable.setItems(FXCollections.observableArrayList(livreService.getAllLivres()));
        }
    }

    @FXML
    private void handleSave() {
        String titre = titleField.getText();
        String auteur = authorField.getText();
        String isbn = isbnField.getText();
        String categorie = categoryField.getText();
        String editeur = publisherField.getText();
        String description = descriptionArea.getText();

        if (titre == null || titre.trim().isEmpty() || auteur == null || auteur.trim().isEmpty()) {
            // Quick validation
            System.out.println("Validation Error: Title and Author are required.");
            return;
        }

        // Basic ID generation (auto-increment simulation based on list size + 1 for
        // now)
        int id = livreService.getAllLivres().size() + 1;

        // Simulating Status as true (Available) by default
        Livre nouveauLivre = new Livre(id, titre, auteur, true);
        // In a real app, we would set ISBN, Category, etc. on the model if fields
        // existed.

        livreService.ajouterLivre(nouveauLivre);

        // Clear form
        clearForm();

        // Refresh table
        loadBooks();
        updateStats();

        System.out.println("Livre ajouté: " + titre);
    }

    private void clearForm() {
        titleField.clear();
        authorField.clear();
        isbnField.clear();
        categoryField.clear();
        publisherField.clear();
        editionField.clear();
        copiesField.clear();
        descriptionArea.clear();
        publishedDate.setValue(null);
    }

    @FXML
    private void handleBack() {
        if (mainController != null) {
            mainController.showDashboard();
        } else {
            System.err.println("MainController reference is null");
        }
    }
}
