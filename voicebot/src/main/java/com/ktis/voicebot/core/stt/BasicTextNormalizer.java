package com.ktis.voicebot.core.stt;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import com.ktis.voicebot.config.NormalizerProperties;

@Component
public class BasicTextNormalizer implements TextNormalizer {

	private static final Pattern MULTI_SPACE = Pattern.compile("\\s+");
	private static final Pattern DIGIT_SPACE_BUN = Pattern.compile("(\\d+)\\s*번");

	private final NormalizerProperties props;

	public BasicTextNormalizer(NormalizerProperties props) {
		this.props = props;
	}

	@Override
	public String normalize(String raw) {
		if (raw == null) return "";

		String s = raw;

		// trim
		s = s.trim();

		// 제어문자 -> 공백
		s = s.replace('\n', ' ').replace('\r', ' ').replace('\t', ' ');

		// 다중 공백 -> 1칸
		s = MULTI_SPACE.matcher(s).replaceAll(" ");

		// "2 번" -> "2번" (패턴 기반)
		s = normalizeDigitBun(s);

		// 텍스트 치환은 설정으로만 (필요할 때만 enable)
		s = applyReplaceMapIfEnabled(s);

		// 양끝 따옴표 제거
		s = stripWrappingQuotes(s);

		return s;
	}

	private String normalizeDigitBun(String s) {
		Matcher m = DIGIT_SPACE_BUN.matcher(s);
		StringBuffer sb = new StringBuffer();
		while (m.find()) {
			String num = m.group(1);
			m.appendReplacement(sb, num + "번");
		}
		m.appendTail(sb);
		return sb.toString();
	}

	private String applyReplaceMapIfEnabled(String s) {
		if (props == null) return s;
		if (!props.isEnableReplaceMap()) return s;

		Map<String, String> map = props.getReplaceMap();
		if (map == null || map.isEmpty()) return s;

		String out = s;
		for (Map.Entry<String, String> e : map.entrySet()) {
			String from = e.getKey();
			String to = e.getValue();
			if (from == null || from.isEmpty() || to == null) continue;
			out = out.replace(from, to);
		}
		return out;
	}

	private String stripWrappingQuotes(String s) {
		if (s == null) return "";
		String t = s.trim();
		if (t.length() >= 2) {
			char first = t.charAt(0);
			char last = t.charAt(t.length() - 1);
			if ((first == '"' && last == '"') ||
				(first == '\'' && last == '\'') ||
				(first == '“' && last == '”') ||
				(first == '‘' && last == '’')) {
				return t.substring(1, t.length() - 1).trim();
			}
		}
		return t;
	}
}