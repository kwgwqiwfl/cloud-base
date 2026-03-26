package com.ring.welkin.common.persistence.mybatis.type;

import com.ring.welkin.common.persistence.enums.Timeunit;
import org.apache.ibatis.type.EnumTypeHandler;

public class TimeunitEnumTypeHandler extends EnumTypeHandler<Timeunit> {
    public TimeunitEnumTypeHandler() {
        super(Timeunit.class);
    }
}
