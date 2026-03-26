package com.ring.welkin.common.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * sql片段生成器
 *
 * @author cloud
 * @date 2022年7月7日 下午2:58:56
 */
@SuppressWarnings("deprecation")
public class SqlSpliceHelper {

    // 操作符
    public static final String OP_OR = "or";
    public static final String OP_AND = "and";

    @Getter
    @AllArgsConstructor
    public enum CompareOperator {
        GT(">"), // 大于
        LT("<"), // 小于
        GE(">="), // 大于等于
        LE("<="), // 小于等于
        EQ("="), // 等于
        NE("!="); // 不等于

        private final String value;
    }

    // 参数名称常量
    public static final String OPERATOR = "operator";
    public static final String VALUE = "value";
    public static final String MIN_VALUE = "minValue";
    public static final String MAX_VALUE = "maxValue";
    public static final String YELLOW_VALUE = "yellowValue";
    public static final String ORANGE_VALUE = "orangeValue";
    public static final String RED_VALUE = "redValue";

    /**
     * 括号包裹条件
     *
     * @param condition sql条件列表
     * @return 包裹括号的条件sql片段
     */
    public static String wrapBracket(String condition) {
        return StringUtils.join(CharUtils.BRACKET_START, condition, CharUtils.BRACKET_END);
    }

    /**
     * 单引号包裹条件
     *
     * @param condition sql条件列表
     * @return 包裹单引号的条件sql片段
     */
    public static String wrapSingleQuote(Object value) {
        return StringUtils.join(CharUtils.SINGLE_QUOTE, value, CharUtils.SINGLE_QUOTE);
    }

    /**
     * 双引号包裹条件
     *
     * @param condition sql条件列表
     * @return 包裹双引号的条件sql片段
     */
    public static String wrapDoubleQuote(Object value) {
        return StringUtils.join(CharUtils.DOUBLE_QUOTES, value, CharUtils.DOUBLE_QUOTES);
    }

    /**
     * 多条件与
     *
     * @param conditions sql条件列表
     * @return 多条件与sql片段
     */
    public static String and(Object... conditions) {
        return StringUtils.joinWith(" and ", conditions);
    }

    /**
     * 多条件或
     *
     * @param conditions sql条件列表
     * @return 多条件或sql片段
     */
    public static String or(Object... conditions) {
        return StringUtils.joinWith(" or ", conditions);
    }

    /**
     * 取非sql片段
     *
     * @param condition sql条件
     * @return 取非sql片段
     */
    public static String not(String condition) {
        return StringUtils.join("!(", condition, ")");
    }

    /**
     * distinct子句
     *
     * @param columnName 字段名称
     * @return distinct子句片段
     */
    public static String distinct(String columnName) {
        return StringUtils.join("distinct ", columnName);
    }

    /**
     * 为null
     *
     * @param columnName 字段名称
     * @return 为nullsql片段
     */
    public static String isNull(String columnName) {
        return StringUtils.join(columnName, " is null");
    }

    /**
     * 值为empty
     *
     * @param columnName 字段名称
     * @return 值为empty的sql片段
     */
    public static String isEmpty(String columnName) {
        return StringUtils.join("IsEmpty(", columnName, ")");
    }

    /**
     * 不为null
     *
     * @param columnName 字段名称
     * @return 不为nullsql片段
     */
    public static String isNotNull(String columnName) {
        return StringUtils.join(columnName, " is not null");
    }

    /**
     * in 条件sql片段
     *
     * @param columnName 字段名称
     * @param values     枚举列表
     * @return in条件sql片段
     */
    public static String in(String columnName, List<?> values) {
        Assert.notEmpty(values, "Parameter 'values' must be not empty!");
        return StringUtils.join(columnName, " in (",
                values.stream()
                        .filter(d -> d != null && StringUtils.isNotEmpty(d.toString()))
                        .map(Object::toString)
                        .collect(Collectors.joining("','", "'", "'")),
                ")");
    }

