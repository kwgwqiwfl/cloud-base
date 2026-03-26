package com.ring.welkin.common.persistence.mybatis.enhance;

import com.ring.welkin.common.persistence.mybatis.annotation.IgnoreByExampleQuery;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.UnknownTypeHandler;
import tk.mybatis.mapper.annotation.ColumnType;
import tk.mybatis.mapper.code.Style;
import tk.mybatis.mapper.entity.Config;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.entity.EntityTable;
import tk.mybatis.mapper.mapperhelper.resolve.DefaultEntityResolve;
import tk.mybatis.mapper.util.SqlReservedWords;
import tk.mybatis.mapper.util.StringUtil;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.text.MessageFormat;

@Slf4j
public class TkEntityResolve extends DefaultEntityResolve {

    /**
     * 处理字段
     *
     * @param entityTable
     * @param field
     * @param config
     * @param style
     */

    @Override
    protected void processField(EntityTable entityTable, EntityField field, Config config, Style style) {
        // 排除字段
        if (field.isAnnotationPresent(Transient.class)) {
            return;
        }
        // Id
        TkEntityColumn entityColumn = new TkEntityColumn(entityTable);
        // 是否使用 {xx, javaType=xxx}
        entityColumn.setUseJavaType(config.isUseJavaType());
        // 记录 field 信息，方便后续扩展使用
        entityColumn.setEntityField(field);
        if (field.isAnnotationPresent(Id.class)) {
            entityColumn.setId(true);
        }
        // Column
        String columnName = null;
        if (field.isAnnotationPresent(Column.class)) {
            Column column = field.getAnnotation(Column.class);
            columnName = column.name();
            entityColumn.setUpdatable(column.updatable());
            entityColumn.setInsertable(column.insertable());
        }
        // ColumnType
        if (field.isAnnotationPresent(ColumnType.class)) {
            ColumnType columnType = field.getAnnotation(ColumnType.class);
            // 是否为 blob 字段
            entityColumn.setBlob(columnType.isBlob());
            // column可以起到别名的作用
            if (StringUtil.isEmpty(columnName) && StringUtil.isNotEmpty(columnType.column())) {
                columnName = columnType.column();
            }
            if (columnType.jdbcType() != JdbcType.UNDEFINED) {
                entityColumn.setJdbcType(columnType.jdbcType());
            }
            if (columnType.typeHandler() != UnknownTypeHandler.class) {
                entityColumn.setTypeHandler(columnType.typeHandler());
            }
        }
        // 列名
        if (StringUtil.isEmpty(columnName)) {
            columnName = StringUtil.convertByStyle(field.getName(), style);
        }
        // 自动处理关键字
        if (StringUtil.isNotEmpty(config.getWrapKeyword()) && SqlReservedWords.containsWord(columnName)) {
            columnName = MessageFormat.format(config.getWrapKeyword(), columnName);
        }
        entityColumn.setProperty(field.getName());
        entityColumn.setColumn(columnName);
        entityColumn.setJavaType(field.getJavaType());
        if (field.getJavaType().isPrimitive()) {
            log.warn("通用 Mapper 警告信息: <[" + entityColumn
                + "]> 使用了基本类型，基本类型在动态 SQL 中由于存在默认值，因此任何时候都不等于 null，建议修改基本类型为对应的包装类型!");
        }
        // OrderBy
        processOrderBy(entityTable, field, entityColumn);
        // 处理主键策略
        processKeyGenerator(entityTable, field, entityColumn);

        // 处理ColumnQueryIgnore 注解
        if (field.isAnnotationPresent(IgnoreByExampleQuery.class)) {
            IgnoreByExampleQuery ignore = field.getAnnotation(IgnoreByExampleQuery.class);
            processColumnQueryIgnore(entityTable, entityColumn, ignore);
        }

        entityTable.getEntityClassColumns().add(entityColumn);
        if (entityColumn.isId()) {
            entityTable.getEntityClassPKColumns().add(entityColumn);
        }
    }

    /**
     * 处理 ColumnQueryIgnore 注解
     *
     * @param entityTable
     * @param entityColumn
     * @param ignore
     */
    protected void processColumnQueryIgnore(EntityTable entityTable, TkEntityColumn entityColumn,
                                            IgnoreByExampleQuery ignore) {
        if (entityColumn.isId() && ignore != null) {
            log.warn(entityColumn.getProperty() + " - 该字段@IgnoreByExampleQuery注解不能设置在主键上，查询将忽略该注解。");
            return;
        }
        entityColumn.setIgnoreByQuery(true);
    }

}
