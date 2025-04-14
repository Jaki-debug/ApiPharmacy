package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.Produit;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/commande")
public class CommandeVueController {

    @GetMapping
    public String afficherPageCommande(Model model) {
        
        model.addAttribute("produits", getProduits()); 
        return "commande"; // Nom de la vue Thymeleaf (commande.html)
    }

    private List<Produit> getProduits() {

        return List.of();
    }
}