    /**
     * 正则匹配
     *
     * @param columnName 字段名称
     * @param regexp     正则表达式
     * @return 正则sql片段
     */
    public static String regexp(String columnName, String regexp) {
        return StringUtils.join(columnName, " regexp '", StringEscapeUtils.escapeJava(regexp), "'");
    }

    /**
     * 等于条件匹配sql片段
     *
     * @param columnName 字段名称
     * @param value      值
     * @return 等于条件匹配sql片段
     */
    public static String eq(String columnName, Object value) {
        Assert.notNull(value, "Parameter 'value' must be not null!");
        if (value instanceof String) {
            return StringUtils.join(columnName, " = '", value, "'");
        }
        return StringUtils.join(columnName, " = ", value);
    }

    /**
     * 不等于条件匹配sql片段
     *
     * @param columnName 字段名称
     * @param value      值
     * @return 不等于条件匹配sql片段
     */
    public static String ne(String columnName, Object value) {
        return StringUtils.join("!(", eq(columnName, value), ")");
    }

    /**
     * 大于条件匹配sql片段
     *
     * @param columnName 字段名称
     * @param value      值
     * @return 大于条件匹配sql片段
     */
    public static String gt(String columnName, Object value) {
        return StringUtils.joinWith(CharUtils.SPACE, columnName, CompareOperator.GT.value, value);
    }

    /**
     * 大于等于条件匹配sql片段
     *
     * @param columnName 字段名称
     * @param value      值
     * @return 大于等于条件匹配sql片段
     */
    public static String gte(String columnName, Object value) {
        return StringUtils.joinWith(CharUtils.SPACE, columnName, CompareOperator.GE.value, value);
    }

    /**
     * 小于条件匹配sql片段
     *
     * @param columnName 字段名称
     * @param value      值
     * @return 小于条件匹配sql片段
     */
    public static String lt(String columnName, Object value) {
        return StringUtils.joinWith(CharUtils.SPACE, columnName, CompareOperator.LT.value, value);
    }

    /**
     * 小于等于条件匹配sql片段
     *
     * @param columnName 字段名称
     * @param value      值
     * @return 小于等于条件匹配sql片段
     */
    public static String lte(String columnName, Object value) {
        return StringUtils.joinWith(CharUtils.SPACE, columnName, CompareOperator.LE.value, value);
    }

    /**
     * 值域内条件匹配sql片段
     *
     * @param columnName 字段名称
     * @param value1     值1
     * @param value2     值2
     * @return 值域内条件匹配sql片段
     */
    public static String bt(String columnName, Object value1, Object value2) {
        return StringUtils.joinWith(CharUtils.SPACE, columnName, "between", value1, "and", value2);
    }

    /**
     * 非范围内条件匹配sql片段
     *
     * @param columnName 字段名称
     * @param value1     值1
     * @param value2     值2
     * @return 非范围内条件匹配sql片段
     */
    public static String nbt(String columnName, Object value1, Object value2) {
        return not(StringUtils.joinWith(CharUtils.SPACE, columnName, "between", value1, "and", value2));
    }

    /**
     * count函数sql片段
     *
     * @param columnName 字段名称
     * @return count函数sql片段
     */
    public static String count(String columnName) {
        return StringUtils.join("count(", columnName, ")");
    }

    /**
     * avg函数sql片段
     *
     * @param columnName 字段名称
     * @return avg函数sql片段
     */
    public static String avg(String columnName) {
        return StringUtils.join("avg(", columnName, ")");
    }

    /**
     * sum函数sql片段
     *
     * @param columnName 字段名称
     * @return sum函数sql片段
     */
    public static String sum(String columnName) {
        return StringUtils.join("sum(", columnName, ")");
    }

    /**
     * max函数sql片段
     *
     * @param columnName 字段名称
     * @return max函数sql片段
     */
    public static String max(String columnName) {
        return StringUtils.join("max(", columnName, ")");
    }

    /**
     * min函数sql片段
     *
     * @param columnName 字段名称
     * @return min函数sql片段
     */
    public static String min(String columnName) {
        return StringUtils.join("min(", columnName, ")");
    }

