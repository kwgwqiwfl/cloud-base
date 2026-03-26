package com.ring.welkin.common.persistence.tree;

import com.ring.welkin.common.persistence.entity.base.IConstants;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdable;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdableExampleQueryService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;

import java.io.Serializable;
import java.util.List;

public interface TreeableQueryService<ID extends Serializable, T extends TreeIdable<ID, T>>
        extends TreeIdableExampleQueryService<ID, T> {

    /**
     * 根据租户ID查询是否存在数据
     *
     * @param tenantId 租户ID
     * @return 是否存在数据
     */
    default boolean isExistsByTenantId(String tenantId) {
        return exists(ExampleQuery.builder().andEqualTo(IConstants.PROPERTY_TENANTID, tenantId));
    }

    /**
     * 初始化数据，如果是新系统或者新租户查询，则根据条件初始化一些原始数据
     *
     * @param tenantId 租户ID
     * @param
     */
    default void initialize(String tenantId) {
        // TODO 默认无需实现，只有需要有初始化数据的时候需要实现
    }

    /**
     * 查询目录树
     *
     * @param tenantId 租户ID
     * @param query    查询条件
     * @return 查询到的根目录，包含子节点
     */
    default List<T> selectAllTree(String tenantId, ExampleQuery query) {
        // 如果是新系统或者新租户，这里根据需要创建初始化数据然后在查询
        synchronized (this) {
            if (!isExistsByTenantId(tenantId)) {
                initialize(tenantId);
            }
        }
        // 过滤租户数据
        query.andEqualTo(IConstants.PROPERTY_TENANTID, tenantId);
        return selectTrees(query);
    }
}
