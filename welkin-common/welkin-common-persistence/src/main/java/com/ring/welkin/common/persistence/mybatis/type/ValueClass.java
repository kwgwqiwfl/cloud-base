package com.ring.welkin.common.persistence.mybatis.type;

public interface ValueClass<T> {

    /**
     * 获取实体类型
     *
     * @return 实体类型
     */
    Class<T> getValueClass();

}
