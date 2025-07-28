package com.pharmacie.pharmacie.dto;

public class PharmacieInscriptionDTO {

    private String nom;
    private String email;
    private String motDePasse;
    private String adresse;
    private String telephone;
    private boolean pharmacieDeGarde;
    private Double latitude;
    private Double longitude;

    // Getters et setters

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
}
