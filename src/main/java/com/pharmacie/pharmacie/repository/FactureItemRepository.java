package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.model.FactureItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FactureItemRepository extends JpaRepository<FactureItem, Long> {
}
