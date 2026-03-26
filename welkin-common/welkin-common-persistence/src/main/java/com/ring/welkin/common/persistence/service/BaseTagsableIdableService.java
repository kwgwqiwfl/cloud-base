package com.ring.welkin.common.persistence.service;

import com.google.common.collect.Lists;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.entity.gene.Tagsable;
import com.ring.welkin.common.persistence.entity.gene.Tagsable.TagsEntity;
import com.ring.welkin.common.persistence.entity.gene.Tagsable.TagsIdEntity;
import com.ring.welkin.common.persistence.service.criteria.example.ExampleHelper;
import com.ring.welkin.common.persistence.service.entity.EntityClassService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public interface BaseTagsableIdableService<ID extends Serializable, T extends Idable<ID> & Tagsable>
        extends EntityClassService<T>, BaseIdableService<ID, T> {

    /**
     * 根据主键更新标签
     *
     * @param id   主键值
     * @param tags 标签列表
     * @return 更新条数
     */
    default int updateTagsById(ID id, Set<String> tags) {
        return updateByPrimaryKey(JsonUtils.fromObject(new TagsIdEntity<ID>(id, tags), getEntityClass()));
    }

    /**
     * 根据主键值批量更新标签
     *
     * @param ids  主键值列表
     * @param tags 标签列表
     * @return 更新条数
     */
    default int updateTagsByIds(Collection<ID> ids, Set<String> tags) {
        if (ICollections.hasElements(ids)) {
            return getMyIdableMapper().updateByExampleSelective(
                    JsonUtils.fromObject(new TagsEntity(tags), getEntityClass()), ExampleHelper.createExample(
                            ExampleQuery.builder(getEntityClass()).andIn(Idable.ID, ids.toArray()), getEntityClass()));
        }
        return 0;
    }

    /**
     * 根据ID批量清理标签
     *
     * @param ids 需要清理的ID列表
     * @return 清理条数
     */
    default int clearTagsByIds(Collection<ID> ids) {
        if (ICollections.hasElements(ids)) {
            for (ID id : ids) {
                getMyIdableMapper().updateByPrimaryKeySelectiveForce(
                        JsonUtils.fromObject(new TagsIdEntity<ID>(id, new HashSet<String>()), getEntityClass()),
                        Lists.newArrayList(Tagsable.TAGS));
            }
            return ids.size();
        }
        return 0;
    }

}
