package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.repository.ProduitSearchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;


@Service
public class ProduitSearchService {

    @Autowired
    private ProduitSearchRepository produitSearchRepository;

    public List<Produit> searchByName(String nom) {
        return produitSearchRepository.searchByName(nom);
    }

    public List<Produit> searchByCategory(String categorie) {
        return produitSearchRepository.searchByCategory(categorie);
    }

    public List<Produit> searchByPriceRange(BigDecimal prixUnitaireMin, BigDecimal prixUnitaireMax) {
        return produitSearchRepository.searchByPriceRange(prixUnitaireMin, prixUnitaireMax);
    }


    public List<Produit> searchByDateExpiration(LocalDate dateExpiration) {
        return produitSearchRepository.findByDateExpirationBefore(dateExpiration);
    }

    public List<Produit> searchByStock(Integer stock) {
        return produitSearchRepository.searchByStock(stock);
    }

    public List<Produit> searchByKeyword(String keyword) {
        return produitSearchRepository.searchByKeyword(keyword);
    }

 // Recherche avancée a plusieurcritères
    public List<Produit> advancedSearch(String nom, String categorie, BigDecimal prixMin, BigDecimal prixMax, String date, Integer stock) {
        LocalDate dateExpiration = null;
        
     
        if (date != null && date.matches("\\d{4}-\\d{2}-\\d{2}")) {
            dateExpiration = LocalDate.parse(date);
        }

        
        return produitSearchRepository.findByCriteria(nom, categorie, prixMin, prixMax, dateExpiration, stock);
    }

}
