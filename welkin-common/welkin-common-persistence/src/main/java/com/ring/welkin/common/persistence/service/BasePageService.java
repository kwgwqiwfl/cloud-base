package com.ring.welkin.common.persistence.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.google.common.collect.Lists;
import com.ring.welkin.common.core.page.IPageable;
import org.apache.commons.lang3.StringUtils;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface BasePageService<T> {
    /**
     * 分页排序
     *
     * @param pageable 分页信息
     */
    default void startPage(IPageable pageable) {
        if (pageable != null) {
            startPage(pageable.getPageNum(), pageable.getPageSize(), pageable.getOrderByClause());
        }
    }

    /**
     * 分页排序
     *
     * @param pageNum       页码
     * @param pageSize      页长
     * @param orderByClause 排序条件
     */
    default void startPage(int pageNum, int pageSize, String orderByClause) {
        PageHelper.startPage(pageNum, pageSize, true);
        PageHelper.orderBy(orderByClause);
    }

    /**
     * 排序
     *
     * @param orderByClause 排序条件
     */
    default void startOrderBy(String orderByClause) {
        if (StringUtils.isNotEmpty(orderByClause)) {
            PageHelper.orderBy(orderByClause);
        }
    }

    /**
     * 将分页数据转化为List对象，mybatis启用pagehelper分页查询之后返回的是{@code com.github.pagehelper.Page}对象，它继承自ArrayList
     *
     * @param <E>  数据类型
     * @param list 数据集合，可能是Page对象
     * @return 集合数据
     */
    default <E> List<E> converPageToList(List<E> list) {
        if (list != null && list instanceof Page) {
            return Lists.newArrayList(list);
        }
        return list;
    }
}
