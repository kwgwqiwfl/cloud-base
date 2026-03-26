package com.ring.welkin.common.queryapi.query;

/**
 * 查询条件构造器接口
 *
 * @param <B>
 * @author cloud
 * @date 2019年9月5日 下午3:10:55
 */
public interface TableQueryBuilder<B extends TableQueryBuilder<B>> {

    /**
     * 设置表名
     *
     * @return this builder
     */
    B table(String table);

    /**
     * 清空表名
     *
     * @return this builder
     */
    B clearTable();
}
