package com.pharmacie.pharmacie.repository;
import java.util.List;
import com.pharmacie.pharmacie.model.Panier;
import com.pharmacie.pharmacie.model.StatutPanier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PanierRepository extends JpaRepository<Panier, Integer> {

    // Trouver un panier par l'ID de l'utilisateur
    Panier findByUtilisateur_Id(Integer utilisateurId);

    // Trouver le panier en cours d'un utilisateur (un seul panier en cours)
    Optional<Panier> findByUtilisateur_IdAndStatut(Integer utilisateurId, StatutPanier statut);

    // Méthode pour supprimer les paniers en cours par utilisateurId
    void deleteByUtilisateur_IdAndStatut(Integer utilisateurId, StatutPanier statut);
    List<Panier> findByUtilisateurIdAndStatut(Integer utilisateurId, StatutPanier statut);
    // Méthode pour récupérer tous les paniers sans filtrer par utilisateur
    List<Panier> findAll();
}
