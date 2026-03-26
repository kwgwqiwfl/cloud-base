package com.ring.welkin.common.queryapi.query.aggregate;

import com.ring.welkin.common.core.page.IPageable;
import com.ring.welkin.common.queryapi.query.AbstractQuery;
import com.ring.welkin.common.queryapi.query.field.Field;
import com.ring.welkin.common.queryapi.query.field.FieldGroup;
import com.ring.welkin.common.queryapi.query.field.FieldItem;
import com.ring.welkin.common.queryapi.query.field.Sort;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.AndOr;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * 构建聚合查询条件
 *
 * <pre>
 * 	<code>
 *   SELECT
 *   	project,
 *   	manager,
 *   	code,
 *   	count(income) count_income,
 *   	sum(income) sum_income,
 *   	avg(income) avg_income,
 *   	max(income) max_income,
 *   	min(income) min_income
 *   FROM
 *   	t_p
 *   WHERE
 *     project <> 'px'
 *   GROUP BY
 *   	project, manager, code
 *   HAVING
 *   	( code > '1' AND avg_income > 10 ) OR min_income > 5000
 *   ORDER BY
 *   	min_income, sum_income
 *   LIMIT 1,2
 *  </code>
 * </pre>
 *
 * @param <T> AbstractQuery子类
 * @author cloud
 * @date 2020年6月24日 上午11:13:02
 */
