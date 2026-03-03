package com.ktis.voicebot.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot")
public class BotProperties {

    private final Session session = new Session();
    private final Nlu nlu = new Nlu();

    public Session getSession() { return session; }
    public Nlu getNlu() { return nlu; }

    public static class Session {
        private long ttlSeconds = 600;
        public long getTtlSeconds() { return ttlSeconds; }
        public void setTtlSeconds(long ttlSeconds) { this.ttlSeconds = ttlSeconds; }
    }

    public static class Nlu {
        private String type = "rule";
        public String getType() { return type; }
        public void setType(String type) { this.type = type; }
    }
}