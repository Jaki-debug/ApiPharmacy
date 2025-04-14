package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.Facture;
import com.pharmacie.pharmacie.model.Panier;
import com.pharmacie.pharmacie.model.FactureItem;
import com.pharmacie.pharmacie.repository.FactureRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class FactureService {

   private FactureRepository factureRepository;  

 
   public FactureService(FactureRepository factureRepository) {
       this.factureRepository = factureRepository;
   }


   // récupérer une facture par son ID
   public Optional<Facture> recupererFacture(Long id) {
       return factureRepository.findById(id);
   }

   // récupérer toutes les factures
   public List<Facture> recupererToutesLesFactures() {
       return factureRepository.findAll();
   }

   //  supprimer une facture
   public void supprimerFacture(Long id) {
       factureRepository.deleteById(id);
   }
}
