package com.ktis.voicebot.api.dto;

import java.util.Map;
import com.ktis.voicebot.core.model.ConversationState;

public class ChatResponse {
    private String sessionId;
    private String replyText;
    private ConversationState state;
    private Map<String, String> slots;
    private Map<String, Object> debug;

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public String getReplyText() { return replyText; }
    public void setReplyText(String replyText) { this.replyText = replyText; }

    public ConversationState getState() { return state; }
    public void setState(ConversationState state) { this.state = state; }

    public Map<String, String> getSlots() { return slots; }
    public void setSlots(Map<String, String> slots) { this.slots = slots; }

    public Map<String, Object> getDebug() { return debug; }
    public void setDebug(Map<String, Object> debug) { this.debug = debug; }
}