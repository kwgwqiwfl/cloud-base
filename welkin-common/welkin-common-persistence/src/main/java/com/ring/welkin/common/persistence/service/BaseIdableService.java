package com.ring.welkin.common.persistence.service;

import com.ring.welkin.common.core.datasource.DialectContext;
import com.ring.welkin.common.core.exception.ServiceException;
import com.ring.welkin.common.core.result.ErrType;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.entity.preprocess.PreDeleteByIdService;
import com.ring.welkin.common.persistence.mybatis.mapper.MyBaseMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.MyIdableMapper;
import com.ring.welkin.common.utils.ICollections;

import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 存在主键属性的实体操作接口定义
 *
 * @author cloud
 * @date 2019-05-29 15:22
 */
@Transactional
public interface BaseIdableService<ID extends Serializable, T extends Idable<ID>>
        extends BaseService<T>, PreDeleteByIdService<ID, T> {

    MyIdableMapper<T> getMyIdableMapper();

    @Override
    default MyBaseMapper<T> getMyBaseMapper() {
        return getMyIdableMapper();
    }

    /**
     * 保存一条记录，并返回保存的结果
     *
     * @param t 待保存数据
     * @return 保存后的结果
     */
    default T save(T t) {
        if (t == null) {
            throw new ServiceException(ErrType.ENTITY_EMPTY, "保存对象不能为空");
        }
        insertSelective(t);
        return postSave(t);
    }

    /**
     * 更新一条记录，并返回更新的结果
     *
     * @param t 待更新数据
     * @return 更新后的结果
     */
    default T update(T t) {
        if (t == null) {
            throw new ServiceException(ErrType.ENTITY_EMPTY, "修改对象不能为空");
        }
        updateByPrimaryKeySelective(t);
        return postUpdate(t);
    }

    /**
     * 插入或者更新：当ID值为空时插入新的数据，否则更新记录
     *
     * @param t           保存的数据
     * @param checkRecord 是否检查数据库中记录是否存在，需要查数据库
     * @return 结果
     */
    default T saveOrUpdate(T t, boolean checkRecord) {
        if (t.getId() == null || t.getId().toString().trim().equals("")) {
            t.setId(null);
            return save(t);
        } else {
            if (checkRecord && !existsWithPrimaryKey(t.getId())) {
                return save(t);
            }
            return update(t);
        }
    }

    /**
     * 插入或者更新：当ID值为空时插入新的数据，否则更新记录
     *
     * @param t 待插入数据
     * @return 插入结果
     */
    default T saveOrUpdate(T t) {
        return saveOrUpdate(t, true);
    }

    /**
     * 批量插入或者更新：当ID值为空时插入新的数据，否则更新记录
     *
     * @param ts 待插入数据
     * @return 插入结果
     */
    default List<T> saveOrUpdateBatch(List<T> ts) {
        if (ICollections.hasElements(ts)) {
            for (T t : ts) {
                saveOrUpdate(t, true);
            }
        }
        return ts;
    }

    /**
     * 根据ID删除单条记录，如果需要做级联删除需重写该方法
     *
     * @param id 删除的ID
     * @return 删除条数
     */
    default int deleteById(ID id) {
        preDeleteById(id);
        return this.deleteByPrimaryKey(id);
    }

    /**
     * 根据ID批量删除，调用deleteById循环删除，适用于有附加操作的场景
     *
     * @param ids 删除的ID集合
     * @return 删除结果
     */
    default int deleteByIds(List<ID> ids) {
        if (ICollections.hasElements(ids)) {
            for (ID id : ids) {
                deleteById(id);
            }
            return ids.size();
        }
        return 0;
    }

    /**
     * 批量插入记录，插入的数据自带主键值
     *
     * @param recordList 待插入的数据集合，每条数据主键不为空
     * @return 插入记录条数
     */
    default int insertListWithPrimaryKey(List<T> recordList) {
        preInsert(recordList);
        return getMyIdableMapper().insertListWithPrimaryKey(recordList);
    }

    /**
     * 根据主键更新记录，更新所有字段，包含空值
     *
     * @param record 待更新记录
     * @return 更新记录条数
     */
    default int updateByPrimaryKey(T record) {
        preUpdate(record);
        return getMyIdableMapper().updateByPrimaryKey(record);
    }

    /**
     * 根据主键更新记录，只更新不为空的属性值，跳过为空的属性
     *
     * @param record 待更新的记录
     * @return 更新数据条数
     */
    default int updateByPrimaryKeySelective(T record) {
        preUpdate(record);
        return getMyIdableMapper().updateByPrimaryKeySelective(record);
    }

    /**
     * 根据主键批量更新记录，更新所有的字段包含空值
     *
     * @param recordList 待更新记录
     * @return 更新的记录条数
     */
    default int updateListByPrimaryKey(List<T> recordList) {
        preUpdate(recordList);
        return getMyIdableMapper().updateListByPrimaryKey(recordList);
    }

    /**
     * 根据主键批量更新记录，只更新不为空的属性值，跳过为空的属性
     *
     * @param recordList 待更新记录
     * @return 更新的记录条数
     */
    default int updateListByPrimaryKeySelective(List<T> recordList) {
        preUpdate(recordList);
        return getMyIdableMapper().updateListByPrimaryKeySelective(recordList);
    }

    /**
     * 根据主键值删除记录
     *
     * @param key 主键值
     * @return 删除记录条数
     */
    @SuppressWarnings("unchecked")
    default int deleteByPrimaryKey(Object key) {
        if (key == null) {
            throw new ParameterCheckException("根据主键删除数据时参数key不能为空");
        }
        preDeleteById((ID) key);
        return getMyIdableMapper().deleteByPrimaryKey(key);
    }

    /**
     * 根据主键值集合批量删除
     *
     * @param keys 主键值集合
     * @return 删除记录条数
     */
    @SuppressWarnings("unchecked")
    default int deleteByPrimaryKeys(List<?> keys) {
        if (ICollections.hasNoElements(keys)) {
            throw new ParameterCheckException("根据主键批量删除数据时参数keys不能为空");
        }
        for (Object key : keys) {
            preDeleteById((ID) key);
        }
        return getMyIdableMapper().deleteByPrimaryKeys(keys);
    }

    /**
     * 根据主键值数组批量删除
     *
     * @param keys 主键值数组
     * @return 删除记录条数
     */
    @SuppressWarnings("unchecked")
    default int deleteByPrimaryKeys(Object[] keys) {
        if (keys == null || keys.length < 1) {
            throw new ParameterCheckException("根据主键批量删除数据时参数keys不能为空");
        }
        preDeleteByIds(Stream.of(keys).map(t -> (ID) t).collect(Collectors.toList()));
        return deleteByPrimaryKeys(Arrays.asList(keys));
    }

    /**
     * 根据主键值查询，返回符合条件的记录
     *
     * @param key 主键值
     * @return 符合条件的记录
     */
    default T selectByPrimaryKey(Object key) {
        return getMyIdableMapper().selectByPrimaryKey(key);
    }

    /**
     * 根据主键值集合查询，返回符合条件的数据集合
     *
     * @param keys 主键值集合
     * @return 符合条件的记录集合
     */
    default List<T> selectByPrimaryKeys(List<?> keys) {
        return getMyIdableMapper().selectByPrimaryKeys(keys);
    }

    /**
     * 根据主键值数组查询，返回符合条件的数据集合
     *
     * @param keys 主键值数组
     * @return 符合条件的记录集合
     */
    default List<T> selectByPrimaryKeys(Object... keys) {
        return selectByPrimaryKeys(Arrays.asList(keys));
    }

    /**
     * 判断对应主键的记录是否存在
     *
     * @param key 主键值
     * @return 匹配结果，存在返回true，不存在返回false
     */
    default boolean existsWithPrimaryKey(Object key) {
        switch (DialectContext.get()) {
            case elasticsearch:
                T t = getMyIdableMapper().selectByPrimaryKey(key);
                return t != null;
            default:
                return getMyIdableMapper().existsWithPrimaryKey(key);
        }
    }
}
