package com.ktis.voicebot.core.dialog;

import java.util.Collections;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ktis.voicebot.config.DialogMessagesProperties;

@Service
public class DialogMessageService {

    private final DialogMessagesProperties props;

    public DialogMessageService(DialogMessagesProperties props) {
        this.props = props;
    }

    public String msg(String key) {
        return msg(key, Collections.<String, String>emptyMap());
    }

    public String msg(String key, Map<String, String> params) {
        String template = props == null ? null : props.get(key);
        if (template == null || template.trim().isEmpty()) {
            // 설정 누락 시 안전 폴백
            return "요청을 처리할 수 없어요. 다시 말씀해 주세요.";
        }
        return apply(template, params);
    }

    private String apply(String template, Map<String, String> params) {
        if (params == null || params.isEmpty()) return template;

        String out = template;
        for (Map.Entry<String, String> e : params.entrySet()) {
            String k = e.getKey();
            String v = e.getValue();
            if (k == null) continue;
            if (v == null) v = "";
            out = out.replace("{" + k + "}", v);
        }
        return out;
    }

    public static Map<String, String> p(String k1, String v1) {
        java.util.Map<String, String> m = new java.util.HashMap<String, String>();
        m.put(k1, v1);
        return m;
    }

    public static Map<String, String> p(String k1, String v1, String k2, String v2) {
        java.util.Map<String, String> m = new java.util.HashMap<String, String>();
        m.put(k1, v1);
        m.put(k2, v2);
        return m;
    }

    public static Map<String, String> p(String k1, String v1, String k2, String v2, String k3, String v3) {
        java.util.Map<String, String> m = new java.util.HashMap<String, String>();
        m.put(k1, v1);
        m.put(k2, v2);
        m.put(k3, v3);
        return m;
    }
}