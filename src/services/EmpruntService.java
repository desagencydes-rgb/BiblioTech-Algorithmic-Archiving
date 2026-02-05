package services;

import models.Emprunt;
import models.Livre;
import models.Membre;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpruntService {
    private static final List<Emprunt> emprunts = new ArrayList<>();
    private static final String FILE_PATH = "data/emprunts.csv";
    private static int nextId = 1;

    static {
        loadData();
    }

    public EmpruntService() {
    }

    public void ajouterEmprunt(Livre livre, Membre membre, LocalDate dateRetour) {
        if (!livre.isDisponible()) {
            // Should not happen if UI is correct, but safety check
            return;
        }

        Emprunt emprunt = new Emprunt(nextId++, livre, membre, LocalDate.now(), dateRetour, false);
        emprunts.add(emprunt);

        // Update book availability
        livre.setDisponible(false);
        new LivreService().updateLivre(livre);

        ActivityService.addActivity("Livre '" + livre.getTitre() + "' emprunté par " + membre.getNom());
        saveData();
    }

    public void retournerEmprunt(Emprunt emprunt) {
        if (emprunt.isEstRetourne())
            return;

        emprunt.setEstRetourne(true);
        // Ensure the book is available again
        Livre livre = emprunt.getLivre();
        if (livre != null) {
            livre.setDisponible(true);
            new LivreService().updateLivre(livre);
        }

        ActivityService.addActivity("Livre '" + (livre != null ? livre.getTitre() : "Inconnu") + "' retourné");
        saveData();
    }

    public List<Emprunt> getAllEmprunts() {
        return new ArrayList<>(emprunts);
    }

    public List<Emprunt> getEmpruntsEnCours() {
        List<Emprunt> enCours = new ArrayList<>();
        for (Emprunt e : emprunts) {
            if (!e.isEstRetourne()) {
                enCours.add(e);
            }
        }
        return enCours;
    }

    private static void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        LivreService livreService = new LivreService();
        MembreService membreService = new MembreService();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            emprunts.clear();
            int maxId = 0;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 6) {
                    int id = Integer.parseInt(parts[0]);
                    int livreId = Integer.parseInt(parts[1]);
                    int membreId = Integer.parseInt(parts[2]);
                    LocalDate dateEmprunt = LocalDate.parse(parts[3]);
                    LocalDate dateRetour = LocalDate.parse(parts[4]);
                    boolean estRetourne = Boolean.parseBoolean(parts[5]);

                    Livre livre = livreService.getLivreById(livreId);
                    Membre membre = membreService.getMembreById(membreId);

                    if (livre != null && membre != null) {
                        Emprunt emprunt = new Emprunt(id, livre, membre, dateEmprunt, dateRetour, estRetourne);
                        emprunts.add(emprunt);
                        if (id > maxId)
                            maxId = id;
                    }
                }
            }
            nextId = maxId + 1;
        } catch (IOException | NumberFormatException e) {
            e.printStackTrace();
        }
    }

    private static void saveData() {
        File file = new File(FILE_PATH);
        if (file.getParentFile() != null) {
            file.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (Emprunt emprunt : emprunts) {
                writer.write(emprunt.getId() + ";" +
                        (emprunt.getLivre() != null ? emprunt.getLivre().getId() : -1) + ";" +
                        (emprunt.getMembre() != null ? emprunt.getMembre().getId() : -1) + ";" +
                        emprunt.getDateEmprunt() + ";" +
                        emprunt.getDateRetour() + ";" +
                        emprunt.isEstRetourne());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
