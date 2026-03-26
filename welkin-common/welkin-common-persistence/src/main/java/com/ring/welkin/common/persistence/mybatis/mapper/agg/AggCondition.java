package com.ring.welkin.common.persistence.mybatis.mapper.agg;

import com.google.common.collect.Lists;
import com.ring.welkin.common.utils.Assert;
import com.ring.welkin.common.utils.ICollections;
import com.ring.welkin.common.utils.JsonUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import tk.mybatis.mapper.additional.aggregation.AggregateType;
import tk.mybatis.mapper.entity.EntityColumn;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class AggCondition {
    /**
     * 聚合字段
     */
    private List<AggColumn> aggColumns = new ArrayList<>();

    /**
     * 分组字段
     */
    private List<GroupByColumn> groupBys = new ArrayList<>();

    public static AggCondition builder() {
        return new AggCondition();
    }

    public AggCondition aggColumns(Collection<AggColumn> aggColumns) {
        Assert.notEmpty(aggColumns, "aggColumns can't be empty!");
        this.aggColumns.addAll(aggColumns);
        return this;
    }

    public AggCondition aggColumns(AggColumn... aggColumns) {
        Assert.notEmpty(aggColumns, "aggColumns can't be empty!");
        return aggColumns(Lists.newArrayList(aggColumns));
    }

    public AggCondition aggColumn(AggregateType type, String name, String alias, boolean distinct) {
        return aggColumns(new AggColumn(type, name, alias, distinct));
    }

    public AggCondition aggColumn(AggregateType type, String name, String alias) {
        return aggColumn(type, name, alias, false);
    }

    public AggCondition aggColumn(AggregateType type, String name, boolean distinct) {
        return aggColumn(type, name, null, distinct);
    }

    public AggCondition aggColumn(AggregateType type, String name) {
        return aggColumn(type, name, null, false);
    }

    public AggCondition count(String name, String alias, boolean distinct) {
        return aggColumn(AggregateType.COUNT, name, alias, distinct);
    }

    public AggCondition count(String name, String alias) {
        return count(name, alias, false);
    }

    public AggCondition count(String name) {
        return count(name, null);
    }

    public AggCondition avg(String name, String alias) {
        return aggColumn(AggregateType.AVG, name, alias, false);
    }

    public AggCondition avg(String name) {
        return avg(name, null);
    }

    public AggCondition min(String name, String alias) {
        return aggColumn(AggregateType.MIN, name, alias, false);
    }

    public AggCondition min(String name) {
        return min(name, null);
    }

    public AggCondition max(String name, String alias) {
        return aggColumn(AggregateType.MAX, name, alias, false);
    }

    public AggCondition max(String name) {
        return max(name, null);
    }

    public AggCondition sum(String name, String alias) {
        return aggColumn(AggregateType.SUM, name, alias, false);
    }

    public AggCondition sum(String name) {
        return sum(name, null);
    }

    public AggCondition groupBys(List<GroupByColumn> groupBys) {
        Assert.notEmpty(groupBys, "groupBys can't be empty!");
        this.groupBys.addAll(groupBys);
        return this;
    }

    public AggCondition groupBys(GroupByColumn... groupBys) {
        Assert.notEmpty(groupBys, "groupBys can't be empty!");
        return groupBys(Lists.newArrayList(groupBys));
    }

    public AggCondition groupBy(String name, String alias) {
        return groupBys(new GroupByColumn(name, alias));
    }

    public AggCondition groupBy(String name) {
        return groupBy(name, name);
    }

    public AggCondition groupBys(String... names) {
        for (String name : names) {
            groupBy(name, name);
        }
        return this;
    }

    public String toSelectSql(Map<String, EntityColumn> propertyMap) {
        List<String> selectColumns = Lists.newArrayList();
        if (ICollections.hasElements(groupBys)) {
            selectColumns.addAll(groupBys.stream().map(t -> t.toSelectSql(propertyMap)).collect(Collectors.toList()));
        }
        if (ICollections.hasElements(aggColumns)) {
            selectColumns.addAll(aggColumns.stream().map(t -> t.toSelectSql(propertyMap)).collect(Collectors.toList()));
        }
        return StringUtils.join(selectColumns, ",");
    }

    public String toGroupBySql(Map<String, EntityColumn> propertyMap) {
        if (ICollections.hasElements(groupBys)) {
            return StringUtils.join("group by ",
                    groupBys.stream().map(t -> t.toGroupBySql(propertyMap)).collect(Collectors.joining(",")));
        }
        return "";
    }

    @Data
    public static class AggResultColumn {

        /**
         * 字段名
         */
        protected String name;

        /**
         * 字段别名
         */
        protected String alias;

        /**
         * 从实体对象属性中取出对应的数据库字段名
         *
         * <pre>
         * 例：
         * 	实体中属性名称     userName
         * 	数据库字段名称 user_name
         * 	返回值       user_name
         * </pre>
         *
         * @param propertyMap 实体的字段映射信息
         * @return 数据库字段名称
         */
        public String getColumnName(Map<String, EntityColumn> propertyMap) {
            return propertyMap.get(name).getColumn();
        }
    }

    /**
     * 聚合统计字段
     *
     * @author cloud
     * @date 2023年7月31日 下午3:56:46
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class AggColumn extends AggResultColumn {

        /**
         * 聚合类型
         */
        private AggregateType type;

        /**
         * 是否去重，默认false
         */
        private boolean distinct;

        public AggColumn(AggregateType type, String name, String alias, boolean distinct) {
            Assert.notNull(type, "type can't be null!");
            Assert.isNotEmpty(name, "name can't be empty!");
            this.type = type;
            this.name = name;
            this.alias = StringUtils.isNotBlank(alias) ? alias : StringUtils.join(type.name().toLowerCase(), "_", name);
            this.distinct = distinct;
        }

        public AggColumn(AggregateType type, String name, boolean distinct) {
            this(type, name, null, distinct);
        }

        /**
         * 根据参数生成查询字段的sql
         *
         * <pre>
         * 案例如下：
         * 	1）统计总数： count(1) as countNum
         * 	2）统计总数： count(id) as count_id
         * 	3）去重统计总数： count(distinct name) as count_name
         * 	4）求平均值： avg(age) as avg_age
         * 	5）取最小值： min(age) as min_age
         * 	6）取最大值： max(age) as max_age
         * 	7）取总和：  sum(age) as sum_age
         * </pre>
         *
         * @param propertyMap 实体的字段映射信息
         * @return 返回查询字段的sql片段
         */
        public String toSelectSql(Map<String, EntityColumn> propertyMap) {
            return StringUtils.join(type.name(), "(", distinct ? "distinct " : "", getColumnName(propertyMap), ") as ",
                    alias);
        }

    }

    /**
     * 分组字段
     *
     * @author cloud
     * @date 2023年7月31日 下午3:56:34
     */
    @Data
    @EqualsAndHashCode(callSuper = false)
    public static class GroupByColumn extends AggResultColumn {

        public GroupByColumn(String name, String alias) {
            Assert.isNotEmpty(name, "name can't be empty!");
            this.name = name;
            this.alias = StringUtils.defaultString(alias, name);
        }

        public GroupByColumn(String name) {
            this(name, null);
        }

        /**
         * 根据条件生成分组字段的查询sql片段
         *
         * <pre>
         * 例：
         * 	以年级分组 grade as grade
         * </pre>
         *
         * @param propertyMap 实体的字段映射信息
         * @return 返回查询字段的sql片段
         */
        public String toSelectSql(Map<String, EntityColumn> propertyMap) {
            return StringUtils.join(getColumnName(propertyMap), " as ", alias);
        }

        /**
         * 根据条件分组条件的sql片段
         *
         * <pre>
         * 例：
         * 	以年级分组 grade
         * </pre>
         *
         * @param propertyMap 实体的字段映射信息
         * @return 返回分组条件的sql片段
         */
        public String toGroupBySql(Map<String, EntityColumn> propertyMap) {
            return getColumnName(propertyMap);
        }
    }

    public static void main(String[] args) {
        // @formatter:off
        AggCondition agg = AggCondition.builder()
                .aggColumn(AggregateType.COUNT, "name", "count_name")
                .aggColumn(AggregateType.AVG, "age", "avg_age")
                .aggColumn(AggregateType.MAX, "age", "max_age")
                .aggColumn(AggregateType.MIN, "age", "min_age")
                .aggColumn(AggregateType.SUM, "age", "sum_age")
                .groupBy("grade", "class");
        System.out.println(JsonUtils.toJson(agg, true));


        AggCondition agg1 = AggCondition.builder()
                .count("name", "count_name")
                .avg("age", "avg_age")
                .max("age", "max_age")
                .min("age", "min_age")
                .sum("age", "sum_age")
                .groupBy("grade", "class");
        System.out.println(JsonUtils.toJson(agg1, true));

        AggCondition agg2 = AggCondition.builder()
                .count("name")
                .avg("age")
                .max("age")
                .min("age")
                .sum("age")
                .groupBy("grade", "class");
        System.out.println(JsonUtils.toJson(agg2, true));
        // @formatter:on
    }

}
