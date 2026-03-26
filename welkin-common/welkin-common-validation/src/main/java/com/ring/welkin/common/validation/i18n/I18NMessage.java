package com.ring.welkin.common.validation.i18n;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;

@Component
public class I18NMessage implements I18nKeys {

    private static MessageSourceAccessor accessor;
    public I18NMessage(@Autowired final MessageSourceAccessor accessor) {
        I18NMessage.accessor = accessor;
    }

    public static String getMessage(String parameterName, String code, String defaultMessage, Object... args) {
        String message = accessor.getMessage(code, args, defaultMessage);
        return StringUtils.join(parameterName, message);
    }

    public static String getMessage(String parameterName, String code, Object... args) {
        return getMessage(parameterName, code, null, args);
    }

    public static String rightFormatMessage(String parameterName) {
        return getMessage(parameterName, RightFormat);
    }

    public static String assertFalseMessage(String parameterName) {
        return getMessage(parameterName, AssertFalse);
    }

    public static String assertTrueMessage(String parameterName) {
        return getMessage(parameterName, AssertTrue);
    }

    public static String emailMessage(String parameterName) {
        return getMessage(parameterName, Email);
    }

    public static String futureMessage(String parameterName) {
        return getMessage(parameterName, Future);
    }

    public static String futureOrPresentMessage(String parameterName) {
        return getMessage(parameterName, FutureOrPresent);
    }

    public static String negativeMessage(String parameterName) {
        return getMessage(parameterName, Negative);
    }

    public static String negativeOrZero(String parameterName) {
        return getMessage(parameterName, NegativeOrZero);
    }

    public static String notBlankMessage(String parameterName) {
        return getMessage(parameterName, NotBlank);
    }

    public static String notEmptyMessage(String parameterName) {
        return getMessage(parameterName, NotEmpty);
    }

    public static String notNullMessage(String parameterName) {
        return getMessage(parameterName, NotNull);
    }

    public static String nullMessage(String parameterName) {
        return getMessage(parameterName, Null);
    }

    public static String pastMessage(String parameterName) {
        return getMessage(parameterName, Past);
    }

    public static String pastOrPresentMessage(String parameterName) {
        return getMessage(parameterName, PastOrPresent);
    }

    public static String positiveMessage(String parameterName) {
        return getMessage(parameterName, Positive);
    }

    public static String positiveOrZeroMessage(String parameterName) {
        return getMessage(parameterName, PositiveOrZero);
    }

    public static String phoneMessage(String parameterName) {
        return getMessage(parameterName, Phone);
    }

    public static String ipMessage(String parameterName) {
        return getMessage(parameterName, IP);
    }
}
