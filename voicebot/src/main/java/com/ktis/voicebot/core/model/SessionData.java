package com.ktis.voicebot.core.model;

import java.util.HashMap;
import java.util.Map;

public class SessionData {
    private String sessionId;
    private ConversationState state = ConversationState.START;
    private Map<String, String> slots = new HashMap<>();
    private long updatedAtEpochMs;

    public SessionData() {}

    public SessionData(String sessionId) {
        this.sessionId = sessionId;
        this.updatedAtEpochMs = System.currentTimeMillis();
        this.slots.put("accountNumber", null);
        this.slots.put("amount", null);
    }

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public ConversationState getState() { return state; }
    public void setState(ConversationState state) { this.state = state; }

    public Map<String, String> getSlots() { return slots; }
    public void setSlots(Map<String, String> slots) { this.slots = slots; }

    public long getUpdatedAtEpochMs() { return updatedAtEpochMs; }
    public void setUpdatedAtEpochMs(long updatedAtEpochMs) { this.updatedAtEpochMs = updatedAtEpochMs; }
}