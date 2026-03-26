package com.ring.welkin.common.core.rest;

/**
 * ApiOperation 解析器
 *
 * @author cloud
 * @date 2021年7月30日 下午5:12:30
 */
public interface ApiOperationParser<E extends ApiOperation, T> {

    /**
     * 解析
     *
     * @param t 来源，如：ServerWebExchange
     * @return ApiOperation 对象
     */
    E parse(T t);
}
