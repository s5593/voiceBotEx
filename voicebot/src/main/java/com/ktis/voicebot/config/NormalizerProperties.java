package com.ktis.voicebot.config;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bot.normalizer")
public class NormalizerProperties {

    // true면 replaceMap 적용
    private boolean enableReplaceMap = false;

    // 치환 테이블 (순서 보장 위해 LinkedHashMap 권장)
    private Map<String, String> replaceMap = new LinkedHashMap<String, String>();

    public boolean isEnableReplaceMap() {
        return enableReplaceMap;
    }

    public void setEnableReplaceMap(boolean enableReplaceMap) {
        this.enableReplaceMap = enableReplaceMap;
    }

    public Map<String, String> getReplaceMap() {
        return replaceMap;
    }

    public void setReplaceMap(Map<String, String> replaceMap) {
        this.replaceMap = replaceMap;
    }
}