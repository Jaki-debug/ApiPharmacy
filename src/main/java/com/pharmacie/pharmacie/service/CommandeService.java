package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.dto.VenteMensuelleDTO;
import com.pharmacie.pharmacie.model.Commande;
import com.pharmacie.pharmacie.model.LigneCommande;
import com.pharmacie.pharmacie.model.Panier;
import com.pharmacie.pharmacie.model.StatutCommande;
import com.pharmacie.pharmacie.model.StatutPanier;
import com.pharmacie.pharmacie.repository.CommandeRepository;
import com.pharmacie.pharmacie.repository.PanierRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    @Autowired
    private CommandeRepository commandeRepository;

    @Autowired
    private PanierRepository panierRepository;

    // Passer une commande à partir d’un panier validé
    public Commande passerCommande(Integer panierId) {
        Panier panier = panierRepository.findById(panierId).orElse(null);
        if (panier == null || panier.getStatut() != StatutPanier.VALIDÉ) {
            return null;  // Panier introuvable ou non validé
        }

        Commande commande = new Commande();
        commande.setPanier(panier);
        commande.setUtilisateur(panier.getUtilisateur());
        commande.setStatut(StatutCommande.EN_ATTENTE);

        if (panier.getFactureItems() != null) {
            panier.getFactureItems().forEach(item -> {
                LigneCommande ligne = new LigneCommande();
                ligne.setProduit(item.getProduit());
                ligne.setQuantite(item.getQuantite());
                ligne.setPrixUnitaire(item.getPrixUnitaire());
                commande.addLigneCommande(ligne);
            });
        }

        return commandeRepository.save(commande);
    }

    // Trouver toutes les commandes
    public List<Commande> findAll() {
        return commandeRepository.findAll();
    }

    // Trouver une commande par ID
    public Commande findById(Long commandeId) {
        return commandeRepository.findById(commandeId).orElse(null);
    }

    // Sauvegarder une commande
    public Commande save(Commande commande) {
        return commandeRepository.save(commande);
    }

    // Mettre à jour le statut d'une commande
    public Commande updateStatutCommande(Long commandeId, StatutCommande statutCommande) {
        Optional<Commande> optCommande = commandeRepository.findById(commandeId);
        if (optCommande.isEmpty()) {
            return null;
        }
        Commande commande = optCommande.get();
        commande.setStatut(statutCommande);
        return commandeRepository.save(commande);
    }

    // Récupérer les ventes mensuelles sous forme DTO
    public List<VenteMensuelleDTO> getVentesMensuelles() {
        List<Object[]> rows = commandeRepository.findRawVentesMensuelles();
        return rows.stream()
            .map(row -> new VenteMensuelleDTO(
                (String) row[0], // mois "YYYY-MM"
                row[1] != null ? (BigDecimal) row[1] : BigDecimal.ZERO
            ))
            .collect(Collectors.toList());
    }

    // Compter par statut
    public long countByStatut(StatutCommande statut) {
        return commandeRepository.countByStatut(statut);
    }

    // Compter toutes les commandes groupées par statut
    public Map<StatutCommande, Long> countAllByStatut() {
        List<Object[]> results = commandeRepository.countGroupByStatut();
        Map<StatutCommande, Long> map = new HashMap<>();
        for (Object[] result : results) {
            map.put((StatutCommande) result[0], (Long) result[1]);
        }
        return map;
    }

    // Chiffre d'affaire par période (jour ou mois)
    public Map<String, BigDecimal> getChiffreAffaireParPeriode(LocalDate dateDebut, LocalDate dateFin, String type) {
        List<Commande> commandes = commandeRepository.findByStatutAndDateCommandeBetween(
            StatutCommande.LIVREE,
            dateDebut.atStartOfDay(),
            dateFin.plusDays(1).atStartOfDay()
        );

        Map<String, BigDecimal> stats = new TreeMap<>();

        for (Commande commande : commandes) {
            String key;
            if ("jour".equalsIgnoreCase(type)) {
                key = commande.getDateCommande().toLocalDate().toString(); // ex: "2025-06-27"
            } else if ("mois".equalsIgnoreCase(type)) {
                key = commande.getDateCommande().getMonth().toString() + " " + commande.getDateCommande().getYear(); // ex: "JUNE 2025"
            } else {
                continue;
            }

            BigDecimal total = commande.getTotalCommande();
            stats.put(key, stats.getOrDefault(key, BigDecimal.ZERO).add(total));
        }

        return stats;
    }
    
    
    
    public List<Map<String, Object>> getTop3CategoriesVente() {
        List<Commande> commandesLivrees = commandeRepository.findByStatut(StatutCommande.LIVREE);

        Map<String, Integer> categoryCount = new HashMap<>();

        for (Commande commande : commandesLivrees) {
            for (LigneCommande ligne : commande.getLignesCommande()) {
                String categorieNom = ligne.getProduit().getCategorie().getNom();
                int quantite = ligne.getQuantite();
                categoryCount.put(categorieNom, categoryCount.getOrDefault(categorieNom, 0) + quantite);
            }
        }

        // Trier par quantité décroissante et prendre les 3 premiers
        return categoryCount.entrySet().stream()
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .map(entry -> {
                    Map<String, Object> data = new HashMap<>();
                    data.put("categorie", entry.getKey());
                    data.put("quantite", entry.getValue());
                    return data;
                })
                .collect(Collectors.toList());
    }
    
    public List<Map<String, Object>> getProduitsPopulaires() {
        List<Object[]> results = commandeRepository.findProduitsPopulaires();

        return results.stream().map(row -> {
            Map<String, Object> map = new HashMap<>();
            map.put("produitId", row[0]);
            map.put("nomProduit", row[1]);
            map.put("quantiteTotale", row[2]);
            return map;
        }).collect(Collectors.toList());
    }


}
