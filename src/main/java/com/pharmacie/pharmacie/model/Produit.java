package com.pharmacie.pharmacie.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "nom")
    private String nom;

    @ManyToOne
    @JoinColumn(name = "categorie_id", referencedColumnName = "id")
    @JsonBackReference // Pour éviter les boucles infinies lors de la sérialisation JSON
    private Categorie categorie;

    @NotNull
    @Column(name = "prixUnitaire")
    private BigDecimal prixUnitaire;

    @NotNull
    @Column(name = "stock")
    private Integer stock;

    @Column(name = "dateExpiration")
    private LocalDate dateExpiration;

    @Column(name = "description")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<MouvementStock> mouvements;

    @JsonIgnore
    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<LigneCommande> lignesCommande;

    @Column(name = "imagePath", nullable = true)
    private String imagePath;

    // Constructeurs
    public Produit() {}

    public Produit(String nom, Categorie categorie, @NotNull BigDecimal prixUnitaire, Integer stock,
                   LocalDate dateExpiration, String description, String imagePath) {
        this.nom = nom;
        this.categorie = categorie;
        this.prixUnitaire = prixUnitaire;
        this.stock = stock;
        this.dateExpiration = dateExpiration;
        this.description = description;
        this.imagePath = imagePath;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    public BigDecimal getPrixUnitaire() { return prixUnitaire; }
    public void setPrixUnitaire(BigDecimal prixUnitaire) { this.prixUnitaire = prixUnitaire; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public LocalDate getDateExpiration() { return dateExpiration; }
    public void setDateExpiration(LocalDate dateExpiration) { this.dateExpiration = dateExpiration; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public List<MouvementStock> getMouvements() { return mouvements; }
    public void setMouvements(List<MouvementStock> mouvements) { this.mouvements = mouvements; }

    public List<LigneCommande> getLignesCommande() { return lignesCommande; }
    public void setLignesCommande(List<LigneCommande> lignesCommande) { this.lignesCommande = lignesCommande; }
}
