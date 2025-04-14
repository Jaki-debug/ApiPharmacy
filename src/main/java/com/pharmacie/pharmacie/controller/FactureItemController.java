package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.model.FactureItem;
import com.pharmacie.pharmacie.service.FactureItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/facture-items")
public class FactureItemController {

    @Autowired
    private FactureItemService factureItemService;

    // Creer nouveau FactureItem
    @PostMapping
    public ResponseEntity<FactureItem> createFactureItem(@RequestBody FactureItem factureItem) {
        FactureItem createdFactureItem = factureItemService.createFactureItem(factureItem);
        return new ResponseEntity<>(createdFactureItem, HttpStatus.CREATED);
    }

    // Récupérer un FactureItem par son ID
    @GetMapping("/{id}")
    public ResponseEntity<FactureItem> getFactureItemById(@PathVariable("id") Long id) {
        FactureItem factureItem = factureItemService.getFactureItemById(id);
        if (factureItem != null) {
            return new ResponseEntity<>(factureItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Récupérer tous les FactureItems
    @GetMapping
    public ResponseEntity<List<FactureItem>> getAllFactureItems() {
        List<FactureItem> factureItems = factureItemService.getAllFactureItems();
        return new ResponseEntity<>(factureItems, HttpStatus.OK);
    }

    // Mettre à jour un FactureItem
    @PutMapping("/{id}")
    public ResponseEntity<FactureItem> updateFactureItem(@PathVariable("id") Long id, @RequestBody FactureItem factureItem) {
        FactureItem updatedFactureItem = factureItemService.updateFactureItem(id, factureItem);
        if (updatedFactureItem != null) {
            return new ResponseEntity<>(updatedFactureItem, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Supprimer un FactureItem
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFactureItem(@PathVariable("id") Long id) {
        boolean isDeleted = factureItemService.deleteFactureItem(id);
        if (isDeleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
