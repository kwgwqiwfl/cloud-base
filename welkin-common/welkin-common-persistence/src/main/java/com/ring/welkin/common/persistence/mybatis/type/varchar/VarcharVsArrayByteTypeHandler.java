package com.ring.welkin.common.persistence.mybatis.type.varchar;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Varchar VS Byte Array TypeHandler
 *
 * @author cloud
 * @date 2019-05-28 13:53
 */
public class VarcharVsArrayByteTypeHandler extends AbstractVarcharTypeHandler<Byte[]> {

    @Override
    public String translate2Str(Byte[] t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Byte[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).map(Byte::valueOf).toArray(Byte[]::new);
    }

}
