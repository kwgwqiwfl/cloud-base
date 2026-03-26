package com.ring.welkin.common.persistence.mybatis.type.clob;

import com.ring.welkin.common.persistence.mybatis.type.DefaultListTypeTranslater;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Clob VS Object TypeHandler
 *
 * @author cloud
 * @date 2019-05-29 09:46
 */
public abstract class ClobVsListTypeHandler<T> extends AbstractClobTypeHandler<List<T>>
	implements DefaultListTypeTranslater<T> {
	protected Class<T> valueClass;

	@SuppressWarnings("unchecked")
	public ClobVsListTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		valueClass = (Class<T>) params[0];
	}

	@Override
	public Class<T> getValueClass() {
		return valueClass;
	}

}
