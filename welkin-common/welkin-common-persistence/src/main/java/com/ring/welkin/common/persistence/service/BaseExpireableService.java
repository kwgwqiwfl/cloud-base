package com.ring.welkin.common.persistence.service;

import com.ring.welkin.common.persistence.entity.base.IConstants;
import com.ring.welkin.common.persistence.entity.gene.Expireable;
import com.ring.welkin.common.persistence.service.criteria.example.ExampleQueryService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;

import javax.transaction.Transactional;
import java.util.List;

/**
 * 存在主键属性的实体操作接口定义
 *
 * @author cloud
 * @date 2019-05-29 15:22
 */
@Transactional
public interface BaseExpireableService<T extends Expireable> extends ExampleQueryService<T> {

    /**
     * 查询过期时间的数据，按照指定批次大小
     *
     * @param expireTime 过期时间戳
     * @param batchSize  查询批次
     * @return 数据集合
     */
    default List<T> findExpired(Long expireTime, int batchSize) {
        return selectList(ExampleQuery.builder()//
                .andLessThan(IConstants.PROPERTY_EXPIREDTIME, expireTime)//
                .andGreaterThanIfNotNull(IConstants.PROPERTY_EXPIREDTIME, 0).offset(0, batchSize));
    }

    /**
     * 查询过期时间的数据条数
     *
     * @param expireTime 过期时间戳
     * @return 过期数据条数
     */
    default int countExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder()//
                .andLessThan(IConstants.PROPERTY_EXPIREDTIME, expireTime)//
                .andGreaterThanIfNotNull(IConstants.PROPERTY_EXPIREDTIME, 0));
    }

    /**
     * 删除过期时间的数据
     *
     * @param expireTime 过期时间
     * @return 删除条数
     */
    default int deleteExpired(Long expireTime) {
        return selectCount(ExampleQuery.builder()//
                .andLessThan(IConstants.PROPERTY_EXPIREDTIME, expireTime)//
                .andGreaterThanIfNotNull(IConstants.PROPERTY_EXPIREDTIME, 0));
    }
}
