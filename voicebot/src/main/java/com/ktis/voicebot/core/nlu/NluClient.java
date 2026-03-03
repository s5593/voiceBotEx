package com.ktis.voicebot.core.nlu;

import com.ktis.voicebot.core.model.NluResult;

public interface NluClient {
    NluResult analyze(String text);
}