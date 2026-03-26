package com.ring.welkin.common.persistence.mybatis.mapper.delete;

import com.ring.welkin.common.persistence.mybatis.mapper.MySqlHelper;
import org.apache.ibatis.mapping.MappedStatement;
import tk.mybatis.mapper.mapperhelper.MapperHelper;
import tk.mybatis.mapper.mapperhelper.MapperTemplate;
import tk.mybatis.mapper.mapperhelper.SqlHelper;

/**
 * 批量删除
 *
 * @author cloud
 * @date 2019-05-28 15:03
 */
public class DeleteListByPrimaryKeyProvider extends MapperTemplate {

    public DeleteListByPrimaryKeyProvider(Class<?> mapperClass, MapperHelper mapperHelper) {
        super(mapperClass, mapperHelper);
    }

    /**
     * 通过实体字段为条件批量删除，根据参数中每个实体的字段值进行匹配删除，相当于视图属性值建立联合条件匹配删除. <br>
     *
     * @param ms 映射语句
     * @return 批量删除动态sql
     */
    public String deleteList(MappedStatement ms) {
        final Class<?> entityClass = getEntityClass(ms);
        StringBuilder sql = new StringBuilder();
        sql.append(SqlHelper.deleteFromTable(entityClass, tableName(entityClass)));
        sql.append(MySqlHelper.whereAllColumnsByList(entityClass, "record", true));
        return sql.toString();
    }
}
