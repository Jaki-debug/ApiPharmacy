package com.pharmacie.pharmacie.repository;

import com.pharmacie.pharmacie.model.MouvementStock;  
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MouvementStockRepository extends JpaRepository<MouvementStock, Long> {
    List<MouvementStock> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