    /**
     * length函数sql片段
     *
     * @param columnName 字段名称
     * @return length函数sql片段
     */
    public static String length(String columnName) {
        return StringUtils.join("length(", columnName, ")");
    }

    /**
     * cast函数sql片段
     *
     * @param columnName 字段名称
     * @param type       cast to 类型
     * @return cast函数sql片段
     */
    public static String cast(String columnName, String type) {
        return StringUtils.join("cast(", columnName, " as ", type, ")");
    }

    /**
     * cast as string sql片段
     *
     * @param columnName 字段名称
     * @return cast as string sql片段
     */
    public static String castToString(String columnName) {
        return cast(columnName, "string");
    }

    /**
     * cast as bigint sql片段
     *
     * @param columnName 字段名称
     * @return cast as bigint sql片段
     */
    public static String castToBingint(String columnName) {
        return cast(columnName, "bigint");
    }

    /**
     * coalesce函数sql片段
     *
     * @param columnName 字段名称
     * @return coalesce函数sql片段
     */
    public static String coalesceZero(String columnName) {
        return coalesce(columnName, 0);
    }

    /**
     * coalesce函数sql片段
     *
     * @param columnName  字段名称
     * @param defaultVals 默认值列表
     * @return coalesce函数sql片段
     */
    public static String coalesce(String columnName, Object... defaultVals) {
        return StringUtils.join("coalesce(",
                StringUtils.join(columnName, CharUtils.COMMA, StringUtils.joinWith(CharUtils.COMMA, defaultVals)), ")");
    }

    /**
     * 逆向规则计数sql片段
     *
     * @param reverseConditionSql 逆向计数条件sql
     * @return 逆向规则计数sql片段
     */
    public static String caseWhen(String condition, Object thenValue, Object elseValue) {
        return StringUtils.join("case when ", condition, " then ", thenValue, " else ", elseValue, " end");
    }

    /**
     * 逆向规则计数sql片段
     *
     * @param condition 逆向计数条件sql
     * @return 逆向规则计数sql片段
     */
    public static String reverseRuleCountSql(String condition) {
        return coalesceZero(StringUtils.join("sum(", caseWhen(condition, 1, 0), ")"));
    }

    /**
      * 正向规则计数sql片段
     *
     * @param condition 正向计数条件sql
     * @return 正向规则计数sql片段
     */
    public static String positiveRuleCountSql(String condition) {
        return coalesceZero(StringUtils.join("sum(", caseWhen(condition, 0, 1), ")"));
    }

    /**
     * 比较条件拼接
     *
     * @param columnName 列名称
     * @param operator   操作类型
     * @param value      值
     * @return 生成的比较表达式
     */
    public static String compare(String columnName, String operator, Object value) {
        return StringUtils.joinWith(CharUtils.SPACE, columnName, operator, value);
    }

    /**
     * 值域比较条件拼接
     *
     * @param columnName 列名称
     * @param operator   操作类型
     * @param minValue   最小值
     * @param maxValue   最大值
     * @return 生成的比较表达式
     */
    public static String dataRange(String columnName, String operator, Object minValue, Object maxValue) {
        Assert.isTrue(StringUtils.isNotEmpty(operator), "Parameter " + OPERATOR + " cannot be empty!");
        Assert.isTrue(ObjectUtils.isNotEmpty(maxValue), "Parameter " + MAX_VALUE + " cannot be empty!");
        Assert.isTrue(ObjectUtils.isNotEmpty(minValue), "Parameter " + MIN_VALUE + " cannot be empty!");
        switch (operator) {
            case "<~<":
                return wrapBracket(and(lt(columnName, maxValue), gt(columnName, minValue)));
            case "<=~<":
                return wrapBracket(and(lt(columnName, maxValue), gte(columnName, minValue)));
            case "<=~<=":
                return wrapBracket(and(lte(columnName, maxValue), gte(columnName, minValue)));
            case "<~<=":
                return wrapBracket(and(lte(columnName, maxValue), gt(columnName, minValue)));
            default:
                throw new IllegalArgumentException("Not supported operator " + operator);
        }
    }
}
