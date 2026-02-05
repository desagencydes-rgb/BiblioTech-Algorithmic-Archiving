package services;

import models.Membre;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MembreService {
    private static final List<Membre> membres = new ArrayList<>();

    private static final String FILE_PATH = "data/members.csv";

    static {
        loadData();
    }

    public MembreService() {
        // Empty constructor
    }

    public void ajouterMembre(Membre membre) {
        membres.add(membre);
        ActivityService.addActivity("Nouveau membre '" + membre.getNom() + "' inscrit");
        saveData();
    }

    public void supprimerMembre(Membre membre) {
        membres.remove(membre);
        ActivityService.addActivity("Membre '" + membre.getNom() + "' supprimé");
        saveData();
    }

    public void updateMembre(Membre membre) {
        int index = membres.indexOf(membre);
        if (index >= 0) {
            membres.set(index, membre);
            saveData();
        }
    }

    public Membre rechercherMembre(String nom) {
        return membres.stream()
                .filter(m -> m.getNom().equalsIgnoreCase(nom))
                .findFirst()
                .orElse(null);
    }

    public List<Membre> getAllMembres() {
        return new ArrayList<>(membres);
    }

    public Membre getMembreById(int id) {
        return membres.stream()
                .filter(m -> m.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public long getActiveMembersCount() {
        return membres.stream().filter(Membre::isActive).count();
    }

    private static void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            membres.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 11) {
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    String cin = parts[3];
                    String phone = parts[4];
                    String address = parts[5];
                    String type = parts[6];
                    boolean active = Boolean.parseBoolean(parts[7]);
                    String studentId = parts[8];
                    String emergencyContact = parts[9];
                    String emergencyPhone = parts[10];

                    // Handle "null" strings if any
                    if ("null".equals(email))
                        email = "";
                    if ("null".equals(cin))
                        cin = "";
                    if ("null".equals(phone))
                        phone = "";
                    if ("null".equals(address))
                        address = "";
                    if ("null".equals(type))
                        type = "";
                    if ("null".equals(studentId))
                        studentId = "";
                    if ("null".equals(emergencyContact))
                        emergencyContact = "";
                    if ("null".equals(emergencyPhone))
                        emergencyPhone = "";

                    membres.add(new Membre(id, name, email, cin, phone, address, type, active, studentId,
                            emergencyContact, emergencyPhone));
                } else if (parts.length >= 3) {
                    // Backward compatibility for old format
                    int id = Integer.parseInt(parts[0]);
                    String name = parts[1];
                    String email = parts[2];
                    membres.add(new Membre(id, name, email));
                }
            }
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
            for (Membre membre : membres) {
                writer.write(membre.getId() + ";" +
                        (membre.getNom() != null ? membre.getNom() : "") + ";" +
                        (membre.getEmail() != null ? membre.getEmail() : "") + ";" +
                        (membre.getCin() != null ? membre.getCin() : "") + ";" +
                        (membre.getPhone() != null ? membre.getPhone() : "") + ";" +
                        (membre.getAddress() != null ? membre.getAddress() : "") + ";" +
                        (membre.getType() != null ? membre.getType() : "") + ";" +
                        membre.isActive() + ";" +
                        (membre.getStudentId() != null ? membre.getStudentId() : "") + ";" +
                        (membre.getEmergencyContact() != null ? membre.getEmergencyContact() : "") + ";" +
                        (membre.getEmergencyPhone() != null ? membre.getEmergencyPhone() : ""));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
