package services;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.concurrent.CompletableFuture;

public class AIService {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static CompletableFuture<String> sendMessage(String userMessage) {
        String apiKey = ConfigService.getAiApiKey();
        String model = ConfigService.getAiModel();
        String systemPrompt = ConfigService.getAiSystemPrompt();
        int maxTokens = ConfigService.getAiMaxTokens();

        if (apiKey == null || apiKey.trim().isEmpty()) {
            return CompletableFuture.failedFuture(new IllegalStateException(
                    "Clé API manquante. Veuillez configurer l'assistant dans les paramètres."));
        }

        // Build JSON Payload manually to avoid external dependencies if possible,
        // but using org.json (if available) or simple string construction is safer.
        // Assuming we might need to be dependency-light, I'll use simple string builder
        // for this prototype
        // checking escaping is important.

        String requestBody = buildJsonPayload(model, systemPrompt, userMessage, maxTokens);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/chat/completions"))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    if (response.statusCode() == 200) {
                        return extractContentFromResponse(response.body());
                    } else {
                        throw new RuntimeException("Erreur API (" + response.statusCode() + "): " + response.body());
                    }
                });
    }

    private static String buildJsonPayload(String model, String systemPrompt, String userMessage, int maxTokens) {
        // Very basic JSON construction. handling quotes is critical.
        return "{"
                + "\"model\": \"" + escape(model) + "\","
                + "\"messages\": ["
                + "  {\"role\": \"system\", \"content\": \"" + escape(systemPrompt) + "\"},"
                + "  {\"role\": \"user\", \"content\": \"" + escape(userMessage) + "\"}"
                + "],"
                + "\"max_tokens\": " + maxTokens
                + "}";
    }

    private static String escape(String s) {
        if (s == null)
            return "";
        return s.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    private static String extractContentFromResponse(String jsonResponse) {
        // Simple manual parsing to find the "content" field inside "choices" ->
        // "message"
        // robust parsing would use a library.
        try {
            int contentIndex = jsonResponse.indexOf("\"content\":");
            if (contentIndex != -1) {
                int startQuote = jsonResponse.indexOf("\"", contentIndex + 10);
                // This is dangerous with manual parsing if content has escaped quotes.
                // But for prototype without heavy libs:

                // Let's try a slightly better manual parser or valid regex if we can't use
                // org.json
                // Actually, assuming the environment has no extra libs, I will stick to this
                // but warn.
                // IF we had org.json:
                // JSONObject obj = new JSONObject(jsonResponse);
                // return
                // obj.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");

                return manualJsonExtract(jsonResponse);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "Désolé, impossible de lire la réponse de l'IA.";
    }

    // Quick helper to extract content field value while handling escaped quotes
    private static String manualJsonExtract(String json) {
        // Search for "content": "
        String marker = "\"content\": \"";
        int start = json.indexOf(marker);
        if (start == -1)
            return "Erreur de parsing.";
        start += marker.length();

        StringBuilder result = new StringBuilder();
        boolean escaped = false;

        for (int i = start; i < json.length(); i++) {
            char c = json.charAt(i);
            if (escaped) {
                if (c == 'n')
                    result.append('\n');
                else if (c == 'r')
                    result.append('\r');
                else if (c == 't')
                    result.append('\t');
                else
                    result.append(c);
                escaped = false;
            } else {
                if (c == '\\') {
                    escaped = true;
                } else if (c == '"') {
                    return result.toString();
                } else {
                    result.append(c);
                }
            }
        }
        return result.toString();
    }
}
