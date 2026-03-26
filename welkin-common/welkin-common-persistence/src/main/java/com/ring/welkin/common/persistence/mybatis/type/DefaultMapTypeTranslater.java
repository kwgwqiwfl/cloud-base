package com.ring.welkin.common.persistence.mybatis.type;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public interface DefaultMapTypeTranslater<K, V> extends TypeTranslater<Map<K, V>>, ValueClass<V> {

	/**
	 * 获取键类型
	 *
	 * @return 键类型
	 */
	Class<K> getKeyClass();

	@SuppressWarnings("unchecked")
	@Override
	default Map<K, V> translate2Bean(String result) {
		if (StringUtils.isNotEmpty(result)) {
			return fromJson(result, HashMap.class, getKeyClass(), getValueClass());
		}
		return null;
	}

}
