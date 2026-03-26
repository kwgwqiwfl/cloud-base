package com.ring.welkin.common.persistence.entity.base;

import com.ring.welkin.common.persistence.entity.preprocess.PreEntityService;
import com.ring.welkin.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.ring.welkin.common.persistence.service.BaseIdableService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;

import java.io.Serializable;
import java.util.List;

/**
 * 继承{@link com.ring.welkin.common.persistence.entity.base.OwnerEntity}的实体类公共查询接口定义
 *
 * @param <T> 继承Maintable的类型
 * @author cloud
 * @date 2019年10月9日 上午11:06:23
 */
public interface BaseOwnerEntityService<ID extends Serializable, T extends OwnerEntity<ID>>
        extends BaseIdableService<ID, T>, PreEntityService<T> {

    @Override
    default BaseExampleMapper<T> getBaseExampleMapper() {
        return getMyIdableMapper();
    }

    @Override
    default void preInsert(T t) {
        t.preInsert();
    }

    @Override
    default void preUpdate(T t) {
        t.preUpdate();
    }

	default List<T> findAllByTenantId(String tenantId) {
        return selectList(ExampleQuery.builder(getEntityClass())//
                .andEqualTo(IConstants.PROPERTY_TENANTID, tenantId)//
        );
    }

	default List<T> findAllByOwnerId(String tenantId, String owner) {
        return selectList(ExampleQuery.builder(getEntityClass())//
                .andEqualTo(IConstants.PROPERTY_TENANTID, tenantId)//
                .andEqualTo(IConstants.PROPERTY_OWNER, owner)//
        );
    }

	default int count(String tenantId) {
        return selectCount(ExampleQuery.builder(getEntityClass())//
                .andEqualTo(IConstants.PROPERTY_TENANTID, tenantId)//
        );
    }

	default int deleteByOwner(String owner) {
        return delete(ExampleQuery.builder(getEntityClass())//
                .andEqualTo(IConstants.PROPERTY_OWNER, owner)//
        );
    }
}
