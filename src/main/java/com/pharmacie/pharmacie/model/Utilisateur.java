package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // Évite les erreurs Hibernate
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String email;

    @JsonIgnore // Empêche l'exposition du mot de passe
    private String motDePasse;

    @Column(nullable = false)
    private String role = "user"; // valeur par défaut

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "panier_id")
    private Panier panier;  // Un seul panier pour l'utilisateur

    // 🔹 Constructeurs
    public Utilisateur() {}

    public Utilisateur(String nom, String email, String motDePasse) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = "user";
    }

    public Utilisateur(String nom, String email, String motDePasse, String role, Panier panier) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.role = (role == null || role.isEmpty()) ? "user" : role;
        this.panier = panier;
    }

    // 🔹 Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = (role == null || role.isEmpty()) ? "user" : role;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }
}
