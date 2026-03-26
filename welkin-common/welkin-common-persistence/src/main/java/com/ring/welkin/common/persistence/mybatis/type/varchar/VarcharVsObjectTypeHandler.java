package com.ring.welkin.common.persistence.mybatis.type.varchar;

import com.ring.welkin.common.persistence.mybatis.type.DefaultBeanTypeTranslater;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Clob VS Object TypeHandler
 *
 * @author cloud
 * @date 2019-05-29 09:46
 */
public abstract class VarcharVsObjectTypeHandler<T> extends AbstractVarcharTypeHandler<T>
    implements DefaultBeanTypeTranslater<T> {
    protected Class<T> valueClass;

    @SuppressWarnings("unchecked")
    public VarcharVsObjectTypeHandler() {
        Type genType = getClass().getGenericSuperclass();
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        valueClass = (Class<T>) params[0];
    }

    @Override
    public Class<T> getValueClass() {
        return valueClass;
    }

}
