package com.ktis.voicebot.core.openai;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OpenAiClient {

    private final HttpClient http;
    private final ObjectMapper om;

    private final String apiKey;
    private final String model;
    private final int timeoutMs;

    public OpenAiClient(String apiKey, String model, int timeoutMs) {
        this.apiKey = apiKey;
        this.model = model;
        this.timeoutMs = timeoutMs;
        this.http = HttpClient.newBuilder()
                .connectTimeout(Duration.ofMillis(timeoutMs))
                .build();
        this.om = new ObjectMapper();
    }

    /**
     * Calls POST https://api.openai.com/v1/responses with:
     *  - model
     *  - instructions
     *  - input (either string or message array)
     *
     * Docs: /v1/responses is the recommended generation endpoint. :contentReference[oaicite:1]{index=1}
     */
    public String generateReply(String instructions, JsonNode input) throws IOException, InterruptedException {
        // Request shape based on Responses API migration guide examples. :contentReference[oaicite:2]{index=2}
        JsonNode body = om.createObjectNode()
                .put("model", model)
                .put("instructions", instructions)
                .set("input", input);

        String json = om.writeValueAsString(body);

        HttpRequest req = HttpRequest.newBuilder()
                .uri(URI.create("https://api.openai.com/v1/responses"))
                .timeout(Duration.ofMillis(timeoutMs))
                .header("Authorization", "Bearer " + apiKey)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> res = http.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() / 100 != 2) {
            throw new IOException("OpenAI API error status=" + res.statusCode() + " body=" + res.body());
        }

        return extractOutputText(res.body());
    }

    /**
     * Robust-ish extraction:
     * Response objects differ from Chat Completions; Responses returns output items. :contentReference[oaicite:3]{index=3}
     * We try common shapes:
     *  - output_text (sometimes present in SDK helpers; may not exist in raw)
     *  - output[].content[].text
     */
    private String extractOutputText(String responseJson) throws IOException {
        JsonNode root = om.readTree(responseJson);

        JsonNode outputText = root.get("output_text");
        if (outputText != null && outputText.isTextual()) {
            return outputText.asText();
        }

        JsonNode output = root.get("output");
        if (output != null && output.isArray()) {
            for (JsonNode item : output) {
                JsonNode content = item.get("content");
                if (content != null && content.isArray()) {
                    for (JsonNode c : content) {
                        // Many Responses outputs include text under c.text
                        JsonNode text = c.get("text");
                        if (text != null && text.isTextual()) {
                            return text.asText();
                        }
                        // Some shapes: c.output_text
                        JsonNode ot = c.get("output_text");
                        if (ot != null && ot.isTextual()) {
                            return ot.asText();
                        }
                    }
                }
            }
        }

        // Fallback: return whole json (trimmed) so you can see what shape you got
        return "응답 파싱 실패. raw=" + responseJson;
    }
}