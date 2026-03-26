package com.ring.welkin.common.persistence.mybatis.type.blob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Blob VS String Set TypeHandler
 *
 * @author cloud
 * @date 2019-05-29 09:43
 */
public class BlobVsStringSetTypeHandler extends AbstractBlobTypeHandler<Set<String>> {

    @Override
    public String translate2Str(Set<String> t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public Set<String> translate2Bean(String result) {
        return Arrays.stream(result.split(DEFAULT_SEPARATOR)).collect(Collectors.toSet());
    }
}
