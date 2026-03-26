package org.apache.logging.log4j.core.layout;

import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import org.apache.logging.log4j.core.config.Node;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginBuilderFactory;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.util.KeyValuePair;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * Note: The JsonLayout should be considered to be deprecated. Please use
 * JsonTemplateLayout instead.
 * <p>
 * Appends a series of JSON events as strings serialized as bytes.
 *
 * <h3>Complete well-formed JSON vs. fragment JSON</h3>
 * <p>
 * If you configure {@code complete="true"}, the appender outputs a well-formed
 * JSON document. By default, with {@code complete="false"}, you should include
 * the output as an <em>external file</em> in a separate file to form a
 * well-formed JSON document.
 * </p>
 * <p>
 * If {@code complete="false"}, the appender does not write the JSON open array
 * character "[" at the start of the document, "]" and the end, nor comma ","
 * between records.
 * </p>
 * <h3>Encoding</h3>
 * <p>
 * Appenders using this layout should have their {@code charset} set to
 * {@code UTF-8} or {@code UTF-16}, otherwise events containing non ASCII
 * characters could result in corrupted log files.
 * </p>
 * <h3>Pretty vs. compact JSON</h3>
 * <p>
 * By default, the JSON layout is not compact (a.k.a. "pretty") with
 * {@code compact="false"}, which means the appender uses end-of-line characters
 * and indents lines to format the text. If {@code compact="true"}, then no
 * end-of-line or indentation is used. Message content may contain, of course,
 * escaped end-of-lines.
 * </p>
 * <h3>Additional Fields</h3>
 * <p>
 * This property allows addition of custom fields into generated JSON.
 * {@code <JsonLayout><KeyValuePair key="foo" value="bar"/></JsonLayout>}
 * inserts {@code "foo":"bar"} directly into JSON output. Supports Lookup
 * expressions.
 * </p>
 */
@Plugin(name = "SensitiveJsonLayout", category = Node.CATEGORY, elementType = Layout.ELEMENT_TYPE, printObject = true)
public final class SensitiveJsonLayout extends AbstractJacksonLayout {

    private static final String DEFAULT_FOOTER = "]";

    private static final String DEFAULT_HEADER = "[";

    static final String CONTENT_TYPE = "application/json";

    public static class Builder<B extends Builder<B>> extends AbstractJacksonLayout.Builder<B>
            implements org.apache.logging.log4j.core.util.Builder<SensitiveJsonLayout> {

        @PluginBuilderAttribute
        private boolean propertiesAsList;

        @PluginBuilderAttribute
        private boolean objectMessageAsJsonObject;

        @PluginElement("AdditionalField")
        private KeyValuePair[] additionalFields;

        @PluginElement("RegexReplaces")
        private RegexReplaces replaces;

        public Builder() {
            setCharset(StandardCharsets.UTF_8);
        }

        @Override
        public SensitiveJsonLayout build() {
            final boolean encodeThreadContextAsList = isProperties() && propertiesAsList;
            final String headerPattern = toStringOrNull(getHeader());
            final String footerPattern = toStringOrNull(getFooter());
            return new SensitiveJsonLayout(getConfiguration(), isLocationInfo(), isProperties(),
                    encodeThreadContextAsList, isComplete(), isCompact(), getEventEol(), getEndOfLine(), headerPattern,
                    footerPattern, getCharset(), isIncludeStacktrace(), isStacktraceAsString(),
                    isIncludeNullDelimiter(), isIncludeTimeMillis(), getAdditionalFields(),
                    getObjectMessageAsJsonObject(), getReplaces());
        }

        public boolean isPropertiesAsList() {
            return propertiesAsList;
        }

        public B setPropertiesAsList(final boolean propertiesAsList) {
            this.propertiesAsList = propertiesAsList;
            return asBuilder();
        }

        public boolean getObjectMessageAsJsonObject() {
            return objectMessageAsJsonObject;
        }

        public B setObjectMessageAsJsonObject(final boolean objectMessageAsJsonObject) {
            this.objectMessageAsJsonObject = objectMessageAsJsonObject;
            return asBuilder();
        }

        @Override
        public KeyValuePair[] getAdditionalFields() {
            return additionalFields;
        }

        @Override
        public B setAdditionalFields(final KeyValuePair[] additionalFields) {
            this.additionalFields = additionalFields;
            return asBuilder();
        }

        public RegexReplaces getReplaces() {
            return replaces;
        }

        //		@Override
        public B setReplaces(final RegexReplaces replaces) {
            this.replaces = replaces;
            return asBuilder();
        }
    }

    private SensitiveJsonLayout(final Configuration config, final boolean locationInfo, final boolean properties,
                                final boolean encodeThreadContextAsList, final boolean complete, final boolean compact,
                                final boolean eventEol, final String endOfLine, final String headerPattern, final String footerPattern,
                                final Charset charset, final boolean includeStacktrace, final boolean stacktraceAsString,
                                final boolean includeNullDelimiter, final boolean includeTimeMillis, final KeyValuePair[] additionalFields,
                                final boolean objectMessageAsJsonObject, RegexReplaces replaces) {
        super(config,
                new SensitiveJacksonFactory(encodeThreadContextAsList, includeStacktrace, stacktraceAsString,
                        objectMessageAsJsonObject, replaces).newWriter(locationInfo, properties, compact, includeTimeMillis),
                charset, compact, complete, eventEol, endOfLine,
                PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(headerPattern)
                        .setDefaultPattern(DEFAULT_HEADER).build(),
                PatternLayout.newSerializerBuilder().setConfiguration(config).setPattern(footerPattern)
                        .setDefaultPattern(DEFAULT_FOOTER).build(),
                includeNullDelimiter, additionalFields);
    }

    /**
     * Returns appropriate JSON header.
     *
     * @return a byte array containing the header, opening the JSON array.
     */
    @Override
    public byte[] getHeader() {
        if (!this.complete) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        final String str = serializeToString(getHeaderSerializer());
        if (str != null) {
            buf.append(str);
        }
        buf.append(this.eol);
        return getBytes(buf.toString());
    }

    /**
     * Returns appropriate JSON footer.
     *
     * @return a byte array containing the footer, closing the JSON array.
     */
    @Override
    public byte[] getFooter() {
        if (!this.complete) {
            return null;
        }
        final StringBuilder buf = new StringBuilder();
        buf.append(this.eol);
        final String str = serializeToString(getFooterSerializer());
        if (str != null) {
            buf.append(str);
        }
        buf.append(this.eol);
        return getBytes(buf.toString());
    }

    @Override
    public Map<String, String> getContentFormat() {
        final Map<String, String> result = new HashMap<>();
        result.put("version", "2.0");
        return result;
    }

    /**
     * @return The content type.
     */
    @Override
    public String getContentType() {
        return CONTENT_TYPE + "; charset=" + this.getCharset();
    }

    @PluginBuilderFactory
    public static <B extends Builder<B>> B newBuilder() {
        return new Builder<B>().asBuilder();
    }

    /**
     * Creates a JSON Layout using the default settings. Useful for testing.
     *
     * @return A JSON Layout.
     */
    public static SensitiveJsonLayout createDefaultLayout() {
        return new SensitiveJsonLayout(new DefaultConfiguration(), false, false, false, false, false, false, null,
                DEFAULT_HEADER, DEFAULT_FOOTER, StandardCharsets.UTF_8, true, false, false, false, null, false, null);
    }

    @Override
    public void toSerializable(final LogEvent event, final Writer writer) throws IOException {
        if (complete && eventCount > 0) {
            writer.append(", ");
        }
        super.toSerializable(event, writer);
    }
}
