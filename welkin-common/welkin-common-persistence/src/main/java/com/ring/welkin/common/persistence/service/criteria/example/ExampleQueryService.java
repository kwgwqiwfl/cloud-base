package com.ring.welkin.common.persistence.service.criteria.example;

import com.github.pagehelper.Page;
import com.ring.welkin.common.core.page.IPage;
import com.ring.welkin.common.core.page.IPageable;
import com.ring.welkin.common.persistence.mybatis.mapper.agg.AggCondition;
import com.ring.welkin.common.persistence.mybatis.mapper.base.BaseExampleMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.example.Example;
import com.ring.welkin.common.persistence.service.BasePageService;
import com.ring.welkin.common.persistence.service.BasePreprocessService;
import com.ring.welkin.common.persistence.service.entity.EntityClassService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.utils.Assert;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 通过Example组合查询数据的接口定义，用于复杂动态条件查询
 *
 * @author cloud
 * @date 2019年5月7日 上午10:11:38
 */
public interface ExampleQueryService<T> extends BasePageService<T>, EntityClassService<T>, BasePreprocessService<T> {

    BaseExampleMapper<T> getBaseExampleMapper();

    /**
     * 根据ExampleQuery创建example对象
     *
     * @param query 查询条件
     * @return example对象
     */
    default Example createExample(ExampleQuery query) {
        return ExampleHelper.createExample(query, getEntityClass());
    }

    /**
     * 为ExampleQuery 参数添加igoneColumn属性
     *
     * @param query ExampleQuery查询条件
     * @return 添加igoneColumn属性的查询条件
     */
    // FIXME 这里将旧的api相关查询都添加了过滤
    default ExampleQuery igoneColumnParam(ExampleQuery query) {
        if (query != null) {
			query.igoneColumn(true);
		}
		return query;
	}

	/**
	 * 根据查询条件获取记录条数
	 *
	 * @param query 查询条件
	 * @return 查询的记录条数
	 */
	default int selectCount(ExampleQuery query) {
        return getBaseExampleMapper().selectCountByExample(createExample(query));
	}

	/**
	 * 查询是否存在符合条件的记录
	 *
	 * @param query 查询条件
	 * @return 如果存在返回true，否则返回false
	 */
	default boolean exists(ExampleQuery query) {
        startPage(1, 1, null);
        return ICollections.hasElements(getBaseExampleMapper().existsWithExample(createExample(query)));
	}

	/**
	 * 根据查询条件获取数据集
	 *
	 * @param query 查询条件
	 * @return 查询的结果集
	 */
	default List<T> selectList(ExampleQuery query) {
		IPage<T> page = selectPage(query);
		if (page != null && page.isNotEmpty()) {
			return page.getList();
		}
		return new ArrayList<>();
	}

	/**
	 * 根据查询条件获取指定条数的数据集
	 *
	 * @param query 查询条件
	 * @param limit 查询条数
	 * @return 查询的结果集
	 */
	default List<T> selectListAndLimit(ExampleQuery query, int limit) {
		if (limit > 0)
			query.offset(0, limit);
		return selectList(query);
	}

	/**
	 * 根据动态查询分页查询
	 *
	 * @param query 查询条件
	 * @return 分页数据集
	 */
	default IPage<T> selectPage(ExampleQuery query) {
        IPage<T> page = null;
        IPageable pageable = query.getPageable();
        Example example = createExample(query);
		if (pageable == null || !pageable.isPageable()) {// 查询所有
			int totalCount = getBaseExampleMapper().selectCountByExample(example);
			page = IPage.<T>of(1, totalCount, totalCount);
			if (totalCount <= 0)
				return page;
			page.setList(getBaseExampleMapper().selectByExample(example));
		} else {// 分页查询
			startPage(pageable);
			Page<T> selectPage = (Page<T>) getBaseExampleMapper().selectByExample(example);
			if (selectPage != null) {
				page = IPage.<T>of(selectPage.getPageNum(), selectPage.getPageSize(), selectPage.getTotal(),
						converPageToList(selectPage.getResult()));
			} else {
				page = IPage.<T>of(pageable);
			}
		}
		return page;
	}

	/**
	 * 根据查询条件获取单条记录
	 *
	 * @param query 查询条件
	 * @return 查询的单条结果
	 */
	default T selectOne(ExampleQuery query) {
        return getBaseExampleMapper().selectOneByExample(createExample(query));
	}

	/**
	 * 根据条件查询唯一符合条件的数据
	 *
	 * @param query 查询条件
	 * @return 唯一符合条件的记录
	 */
	default Map<String, Object> selectOneMap(ExampleQuery query) {
        return getBaseExampleMapper().selectOneMapByExample(createExample(query));
	}

	/**
	 * 根据条件查询，返回Map对象
	 *
	 * @param query 查询条件，不分页
	 * @return 返回查询结果，使用List包装
	 */
	default List<Map<String, Object>> selectMapList(ExampleQuery query) {
        return getBaseExampleMapper().selectMapByExample(createExample(query));
	}

