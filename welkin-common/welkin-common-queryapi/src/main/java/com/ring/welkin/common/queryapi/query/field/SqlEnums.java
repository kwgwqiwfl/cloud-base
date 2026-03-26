package com.ring.welkin.common.queryapi.query.field;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class SqlEnums {
    // 运算符
    @Getter
    @AllArgsConstructor
    public enum Operator {
        EQUAL("等于"), //
        NOT_EQUAL("不等于"), //
        LIKE("匹配"), //
        NOT_LIKE("不匹配"), //
        BETWEEN("介于之间"), //
        NOT_BETWEEN("不介于之间"), //
        GREATER_THAN("大于"), //
        GREATER_THAN_OR_EQUAL("大于等于"), //
        LESS_THAN("小于"), //
        LESS_THAN_OR_EQUAL("小于等于"), //
        IS_NULL("为空"), //
        NOT_NULL("不为空"), //
        IN("在范围内"), //
        NOT_IN("在范围内");

        private final String value;
    }

    // 逻辑关系
    public enum AndOr {
        AND, OR
    }

    // 排序类型
    public enum OrderType {
        ASC, DESC
    }
}