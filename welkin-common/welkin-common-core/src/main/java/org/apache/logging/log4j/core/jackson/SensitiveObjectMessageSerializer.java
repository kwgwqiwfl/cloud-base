package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;
import org.apache.logging.log4j.message.ObjectMessage;

import java.io.IOException;

/**
 * <p>
 * <em>Consider this class private.</em>
 * </p>
 */
public class SensitiveObjectMessageSerializer extends StdScalarSerializer<ObjectMessage> {

    private static final long serialVersionUID = 1L;

    SensitiveObjectMessageSerializer() {
        super(ObjectMessage.class);
    }

    @Override
    public void serialize(final ObjectMessage value, final JsonGenerator jgen, final SerializerProvider provider)
            throws IOException, JsonGenerationException {
        jgen.writeObject(value.getParameter());
    }
}
