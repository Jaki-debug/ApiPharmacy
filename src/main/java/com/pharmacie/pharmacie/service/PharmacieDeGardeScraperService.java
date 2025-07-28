package com.pharmacie.pharmacie.service;

import com.pharmacie.pharmacie.model.Pharmacie;
import com.pharmacie.pharmacie.repository.PharmacieRepository;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.io.IOException;
import java.util.*;

@Transactional
@Service
public class PharmacieDeGardeScraperService {

    private final PharmacieRepository pharmacieRepository;
    private final OverpassService overpassService;

    public PharmacieDeGardeScraperService(PharmacieRepository pharmacieRepository,
                                          OverpassService overpassService) {
        this.pharmacieRepository = pharmacieRepository;
        this.overpassService = overpassService;
    }

    @Scheduled(cron = "0 0 6 * * MON") // Tous les lundis à 6h
    public void scrapePharmaciesDeGarde() {
        try {
            System.out.println("⏳ Scraping en cours...");
            pharmacieRepository.deleteAllByPharmacieDeGardeTrue();

            String url = "https://www.savoirnews.net/blog/2025/07/15/togo-pharmacies-de-garde-du-14-au-21-juillet-2025-a-lome/";
            scrapeArticlePharmacies(url);
        } catch (IOException e) {
            System.out.println("❌ Erreur pendant le scraping : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void scrapeArticlePharmacies(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        Element content = doc.selectFirst("div.entry-content");
        if (content == null) {
            System.out.println("❌ Aucun contenu trouvé dans la section principale de la page");
            return;
        }

        Elements elements = content.select("p, li");
        System.out.println("📄 Nombre d'éléments analysés : " + elements.size());

        Set<String> nomsPharmacies = new HashSet<>();

        for (int i = 0; i < elements.size(); i++) {
            String ligneNom = elements.get(i).text().trim();

            if (ligneNom.toLowerCase().contains("pharmacie")) {
                // Lire la ligne suivante pour adresse/téléphone
                String ligneAdresseTel = "";
                if (i + 1 < elements.size()) {
                    ligneAdresseTel = elements.get(i + 1).text().trim();
                }

                System.out.println("🔎 Texte extrait : " + ligneNom);
                Pharmacie p = extrairePharmacieDepuisTexte(ligneNom, ligneAdresseTel);

                if (p != null && !nomsPharmacies.contains(p.getNom())) {
                    p.setPharmacieDeGarde(true);

                    // Utilisation d'Overpass pour récupérer les coordonnées GPS
                    double[] coords = overpassService.getCoordinatesFromOverpass(p.getNom());
                    if (coords != null) {
                        p.setLatitude(coords[0]);
                        p.setLongitude(coords[1]);
                        System.out.println("📍 Coordonnées trouvées pour " + p.getNom() + " : lat=" + coords[0] + ", lon=" + coords[1]);
                    } else {
                        System.out.println("⚠️ Coordonnées non trouvées pour " + p.getNom());
                    }

                    pharmacieRepository.save(p);
                    nomsPharmacies.add(p.getNom());
                    System.out.println("✅ Pharmacie enregistrée : " + p.getNom());
                }
            }
        }

        System.out.println("💊 Total pharmacies extraites : " + nomsPharmacies.size());
    }

    private Pharmacie extrairePharmacieDepuisTexte(String ligneNom, String ligneAdresseTel) {
        System.out.println("📦 Analyse du texte : " + ligneNom);

        String nom = ligneNom.replace("*", "").replace("-", "").trim();
        String adresse = null;
        String telephone = null;

        if (ligneAdresseTel != null && !ligneAdresseTel.isEmpty()) {
            // Regex pour capter 8 chiffres (ex: 96 80 09 70) au début de la ligne
            java.util.regex.Pattern patternTel = java.util.regex.Pattern.compile("^(\\d{2}(?:\\s?\\d{2}){3})\\s*(.*)$");
            java.util.regex.Matcher matcher = patternTel.matcher(ligneAdresseTel);

            if (matcher.matches()) {
                telephone = matcher.group(1).replaceAll("\\s+", "");
                adresse = matcher.group(2).trim();
            } else {
                adresse = ligneAdresseTel;
            }
        }

        if (nom.isEmpty()) return null;

        Pharmacie pharmacie = new Pharmacie();
        pharmacie.setNom(nom);
        pharmacie.setAdresse(adresse);
        pharmacie.setTelephone(telephone);
        pharmacie.setPharmacieDeGarde(true);

        System.out.println("➡️ Nom: " + nom);
        System.out.println("➡️ Téléphone: " + telephone);
        System.out.println("➡️ Adresse: " + adresse);

        return pharmacie;
    }
}