	/**
	 * 根据条件分页查询，返回Map对象
	 *
	 * @param query 查询条件，可分页可不分页
	 * @return 返回查询结果，使用IPage包装
	 */
	default IPage<Map<String, Object>> selectMapPage(ExampleQuery query) {
        IPage<Map<String, Object>> page = null;
        IPageable pageable = query.getPageable();
        Example example = createExample(query);
		if (pageable == null || !pageable.isPageable()) {// 查询所有
            int totalCount = getBaseExampleMapper().selectCountByExample(example);
            page = IPage.<Map<String, Object>>of(1, totalCount, totalCount);
            if (totalCount <= 0)
                return page;
            page.setList(getBaseExampleMapper().selectMapByExample(example));
        } else {// 分页查询
            startPage(pageable);
            Page<Map<String, Object>> selectPage = (Page<Map<String, Object>>) getBaseExampleMapper()
                    .selectMapByExample(example);
            if (selectPage != null) {
                page = IPage.<Map<String, Object>>of(selectPage.getPageNum(), selectPage.getPageSize(),
                        selectPage.getTotal(), converPageToList(selectPage.getResult()));
            } else {
                page = IPage.<Map<String, Object>>of(pageable);
			}
		}
		return page;
	}

	/**
	 * 根据查询条件获取指定条件的第一条，一般配合排序条件使用
	 *
	 * @param query 查询条件
	 * @return 查询的结果集的第一条
	 */
	default T selectFirstOne(ExampleQuery query) {
		List<T> list = selectListAndLimit(query, 1);
		return ICollections.hasElements(list) ? list.get(0) : null;
	}

	/**
	 * 根据条件删除数据
	 *
	 * @param query 删除条件
	 */
	default int delete(ExampleQuery query) {
        return getBaseExampleMapper().deleteByExample(createExample(query));
	}

	/**
	 * 根据条件更新数据（更新全部字段，即参数record中包含的所有属性）
	 *
	 * @param record 需要更新的属性列表包装对象
	 * @param query  更新的记录匹配条件
	 * @return 更新的数据条数
	 */
	default int updateByQuery(T record, ExampleQuery query) {
        preUpdate(record);
        return getBaseExampleMapper().updateByExample(record, createExample(query));
	}

	/**
	 * 根据条件更新数据（更新选择的字段，即参数record中不为空的属性）
     *
     * @param record 需要更新的属性列表包装对象
     * @param query  更新的记录匹配条件
     * @return 更新的数据条数
     */
    default int updateByQuerySelective(T record, ExampleQuery query) {
        preUpdate(record);
        return getBaseExampleMapper().updateByExampleSelective(record, createExample(query));
    }

    /**
     * 聚合查询
     *
     * @param query 查询条件
     * @param agg   聚合条件
     * @return 查询结果
     */
    default List<Map<String, Object>> selectAgg(ExampleQuery query, AggCondition agg) {
        return getBaseExampleMapper().selectAggByExample(createExample(query), agg);
    }

    /**
     * 聚合查询
     *
     * @param query  查询条件
     * @param agg    聚合条件
	 * @param eClass 泛型类型
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	default <E> List<E> selectAgg(ExampleQuery query, AggCondition agg, Class<E> eClass) {
		List<Map<String, Object>> list = selectAgg(query, agg);
		if (ICollections.hasElements(list)) {
			return JsonUtils.fromObject(list, List.class, eClass);
		}
		return new ArrayList<>();
	}

	/**
	 * 根据条件查询一个String列数据，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return String列数据
	 */
	default List<String> selectStringList(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectStringListByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Integer列数据，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Integer列数据
	 */
	default List<Integer> selectIntegerList(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectIntegerListByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Long列数据，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Long列数据
	 */
	default List<Long> selectLongList(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectLongListByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Float列数据，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Float列数据
	 */
	default List<Float> selectFloatList(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectFloatListByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Double列数据，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Double列数据
	 */
	default List<Double> selectDoubleList(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectDoubleListByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Object列数据，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Object列数据
	 */
	default List<Object> selectObjectList(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectObjectListByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个泛型动态列数据，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return 泛型动态列数据
	 */
	@SuppressWarnings("unchecked")
	default <E> List<E> selectObjectList(String propertyName, ExampleQuery query, Class<E> tClass) {
		List<Object> list = selectObjectList(propertyName, query);
		if (ICollections.hasElements(list)) {
			return JsonUtils.fromObject(list, List.class, tClass);
		}
		return new ArrayList<>();
	}

	/**
	 * 根据条件查询一个String列唯一值，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return String唯一值
	 */
	default String selectStringOne(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectStringOneByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Integer列唯一值，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Integer唯一值
	 */
	default Integer selectIntegerOne(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectIntegerOneByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Long列唯一值，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Long唯一值
	 */
	default Long selectLongOne(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectLongOneByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Float列唯一值，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Float唯一值
	 */
	default Float selectFloatOne(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectFloatOneByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个Double列唯一值，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Double唯一值
	 */
	default Double selectDoubleOne(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectDoubleOneByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
    }

    default void checkNotBlankPropertyName(String propertyName) {
        Assert.isNotBlank(propertyName, "Parameter 'propertyName' can't be blank!");
	}

	/**
	 * 根据条件查询一个Object列唯一值，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return Object唯一值
	 */
	default Object selectObjectOne(String propertyName, ExampleQuery query) {
        checkNotBlankPropertyName(propertyName);
		return getBaseExampleMapper().selectObjectOneByExample(ExampleHelper
				.createExample(query.clearSelectProperties().selectProperties(propertyName), getEntityClass()));
	}

	/**
	 * 根据条件查询一个泛型动态列唯一值，需要在查询条件中指定唯一查询的列表名称
	 *
	 * @param propertyName 查询的唯一属性名称
	 * @param query        过滤条件
	 * @return 泛型动态列唯一值
	 */
	default <E> E selectObjectOne(String propertyName, ExampleQuery query, Class<E> tClass) {
		Object object = selectObjectOne(propertyName, query);
		if (object != null) {
			return JsonUtils.fromObject(object, tClass);
		}
		return null;
	}
}
