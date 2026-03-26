package org.apache.logging.log4j.core.layout;

import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.MinimalPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.core.jackson.JsonConstants;
import org.apache.logging.log4j.core.jackson.SensitiveLog4jJsonObjectMapper;

public class SensitiveJacksonFactory extends JacksonFactory {

    private final boolean encodeThreadContextAsList;
    private final boolean includeStacktrace;
    private final boolean stacktraceAsString;
    private final boolean objectMessageAsJsonObject;
    private final RegexReplaces replaces;

    public SensitiveJacksonFactory(final boolean encodeThreadContextAsList, final boolean includeStacktrace,
                                   final boolean stacktraceAsString, final boolean objectMessageAsJsonObject, final RegexReplaces replaces) {
        this.encodeThreadContextAsList = encodeThreadContextAsList;
        this.includeStacktrace = includeStacktrace;
        this.stacktraceAsString = stacktraceAsString;
        this.objectMessageAsJsonObject = objectMessageAsJsonObject;
        this.replaces = replaces;
    }

    @Override
    protected String getPropertNameForContextMap() {
        return JsonConstants.ELT_CONTEXT_MAP;
    }

    @Override
    protected String getPropertyNameForTimeMillis() {
        return JsonConstants.ELT_TIME_MILLIS;
    }

    @Override
    protected String getPropertyNameForInstant() {
        return JsonConstants.ELT_INSTANT;
    }

    @Override
    protected String getPropertNameForSource() {
        return JsonConstants.ELT_SOURCE;
    }

    @Override
    protected String getPropertNameForNanoTime() {
        return JsonConstants.ELT_NANO_TIME;
    }

    @Override
    protected PrettyPrinter newCompactPrinter() {
        return new MinimalPrettyPrinter();
    }

    @Override
    protected ObjectMapper newObjectMapper() {
        return new SensitiveLog4jJsonObjectMapper(encodeThreadContextAsList, includeStacktrace, stacktraceAsString,
                objectMessageAsJsonObject, replaces);
    }

    @Override
    protected PrettyPrinter newPrettyPrinter() {
        return new DefaultPrettyPrinter();
    }
}
