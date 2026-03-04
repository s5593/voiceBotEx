package com.ktis.voicebot.core.model;

public enum ConversationState {

    // 기본 상태 (일반 대화 또는 새로운 세션 시작 상태)
    START,

    // 사용자에게 계좌번호 입력을 요청하는 상태
    ASK_ACCOUNT,

    // 사용자에게 이체 금액을 입력받는 상태
    ASK_AMOUNT,

    // 실제 업무 처리 진행 중 상태 (잔액조회, 이체 등)
    PROCESSING,

    // 업무 플로우가 정상적으로 완료된 상태
    COMPLETE,
    
    // 일반 대화 진행 상태 (LLM 기반 대화)
    CHAT
}