package com.ktis.voicebot.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot.nlu")
public class NluProperties {

    private List<String> netflixKeywords = new ArrayList<String>();
    private List<String> playKeywords = new ArrayList<String>();
    private List<String> previousKeywords = new ArrayList<String>();
    private List<String> nextKeywords = new ArrayList<String>();
    private List<String> otherKeywords = new ArrayList<String>();

    public List<String> getNetflixKeywords() {
        return netflixKeywords;
    }

    public void setNetflixKeywords(List<String> netflixKeywords) {
        this.netflixKeywords = netflixKeywords;
    }

    public List<String> getPlayKeywords() {
        return playKeywords;
    }

    public void setPlayKeywords(List<String> playKeywords) {
        this.playKeywords = playKeywords;
    }

    public List<String> getPreviousKeywords() {
        return previousKeywords;
    }

    public void setPreviousKeywords(List<String> previousKeywords) {
        this.previousKeywords = previousKeywords;
    }

    public List<String> getNextKeywords() {
        return nextKeywords;
    }

    public void setNextKeywords(List<String> nextKeywords) {
        this.nextKeywords = nextKeywords;
    }

    public List<String> getOtherKeywords() {
        return otherKeywords;
    }

    public void setOtherKeywords(List<String> otherKeywords) {
        this.otherKeywords = otherKeywords;
    }
}