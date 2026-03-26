package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.apache.logging.log4j.core.layout.RegexReplaces;
import org.apache.logging.log4j.message.Message;

import java.io.IOException;

public class SensitiveMessageSerializer extends StdScalarSerializer<Message> {

    private static final long serialVersionUID = 1L;

    private final RegexReplaces replaces;

    SensitiveMessageSerializer(final RegexReplaces replaces) {
        super(Message.class);
        this.replaces = replaces;
    }

    @Override
    public void serialize(final Message value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException, JsonGenerationException {
        String formattedMessage = value.getFormattedMessage();
        if (replaces != null && formattedMessage != null && !formattedMessage.isEmpty()) {
            formattedMessage = replaces.format(formattedMessage);
        }
        jgen.writeString(formattedMessage);
    }

}
