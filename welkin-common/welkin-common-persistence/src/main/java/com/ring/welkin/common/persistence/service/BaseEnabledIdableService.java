package com.ring.welkin.common.persistence.service;

import com.ring.welkin.common.core.exception.ServiceException;
import com.ring.welkin.common.core.result.ErrType;
import com.ring.welkin.common.persistence.entity.gene.Enabled;
import com.ring.welkin.common.persistence.entity.gene.Enabled.EnabledEntity;
import com.ring.welkin.common.persistence.entity.gene.Enabled.EnabledIdEntity;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.service.entity.EntityClassService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.Collection;

/**
 * 具有enabled属性的对象更新状态接口
 *
 * @param <ID> 主键类型
 * @param <E>  enabled 字段类型
 * @param <T>  实体类型
 * @author cloud
 * @date 2020年11月12日 上午9:57:39
 */
public interface BaseEnabledIdableService<ID extends Serializable, E extends Serializable, T extends Enabled<E> & Idable<ID>>
        extends EntityClassService<T>, BaseIdableService<ID, T> {

    default int updateEnabled(ID id, E enabled) {
        if (id == null) {
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, "Entity id could not be null.");
        }
        return updateByPrimaryKeySelective(
                JsonUtils.fromObject(new EnabledIdEntity<ID, E>(id, enabled), getEntityClass()));
    }

    default int updateEnabled(Collection<ID> ids, E enabled) {
        if (ICollections.hasNoElements(ids)) {
            throw new ServiceException(ErrType.INTERNAL_SERVER_ERROR, "Entity ids could not be null or empty.");
        }
        if (ICollections.hasElements(ids)) {
            return updateByQuerySelective(JsonUtils.fromObject(new EnabledEntity<E>(enabled), getEntityClass()),
                    ExampleQuery.builder(getEntityClass()).andIn(Idable.ID, ids.toArray()));
        }
        return 0;
    }
}
