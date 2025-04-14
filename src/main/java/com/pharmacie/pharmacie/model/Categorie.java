package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom")
    private String nom;

    // Une catégorie peut avoir plusieurs produits
    @JsonManagedReference  // Utilisé ici pour éviter la boucle infinie
    @OneToMany(mappedBy = "categorie")
    private List<Produit> produits;

    // Constructeur, getters et setters
    public Categorie() {}

    public Categorie(String nom) {
        this.nom = nom;
    }

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

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }
}
