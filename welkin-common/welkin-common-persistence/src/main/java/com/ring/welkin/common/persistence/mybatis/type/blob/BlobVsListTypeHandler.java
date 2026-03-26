package com.ring.welkin.common.persistence.mybatis.type.blob;

import com.ring.welkin.common.persistence.mybatis.type.DefaultListTypeTranslater;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Blob VS String List TypeHandler
 *
 * @author cloud
 * @date 2019-05-29 09:43
 */
public class BlobVsListTypeHandler<T> extends AbstractBlobTypeHandler<List<T>> implements DefaultListTypeTranslater<T> {

	protected Class<T> valueClass;

	@SuppressWarnings("unchecked")
	public BlobVsListTypeHandler() {
		Type genType = getClass().getGenericSuperclass();
		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
		valueClass = (Class<T>) params[0];
	}

	@Override
	public Class<T> getValueClass() {
		return valueClass;
	}
}
