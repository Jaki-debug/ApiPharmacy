package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.model.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.math.BigDecimal;


public interface ProduitSearchRepository extends JpaRepository<Produit, Long> {

    @Query("SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))")
    List<Produit> searchByName(@Param("nom") String nom);

    @Query("SELECT p FROM Produit p WHERE p.categorie = :categorie")
    List<Produit> searchByCategory(@Param("categorie") String categorie);

    @Query("SELECT p FROM Produit p WHERE p.prixUnitaire BETWEEN :minPrice AND :maxPrice")
    List<Produit> searchByPriceRange(@Param("minPrice") BigDecimal minPrice, @Param("maxPrice") BigDecimal maxPrice);


    @Query("SELECT p FROM Produit p WHERE p.dateExpiration <= :dateExpiration")
    List<Produit> findByDateExpirationBefore(@Param("dateExpiration") LocalDate dateExpiration);

    @Query("SELECT p FROM Produit p WHERE p.stock >= :stock")
    List<Produit> searchByStock(@Param("stock") Integer stock);

    @Query("SELECT p FROM Produit p WHERE LOWER(p.nom) LIKE LOWER(CONCAT('%', :keyword, '%')) "
            + "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Produit> searchByKeyword(@Param("keyword") String keyword);

    @Query("SELECT p FROM Produit p WHERE "
            + "(:nom IS NULL OR LOWER(p.nom) LIKE LOWER(CONCAT('%', :nom, '%'))) "
            + "AND (:categorie IS NULL OR p.categorie = :categorie) "
            + "AND (:prixMin IS NULL OR p.prixUnitaire >= :prixMin) "  // Changer p.prix en p.prixUnitaire
            + "AND (:prixMax IS NULL OR p.prixUnitaire <= :prixMax) "  // Changer p.prix en p.prixUnitaire
            + "AND (:dateExpiration IS NULL OR p.dateExpiration = :dateExpiration) "
            + "AND (:stock IS NULL OR p.stock >= :stock)")
    List<Produit> findByCriteria(
            @Param("nom") String nom,
            @Param("categorie") String categorie,
            @Param("prixMin") BigDecimal prixMin,  // Modifier le type en BigDecimal
            @Param("prixMax") BigDecimal prixMax,  // Modifier le type en BigDecimal
            @Param("dateExpiration") LocalDate dateExpiration,
            @Param("stock") Integer stock);
    
    
    
}



