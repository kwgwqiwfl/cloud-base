package com.ring.welkin.common.persistence.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Varchar VS Boolean Array TypeHandler
 *
 * @author cloud
 * @date 2019-05-28 13:53
 */
public class VarcharVsArrayBooleanTypeHandler extends AbstractVarcharTypeHandler<Boolean[]> {

    @Override
    public String translate2Str(Boolean[] t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Boolean[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).map(Boolean::valueOf).toArray(Boolean[]::new);
    }
}
