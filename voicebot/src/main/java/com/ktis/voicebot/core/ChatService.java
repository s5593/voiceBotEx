package com.ktis.voicebot.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.ktis.voicebot.api.dto.ChatRequest;
import com.ktis.voicebot.api.dto.ChatResponse;
import com.ktis.voicebot.core.dialog.DialogManager;
import com.ktis.voicebot.core.dialog.DialogOutcome;
import com.ktis.voicebot.core.log.TurnLogService;
import com.ktis.voicebot.core.model.NluResult;
import com.ktis.voicebot.core.model.SessionData;
import com.ktis.voicebot.core.model.UiContext;
import com.ktis.voicebot.core.nlu.NluClient;
import com.ktis.voicebot.core.session.SessionStore;
import com.ktis.voicebot.core.stt.TextNormalizer;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final SessionStore sessionStore;
    private final NluClient nluClient;
    private final DialogManager dialogManager;
    private final TurnLogService turnLogService;
    private final TextNormalizer textNormalizer;

    public ChatService(
            SessionStore sessionStore,
            NluClient nluClient,
            DialogManager dialogManager,
            TurnLogService turnLogService,
            TextNormalizer textNormalizer
    ) {
        this.sessionStore = sessionStore;
        this.nluClient = nluClient;
        this.dialogManager = dialogManager;
        this.turnLogService = turnLogService;
        this.textNormalizer = textNormalizer;

        log.info("[BOOT] NluClient injected = {}", nluClient.getClass().getName());
        log.info("[BOOT] TextNormalizer injected = {}", textNormalizer.getClass().getName());
    }

    public ChatResponse handle(ChatRequest req) {
        String sessionId = (req == null ? null : req.getSessionId());
        String rawText = (req == null ? null : req.getText());
        String text = textNormalizer.normalize(rawText);

        long start = System.currentTimeMillis();

        SessionData session = sessionStore.getOrCreate(sessionId);
        String stateBefore = session.getState().name();

        // UI 컨텍스트 반영 (요청이 오면 세션에 저장)
        applyUiContext(session, (req == null ? null : req.getUiContext()));

        NluResult nlu = nluClient.analyze(text);
        DialogOutcome outcome = dialogManager.next(session, nlu);

        session.setState(outcome.getNextState());
        session.setUpdatedAtEpochMs(System.currentTimeMillis());
        sessionStore.save(session);

        long latencyMs = System.currentTimeMillis() - start;

        turnLogService.logTurn(
                sessionId,
                text,
                outcome.getIntent().name(),
                stateBefore,
                session.getState().name(),
                latencyMs,
                outcome.getReplyText()
        );

        ChatResponse res = new ChatResponse();
        res.setSessionId(sessionId);
        res.setReplyText(outcome.getReplyText());
        res.setState(session.getState());
        res.setSlots(session.getSlots());

        Map<String, Object> debug = new HashMap<String, Object>();
        debug.put("intent", outcome.getIntent().name());
        debug.put("confidence", nlu.getConfidence());
        debug.put("normalizedText", text);
        debug.put("hasUiContext", Boolean.valueOf(session.getUiContext() != null));
        res.setDebug(debug);

        return res;
    }

    private void applyUiContext(SessionData session, UiContext incoming) {
        if (incoming == null) return;

        UiContext current = session.getUiContext();
        if (current == null) {
            session.setUiContext(incoming);
            return;
        }

        // null 아닌 필드만 덮어쓰기(merge)
        if (incoming.getScreenId() != null) current.setScreenId(incoming.getScreenId());
        if (incoming.getFocusIndex() != null) current.setFocusIndex(incoming.getFocusIndex());
        if (incoming.getLastAction() != null) current.setLastAction(incoming.getLastAction());

        List<UiContext.ContentItem> list = incoming.getLastContentList();
        if (list != null && !list.isEmpty()) current.setLastContentList(list);
    }
}