package com.pharmacie.pharmacie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.pharmacie.pharmacie.model.MouvementStock;
import com.pharmacie.pharmacie.service.MouvementStockService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/mouvements")
public class MouvementStockController {

    @Autowired
    private MouvementStockService service;

    // Enregistrer un mouvement de stock
    @PostMapping
    public ResponseEntity<MouvementStock> enregistrerMouvement(@Validated @RequestBody MouvementStock mouvement) {
        MouvementStock savedMouvement = service.enregistrerMouvement(mouvement);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedMouvement);
    }

    // Lister les mouvements entre deux dates
    @GetMapping
    public ResponseEntity<List<MouvementStock>> listerMouvements(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MouvementStock> mouvements = service.listerMouvements(startDate, endDate);
        return ResponseEntity.ok(mouvements);
    }

    // Rechercher des mouvements par date sous forme de chaînes
    @GetMapping("/search")
    public List<MouvementStock> rechercherMouvements(
            @RequestParam("startDate") String startDate,
            @RequestParam("endDate") String endDate) {

        // Convertion  chaînes de caractères en LocalDateTime
        LocalDateTime start = LocalDateTime.parse(startDate);
        LocalDateTime end = LocalDateTime.parse(endDate);

        return service.listerMouvements(start, end);
    }
}
