package com.ring.welkin.common.persistence.mybatis.type;

import com.google.common.collect.Lists;
import com.ring.welkin.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public interface DefaultListTypeTranslater<T> extends TypeTranslater<List<T>>, ValueClass<T> {

	@SuppressWarnings("unchecked")
	@Override
	default List<T> translate2Bean(String result) {
		if (StringUtils.isNotEmpty(result)) {
			try {
				return fromJson(result, ArrayList.class, getValueClass());
			} catch (Exception e) {
				if (result.startsWith("[") && result.endsWith("]")) {
					result = result.substring(0, result.length()).replaceFirst("\\[", "");
				}
				String[] values = StringUtils.split(result, DEFAULT_SEPARATOR);
				if (values != null && values.length > 0) {
					return Arrays.stream(values).map(t -> {
						return fromJson(t, getValueClass());
					}).collect(Collectors.toList());
				}
			}
		}
		return null;
	}

	public static void main(String[] args) {
		// String result = "34334cxdf";
		// System.out.println(JsonUtils.fromJson(result, String.class));
		// System.out.println(JsonUtils.fromJson(result, Integer.class));
		/*
		 * if (StringUtils.isNotEmpty(result)) { try { ArrayList fromJson = JsonUtils.fromJson(result, ArrayList.class,
		 * String.class); System.out.println(fromJson); } catch (Exception e) { String[] values =
		 * StringUtils.split(result, DEFAULT_SEPARATOR); if (values != null && values.length > 0) { List<String> collect
		 * = Arrays.stream(values).map(t -> JsonUtils.fromJson(t, String.class)) .collect(Collectors.toList());
		 * System.out.println("collect" + collect); } } }
		 */

        String json = JsonUtils.toJson(Lists.newArrayList(1l, 2l, 3L, 45L));
        System.out.println(json);

        if (json.startsWith("[") && json.endsWith("]")) {
            System.out.println(json.substring(0, json.length() - 1).replaceFirst("\\[", ""));
        }
        List<?> fromObject = JsonUtils.fromJson(json, List.class);
        System.out.println(fromObject);

        String s = "监控规则关联对象管理";
        // String s = "true";
        // System.out.println(JsonUtils.fromJson(s, Long.class));
        System.out.println(JsonUtils.fromJson(s, String.class));
    }

}
