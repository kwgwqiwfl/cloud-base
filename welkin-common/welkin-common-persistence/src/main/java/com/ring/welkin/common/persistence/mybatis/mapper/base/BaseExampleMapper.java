package com.ring.welkin.common.persistence.mybatis.mapper.base;

import com.ring.welkin.common.persistence.mybatis.mapper.agg.AggMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.example.ExampleMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.example.SelectByExampleRowBoundsMapper;
import com.ring.welkin.common.persistence.mybatis.mapper.example.SelectOneColumnByExampleMapper;

public interface BaseExampleMapper<T> extends SelectByExampleRowBoundsMapper<T>, ExampleMapper<T>,
        SelectOneColumnByExampleMapper<T>, AggMapper {
}
