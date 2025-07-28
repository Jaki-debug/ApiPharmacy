package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER) // Important : charge le produit avec la ligne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    @JsonIgnore // Évite les boucles infinies
    private Commande commande;

    private BigDecimal prixUnitaire;

    private int quantite;

    // Constructeurs
    public LigneCommande() {}

    public LigneCommande(Produit produit, BigDecimal prixUnitaire, int quantite) {
        this.produit = produit;
        this.prixUnitaire = prixUnitaire;
        this.quantite = quantite;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    // Calcul du total pour une ligne
    public BigDecimal getTotalLigne() {
        return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
    }
}
