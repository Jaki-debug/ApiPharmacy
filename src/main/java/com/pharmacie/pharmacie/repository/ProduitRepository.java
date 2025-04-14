package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ProduitRepository extends JpaRepository<Produit, Long> {
    
    Optional<Produit> findByNom(String nom);
    
    List<Produit> findByNomContainingIgnoreCase(String nom);

    List<Produit> findByPrixUnitaireBetween(Double minPrix, Double maxPrix);

    List<Produit> findByStockGreaterThanEqual(Integer stock);
    
    List<Produit> findByCategorie_Nom(String categorieNom);
    
    List<Produit> findByDateExpirationBefore(LocalDate date);

    List<Produit> findByStockLessThan(Integer seuil);
    
    Optional<Produit> findByImagePath(String imagePath);
    
    List<Produit> findByDateExpirationBeforeOrDateExpirationBetween(LocalDate startDate, LocalDate endDate, LocalDate thresholdDate);
    
    // Recherche avec mot-clé (nom), prix et stock
    List<Produit> findByNomContainingIgnoreCaseAndPrixUnitaireBetweenAndStockGreaterThan(
        String keyword, Double minPrix, Double maxPrix, Integer minStock
    );

    // Recherche uniquement par prix et stock (sans mot-clé)
    List<Produit> findByPrixUnitaireBetweenAndStockGreaterThan(
        Double minPrix, Double maxPrix, Integer minStock
    );
    
    List<Produit> findByNomContainingIgnoreCaseOrCategorie_NomContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String nom, String categorieNom, String description
    );
    
    List<Produit> findByCategorie(Categorie categorie);
    
    
    long countByStockLessThan(int stock);
    long countByDateExpirationBefore(LocalDate date);
    long count();
    long countByDateExpirationBetween(LocalDate startDate, LocalDate endDate);

}
