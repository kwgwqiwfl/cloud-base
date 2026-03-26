package com.ring.welkin.common.persistence.service;

import com.ring.welkin.common.persistence.entity.base.IConstants;
import com.ring.welkin.common.persistence.entity.gene.Idable;
import com.ring.welkin.common.persistence.entity.gene.Orderable;
import com.ring.welkin.common.persistence.entity.gene.Orderable.OrderableIdEntity;
import com.ring.welkin.common.persistence.service.entity.EntityClassService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 具有enabled属性的对象更新状态接口
 *
 * @param <ID> 主键类型
 * @param <T>  实体类型
 * @author cloud
 * @date 2020年11月12日 上午9:57:39
 */
public interface BaseOrderableIdableService<ID extends Serializable, T extends Orderable<T> & Idable<ID>>
		extends EntityClassService<T>, BaseIdableService<ID, T> {

	/**
	 * 所有记录重新排序
	 *
	 * @param sortIds 所有的记录ID排序后的集合
	 * @return 更新记录数
	 */
	default int sortAll(List<ID> sortIds) {
		if (ICollections.hasElements(sortIds)) {
			int order = 1;
			for (ID id : sortIds) {
				updateByPrimaryKeySelective(
						JsonUtils.fromObject(new OrderableIdEntity<ID>(id, order++), getEntityClass()));
			}
			return sortIds.size();
		}
		return 0;
	}

	/**
	 * 根据移动描述信息移动记录排序
	 *
	 * @param id       移动记录的ID
	 * @param reverse  是否按照逆序的排列移动，默认按照正序排序在移动，如果按照逆序排序移动就跟正序的排序结果相反
	 * @param backward true-后移，false-前移
	 * @param step     移动步长，默认1,必须大于0
	 * @param query    查询条件
	 * @return 移动结果
	 */
	default int sortByStep(ID id, boolean reverse, boolean backward, int step, ExampleQuery query) {
		T t = selectByPrimaryKey(id);
		if (t != null) {
			query = ExampleQuery.builder(query)//
				.clearExcludeProperties()//
				.clearSelectProperties()//
				.clearSorts()//
				.clearPageable()//
				.offset(0, step + 1)//
				.selectProperties(IConstants.PROPERTY_ID, IConstants.PROPERTY_ORDER)//
				.orderBy(IConstants.PROPERTY_ORDER);

			// 正序前移
			if ((!reverse && !backward) || (reverse && backward)) {
				query.andLessThanOrEqualTo(IConstants.PROPERTY_ORDER, t.getOrder());
			} else {
				query.andGreaterThanOrEqualTo(IConstants.PROPERTY_ORDER, t.getOrder());
			}

			// 查出需要排序的记录，调换order属性值重新排序
			List<T> list = selectList(query);
			if (ICollections.hasElements(list) && list.size() > 1) {
				int size = list.size();
				List<Integer> orders = list.stream().map(i -> i.getOrder()).collect(Collectors.toList());
				if (!backward) {
					t = list.get(size - 1);
					list.remove(size - 1);
					list.add(0, t);
				} else {
					t = list.get(0);
					list.remove(0);
					list.add(t);
				}

				// 设置序号
				for (int j = 0; j < orders.size(); j++) {
					list.get(j).setOrder(orders.get(j));
				}

				for (T item : list) {
					updateByPrimaryKeySelective(item);
				}
				return size;
			}
		}
		return 0;
	}

	/**
	 * 按照集合顺序重新调整顺序
	 *
	 * @param list 调整后的记录列表，序号是乱的，序号按照集合顺序重设order
	 * @return 排序结果
	 */
	default int sortByRecords(List<T> list) {
		if (ICollections.hasElements(list) && list.size() > 1) {
			int size = list.size();
			List<Integer> orders = list.stream().map(i -> i.getOrder()).collect(Collectors.toList());
			Collections.sort(orders);
			T t = list.get(size - 1);
			list.remove(size - 1);
			list.add(0, t);

			// 设置序号
			for (int j = 0; j < orders.size(); j++) {
				list.get(j).setOrder(orders.get(j));
			}

			for (T item : list) {
				updateByPrimaryKeySelective(item);
			}
			return size;
		}
		return 0;
	}
}
