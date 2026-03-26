package com.ring.welkin.common.persistence.service.tree.path;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.ring.welkin.common.persistence.entity.base.IConstants;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdableExampleQueryService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.utils.EntityUtils;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface TreeIdablePathableService<ID extends Serializable, T extends TreeIdablePathable<ID, T>>
    extends TreeIdableExampleQueryService<ID, T> {

    /**
     * 根据父节点的递归路径查询所有的子孙节点集合
     *
     * @param recursionPath   父节点的递归路径
     * @param otherQueryParam 其它的过滤条件
     * @return 所有子孙节点和本身的集合
     */
    default List<T> selectListByRecursionPath(String recursionPath, ExampleQuery otherQueryParam) {
        if (StringUtils.isNotEmpty(recursionPath)) {
            // select * from t where path like 'level1;level2;%'
            otherQueryParam = ExampleQuery.builder(otherQueryParam).unpaged();
            otherQueryParam.fieldGroup().andRightLike(IConstants.PROPERTY_PATH, recursionPath);
            return selectList(otherQueryParam);
        }
        return null;
    }

    /**
     * 根据父节点的相对路径查询所有的子孙节点ID集合
     *
     * @param parentId        父节点ID
     * @param otherQueryParam 其它的过滤条件
     * @return 所有子孙节点和本身的集合
     */
    default List<T> selectListByRecursionPathOfParantId(ID parentId, ExampleQuery otherQueryParam) {
        return selectListByRecursionPath(selectRecursionPathByParentId(parentId), null);
    }

    /**
     * 根据父节点的相对路径查询所有的子孙节点ID集合
     *
     * @param parentId        父节点ID
     * @param otherQueryParam 其它的过滤条件
     * @return 所有子孙节点和本身的ID集合
     */
    default List<ID> selectIdsByRecursionPathOfParantId(ID parentId, ExampleQuery otherQueryParam) {
        List<T> list = selectListByRecursionPath(selectRecursionPathByParentId(parentId),
            ExampleQuery.builder(otherQueryParam).selectProperties(IConstants.PROPERTY_ID));
        if (ICollections.hasElements(list)) {
            return list.stream().map(t -> t.getId()).collect(Collectors.toList());
        }
        return null;
    }

    /**
     * 根据父节点的相对路径查询所有的子孙节点，最终返回根节点，子孙节点通过children属性带出
     *
     * @param parentId 父节点ID
     * @return ID是parentId的节点，包含children
     */
    default T selectRootByRecursionPathOfParantId(ID parentId) {
        List<T> selectList = selectListByRecursionPathOfParantId(parentId, null);
        if (ICollections.hasElements(selectList)) {
            return fetchRoot(selectList);
        }
        return null;
    }

    /**
     * 根据父节点ID查询前置的path
     *
     * @param parentId 父节点ID
     * @return 前置路径
     */
    default String selectRecursionPathByParentId(ID parentId) {
        T t = selectByPrimaryKey(parentId);
        String path = null;
        if (t == null) {
            return "";
        } else {
            path = t.getPath();
        }

        if (StringUtils.isNotEmpty(path)) {
            return path;
        } else {
            List<T> allParents = selectAllParentsByParentId(parentId);
            if (ICollections.hasElements(allParents)) {
                path = allParents.stream().map(p -> p.getName())
                    .collect(Collectors.joining(Pathable.PATH_SEPARATOR, "", Pathable.PATH_SEPARATOR));
                t.setPath(path);
                updateByPrimaryKeySelective(t);
            }
            return path;
        }
    }

    /**
     * 获取父节点并按照层级排序
     *
     * @param parentId 迭代开始的最底级的节点ID
     * @return 父节点列表
     */
    default List<T> selectAllParentsByParentId(ID parentId) {
        return selectAllParentsByBottomId(parentId, Lists.newArrayList());
    }

    /**
     * 获取父节点并按照层级排序
     *
     * @param bottomId 迭代开始的最底级的节点ID
     * @param parents  已经查询出来的父节点列表，从高往低排序
     * @return 父节点列表
     */
    default List<T> selectAllParentsByBottomId(ID bottomId, List<T> parents) {
        // 如果已经是顶层了，则不必要再向上查询了
        if (EntityUtils.isDefaultValue(bottomId)) {
            return parents;
        }

        T t = selectByPrimaryKey(bottomId);
        if (t == null) {
            return parents;
        }

        // 新查出来的父节点放入集合最前面
        parents.add(0, t);

        // 如果已经到达根节点了就不继续向上查询了
        if (t.isRoot()) {
            return parents;
        }

        // 继续递归向上查询直到根节点
        return selectAllParentsByBottomId(t.getParentId(), parents);
    }

    @Override
    default int move(ID parentId, Collection<ID> moveIds) {
        if (ICollections.hasElements(moveIds) && parentId != null) {
            List<T> items = selectByPrimaryKeys(Lists.newArrayList(moveIds));
            if (ICollections.hasElements(items)) {
                String path = null;
                // 目标父节点的path值
                String targetParentPath = selectRecursionPathByParentId(parentId);
                for (T item : items) {
                    path = StringUtils.join(targetParentPath, item.getName(), Pathable.PATH_SEPARATOR);
                    updateByPrimaryKeySelective(JsonUtils.fromObject(ImmutableMap.of(IConstants.PROPERTY_ID,
                        item.getId(), IConstants.PROPERTY_PARENTID, parentId, IConstants.PROPERTY_PATH, path),
                        getEntityClass()));
                }
                return items.size();
            }
        }
        return 0;
    }
}
