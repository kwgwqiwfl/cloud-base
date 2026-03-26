package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.ThreadContext.ContextStack;
import org.apache.logging.log4j.core.layout.RegexReplaces;
import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.ObjectMessage;

public class SensitiveSimpleModuleInitializer {
    void initialize(final SimpleModule simpleModule, final boolean objectMessageAsJsonObject, RegexReplaces replaces) {
        // Workaround because mix-ins do not work for classes that already have a
        // built-in deserializer.
        // See Jackson issue 429.
        simpleModule.addDeserializer(StackTraceElement.class, new Log4jStackTraceElementDeserializer());
        simpleModule.addDeserializer(ContextStack.class, new MutableThreadContextStackDeserializer());
        if (objectMessageAsJsonObject) {
            simpleModule.addSerializer(ObjectMessage.class, new SensitiveObjectMessageSerializer());
        }
        simpleModule.addSerializer(Message.class, new SensitiveMessageSerializer(replaces));
    }
}
