package com.ring.welkin.common.persistence.mybatis.type.varchar;

import com.ring.welkin.common.persistence.mybatis.type.DefaultMapTypeTranslater;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Map;

public abstract class VarcharVsMapTypeHandler<K, V> extends AbstractVarcharTypeHandler<Map<K, V>>
	implements DefaultMapTypeTranslater<K, V> {

	protected Class<K> keyClass;
	protected Class<V> valueClass;

	@SuppressWarnings("unchecked")
	public VarcharVsMapTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		keyClass = (Class<K>) params[0];
		Type type = params[1];
		if (type instanceof Class) {
			valueClass = (Class<V>) type;
		} else if (type instanceof ParameterizedType) {
			valueClass = (Class<V>) ((ParameterizedType) type).getRawType();
		}
	}

	@Override
	public Class<K> getKeyClass() {
		return keyClass;
	}

	@Override
	public Class<V> getValueClass() {
		return valueClass;
	}

}
