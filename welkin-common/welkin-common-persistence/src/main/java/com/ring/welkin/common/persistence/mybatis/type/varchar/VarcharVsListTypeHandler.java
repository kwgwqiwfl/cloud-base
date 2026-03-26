package com.ring.welkin.common.persistence.mybatis.type.varchar;

import com.ring.welkin.common.persistence.mybatis.type.DefaultListTypeTranslater;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class VarcharVsListTypeHandler<T> extends AbstractVarcharTypeHandler<List<T>>
	implements DefaultListTypeTranslater<T> {

	protected Class<T> valueClass;

	@SuppressWarnings("unchecked")
	public VarcharVsListTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		valueClass = (Class<T>) params[0];
	}

	@Override
	public Class<T> getValueClass() {
		return valueClass;
	}

}
