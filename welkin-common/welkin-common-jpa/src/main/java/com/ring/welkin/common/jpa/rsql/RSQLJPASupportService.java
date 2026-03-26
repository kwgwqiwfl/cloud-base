package com.ring.welkin.common.jpa.rsql;

import com.ring.welkin.common.core.page.IPageable;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * JPA RSQL对ExampleQuery参数支持API
 *
 * @param <T> 查询类型
 * @author cloud
 * @date 2022年3月28日 上午11:55:53
 */
public interface RSQLJPASupportService<T> {

	JpaSpecificationExecutor<T> getJpaSpecificationExecutor();

	/**
	 * 查询唯一一条记录
	 *
	 * @param query 查询条件
	 * @return 符合条件的一条记录或空
	 */
	@SuppressWarnings("unchecked")
	default T findOne(ExampleQuery query) {
		Optional<?> optional = RSQLJPASupport.findOne(getJpaSpecificationExecutor(),
				RSQLExampleQueryHelper.toConditionSql(query));
		return optional.isPresent() ? (T) optional.get() : null;
	}

	/**
	 * 查询列表，不分页，排序
	 *
	 * @param query 查询条件
	 * @return 符合条件的列表并排序
	 */
	@SuppressWarnings("unchecked")
	default List<T> findList(ExampleQuery query) {
		return (List<T>) RSQLJPASupport.findAll(getJpaSpecificationExecutor(),
				RSQLExampleQueryHelper.toConditionSql(query), RSQLExampleQueryHelper.toSort(query.getOrdSort()));
	}

	/**
	 * 分页查询
	 *
	 * @param query 查询条件
	 * @return 指定页码的记录
	 */
	@SuppressWarnings("unchecked")
	default Page<T> findPage(ExampleQuery query) {
		return (Page<T>) RSQLJPASupport.findAll(getJpaSpecificationExecutor(),
				RSQLExampleQueryHelper.toConditionSql(query), RSQLExampleQueryHelper.toPageable(query));
	}

	/**
	 * 符合条件的数据条数
	 *
	 * @param query 查询条件
	 * @return 数据条数
	 */
	default long count(ExampleQuery query) {
		return RSQLJPASupport.count(getJpaSpecificationExecutor(), RSQLExampleQueryHelper.toConditionSql(query));
	}

	/**
	 * 是否存在符合条件的数据
	 *
	 * @param query 查询条件
	 * @return 是否存在
	 */
	default boolean exists(ExampleQuery query) {
		return count(query) > 0;
	}

	/**
	 * 是否不存在符合条件的数据
	 *
	 * @param query 查询条件
	 * @return 是否存在
	 */
	default boolean notExists(ExampleQuery query) {
		return !exists(query);
	}

	public static class RSQLExampleQueryHelper {

		public static String toConditionSql(ExampleQuery query) {
			return RSQLCondition.build(query.fieldGroup()).getConditionSql();
		}

		public static Pageable toPageable(ExampleQuery query) {
			IPageable pageable = query.getPageable();
			if (pageable != null && pageable.isPageable()) {
				int pageNum = pageable.getPageNum();
				return PageRequest.of(pageNum > 0 ? pageNum - 1 : 0, pageable.getPageSize(),
						toSort(query.getOrdSort()));
			}
			return null;
		}

		public static Sort toSort(Collection<com.ring.welkin.common.queryapi.query.field.Sort> sorts) {
			return RSQLSort.build(sorts).getSort();
		}

		public static String toSortSql(ExampleQuery query) {
			return RSQLSort.build(query.getOrdSort()).getSortSql();
		}
	}
}
