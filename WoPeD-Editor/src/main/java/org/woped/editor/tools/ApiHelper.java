package org.woped.editor.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class ApiHelper {

    public static List<String> fetchModels() throws IOException, ParseException {
        String urlString = "http://localhost:8080/p2t/gptModels";
        List<String> models = new ArrayList<>();

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        if (responseCode == 200) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();

            JSONParser parser = new JSONParser();
            JSONArray modelsArray = (JSONArray) parser.parse(content.toString());
            for (Object model : modelsArray) {
                models.add(model.toString());
            }
        } else {
            throw new IOException("Failed to fetch models. Response Code: " + responseCode);
        }

        return models;
    }
}
