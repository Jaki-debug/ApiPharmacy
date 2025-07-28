package com.pharmacie.pharmacie.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.service.ProduitService;
import com.pharmacie.pharmacie.model.Categorie;
import com.pharmacie.pharmacie.service.EmailService;
import com.pharmacie.pharmacie.service.NotificationService;
import com.pharmacie.pharmacie.repository.ProduitRepository;
import com.pharmacie.pharmacie.repository.CategorieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Map;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.time.format.DateTimeParseException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/produits")
public class ProduitController {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private ProduitService produitService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private NotificationService notificationService;

    // Ajout d’un produit
    @PostMapping("/ajouter")
    public ResponseEntity<?> addProduit(
            @RequestParam("nom") String nom,
            @RequestParam("categorieId") Long categorieId,
            @RequestParam("prixAchat") BigDecimal prixAchat,
            @RequestParam(value = "prixUnitaire", required = false) BigDecimal prixUnitaire,
            @RequestParam("stock") Integer stock,
            @RequestParam("dateExpiration") String dateExpiration,
            @RequestParam("description") String description,
            @RequestParam(value = "image", required = false) MultipartFile image) {

        try {
            Categorie categorie = categorieRepository.findById(categorieId)
                    .orElseThrow(() -> new IllegalArgumentException("Catégorie non trouvée"));

            LocalDate expirationDate;
            try {
                expirationDate = LocalDate.parse(dateExpiration);
            } catch (DateTimeParseException e) {
                return ResponseEntity.badRequest().body("Format de date invalide. Format attendu : yyyy-MM-dd");
            }

            Produit produit = new Produit(
                    nom,
                    categorie,
                    prixAchat,
                    prixUnitaire,
                    stock,
                    expirationDate,
                    description,
                    "default_image.png"
            );

            Produit savedProduit = produitService.addProduit(produit, image);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedProduit);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Une erreur est survenue lors de l'ajout du produit");
        }
    }

    // Recherche simple par mot-clé uniquement (route spécifique AVANT le {id})
    @GetMapping("/search/simple")
    public ResponseEntity<List<Produit>> searchProduitsSimple(
            @RequestParam(required = false, defaultValue = "") String keyword) {

        List<Produit> produits = produitService.searchProduits(keyword, 0.0, 10000.0, 0);

        if (produits.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(produits);
    }

    // Recherche avancée avec filtres
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

    // Récupérer tous les produits
    @GetMapping
    public List<Produit> getAllProduits() {
        return produitService.getAllProduits();
    }

    // Produits en stock faible
    @GetMapping("/low-stock")
    public ResponseEntity<List<Produit>> getLowStockProducts() {
        List<Produit> lowStockProducts = produitService.findLowStockProducts();

        for (Produit produit : lowStockProducts) {
            notificationService.sendLowStockNotification(produit);
        }

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

    // Obtenir un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable Long id) {
        Optional<Produit> produit = produitService.getProduitById(id);
        return produit.map(ResponseEntity::ok)
                      .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Mettre à jour un produit
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

    // Produits qui expirent bientôt
    @GetMapping("/expiring-soon")
    public ResponseEntity<List<Produit>> getExpiringProducts() {
        List<Produit> expiringProducts = produitService.findExpiringProducts();

        if (expiringProducts.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(expiringProducts);
    }

    // Récupérer une image produit
    @GetMapping("/images/{filename:.+}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path imagePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(imagePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // adapter selon le type d’image
                    .body(resource);
        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Statistiques
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Long>> getProduitStats() {
        Map<String, Long> stats = produitService.getProduitStats();
        return ResponseEntity.ok(stats);
    }

    // Produits par catégorie
    @GetMapping("/categorie/{categorieId}")
    public ResponseEntity<List<Produit>> getProduitsParCategorieId(@PathVariable Long categorieId) {
        List<Produit> produits = produitService.getProduitsParCategorieId(categorieId);
        if (produits.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(produits);
    }
}
