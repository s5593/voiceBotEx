package com.ktis.voicebot.core.nlu;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktis.voicebot.config.NluProperties;
import com.ktis.voicebot.core.model.Intent;
import com.ktis.voicebot.core.model.NluResult;

public class RuleBasedNluClient implements NluClient {

    private static final Logger log = LoggerFactory.getLogger(RuleBasedNluClient.class);

    private static final Pattern INDEX_PATTERN = Pattern.compile("(\\d{1,3})\\s*번");

    private final NluProperties props;

    public RuleBasedNluClient(NluProperties props) {
        this.props = props;
    }

    @Override
    public NluResult analyze(String text) {

        String t = (text == null) ? "" : text.trim();
        Intent intent = Intent.UNKNOWN;

        if (containsAny(t, props.getNetflixKeywords())) {
            intent = Intent.OPEN_NETFLIX;
        }

        if (intent == Intent.UNKNOWN && containsAny(t, props.getPlayKeywords())) {

            Matcher m = INDEX_PATTERN.matcher(t);
            if (m.find()) {
                intent = Intent.PLAY_BY_INDEX;
            }
            else if (containsAny(t, props.getPreviousKeywords())) {
                intent = Intent.PLAY_PREVIOUS;
            }
            else if (containsAny(t, props.getNextKeywords())) {
                intent = Intent.PLAY_NEXT;
            }
            else if (containsAny(t, props.getOtherKeywords())) {
                intent = Intent.PLAY_OTHER;
            }
        }

        double confidence = (intent == Intent.UNKNOWN) ? 0.5 : 0.9;

        NluResult r = new NluResult(intent, confidence);

        Matcher m = INDEX_PATTERN.matcher(t);
        if (m.find()) {
            r.getEntities().put("index", m.group(1));
        }

        log.info("[NLU] intent={}, text={}", intent, t);

        return r;
    }

    private boolean containsAny(String text, List<String> list) {

        if (text == null || list == null) return false;

        for (String k : list) {
            if (text.contains(k)) {
                return true;
            }
        }

        return false;
    }
}