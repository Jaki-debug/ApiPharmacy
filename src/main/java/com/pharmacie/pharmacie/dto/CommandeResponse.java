package com.pharmacie.pharmacie.dto;

import com.pharmacie.pharmacie.model.StatutCommande;

import java.math.BigDecimal;
import java.util.List;

public class CommandeResponse {

    private Long id;
    private String modeCommande;
    private StatutCommande statut;
    private BigDecimal totalCommande;
    private List<LigneCommandeResponse> lignesCommande;
    private UtilisateurResponse utilisateur;

    // Getters et setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getModeCommande() {
        return modeCommande;
    }

    public void setModeCommande(String modeCommande) {
        this.modeCommande = modeCommande;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public BigDecimal getTotalCommande() {
        return totalCommande;
    }

    public void setTotalCommande(BigDecimal totalCommande) {
        this.totalCommande = totalCommande;
    }

    public List<LigneCommandeResponse> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommandeResponse> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    public UtilisateurResponse getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurResponse utilisateur) {
        this.utilisateur = utilisateur;
    }

    // Classe LigneCommandeResponse
    public static class LigneCommandeResponse {
        private Long id;
        private ProduitResponse produit;
        private BigDecimal prixUnitaire;  // Changement en BigDecimal
        private int quantite;
        private BigDecimal totalLigne;  // Changement en BigDecimal

        // Getters et setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public ProduitResponse getProduit() {
            return produit;
        }

        public void setProduit(ProduitResponse produit) {
            this.produit = produit;
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

        public BigDecimal getTotalLigne() {
            return totalLigne;
        }

        public void setTotalLigne(BigDecimal totalLigne) {
            this.totalLigne = totalLigne;
        }
    }
 // Classe ProduitResponse
    public static class ProduitResponse {
        private Long id;
        private String nom;
        private String description;
        private BigDecimal prixUnitaire;  // Utilise prixUnitaire au lieu de prix
        private int stock;

        // Getters et setters
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getPrixUnitaire() {  // Changer ici pour retourner prixUnitaire
            return prixUnitaire;
        }

        public void setPrixUnitaire(BigDecimal prixUnitaire) {  // Changer ici pour définir prixUnitaire
            this.prixUnitaire = prixUnitaire;
        }

        public int getStock() {
            return stock;
        }

        public void setStock(int stock) {
            this.stock = stock;
        }
    }


    // Classe UtilisateurResponse
    public static class UtilisateurResponse {
        private Long id;
        private String nom;
        private String email;
        private List<String> roles; // Rôles utilisateur

        // Getters et setters
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

        public List<String> getRoles() {
            return roles;
        }

        public void setRoles(List<String> roles) {
            this.roles = roles;
        }
    }
}
