package services;

import models.Livre;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LivreService {
    // Shared data (Static) to persist across controller instances
    private static final List<Livre> livres = new ArrayList<>();

    private static final String FILE_PATH = "data/books.csv";

    static {
        loadData();
    }

    public LivreService() {
        // Constructor
    }

    public void ajouterLivre(Livre livre) {
        livres.add(livre);
        ActivityService.addActivity("Nouveau livre '" + livre.getTitre() + "' ajouté");
        saveData();
    }

    public void supprimerLivre(Livre livre) {
        livres.remove(livre);
        ActivityService.addActivity("Livre '" + livre.getTitre() + "' supprimé");
        saveData();
    }

    public void updateLivre(Livre livre) {
        int index = livres.indexOf(livre);
        if (index >= 0) {
            livres.set(index, livre);
            saveData();
        }
    }

    public List<Livre> rechercherLivre(String motCle) {
        return livres.stream()
                .filter(l -> l.getTitre().toLowerCase().contains(motCle.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Livre> getAllLivres() {
        return new ArrayList<>(livres);
    }

    public Livre getLivreById(int id) {
        return livres.stream()
                .filter(l -> l.getId() == id)
                .findFirst()
                .orElse(null);
    }

    // Persistence
    private static void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            // Mock data if file doesn't exist
            livres.add(new Livre(1, "Programmation Java", "Jean Dupont", false));
            livres.add(new Livre(2, "Design Patterns", "Erich Gamma", true));
            livres.add(new Livre(3, "Clean Code", "Robert Martin", true));
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            livres.clear();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length >= 4) {
                    int id = Integer.parseInt(parts[0]);
                    String title = parts[1];
                    String author = parts[2];
                    boolean isDisponible = Boolean.parseBoolean(parts[3]);
                    livres.add(new Livre(id, title, author, isDisponible));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void saveData() {
        File titleFile = new File(FILE_PATH);
        if (titleFile.getParentFile() != null) {
            titleFile.getParentFile().mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(titleFile))) {
            for (Livre livre : livres) {
                writer.write(livre.getId() + ";" +
                        livre.getTitre() + ";" +
                        livre.getAuteur() + ";" +
                        livre.isDisponible());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
