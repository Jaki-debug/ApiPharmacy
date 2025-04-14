package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.LigneCommande;
import com.pharmacie.pharmacie.repository.LigneCommandeRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LigneCommandeService {

    private final LigneCommandeRepository ligneCommandeRepository;

    // Injection via le constructeur
    public LigneCommandeService(LigneCommandeRepository ligneCommandeRepository) {
        this.ligneCommandeRepository = ligneCommandeRepository;
    }

    // Enregistrer une ligne de commande
    public LigneCommande enregistrerLigneCommande(LigneCommande ligneCommande) {
        return ligneCommandeRepository.save(ligneCommande);
    }

    // Récupérer toutes les lignes de commande
    public List<LigneCommande> getAllLignesCommande() {
        return ligneCommandeRepository.findAll();
    }

    

    // Récupérer les produits par commande
    public List<Object[]> getProduitsParCommande(Long commandeId) {
        return ligneCommandeRepository.findProduitsByCommandeId(commandeId);
    }

    public List<LigneCommande> getLignesCommandeByCommandeId(Long commandeId) {
        // Log pour vérifier l'appel du service
        System.out.println("Recherche des lignes pour la commande ID: " + commandeId);
        
        List<LigneCommande> lignes = ligneCommandeRepository.findByCommandeId(commandeId);
        
        // Log pour vérifier les résultats retournés
        System.out.println("Lignes trouvées : " + lignes.size());
        
        return lignes;
    }
}
