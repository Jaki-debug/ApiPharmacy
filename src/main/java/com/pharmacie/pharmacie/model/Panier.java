package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@JsonIdentityInfo(
  generator = ObjectIdGenerators.PropertyGenerator.class,
  property = "id"
)
public class Panier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<FactureItem> factureItems = new ArrayList<>();

    private BigDecimal total = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    private StatutPanier statut = StatutPanier.EN_COURS;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", unique = true)
    private Utilisateur utilisateur;

    @OneToMany(mappedBy = "panier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Commande> commandes = new ArrayList<>();

    // 🔹 Constructeur par défaut
    public Panier() {}

    // 🔹 Constructeur avec utilisateur
    public Panier(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    // ✅ Ajouter un produit au panier
    public void ajouterProduit(Produit produit, int quantite) {
        if (produit.getStock() < quantite) {
            throw new IllegalArgumentException("Stock insuffisant");
        }

        // Vérifier si le produit existe déjà dans le panier
        FactureItem existant = factureItems.stream()
                .filter(item -> item.getProduit().getId().equals(produit.getId()))
                .findFirst()
                .orElse(null);

        if (existant != null) {
            existant.setQuantite(existant.getQuantite() + quantite);
        } else {
            FactureItem factureItem = new FactureItem(produit, quantite, this);
            factureItems.add(factureItem);
        }
    }

    // ✅ Ajouter un FactureItem existant
    public void ajouterFactureItem(FactureItem factureItem) {
        factureItems.add(factureItem);
        recalculerTotal();
    }

    // ✅ Supprimer un FactureItem du panier
    public void supprimerFactureItem(FactureItem factureItem) {
        if (factureItems.remove(factureItem)) {
            recalculerTotal();
        }
    }

    // ✅ Recalculer le total du panier
    public void recalculerTotal() {
        this.total = factureItems.stream()
                .map(item -> item.getProduit().getPrixUnitaire().multiply(BigDecimal.valueOf(item.getQuantite())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ✅ Récupérer les produits du panier
    public List<Produit> getProduits() {
        List<Produit> produits = new ArrayList<>();
        for (FactureItem item : factureItems) {
            produits.add(item.getProduit());
        }
        return produits;
    }

    // ✅ Valider le panier
    public void validerPanier() {
        if (factureItems.isEmpty()) {
            throw new IllegalStateException("Impossible de valider un panier vide.");
        }
        this.statut = StatutPanier.VALIDÉ;
    }

    // ✅ Annuler le panier
    public void annulerPanier() {
        this.statut = StatutPanier.ANNULÉ;
    }

    // ✅ Vérifier si le panier est validé
    public boolean estValide() {
        return statut == StatutPanier.VALIDÉ;
    }

    // ✅ Vider le panier
    public void viderPanier() {
        factureItems.clear();
        total = BigDecimal.ZERO;
        statut = StatutPanier.EN_COURS;
    }

    // 🔹 Getters et Setters
    public Long getId() {
        return id;
    }

    public List<FactureItem> getFactureItems() {
        return factureItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public StatutPanier getStatut() {
        return statut;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public List<Commande> getCommandes() {
        return commandes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public void setFactureItems(List<FactureItem> factureItems) {
        this.factureItems = factureItems;
        recalculerTotal();
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public void setStatut(StatutPanier statut) {
        this.statut = statut;
    }

    public void setCommandes(List<Commande> commandes) {
        this.commandes = commandes;
    }
}
