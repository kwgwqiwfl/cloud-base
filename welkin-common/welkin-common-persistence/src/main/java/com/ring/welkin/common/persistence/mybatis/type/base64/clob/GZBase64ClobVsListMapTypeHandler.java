package com.ring.welkin.common.persistence.mybatis.type.base64.clob;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ring.welkin.common.persistence.mybatis.type.base64.GZBase64Parser;
import com.ring.welkin.common.persistence.mybatis.type.clob.AbstractClobTypeHandler;

import java.util.List;
import java.util.Map;

public class GZBase64ClobVsListMapTypeHandler extends AbstractClobTypeHandler<List<Map<?, ?>>>
    implements GZBase64Parser {

    @Override
    public String translate2Str(List<Map<?, ?>> t) {
        return encodeToJson(t);
    }

    @Override
    public List<Map<?, ?>> translate2Bean(String result) {
        return decodeFromJson(result, new TypeReference<List<Map<?, ?>>>() {
        });
    }
}
