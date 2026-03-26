package com.ring.welkin.common.queryapi.query.record;

/**
 * 字段过滤查询条件构造器接口
 *
 * @param <B>
 * @author cloud
 * @date 2019年9月5日 下午3:10:55
 */
public interface PropertiesQueryBuilder<B extends PropertiesQueryBuilder<B>>
    extends SelectPropertiesQueryBuilder<B>, ExcludePropertiesQueryBuilder<B> {
}
