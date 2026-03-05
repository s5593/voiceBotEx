package com.ktis.voicebot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ktis.voicebot.core.model.Intent;
import com.ktis.voicebot.core.model.NluResult;
import com.ktis.voicebot.core.nlu.NluClient;
import com.ktis.voicebot.core.nlu.RuleBasedNluClient;

@Configuration
public class NluConfig {

    @Bean
    @ConditionalOnProperty(name = "bot.nlu.type", havingValue = "rule", matchIfMissing = true)
    public NluClient ruleNluClient(NluProperties props) {
        return new RuleBasedNluClient(props);
    }

    @Bean
    @ConditionalOnProperty(name = "bot.nlu.type", havingValue = "openai")
    public NluClient openAiNluClient() {
        return new OpenAiNluClientStub();
    }

    static class OpenAiNluClientStub implements NluClient {
        @Override
        public NluResult analyze(String text) {
            return new NluResult(Intent.UNKNOWN, 0.5);
        }
    }
}