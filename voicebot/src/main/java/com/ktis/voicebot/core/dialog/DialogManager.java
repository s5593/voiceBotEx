package com.ktis.voicebot.core.dialog;

import java.util.List;

import org.springframework.stereotype.Component;

import com.ktis.voicebot.core.model.Intent;
import com.ktis.voicebot.core.model.NluResult;
import com.ktis.voicebot.core.model.SessionData;
import com.ktis.voicebot.core.model.UiContext;

@Component
public class DialogManager {

    private final DialogMessageService messages;

    public DialogManager(DialogMessageService messages) {
        this.messages = messages;
    }

    public DialogOutcome next(SessionData session, NluResult nlu) {

        if (nlu == null || nlu.getConfidence() < 0.6) {
            return new DialogOutcome(messages.msg("sttmessages.msg(retry"), session.getState(), Intent.UNKNOWN);
        }

        Intent intent = nlu.getIntent();

        if (intent == Intent.OPEN_NETFLIX) {
            ensureUiContext(session);
            session.getUiContext().setLastAction("OPEN_NETFLIX");
            return new DialogOutcome(messages.msg("open_netflix"), session.getState(), intent);
        }

        if (intent == Intent.PLAY_BY_INDEX) {
            return handlePlayByIndex(session, nlu);
        }

        if (intent == Intent.PLAY_PREVIOUS) {
            return handleMoveAndPlay(session, -1, intent);
        }

        if (intent == Intent.PLAY_NEXT) {
            return handleMoveAndPlay(session, +1, intent);
        }

        if (intent == Intent.PLAY_OTHER) {
            return handlePlayOther(session, intent);
        }

        return new DialogOutcome(messages.msg("unknown"), session.getState(), Intent.UNKNOWN);
    }

    private DialogOutcome handlePlayByIndex(SessionData session, NluResult nlu) {
        ensureUiContext(session);

        UiContext ui = session.getUiContext();
        List<UiContext.ContentItem> list = ui.getLastContentList();

        if (list == null || list.isEmpty()) {
            return new DialogOutcome(messages.msg("list_missing"), session.getState(), Intent.PLAY_BY_INDEX);
        }

        String idxStr = nlu.getEntities().get("index");
        int idx = parsePositiveInt(idxStr, -1);

        if (idx <= 0) {
            return new DialogOutcome(messages.msg("index_ask"), session.getState(), Intent.PLAY_BY_INDEX);
        }

        int pos = idx - 1;
        if (pos < 0 || pos >= list.size()) {
            return new DialogOutcome(
                    messages.msg("index.outOfRange",
                            DialogMessageService.p("max", String.valueOf(list.size()))),
                    session.getState(),
                    Intent.PLAY_BY_INDEX
            );
        }

        UiContext.ContentItem item = list.get(pos);
        ui.setFocusIndex(Integer.valueOf(idx));
        ui.setLastAction("PLAY_BY_INDEX");

        String title = safeTitle(item);

        return new DialogOutcome(
                messages.msg("play.byIndex",
                        DialogMessageService.p("index", String.valueOf(idx), "title", title)),
                session.getState(),
                Intent.PLAY_BY_INDEX
        );
    }

    private DialogOutcome handleMoveAndPlay(SessionData session, int delta, Intent intent) {
        ensureUiContext(session);

        UiContext ui = session.getUiContext();
        List<UiContext.ContentItem> list = ui.getLastContentList();

        if (list == null || list.isEmpty()) {
            return new DialogOutcome(messages.msg("list_missing"), session.getState(), intent);
        }

        Integer focus = ui.getFocusIndex();
        if (focus == null) {
            return new DialogOutcome(messages.msg("focus_missing"), session.getState(), intent);
        }

        int next = focus.intValue() + delta;
        if (next < 1) next = 1;
        if (next > list.size()) next = list.size();

        UiContext.ContentItem item = list.get(next - 1);

        ui.setFocusIndex(Integer.valueOf(next));
        ui.setLastAction(intent.name());

        String title = safeTitle(item);

        return new DialogOutcome(
                messages.msg("play.byIndex",
                        DialogMessageService.p("index", String.valueOf(next), "title", title)),
                session.getState(),
                intent
        );
    }

    private DialogOutcome handlePlayOther(SessionData session, Intent intent) {
        ensureUiContext(session);

        UiContext ui = session.getUiContext();
        List<UiContext.ContentItem> list = ui.getLastContentList();

        if (list == null || list.isEmpty()) {
            ui.setLastAction("PLAY_OTHER_NO_LIST");
            return new DialogOutcome(messages.msg("other_noList"), session.getState(), intent);
        }

        Integer focus = ui.getFocusIndex();
        int next = 1;
        if (focus != null) {
            next = focus.intValue() + 1;
            if (next > list.size()) next = 1;
        }

        UiContext.ContentItem item = list.get(next - 1);

        ui.setFocusIndex(Integer.valueOf(next));
        ui.setLastAction("PLAY_OTHER");

        String title = safeTitle(item);

        return new DialogOutcome(
                messages.msg("play_byIndex",
                        DialogMessageService.p("index", String.valueOf(next), "title", title)),
                session.getState(),
                intent
        );
    }

    private void ensureUiContext(SessionData session) {
        if (session.getUiContext() == null) {
            session.setUiContext(new UiContext());
        }
    }

    private int parsePositiveInt(String s, int def) {
        if (s == null) return def;
        try {
            int v = Integer.parseInt(s.trim());
            return (v > 0) ? v : def;
        } catch (Exception e) {
            return def;
        }
    }

    private String safeTitle(UiContext.ContentItem item) {
        if (item == null) return "선택한 콘텐츠";
        String t = item.getTitle();
        if (t == null || t.trim().isEmpty()) return "선택한 콘텐츠";
        return t;
    }
}