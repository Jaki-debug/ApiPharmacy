package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.Categorie;
import com.pharmacie.pharmacie.service.CategorieService;
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
    private CategorieService categorieService;

    // Récupérer toutes les catégories
    @GetMapping
    public ResponseEntity<List<Categorie>> getAllCategories() {
        List<Categorie> categories = categorieService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }

    // Récupérer une catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable Long id) {
        return categorieService.getCategorieById(id)
                .map(categorie -> new ResponseEntity<>(categorie, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Créer une nouvelle catégorie
    @PostMapping
    public ResponseEntity<Categorie> createCategorie(@RequestBody Categorie categorie) {
        Categorie savedCategorie = categorieService.saveCategorie(categorie);
        return new ResponseEntity<>(savedCategorie, HttpStatus.CREATED);
    }

    // Mettre à jour une catégorie existante
    @PutMapping("/{id}")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable Long id, @RequestBody Categorie categorie) {
        return categorieService.getCategorieById(id)
                .map(existingCategorie -> {
                    existingCategorie.setNom(categorie.getNom());
                    Categorie updatedCategorie = categorieService.saveCategorie(existingCategorie);
                    return new ResponseEntity<>(updatedCategorie, HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Supprimer une catégorie avec gestion de la règle métier
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCategorie(@PathVariable Long id) {
        try {
            categorieService.deleteCategorie(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalStateException e) {
            // Par exemple, si la catégorie contient encore des produits
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (RuntimeException e) {
            // Catégorie non trouvée
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
