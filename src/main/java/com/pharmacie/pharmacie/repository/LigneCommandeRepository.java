package com.pharmacie.pharmacie.repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.pharmacie.pharmacie.model.LigneCommande;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface LigneCommandeRepository extends JpaRepository<LigneCommande, Long> {
	
	
	  // Trouver toutes les lignes de commande par ID de commande
    List<LigneCommande> findByCommandeId(Long commandeId);
    
    
    @Query("SELECT p.nom, lc.quantite, lc.prixUnitaire, (lc.prixUnitaire * lc.quantite) " +
            "FROM LigneCommande lc JOIN lc.produit p " +
            "WHERE lc.commande.id = :commandeId")
     List<Object[]> findProduitsByCommandeId(@Param("commandeId") Long commandeId);
     

}
