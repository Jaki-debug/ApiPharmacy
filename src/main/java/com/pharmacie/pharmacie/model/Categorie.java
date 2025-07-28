package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.List;

@Entity
@JsonIdentityInfo(
    generator = ObjectIdGenerators.PropertyGenerator.class,
    property = "id"
)
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom")
    private String nom;

    @Column(name = "margePourcentage")
    private BigDecimal margePourcentage; // ex: 20 pour 20% de marge

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonIgnore  // 👉 Ignore côté JSON
    private List<Produit> produits;

    // Constructeurs
    public Categorie() {}

    public Categorie(String nom) {
        this.nom = nom;
    }

    public Categorie(String nom, BigDecimal margePourcentage) {
        this.nom = nom;
        this.margePourcentage = margePourcentage;
    }

    // Getters & Setters
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

    public BigDecimal getMargePourcentage() {
        return margePourcentage;
    }

    public void setMargePourcentage(BigDecimal margePourcentage) {
        this.margePourcentage = margePourcentage;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }
}
