package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.Livre;
import models.Membre;
import services.LivreService;
import services.MembreService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;

public class MembresController implements Initializable {

    @FXML
    private TextField fullNameField;
    @FXML
    private TextField cinField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField addressField;

    @FXML
    private RadioButton radioStudent;
    @FXML
    private RadioButton radioTeacher;
    @FXML
    private RadioButton radioExternal;
    @FXML
    private ToggleGroup membershipType;

    @FXML
    private CheckBox activeCheckBox;

    @FXML
    private TextField studentIdField;
    @FXML
    private TextField emergencyContactField;
    @FXML
    private TextField emergencyPhoneField;

    @FXML
    private TableView<Membre> membersTable;
    @FXML
    private TableColumn<Membre, Integer> idColumn;
    @FXML
    private TableColumn<Membre, String> nameColumn;
    @FXML
    private TableColumn<Membre, String> emailColumn;

    @FXML
    private Label totalBooksLabel;
    @FXML
    private Label activeMembersLabel;
    @FXML
    private Label activeLoansLabel;
    @FXML
    private Label availableBooksLabel;

    private MembreService membreService;
    private LivreService livreService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        membreService = new MembreService();
        livreService = new LivreService();

        setupTable();
        loadMembers();

        // Initialize logic
        if (membershipType == null) {
            membershipType = new ToggleGroup();
            if (radioStudent != null)
                radioStudent.setToggleGroup(membershipType);
            if (radioTeacher != null)
                radioTeacher.setToggleGroup(membershipType);
            if (radioExternal != null)
                radioExternal.setToggleGroup(membershipType);
        }
        if (radioStudent != null)
            radioStudent.setSelected(true);
        if (activeCheckBox != null)
            activeCheckBox.setSelected(true);

        updateStats();
    }

    private void setupTable() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
    }

    private void loadMembers() {
        if (membersTable != null) {
            membersTable.setItems(FXCollections.observableArrayList(membreService.getAllMembres()));
        }
    }

    private void updateStats() {
        if (livreService == null)
            livreService = new LivreService();
        if (membreService == null)
            membreService = new MembreService();

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

    @FXML
    private void handleSave() {
        String name = fullNameField.getText();
        String email = emailField.getText();
        String cin = cinField.getText();
        String phone = phoneField.getText();
        String address = addressField.getText();
        String studentId = studentIdField.getText();
        String emergencyContact = emergencyContactField.getText();
        String emergencyPhone = emergencyPhoneField.getText();

        String type = "Autre";
        if (radioStudent.isSelected())
            type = "Etudiant";
        else if (radioTeacher.isSelected())
            type = "Enseignant";
        else if (radioExternal.isSelected())
            type = "Externe";

        boolean isActive = activeCheckBox.isSelected();

        if (name == null || name.trim().isEmpty()) {
            System.out.println("Nom requis");
            return;
        }

        // Auto-increment ID
        int id = 0;
        for (Membre m : membreService.getAllMembres()) {
            if (m.getId() > id)
                id = m.getId();
        }
        id++; // Next ID

        Membre nouveauMembre = new Membre(id, name, email, cin, phone, address, type, isActive, studentId,
                emergencyContact, emergencyPhone);

        membreService.ajouterMembre(nouveauMembre);
        System.out.println("Membre ajouté: " + name);

        handleReset();
        loadMembers(); // Refresh list
        updateStats(); // Refresh stats
    }

    @FXML
    private void handleReset() {
        if (fullNameField != null)
            fullNameField.clear();
        if (cinField != null)
            cinField.clear();
        if (phoneField != null)
            phoneField.clear();
        if (emailField != null)
            emailField.clear();
        if (addressField != null)
            addressField.clear();
        if (radioStudent != null)
            radioStudent.setSelected(true);
        if (activeCheckBox != null)
            activeCheckBox.setSelected(true);
        if (studentIdField != null)
            studentIdField.clear();
        if (emergencyContactField != null)
            emergencyContactField.clear();
        if (emergencyPhoneField != null)
            emergencyPhoneField.clear();
    }
}
