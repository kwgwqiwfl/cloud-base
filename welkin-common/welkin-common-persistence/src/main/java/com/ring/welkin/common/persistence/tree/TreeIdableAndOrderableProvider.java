package com.ring.welkin.common.persistence.tree;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 树数据查询和排序查询sql提供器
 *
 * @author cloud
 * @date 2019-05-28 15:05
 */
public class TreeIdableAndOrderableProvider extends MapperTemplate {

    public TreeIdableAndOrderableProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    public String selectByParentId(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        // 将返回值修改为实体类型
        setResultType(ms, entityClass);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.selectAllColumns(entityClass));
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>");
        sql.append("parent_id = #{parentId,jdbcType=BIGINT}");
        sql.append("</where>");
        return sql.toString();
    }

    public String selectMaxOrderByParentId(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("select max(ord) ");
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>");
        sql.append("parent_id = #{parentId,jdbcType=BIGINT}");
        sql.append("</where>");
        return sql.toString();
    }

    public String selectMaxOrderOfRoots(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append("select max(ord) ");
        sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
        sql.append("<where>");
        sql.append("parent_id = 0 or parent_id is null");
        sql.append("</where>");
        return sql.toString();
    }

}
