package com.pharmacie.pharmacie.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.scheduling.annotation.Scheduled;

import com.pharmacie.pharmacie.model.Produit;
import com.pharmacie.pharmacie.model.Categorie;
import com.pharmacie.pharmacie.repository.ProduitRepository;
import com.pharmacie.pharmacie.repository.CategorieRepository;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.Map;
import java.util.HashMap;


@Service
public class ProduitService {

    @Autowired
    private ProduitRepository produitRepository;

    @Autowired
    private CategorieRepository categorieRepository;
    
    
    

    // Dossier où les images seront stockées
    private static final String IMAGE_DIRECTORY = "uploads/";

    // Ajouter un produit avec une image
    public Produit addProduit(Produit produit, MultipartFile imageFile) throws IOException {
        if (imageFile != null && !imageFile.isEmpty()) {
            // Sauvegarder l'image et obtenir son chemin
            String imagePath = saveImage(imageFile);
            produit.setImagePath(imagePath); // Ajouter le chemin de l'image au produit
        }
        return produitRepository.save(produit); // Sauvegarder le produit dans la base de données
    }

    // Rendre la méthode saveImage publique pour la rendre accessible dans le contrôleur
    public String saveImage(MultipartFile imageFile) throws IOException {
        String uploadDir = "uploads/";
        Path uploadPath = Paths.get(uploadDir);

        // Vérifie si le dossier existe, sinon le créer
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath); // Création du dossier
        }

        String fileName = UUID.randomUUID().toString() + "_" + imageFile.getOriginalFilename();
        Path filePath = uploadPath.resolve(fileName);

        // Sauvegarde du fichier
        Files.copy(imageFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return filePath.toString();
    }

    // Récupérer tous les produits
    public List<Produit> getAllProduits() {
        return produitRepository.findAll(); // Utilise le repository pour récupérer tous les produits
    }

    public Optional<Produit> getProduitById(Long id) {
        return produitRepository.findById(id);
    }
    
    public Produit updateProduit(Long id, Produit produit) {
        produit.setId(id);
        return produitRepository.save(produit);
    }

    
    private String storeImage(MultipartFile imageFile) throws IOException {
        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
        Path imagePath = Path.of(IMAGE_DIRECTORY, fileName);
        Files.copy(imageFile.getInputStream(), imagePath, StandardCopyOption.REPLACE_EXISTING);
        return imagePath.toString();
    }

    public Optional<Produit> getProduitByNom(String nom) {
        return produitRepository.findByNom(nom);
    }

    public List<Produit> findLowStockProducts() {
        return produitRepository.findAll().stream()
                .filter(p -> p.getStock() < 10)
                .collect(Collectors.toList());
    }

   

    @Autowired
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public List<Produit> getProduitsProchesExpiration() {
        LocalDate today = LocalDate.now();
        LocalDate alertDate = today.plusDays(7);
        return produitRepository.findByDateExpirationBefore(alertDate);
    }

    @Scheduled(cron = "0 0 8 * * ?")
    public void verifierProduitsProchesExpiration() {
        List<Produit> produitsAlerte = getProduitsProchesExpiration();
        for (Produit produit : produitsAlerte) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo("lineguidi@gmail.com");
            message.setSubject("Alerte : Produit proche de la date d'expiration");
            message.setText("Le produit " + produit.getNom() + " expire le " + produit.getDateExpiration());
            mailSender.send(message);
        }
    }

    public Produit createProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public Produit updateProduit(Long id, Produit produit, MultipartFile imageFile) throws IOException {
        produit.setId(id);
        if (imageFile != null && !imageFile.isEmpty()) {
            deleteOldImage(produit.getImagePath());
            String imagePath = saveImage(imageFile);
            produit.setImagePath(imagePath);
        }
        return produitRepository.save(produit);
    }

    private void deleteOldImage(String oldImagePath) throws IOException {
        if (oldImagePath != null && !oldImagePath.isEmpty()) {
            Path oldPath = Path.of(oldImagePath);
            if (Files.exists(oldPath)) {
                Files.delete(oldPath);
            }
        }
    }

    public boolean existsById(Long id) {
        return produitRepository.existsById(id);
    }

    public boolean deleteProduit(Long id) {
        if (produitRepository.existsById(id)) {
            produitRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public List<Produit> findExpiringProducts() {
        LocalDate today = LocalDate.now();
        LocalDate thresholdDate = today.plusDays(7);
        return produitRepository.findByDateExpirationBeforeOrDateExpirationBetween(today, thresholdDate, thresholdDate);
    }

    public List<Produit> searchProduits(String keyword, Double minPrix, Double maxPrix, Integer minStock) {
        if (keyword != null && !keyword.isEmpty()) {
            return produitRepository.findByNomContainingIgnoreCaseAndPrixUnitaireBetweenAndStockGreaterThan(
                keyword, minPrix, maxPrix, minStock
            );
        }
        return produitRepository.findByPrixUnitaireBetweenAndStockGreaterThan(minPrix, maxPrix, minStock);
    }

    public Produit findProduitById(Long produitId) {
        return produitRepository.findById(produitId)
                .orElseThrow(() -> new IllegalArgumentException("Produit non trouvé"));
    }

    // Gestion des catégories
    public Categorie saveCategorie(Categorie categorie) {
        return categorieRepository.save(categorie);
    }

    public List<Categorie> getAllCategories() {
        return categorieRepository.findAll();
    }

    public void deleteCategorie(Long id) {
        categorieRepository.deleteById(id);
    }
    
    
    public List<Produit> getProduitsParCategorie(Categorie categorie) {
        return produitRepository.findByCategorie(categorie);
    }
    
    
    
    
    public Map<String, Long> getProduitStats() {
        long lowStockCount = produitRepository.countByStockLessThan(5);
        long expiredCount = produitRepository.countByDateExpirationBefore(LocalDate.now());
        long totalInStock = produitRepository.count();
        long expiringSoonCount = produitRepository.countByDateExpirationBetween(LocalDate.now(), LocalDate.now().plusDays(7)); // Ajout des produits expirant dans les 7 jours


        Map<String, Long> stats = new HashMap<>();
        stats.put("stockFaible", lowStockCount);
        stats.put("expirés", expiredCount);
        stats.put("totalProduits", totalInStock);
        stats.put("expirantDans7Jours", expiringSoonCount);

        return stats;
    }

}
