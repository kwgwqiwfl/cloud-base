package com.ring.welkin.common.persistence.jpa.criteria;

import com.google.common.collect.Lists;
import com.ring.welkin.common.core.page.IPage;
import com.ring.welkin.common.persistence.entity.base.IConstants;
import com.ring.welkin.common.persistence.jpa.criteria.query.FieldObject;
import com.ring.welkin.common.persistence.jpa.criteria.query.JpaCriteriaHelper.ComparatorOperator;
import com.ring.welkin.common.persistence.jpa.criteria.query.JpaCriteriaHelper.LogicalOperator;
import com.ring.welkin.common.persistence.jpa.criteria.query.JpaCriteriaHelper.OrderDirection;
import com.ring.welkin.common.persistence.jpa.criteria.query.QueryObject;
import com.ring.welkin.common.persistence.jpa.criteria.query.SortObject;
import com.ring.welkin.common.persistence.jpa.page.Page;
import com.ring.welkin.common.persistence.service.criteria.example.ExampleQueryService;
import com.ring.welkin.common.queryapi.query.example.ExampleQuery;
import com.ring.welkin.common.queryapi.query.field.Field;
import com.ring.welkin.common.queryapi.query.field.FieldGroup;
import com.ring.welkin.common.queryapi.query.field.Sort;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.AndOr;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.Operator;
import com.ring.welkin.common.queryapi.query.field.SqlEnums.OrderType;
import com.ring.welkin.common.utils.ICollections;

import java.util.Date;
import java.util.List;

/**
 * QueryObject对象到ExampleQuery对象转化辅助类，QueryObject相关方法已经弃用，推荐使用ExampleQuery相关的查询API，这里是为了暂时兼容原来的JPA版本做的过渡逻辑
 *
 * @author cloud
 * @date 2019年8月23日 上午9:49:59
 */
@SuppressWarnings("deprecation")
class ExampleQueryTransferHelper {

    /**
     * 条件转化
     *
     * @param queryObject 原来的JPA QueryObject条件
     * @return Mybatis的ExampleQuery查询条件
     */
    public static ExampleQuery queryObjectToExampleQuery(QueryObject queryObject, Class<?> entityClass) {
        ExampleQuery query = ExampleQuery.builder(entityClass);
        if (queryObject == null) {
            return query;
        }

        // 分页
        Integer limit = queryObject.getLimit();
        if (limit != null) {
            Integer offset = queryObject.getOffset();
            if (offset == null) {
                offset = 0;
            }
            query.offset(offset, limit);
        }

        // 条件
        List<FieldObject> fieldList = queryObject.getFieldList();
        if (fieldList != null) {
            query.fieldGroup(ExampleQueryTransferHelper.fieldListToFieldGroup(fieldList));//
        }

        // 排序
        SortObject sortObject = queryObject.getSortObject();
        if (sortObject != null) {
            query.sorts(ExampleQueryTransferHelper.sortObjectToSort(sortObject));
        }

        return query;
    }

    public static FieldGroup fieldListToFieldGroup(List<FieldObject> fieldList) {
        FieldGroup group = FieldGroup.builder();
        if (fieldList != null) {
            for (FieldObject fieldObject : fieldList) {
                Field field = fieldObjectToField(fieldObject);
                if (field != null) {
                    group.field(field);
                }
            }
        }
        return group;
    }

    // 检查一些不合格的参数或者格式，进行格式化处理
    private static FieldObject checkFieldName(FieldObject fieldObject) {
        if (fieldObject != null) {
            String fieldName = fieldObject.getFieldName();
            Object fieldValue = fieldObject.getFieldValue();
            if (IConstants.PROPERTY_LASTMODIFIEDTIME.equals(fieldName) || IConstants.PROPERTY_CREATETIME.equals(fieldName)) {
                if (fieldValue instanceof Long) {
                    fieldObject = new FieldObject(fieldName, new Date((long) fieldValue),
                            fieldObject.getComparatorOperator(), fieldObject.getLogicalOperator());
                }
            }
        }
        return fieldObject;
    }

    public static Field fieldObjectToField(FieldObject fieldObject) {
        fieldObject = checkFieldName(fieldObject);
        if (fieldObject == null) {
            return null;
        }

        Object obj = fieldObject.getFieldValue();
        if (obj == null) {
            switch (fieldObject.getComparatorOperator()) {
                // 如果比较关系不需要值得时候则添加否则不添加条件
                case NULL:
                case NOT_NULL:
                    return Field.apply(logicalOperatorToAndOr(fieldObject.getLogicalOperator()), fieldObject.getFieldName(),
                        comparatorOperatorToOperator(fieldObject.getComparatorOperator()));
                default:
                    // 如果参数值为空则忽略改条件
                    return null;
            }
        } else {
            Class<? extends Object> clazz = obj.getClass();
            if (Iterable.class.isAssignableFrom(clazz)) {
                List<Object> list = Lists.newArrayList((Iterable<?>) obj);
                if (ICollections.hasElements(list)) {
                    return Field.apply(logicalOperatorToAndOr(fieldObject.getLogicalOperator()),
                        fieldObject.getFieldName(),
                        comparatorOperatorToOperator(fieldObject.getComparatorOperator()), list.toArray());
                }
            } else if (clazz.isArray()) {
                Object[] arrValues = (Object[]) obj;
                if (arrValues.length > 0) {
                    return Field.apply(logicalOperatorToAndOr(fieldObject.getLogicalOperator()),
                        fieldObject.getFieldName(),
                        comparatorOperatorToOperator(fieldObject.getComparatorOperator()), arrValues);
                }
            } else {
                return Field.apply(logicalOperatorToAndOr(fieldObject.getLogicalOperator()), fieldObject.getFieldName(),
                    comparatorOperatorToOperator(fieldObject.getComparatorOperator()), new Object[]{obj});
            }
        }
        return null;
    }

    public static Sort sortObjectToSort(SortObject sortObject) {
        OrderDirection orderDirection = sortObject.getOrderDirection();
        switch (orderDirection) {
            case DESC:
                return new Sort(sortObject.getField(), OrderType.DESC);
            default:
                return new Sort(sortObject.getField(), OrderType.ASC);
        }
    }

    public static AndOr logicalOperatorToAndOr(LogicalOperator logicalOperator) {
        switch (logicalOperator) {
            case OR:
                return AndOr.OR;
            default:
                return AndOr.AND;
        }
    }

    public static Operator comparatorOperatorToOperator(ComparatorOperator comparatorOperator) {
        switch (comparatorOperator) {
            case EQUAL:
                return Operator.EQUAL;
            case NOT_EQUAL:
                return Operator.NOT_EQUAL;
            case LIKE:
                return Operator.LIKE;
            case NOT_LIKE:
                return Operator.NOT_LIKE;
            case BETWEEN:
                return Operator.BETWEEN;
            case NOT_BETWEEN:
                return Operator.NOT_BETWEEN;
            case GREATER_THAN:
                return Operator.GREATER_THAN;
            case GREATER_THAN_OR_EQUAL:
                return Operator.GREATER_THAN_OR_EQUAL;
            case LESS_THAN:
                return Operator.LESS_THAN;
            case LESS_THAN_OR_EQUAL:
                return Operator.LESS_THAN_OR_EQUAL;
            case NULL:
                return Operator.IS_NULL;
            case NOT_NULL:
                return Operator.NOT_NULL;
            case IN:
                return Operator.IN;
            case NOT_IN:
                return Operator.NOT_IN;
            default:
                return Operator.EQUAL;
        }
    }

}
