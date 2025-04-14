package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.Produit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

   
    public void sendLowStockNotification(Produit produit) {
        String message = "Le produit " + produit.getNom() + " a un stock faible : " + produit.getStock() + " restant.";
        messagingTemplate.convertAndSend("/topic/alerts", message);
    }
}
