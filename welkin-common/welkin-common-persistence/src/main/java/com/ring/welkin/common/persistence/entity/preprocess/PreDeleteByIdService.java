package com.ring.welkin.common.persistence.entity.preprocess;

import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.utils.ICollections;

import java.io.Serializable;
import java.util.Collection;

/**
 * 定义一个删除记录的前置处理的接口类
 *
 * @author cloud
 * @date 2019年9月27日 下午3:36:02
 */
public interface PreDeleteByIdService<ID extends Serializable, T extends Idable<ID>> extends PreDeleteService<T> {

    /**
     * 根据ID删除记录前置处理
     *
     * @param id 删除记录ID
     */
    default void preDeleteById(ID id) {
    }

    /**
     * 根据ID批量删除记录前置处理
     *
     * @param ids 删除记录ID集合
     */
    default void preDeleteByIds(Collection<ID> ids) {
        if (ICollections.hasElements(ids)) {
            for (ID id : ids) {
                preDeleteById(id);
            }
        }
    }
}
