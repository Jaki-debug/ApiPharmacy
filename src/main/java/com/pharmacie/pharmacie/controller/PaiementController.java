package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.Commande;
import com.pharmacie.pharmacie.service.PaiementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/paiement")
public class PaiementController {

    @Autowired
    private PaiementService paiementService;

    // Crée une commande et génère la référence unique
    @PostMapping("/creer-commande")
    public ResponseEntity<Commande> creerCommande(@RequestBody Commande commande) {
        // Générer référence unique avant sauvegarde
        String reference = "CMD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        commande.setReference(reference);
        commande.setStatutPaiement("pending");

        Commande sauvegardee = paiementService.sauvegarderCommande(commande);
        return ResponseEntity.ok(sauvegardee);
    }

    // Endpoint IPN PayDunya - reçoit la notification de paiement
    @PostMapping("/notification")
    public ResponseEntity<String> recevoirNotification(@RequestBody Map<String, Object> payload) {
        String reference = (String) payload.get("custom_data"); // ou "invoice_token" selon PayDunya
        String statut = (String) payload.get("status"); // ex: completed, cancelled

        paiementService.trouverParReference(reference).ifPresent(commande -> {
            commande.setStatutPaiement(statut);
            paiementService.sauvegarderCommande(commande);
        });

        return ResponseEntity.ok("Notification reçue");
    }
}
