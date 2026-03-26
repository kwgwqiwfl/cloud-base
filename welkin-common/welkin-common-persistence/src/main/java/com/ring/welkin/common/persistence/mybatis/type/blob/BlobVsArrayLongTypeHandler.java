package com.ring.welkin.common.persistence.mybatis.type.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * Blob VS Long Array TypeHandler
 *
 * @author cloud
 * @date 2019-05-29 09:42
 */
public class BlobVsArrayLongTypeHandler extends AbstractBlobTypeHandler<Long[]> {

    @Override
    public String translate2Str(Long[] t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Long[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).map(Long::valueOf).toArray(Long[]::new);
    }
}
