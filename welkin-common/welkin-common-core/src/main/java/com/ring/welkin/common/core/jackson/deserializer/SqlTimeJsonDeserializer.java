package com.ring.welkin.common.core.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public final class SqlTimeJsonDeserializer extends JsonDeserializer<Time> implements Serializable {
	private static final long serialVersionUID = 3814918723893174662L;

	public final static SqlTimeJsonDeserializer instance = new SqlTimeJsonDeserializer();

	@Override
	public Time deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Date date = DateJsonDeserializer.instance.deserialize(p, ctxt);
		if (date != null) {
			return new Time(date.getTime());
		}
		return null;
	}

}