@ApiModel
@Getter
@Setter
@SuppressWarnings("unchecked")
public abstract class AbstractAggQuery<T extends AbstractAggQuery<T>> extends AbstractQuery<AbstractAggQuery<T>>
        implements AggFieldQueryBuilder<T>, GroupByFieldQueryBuilder<T>, HavingFieldGroupQueryBuilder<T> {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty(value = "聚合条件",required = true)
    protected LinkedHashSet<AggField> aggFields;

    @ApiModelProperty(value = "分组字段",required = true)
    protected LinkedHashSet<String> groupFields;

    @ApiModelProperty(value = "having查询条件",required = false)
    protected FieldGroup havingFieldGroup;

    public AbstractAggQuery() {
        super();
    }

    @Override
    public T aggFields(Collection<AggField> aggFields) {
        if (aggFields != null && !aggFields.isEmpty()) {
            if (this.aggFields == null) {
                this.aggFields = new LinkedHashSet<>();
            }
            this.aggFields.addAll(aggFields);
        }
        return (T) this;
    }

    @Override
    public T clearAggFields() {
        clear(this.aggFields);
        return (T) this;
    }

    @Override
    public T groupFields(Collection<String> groupFields) {
        if (groupFields != null && !groupFields.isEmpty()) {
            if (this.groupFields == null) {
                this.groupFields = new LinkedHashSet<>();
            }
            this.groupFields.addAll(groupFields);
        }
        return (T) this;
    }

    @Override
    public T clearGroupFields() {
        clear(this.groupFields);
        return (T) this;
    }

    @Override
    public FieldGroup havingFieldGroup() {
        if (this.havingFieldGroup == null) {
            this.havingFieldGroup = FieldGroup.builder(AndOr.AND);
        }
        return havingFieldGroup;
    }

    @Override
    public T havingFieldGroup(FieldGroup fieldGroup) {
        if (this.havingFieldGroup == null) {
            this.havingFieldGroup = fieldGroup;
        } else {
            List<FieldItem> ordItems = fieldGroup.ordItems();
            if (ordItems != null) {
                for (FieldItem item : ordItems) {
                    if (item instanceof Field) {
                        this.havingFieldGroup.field((Field) item);
                    } else {
                        this.havingFieldGroup.group((FieldGroup) item);
                    }
                }
            }
        }
        return (T) this;
    }

    @Override
    public T clearHavingFieldGroup() {
        if (this.havingFieldGroup != null) {
            this.havingFieldGroup = null;
        }
        return (T) this;
    }

    @Override
    public T clear() {
        super.clear();
        clearAggFields();
        clearGroupFields();
        clearHavingFieldGroup();
        return (T) this;
    }

    @Override
    public T clearFieldGroup() {
        super.clearFieldGroup();
        return (T) this;
    }

    @Override
    public T clearSorts() {
        super.clearSorts();
        return (T) this;
    }

    @Override
    public T sorts(Sort... sorts) {
        super.sorts(sorts);
        return (T) this;
    }

    @Override
    public T sort(Sort sort) {
        super.sort(sort);
        return (T) this;
    }

    @Override
    public T orderBy(String fieldName, boolean asc) {
        super.orderBy(fieldName, asc);
        return (T) this;
    }

    @Override
    public T orderBy(String fieldName) {
        super.orderBy(fieldName);
        return (T) this;
    }

    @Override
    public T orderByAsc(String fieldName) {
        super.orderByAsc(fieldName);
        return (T) this;
    }

    @Override
    public T orderByAsc(String... fieldNames) {
        super.orderByAsc(fieldNames);
        return (T) this;
    }

    @Override
    public T orderByDesc(String fieldName) {
        super.orderByDesc(fieldName);
        return (T) this;
    }

    @Override
    public T orderByDesc(String... fieldNames) {
        super.orderByDesc(fieldNames);
        return (T) this;
    }

    @Override
    public T paged() {
        super.paged();
        return (T) this;
    }

    @Override
    public T unpaged() {
        super.unpaged();
        return (T) this;
    }

    @Override
    public T page(int pageNum, int pageSize) {
        super.page(pageNum, pageSize);
        return (T) this;
    }

    @Override
    public T offset(int offset, int limit) {
        super.offset(offset, limit);
        return (T) this;
    }

    @Override
    public T fieldGroup(FieldGroup fieldGroup) {
        super.fieldGroup(fieldGroup);
        return (T) this;
    }

    @Override
    public T sorts(Collection<Sort> sorts) {
        super.sorts(sorts);
        return (T) this;
    }

    @Override
    public T paged(boolean pageable) {
        super.paged(pageable);
        return (T) this;
    }

    @Override
    public T pageable(IPageable pageable) {
        super.pageable(pageable);
        return (T) this;
    }

    @Override
    public T pageNum(int pageNum) {
        super.pageNum(pageNum);
        return (T) this;
    }

    @Override
    public T pageSize(int pageSize) {
        super.pageSize(pageSize);
        return (T) this;
    }

    @Override
    public T clearPageable() {
        super.clearPageable();
        return (T) this;
    }

    @Override
    public T table(String table) {
        super.table(table);
        return (T) this;
    }

    @Override
    public T clearTable() {
        super.clearTable();
        return (T) this;
    }

    @Override
    public T andOr(AndOr andOr) {
        return (T) super.andOr(andOr);
    }

    @Override
    public T groups(Collection<FieldGroup> fieldGroups) {
        return (T) super.groups(fieldGroups);
    }

    @Override
    public T groups(FieldGroup... fieldGroups) {
        return (T) super.groups(fieldGroups);
    }

    @Override
    public T group(FieldGroup group) {
        return (T) super.group(group);
    }

    @Override
    public T group(FieldGroup group, boolean requirement) {
        return (T) super.group(group, requirement);
    }

    @Override
    public T andGroup(FieldGroup group) {
        return (T) super.andGroup(group);
    }

    @Override
    public T andGroup(FieldGroup group, boolean requirement) {
        return (T) super.andGroup(group, requirement);
    }

    @Override
    public T orGroup(FieldGroup group) {
        return (T) super.orGroup(group);
    }

    @Override
    public T orGroup(FieldGroup group, boolean requirement) {
        return (T) super.orGroup(group, requirement);
    }

    @Override
    public T removeFields(Collection<Field> fields) {
        return (T) super.removeFields(fields);
    }

    @Override
    public T removeFields(Field... fields) {
        return (T) super.removeFields(fields);
    }

    @Override
    public T removeFields(String... fieldNames) {
        return (T) super.removeFields(fieldNames);
    }

    @Override
    public T removeField(Field field) {
        return (T) super.removeField(field);
    }

    @Override
    public T removeField(String fieldName) {
        return (T) super.removeField(fieldName);
    }

    @Override
    public T removeGroup(FieldGroup group) {
        return (T) super.removeGroup(group);
    }

    @Override
    public T removeGroups(FieldGroup... groups) {
        return (T) super.removeGroups(groups);
    }

    @Override
    public T removeGroups(Collection<FieldGroup> groups) {
        return (T) super.removeGroups(groups);
    }

    @Override
    public T reIndex() {
        return (T) super.reIndex();
    }

    @Override
    public T fields(Collection<Field> fields) {
        return (T) super.fields(fields);
    }

    @Override
    public T fields(Field... fields) {
        return (T) super.fields(fields);
    }

    @Override
    public T field(Field field) {
        return (T) super.field(field);
    }

    @Override
    public T andIsNull(String property) {
        return (T) super.andIsNull(property);
    }

    @Override
    public T andIsNull(String property, boolean requirement) {
        return (T) super.andIsNull(property, requirement);
    }

    @Override
    public T andIsNotNull(String property) {
        return (T) super.andIsNotNull(property);
    }

    @Override
    public T andIsNotNull(String property, boolean requirement) {
        return (T) super.andIsNotNull(property, requirement);
    }

    @Override
    public T andEqualTo(String property, Object value) {
        return (T) super.andEqualTo(property, value);
    }

    @Override
    public T andEqualTo(String property, Object value, boolean requirement) {
        return (T) super.andEqualTo(property, value, requirement);
    }

    @Override
    public T andEqualToIfNotNull(String property, Object value) {
        return (T) super.andEqualToIfNotNull(property, value);
    }

    @Override
    public T andNotEqualTo(String property, Object value) {
        return (T) super.andNotEqualTo(property, value);
    }

    @Override
    public T andNotEqualTo(String property, Object value, boolean requirement) {
        return (T) super.andNotEqualTo(property, value, requirement);
    }

    @Override
    public T andNotEqualToIfNotNull(String property, Object value) {
        return (T) super.andNotEqualToIfNotNull(property, value);
    }

    @Override
    public T andGreaterThan(String property, Object value) {
        return (T) super.andGreaterThan(property, value);
    }

    @Override
    public T andGreaterThan(String property, Object value, boolean requirement) {
        return (T) super.andGreaterThan(property, value, requirement);
    }

    @Override
    public T andGreaterThanIfNotNull(String property, Object value) {
        return (T) super.andGreaterThanIfNotNull(property, value);
    }

    @Override
    public T andGreaterThanOrEqualTo(String property, Object value) {
        return (T) super.andGreaterThanOrEqualTo(property, value);
    }

    @Override
    public T andGreaterThanOrEqualTo(String property, Object value, boolean requirement) {
        return (T) super.andGreaterThanOrEqualTo(property, value, requirement);
    }

    @Override
    public T andGreaterThanOrEqualToIfNotNull(String property, Object value) {
        return (T) super.andGreaterThanOrEqualToIfNotNull(property, value);
    }

    @Override
    public T andLessThan(String property, Object value) {
        return (T) super.andLessThan(property, value);
    }

    @Override
    public T andLessThan(String property, Object value, boolean requirement) {
        return (T) super.andLessThan(property, value, requirement);
    }

    @Override
    public T andLessThanIfNotNull(String property, Object value) {
        return (T) super.andLessThanIfNotNull(property, value);
    }

    @Override
    public T andLessThanOrEqualTo(String property, Object value) {
        return (T) super.andLessThanOrEqualTo(property, value);
    }

    @Override
    public T andLessThanOrEqualTo(String property, Object value, boolean requirement) {
        return (T) super.andLessThanOrEqualTo(property, value, requirement);
    }

    @Override
    public T andLessThanOrEqualToIfNotNull(String property, Object value) {
        return (T) super.andLessThanOrEqualToIfNotNull(property, value);
    }

    @Override
    public T andIn(String property, Object[] values) {
        return (T) super.andIn(property, values);
    }

    @Override
    public T andIn(String property, Collection<?> values, boolean requirement) {
        return (T) super.andIn(property, values, requirement);
    }

    @Override
    public T andInIfNotEmpty(String property, Object[] values) {
        return (T) super.andInIfNotEmpty(property, values);
    }

    @Override
    public T andIn(String property, Collection<?> values) {
        return (T) super.andIn(property, values);
    }

    @Override
    public T andInIfNotEmpty(String property, Collection<?> values) {
        return (T) super.andInIfNotEmpty(property, values);
    }

    @Override
    public T andNotIn(String property, Object[] values) {
        return (T) super.andNotIn(property, values);
    }

    @Override
    public T andNotIn(String property, Object[] values, boolean requirement) {
        return (T) super.andNotIn(property, values, requirement);
    }

    @Override
    public T andNotInIfNotEmpty(String property, Object[] values) {
        return (T) super.andNotInIfNotEmpty(property, values);
    }

    @Override
    public T andNotIn(String property, Collection<?> values) {
        return (T) super.andNotIn(property, values);
    }

    @Override
    public T andNotIn(String property, Collection<?> values, boolean requirement) {
        return (T) super.andNotIn(property, values, requirement);
    }

    @Override
    public T andNotInIfNotEmpty(String property, Collection<?> values) {
        return (T) super.andNotInIfNotEmpty(property, values);
    }

    @Override
    public T andBetween(String property, Object value1, Object value2) {
        return (T) super.andBetween(property, value1, value2);
    }

    @Override
    public T andBetween(String property, Object value1, Object value2, boolean requirement) {
        return (T) super.andBetween(property, value1, value2, requirement);
    }

    @Override
    public T andNotBetween(String property, Object value1, Object value2) {
        return (T) super.andNotBetween(property, value1, value2);
    }

    @Override
    public T andIn(String property, Object[] values, boolean requirement) {
        return (T) super.andIn(property, values, requirement);
    }

    @Override
    public T andNotBetween(String property, Object value1, Object value2,
                           boolean requirement) {
        return (T) super.andNotBetween(property, value1, value2, requirement);
    }

    @Override
    public T andLike(String property, String value) {
        return (T) super.andLike(property, value);
    }

    @Override
    public T andLike(String property, String value, boolean requirement) {
        return (T) super.andLike(property, value, requirement);
    }

    @Override
    public T andLikeIfNotNull(String property, String value) {
        return (T) super.andLikeIfNotNull(property, value);
    }

    @Override
    public T andLeftLike(String property, String value) {
        return (T) super.andLeftLike(property, value);
    }

    @Override
    public T andLeftLike(String property, String value, boolean requirement) {
        return (T) super.andLeftLike(property, value, requirement);
    }

    @Override
    public T andLeftLikeIfNotNull(String property, String value) {
        return (T) super.andLeftLikeIfNotNull(property, value);
    }

    @Override
    public T andRightLike(String property, String value) {
        return (T) super.andRightLike(property, value);
    }

    @Override
    public T andRightLike(String property, String value, boolean requirement) {
        return (T) super.andRightLike(property, value, requirement);
    }

    @Override
    public T andRightLikeIfNotNull(String property, String value) {
        return (T) super.andRightLikeIfNotNull(property, value);
    }

    @Override
    public T andFullLike(String property, String value) {
        return (T) super.andFullLike(property, value);
    }

    @Override
    public T andFullLike(String property, String value, boolean requirement) {
        return (T) super.andFullLike(property, value, requirement);
    }

    @Override
    public T andFullLikeIfNotNull(String property, String value) {
        return (T) super.andFullLikeIfNotNull(property, value);
    }

    @Override
    public T andNotLike(String property, String value) {
        return (T) super.andNotLike(property, value);
    }

    @Override
    public T andNotLike(String property, String value, boolean requirement) {
        return (T) super.andNotLike(property, value, requirement);
    }

    @Override
    public T andNotLikeIfNotNull(String property, String value) {
        return (T) super.andNotLikeIfNotNull(property, value);
    }

    @Override
    public T andNotLeftLike(String property, String value) {
        return (T) super.andNotLeftLike(property, value);
    }

    @Override
    public T andNotLeftLike(String property, String value, boolean requirement) {
        return (T) super.andNotLeftLike(property, value, requirement);
    }

    @Override
    public T andNotLeftLikeIfNotNull(String property, String value) {
        return (T) super.andNotLeftLikeIfNotNull(property, value);
    }

    @Override
    public T andNotRightLike(String property, String value) {
        return (T) super.andNotRightLike(property, value);
    }

    @Override
    public T andNotRightLike(String property, String value, boolean requirement) {
        return (T) super.andNotRightLike(property, value, requirement);
    }

    @Override
    public T andNotRightLikeIfNotNull(String property, String value) {
        return (T) super.andNotRightLikeIfNotNull(property, value);
    }

    @Override
    public T andNotFullLike(String property, String value) {
        return (T) super.andNotFullLike(property, value);
    }

    @Override
    public T andNotFullLike(String property, String value, boolean requirement) {
        return (T) super.andNotFullLike(property, value, requirement);
    }

    @Override
    public T andNotFullLikeIfNotNull(String property, String value) {
        return (T) super.andNotFullLikeIfNotNull(property, value);
    }

    @Override
    public T andEqualTo(Object param) {
        return (T) super.andEqualTo(param);
    }

    @Override
    public T andEqualTo(Object param, boolean requirement) {
        return (T) super.andEqualTo(param, requirement);
    }

    @Override
    public T andAllEqualTo(Object param) {
        return (T) super.andAllEqualTo(param);
    }

    @Override
    public T andAllEqualTo(Object param, boolean requirement) {
        return (T) super.andAllEqualTo(param, requirement);
    }

    @Override
    public T orIsNull(String property) {
        return (T) super.orIsNull(property);
    }

    @Override
    public T orIsNull(String property, boolean requirement) {
        return (T) super.orIsNull(property, requirement);
    }

    @Override
    public T orIsNotNull(String property) {
        return (T) super.orIsNotNull(property);
    }

    @Override
    public T orIsNotNull(String property, boolean requirement) {
        return (T) super.orIsNotNull(property, requirement);
    }

    @Override
    public T orEqualTo(String property, Object value) {
        return (T) super.orEqualTo(property, value);
    }

    @Override
    public T orEqualTo(String property, Object value, boolean requirement) {
        return (T) super.orEqualTo(property, value, requirement);
    }

    @Override
    public T orEqualToIfNotNull(String property, Object value) {
        return (T) super.orEqualToIfNotNull(property, value);
    }

    @Override
    public T orNotEqualTo(String property, Object value) {
        return (T) super.orNotEqualTo(property, value);
    }

    @Override
    public T orNotEqualTo(String property, Object value, boolean requirement) {
        return (T) super.orNotEqualTo(property, value, requirement);
    }

    @Override
    public T orNotEqualToIfNotNull(String property, Object value) {
        return (T) super.orNotEqualToIfNotNull(property, value);
    }

    @Override
    public T orGreaterThan(String property, Object value) {
        return (T) super.orGreaterThan(property, value);
    }

    @Override
    public T orGreaterThan(String property, Object value, boolean requirement) {
        return (T) super.orGreaterThan(property, value, requirement);
    }

    @Override
    public T orGreaterThanIfNotNull(String property, Object value) {
        return (T) super.orGreaterThanIfNotNull(property, value);
    }

    @Override
    public T orGreaterThanOrEqualTo(String property, Object value) {
        return (T) super.orGreaterThanOrEqualTo(property, value);
    }

    @Override
    public T orGreaterThanOrEqualTo(String property, Object value, boolean requirement) {
        return (T) super.orGreaterThanOrEqualTo(property, value, requirement);
    }

    @Override
    public T orGreaterThanOrEqualToIfNotNull(String property, Object value) {
        return (T) super.orGreaterThanOrEqualToIfNotNull(property, value);
    }

    @Override
    public T orLessThan(String property, Object value) {
        return (T) super.orLessThan(property, value);
    }

    @Override
    public T orLessThan(String property, Object value, boolean requirement) {
        return (T) super.orLessThan(property, value, requirement);
    }

    @Override
    public T orLessThanIfNotNull(String property, Object value) {
        return (T) super.orLessThanIfNotNull(property, value);
    }

    @Override
    public T orLessThanOrEqualTo(String property, Object value) {
        return (T) super.orLessThanOrEqualTo(property, value);
    }

    @Override
    public T orLessThanOrEqualTo(String property, Object value, boolean requirement) {
        return (T) super.orLessThanOrEqualTo(property, value, requirement);
    }

    @Override
    public T orLessThanOrEqualToIfNotNull(String property, Object value) {
        return (T) super.orLessThanOrEqualToIfNotNull(property, value);
    }

    @Override
    public T orIn(String property, Object[] values) {
        return (T) super.orIn(property, values);
    }

    @Override
    public T orIn(String property, Collection<?> values, boolean requirement) {
        return (T) super.orIn(property, values, requirement);
    }

    @Override
    public T orIn(String property, Object[] values, boolean requirement) {
        return (T) super.orIn(property, values, requirement);
    }

    @Override
    public T orInIfNotEmpty(String property, Object[] values) {
        return (T) super.orInIfNotEmpty(property, values);
    }

    @Override
    public T orIn(String property, Collection<?> values) {
        return (T) super.orIn(property, values);
    }

    @Override
    public T orInIfNotEmpty(String property, Collection<?> values) {
        return (T) super.orInIfNotEmpty(property, values);
    }

    @Override
    public T orNotIn(String property, Object[] values) {
        return (T) super.orNotIn(property, values);
    }

    @Override
    public T orNotIn(String property, Object[] values, boolean requirement) {
        return (T) super.orNotIn(property, values, requirement);
    }

    @Override
    public T orNotInIfNotEmpty(String property, Object[] values) {
        return (T) super.orNotInIfNotEmpty(property, values);
    }

    @Override
    public T orNotIn(String property, Collection<?> values) {
        return (T) super.orNotIn(property, values);
    }

    @Override
    public T orNotIn(String property, Collection<?> values, boolean requirement) {
        return (T) super.orNotIn(property, values, requirement);
    }

    @Override
    public T orNotInIfNotEmpty(String property, Collection<?> values) {
        return (T) super.orNotInIfNotEmpty(property, values);
    }

    @Override
    public T orBetween(String property, Object value1, Object value2) {
        return (T) super.orBetween(property, value1, value2);
    }

    @Override
    public T orBetween(String property, Object value1, Object value2, boolean requirement) {
        return (T) super.orBetween(property, value1, value2, requirement);
    }

    @Override
    public T orNotBetween(String property, Object value1, Object value2) {
        return (T) super.orNotBetween(property, value1, value2);
    }

    @Override
    public T orNotBetween(String property, Object value1, Object value2, boolean requirement) {
        return (T) super.orNotBetween(property, value1, value2, requirement);
    }

    @Override
    public T orLike(String property, String value) {
        return (T) super.orLike(property, value);
    }

    @Override
    public T orLike(String property, String value, boolean requirement) {
        return (T) super.orLike(property, value, requirement);
    }

    @Override
    public T orLikeIfNotNull(String property, String value) {
        return (T) super.orLikeIfNotNull(property, value);
    }

    @Override
    public T orLeftLike(String property, String value) {
        return (T) super.orLeftLike(property, value);
    }

    @Override
    public T orLeftLike(String property, String value, boolean requirement) {
        return (T) super.orLeftLike(property, value, requirement);
    }

    @Override
    public T orLeftLikeIfNotNull(String property, String value) {
        return (T) super.orLeftLikeIfNotNull(property, value);
    }

    @Override
    public T orRightLike(String property, String value) {
        return (T) super.orRightLike(property, value);
    }

    @Override
    public T orRightLike(String property, String value, boolean requirement) {
        return (T) super.orRightLike(property, value, requirement);
    }

    @Override
    public T orRightLikeIfNotNull(String property, String value) {
        return (T) super.orRightLikeIfNotNull(property, value);
    }

    @Override
    public T orFullLike(String property, String value) {
        return (T) super.orFullLike(property, value);
    }

    @Override
    public T orFullLike(String property, String value, boolean requirement) {
        return (T) super.orFullLike(property, value, requirement);
    }

    @Override
    public T orFullLikeIfNotNull(String property, String value) {
        return (T) super.orFullLikeIfNotNull(property, value);
    }

    @Override
    public T orNotLike(String property, String value) {
        return (T) super.orNotLike(property, value);
    }

    @Override
    public T orNotLike(String property, String value, boolean requirement) {
        return (T) super.orNotLike(property, value, requirement);
    }

    @Override
    public T orNotLikeIfNotNull(String property, String value) {
        return (T) super.orNotLikeIfNotNull(property, value);
    }

    @Override
    public T orNotLeftLike(String property, String value) {
        return (T) super.orNotLeftLike(property, value);
    }

    @Override
    public T orNotLeftLike(String property, String value, boolean requirement) {
        return (T) super.orNotLeftLike(property, value, requirement);
    }

    @Override
    public T orNotLeftLikeIfNotNull(String property, String value) {
        return (T) super.orNotLeftLikeIfNotNull(property, value);
    }

    @Override
    public T orNotRightLike(String property, String value) {
        return (T) super.orNotRightLike(property, value);
    }

    @Override
    public T orNotRightLike(String property, String value, boolean requirement) {
        return (T) super.orNotRightLike(property, value, requirement);
    }

    @Override
    public T orNotRightLikeIfNotNull(String property, String value) {
        return (T) super.orNotRightLikeIfNotNull(property, value);
    }

    @Override
    public T orNotFullLike(String property, String value) {
        return (T) super.orNotFullLike(property, value);
    }

    @Override
    public T orNotFullLike(String property, String value, boolean requirement) {
        return (T) super.orNotFullLike(property, value, requirement);
    }

    @Override
    public T orNotFullLikeIfNotNull(String property, String value) {
        return (T) super.orNotFullLikeIfNotNull(property, value);
    }

    @Override
    public T orEqualTo(Object param) {
        return (T) super.orEqualTo(param);
    }

    @Override
    public T orEqualTo(Object param, boolean requirement) {
        return (T) super.orEqualTo(param, requirement);
    }

    @Override
    public T orAllEqualTo(Object param) {
        return (T) super.orAllEqualTo(param);
    }

    @Override
    public T orAllEqualTo(Object param, boolean requirement) {
        return (T) super.orAllEqualTo(param, requirement);
    }

    @Override
    public T andEqualToIfNotBlank(String property, String value) {
        return (T) super.andEqualToIfNotBlank(property, value);
    }

    @Override
    public T andNotEqualToIfNotBlank(String property, String value) {
        return (T) super.andNotEqualToIfNotBlank(property, value);
    }

    @Override
    public T andLikeIfNotBlank(String property, String value) {
        return (T) super.andLikeIfNotBlank(property, value);
    }

    @Override
    public T andLeftLikeIfNotBlank(String property, String value) {
        return (T) super.andLeftLikeIfNotBlank(property, value);
    }

    @Override
    public T andRightLikeIfNotBlank(String property, String value) {
        return (T) super.andRightLikeIfNotBlank(property, value);
    }

    @Override
    public T andFullLikeIfNotBlank(String property, String value) {
        return (T) super.andFullLikeIfNotBlank(property, value);
    }

    @Override
    public T andNotLikeIfNotBlank(String property, String value) {
        return (T) super.andNotLikeIfNotBlank(property, value);
    }

    @Override
    public T andNotLeftLikeIfNotBlank(String property, String value) {
        return (T) super.andNotLeftLikeIfNotBlank(property, value);
    }

    @Override
    public T andNotRightLikeIfNotBlank(String property, String value) {
        return (T) super.andNotRightLikeIfNotBlank(property, value);
    }

    @Override
    public T andNotFullLikeIfNotBlank(String property, String value) {
        return (T) super.andNotFullLikeIfNotBlank(property, value);
    }

    @Override
    public T orEqualToIfNotBlank(String property, String value) {
        return (T) super.orEqualToIfNotBlank(property, value);
    }

    @Override
    public T orNotEqualToIfNotBlank(String property, String value) {
        return (T) super.orNotEqualToIfNotBlank(property, value);
    }

    @Override
    public T orLikeIfNotBlank(String property, String value) {
        return (T) super.orLikeIfNotBlank(property, value);
    }

    @Override
    public T orLeftLikeIfNotBlank(String property, String value) {
        return (T) super.orLeftLikeIfNotBlank(property, value);
    }

    @Override
    public T orRightLikeIfNotBlank(String property, String value) {
        return (T) super.orRightLikeIfNotBlank(property, value);
    }

    @Override
    public T orFullLikeIfNotBlank(String property, String value) {
        return (T) super.orFullLikeIfNotBlank(property, value);
    }

    @Override
    public T orNotLikeIfNotBlank(String property, String value) {
        return (T) super.orNotLikeIfNotBlank(property, value);
    }

    @Override
    public T orNotLeftLikeIfNotBlank(String property, String value) {
        return (T) super.orNotLeftLikeIfNotBlank(property, value);
    }

    @Override
    public T orNotRightLikeIfNotBlank(String property, String value) {
        return (T) super.orNotRightLikeIfNotBlank(property, value);
    }

    @Override
    public T orNotFullLikeIfNotBlank(String property, String value) {
        return (T) super.orNotFullLikeIfNotBlank(property, value);
    }

}
