package com.pharmacie.pharmacie.dto;

public class UtilisateurDTO {
    private String email;
    private String motDePasse;  // adapte le nom aussi pour rester cohérent
    private String role;

    public UtilisateurDTO() {}

    public UtilisateurDTO(String email, String motDePasse, String role) {
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    // getters/setters

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMotDePasse() { return motDePasse; }
    public void setMotDePasse(String motDePasse) { this.motDePasse = motDePasse; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
}
