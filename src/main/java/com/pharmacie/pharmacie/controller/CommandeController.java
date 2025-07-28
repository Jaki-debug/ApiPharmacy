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
import com.pharmacie.pharmacie.model.StatutCommande;
import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDate;



@RestController
@RequestMapping("/api/commande")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    @Autowired
    private PanierService panierService;  // Service pour récupérer le panier

    // ✅ Passer une commande à partir d’un panier validé
    @PostMapping("/passer/{panierId}")
    public ResponseEntity<?> passerCommande(@PathVariable Integer panierId) {
        try {
            Panier panier = panierService.findById(panierId);
            if (panier == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Panier non trouvé.");
            }

            if (!panier.estValide()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Le panier n'est pas validé.");
            }

            Commande commande = commandeService.passerCommande(panierId);
            if (commande == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erreur lors du passage de la commande.");
            }

            // ✅ Retourner l’objet Commande pour que Angular puisse le parser
            return ResponseEntity.ok(commande);
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
    
    
    
    
    
    // AUJOURDHUI
 // ✅ Mettre à jour le statut d'une commande
 // Mise à jour du statut d'une commande
    @PutMapping("/{commandeId}/statut")
    public ResponseEntity<?> updateStatutCommande(
            @PathVariable Long commandeId,
            @RequestParam String statut) {
        try {
            StatutCommande statutCommande;
            try {
                statutCommande = StatutCommande.valueOf(statut.toUpperCase());
            } catch (IllegalArgumentException e) {
                return ResponseEntity.badRequest().body("Statut invalide: " + statut);
            }

            Commande commande = commandeService.updateStatutCommande(commandeId, statutCommande);
            if (commande == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Commande non trouvée");
            }
            return ResponseEntity.ok(commande);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erreur serveur: " + e.getMessage());
        }
    }

    
    
    
    
    
    @GetMapping("/stats")
    public Map<StatutCommande, Long> getStats() {
        return commandeService.countAllByStatut();
    }
    
    
    @GetMapping("/statistiques")
    public ResponseEntity<?> getStatistiques(
            @RequestParam String dateDebut,
            @RequestParam String dateFin,
            @RequestParam(defaultValue = "jour") String type
    ) {
        try {
            LocalDate debut = LocalDate.parse(dateDebut);
            LocalDate fin = LocalDate.parse(dateFin);

            Map<String, BigDecimal> stats = commandeService.getChiffreAffaireParPeriode(debut, fin, type);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Erreur : " + e.getMessage());
        }
    }
    
    
    @GetMapping("/stats/top3-categories")
    public ResponseEntity<List<Map<String, Object>>> getTop3Categories() {
        List<Map<String, Object>> stats = commandeService.getTop3CategoriesVente();
        return ResponseEntity.ok(stats);
    }
    
    @GetMapping("/stats/produits-populaires")
    public ResponseEntity<List<Map<String, Object>>> getProduitsPopulaires() {
        List<Map<String, Object>> populaires = commandeService.getProduitsPopulaires();
        return ResponseEntity.ok(populaires);
    }


}
