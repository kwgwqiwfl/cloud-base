package com.ring.welkin.common.persistence.mybatis.type.varchar;

public class VarcharVsTimeTypeHandler extends VarcharVsDateTimeTypeHandler {

    private static final String PATTERN = "HH:mm:ss";

    public VarcharVsTimeTypeHandler() {
        super(PATTERN);
    }
}
