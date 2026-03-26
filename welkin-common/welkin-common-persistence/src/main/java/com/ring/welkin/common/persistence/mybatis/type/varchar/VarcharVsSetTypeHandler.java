package com.ring.welkin.common.persistence.mybatis.type.varchar;

import com.ring.welkin.common.persistence.mybatis.type.DefaultSetTypeTranslater;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

public class VarcharVsSetTypeHandler<T> extends AbstractVarcharTypeHandler<Set<T>>
	implements DefaultSetTypeTranslater<T> {

	protected Class<T> valueClass;

	@SuppressWarnings("unchecked")
	public VarcharVsSetTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		valueClass = (Class<T>) params[0];
	}

	@Override
	public Class<T> getValueClass() {
		return valueClass;
	}

}
