package com.ring.welkin.common.persistence.mybatis.mapper.agg;

import com.ring.welkin.common.persistence.mybatis.mapper.example.Example;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;

import java.util.List;
import java.util.Map;

/**
 * 通用聚合查询接口,特殊方法
 */
@tk.mybatis.mapper.annotation.RegisterMapper
public interface AggMapper {

    /**
     * 根据example和aggregateCondition进行聚合查询 分组不支持having条件过滤， 如需要建议使用xml文件
     *
     * @param example
     * @param agg     可以设置聚合查询的属性和分组属性
     * @return 返回聚合查询属性和分组属性的值
     */
    @SelectProvider(type = AggProvider.class, method = "dynamicSQL")
    List<Map<String, Object>> selectAggByExample(@Param("example") Example example, @Param("agg") AggCondition agg);

}
