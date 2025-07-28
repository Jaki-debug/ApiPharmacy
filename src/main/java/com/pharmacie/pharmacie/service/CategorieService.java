package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.Categorie;
import com.pharmacie.pharmacie.repository.CategorieRepository;
import com.pharmacie.pharmacie.repository.ProduitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategorieService {

    @Autowired
    private CategorieRepository categorieRepository;

    @Autowired
    private ProduitRepository produitRepository;

    // Récupérer toutes les catégories
    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    // Récupérer une catégorie par ID
    public Optional<Categorie> getCategorieById(Long id) {
        return categorieRepository.findById(id);
    }

    // Créer ou mettre à jour une catégorie
    public Categorie saveCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    // Supprimer une catégorie par ID avec vérification
    public void deleteCategorie(Long id) {
        boolean estUtilisee = produitRepository.existsByCategorie_Id(id);

        if (estUtilisee) {
            throw new IllegalStateException("Impossible de supprimer : des produits sont liés à cette catégorie.");
        }

        if (!categorieRepository.existsById(id)) {
            throw new RuntimeException("Catégorie introuvable.");
        }

        categorieRepository.deleteById(id);
    }
}
