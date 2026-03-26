package com.ring.welkin.common.persistence.mybatis.type;

import org.apache.commons.lang3.StringUtils;

public interface DefaultBeanTypeTranslater<T> extends TypeTranslater<T>, ValueClass<T> {

    @Override
    default T translate2Bean(String result) {
        if (StringUtils.isNotEmpty(result)) {
            return fromJson(result, getValueClass());
        }
        return null;
    }
}
