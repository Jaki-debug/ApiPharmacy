package com.pharmacie.pharmacie.dto;

import com.pharmacie.pharmacie.model.StatutPanier;
import java.math.BigDecimal;
import java.util.List;

public class PanierDTO {

    private Long id;
    private BigDecimal total;
    private StatutPanier statut;
    private Long utilisateurId;
    private List<FactureItemDTO> factureItems;

    // Constructeur
    public PanierDTO(Long id, BigDecimal total, StatutPanier statut, Long utilisateurId, List<FactureItemDTO> factureItems) {
        this.id = id;
        this.total = total;
        this.statut = statut;
        this.utilisateurId = utilisateurId;
        this.factureItems = factureItems;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public StatutPanier getStatut() {
        return statut;
    }

    public void setStatut(StatutPanier statut) {
        this.statut = statut;
    }

    public Long getUtilisateurId() {
        return utilisateurId;
    }

    public void setUtilisateurId(Long utilisateurId) {
        this.utilisateurId = utilisateurId;
    }

    public List<FactureItemDTO> getFactureItems() {
        return factureItems;
    }

    public void setFactureItems(List<FactureItemDTO> factureItems) {
        this.factureItems = factureItems;
    }
}
