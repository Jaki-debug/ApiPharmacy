package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModeCommande modeCommande;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<LigneCommande> lignesCommande = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
    @JsonBackReference
    private Utilisateur utilisateur;

    @ManyToOne
    @JoinColumn(name = "pharmacien_id")
    private User pharmacien;

    @ManyToOne
    @JoinColumn(name = "panier_id")
    private Panier panier;

    @Enumerated(EnumType.STRING)
    private StatutCommande statut;

    @Column(nullable = false)
    private LocalDateTime dateCommande = LocalDateTime.now();

    // Constructeur par défaut
    public Commande() {
        this.statut = StatutCommande.EN_ATTENTE;
    }

    // Méthodes utilitaires pour gérer la relation bidirectionnelle
    public void addLigneCommande(LigneCommande ligneCommande) {
        lignesCommande.add(ligneCommande);
        ligneCommande.setCommande(this);
    }

    public void removeLigneCommande(LigneCommande ligneCommande) {
        lignesCommande.remove(ligneCommande);
        ligneCommande.setCommande(null);
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ModeCommande getModeCommande() {
        return modeCommande;
    }

    public void setModeCommande(ModeCommande modeCommande) {
        this.modeCommande = modeCommande;
    }

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

    public User getPharmacien() {
        return pharmacien;
    }

    public void setPharmacien(User pharmacien) {
        this.pharmacien = pharmacien;
    }

    public Panier getPanier() {
        return panier;
    }

    public void setPanier(Panier panier) {
        this.panier = panier;
    }

    public StatutCommande getStatut() {
        return statut;
    }

    public void setStatut(StatutCommande statut) {
        this.statut = statut;
    }

    public LocalDateTime getDateCommande() {
        return dateCommande;
    }

    public void setDateCommande(LocalDateTime dateCommande) {
        this.dateCommande = dateCommande;
    }

    // Calcul du total de la commande
    public BigDecimal calculerTotal() {
        if (lignesCommande == null || lignesCommande.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return lignesCommande.stream()
                .map(LigneCommande::getTotalLigne)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCommande() {
        return calculerTotal();
    }
}
