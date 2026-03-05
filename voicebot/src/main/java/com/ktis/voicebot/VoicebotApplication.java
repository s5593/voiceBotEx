package com.ktis.voicebot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackages = "com.ktis.voicebot.config")
public class VoicebotApplication {

    public static void main(String[] args) {
        SpringApplication.run(VoicebotApplication.class, args);
    }
}