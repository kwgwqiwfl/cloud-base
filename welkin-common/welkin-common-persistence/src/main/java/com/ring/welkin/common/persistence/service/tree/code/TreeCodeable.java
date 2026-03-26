package com.ring.welkin.common.persistence.service.tree.code;

import com.ring.welkin.common.persistence.service.tree.Treeable;
import com.ring.welkin.common.utils.EntityUtils;

import java.beans.Transient;
import java.io.Serializable;

/**
 * 根据编码(code)维护的树结构数据类型接口
 *
 * @param <C> 编码类型，即关系维护的字段类型
 * @param <T> 实体类型
 * @author cloud
 * @date 2019年9月24日 下午5:20:24
 */
public interface TreeCodeable<C extends Serializable, T extends TreeCodeable<C, T>> extends Treeable<T> {

    /**
     * 获取节点编码
     *
     * @return 节点编码
     */
    C getSelfCode();

    /**
     * 设置节点编码
     *
     * @param selfCode 节点编码
     */
    void setSelfCode(C selfCode);

    /**
     * 获取父节点编码
     *
     * @return 父节点编码
     */
    C getParentCode();

    /**
     * 设置父节点编码
     *
     * @param parentCode 父节点编码
     */
    void setParentCode(C parentCode);

    /**
     * 该节点是否是根节点，节点的父级编码为默认值或为空的判为根节点，如果Number类型的0，String类型的空字符串，其他类型的null值
     *
     * @return 是否是根节点
     */
    @Transient
    default boolean isRoot() {
        return EntityUtils.isDefaultValue(getParentCode());
    }

}
