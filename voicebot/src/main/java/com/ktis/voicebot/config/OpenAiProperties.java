package com.ktis.voicebot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "openai")
public class OpenAiProperties {

    /**
     * OpenAI API Key (server-side only)
     */
    private String apiKey;

    /**
     * e.g. gpt-5 (or your chosen model)
     */
    private String model = "gpt-5";

    /**
     * Request timeout ms
     */
    private int timeoutMs = 15000;

    /**
     * Session history turns to keep (user+assistant pairs)
     */
    private int maxHistoryTurns = 6;

    public String getApiKey() { return apiKey; }
    public void setApiKey(String apiKey) { this.apiKey = apiKey; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getTimeoutMs() { return timeoutMs; }
    public void setTimeoutMs(int timeoutMs) { this.timeoutMs = timeoutMs; }

    public int getMaxHistoryTurns() { return maxHistoryTurns; }
    public void setMaxHistoryTurns(int maxHistoryTurns) { this.maxHistoryTurns = maxHistoryTurns; }
}