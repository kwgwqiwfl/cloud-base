package com.ring.welkin.common.persistence.mybatis.type;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ring.welkin.common.utils.JsonUtils;

import java.util.Collection;
import java.util.Map;

public interface JsonFormatTypeHandler {

    default <O> String toJson(O t) {
        try {
            return JsonUtils.toJson(t);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <M extends Map<K, V>, K, V> M fromJson(String json, Class<M> mCkass, Class<K> kClass, Class<V> vClass) {
        try {
            return JsonUtils.fromJson(json, mCkass, kClass, vClass);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <C extends Collection<O>, O> C fromJson(String json, Class<C> cClass, Class<O> oClass) {
        try {
            return JsonUtils.fromJson(json, cClass, oClass);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    @SuppressWarnings("unchecked")
    default <O> O fromJson(String json, Class<O> tClass) {
        try {
            //fix error "Unrecognized token '监控规则关联对象管理': was expecting (JSON String, Number, Array, Object or token 'null', 'true' or 'false')" for String,class
            if (String.class.equals(tClass)) {
                return (O) json;
            } else {
                return JsonUtils.fromJson(json, tClass);
            }
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }

    default <O> O fromJson(String json, TypeReference<O> typeReference) {
        try {
            return JsonUtils.fromJson(json, typeReference);
        } catch (Exception e) {
            throw new TypeHandleException(e);
        }
    }
}
