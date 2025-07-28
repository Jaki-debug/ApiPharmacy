package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.dto.VenteMensuelleDTO;
import com.pharmacie.pharmacie.model.Commande;
import com.pharmacie.pharmacie.model.Panier;
import com.pharmacie.pharmacie.model.StatutCommande;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.util.Optional;
public interface CommandeRepository extends JpaRepository<Commande, Long> {

    List<Commande> findByPharmacienEmail(String email);
    Optional<Commande> findByReference(String reference);

    List<Commande> findByPharmacienIdOrderByDateCommandeDesc(Long pharmacienId);

    List<Commande> findByStatut(StatutCommande statut);

    Commande findByPanier(Panier panier);

    List<Commande> findByPharmacienId(Long pharmacienId);

    Commande findByUtilisateurId(Long utilisateurId);

    @Query("SELECT c FROM Commande c LEFT JOIN FETCH c.lignesCommande WHERE c.id = :id")
    Optional<Commande> findByIdWithLignes(@Param("id") Long id);

    // ✅ Requête native MySQL pour les ventes mensuelles (commandes LIVREE uniquement)
    @Query(value = """
    	    SELECT 
    	      DATE_FORMAT(c.date_commande, '%Y-%m') AS mois,
    	      SUM(l.quantite * l.prix_unitaire) AS total
    	    FROM commande c
    	    JOIN ligne_commande l ON l.commande_id = c.id
    	    WHERE c.statut = 'LIVREE'
    	    GROUP BY mois
    	    ORDER BY mois
    	    """, nativeQuery = true)
    	List<Object[]> findRawVentesMensuelles();
    	
    	 long countByStatut(StatutCommande statut);

    	    @Query("SELECT c.statut, COUNT(c) FROM Commande c GROUP BY c.statut")
    	    List<Object[]> countGroupByStatut();
    	    List<Commande> findByStatutAndDateCommandeBetween(
    	            StatutCommande statut,
    	            LocalDateTime dateDebut,
    	            LocalDateTime dateFin
    	        );
    	    
    	    
    	    @Query("""
    	    	    SELECT p.id, p.nom, SUM(lc.quantite) AS quantiteTotale
    	    	    FROM LigneCommande lc
    	    	    JOIN lc.produit p
    	    	    JOIN lc.commande c
    	    	    WHERE c.statut = com.pharmacie.pharmacie.model.StatutCommande.LIVREE
    	    	    GROUP BY p.id, p.nom
    	    	    ORDER BY quantiteTotale DESC
    	    	""")
    	    	List<Object[]> findProduitsPopulaires();


}
