package com.ring.welkin.common.persistence.mybatis.type.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 说明： Blob VS Byte Array TypeHandler. <br>
 *
 * @author cloud
 * @date 2017年11月10日 下午12:44:32
 */
public class BlobVsArrayByteTypeHandler extends AbstractBlobTypeHandler<Byte[]> {

    @Override
    public String translate2Str(Byte[] t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Byte[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).map(Byte::valueOf).toArray(Byte[]::new);
    }
}
