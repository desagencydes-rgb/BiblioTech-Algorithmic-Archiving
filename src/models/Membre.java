package models;

public class Membre {
    private int id;
    private String nom;
    private String email;
    private String cin;
    private String phone;
    private String address;
    private String type; // Etudiant, Enseignant, Externe
    private boolean active;
    private String studentId;
    private String emergencyContact;
    private String emergencyPhone;

    public Membre(int id, String nom, String email) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.active = true; // Default
    }

    public Membre(int id, String nom, String email, String cin, String phone, String address, String type,
            boolean active, String studentId, String emergencyContact, String emergencyPhone) {
        this.id = id;
        this.nom = nom;
        this.email = email;
        this.cin = cin;
        this.phone = phone;
        this.address = address;
        this.type = type;
        this.active = active;
        this.studentId = studentId;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getEmergencyPhone() {
        return emergencyPhone;
    }

    public void setEmergencyPhone(String emergencyPhone) {
        this.emergencyPhone = emergencyPhone;
    }
}
