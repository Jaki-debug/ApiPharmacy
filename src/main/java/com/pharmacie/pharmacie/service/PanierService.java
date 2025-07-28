package com.pharmacie.pharmacie.service;

import java.util.List;
import java.util.Optional;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;
import com.pharmacie.pharmacie.model.Panier;
import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.model.StatutPanier;
import com.pharmacie.pharmacie.model.FactureItem;
import com.pharmacie.pharmacie.repository.PanierRepository;
import com.pharmacie.pharmacie.repository.ProduitRepository;
import com.pharmacie.pharmacie.repository.FactureItemRepository;
import com.pharmacie.pharmacie.service.UtilisateurService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;


@Service
public class PanierService {

    @Autowired
    private PanierRepository panierRepository;

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private FactureItemRepository factureItemRepository;

    @Autowired
    private UtilisateurService utilisateurService;

    private static final Logger logger = LoggerFactory.getLogger(PanierService.class);

    @Transactional
    public Panier ajouterProduitAuPanier(Integer panierId, Long produitId, int quantite) {
        Optional<Panier> optionalPanier = panierRepository.findById(panierId);
        Optional<Produit> optionalProduit = produitRepository.findById(produitId);

        if (optionalPanier.isEmpty() || optionalProduit.isEmpty()) {
            logger.error("Panier ou Produit introuvable: panierId = {}, produitId = {}", panierId, produitId);
            throw new IllegalArgumentException("Panier ou Produit introuvable");
        }

        Panier panier = optionalPanier.get();
        Produit produit = optionalProduit.get();

        // Vérification du stock disponible
        if (produit.getStock() < quantite) {
            logger.error("Stock insuffisant pour produitId = {}. Quantité demandée: {}, Stock disponible: {}", produitId, quantite, produit.getStock());
            throw new IllegalArgumentException("Stock insuffisant");
        }

        panier.ajouterProduit(produit, quantite);  // Utilise ta logique propre dans Panier.java

        produit.setStock(produit.getStock() - quantite);

        produitRepository.save(produit);
        panierRepository.save(panier);

        return panier;


        
    }

    // ✅ Méthode pour récupérer un panier par ID
    public Panier getPanierById(Integer id) {
        Optional<Panier> panier = panierRepository.findById(id);
        return panier.orElse(null);
    }

    // Méthode pour trouver un panier par ID
    public Panier findPanierById(Integer panierId) {
        return panierRepository.findById(panierId)
                .orElseThrow(() -> new IllegalArgumentException("Panier non trouvé"));
    }

    // Méthode pour obtenir le total du panier
    public BigDecimal getTotalPanier(Integer panierId) {
        Panier panier = findPanierById(panierId);
        return panier.getTotal();
    }

   

    // Méthode pour obtenir un panier en fonction de l'utilisateur
    public Panier findPanierByUtilisateurId(Integer utilisateurId) {
        return panierRepository.findByUtilisateur_Id(utilisateurId);
    }

  
    // Méthode pour supprimer tous les paniers en cours d'un utilisateur
    public void supprimerPaniersEnCours(Integer utilisateurId) {
        // Suppression de tous les paniers en cours pour l'utilisateur spécifié
        panierRepository.deleteByUtilisateur_IdAndStatut(utilisateurId, StatutPanier.EN_COURS);
        logger.info("Tous les paniers en cours ont été supprimés pour l'utilisateur avec ID: " + utilisateurId);
    }
    
    
 // Méthode pour obtenir un panier en cours par utilisateur
 // Méthode pour obtenir un panier en cours par utilisateur
    public Panier findPanierEnCoursByUtilisateurId(Integer utilisateurId) {
        // Recherche du panier en cours pour l'utilisateur
        Optional<Panier> panierEnCours = panierRepository.findByUtilisateur_IdAndStatut(utilisateurId, StatutPanier.EN_COURS);

        // Si aucun panier en cours n'est trouvé, retourner null ou gérer autrement
        return panierEnCours.orElse(null);  // Retourne null si aucun panier n'est trouvé
    }

    
    
    // Méthode pour créer un panier si aucun panier en cours n'est trouvé
    public Panier creerPanierSiAbsent(Integer utilisateurId) {
        // Recherche du panier en cours
        Optional<Panier> panierOptional = panierRepository.findByUtilisateur_IdAndStatut(utilisateurId, StatutPanier.EN_COURS);

        // Si aucun panier n'est trouvé, on crée un nouveau panier
        if (panierOptional.isEmpty()) {
            Panier panier = new Panier();
            panier.setUtilisateur(utilisateurService.findUtilisateurById(utilisateurId));
            panier.setStatut(StatutPanier.EN_COURS);  // Définir le statut comme "EN_COURS"
            panierRepository.save(panier);  // Sauvegarder le panier dans la base de données
            logger.info("Panier créé automatiquement pour l'utilisateur avec ID: {}", utilisateurId);
            return panier;  // Retourner le panier créé
        }

        // Si un panier existe déjà, on le retourne
        return panierOptional.get();
    }

    
 // Méthode pour vider le contenu du panier
    @Transactional
    public void viderContenuPanier(Integer panierId) throws Exception {
        // Récupérer le panier depuis la base de données
        Panier panier = panierRepository.findById(panierId)
                .orElseThrow(() -> new IllegalArgumentException("Panier non trouvé pour l'ID: " + panierId));
        
        // Vider le contenu du panier (exemple: supprimer tous les articles)
        panier.getFactureItems().clear();  // Assurez-vous que "factureItems" représente les éléments du panier dans votre modèle
        
        // Sauvegarder les modifications dans la base de données
        panierRepository.save(panier);
    }

    
    
    
    
    
    // Trouver un panier par ID
    public Panier findById(Integer panierId) {
        return panierRepository.findById(panierId).orElse(null);
    }

    // Sauvegarder un panier
    public Panier save(Panier panier) {
        return panierRepository.save(panier);
    }

    // Méthode pour valider le panier
    public Panier validerPanier(Integer panierId) {
        Panier panier = findById(panierId);
        if (panier == null) {
            throw new IllegalArgumentException("Panier non trouvé.");
        }

        // Vérification si le panier est déjà validé
        if (panier.getStatut() == StatutPanier.VALIDÉ) {
            throw new IllegalStateException("Le panier est déjà validé.");
        }

        // Modifier le statut du panier en "VALIDÉ"
        panier.setStatut(StatutPanier.VALIDÉ);
        return save(panier); // Sauvegarder le panier avec le nouveau statut
    }
    
    
 // Méthode pour récupérer tous les paniers
    public List<Panier> findTousLesPaniers() {
        // On récupère tous les paniers de la base de données
        return panierRepository.findAll();
    }
    
    
}
