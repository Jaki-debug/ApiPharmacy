package com.pharmacie.pharmacie.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class Pharmacie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Nom de la pharmacie (obligatoire)
    @Column(nullable = false)
    private String nom;

    // Email (obligatoire, unique pour la connexion)
    @Column(nullable = false, unique = true)
    private String email;

    // Mot de passe (obligatoire)
    @Column(nullable = false)
    private String motDePasse;

    // Adresse physique (optionnelle)
    private String adresse;

    // Téléphone (optionnel)
    private String telephone;

    // Statut : est-ce une pharmacie de garde ?
    private boolean pharmacieDeGarde;

    // Coordonnées GPS
    private Double latitude;
    private Double longitude;

    // Liste des produits disponibles dans cette pharmacie
    @ManyToMany
    @JoinTable(
        name = "pharmacie_produit",
        joinColumns = @JoinColumn(name = "pharmacie_id"),
        inverseJoinColumns = @JoinColumn(name = "produit_id")
    )
    private List<Produit> produits;

    // ----- Getters & Setters -----

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

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public boolean isPharmacieDeGarde() {
        return pharmacieDeGarde;
    }

    public void setPharmacieDeGarde(boolean pharmacieDeGarde) {
        this.pharmacieDeGarde = pharmacieDeGarde;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public List<Produit> getProduits() {
        return produits;
    }

    public void setProduits(List<Produit> produits) {
        this.produits = produits;
    }
}
