package com.pharmacie.pharmacie.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;

@Entity
public class MouvementStock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull
    private TypeMouvement type;

    @NotNull
    private int quantite;

    @NotNull
    private LocalDateTime date;

    @NotNull
    private String utilisateur;

    @NotNull
    private String motif;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public TypeMouvement getType() { return type; }
    public void setType(TypeMouvement type) { this.type = type; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public String getUtilisateur() { return utilisateur; }
    public void setUtilisateur(String utilisateur) { this.utilisateur = utilisateur; }

    public String getMotif() { return motif; }
    public void setMotif(String motif) { this.motif = motif; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }
}
