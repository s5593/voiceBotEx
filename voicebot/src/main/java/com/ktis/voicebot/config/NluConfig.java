package com.ktis.voicebot.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.ktis.voicebot.core.nlu.NluClient;
import com.ktis.voicebot.core.nlu.RuleBasedNluClient;

@Configuration
public class NluConfig {

    @Bean
    @ConditionalOnProperty(name = "bot.nlu.type", havingValue = "rule", matchIfMissing = true)
    public NluClient ruleNluClient() {
        return new RuleBasedNluClient();
    }

    // OpenAI 연동은 오늘 "스텁" 먼저 붙인다.
    @Bean
    @ConditionalOnProperty(name = "bot.nlu.type", havingValue = "openai")
    public NluClient openAiNluClient() {
        return new OpenAiNluClientStub();
    }

    /**
     * 오늘은 구조만 완성하기 위해 스텁으로 둔다.
     * 내일 OpenAI API 실제 호출로 교체하면 된다.
     */
    static class OpenAiNluClientStub implements NluClient {
        @Override
        public com.ktis.voicebot.core.model.NluResult analyze(String text) {
            // 지금은 규칙 기반과 동일하게 동작하도록 우회하거나,
            // UNKNOWN으로 두고 DM이 재질문하게 해도 된다.
            // 일단 UNKNOWN 처리:
            return new com.ktis.voicebot.core.model.NluResult(
                    com.ktis.voicebot.core.model.Intent.UNKNOWN,
                    0.5
            );
        }
    }
}