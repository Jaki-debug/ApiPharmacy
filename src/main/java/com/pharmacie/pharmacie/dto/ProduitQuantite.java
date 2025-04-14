package com.pharmacie.pharmacie.dto;  

public class ProduitQuantite {
    private Long produitId;  
    private int quantite;    

    // Constructeur par défaut
    public ProduitQuantite() {}

    // Constructeur avec paramètres
    public ProduitQuantite(Long produitId, int quantite) {
        this.produitId = produitId;
        this.quantite = quantite;
    }

    // Getters et setters
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

    @Override
    public String toString() {
        return "ProduitQuantite{" +
                "produitId=" + produitId +
                ", quantite=" + quantite +
                '}';
    }
}
