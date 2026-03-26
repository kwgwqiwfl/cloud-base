package com.ring.welkin.common.persistence.entity.base;

import com.google.common.collect.Lists;
import com.ring.welkin.common.persistence.entity.preprocess.PreEntityService;
import com.ring.welkin.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.ring.welkin.common.persistence.service.BaseEnabledIdableService;
import com.ring.welkin.common.persistence.service.BaseIdableService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.queryapi.query.field.Field;
import com.ring.welkin.common.queryapi.query.field.FieldGroup;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.AndOr;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.Operator;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface BaseCommonEntityService<ID extends Serializable, T extends CommonEntity<ID>>
        extends BaseOwnerEntityService<ID, T>, BaseIdableService<ID, T>, BaseEnabledIdableService<ID, Integer, T>,
        PreEntityService<T> {

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

    /**
     * 根据租户ID和名称查询所有复合条件的数据列表
     *
     * @param tenantId 租户ID
     * @param name     名称
     * @return 复合条件数据列表
     */
    default List<T> findAllByTenantIdAndName(String tenantId, String name) {
        // @formatter:off
        return selectList(ExampleQuery.builder(getEntityClass())
                .andEqualTo(IConstants.PROPERTY_TENANTID, tenantId)
                .andEqualTo(IConstants.PROPERTY_NAME, name));
        // @formatter:on
    }

    /**
     * 根据租户ID和名称查询唯一数据
     *
     * @param tenantId 租户ID
     * @param name     数据名称
     * @return 唯一记录，不存在返回null
     */
    default T findOneByTenantIdAndName(String tenantId, String name) {
        // @formatter:off
        return selectOne(ExampleQuery.builder(getEntityClass())
                .andEqualTo(IConstants.PROPERTY_TENANTID, tenantId)
                .andEqualTo(IConstants.PROPERTY_NAME, name)
        );
        // @formatter:on
    }

    /**
     * 根据租户ID和名称查询唯一数据
     *
     * @param tenantId 租户ID
     * @param name     数据名称
     * @return 唯一记录，不存在返回null
     * @deprecated 推荐使用{link
     * BaseCommonEntityService.findOneByTenantIdAndName(String, String)}
     */
    default T findOneByName(String tenantId, String name) {
        return findOneByTenantIdAndName(tenantId, name);
    }

    /**
     * 查询对应租户下是否存在数据
     *
     * @param tenantId 租户ID
     * @return 是否存在据
     */
    default boolean existsByTenantId(String tenantId) {
        return exists(ExampleQuery.builder(getEntityClass())//
                .andEqualTo(IConstants.PROPERTY_TENANTID, tenantId));
    }

    /**
     * 查询对应租户下是否存在复合条件的数据
     *
     * @param tenantId 租户ID
     * @param fields   条件列表
     * @return 是否存在复合条件的数据
     */
    default boolean existsByTenantId(String tenantId, Field... fields) {
        FieldGroup group = FieldGroup.builder().andEqualTo(IConstants.PROPERTY_TENANTID, tenantId);
        if (fields != null && fields.length > 0) {
            group.fields(Lists.newArrayList(fields));
        }
        return exists(ExampleQuery.builder(getEntityClass())//
                .fieldGroup(group));
    }

    /**
     * 查询对应租户下是否存在复合条件的数据，条件是EQUAL
     *
     * @param tenantId      租户名称
     * @param propertyName  参数名称
     * @param propertyValue 参数值
     * @return 是否存在复合条件的数据
     */
    default boolean existsByTenantId(String tenantId, String propertyName, Object propertyValue) {
        return existsByTenantId(tenantId, Field.apply(AndOr.AND, propertyName, Operator.EQUAL, propertyValue));
    }

    /**
     * 查询对应租户下是否存在复合条件的数据
     *
     * @param tenantId   租户ID
     * @param conditions 条件列表，key为条件名，value为条件值，条件之间是and关系
     * @return 是否存在复合条件的数据
     */
    default boolean existsByTenantId(String tenantId, Map<String, Object> conditions) {
        final FieldGroup group = FieldGroup.builder().andEqualTo(IConstants.PROPERTY_TENANTID, tenantId);
        if (!conditions.isEmpty()) {
            conditions.forEach((k, v) -> {
                group.andEqualTo(k, v);
            });
        }
        return exists(ExampleQuery.builder(getEntityClass()).fieldGroup(group));
    }

    /**
     * 根据tenantId、name查询是否存在数据
     *
     * @param tenantId 租户ID
     * @param name     名称
     * @return 是否存在复合条件的数据
     */
    default boolean existsByTenantIdAndName(String tenantId, String name) {
        return existsByTenantId(tenantId, IConstants.PROPERTY_NAME, name);
    }

    /**
     * 查询数据名称列表
     *
     * @param query 查询条件
     * @return 查询的名称列表
     */
    default List<String> findNameList(ExampleQuery query) {
        return selectStringList(IConstants.PROPERTY_NAME, query);
    }
}
