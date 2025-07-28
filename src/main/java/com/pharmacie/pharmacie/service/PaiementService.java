package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.Commande;
import com.pharmacie.pharmacie.repository.CommandeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PaiementService {

    @Autowired
    private CommandeRepository commandeRepository;

    public Optional<Commande> trouverParReference(String reference) {
        return commandeRepository.findByReference(reference);
    }

    public Commande sauvegarderCommande(Commande commande) {
        return commandeRepository.save(commande);
    }
}
