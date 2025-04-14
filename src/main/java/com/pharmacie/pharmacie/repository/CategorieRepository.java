package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.model.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieRepository extends JpaRepository<Categorie, Long> {
    // Vous pouvez ajouter des méthodes personnalisées ici si nécessaire
	
}
