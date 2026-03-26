package com.ring.welkin.common.persistence.entity.gene;

import java.io.Serializable;

/**
 * Name定义接口，一般声明该实体类有name属性才需要继承该接口，便于在一些功能逻辑中提取对象名称使用
 *
 * @author cloud
 * @date 2019-05-29 15:59
 */
public interface Nameable extends Serializable {
    public static final String NAME = "name";

    /**
     * 获取名称信息
     *
     * @return 名称信息
     */
    String getName();

    /**
     * 设置名称信息
     *
     * @param name 名称信息
     */
    void setName(String name);

}
