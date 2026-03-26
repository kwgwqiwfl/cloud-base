package com.ring.welkin.common.persistence.mybatis.mapper.select;

import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

public class ExistsWithRecordProvider extends MapperTemplate {

	public ExistsWithRecordProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
		super(mapperClass, mapperHelper);
	}

	public String existsWithRecord(MappedStatement ms) {
		Class<?> entityClass = getEntityClass(ms);
		StringBuilder sql = new StringBuilder("SELECT 1");
		sql.append(SqlHelper.fromTable(entityClass, tableName(entityClass)));
		sql.append(SqlHelper.whereAllIfColumns(entityClass, isNotEmpty()));
		sql.append(SqlHelper.orderByDefault(entityClass));
		return sql.toString();
	}

}
