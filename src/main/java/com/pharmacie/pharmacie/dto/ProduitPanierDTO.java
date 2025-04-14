package com.pharmacie.pharmacie.dto;

import java.math.BigDecimal;

public class ProduitPanierDTO {
    private String nom;
    private int quantite;
    private BigDecimal prixUnitaire;
    private BigDecimal sousTotal;

    public ProduitPanierDTO(String nom, int quantite, BigDecimal prixUnitaire) {
        this.nom = nom;
        this.quantite = quantite;
        this.prixUnitaire = prixUnitaire;
        this.sousTotal = prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }

    public String getNom() { return nom; }
    public int getQuantite() { return quantite; }
    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public BigDecimal getSousTotal() { return sousTotal; }
}
