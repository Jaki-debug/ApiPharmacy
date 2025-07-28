package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id"
)
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ModeCommande modeCommande;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LigneCommande> lignesCommande = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "utilisateur_id")
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

    @Column(name = "reference", unique = true)
    private String reference;

    @Column(name = "statut_paiement")
    private String statutPaiement;

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

    // --- NOUVELLE METHODE POUR LE TOTAL ---
    public BigDecimal getTotalCommande() {
        if (lignesCommande == null || lignesCommande.isEmpty()) {
            return BigDecimal.ZERO;
        }
        return lignesCommande.stream()
            .map(ligne -> ligne.getPrixUnitaire().multiply(BigDecimal.valueOf(ligne.getQuantite())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // Getters et Setters habituels

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

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getStatutPaiement() {
        return statutPaiement;
    }

    public void setStatutPaiement(String statutPaiement) {
        this.statutPaiement = statutPaiement;
    }
}
