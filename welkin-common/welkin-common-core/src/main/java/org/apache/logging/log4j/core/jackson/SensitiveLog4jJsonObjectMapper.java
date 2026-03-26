package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.core.layout.RegexReplaces;

/**
 * A Jackson JSON {@link ObjectMapper} initialized for Log4j.
 * <p>
 * <em>Consider this class private.</em>
 * </p>
 */
public class SensitiveLog4jJsonObjectMapper extends ObjectMapper {

    private static final long serialVersionUID = 1L;

    /**
     * Create a new instance using the {@link SensitiveLog4jJsonModule}.
     */
    public SensitiveLog4jJsonObjectMapper() {
        this(false, true, false, false, null);
    }

    /**
     * Create a new instance using the {@link SensitiveLog4jJsonModule}.
     */
    public SensitiveLog4jJsonObjectMapper(final boolean encodeThreadContextAsList, final boolean includeStacktrace,
                                          final boolean stacktraceAsString, final boolean objectMessageAsJsonObject, final RegexReplaces replaces) {
        this.registerModule(new SensitiveLog4jJsonModule(encodeThreadContextAsList, includeStacktrace,
                stacktraceAsString, objectMessageAsJsonObject, replaces));
        this.setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
    }
}
