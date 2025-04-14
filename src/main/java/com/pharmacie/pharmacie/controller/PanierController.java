package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.Panier;
import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.service.PanierService;
import com.pharmacie.pharmacie.service.ProduitService;
import com.pharmacie.pharmacie.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.pharmacie.pharmacie.model.StatutPanier;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.HttpStatus;
import com.pharmacie.pharmacie.repository.PanierRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/panier")
public class PanierController {

    @Autowired
    private PanierService panierService;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private UtilisateurService utilisateurService;

    // Logger pour afficher les messages de log
    private static final Logger logger = LoggerFactory.getLogger(PanierController.class);

    // Création d'un panier
    @PostMapping("/creer")
    public ResponseEntity<?> creerPanier(@RequestBody Integer utilisateurId) {
        try {
            // Supprimer tous les paniers en cours avant de créer un nouveau panier
            panierService.supprimerPaniersEnCours(utilisateurId);

            // Création d'un nouveau panier
            Panier panier = new Panier();
            panier.setUtilisateur(utilisateurService.findUtilisateurById(utilisateurId)); // Associer l'utilisateur
            panier.setStatut(StatutPanier.EN_COURS); // Définir le statut du panier comme "EN_COURS"
            panierService.save(panier); // Sauvegarder dans la base de données

            return ResponseEntity.ok("Panier créé avec succès. ID du panier: " + panier.getId());
        } catch (Exception e) {
            logger.error("Erreur lors de la création du panier: " + e.getMessage());
            return ResponseEntity.badRequest().body("Erreur: " + e.getMessage());
        }
    }
    // Méthode pour ajouter un produit au panier avec vérification du stock
    @PostMapping("/{panierId}/ajouter/{produitId}/{quantite}")
    public ResponseEntity<Map<String, Object>> ajouterProduitAuPanier(
            @PathVariable Long panierId,  
            @PathVariable Long produitId, 
            @PathVariable int quantite) { 

        logger.info("Début de l'ajout d'un produit au panier: panierId = {}, produitId = {}, quantite = {}", panierId, produitId, quantite);

        // Récupérer le produit depuis la base de données
        Produit produit = produitService.findProduitById(produitId);
        if (produit == null) {
            logger.error("Produit introuvable pour produitId = {}", produitId);
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Produit introuvable pour produitId = " + produitId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        logger.info("Produit trouvé: {}", produit);

        // Vérification du stock disponible
        if (produit.getStock() < quantite) {
            logger.warn("Stock insuffisant pour produitId = {}. Quantité demandée: {}, Stock disponible: {}", produitId, quantite, produit.getStock());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Stock insuffisant pour produitId = " + produitId + ". Quantité demandée: " + quantite + ", Stock disponible: " + produit.getStock());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Si le stock est suffisant, ajouter le produit au panier
        try {
            Panier panierMisAJour = panierService.ajouterProduitAuPanier(panierId.intValue(), produitId, quantite);
            logger.info("Produit ajouté avec succès au panierId = {}: produitId = {}, quantite = {}", panierId, produitId, quantite);

            // Réponse si l'ajout est réussi
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Produit ajouté avec succès");
            response.put("quantite_ajoutee", quantite);
            response.put("panier", panierMisAJour);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            logger.error("Erreur lors de l'ajout du produit au panierId = {}: produitId = {}, quantite = {}. Exception: {}", panierId, produitId, quantite, e.getMessage());
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Erreur lors de l'ajout du produit au panier.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    // Méthode pour obtenir le panier en cours de l'utilisateur
    @GetMapping("/utilisateur/{utilisateurId}")
    public ResponseEntity<?> getPanierEnCours(@PathVariable Long utilisateurId) {
        try {
            logger.info("Tentative de récupération du panier en cours pour utilisateurId = {}", utilisateurId);

            Integer utilisateurIdInteger = utilisateurId.intValue();
            
            // Récupérer ou créer automatiquement le panier si aucun n'existe
            Panier panier = panierService.creerPanierSiAbsent(utilisateurIdInteger);  

            logger.info("Panier trouvé ou créé pour utilisateurId = {}: {}", utilisateurIdInteger, panier);
            return ResponseEntity.ok(panier);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du panier en cours pour utilisateurId = {}: Exception: {}", utilisateurId, e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Erreur : " + e.getMessage());
        }
    }


    
    
    
    
    @DeleteMapping("/{panierId}")
    public ResponseEntity<Map<String, Object>> viderContenuPanier(@PathVariable Integer panierId) {
        try {
            // Appeler la méthode pour vider le contenu du panier
            panierService.viderContenuPanier(panierId);

            // Réponse si la suppression du contenu est réussie
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Contenu du panier vidé avec succès.");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Gérer les erreurs lorsque le panier n'est pas trouvé
            Map<String, Object> response = new HashMap<>();
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (Exception e) {
            // Gérer les erreurs internes
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Erreur lors de la suppression du contenu du panier.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    
    
    
    
    
    @PostMapping("/valider/{panierId}")
    public ResponseEntity<Map<String, Object>> validerPanier(@PathVariable Integer panierId) {
        try {
            Panier panier = panierService.findById(panierId);
            
            // Vérifier si le panier existe
            if (panier == null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Panier non trouvé.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            // Vérifier si le panier est vide (aucun produit dans le panier)
            if (panier.getFactureItems() == null || panier.getFactureItems().isEmpty()) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Le panier est vide. Impossible de valider un panier sans produits.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Vérifier si le panier est déjà validé
            if (panier.getStatut() == StatutPanier.VALIDÉ) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Le panier est déjà validé.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            // Modifier le statut du panier en "VALIDÉ"
            panier.setStatut(StatutPanier.VALIDÉ);
            panierService.save(panier);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Panier validé avec succès.");
            response.put("panier", panier);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Erreur lors de la validation du panier: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    

    }

    
    

    // Méthode pour récupérer tous les paniers de tous les utilisateurs
    @GetMapping("/toutpanier")
    public ResponseEntity<?> getTousLesPaniers() {
        try {
            // Récupérer tous les paniers
            List<Panier> paniers = panierService.findTousLesPaniers();

            if (paniers.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun panier trouvé.");
            }

            return ResponseEntity.ok(paniers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Erreur lors de la récupération des paniers: " + e.getMessage());
        }
    }
    }
