package com.pharmacie.pharmacie.dto;

import java.math.BigDecimal;

public class ProduitDTO {

    private Long id;
    private String nom;
    private BigDecimal prixUnitaire;
    private int stock;

    // Constructeur
    public ProduitDTO(Long id, String nom, BigDecimal prixUnitaire, int stock) {
        this.id = id;
        this.nom = nom;
        this.prixUnitaire = prixUnitaire;
        this.stock = stock;
    }

    // Getters et Setters
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

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
}
