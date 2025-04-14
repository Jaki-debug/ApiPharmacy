package com.pharmacie.pharmacie.dto;

import com.pharmacie.pharmacie.model.LigneCommande;
import com.pharmacie.pharmacie.model.Utilisateur;

import java.util.List;

public class CommandeEnLigneRequest {
    private List<LigneCommande> lignesCommande;
    private Utilisateur utilisateur;

    // Getters et Setters
    public List<LigneCommande> getLignesCommande() {
        return lignesCommande;
    }

    public void setLignesCommande(List<LigneCommande> lignesCommande) {
        this.lignesCommande = lignesCommande;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
