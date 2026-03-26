package com.ring.welkin.common.persistence.service.tree;

import com.google.common.collect.Lists;
import com.ring.welkin.common.core.exception.ServiceException;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdable;
import com.ring.welkin.common.persistence.service.tree.id.TreeIdableMapper;
import com.ring.welkin.common.persistence.service.tree.sreach.TreeSearchable;
import com.ring.welkin.common.persistence.service.tree.sreach.TreeSearchableService;

import java.io.Serializable;
import java.util.List;

/**
 * 树状数据结构的数据模型业务处理接口定义
 *
 * @author cloud
 * @date 2019-05-29 15:56
 */
public interface TreeableAndIdableService<ID extends Serializable, T extends TreeSearchable<T> & TreeIdable<ID, T>>
		extends TreeSearchableService<T> {

	TreeIdableMapper<ID, T> getTreeableMapper();

	/**
	 * 树数据查询，可根据根节点主键和关键字检索
	 *
	 * @param rootId  根节点主键
	 * @param keyword 检索关键字
	 * @return 根节点及符合条件的后代节点
	 * @throws ServiceException
     */
    default T selectTree(ID rootId, String keyword) throws ServiceException {
        return search(selectTree(rootId), keyword);
    }

    /**
     * 根据节点ID查询以此节点为根的所有子节点
     *
     * @param rootId 根节点ID
     * @return 该节点及所有的子节点树
     * @throws ServiceException
     */
    default T selectTree(ID rootId) throws ServiceException {
        return getTreeableMapper().selectTree(rootId);
    }

	/**
	 * 根据父节点ID查询子集列表
	 *
	 * @param parentId 父节点ID
	 * @return 子集列表
	 */
	default List<T> selectByParentId(ID parentId) throws ServiceException {
		return getTreeableMapper().selectByParentId(parentId);
	}

	/**
	 * 根据叶子节点向上递归查询根节点，上一层的children属性是子节点的集合（只包含一个节点）
	 *
	 * @param leaf 叶子节点
	 * @return 根节点
	 */
	default T selectRootParentByLeaf(T leaf) {
		if (leaf != null) {
			ID parentId = leaf.getParentId();
			if (parentId != null) {
				T parent = getTreeableMapper().selectByPrimaryKey(parentId);
				if (parent != null) {
					parent.setChildren(Lists.newArrayList(leaf));
					return selectRootParentByLeaf(parent);
				}
			}
		}
		return leaf;
	}
}
