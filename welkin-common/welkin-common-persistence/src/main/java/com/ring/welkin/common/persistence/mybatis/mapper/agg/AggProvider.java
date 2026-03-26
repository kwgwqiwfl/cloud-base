package com.ring.welkin.common.persistence.mybatis.mapper.agg;

import com.ring.welkin.common.persistence.mybatis.mapper.example.ExampleSqlHelper;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.EntityHelper;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;
import tk.mybatis.mapper.util.Assert;

public class AggProvider extends MapperTemplate {

    public AggProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public static String aggSelectClause(Class<?> entityClass, AggCondition condition) {
        Assert.notEmpty(condition.getAggColumns(), "aggColumns can't be null or empty");
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        return condition.toSelectSql(entityTable.getPropertyMap());
    }

    public static String aggGroupBy(Class<?> entityClass, AggCondition condition) {
        EntityTable entityTable = EntityHelper.getEntityTable(entityClass);
        return condition.toGroupBySql(entityTable.getPropertyMap());
    }

    /**
     * 根据Example查询总数
     *
     * @param ms
     * @return
     */
    public String selectAggByExample(MappedStatement ms) {
        Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        if (isCheckExampleEntityClass()) {
            sql.append(ExampleSqlHelper.exampleCheck(entityClass));
        }
        sql.append("SELECT ${@com.ring.welkin.common.persistence.mybatis.mapper.agg.AggProvider@aggSelectClause(");
        sql.append("@").append(entityClass.getCanonicalName()).append("@class");
        sql.append(", agg");
        sql.append(")} ");
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append(ExampleSqlHelper.updateByExampleWhereClause());
        sql.append(" ${@com.ring.welkin.common.persistence.mybatis.mapper.agg.AggProvider@aggGroupBy(");
        sql.append("@").append(entityClass.getCanonicalName()).append("@class");
        sql.append(", agg");
        sql.append(")} ");
        sql.append(ExampleSqlHelper.exampleOrderBy("example", entityClass));
        sql.append(ExampleSqlHelper.exampleForUpdate());
        return sql.toString();
    }

}
