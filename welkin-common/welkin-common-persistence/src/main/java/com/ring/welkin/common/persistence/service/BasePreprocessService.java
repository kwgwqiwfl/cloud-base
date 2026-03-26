package com.ring.welkin.common.persistence.service;

import com.ring.welkin.common.persistence.entity.preprocess.PreDeleteService;
import com.ring.welkin.common.utils.ICollections;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 实体业务预处理接口类，主要用于实体的数据库插入与更新预处理操作
 *
 * @author cloud
 * @date 2019-05-29 14:57
 */
@Transactional
public interface BasePreprocessService<T> extends PreDeleteService<T> {

    /**
     * 插入操作前置处理逻辑
     *
     * @param t 插入数据
     */
    default void preInsert(T t) {
        // default do nothing
    }

    /**
     * 批量插入操作前置处理逻辑
     *
     * @param list 插入数据列表
     */
    default void preInsert(List<? extends T> list) {
        if (ICollections.hasElements(list)) {
            for (T t : list) {
                preInsert(t);
            }
        }
    }

    /**
     * 在调用save(T t)方法后执行
     *
     * @param saved 保存后的对象
     * @return 保存结果
     */
    default T postSave(T saved) {
        // default do nothing
        return saved;
    }

    /**
     * 更新操作前置处理逻辑
     *
     * @param t 更新数据
     */
    default void preUpdate(T t) {
        // default do nothing
    }

    /**
     * 批量更新操作前置处理逻辑
     *
     * @param list 更新列表
     */
    default void preUpdate(List<? extends T> list) {
        if (ICollections.hasElements(list)) {
            for (T t : list) {
                preUpdate(t);
            }
        }
    }

    /**
     * 在调用update(T t)方法后执行
     *
     * @param updated 保存后的对象
     * @return 保存结果
     */
    default T postUpdate(T updated) {
        // default do nothing
        return updated;
    }
}
