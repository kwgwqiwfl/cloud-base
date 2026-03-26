package com.ring.welkin.common.persistence.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Varchar VS Double Array TypeHandler
 *
 * @author cloud
 * @date 2019-05-28 13:53
 */
public class VarcharVsArrayDoubleTypeHandler extends AbstractVarcharTypeHandler<Double[]> {

    @Override
    public String translate2Str(Double[] t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Double[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).map(Double::valueOf).toArray(Double[]::new);
    }
}
