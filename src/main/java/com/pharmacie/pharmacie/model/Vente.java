package com.pharmacie.pharmacie.model;




import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
public class Vente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "produit_id", nullable = false)
    private Produit produit;

    @Column(nullable = false)
    private Integer quantite;

    @Column(nullable = false)
    private BigDecimal total;

    @Column(nullable = false)
    private LocalDate dateVente;

    // Constructeurs
    public Vente() {}

    public Vente(Produit produit, Integer quantite, BigDecimal total) {
        this.produit = produit;
        this.quantite = quantite;
        this.total = total;
        this.dateVente = LocalDate.now();
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Produit getProduit() { return produit; }
    public void setProduit(Produit produit) { this.produit = produit; }

    public Integer getQuantite() { return quantite; }
    public void setQuantite(Integer quantite) { this.quantite = quantite; }

    public BigDecimal getTotal() { return total; }
    public void setTotal(BigDecimal total) { this.total = total; }

    public LocalDate getDateVente() { return dateVente; }
    public void setDateVente(LocalDate dateVente) { this.dateVente = dateVente; }
}
