package com.ktis.voicebot.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot.dialog")
public class DialogMessagesProperties {

    // key -> template
    // 예: play.byIndex = "{index}번 '{title}' 재생할게요."
    private Map<String, String> messages = new LinkedHashMap<String, String>();

    public Map<String, String> getMessages() {
        return messages;
    }

    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    public String get(String key) {
        if (key == null) return null;
        return messages.get(key);
    }
}