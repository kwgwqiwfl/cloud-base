package com.ring.welkin.common.core.jackson.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Date;

public final class SqlTimestampJsonDeserializer extends JsonDeserializer<Timestamp> implements Serializable {
    private static final long serialVersionUID = 3814918723893174662L;

    public final static SqlTimestampJsonDeserializer instance = new SqlTimestampJsonDeserializer();

    @Override
    public Timestamp deserialize(JsonParser p, DeserializationContext ctxt)
        throws IOException, JsonProcessingException {
        Date date = DateJsonDeserializer.instance.deserialize(p, ctxt);
        if (date != null) {
            return new Timestamp(date.getTime());
        }
        return null;
    }

}
