package com.ktis.voicebot.api;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.*;

import com.ktis.voicebot.api.dto.ChatRequest;
import com.ktis.voicebot.api.dto.ChatResponse;
import com.ktis.voicebot.core.dialog.DialogManager;
import com.ktis.voicebot.core.dialog.DialogOutcome;
import com.ktis.voicebot.core.model.NluResult;
import com.ktis.voicebot.core.model.SessionData;
import com.ktis.voicebot.core.nlu.NluClient;
import com.ktis.voicebot.core.session.SessionStore;
import com.ktis.voicebot.core.log.TurnLogService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class ChatController {

    private final SessionStore sessionStore;
    private final NluClient nluClient;
    private final DialogManager dialogManager;
    private final TurnLogService turnLogService;

    public ChatController(SessionStore sessionStore, NluClient nluClient, DialogManager dialogManager, TurnLogService turnLogService) {
        this.sessionStore = sessionStore;
        this.nluClient = nluClient;
        this.dialogManager = dialogManager;
        this.turnLogService = turnLogService;
    }

    @PostMapping("/chat")
    public ChatResponse handle(ChatRequest req) {
        long start = System.currentTimeMillis();

        SessionData session = sessionStore.getOrCreate(req.getSessionId());
        String stateBefore = session.getState().name();

        NluResult nlu = nluClient.analyze(req.getText());
        DialogOutcome outcome = dialogManager.next(session, nlu);

        session.setState(outcome.getNextState());
        sessionStore.save(session);

        long latencyMs = System.currentTimeMillis() - start;

        turnLogService.logTurn(
                req.getSessionId(),
                req.getText(),
                outcome.getIntent().name(),
                stateBefore,
                session.getState().name(),
                latencyMs,
                outcome.getReplyText()
        );

        ChatResponse res = new ChatResponse();
        res.setSessionId(req.getSessionId());
        res.setReplyText(outcome.getReplyText());
        res.setState(session.getState());
        res.setSlots(session.getSlots());

        Map<String, Object> debug = new HashMap<>();
        debug.put("intent", outcome.getIntent().name());
        debug.put("confidence", nlu.getConfidence());
        res.setDebug(debug);

        return res;
    }
}