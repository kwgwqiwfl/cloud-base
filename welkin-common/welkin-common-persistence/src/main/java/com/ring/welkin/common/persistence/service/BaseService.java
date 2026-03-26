package com.ring.welkin.common.persistence.service;

import com.github.pagehelper.Page;
import com.ring.welkin.common.core.page.IPage;
import com.ring.welkin.common.core.page.IPageable;
import com.ring.welkin.common.persistence.mybatis.mapper.MyBaseMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.ring.welkin.common.persistence.service.criteria.example.ExampleQueryService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.utils.ICollections;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 业务层接口，继承通用方法
 *
 * @author cloud
 * @date 2019-05-29 14:57
 */
@Transactional
public interface BaseService<T> extends ExampleQueryService<T>, BasePreprocessService<T> {

    MyBaseMapper<T> getMyBaseMapper();

    @Override
    default BaseExampleMapper<T> getBaseExampleMapper() {
        return getMyBaseMapper();
    }

    /**
     * 插入单个记录，插入所有字段，包含空值字段
     *
     * @param record 记录数据，可包含空值
     * @return 插入记录条数
     */
    default int insert(T record) {
        if (record != null) {
            preInsert(record);
            return getMyBaseMapper().insert(record);
        }
        return 0;
    }

    /**
     * 插入单个记录，只插入非空字段，不插入空值字段
     *
     * @param record 记录数据，可包含空值
     * @return 插入记录条数
     */
    default int insertSelective(T record) {
        if (record != null) {
            preInsert(record);
            return getMyBaseMapper().insertSelective(record);
        }
        return 0;
    }

    /**
     * 批量插入记录，插入所有字段，包含空值字段
     *
     * @param recordList 待插入数据集合
     * @return 插入记录条数
     */
    default int insertList(List<? extends T> recordList) {
        if (ICollections.hasElements(recordList)) {
            preInsert(recordList);
            return getMyBaseMapper().insertList(recordList);
        }
        return 0;
    }

    /**
     * 批量插入，插入非空字段，不插入空值字段
     *
     * @param recordList 待插入数据集合
     * @return 插入记录条数
     */
    default int insertListSelective(List<T> recordList) {
        int i = 0;
        if (ICollections.hasElements(recordList)) {
            for (T t : recordList) {
                i += insertSelective(t);
            }
        }
        return i;
    }

    /**
     * 根据新旧数据不同更新数据，即只更新值不相同的字段
     *
     * @param old   旧数据
     * @param newer 新数据
     * @return 更新记录条数
     */
    default int updateByDiffer(T old, T newer) {
        preUpdate(newer);
        int i = getMyBaseMapper().updateByDiffer(old, newer);
        return i;
    }

    /**
     * 根据主键更新非空属性的字段，不更新属性为空的字段
     *
     * @param record                待更新的对象
     * @param forceUpdateProperties 强制更新的属性列表，这些属性不论是否为空强制更新
     * @return 更新记录条数
     */
    default int updateByPrimaryKeySelectiveForce(T record, List<String> forceUpdateProperties) {
        preUpdate(record);
        int i = getMyBaseMapper().updateByPrimaryKeySelectiveForce(record, forceUpdateProperties);
        return i;
    }

    /**
     * 根据对象条件删除数据，对象中非空的属性转化为过滤条件
     *
     * @param record 条件对象，非空属性转化为过滤条件
     * @return 删除记录条数
     */
    default int delete(T record) {
        if (record == null) {
            throw new ParameterCheckException("根据条件删除数据时参数record不能为空");
        }
        return getMyBaseMapper().delete(record);
    }

    /**
     * 删除所有数据，无条件过滤（慎用），当配置中mapper.safeDelete=true时，该操作无效
     *
     * @return 删除数据条数
     * @deprecated Dangerous operation! Use with caution!
     */
    @Deprecated
    default int deleteAll() {
        return getMyBaseMapper().delete(null);
    }

