package org.apache.logging.log4j.core.jackson;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.logging.log4j.core.jackson.Initializers.SetupContextInitializer;
import org.apache.logging.log4j.core.layout.RegexReplaces;

class SensitiveLog4jJsonModule extends SimpleModule {

    private static final long serialVersionUID = 1L;
    private final boolean encodeThreadContextAsList;
    private final boolean includeStacktrace;
    private final boolean stacktraceAsString;
    private final boolean objectMessageAsJsonObject;
    private final RegexReplaces replaces;

    SensitiveLog4jJsonModule(final boolean encodeThreadContextAsList, final boolean includeStacktrace,
                             final boolean stacktraceAsString, final boolean objectMessageAsJsonObject, final RegexReplaces replaces) {
        super(SensitiveLog4jJsonModule.class.getName(), new Version(2, 0, 0, null, null, null));
        this.encodeThreadContextAsList = encodeThreadContextAsList;
        this.includeStacktrace = includeStacktrace;
        this.stacktraceAsString = stacktraceAsString;
        this.objectMessageAsJsonObject = objectMessageAsJsonObject;
        this.replaces = replaces;
        // MUST init here.
        // Calling this from setupModule is too late!
        // noinspection ThisEscapedInObjectConstruction
        new SensitiveSimpleModuleInitializer().initialize(this, objectMessageAsJsonObject, replaces);
    }

    @Override
    public void setupModule(final SetupContext context) {
        // Calling super is a MUST!
        super.setupModule(context);
        if (encodeThreadContextAsList) {
            new SetupContextInitializer().setupModule(context, includeStacktrace, stacktraceAsString);
        } else {
            new SensitiveSetupContextJsonInitializer().setupModule(context, includeStacktrace, stacktraceAsString);
        }
    }
}
