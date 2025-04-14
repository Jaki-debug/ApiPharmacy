package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.Categorie;
import com.pharmacie.pharmacie.service.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/categories")
public class CategorieController {

    @Autowired
    private ProduitService produitService;

    // Récupérer toutes les catégories
    @GetMapping
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = produitService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Long id) {
        Categorie categorie = produitService.getAllCategories().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (categorie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(categorie, HttpStatus.OK);
    }

    // Créer une nouvelle catégorie
    @PostMapping
    public ResponseEntity<Categorie> createCategorie(@RequestBody Categorie categorie) {
        Categorie savedCategorie = produitService.saveCategorie(categorie);
        return new ResponseEntity<>(savedCategorie, HttpStatus.CREATED);
    }

    // Mettre à jour une catégorie existante
    @PutMapping("/{id}")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable Long id, @RequestBody Categorie categorie) {
        Categorie existingCategorie = produitService.getAllCategories().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (existingCategorie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        existingCategorie.setNom(categorie.getNom());  // Mise à jour des attributs
        Categorie updatedCategorie = produitService.saveCategorie(existingCategorie);
        return new ResponseEntity<>(updatedCategorie, HttpStatus.OK);
    }

    // Supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategorie(@PathVariable Long id) {
        Categorie categorie = produitService.getAllCategories().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);

        if (categorie == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        produitService.deleteCategorie(id);  // Suppression de la catégorie
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
