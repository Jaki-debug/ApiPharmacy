package com.pharmacie.pharmacie.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;
import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.repository.ProduitRepository;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private ProduitRepository produitRepository;

    // Envoi manuel de notification avec HTTP POST
    @PostMapping("/send")
    public void sendManualNotification(@RequestBody String message) {
        messagingTemplate.convertAndSend("/topic/alerts", message);
    }

    // envois auto de notification si stock bas
    @GetMapping("/check-stock")
    public void checkStock() {
        int seuilBas = 5; // Le seuil de stock bas
        List<Produit> produitsEnRupture = produitRepository.findByStockLessThan(seuilBas);

        if (!produitsEnRupture.isEmpty()) {
            StringBuilder message = new StringBuilder("⚠️ Attention, stock bas pour : ");
            for (Produit p : produitsEnRupture) {
                message.append(p.getNom())
                        .append(" (Stock : ")
                        .append(p.getStock())
                        .append(") ");
            }
            // Envoi de la notification
            messagingTemplate.convertAndSend("/topic/alerts", message.toString());
        }
    }

    // verification le stock toutes les 5 minutes
    @Scheduled(fixedRate = 300000)  // 300000 ms = 5 minutes
    public void checkStockPeriodically() {
        checkStock();  
    }

    // Recevoi d'un message depuis le client 
    @MessageMapping("/receive")
    @SendTo("/topic/alerts")
    public String receiveMessage(String message) {
        return "Message reçu : " + message;
    }
}
