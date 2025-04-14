package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.model.Commande;
import com.pharmacie.pharmacie.model.Panier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.EntityGraph;
import com.pharmacie.pharmacie.model.StatutCommande;

import java.util.List;

public interface CommandeRepository extends JpaRepository<Commande, Long> {
    
    // Récupérer les commandes d'un pharmacien par son ID

    
    // Optionnellement, vous pouvez aussi filtrer par email
    List<Commande> findByPharmacienEmail(String email);
    
    // Récupérer les commandes d'un pharmacien triées par date
    List<Commande> findByPharmacienIdOrderByDateCommandeDesc(Long pharmacienId);
    List<Commande> findByStatut(StatutCommande statut);

    Commande findByPanier(Panier panier);
    List<Commande> findByPharmacienId(Long pharmacienId);
    Commande findByUtilisateurId(Long utilisateurId);
    
   
    
    
}
