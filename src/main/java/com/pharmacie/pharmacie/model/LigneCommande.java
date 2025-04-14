package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class LigneCommande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Association à la commande (Evite la récursion infinie avec la commande)
    @ManyToOne
    @JoinColumn(name = "commande_id", nullable = false)  // Ajout de 'nullable = false' pour forcer l'association
    @JsonBackReference // Évite la récursion infinie avec Commande
    private Commande commande;

    // Association au produit (Evite la récursion infinie avec Produit)
    @ManyToOne
    @JoinColumn(name = "produit_id")
    @JsonBackReference // Évite la récursion infinie avec Produit
    private Produit produit;

    private BigDecimal prixUnitaire;
    private Integer quantite;

    // Constructeur par défaut requis par Hibernate
    public LigneCommande() {
    }

    // Constructeur avec Produit, Quantité et Commande
    public LigneCommande(Produit produit, Integer quantite, Commande commande) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = produit.getPrixUnitaire();
        this.commande = commande;  // Associé à la commande
    }

    // Méthode pour calculer le total de la ligne de commande
    public BigDecimal getTotalLigne() {
        if (prixUnitaire != null && quantite != null) {
            return prixUnitaire.multiply(BigDecimal.valueOf(quantite));
        }
        return BigDecimal.ZERO;
    }

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Commande getCommande() {
        return commande;
    }

    public void setCommande(Commande commande) {
        this.commande = commande;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
    }

    public Integer getQuantite() {
        return quantite;
    }

    public void setQuantite(Integer quantite) {
        this.quantite = quantite;
    }
}
