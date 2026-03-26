package com.ring.welkin.common.persistence.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Varchar VS Float Array TypeHandler
 *
 * @author cloud
 * @date 2019-05-28 13:53
 */
public class VarcharVsArrayFloatTypeHandler extends AbstractVarcharTypeHandler<Float[]> {

    @Override
    public String translate2Str(Float[] t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Float[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).map(Float::valueOf).toArray(Float[]::new);
    }
}
