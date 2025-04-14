package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.Commande;
import com.pharmacie.pharmacie.model.Panier;
import com.pharmacie.pharmacie.service.CommandeService;
import com.pharmacie.pharmacie.service.PanierService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private PanierService panierService;  // Service pour récupérer le panier

    // ✅ Passer une commande à partir d’un panier validé
    @PostMapping("/passer/{panierId}")
    public ResponseEntity<?> passerCommande(@PathVariable Integer panierId) {  // Utiliser Integer ici
        try {
            // Vérifier si le panier existe
            Panier panier = panierService.findById(panierId);  // Utiliser Integer ici
            if (panier == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Panier non trouvé.");
            }

            // Vérifier si le panier est validé
            if (!panier.estValide()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le panier n'est pas validé. Impossible de passer la commande.");
            }

            // Passer la commande si le panier est validé
            Commande commande = commandeService.passerCommande(panierId);  // Utiliser Integer ici
            if (commande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Erreur lors du passage de la commande.");
            }
            return ResponseEntity.ok("Commande passée avec succès. ID: " + commande.getId());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur lors du passage de la commande: " + e.getMessage());
        }
    }

    // ✅ Récupérer toutes les commandes
    @GetMapping("/toutes")
    public ResponseEntity<List<Commande>> getToutesLesCommandes() {
        List<Commande> commandes = commandeService.findAll();
        return ResponseEntity.ok(commandes);
    }

    // ✅ Récupérer une commande par ID
    @GetMapping("/{commandeId}")
    public ResponseEntity<?> getCommandeById(@PathVariable Long commandeId) {
        Commande commande = commandeService.findById(commandeId);
        if (commande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande non trouvée.");
        }
        return ResponseEntity.ok(commande);
    }

    // ✅ Récupérer toutes les lignes de commande d'une commande donnée
    @GetMapping("/{commandeId}/lignes")
    public ResponseEntity<?> getLignesCommande(@PathVariable Long commandeId) {
        Commande commande = commandeService.findById(commandeId);
        if (commande == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande non trouvée.");
        }
        return ResponseEntity.ok(commande.getLignesCommande());
    }
    
    
    
    
    
    
}
