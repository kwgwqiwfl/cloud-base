package com.ring.welkin.common.persistence.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Varchar VS Integer Array TypeHandler
 *
 * @author cloud
 * @date 2019-05-28 13:53
 */
public class VarcharVsArrayIntegerTypeHandler extends AbstractVarcharTypeHandler<Integer[]> {

    @Override
    public String translate2Str(Integer[] t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Integer[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).map(Integer::valueOf).toArray(Integer[]::new);
    }
}
