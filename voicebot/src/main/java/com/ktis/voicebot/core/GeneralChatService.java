package com.ktis.voicebot.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ktis.voicebot.api.dto.ChatRequest;
import com.ktis.voicebot.api.dto.ChatResponse;
import com.ktis.voicebot.core.model.ConversationState;
import com.ktis.voicebot.core.model.SessionData;
import com.ktis.voicebot.core.openai.OpenAiClient;
import com.ktis.voicebot.core.session.SessionStore;

public class GeneralChatService {

    private final SessionStore sessionStore;
    private final OpenAiClient openAiClient;
    private final int maxHistoryTurns;

    private final ObjectMapper om = new ObjectMapper();

    public GeneralChatService(SessionStore sessionStore, OpenAiClient openAiClient, int maxHistoryTurns) {
        this.sessionStore = sessionStore;
        this.openAiClient = openAiClient;
        this.maxHistoryTurns = maxHistoryTurns;
    }

    public ChatResponse handle(ChatRequest req) throws Exception {
        long start = System.currentTimeMillis();

        SessionData session = sessionStore.getOrCreate(req.getSessionId());
        if (session.getHistory() == null) {
            session.setHistory(new ArrayList<SessionData.ChatTurn>());
        }

        // add user turn
        session.getHistory().add(SessionData.ChatTurn.user(req.getText()));
        trimHistory(session);

        // build Responses input as message array: [{role, content}, ...] :contentReference[oaicite:4]{index=4}
        ArrayNode input = om.createArrayNode();
        for (SessionData.ChatTurn t : session.getHistory()) {
            ObjectNode msg = om.createObjectNode();
            msg.put("role", t.getRole());
            msg.put("content", t.getContent());
            input.add(msg);
        }

        // Voice-friendly style: short, natural, one follow-up question max.
        String instructions =
                "너는 일상 대화용 보이스봇이다.\n" +
                "- 답변은 1~2문장으로 짧고 자연스럽게.\n" +
                "- 필요하면 후속 질문은 1개만.\n" +
                "- 모르면 모른다고 말하고, 추측은 단정하지 말 것.\n" +
                "- 사용자가 위험하거나 민감한 요청을 하면 안전한 방향으로 안내할 것.";

        String reply = openAiClient.generateReply(instructions, input);

        // add assistant turn + save
        session.getHistory().add(SessionData.ChatTurn.assistant(reply));
        trimHistory(session);
        session.setState(ConversationState.CHAT); // 새 상태를 쓰거나, 기존 START를 유지해도 됨
        sessionStore.save(session);

        long latencyMs = System.currentTimeMillis() - start;

        ChatResponse res = new ChatResponse();
        res.setSessionId(req.getSessionId());
        res.setReplyText(reply);
        res.setState(session.getState());
        res.setSlots(session.getSlots()); // 일반 대화는 slots 비워도 됨(기존 구조 유지용)

        Map<String, Object> debug = new HashMap<String, Object>();
        debug.put("mode", "GENERAL_CHAT");
        debug.put("latencyMs", Long.valueOf(latencyMs));
        res.setDebug(debug);

        return res;
    }

    private void trimHistory(SessionData session) {
        // keep last N turns (user/assistant each counts as 1)
        int max = Math.max(2, maxHistoryTurns * 2);
        List<SessionData.ChatTurn> h = session.getHistory();
        if (h.size() > max) {
            int from = h.size() - max;
            session.setHistory(new ArrayList<SessionData.ChatTurn>(h.subList(from, h.size())));
        }
    }
}