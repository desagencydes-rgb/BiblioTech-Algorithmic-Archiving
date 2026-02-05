package models;

public class Livre {
    private int id;
    private String titre;
    private String auteur;
    private boolean disponible;

    public Livre(int id, String titre, String auteur, boolean disponible) {
        this.id = id;
        this.titre = titre;
        this.auteur = auteur;
        this.disponible = disponible;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
}
