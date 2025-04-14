package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.Commande;
import com.pharmacie.pharmacie.model.Panier;
import com.pharmacie.pharmacie.model.StatutCommande;  // Ajouter cet import
import com.pharmacie.pharmacie.model.StatutPanier;  // Ajouter cet import
import com.pharmacie.pharmacie.repository.CommandeRepository;
import com.pharmacie.pharmacie.repository.PanierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;  // Ajouter cet import pour la liste

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private PanierRepository panierRepository;

    // ✅ Passer une commande à partir d’un panier validé
    public Commande passerCommande(Integer panierId) {
        Panier panier = panierRepository.findById(panierId).orElse(null);
        if (panier == null || panier.getStatut() != StatutPanier.VALIDÉ) {
            return null;  // Panier introuvable ou non validé
        }

        Commande commande = new Commande();
        commande.setPanier(panier);
        commande.setUtilisateur(panier.getUtilisateur());
        commande.setStatut(StatutCommande.EN_ATTENTE);

        return commandeRepository.save(commande); // Sauvegarde la commande
    }

    // ✅ Trouver toutes les commandes
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }

    // ✅ Trouver une commande par ID
    public Commande findById(Long commandeId) {
        return commandeRepository.findById(commandeId).orElse(null);
    }
    
    
    
}
