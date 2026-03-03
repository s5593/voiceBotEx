package com.ktis.voicebot.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TurnLogService {
    private static final Logger log = LoggerFactory.getLogger(TurnLogService.class);

    public void logTurn(String sessionId, String userText, String intent,
	            String stateBefore, String stateAfter, long latencyMs,
	            String replyText) {
	log.info("turn sessionId={} intent={} state={}->{} latencyMs={} userText={} replyText={}",
	    sessionId, intent, stateBefore, stateAfter, latencyMs, userText, replyText);
	}
}