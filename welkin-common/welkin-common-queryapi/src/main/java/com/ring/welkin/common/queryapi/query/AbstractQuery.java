package com.ring.welkin.common.queryapi.query;

import com.google.common.collect.Lists;
import com.ring.welkin.common.core.page.IPageable;
import com.ring.welkin.common.queryapi.query.field.Field;
import com.ring.welkin.common.queryapi.query.field.FieldGroup;
import com.ring.welkin.common.queryapi.query.field.FieldItem;
import com.ring.welkin.common.queryapi.query.field.Sort;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.AndOr;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.Operator;
import com.ring.welkin.common.queryapi.utils.MapUtils;
import com.ring.welkin.common.utils.ICollections;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.*;

/**
 * AbstractQuery 构建常用的条件结构:条件，排序，分页
 *
 * @author cloud
 * @date 2020年6月24日 上午11:13:02
 */
@ApiModel
@Getter
@SuppressWarnings("unchecked")
public abstract class AbstractQuery<T extends AbstractQuery<T>> extends PageableQuery<AbstractQuery<T>>
        implements FieldGroupQueryBuilder<T>, SortQueryBuilder<T>, TableQueryBuilder<T>, QueryBuilder<T>, Serializable {
    private static final long serialVersionUID = 4850854513242762929L;

    @ApiModelProperty(value = "表名称", hidden = true, required = false)
    private String table;

    @ApiModelProperty(value = "条件规则，多个组合条件的组合", required = false)
    protected FieldGroup fieldGroup;

    @ApiModelProperty(value = "排序属性信息", required = false)
    protected LinkedHashSet<Sort> ordSort;

    public AbstractQuery() {
        super();
    }

    public T table(String table) {
        this.table = table;
        return (T) this;
    }

    @Override
    public T clearTable() {
        this.table = null;
        return (T) this;
    }

    @Override
    public FieldGroup fieldGroup() {
        if (this.fieldGroup == null) {
            this.fieldGroup = FieldGroup.builder(AndOr.AND);
        }
        return fieldGroup;
    }

    @Override
    public T fieldGroup(FieldGroup fieldGroup) {
        if (this.fieldGroup == null) {
            this.fieldGroup = fieldGroup;
        } else {
            List<FieldItem> ordItems = fieldGroup.ordItems();
            if (ordItems != null) {
                for (FieldItem item : ordItems) {
                    if (item instanceof Field) {
                        this.fieldGroup.field((Field) item);
                    } else {
                        this.fieldGroup.group((FieldGroup) item);
                    }
                }
            }
        }
        return (T) this;
    }

    /***** clear properties ****/
    @Override
    public T clearFieldGroup() {
        if (this.fieldGroup != null) {
            this.fieldGroup = null;
        }
        return (T) this;
    }

    @Override
    public T sorts(Collection<Sort> sorts) {
        if (sorts != null && !sorts.isEmpty()) {
            if (this.ordSort == null) {
                this.ordSort = new LinkedHashSet<>();
            }
            this.ordSort.addAll(sorts);
        }
        return (T) this;
    }

    @Override
    public T clearSorts() {
        clear(this.ordSort);
        return (T) this;
    }

    @Override
    public T paged(boolean pageable) {
        return (T) super.paged(pageable);
    }

    @Override
    public T pageable(IPageable pageable) {
        return (T) super.pageable(pageable);
    }

    @Override
    public T pageNum(int pageNum) {
        return (T) super.pageNum(pageNum);
    }

    @Override
    public T pageSize(int pageSize) {
        return (T) super.pageSize(pageSize);
    }

    @Override
    public T clearPageable() {
        return (T) super.clearPageable();
    }

    @Override
    public T clear() {
        clearTable();
        clearPageable();
        clearFieldGroup();
        clearSorts();
        return (T) this;
    }

    /**
     * 获取最终的查询字段列表
     *
     * @return 最终查询字段列表
     */
    public abstract List<String> getFinalSelectProperties();

    @Override
    public T andOr(AndOr andOr) {
        // do nothing
        return (T) this;
    }

    /***** common conditions ****/
    /**
     * 直接追加条件组
     *
     * @param fieldGroups 条件组
     * @return 查询对象
     */
    public T groups(Collection<FieldGroup> fieldGroups) {
        if (ICollections.hasElements(fieldGroups)) {
            for (FieldGroup fieldGroup : fieldGroups) {
                this.fieldGroup().group(fieldGroup);
            }
        }
        return (T) this;
    }

    /**
     * 直接追加条件组
     *
     * @param fieldGroups 条件组
     * @return 查询对象
     */
    public T groups(FieldGroup... fieldGroups) {
        if (fieldGroups != null && fieldGroups.length > 0) {
            return groups(Lists.newArrayList(fieldGroups));
        }
        return (T) this;
    }

    @Override
    public T group(FieldGroup group) {
        return groups(group);
    }

    @Override
    public T group(FieldGroup group, boolean requirement) {
        if (requirement) {
            return group(group);
        }
        return (T) this;
    }

    @Override
    public T andGroup(FieldGroup group) {
        group.andOr(AndOr.AND);
        return group(group);
    }

    @Override
    public T andGroup(FieldGroup group, boolean requirement) {
        if (requirement) {
            return andGroup(group);
        }
        return (T) this;
    }

    @Override
    public T orGroup(FieldGroup group) {
        group.andOr(AndOr.OR);
        return group(group);
    }

    @Override
    public T orGroup(FieldGroup group, boolean requirement) {
        if (requirement) {
            return orGroup(group);
        }
        return (T) this;
    }

    @Override
    public T removeFields(Collection<Field> fields) {
        if (fields != null && !fields.isEmpty()) {
            this.fieldGroup().removeFields(fields);
        }
        return (T) this;
    }

    @Override
    public T removeFields(Field... fields) {
        return removeFields(Arrays.asList(fields));
    }

    @Override
    public T removeFields(String... fieldNames) {
        if (fieldNames != null && fieldNames.length > 0) {
            this.fieldGroup().removeFields(fieldNames);
        }
        return (T) this;
    }

    @Override
    public T removeField(Field field) {
        return removeFields(field);
    }

    @Override
    public T removeField(String fieldName) {
        return removeFields(fieldName);
    }

    @Override
    public T removeGroup(FieldGroup group) {
        return removeGroups(group);
    }

    @Override
    public T removeGroups(FieldGroup... groups) {
        return removeGroups(Arrays.asList(groups));
    }

    @Override
    public T removeGroups(Collection<FieldGroup> groups) {
        if (groups != null && !groups.isEmpty()) {
            this.fieldGroup().removeGroups(groups);
        }
        return (T) this;
    }

    /************************* 组装条件 ******************************/
    @Override
    public T reIndex() {
        // do nothing
        return (T) this;
    }

    @Override
    public T fields(Collection<Field> fields) {
        if (ICollections.hasElements(fields)) {
            for (Field field : fields) {
                field(field);
            }
        }
        return (T) this;
    }

    @Override
    public T fields(Field... fields) {
        if (fields != null && fields.length > 0) {
            return fields(Lists.newArrayList(fields));
        }
        return (T) this;
    }

    @Override
    public T field(Field field) {
        if (field != null) {
            this.fieldGroup().field(field);
        }
        return (T) this;
    }

    @Override
    public T andIsNull(String property) {
        field(Field.apply(AndOr.AND, property, Operator.IS_NULL));
        return (T) this;
    }

    @Override
    public T andIsNull(String property, boolean requirement) {
        if (requirement) {
            return andIsNull(property);
        }
        return (T) this;
    }

    @Override
    public T andIsNotNull(String property) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_NULL));
        return (T) this;
    }

    @Override
    public T andIsNotNull(String property, boolean requirement) {
        if (requirement) {
            return andIsNotNull(property);
        }
        return (T) this;
    }

    @Override
    public T andEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.EQUAL, value));
        return (T) this;
    }

    @Override
    public T andEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_EQUAL, value));
        return (T) this;
    }

    @Override
    public T andNotEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andNotEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andNotEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andGreaterThan(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.GREATER_THAN, value));
        return (T) this;
    }

    @Override
    public T andGreaterThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return andGreaterThan(property, value);
        }
        return (T) this;
    }

    @Override
    public T andGreaterThanIfNotNull(String property, Object value) {
        if (value != null) {
            return andGreaterThan(property, value);
        }
        return (T) this;
    }

    @Override
    public T andGreaterThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.GREATER_THAN_OR_EQUAL, value));
        return (T) this;
    }

    @Override
    public T andGreaterThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andGreaterThanOrEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andGreaterThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andGreaterThanOrEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLessThan(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.LESS_THAN, value));
        return (T) this;
    }

    @Override
    public T andLessThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return andLessThan(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLessThanIfNotNull(String property, Object value) {
        if (value != null) {
            return andLessThan(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLessThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.AND, property, Operator.LESS_THAN_OR_EQUAL, value));
        return (T) this;
    }

    @Override
    public T andLessThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return andLessThanOrEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLessThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return andLessThanOrEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andIn(String property, Object[] values) {
        field(Field.apply(AndOr.AND, property, Operator.IN, values));
        return (T) this;
    }

    @Override
    public T andIn(String property, Collection<?> values, boolean requirement) {
        if (requirement) {
            return andIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T andInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return andIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T andIn(String property, Collection<?> values) {
        return andIn(property, values.toArray());
    }

    @Override
    public T andInIfNotEmpty(String property, Collection<?> values) {
        if (ICollections.hasElements(values)) {
            return andIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T andNotIn(String property, Object[] values) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_IN, values));
        return (T) this;
    }

    @Override
    public T andNotIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return andNotIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T andNotInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return andNotIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T andNotIn(String property, Collection<?> values) {
        return andNotIn(property, values.toArray());
    }

    @Override
    public T andNotIn(String property, Collection<?> values, boolean requirement) {
        if (requirement) {
            return andNotIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T andNotInIfNotEmpty(String property, Collection<?> values) {
        if (ICollections.hasElements(values)) {
            return andNotIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T andBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.AND, property, Operator.BETWEEN, value1, value2));
        return (T) this;
    }

    @Override
    public T andBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return andBetween(property, value1, value2);
        }
        return (T) this;
    }

    @Override
    public T andNotBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_BETWEEN, value1, value2));
        return (T) this;
    }

    @Override
    public T andIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return andIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T andNotBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return andNotBetween(property, value1, value2);
        }
        return (T) this;
    }

    @Override
    public T andLike(String property, String value) {
        field(Field.apply(AndOr.AND, property, Operator.LIKE, value));
        return (T) this;
    }

    @Override
    public T andLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLeftLike(String property, String value) {
        andLike(property, "%".concat(value));
        return (T) this;
    }

    @Override
    public T andLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andRightLike(String property, String value) {
        andLike(property, value.concat("%"));
        return (T) this;
    }

    @Override
    public T andRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andFullLike(String property, String value) {
        andLike(property, "%".concat(value).concat("%"));
        return (T) this;
    }

    @Override
    public T andFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotLike(String property, String value) {
        field(Field.apply(AndOr.AND, property, Operator.NOT_LIKE, value));
        return (T) this;
    }

    @Override
    public T andNotLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotLeftLike(String property, String value) {
        andNotLike(property, "%".concat(value));
        return (T) this;
    }

    @Override
    public T andNotLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotRightLike(String property, String value) {
        andNotLike(property, value.concat("%"));
        return (T) this;
    }

    @Override
    public T andNotRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotFullLike(String property, String value) {
        andNotLike(property, "%".concat(value).concat("%"));
        return (T) this;
    }

    @Override
    public T andNotFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return andNotFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return andNotFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andEqualTo(Object param) {
        Map<String, Object> map = MapUtils.transBean2Map(param);
        map.forEach((k, v) -> {
            if (v != null) {
                andEqualTo(k, v);
            }
        });
        return (T) this;
    }

    @Override
    public T andEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return andEqualTo(param);
        }
        return (T) this;
    }

    @Override
    public T andAllEqualTo(Object param) {
        Map<String, Object> map = MapUtils.transBean2Map(param);
        map.forEach((k, v) -> {
            if (v != null) {
                andEqualTo(k, v);
            } else {
                andIsNull(k);
            }
        });
        return (T) this;
    }

    @Override
    public T andAllEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return andAllEqualTo(param);
        }
        return (T) this;
    }

    @Override
    public T orIsNull(String property) {
        field(Field.apply(AndOr.OR, property, Operator.IS_NULL));
        return (T) this;
    }

    @Override
    public T orIsNull(String property, boolean requirement) {
        if (requirement) {
            return orIsNull(property);
        }
        return (T) this;
    }

    @Override
    public T orIsNotNull(String property) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_NULL));
        return (T) this;
    }

    @Override
    public T orIsNotNull(String property, boolean requirement) {
        if (requirement) {
            return orIsNotNull(property);
        }
        return (T) this;
    }

    @Override
    public T orEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.EQUAL, value));
        return (T) this;
    }

    @Override
    public T orEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_EQUAL, value));
        return (T) this;
    }

    @Override
    public T orNotEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orNotEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orNotEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orGreaterThan(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.GREATER_THAN, value));
        return (T) this;
    }

    @Override
    public T orGreaterThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return orGreaterThan(property, value);
        }
        return (T) this;
    }

    @Override
    public T orGreaterThanIfNotNull(String property, Object value) {
        if (value != null) {
            return orGreaterThan(property, value);
        }
        return (T) this;
    }

    @Override
    public T orGreaterThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.GREATER_THAN_OR_EQUAL, value));
        return (T) this;
    }

    @Override
    public T orGreaterThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orGreaterThanOrEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orGreaterThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orGreaterThanOrEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLessThan(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.LESS_THAN, value));
        return (T) this;
    }

    @Override
    public T orLessThan(String property, Object value, boolean requirement) {
        if (requirement) {
            return orLessThan(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLessThanIfNotNull(String property, Object value) {
        if (value != null) {
            return orLessThan(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLessThanOrEqualTo(String property, Object value) {
        field(Field.apply(AndOr.OR, property, Operator.LESS_THAN_OR_EQUAL, value));
        return (T) this;
    }

    @Override
    public T orLessThanOrEqualTo(String property, Object value, boolean requirement) {
        if (requirement) {
            return orLessThanOrEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLessThanOrEqualToIfNotNull(String property, Object value) {
        if (value != null) {
            return orLessThanOrEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orIn(String property, Object[] values) {
        field(Field.apply(AndOr.OR, property, Operator.IN, values));
        return (T) this;
    }

    @Override
    public T orIn(String property, Collection<?> values, boolean requirement) {
        if (requirement) {
            return orIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T orIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return orIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T orInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return orIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T orIn(String property, Collection<?> values) {
        return orIn(property, values.toArray());
    }

    @Override
    public T orInIfNotEmpty(String property, Collection<?> values) {
        if (ICollections.hasElements(values)) {
            return orIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T orNotIn(String property, Object[] values) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_IN, values));
        return (T) this;
    }

    @Override
    public T orNotIn(String property, Object[] values, boolean requirement) {
        if (requirement) {
            return orNotIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T orNotInIfNotEmpty(String property, Object[] values) {
        if (values != null && values.length > 0) {
            return orNotIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T orNotIn(String property, Collection<?> values) {
        return orNotIn(property, values.toArray());
    }

    @Override
    public T orNotIn(String property, Collection<?> values, boolean requirement) {
        if (requirement) {
            return orNotIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T orNotInIfNotEmpty(String property, Collection<?> values) {
        if (ICollections.hasElements(values)) {
            return orNotIn(property, values);
        }
        return (T) this;
    }

    @Override
    public T orBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.OR, property, Operator.BETWEEN, value1, value2));
        return (T) this;
    }

    @Override
    public T orBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return orBetween(property, value1, value2);
        }
        return (T) this;
    }

    @Override
    public T orNotBetween(String property, Object value1, Object value2) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_BETWEEN, value1, value2));
        return (T) this;
    }

    @Override
    public T orNotBetween(String property, Object value1, Object value2, boolean requirement) {
        if (requirement) {
            return orNotBetween(property, value1, value2);
        }
        return (T) this;
    }

    @Override
    public T orLike(String property, String value) {
        field(Field.apply(AndOr.OR, property, Operator.LIKE, value));
        return (T) this;
    }

    @Override
    public T orLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLeftLike(String property, String value) {
        orLike(property, "%".concat(value));
        return (T) this;
    }

    @Override
    public T orLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orRightLike(String property, String value) {
        orLike(property, value.concat("%"));
        return (T) this;
    }

    @Override
    public T orRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orFullLike(String property, String value) {
        orLike(property, "%".concat(value).concat("%"));
        return (T) this;
    }

    @Override
    public T orFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotLike(String property, String value) {
        field(Field.apply(AndOr.OR, property, Operator.NOT_LIKE, value));
        return (T) this;
    }

    @Override
    public T orNotLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotLeftLike(String property, String value) {
        orNotLike(property, "%".concat(value));
        return (T) this;
    }

    @Override
    public T orNotLeftLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotLeftLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotRightLike(String property, String value) {
        orNotLike(property, value.concat("%"));
        return (T) this;
    }

    @Override
    public T orNotRightLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotRightLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotFullLike(String property, String value) {
        orNotLike(property, "%".concat(value).concat("%"));
        return (T) this;
    }

    @Override
    public T orNotFullLike(String property, String value, boolean requirement) {
        if (requirement) {
            return orNotFullLike(property, value);
        }
        return (T) this;

    }

    @Override
    public T orNotFullLikeIfNotNull(String property, String value) {
        if (value != null) {
            return orNotFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orEqualTo(Object param) {
        Map<String, Object> map = MapUtils.transBean2Map(param);
        map.forEach((k, v) -> {
            if (v != null) {
                orEqualTo(k, v);
            }
        });
        return (T) this;
    }

    @Override
    public T orEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return orEqualTo(param);
        }
        return (T) this;
    }

    @Override
    public T orAllEqualTo(Object param) {
        Map<String, Object> map = MapUtils.transBean2Map(param);
        map.forEach((k, v) -> {
            if (v != null) {
                orEqualTo(k, v);
            } else {
                orIsNull(k);
            }
        });
        return (T) this;
    }

    @Override
    public T orAllEqualTo(Object param, boolean requirement) {
        if (requirement) {
            return orAllEqualTo(param);
        }
        return (T) this;
    }

    @Override
    public T andEqualToIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotEqualToIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andNotEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andLeftLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andRightLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andFullLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andNotLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotLeftLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andNotLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotRightLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andNotRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T andNotFullLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return andNotFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orEqualToIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotEqualToIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orNotEqualTo(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orLeftLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orRightLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orFullLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orFullLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orNotLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotLeftLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orNotLeftLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotRightLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orNotRightLike(property, value);
        }
        return (T) this;
    }

    @Override
    public T orNotFullLikeIfNotBlank(String property, String value) {
        if (StringUtils.isNotBlank(value)) {
            return orNotFullLike(property, value);
        }
        return (T) this;
    }

}
