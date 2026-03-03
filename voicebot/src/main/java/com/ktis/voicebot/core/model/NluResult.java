package com.ktis.voicebot.core.model;

import java.util.HashMap;
import java.util.Map;

public class NluResult {
    private Intent intent;
    private double confidence;
    private Map<String, String> entities = new HashMap<>();

    public NluResult() {}

    public NluResult(Intent intent, double confidence) {
        this.intent = intent;
        this.confidence = confidence;
    }

    public Intent getIntent() { return intent; }
    public void setIntent(Intent intent) { this.intent = intent; }

    public double getConfidence() { return confidence; }
    public void setConfidence(double confidence) { this.confidence = confidence; }

    public Map<String, String> getEntities() { return entities; }
    public void setEntities(Map<String, String> entities) { this.entities = entities; }
}