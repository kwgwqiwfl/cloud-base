package com.ring.welkin.common.validation.constraints;

import com.ring.welkin.common.utils.Assert;
import com.ring.welkin.common.utils.RegexUtils;
import com.ring.welkin.common.validation.i18n.I18NMessage;

import java.util.Collection;
import java.util.Map;

public class Validate {
	public static String getStringValue(Map<String, ?> map, String key) {
		Object x = map.get(key);
		if (x == null) {
			x = "";
		}
		return x.toString();
	}

	public static <T> void notNull(T value, String parameterName) {
		Assert.notNull(value, I18NMessage.notNullMessage(parameterName));
	}

	public static void notNull(Map<String, ?> map, String key, String parameterName) {
		notNull(map.get(key), I18NMessage.notNullMessage(parameterName));
	}

	public static void notBlank(String value, String parameterName) {
		Assert.isNotBlank(value, I18NMessage.notBlankMessage(parameterName));
	}

	public static void notBlank(Map<String, ?> map, String key, String parameterName) {
		notBlank(getStringValue(map, key), parameterName);
	}

	public static void rightFormat(boolean expression, String parameterName) {
		Assert.isTrue(expression, I18NMessage.rightFormatMessage(parameterName));
	}

	public static <T> void notEmpty(T[] array, String parameterName) {
		Assert.notEmpty(array, I18NMessage.notEmptyMessage(parameterName));
	}

	public static void notEmpty(Map<?, ?> map, String parameterName) {
		Assert.notEmpty(map, I18NMessage.notEmptyMessage(parameterName));
	}

	public static void notEmpty(Collection<?> collection, String parameterName) {
		Assert.notEmpty(collection, I18NMessage.notEmptyMessage(parameterName));
	}

	public static void email(String value, String parameterName) {
		Assert.isTrue(RegexUtils.checkEmail(value), I18NMessage.emailMessage(parameterName));
	}

	public static void phone(String value, String parameterName) {
		Assert.isTrue(RegexUtils.checkPhone(value), I18NMessage.phoneMessage(parameterName));
	}

	public static void ip(String value, String parameterName) {
		Assert.isTrue(RegexUtils.checkIp(value), I18NMessage.ipMessage(parameterName));
	}

}
