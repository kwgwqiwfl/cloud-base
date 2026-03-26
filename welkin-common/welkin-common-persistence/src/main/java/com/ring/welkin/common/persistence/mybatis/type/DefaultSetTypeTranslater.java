package com.ring.welkin.common.persistence.mybatis.type;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public interface DefaultSetTypeTranslater<T> extends TypeTranslater<Set<T>>, ValueClass<T> {

	@SuppressWarnings("unchecked")
	@Override
	default Set<T> translate2Bean(String result) {
		if (StringUtils.isNotEmpty(result)) {
			try {
				return fromJson(result, HashSet.class, getValueClass());
			} catch (Exception e) {
                if (result.startsWith("[") && result.endsWith("]")) {
                    result = result.substring(0, result.length()).replaceFirst("\\[", "");
                }
                String[] values = StringUtils.split(result, DEFAULT_SEPARATOR);
                if (values != null && values.length > 0) {
                    return Arrays.stream(values).map(t -> {
                        return fromJson(t, getValueClass());
                    }).collect(Collectors.toSet());
                }
            }
		}
		return null;
	}
}
