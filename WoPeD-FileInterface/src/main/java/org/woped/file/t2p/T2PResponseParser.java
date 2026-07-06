package org.woped.file.t2p;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.woped.file.t2p.model.LlmResponse;

final class T2PResponseParser {
    private static final Gson GSON = new Gson();

    private T2PResponseParser() {
    }

    static String extractPnml(String json) throws JsonSyntaxException {
        if (json == null) {
            return null;
        }

        LlmResponse response = GSON.fromJson(json, LlmResponse.class);
        if (response == null) {
            return null;
        }

        return response.getResult();
    }

    static String extractErrorMessage(String json) {
        if (json == null || json.isBlank()) {
            return null;
        }

        try {
            JsonObject root = GSON.fromJson(json, JsonObject.class);
            if (root == null) {
                return json;
            }
            if (root.has("error")) {
                if (root.get("error").isJsonObject()) {
                    JsonObject error = root.getAsJsonObject("error");
                    if (error.has("message")) {
                        return error.get("message").getAsString();
                    }
                } else if (root.get("error").isJsonPrimitive()) {
                    return root.get("error").getAsString();
                }
            }
        } catch (JsonSyntaxException ignored) {
            return json;
        }

        return json;
    }
}
