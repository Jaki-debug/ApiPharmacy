package com.pharmacie.pharmacie.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.service.ProduitService;
import com.pharmacie.pharmacie.model.Categorie;
import com.pharmacie.pharmacie.service.EmailService;
import com.pharmacie.pharmacie.service.NotificationService;
import com.pharmacie.pharmacie.repository.ProduitRepository; // Importer le repository
import com.pharmacie.pharmacie.repository.CategorieRepository; // Importer le repository Categorie
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import java.util.Map;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository; // Injection du repository Produit

    @Autowired
    private CategorieRepository categorieRepository; // Injection du repository Categorie

    @Autowired
    private ProduitService produitService; // Injection du service Produit

    @Autowired
    private EmailService emailService; // Injection du service Email

    @Autowired
    private NotificationService notificationService; // Injection du service Notification

    // Endpoint pour ajouter un produit
    @PostMapping("/ajouter")
    public ResponseEntity<Produit> addProduit(
            @RequestParam("nom") String nom,
            @RequestParam("categorieId") Long categorieId,
            @RequestParam("prixUnitaire") BigDecimal prixUnitaire,
            @RequestParam("stock") Integer stock,
            @RequestParam("dateExpiration") String dateExpiration,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        
        try {
            // Récupérer la catégorie depuis la base de données
            Categorie categorie = categorieRepository.findById(categorieId)
                    .orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée")); // Lancer une exception si la catégorie n'existe pas

            // Convertir la date d'expiration
            LocalDate expirationDate = LocalDate.parse(dateExpiration);

            // Créer un produit avec une image par défaut
            Produit produit = new Produit(nom, categorie, prixUnitaire, stock, expirationDate, description, "default_image.png");

            // Si une image est présente, l'enregistrer
            if (image != null && !image.isEmpty()) {
                String imagePath = produitService.saveImage(image); // Gérer l'upload de l'image
                produit.setImagePath(imagePath); // Mettre à jour le chemin de l'image
            }

            // Enregistrer le produit dans la base de données via le service
            Produit savedProduit = produitService.addProduit(produit, image); // Ajouter le produit avec l'image si présente

            // Retourner une réponse avec le produit ajouté
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduit);
        } catch (IllegalArgumentException e) {
            // Retourner une réponse d'erreur si la catégorie n'est pas trouvée
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            e.printStackTrace(); // Log l'exception pour le débogage
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Retourner une erreur interne
        }
    }



    // ✅ Méthode pour récupérer tous les produits
    @GetMapping
    public List<Produit> getAllProduits() {
        return produitService.getAllProduits();
    }

    // ✅ Méthode pour récupérer les produits en stock faible
    @GetMapping("/low-stock")
    public ResponseEntity<List<Produit>> getLowStockProducts() {
        int seuil = 5;  // Définition du seuil
        List<Produit> lowStockProducts = produitService.findLowStockProducts();

        // Envoyer des notifications pour les produits en stock faible
        for (Produit produit : lowStockProducts) {
            notificationService.sendLowStockNotification(produit);
        }

        // Retourner la réponse HTTP
        if (lowStockProducts.isEmpty()) {
            return ResponseEntity.noContent().build();  
        } else {
            return ResponseEntity.ok(lowStockProducts);  
        }
    }

    // Créer un produit
    @PostMapping
    public Produit createProduit(@RequestBody Produit produit) {
        return produitService.createProduit(produit);
    }

    // Obtenir un produit par son ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Optional<Produit> produit = produitService.getProduitById(id);
        return produit.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable Long id, @RequestBody Produit produit) {
        if (!produitRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        produit.setId(id);
        Produit updatedProduit = produitRepository.save(produit);
        return ResponseEntity.ok(updatedProduit);
    }


    // Supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable Long id) {
        boolean isDeleted = produitService.deleteProduit(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Récupérer les produits qui expirent bientôt
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<Produit>> getExpiringProducts() {
        List<Produit> expiringProducts = produitService.findExpiringProducts();

        if (expiringProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(expiringProducts);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Produit>> searchProduits(
        @RequestParam(required = false, defaultValue = "") String keyword, 
        @RequestParam(required = false, defaultValue = "0.0") Double minPrix, 
        @RequestParam(required = false, defaultValue = "10000.0") Double maxPrix, 
        @RequestParam(required = false, defaultValue = "0") Integer minStock) {

        if (minPrix < 0 || maxPrix < 0 || minStock < 0) {
            return ResponseEntity.badRequest().body(null); 
        }

        if (minPrix > maxPrix) {
            return ResponseEntity.badRequest().body(null); 
        }

        List<Produit> produits = produitService.searchProduits(keyword, minPrix, maxPrix, minStock);

        if (produits.isEmpty()) {
            return ResponseEntity.noContent().build(); 
        }

        return ResponseEntity.ok(produits); 
    }
    
    
    
    
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // ou IMAGE_PNG selon ton format
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    
    
    
    
    
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getProduitStats() {
        Map<String, Long> stats = produitService.getProduitStats();
        return ResponseEntity.ok(stats);
    }

    
}
