package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.dto.PharmacieInscriptionDTO;
import com.pharmacie.pharmacie.dto.PharmacieLoginDTO;
import com.pharmacie.pharmacie.model.Pharmacie;
import com.pharmacie.pharmacie.service.PharmacieService;
import com.pharmacie.pharmacie.service.PharmacieDeGardeScraperService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pharmacies")
public class PharmacieController {

    private final PharmacieService pharmacieService;
    private final PharmacieDeGardeScraperService pharmacieDeGardeScraperService;

    public PharmacieController(PharmacieService pharmacieService,
                               PharmacieDeGardeScraperService pharmacieDeGardeScraperService) {
        this.pharmacieService = pharmacieService;
        this.pharmacieDeGardeScraperService = pharmacieDeGardeScraperService;
    }

    @PostMapping("/inscription")
    public ResponseEntity<String> inscrirePharmacie(@RequestBody PharmacieInscriptionDTO dto) {
        try {
            pharmacieService.inscrire(dto);
            return ResponseEntity.ok("Inscription réussie.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Échec de l'inscription : " + e.getMessage());
        }
    }

    @PostMapping("/connexion")
    public ResponseEntity<String> connecterPharmacie(@RequestBody PharmacieLoginDTO dto) {
        try {
            pharmacieService.connecter(dto);
            return ResponseEntity.ok("Connexion réussie.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Échec de la connexion : " + e.getMessage());
        }
    }

    @GetMapping("/de-garde/produit/{produitId}")
    public List<Pharmacie> getPharmaciesDeGardeParProduit(@PathVariable Long produitId) {
        return pharmacieService.getPharmaciesDeGardeParProduit(produitId);
    }

    @GetMapping("/scraper")
    public String lancerScraping() {
        pharmacieDeGardeScraperService.scrapePharmaciesDeGarde();
        return "Scraping lancé avec succès.";
    }

    @GetMapping("/de-garde")
    public List<Pharmacie> getPharmaciesDeGarde() {
        return pharmacieService.getPharmaciesDeGarde();
    }

    @GetMapping("/test")
    public String test() {
        return "API fonctionne";
    }

    @GetMapping("/proches")
    public List<Pharmacie> getPharmaciesProches(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "5") double rayonKm) {
        return pharmacieService.getPharmaciesDeGardeProches(latitude, longitude, rayonKm);
    }

    // Récupérer le profil d'une pharmacie via son email (ex: https://.../api/pharmacies/profil?email=xxx)
    @GetMapping("/profil")
    public ResponseEntity<PharmacieInscriptionDTO> getProfil(@RequestParam String email) {
        PharmacieInscriptionDTO dto = pharmacieService.recupererProfil(email);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // Mettre à jour le profil d'une pharmacie
    @PutMapping("/profil")
    public ResponseEntity<String> updateProfil(@RequestBody PharmacieInscriptionDTO dto) {
        boolean updated = pharmacieService.mettreAJourProfil(dto);
        if (updated) {
            return ResponseEntity.ok("Profil mis à jour avec succès.");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pharmacie non trouvée.");
        }
    }
}
