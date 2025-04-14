package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
public class FactureItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "panier_id", nullable = false)
    @JsonBackReference  // Évite les boucles infinies lors de la sérialisation JSON
    private Panier panier;

    private int quantite;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixUnitaire;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal total;

    // 🔹 Constructeur par défaut
    public FactureItem() {}

    // 🔹 Constructeur avec produit, quantité et panier
    public FactureItem(Produit produit, int quantite, Panier panier) {
        this.produit = produit;
        this.quantite = quantite;
        this.prixUnitaire = produit.getPrixUnitaire();
        this.panier = panier;
        this.total = calculerTotal();
    }

    // ✅ Calculer le total de cet item
    private BigDecimal calculerTotal() {
        return this.prixUnitaire.multiply(BigDecimal.valueOf(this.quantite));
    }

    // ✅ Getters et Setters
    public Long getId() {
        return id;
    }

    public Produit getProduit() {
        return produit;
    }

    public void setProduit(Produit produit) {
        this.produit = produit;
        this.prixUnitaire = produit.getPrixUnitaire();
        this.total = calculerTotal();
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
        this.total = calculerTotal();
    }

    public BigDecimal getPrixUnitaire() {
        return prixUnitaire;
    }

    public void setPrixUnitaire(BigDecimal prixUnitaire) {
        this.prixUnitaire = prixUnitaire;
        this.total = calculerTotal();
    }

    public BigDecimal getTotal() {
        return total;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    @Override
    public String toString() {
        return "FactureItem{" +
                "id=" + id +
                ", produit=" + produit.getNom() +
                ", quantite=" + quantite +
                ", prixUnitaire=" + prixUnitaire +
                ", total=" + total +
                '}';
    }
}
