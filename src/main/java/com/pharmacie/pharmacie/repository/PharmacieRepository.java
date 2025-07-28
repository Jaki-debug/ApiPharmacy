package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.model.Pharmacie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PharmacieRepository extends JpaRepository<Pharmacie, Long> {

    // 🔹 Pour l'authentification
    Optional<Pharmacie> findByEmail(String email);

    // 🔹 Trouver toutes les pharmacies de garde
    List<Pharmacie> findByPharmacieDeGardeTrue();

    // 🔹 Pharmacies de garde qui ont un produit spécifique
    @Query("SELECT p FROM Pharmacie p JOIN p.produits prod WHERE p.pharmacieDeGarde = true AND prod.id = :produitId")
    List<Pharmacie> findPharmaciesDeGardeParProduit(@Param("produitId") Long produitId);

    // 🔹 Supprimer toutes les pharmacies de garde (ex : reset)
    void deleteAllByPharmacieDeGardeTrue();

    // 🔹 Trouver les pharmacies de garde proches (géolocalisation)
    @Query(value = "SELECT * FROM pharmacie p WHERE p.pharmacie_de_garde = true AND ("
            + "6371 * acos("
            + "cos(radians(:latitude)) * cos(radians(p.latitude)) * "
            + "cos(radians(p.longitude) - radians(:longitude)) + "
            + "sin(radians(:latitude)) * sin(radians(p.latitude))"
            + ")) <= :distanceKm", nativeQuery = true)
    List<Pharmacie> findPharmaciesDeGardeProches(
            @Param("latitude") double latitude,
            @Param("longitude") double longitude,
            @Param("distanceKm") double distanceKm);
}
