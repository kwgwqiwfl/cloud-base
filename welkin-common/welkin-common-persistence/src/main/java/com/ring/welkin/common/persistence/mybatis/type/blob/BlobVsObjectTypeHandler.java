package com.ring.welkin.common.persistence.mybatis.type.blob;

import com.ring.welkin.common.persistence.mybatis.type.DefaultBeanTypeTranslater;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BlobVsObjectTypeHandler<T> extends AbstractBlobTypeHandler<T>
    implements DefaultBeanTypeTranslater<T> {

    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public BlobVsObjectTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        valueClass = (Class<T>) params[0];
    }

    @Override
    public Class<T> getValueClass() {
        return valueClass;
    }
}
