package com.pharmacie.pharmacie.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.pharmacie.pharmacie.repository.MouvementStockRepository;
import com.pharmacie.pharmacie.model.MouvementStock;

@Service
public class MouvementStockService {

    @Autowired
    private MouvementStockRepository mouvementStockRepository;
    
    public MouvementStock enregistrerMouvement(MouvementStock mouvementStock) {
       
        return mouvementStockRepository.save(mouvementStock);
    }

  
    public List<MouvementStock> getAllMouvements() {
        return mouvementStockRepository.findAll();
    }

    
    public List<MouvementStock> getMouvementsByDate(LocalDateTime startDate, LocalDateTime endDate) {
        return mouvementStockRepository.findByDateBetween(startDate, endDate);
    }
    
    public List<MouvementStock> listerMouvements(LocalDateTime startDate, LocalDateTime endDate) {
        return mouvementStockRepository.findByDateBetween(startDate, endDate);
    }

	public List<MouvementStock> listerTousLesMouvements() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
