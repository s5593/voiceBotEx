package com.ktis.voicebot.core.biz;

import org.springframework.stereotype.Service;

@Service
public class BusinessApiService {
    public String getBalance(String accountNumber) {
        return "30000"; // mock
    }

    public boolean transfer(String accountNumber, String amount) {
        return true; // mock
    }
}