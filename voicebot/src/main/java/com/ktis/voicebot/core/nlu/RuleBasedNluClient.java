package com.ktis.voicebot.core.nlu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktis.voicebot.core.model.Intent;
import com.ktis.voicebot.core.model.NluResult;

public class RuleBasedNluClient implements NluClient {

    private static final Logger log = LoggerFactory.getLogger(RuleBasedNluClient.class);

    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("\\b\\d{6,12}\\b");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("\\b(\\d{1,9})\\s*(원)?\\b");

    public RuleBasedNluClient() {
        // 부팅 시점에 빈이 실제로 생성되는지 확인
        log.info("[BOOT] RuleBasedNluClient created");
    }

    @Override
    public NluResult analyze(String text) {
        String t = (text == null) ? "" : text.trim();

        // 호출 여부/입력 확인
        log.info("[NLU] input='{}'", t);

        Intent intent = Intent.UNKNOWN;
        if (t.contains("잔액")) {
            intent = Intent.CHECK_BALANCE;
        } else if (t.contains("이체") || t.contains("송금") || t.contains("보내")) {
            intent = Intent.TRANSFER;
        }

        double confidence = (intent == Intent.UNKNOWN) ? 0.5 : 0.9;
        NluResult r = new NluResult(intent, confidence);

        Matcher m1 = ACCOUNT_PATTERN.matcher(t);
        if (m1.find()) {
            r.getEntities().put("accountNumber", m1.group());
        }

        Matcher m2 = AMOUNT_PATTERN.matcher(t);
        if (m2.find()) {
            r.getEntities().put("amount", m2.group(1));
        }

        // 결과 확인 (intent/신뢰도/엔티티)
        log.info("[NLU] output intent={}, confidence={}, entities={}",
                r.getIntent(),
                Double.valueOf(r.getConfidence()),
                r.getEntities());

        return r;
    }
}