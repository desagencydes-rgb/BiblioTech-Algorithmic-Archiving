package models;

import java.time.LocalDate;

public class Emprunt {
    private int id;
    private Livre livre;
    private Membre membre;
    private LocalDate dateEmprunt;
    private LocalDate dateRetour;
    private boolean estRetourne;

    public Emprunt(int id, Livre livre, Membre membre, LocalDate dateEmprunt, LocalDate dateRetour,
            boolean estRetourne) {
        this.id = id;
        this.livre = livre;
        this.membre = membre;
        this.dateEmprunt = dateEmprunt;
        this.dateRetour = dateRetour;
        this.estRetourne = estRetourne;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Membre getMembre() {
        return membre;
    }

    public void setMembre(Membre membre) {
        this.membre = membre;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }

    public boolean isEstRetourne() {
        return estRetourne;
    }

    public void setEstRetourne(boolean estRetourne) {
        this.estRetourne = estRetourne;
    }
}
