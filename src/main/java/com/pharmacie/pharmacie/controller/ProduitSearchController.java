package com.pharmacie.pharmacie.controller;
import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.service.ProduitSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/api/produits")
public class ProduitSearchController {

    @Autowired
    private ProduitSearchService produitSearchService;

    @GetMapping("/searcher")
    public ResponseEntity<List<Produit>> searchProducts(
        @RequestParam(required = false) String nom,
        @RequestParam(required = false) String categorie,
        @RequestParam(required = false) Long prixMin,
        @RequestParam(required = false) Long prixMax,
        @RequestParam(required = false) String date,
        @RequestParam(required = false) Integer stock
    ) {
        
        BigDecimal prixMinBigDecimal = (prixMin != null) ? new BigDecimal(prixMin) : null;
        BigDecimal prixMaxBigDecimal = (prixMax != null) ? new BigDecimal(prixMax) : null;

        
        List<Produit> produits = produitSearchService.advancedSearch(nom, categorie, prixMinBigDecimal, prixMaxBigDecimal, date, stock);
        return ResponseEntity.ok(produits);
    }
}
