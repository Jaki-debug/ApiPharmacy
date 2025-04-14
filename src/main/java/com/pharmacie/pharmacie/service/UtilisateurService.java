package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.Utilisateur;
import com.pharmacie.pharmacie.repository.UtilisateurRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.pharmacie.pharmacie.service.UtilisateurService;  // Assurez-vous que l'importation est correcte

import java.util.List;
import java.util.Optional;

@Service
public class UtilisateurService {

    private final UtilisateurRepository utilisateurRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UtilisateurService(UtilisateurRepository utilisateurRepository) {
        this.utilisateurRepository = utilisateurRepository;
    }

    // Cré utilisateur, avec hachage du mot de passe
    public Utilisateur creerUtilisateur(Utilisateur utilisateur) {
        utilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
        return utilisateurRepository.save(utilisateur);
    }

    // Récupérer un utilisateur par son ID
    public Optional<Utilisateur> getUtilisateurById(Integer id) {
        return utilisateurRepository.findById(id);
    }

    // Récupérer tous les utilisateurs
    public List<Utilisateur> getTousLesUtilisateurs() {
        return utilisateurRepository.findAll();
    }

    // Mettre à jour un utilisateur
    public Optional<Utilisateur> updateUtilisateur(Integer id, Utilisateur utilisateur) {
        Optional<Utilisateur> utilisateurExist = utilisateurRepository.findById(id);
        if (utilisateurExist.isPresent()) {
            Utilisateur existingUtilisateur = utilisateurExist.get();

            existingUtilisateur.setNom(utilisateur.getNom());
            existingUtilisateur.setEmail(utilisateur.getEmail());
            if (utilisateur.getMotDePasse() != null && !utilisateur.getMotDePasse().isEmpty()) {
                existingUtilisateur.setMotDePasse(passwordEncoder.encode(utilisateur.getMotDePasse()));
            }
            return Optional.of(utilisateurRepository.save(existingUtilisateur));
        }
        return Optional.empty();
    }

    // Supprimer un utilisateur
    public boolean deleteUtilisateur(Integer id) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findById(id);
        if (utilisateur.isPresent()) {
            utilisateurRepository.deleteById(id);
            return true;
        }
        return false;
    }

    // Méthode pour trouver un utilisateur par ID
    public Utilisateur findUtilisateurById(Integer id) {
        return utilisateurRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
    }
}
