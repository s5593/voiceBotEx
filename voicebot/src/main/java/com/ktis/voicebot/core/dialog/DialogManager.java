package com.ktis.voicebot.core.dialog;

import org.springframework.stereotype.Component;

import com.ktis.voicebot.core.biz.BusinessApiService;
import com.ktis.voicebot.core.model.ConversationState;
import com.ktis.voicebot.core.model.Intent;
import com.ktis.voicebot.core.model.NluResult;
import com.ktis.voicebot.core.model.SessionData;

@Component
public class DialogManager {

    private final BusinessApiService biz;

    public DialogManager(BusinessApiService biz) {
        this.biz = biz;
    }

    public DialogOutcome next(SessionData session, NluResult nlu) {
        // slot filling
        if (nlu.getEntities().containsKey("accountNumber")) {
            session.getSlots().put("accountNumber", nlu.getEntities().get("accountNumber"));
        }
        if (nlu.getEntities().containsKey("amount")) {
            session.getSlots().put("amount", nlu.getEntities().get("amount"));
        }

        if (nlu.getConfidence() < 0.6) {
            return new DialogOutcome("잘 못 들었어요. 다시 말씀해 주세요.", session.getState(), nlu.getIntent());
        }

        Intent intent = nlu.getIntent();
        String account = session.getSlots().get("accountNumber");

        if (intent == Intent.CHECK_BALANCE) {
            if (account == null) {
                return new DialogOutcome("계좌번호를 말씀해 주세요.", ConversationState.ASK_ACCOUNT, intent);
            }
            String balance = biz.getBalance(account);
            return new DialogOutcome("잔액은 " + balance + "원입니다. 다른 업무를 도와드릴까요?", ConversationState.COMPLETE, intent);
        }

        if (intent == Intent.TRANSFER) {
            String amount = session.getSlots().get("amount");
            if (account == null) {
                return new DialogOutcome("이체할 계좌번호를 말씀해 주세요.", ConversationState.ASK_ACCOUNT, intent);
            }
            if (amount == null) {
                return new DialogOutcome("이체 금액을 말씀해 주세요.", ConversationState.ASK_AMOUNT, intent);
            }
            boolean ok = biz.transfer(account, amount);
            return new DialogOutcome(ok ? "이체가 완료되었습니다." : "이체에 실패했습니다. 잠시 후 다시 시도해 주세요.", ConversationState.COMPLETE, intent);
        }

        return new DialogOutcome("요청을 이해하지 못했어요. 잔액 조회 또는 이체처럼 말씀해 주세요.", session.getState(), intent);
    }
}