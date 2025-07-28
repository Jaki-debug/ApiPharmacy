package com.pharmacie.pharmacie.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class OverpassService {

    public double[] getCoordinatesFromOverpass(String nomPharmacie) {
        String query = "[out:json];"
                + "node[\"amenity\"=\"pharmacy\"][\"name\"~\"" + nomPharmacie + "\",i]"
                + "(around:20000,6.1319,1.2228);"
                + "out body;";

        try {
            // URL de l'API Overpass
            String overpassUrl = "https://overpass-api.de/api/interpreter";
            HttpURLConnection connection = (HttpURLConnection) new URL(overpassUrl).openConnection();

            // Configuration de la requête
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            // Envoi de la requête
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = query.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }

            // Lecture de la réponse
            StringBuilder response = new StringBuilder();
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    response.append(line.trim());
                }
            }

            // Traitement du JSON
            JSONObject json = new JSONObject(response.toString());
            JSONArray elements = json.getJSONArray("elements");

            if (elements.length() > 0) {
                JSONObject pharmacie = elements.getJSONObject(0);
                double lat = pharmacie.getDouble("lat");
                double lon = pharmacie.getDouble("lon");
                return new double[]{lat, lon};
            }

        } catch (Exception e) {
            System.out.println("❌ Erreur Overpass API : " + e.getMessage());
        }

        return null;
    }
}
