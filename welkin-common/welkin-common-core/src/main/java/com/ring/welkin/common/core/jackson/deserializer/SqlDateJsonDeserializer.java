package com.ring.welkin.common.core.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Date;

public final class SqlDateJsonDeserializer extends JsonDeserializer<Date> implements Serializable {
	private static final long serialVersionUID = 3814918723893174662L;

	public final static SqlDateJsonDeserializer instance = new SqlDateJsonDeserializer();

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		java.util.Date date = DateJsonDeserializer.instance.deserialize(p, ctxt);
		if (date != null) {
			return new Date(date.getTime());
		}
		return null;
	}

}
