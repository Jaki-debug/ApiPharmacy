package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.FactureItem;
import com.pharmacie.pharmacie.repository.FactureItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FactureItemService {

    @Autowired
    private FactureItemRepository factureItemRepository;

    // 🔹 Créer un FactureItem
    public FactureItem createFactureItem(FactureItem factureItem) {
        return factureItemRepository.save(factureItem);
    }

    // 🔹 Récupérer un FactureItem par son ID
    public FactureItem getFactureItemById(Long id) {
        return factureItemRepository.findById(id).orElse(null);
    }

    // 🔹 Récupérer tous les FactureItems
    public List<FactureItem> getAllFactureItems() {
        return factureItemRepository.findAll();
    }

    // 🔹 Mettre à jour un FactureItem existant
    public FactureItem updateFactureItem(Long id, FactureItem updatedFactureItem) {
        Optional<FactureItem> existingFactureItemOptional = factureItemRepository.findById(id);

        if (existingFactureItemOptional.isPresent()) {
            FactureItem existingFactureItem = existingFactureItemOptional.get();
            
            // Mise à jour des champs nécessaires
            existingFactureItem.setProduit(updatedFactureItem.getProduit());
            existingFactureItem.setQuantite(updatedFactureItem.getQuantite());
            existingFactureItem.setPrixUnitaire(updatedFactureItem.getPrixUnitaire());
            
            return factureItemRepository.save(existingFactureItem);
        }

        return null;  // Retourne null si l'ID n'existe pas
    }

    // 🔹 Supprimer un FactureItem
    public boolean deleteFactureItem(Long id) {
        if (factureItemRepository.existsById(id)) {
            factureItemRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
