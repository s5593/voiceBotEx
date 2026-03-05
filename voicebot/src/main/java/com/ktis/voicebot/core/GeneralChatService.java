package com.ktis.voicebot.core;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ktis.voicebot.config.OpenAiProperties;
import com.ktis.voicebot.core.model.SessionData;
import com.ktis.voicebot.core.openai.OpenAiClient;

@Service
public class GeneralChatService {

    private final OpenAiClient openAiClient;
    private final OpenAiProperties openAiProperties;

    private final ObjectMapper om = new ObjectMapper();

    public GeneralChatService(OpenAiClient openAiClient, OpenAiProperties openAiProperties) {
        this.openAiClient = openAiClient;
        this.openAiProperties = openAiProperties;
    }

    public String generate(SessionData session, String userText) {
        try {
            ensureHistory(session);

            session.getHistory().add(SessionData.ChatTurn.user(userText));
            trimHistory(session, openAiProperties.getMaxHistoryTurns());

            ArrayNode input = om.createArrayNode();
            for (SessionData.ChatTurn t : session.getHistory()) {
                ObjectNode msg = om.createObjectNode();
                msg.put("role", t.getRole());
                msg.put("content", t.getContent());
                input.add(msg);
            }

            String instructions =
                    "너는 TV/모바일 환경의 일상 대화용 보이스봇이다.\n" +
                    "- 답변은 1~2문장으로 짧고 자연스럽게.\n" +
                    "- 필요하면 후속 질문은 1개만.\n" +
                    "- 모르면 모른다고 말하고, 추측은 단정하지 말 것.\n" +
                    "- 위험하거나 민감한 요청은 안전한 방향으로 안내할 것.";

            String reply = openAiClient.generateReply(instructions, input);

            session.getHistory().add(SessionData.ChatTurn.assistant(reply));
            trimHistory(session, openAiProperties.getMaxHistoryTurns());

            return reply;
        } catch (Exception e) {
            return "지금은 응답을 만들기 어려워요. 다시 한 번만 말씀해 주세요.";
        }
    }

    private void ensureHistory(SessionData session) {
        if (session.getHistory() == null) {
            session.setHistory(new ArrayList<SessionData.ChatTurn>());
        }
    }

    private void trimHistory(SessionData session, int maxHistoryTurns) {
        int max = Math.max(2, maxHistoryTurns * 2);
        List<SessionData.ChatTurn> h = session.getHistory();
        if (h.size() > max) {
            int from = h.size() - max;
            session.setHistory(new ArrayList<SessionData.ChatTurn>(h.subList(from, h.size())));
        }
    }
}