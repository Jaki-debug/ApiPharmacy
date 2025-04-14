package com.pharmacie.pharmacie.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pharmacie.pharmacie.model.Facture;

public interface FactureRepository extends JpaRepository<Facture, Long> {
   
}
