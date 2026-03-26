package com.ring.welkin.common.persistence.entity.preprocess;

import com.ring.welkin.common.utils.ICollections;

import java.util.Collection;

/**
 * 定义一个删除记录的前置处理的接口类
 *
 * @author cloud
 * @date 2019年9月27日 下午3:36:02
 */
public interface PreDeleteService<T> {

    /**
     * 删除记录前置处理逻辑
     *
     * @param t 删除记录
     */
    default void preDelete(T t) {
    }

    /**
     * 删除记录前置处理逻辑
     *
     * @param ts 删除记录列表
     */
    default void preDelete(Collection<T> ts) {
        if (ICollections.hasElements(ts)) {
            for (T t : ts) {
                preDelete(t);
            }
        }
    }
}
