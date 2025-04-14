package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "panier_id")
    private Panier panier;  // Un seul panier pour l'utilisateur

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id")
    private List<Role> roles = new ArrayList<>();

    // 🔹 Constructeurs
    public Utilisateur() {}

    public Utilisateur(String nom, String email, String motDePasse) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
    }

    public Utilisateur(String nom, String email, String motDePasse, List<Role> roles, Panier panier) {
        this.nom = nom;
        this.email = email;
        this.motDePasse = motDePasse;
        this.roles = roles;
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

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }
}
