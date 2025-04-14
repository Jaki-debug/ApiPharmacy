package com.pharmacie.pharmacie.dto;

import java.math.BigDecimal;

public class FactureItemDTO {

    private Long id;
    private Long produitId;
    private int quantite;
    private BigDecimal prixTotal;

    // Constructeur
    public FactureItemDTO(Long id, Long produitId, int quantite, BigDecimal prixTotal) {
        this.id = id;
        this.produitId = produitId;
        this.quantite = quantite;
        this.prixTotal = prixTotal;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProduitId() {
        return produitId;
    }

    public void setProduitId(Long produitId) {
        this.produitId = produitId;
    }

    public int getQuantite() {
        return quantite;
    }

    public void setQuantite(int quantite) {
        this.quantite = quantite;
    }

    public BigDecimal getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(BigDecimal prixTotal) {
        this.prixTotal = prixTotal;
    }
}
