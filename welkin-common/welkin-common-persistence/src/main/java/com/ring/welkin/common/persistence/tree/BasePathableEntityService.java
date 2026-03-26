package com.ring.welkin.common.persistence.tree;

import com.ring.welkin.common.persistence.entity.base.BaseEntityService;
import com.ring.welkin.common.persistence.service.BaseOrderableIdableService;
import com.ring.welkin.common.persistence.service.tree.path.Pathable;
import com.ring.welkin.common.persistence.service.tree.path.TreeIdablePathableService;
import org.apache.commons.lang3.StringUtils;

public interface BasePathableEntityService<T extends BasePathableEntity<T>>
		extends BaseEntityService<T>, TreeIdablePathableService<Long, T>, BaseOrderableIdableService<Long, T> {

	@Override
	default T save(T t) {
		beforeSave(t);
		T saved = BaseEntityService.super.save(t);
		afterSave(saved);
		return saved;
	}

	/**
	 * 在保存之前设置path和order等信息
	 *
	 * @param t 保存的信息
	 */
	default void beforeSave(T t) {
		Long parentId = t.getParentId();
		resolvePath(t, parentId);
		t.setOrder(selectNewOrderByParentId(parentId));
	}

	default void resolvePath(T t, Long parentId) {
		if (parentId == null || parentId == 0L) {
			t.setParentId(null);
		} else {
			t.setPath(StringUtils.join(selectRecursionPathByParentId(parentId), t.getName(), Pathable.PATH_SEPARATOR));
		}
	}

	/**
	 * 保存之后提交审批等动作
	 *
	 * @param t 保存后的对象
	 */
	default void afterSave(T t) {
	}

	@Override
	default T update(T t) {
		beforeUpdate(t);
		return BaseEntityService.super.update(t);
	}

	default void beforeUpdate(T t) {
		resolvePath(t, t.getParentId());
	}

	/**
	 * 根据parentId查询子节点中最新的order值,需要区分是否是根目录，parentId为空或者为0则是判为根目录
	 *
	 * @param parentId 父节点ID
	 * @return 最新的order值
	 */
	int selectNewOrderByParentId(Long parentId);
}
