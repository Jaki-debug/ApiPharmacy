package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.dto.PharmacieInscriptionDTO;
import com.pharmacie.pharmacie.dto.PharmacieLoginDTO;
import com.pharmacie.pharmacie.model.Pharmacie;
import com.pharmacie.pharmacie.repository.PharmacieRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PharmacieService {

    private final PharmacieRepository pharmacieRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public PharmacieService(PharmacieRepository pharmacieRepository) {
        this.pharmacieRepository = pharmacieRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public List<Pharmacie> getPharmaciesDeGarde() {
        return pharmacieRepository.findByPharmacieDeGardeTrue();
    }

    public List<Pharmacie> getPharmaciesDeGardeParProduit(Long produitId) {
        return pharmacieRepository.findPharmaciesDeGardeParProduit(produitId);
    }

    public List<Pharmacie> getPharmaciesDeGardeProches(double latitude, double longitude, double rayonKm) {
        return pharmacieRepository.findPharmaciesDeGardeProches(latitude, longitude, rayonKm);
    }

    public void enregistrerPharmacie(Pharmacie pharmacie) {
        pharmacieRepository.save(pharmacie);
    }

    // Inscription avec PharmacieInscriptionDTO
    public Pharmacie inscrire(PharmacieInscriptionDTO dto) {
        if (pharmacieRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé.");
        }

        Pharmacie pharmacie = new Pharmacie();
        pharmacie.setNom(dto.getNom());
        pharmacie.setEmail(dto.getEmail());
        pharmacie.setMotDePasse(passwordEncoder.encode(dto.getMotDePasse()));
        pharmacie.setAdresse(dto.getAdresse());
        pharmacie.setTelephone(dto.getTelephone());
        pharmacie.setPharmacieDeGarde(dto.isPharmacieDeGarde());
        pharmacie.setLatitude(dto.getLatitude());
        pharmacie.setLongitude(dto.getLongitude());

        return pharmacieRepository.save(pharmacie);
    }

    // Connexion avec PharmacieLoginDTO
    public Pharmacie connecter(PharmacieLoginDTO dto) {
        Optional<Pharmacie> optPharmacie = pharmacieRepository.findByEmail(dto.getEmail());
        if (optPharmacie.isEmpty()) {
            throw new RuntimeException("Pharmacie non trouvée avec cet email.");
        }
        Pharmacie pharmacie = optPharmacie.get();

        if (!passwordEncoder.matches(dto.getMotDePasse(), pharmacie.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect.");
        }

        return pharmacie;
    }

    // Récupérer profil par email
    public PharmacieInscriptionDTO recupererProfil(String email) {
        Optional<Pharmacie> optPharmacie = pharmacieRepository.findByEmail(email);
        if (optPharmacie.isEmpty()) {
            return null;
        }
        Pharmacie pharmacie = optPharmacie.get();
        return convertirEnDTO(pharmacie);
    }

    // Mettre à jour profil
    public boolean mettreAJourProfil(PharmacieInscriptionDTO dto) {
        Optional<Pharmacie> optPharmacie = pharmacieRepository.findByEmail(dto.getEmail());
        if (optPharmacie.isEmpty()) {
            return false;
        }
        Pharmacie pharmacie = optPharmacie.get();
        pharmacie.setNom(dto.getNom());
        pharmacie.setAdresse(dto.getAdresse());
        pharmacie.setTelephone(dto.getTelephone());
        pharmacie.setPharmacieDeGarde(dto.isPharmacieDeGarde());
        pharmacie.setLatitude(dto.getLatitude());
        pharmacie.setLongitude(dto.getLongitude());

        pharmacieRepository.save(pharmacie);
        return true;
    }

    // Conversion Entity -> DTO
    private PharmacieInscriptionDTO convertirEnDTO(Pharmacie pharmacie) {
        PharmacieInscriptionDTO dto = new PharmacieInscriptionDTO();
        dto.setNom(pharmacie.getNom());
        dto.setEmail(pharmacie.getEmail());
        dto.setAdresse(pharmacie.getAdresse());
        dto.setTelephone(pharmacie.getTelephone());
        dto.setPharmacieDeGarde(pharmacie.isPharmacieDeGarde());
        dto.setLatitude(pharmacie.getLatitude());
        dto.setLongitude(pharmacie.getLongitude());
        return dto;
    }
}
