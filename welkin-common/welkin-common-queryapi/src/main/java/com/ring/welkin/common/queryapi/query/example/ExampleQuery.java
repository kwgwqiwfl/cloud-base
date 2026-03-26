package com.ring.welkin.common.queryapi.query.example;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.ring.welkin.common.core.saas.SaasContext;
import com.ring.welkin.common.queryapi.query.field.FieldGroup;
import com.ring.welkin.common.queryapi.query.record.AbstractPropertiesQuery;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * 通用查询条件包装类
 *
 * @author cloud
 * @date 2019年6月27日 上午10:08:57
 */
@ApiModel
@Getter
@Setter
@ToString
public class ExampleQuery extends AbstractPropertiesQuery<ExampleQuery> implements ExampleQueryBuilder<ExampleQuery>,
		DistinctQueryBuilder<ExampleQuery>, ForUpdateQueryBuilder<ExampleQuery>, Serializable {
	private static final long serialVersionUID = 4850854513242762929L;

	@ApiModelProperty(value = "查询数据类型", hidden = true)
	private Class<?> entityClass;

	@ApiModelProperty(value = "是否去重，默认false", hidden = true)
	private boolean distinct;

	@ApiModelProperty(value = "忽略查询@ColumnQueryIgnore注解的字段对应列，默认false,只针对批量查询，单条无效", hidden = true)
	private boolean igoneColumn;

	@ApiModelProperty(value = "是否锁表，默认false", hidden = true)
	private boolean forUpdate;

	@ApiModelProperty(value = "需要统计的字段名，如：count(id)则该属性为'id'，统计时使用，默认为空", hidden = true)
	private String countProperty;

	@ApiModelProperty(value = "join sql：即子查询语句组合", hidden = true)
	private JoinSql joinSql;

	@ApiModelProperty(value = "查询原生字段，不使用字段映射", required = false)
	private Set<String> selectOriginalColumns;

	/*************************************
	 * 建造器
	 *****************************************/
	/**
	 * 创建一个Query的构建器
	 *
	 * @return 默认的构建器
	 */
	public static ExampleQuery builder() {
		return new ExampleQuery();
	}

	/**
	 * 创建一个Query的构建器
	 *
	 * @return 默认的构建器
	 */
	public static ExampleQuery builder(ExampleQuery query) {
		return query == null ? new ExampleQuery() : query;
	}

	/**
	 * 创建一个Query的构建器
	 *
	 * @return 默认的构建器
	 */
	public static ExampleQuery builder(FieldGroup fieldGroup) {
		return builder().fieldGroup(fieldGroup);
	}

	/**
	 * 创建一个Query的构建器
	 *
	 * @param entityClass 指定查询的数据实体类型
	 * @return 默认的构建器
	 */
	public static ExampleQuery builder(Class<?> entityClass) {
		return new ExampleQuery(entityClass);
	}

	/*************************************
	 * 构造器
	 *****************************************/
	public ExampleQuery() {
		super();
	}

	public ExampleQuery(Class<?> entityClass) {
		super();
		this.entityClass = entityClass;
	}

	public ExampleQuery igoneColumn(boolean igoneColumn) {
		this.igoneColumn = igoneColumn;
		return this;
	}

	@Override
	public ExampleQuery distinct(boolean distinct) {
		this.distinct = distinct;
		return this;
	}

	@Override
	public ExampleQuery forUpdate(boolean forUpdate) {
		this.forUpdate = forUpdate;
		return this;
	}

	@Override
	public ExampleQuery countProperty(String countProperty) {
		this.countProperty = countProperty;
		return this;
	}

	@Override
	public ExampleQuery joinSql(JoinSql joinSql) {
		this.joinSql = joinSql;
		return this;
	}

	/**
	 * 查询原始字段列表
	 *
	 * @param selectOriginalColumns 原始字段列表
	 * @return 查询对象
	 */
	public ExampleQuery selectOriginalColumns(Collection<String> selectOriginalColumns) {
		if (selectOriginalColumns != null && !selectOriginalColumns.isEmpty()) {
			if (this.selectOriginalColumns == null) {
				this.selectOriginalColumns = new LinkedHashSet<>();
			}
			this.selectOriginalColumns.addAll(selectOriginalColumns);
		}
		return this;
	}

	/**
	 * 查询原始字段列表
	 *
	 * @param selectOriginalColumns 原始字段列表
	 * @return 查询对象
	 */
	public ExampleQuery selectOriginalColumns(String... selectOriginalColumns) {
		selectOriginalColumns(Sets.newHashSet(selectOriginalColumns));
		return this;
	}

	/***** clear logic ****/
	public ExampleQuery clearIgoneColumn() {
		if (this.igoneColumn) {
			this.igoneColumn = false;
		}
		return this;
	}

	@Override
	public ExampleQuery clearDistinct() {
		if (this.distinct) {
			this.distinct = false;
		}
		return this;
	}

	@Override
	public ExampleQuery clearForUpdate() {
		if (this.forUpdate) {
			this.forUpdate = false;
		}
		return this;
	}

	@Override
	public ExampleQuery clearCountProperty() {
		if (this.countProperty != null) {
			this.countProperty = null;
		}
		return this;
	}

	@Override
	public ExampleQuery clearJoinSql() {
		this.joinSql = null;
		return this;
	}

	public ExampleQuery clearSelectOriginalColumns() {
		this.selectOriginalColumns = null;
		return this;
	}

	@Override
	public ExampleQuery clear() {
		super.clear();
		clearIgoneColumn();
		clearForUpdate();
		clearCountProperty();
		clearJoinSql();
		clearSelectOriginalColumns();
		return this;
	}

	@Override
	public List<String> getFinalSelectProperties() {
		return Lists.newArrayList(selectProperties);
	}

	/**
	 * 添加租户ID条件
	 *
	 * @param tenantId 租户ID
	 * @return 添加后的查询条件
	 */
	public ExampleQuery withTenantIdCondition(String tenantId) {
		return this.andEqualTo("tenantId", tenantId);
	}

	/**
	 * 添加当前租户ID条件
	 *
	 * @return 添加后的查询条件
	 */
	public ExampleQuery withCurrentTenantIdCondition() {
		return withTenantIdCondition(SaasContext.getCurrentTenantId());
	}

	/**
	 * 添加所属人ID条件
	 *
	 * @param owner 所属人ID
	 * @return 添加后的查询条件
	 */
	public ExampleQuery withOwnerCondition(String owner) {
		return this.andEqualTo("owner", owner);
	}

	/**
	 * 添加当前所属人ID条件
	 *
	 * @return 添加后的查询条件
	 */
	public ExampleQuery withCurrentOwnerCondition() {
		return withOwnerCondition(SaasContext.getCurrentUserId());
	}
}
