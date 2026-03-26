package com.ring.welkin.common.persistence.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

@Getter
@AllArgsConstructor
public enum TimePeriod {

    Y("Y", 1, "年"), //
    M("M", 1, "月"), //
    W("W", 1, "周"), //
    D("D", 1, "天"), //
    H("H", 1, "小时"), //
    H_2("H", 2, "小时"), //
    H_3("H", 3, "小时"), //
    H_4("H", 4, "小时"), //
    H_6("H", 6, "小时"), //
    H_8("H", 8, "小时"), //
    H_12("H", 12, "小时"), //
    MIN("MIN", 1, "分钟"), //
    MIN_5("MIN", 5, "分钟"), //
    MIN_10("MIN", 10, "分钟"), //
    MIN_15("MIN", 15, "分钟"), //
    MIN_20("MIN", 20, "分钟"), //
    MIN_30("MIN", 30, "分钟"), //
    S_1("S", 1, "秒"), //
    S_5("S", 5, "秒"), //
    S_10("S", 10, "秒"), //
    S_15("S", 15, "秒"), //
    S_20("S", 20, "秒"), //
    S_30("S", 30, "秒")//
    ;

    private final String format;
    private final int step;
    private final String label;

    public String floor(String timeField, String originalFormat) {
        return StringUtils.join("floorTime(", timeField, StringUtils.join("'", format, "'"), step,
                StringUtils.isEmpty(originalFormat) ? "''" : StringUtils.join("'", originalFormat, "'"), ")");
    }

    public String floor(String timeField) {
        return floor(timeField, "''");
    }
}
