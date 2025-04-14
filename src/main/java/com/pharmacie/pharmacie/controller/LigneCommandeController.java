package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.LigneCommande;
import com.pharmacie.pharmacie.service.LigneCommandeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/lignes-commande")
public class LigneCommandeController {

    private final LigneCommandeService ligneCommandeService;

    public LigneCommandeController(LigneCommandeService ligneCommandeService) {
        this.ligneCommandeService = ligneCommandeService;
    }
 // Enregistrer une ligne de commande
    @PostMapping
    public ResponseEntity<LigneCommande> enregistrerLigneCommande(@RequestBody LigneCommande ligneCommande) {
        LigneCommande ligneCommandeEnregistree = ligneCommandeService.enregistrerLigneCommande(ligneCommande);
        return ResponseEntity.status(HttpStatus.CREATED).body(ligneCommandeEnregistree);
    }

    
    // Récupérer toutes les lignes de commande
    @GetMapping
    public ResponseEntity<List<LigneCommande>> getAllLignesCommande() {
        List<LigneCommande> lignesCommande = ligneCommandeService.getAllLignesCommande();
        return ResponseEntity.ok(lignesCommande);
    }

    // Récupérer les lignes de commande pour une commande spécifique (par ID de commande)
    @GetMapping("/commande/{commandeId}")
    public ResponseEntity<List<LigneCommande>> getLignesCommandeByCommandeId(@PathVariable Long commandeId) {
        List<LigneCommande> lignesCommande = ligneCommandeService.getLignesCommandeByCommandeId(commandeId);
        return ResponseEntity.ok(lignesCommande);
    }
    
    
    @GetMapping("/produits/{commandeId}")
    public List<Object[]> getProduitsParCommande(@PathVariable Long commandeId) {
        return ligneCommandeService.getProduitsParCommande(commandeId);
    }
}



