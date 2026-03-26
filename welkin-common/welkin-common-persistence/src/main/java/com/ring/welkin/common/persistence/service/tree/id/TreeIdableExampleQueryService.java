package com.ring.welkin.common.persistence.service.tree.id;

import com.google.common.collect.ImmutableMap;
import com.ring.welkin.common.persistence.entity.base.IConstants;
import com.ring.welkin.common.persistence.service.BaseIdableService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.*;

/**
 * 树结构数据查询公共接口，提取一些比较通用的方法，如：平铺的数据集转化成只有根节点的数据集
 *
 * @param <ID> 主键类型
 * @param <T>  实体类型
 * @author cloud
 * @date 2019年9月24日 下午5:38:52
 */
public interface TreeIdableExampleQueryService<ID extends Serializable, T extends TreeIdable<ID, T>>
        extends TreeIdableService<ID, T>, BaseIdableService<ID, T> {

    default T selectTree(ID rootId) {
        T t = selectByPrimaryKey(rootId);
        if (t != null) {
            fetchAllChildren(t);
        }
        return t;
    }

    /**
     * 加载指定条件的tree数据
     *
     * @param query 查询条件
     * @return 复合条件的列表
     */
    default List<T> selectTrees(ExampleQuery query) {
        List<T> allList = selectList(query);
        if (ICollections.hasElements(allList)) {
            return fetchTree(allList);
        }
        return null;
    }

    /**
     * 根据根节点ID查询子孙节点树，递归查询
     *
     * @param rootId 根节点ID
     * @return tree
     */
    default List<T> selectChilrenTree(ID rootId) {
        return selectChilrenTree(rootId, new HashMap<String, Object>());
    }

    /**
     * 根据根节点ID和其他条件查询子孙节点树,递归查询
     *
     * @param rootId    根节点ID
     * @param extParams 其它查询条件
     * @return tree
     */
    default List<T> selectChilrenTree(ID rootId, Map<String, Object> extParams) {
        List<T> roots = selectByParentId(rootId, extParams);
        for (T t : roots) {
            fetchAllChildren(t);
        }
        return roots;
    }

    /**
     * 根据根节点信息查询子孙节点树，递归查询
     *
     * @param t 根节点
     */
    default void fetchAllChildren(T t) {
        if (t == null) {
            return;
        }
        List<T> children = selectByParentId(t.getId());
        if (ICollections.hasElements(children)) {
            for (T t2 : children) {
                fetchAllChildren(t2);
            }
        }
        t.setChildren(children);
    }

    default List<T> selectByParentId(ID parentId) {
        return selectByParentId(parentId, new HashMap<String, Object>());
    }

    /**
     * 根据根节点查询子级数列表，不包含根自己
     *
     * @param parentId  父节点ID
     * @param extParams 查询条件
     * @return 子级树列表
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    default List<T> selectByParentId(ID parentId, Map<String, Object> extParams) {
        Class<T> entityClass = getEntityClass();
        List<T> list = selectList(ExampleQuery.builder(entityClass)
                .andEqualTo(IConstants.PROPERTY_PARENTID, parentId).andAllEqualTo(extParams));
        // 如果可排序，需要排序，如：继承Comparable或者Orderable的类型
        if (ICollections.hasElements(list)) {
            if (entityClass.isAssignableFrom(Comparable.class)) {
                Collections.sort(list, (o1, o2) -> ((Comparable) o1).compareTo(((Comparable) o2)));
            }
        }
        return list;
    }

    /**
     * 节点移动，将节点批量移动到指定父节点下
     *
     * @param parentId 父节点ID
     * @param moveIds  需要移动的节点ID列表
     * @return 移动节点条数
     */
    default int move(ID parentId, Collection<ID> moveIds) {
        if (ICollections.hasElements(moveIds) && parentId != null) {
            for (ID moveId : moveIds) {
                updateByPrimaryKeySelective(JsonUtils.fromObject(
                        ImmutableMap.of(IConstants.PROPERTY_ID, moveId, IConstants.PROPERTY_PARENTID, parentId),
                        getEntityClass()));
            }
            return moveIds.size();
        }
        return 0;
    }

}
