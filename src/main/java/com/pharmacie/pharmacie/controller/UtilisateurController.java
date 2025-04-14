package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.Utilisateur;
import com.pharmacie.pharmacie.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/utilisateur")
public class UtilisateurController {

    @Autowired
    private UtilisateurService utilisateurService;

    @PostMapping("/creer")
    public ResponseEntity<String> creerUtilisateur(@RequestBody Utilisateur utilisateur) {
        return ResponseEntity.ok("Utilisateur créé !");
    }


    @GetMapping("/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable Integer id) {  // Changed Long to Integer
        Optional<Utilisateur> utilisateur = utilisateurService.getUtilisateurById(id);
        if (utilisateur.isPresent()) {
            return new ResponseEntity<>(utilisateur.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public List<Utilisateur> getAllUtilisateurs() {
        return utilisateurService.getTousLesUtilisateurs();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Utilisateur> updateUtilisateur(@PathVariable Integer id, @RequestBody Utilisateur utilisateur) {  // Changed Long to Integer
        Optional<Utilisateur> utilisateurUpdated = utilisateurService.updateUtilisateur(id, utilisateur);
        if (utilisateurUpdated.isPresent()) {
            return new ResponseEntity<>(utilisateurUpdated.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUtilisateur(@PathVariable Integer id) {  // Changed Long to Integer
        boolean isDeleted = utilisateurService.deleteUtilisateur(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
