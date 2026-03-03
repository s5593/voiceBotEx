package com.ktis.voicebot.core.dialog;

import com.ktis.voicebot.core.model.ConversationState;
import com.ktis.voicebot.core.model.Intent;

public class DialogOutcome {
    private final String replyText;
    private final ConversationState nextState;
    private final Intent intent;

    public DialogOutcome(String replyText, ConversationState nextState, Intent intent) {
        this.replyText = replyText;
        this.nextState = nextState;
        this.intent = intent;
    }

    public String getReplyText() { return replyText; }
    public ConversationState getNextState() { return nextState; }
    public Intent getIntent() { return intent; }
}