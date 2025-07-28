package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.dto.UtilisateurDTO;
import com.pharmacie.pharmacie.model.Utilisateur;
import com.pharmacie.pharmacie.service.UtilisateurService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UtilisateurService utilisateurService;

    public AuthController(UtilisateurService utilisateurService) {
        this.utilisateurService = utilisateurService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody UtilisateurDTO utilisateurDTO) {
        try {
            // Convertir DTO en entité Utilisateur
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setEmail(utilisateurDTO.getEmail());
            utilisateur.setMotDePasse(utilisateurDTO.getMotDePasse());
            utilisateur.setRole(utilisateurDTO.getRole());

            utilisateurService.registerUser(utilisateur);

            return ResponseEntity.ok("Inscription réussie !");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Erreur : " + e.getMessage());
        }
    }
}