    /**
     * @param recordList
     * @return
     */
    default int deleteList(List<T> recordList) {
        if (ICollections.hasNoElements(recordList)) {
            throw new ParameterCheckException("根据条件批量删除数据时参数recordList不能为空");
        }
        preDelete(recordList);
        return getMyBaseMapper().deleteList(recordList);
    }

    /**
     * 根据对象条件查询唯一的记录，对象中非空的属性转化为过滤条件
     *
     * @param record 查询条件，非空的属性转化为过滤条件
     * @return 唯一符合条件的记录
     */
    default T selectOne(T record) {
        return getMyBaseMapper().selectOne(record);
    }

    /**
     * 根据对象条件查询数据集合，对象中非空的属性转化为过滤条件
     *
     * @param record 查询条件，非空的属性转化为过滤条件
     * @return 符合条件的记录集合
     */
    default List<T> select(T record) {
        return getMyBaseMapper().select(record);
    }

    /**
     * 根据对象条件查询数据集合并排序，对象中非空的属性转化为过滤条件
     *
     * @param record        查询条件，非空的属性转化为过滤条件
     * @param orderByClause 排序字段，如：id asc, name desc
     * @return 符合条件的记录集合
     */
    default List<T> select(T record, String orderByClause) {
        startOrderBy(orderByClause);
        return select(record);
    }

    /**
     * 查询所有记录，全表查询没有过滤条件
     *
     * @return 所有的数据记录
     */
    default List<T> selectAll() {
        return getMyBaseMapper().selectAll();
    }

    /**
     * 根据条件对象条件分页查询，对象中非空的属性转化为过滤条件，返回对应分页的数据集合
     *
     * @param record   查询条件，非空的属性转化为过滤条件
     * @param pageable 分页信息
     * @return 符合条件的记录集合
     */
    default List<T> selectByPage(T record, IPageable pageable) {
        IPage<T> page = selectPage(record, pageable);
        if (page != null) {
            return page.getList();
        }
        return null;
    }

    /**
     * 根据条件对象条件分页查询，对象中非空的属性转化为过滤条件，返回分页数据
     *
     * @param record   查询条件，非空的属性转化为过滤条件
     * @param pageable 分页信息
     * @return 查询的分页包装数据
     */
    default IPage<T> selectPage(T record, IPageable pageable) {
        IPage<T> page = null;
        if (pageable == null || !pageable.isPageable()) {// 查询所有
            int totalCount = selectCount(record);
            page = IPage.<T>of(1, totalCount, totalCount);
            if (totalCount <= 0)
                return page;
            page.setList(select(record));
        } else {// 分页查询
            startPage(pageable);
            Page<T> selectPage = (Page<T>) select(record);
            if (selectPage != null) {
                page = IPage.<T>of(selectPage.getPageNum(), selectPage.getPageSize(), selectPage.getTotal(),
                        selectPage.getResult());
            }
        }
        return page;
    }

    /**
     * 根据对象条件查询记录总数，对象中非空的属性转化为过滤条件
     *
     * @param record 查询条件，非空的属性转化为过滤条件
     * @return 符合条件的记录数
     */
    default int selectCount(T record) {
        return getMyBaseMapper().selectCount(record);
    }

    /**
     * 查询所有数据总数，无过滤条件
     *
     * @return 数据总数
     */
    default int selectCount() {
        return selectCount(ExampleQuery.builder());
    }

    /**
     * 判断是否存在符合对象条件的记录，对象中非空的属性转化为过滤条件
     *
     * @param record 过滤条件，非空的属性转化为过滤条件
     * @return 符合条件的结果集
     */
    default List<Integer> existsWithRecord(T record) {
        return getMyBaseMapper().existsWithRecord(record);
    }

    /**
     * 判断是否存在符合对象条件的记录，对象中非空的属性转化为过滤条件
     *
     * @param record 过滤条件，非空的属性转化为过滤条件
     * @return 匹配结果，存在返回true，不存在返回false
     */
    default boolean exists(T record) {
        startPage(1, 1, null);
        return ICollections.hasElements(existsWithRecord(record));
    }
}
