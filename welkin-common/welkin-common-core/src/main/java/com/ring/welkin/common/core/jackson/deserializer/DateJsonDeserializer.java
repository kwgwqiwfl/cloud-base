package com.ring.welkin.common.core.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.DateDeserializers.DateDeserializer;
import com.ring.welkin.common.utils.DateUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.ParseException;

public final class DateJsonDeserializer extends DateDeserializer {
	private static final long serialVersionUID = 8161670483997066183L;

	public final static DateJsonDeserializer instance = new DateJsonDeserializer();

	@Override
	public java.util.Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		String source = p.getText().trim();
		if (StringUtils.isNotEmpty(source)) {
			try {
				return super.deserialize(p, ctxt);
			} catch (Exception e) {
				try {
					return DateUtils.parse(source);
				} catch (ParseException e1) {
					throw new RuntimeException("DateTime format that cannot be resolved:" + source, e1);
				}
			}
		}
		return null;
	}

}
