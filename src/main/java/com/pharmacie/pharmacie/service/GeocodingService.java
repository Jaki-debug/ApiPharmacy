package com.pharmacie.pharmacie.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;

@Service
public class GeocodingService {

    public double[] getCoordinatesFromName(String nomPharmacie) {
        try {
            String baseUrl = "https://nominatim.openstreetmap.org/search";
            String query = URLEncoder.encode(nomPharmacie + ", Lomé, Togo", StandardCharsets.UTF_8);
            String urlStr = baseUrl + "?q=" + query + "&format=json&limit=1";

            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("User-Agent", "Mozilla/5.0"); // Important pour éviter erreur 403

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            JSONArray jsonArray = new JSONArray(response.toString());

            if (!jsonArray.isEmpty()) {
                JSONObject obj = jsonArray.getJSONObject(0);
                double lat = obj.getDouble("lat");
                double lon = obj.getDouble("lon");
                return new double[]{lat, lon};
            }
        } catch (Exception e) {
            System.out.println("Erreur géocodage : " + e.getMessage());
        }
        return null;
    }
}
