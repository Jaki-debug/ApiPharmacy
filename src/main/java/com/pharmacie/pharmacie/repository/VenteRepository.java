package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.model.Vente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface VenteRepository extends JpaRepository<Vente, Long> {

    // obtenir  ventes mensuel
    @Query("SELECT SUM(v.total) FROM Vente v WHERE YEAR(v.dateVente) = :year AND MONTH(v.dateVente) = :month")
    BigDecimal findVentesMensuelles(@Param("year") int year, @Param("month") int month);

    // obttenir les ventes journalières pour un mois 
    @Query("SELECT v.dateVente, SUM(v.total) FROM Vente v WHERE YEAR(v.dateVente) = :year AND MONTH(v.dateVente) = :month GROUP BY v.dateVente ORDER BY v.dateVente")
    List<Object[]> findVentesJournalieres(@Param("year") int year, @Param("month") int month);
}
