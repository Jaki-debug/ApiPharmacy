package com.pharmacie.pharmacie.controller;

import com.pharmacie.pharmacie.dto.VenteMensuelleDTO;
import com.pharmacie.pharmacie.service.CommandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/statistiques")
public class StatistiqueController {

    private final CommandeService commandeService;

    public StatistiqueController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    @GetMapping("/ventes-mensuelles")
    public ResponseEntity<List<VenteMensuelleDTO>> getVentesMensuelles() {
        List<VenteMensuelleDTO> ventes = commandeService.getVentesMensuelles();
        return ResponseEntity.ok(ventes);
    }
}
