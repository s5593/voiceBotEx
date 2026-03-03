package com.ktis.voicebot.core.nlu;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.ktis.voicebot.core.model.Intent;
import com.ktis.voicebot.core.model.NluResult;


public class RuleBasedNluClient implements NluClient {

    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("\\b\\d{6,12}\\b");
    private static final Pattern AMOUNT_PATTERN = Pattern.compile("\\b(\\d{1,9})\\s*(원)?\\b");

    @Override
    public NluResult analyze(String text) {
        String t = text == null ? "" : text.trim();

        Intent intent = Intent.UNKNOWN;
        if (t.contains("잔액")) intent = Intent.CHECK_BALANCE;
        else if (t.contains("이체") || t.contains("송금") || t.contains("보내")) intent = Intent.TRANSFER;

        NluResult r = new NluResult(intent, intent == Intent.UNKNOWN ? 0.5 : 0.9);

        Matcher m1 = ACCOUNT_PATTERN.matcher(t);
        if (m1.find()) r.getEntities().put("accountNumber", m1.group());

        Matcher m2 = AMOUNT_PATTERN.matcher(t);
        if (m2.find()) r.getEntities().put("amount", m2.group(1));

        return r;
    }
}