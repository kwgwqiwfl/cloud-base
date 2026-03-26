package com.ring.welkin.common.persistence.mybatis.type.clob;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Clob VS Long List TypeHandler
 *
 * @author cloud
 * @date 2019-05-29 09:48
 */
public class ClobVsLongListTypeHandler extends AbstractClobTypeHandler<List<Long>> {

    @Override
    public String translate2Str(List<Long> t) {
        return StringUtils.join(t, DEFAULT_SEPARATOR);
    }

    @Override
    public List<Long> translate2Bean(String result) {
        if (StringUtils.isNotBlank(result)) {
            return Arrays.stream(result.split(DEFAULT_SEPARATOR)).filter(t -> StringUtils.isNotBlank(t))
                    .map(t -> Long.valueOf(t)).collect(Collectors.toList());
        }
        return null;
    }
}
