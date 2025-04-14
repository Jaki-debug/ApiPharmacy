package com.pharmacie.pharmacie.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "factures")
public class Facture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "facture_id")
    private List<FactureItem> factureItems;

    @Column(precision = 10, scale = 2)
    private BigDecimal total;

    private String date;

    // Constructeur sans paramètre nécessaire pour JPA
    public Facture() {
        this.date = LocalDate.now().toString();
    }

    // Constructeur existant
    public Facture(List<FactureItem> factureItems, BigDecimal total) {
        this.factureItems = factureItems;
        this.total = total;
        this.date = LocalDate.now().toString();
    }
    
    // Constructeur pour accepter un id de type long, une liste de FactureItem et un total en double
    public Facture(long id, List<FactureItem> factureItems, double total) {
        this.id = id;
        this.factureItems = factureItems;
        this.total = BigDecimal.valueOf(total);
        this.date = LocalDate.now().toString();
    }
    
    // Nouveau constructeur pour accepter un id de type Long, une liste de FactureItem et un total en BigDecimal
    public Facture(Long id, List<FactureItem> factureItems, BigDecimal total) {
        this.id = id;
        this.factureItems = factureItems;
        this.total = total;
        this.date = LocalDate.now().toString();
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<FactureItem> getFactureItems() {
        return factureItems;
    }

    public void setFactureItems(List<FactureItem> factureItems) {
        this.factureItems = factureItems;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        StringBuilder factureDetails = new StringBuilder("Facture{id=" + id + ", date=" + date + ", factureItems=[");
        for (FactureItem item : factureItems) {
            factureDetails.append(item.toString()).append(", ");
        }
        factureDetails.delete(factureDetails.length() - 2, factureDetails.length());
        factureDetails.append("], total=").append(total).append("}");
        return factureDetails.toString();
    }
}
