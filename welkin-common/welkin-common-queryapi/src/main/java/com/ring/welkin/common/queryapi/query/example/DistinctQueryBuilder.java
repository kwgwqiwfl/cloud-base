package com.ring.welkin.common.queryapi.query.example;

/**
 * 去重查询条件构造器接口
 *
 * @param <B> 构建器
 * @author cloud
 * @date 2019年9月5日 下午3:10:55
 */
public interface DistinctQueryBuilder<B extends DistinctQueryBuilder<B>> {

    /**
     * 是否去重查询，一般无需指定，默认不去重
     *
     * @param distinct 是否去重，默认false
     * @return this builder
     */
    B distinct(boolean distinct);

    /**
     * 清空Distinct条件
     *
     * @return this builder
     */
    B clearDistinct();

}
