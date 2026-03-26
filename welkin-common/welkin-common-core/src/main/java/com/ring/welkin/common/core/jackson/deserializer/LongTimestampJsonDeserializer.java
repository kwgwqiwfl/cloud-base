package com.ring.welkin.common.core.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.regex.Pattern;

public final class LongTimestampJsonDeserializer extends JsonDeserializer<Long> implements Serializable {
    private static final long serialVersionUID = 3814918723893174662L;

    public final static LongTimestampJsonDeserializer instance = new LongTimestampJsonDeserializer();

    @Override
	public Long deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		String source = p.getText().trim();
		if (StringUtils.isNotEmpty(source)) {
			if (Pattern.matches("^[-+]?\\d{1,13}$", source)) {// 支持13位时间戳
				return Long.parseLong(source);
			}
			Date date = DateJsonDeserializer.instance.deserialize(p, ctxt);
			if (date != null) {
				return date.getTime();
			}
		}
		return null;
	}
}
