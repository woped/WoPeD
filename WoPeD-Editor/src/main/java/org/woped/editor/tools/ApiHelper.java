package org.woped.editor.tools;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.woped.core.utilities.LoggerManager;
import org.woped.editor.Constants;

public class ApiHelper {

    private static final int CONNECT_TIMEOUT_MS = 10000;
    private static final int READ_TIMEOUT_MS    = 10000;

    /**
     * Fetches the list of available model IDs directly from the selected LLM
     * provider's API.
     *
     * @param apiKey   user's API key (ignored for lmStudio)
     * @param provider one of "openAi", "gemini", "lmStudio"
     * @return alphabetically sorted list of model IDs reported by the provider
     */
    public static List<String> fetchModels(String apiKey, String provider) throws IOException, ParseException {
        LoggerManager.info(Constants.EDITOR_LOGGER, "Fetching models for provider: " + provider);

        if (provider == null || provider.isEmpty()) provider = "openAi";
        if (apiKey   == null) apiKey = "";

        String  urlString;
        boolean useBearerAuth;
        boolean parseAsOpenAi;
        switch (provider) {
            case "openAi":
                urlString = "https://api.openai.com/v1/models";
                useBearerAuth = true;
                parseAsOpenAi = true;
                break;
            case "gemini":
                urlString = "https://generativelanguage.googleapis.com/v1beta/models?key="
                        + URLEncoder.encode(apiKey, StandardCharsets.UTF_8);
                useBearerAuth = false;
                parseAsOpenAi = false;
                break;
            case "lmStudio":
                urlString = "http://localhost:1234/v1/models";
                useBearerAuth = false;
                parseAsOpenAi = true;
                break;
            default:
                throw new IOException("Unknown provider: " + provider);
        }

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(CONNECT_TIMEOUT_MS);
        connection.setReadTimeout(READ_TIMEOUT_MS);
        if (useBearerAuth) {
            connection.setRequestProperty("Authorization", "Bearer " + apiKey);
        }
        connection.setRequestProperty("Accept", "application/json");

        int responseCode = connection.getResponseCode();
        if (responseCode != 200) {
            LoggerManager.error(Constants.EDITOR_LOGGER,
                    "Failed to fetch models from " + provider + " (HTTP " + responseCode + ")");
            throw new IOException("Failed to fetch models from " + provider
                    + ". HTTP " + responseCode + ". Please check your API key and network.");
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader in = new BufferedReader(
                new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            while ((line = in.readLine()) != null) {
                content.append(line);
            }
        }

        List<String> models = new ArrayList<>();
        JSONParser   parser = new JSONParser();
        Object       parsed = parser.parse(content.toString());
        if (!(parsed instanceof JSONObject)) {
            LoggerManager.error(Constants.EDITOR_LOGGER,
                    "Unexpected response root type from " + provider + ": " + parsed);
            return models;
        }
        JSONObject root = (JSONObject) parsed;

        JSONArray array;
        String    idField;
        if (parseAsOpenAi) {
            array   = (JSONArray) root.get("data");
            idField = "id";
        } else {
            array   = (JSONArray) root.get("models");
            idField = "name";
        }
        if (array == null) {
            LoggerManager.error(Constants.EDITOR_LOGGER,
                    "Response from " + provider + " did not contain expected array field.");
            return models;
        }

        for (Object item : array) {
            if (!(item instanceof JSONObject)) continue;
            Object id = ((JSONObject) item).get(idField);
            if (id == null) continue;
            String idStr = id.toString();
            if (!parseAsOpenAi && idStr.startsWith("models/")) {
                idStr = idStr.substring("models/".length());
            }
            models.add(idStr);
        }

        Collections.sort(models);
        LoggerManager.info(Constants.EDITOR_LOGGER,
                "Fetched " + models.size() + " models from " + provider);
        return models;
    }
}
