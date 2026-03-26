package com.ring.welkin.common.persistence.mybatis.type.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

/**
 * 说明： Blob VS Double Array TypeHandler. <br>
 *
 * @author cloud
 * @date 2017年11月10日 下午12:44:32
 */
public class BlobVsArrayDoubleTypeHandler extends AbstractBlobTypeHandler<Double[]> {

    @Override
    public String translate2Str(Double[] t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Double[] translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).map(Double::valueOf).toArray(Double[]::new);
    }

}
