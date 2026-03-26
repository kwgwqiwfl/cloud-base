package com.ring.welkin.common.persistence.enums;

import com.google.common.collect.Lists;
import com.ring.welkin.common.utils.ICollections;
import com.xkzhangsan.time.converter.DateTimeConverterUtil;
import com.xkzhangsan.time.formatter.DateTimeFormatterUtil;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor
public enum Timeunit {
    YEAR("Y", "y", "年", "yyyy"), //
    MONTH("M", "m", "月", "yyyyMM"), //
    WEEK("W", "w", "周", "yyyyww"), //
    DAY("D", "d", "天", "yyyyMMdd"), //
    HOUR("H", "h", "小时", "yyyyMMddHH"), //
    MINUTE("MIN", "min", "分钟", "yyyyMMddHHmm"), //
    SECOND("S", "s", "秒", "yyyyMMddHHmmss");

    private final String value;
    private final String abbr;
    private final String name;
    private final String dateformat;

    public String getNName(int n) {
        return StringUtils.join(n, name);
    }

    public String getNAbbr(int n) {
        return StringUtils.join(n, abbr);
    }

    /**
     * 根据时间类型获取cron表达式，默认按天
     *
     * @return cron表达式
     */
    public String getCorn() {
        switch (this) {
            case MINUTE:
                return "0 * * * * ?";// 每分钟第10秒执行一次
            case HOUR:
                return "0 0 * * * ?";// 每小时第0分钟执行一次
            case DAY:
                return "0 0 0 * * ?";// 每天早上一点
            // return "0 0/1 * * * ?";//调试使用
            case WEEK:
                return "0 0 0 ? * MON";// 每周一早上1点执行
            case MONTH:
                return "0 0 0 1 * ?";// 每月1号1点执行
            default:
                throw new IllegalArgumentException("Not supported time unit " + this.name());
        }
    }

    /**
     * 提前1周期时间
     *
     * @param dateTime 参考周期时间
     * @return 提前1周期的时间
     */
    public LocalDateTime getPreviousCycle(LocalDateTime dateTime) {
        return getPreviousNCycle(dateTime, 1);
    }

    public LocalDateTime getPreviousCycle(Date dateTime) {
        return getPreviousNCycle(dateTime, 1);
    }

    public LocalDateTime getNextCycle(LocalDateTime dateTime) {
        return getNextNCycle(dateTime, 1);
    }

    public LocalDateTime getNextCycle(Date dateTime) {
        return getNextNCycle(dateTime, 1);
    }

    /**
     * 格式化时间
     *
     * @param dateTime 业务时间
     * @return 格式化之后的时间字符串
     */
    public String dateFormat(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern(dateformat));
    }

    /**
     * 格式化时间
     *
     * @param dateTime 业务时间
     * @return 格式化之后的时间字符串
     */
    public String dateFormat(Date dateTime) {
        return dateFormat(LocalDateTime.ofInstant(dateTime.toInstant(), ZoneId.systemDefault()));
    }

    public LocalDateTime toLocalDateTime(String bizdate) {
        return LocalDateTime.ofInstant(toDate(bizdate).toInstant(), ZoneId.systemDefault());
    }

    public Date toDate(String bizdate) {
        String dateformat2 = getDateformat();
        if (dateformat2 != null && !dateformat2.isEmpty()) {
            return DateTimeFormatterUtil.parseToDate(bizdate, dateformat2);
        }
        return DateTimeFormatterUtil.smartParseToDate(bizdate);
    }

    /**
     * 提前N周期时间
     *
     * @param dateTime 参考周期时间
     * @param n        周期数
     * @return 提前N周期的时间
     */
    public LocalDateTime getPreviousNCycle(Date dateTime, Integer n) {
        return getPreviousNCycle(DateTimeConverterUtil.toLocalDateTime(dateTime), n);
    }

    /**
     * 提前N周期时间
     *
     * @param dateTime 参考周期时间
     * @param n        周期数
     * @return 提前N周期的时间
     */
    public LocalDateTime getPreviousNCycle(LocalDateTime dateTime, Integer n) {
        switch (this) {
            case SECOND:
                return dateTime.minusSeconds(n);
            case MINUTE:
                return dateTime.minusMinutes(n);
            case HOUR:
                return dateTime.minusHours(n);
            case DAY:
                return dateTime.minusDays(n);
            case WEEK:
                return dateTime.minusWeeks(n);
            case MONTH:
                return dateTime.minusMonths(n);
            case YEAR:
                return dateTime.minusYears(n);
            default:
                throw new IllegalArgumentException("Not supported time unit " + this.name());
        }
    }

    /**
     * 后推N周期时间
     *
     * @param dateTime 参考周期时间
     * @param n        周期数
     * @return 后推N周期的时间
     */
    public LocalDateTime getNextNCycle(LocalDateTime dateTime, Integer n) {
        return getPreviousNCycle(dateTime, -n);
    }

    /**
     * 后推N周期时间
     *
     * @param dateTime 参考周期时间
     * @param n        周期数
     * @return 后推N周期的时间
     */
    public LocalDateTime getNextNCycle(Date dateTime, Integer n) {
        return getNextNCycle(DateTimeConverterUtil.toLocalDateTime(dateTime), n);
    }

    public LocalDateTime getStartTime(Date bizdate) {
        return getStartTime(DateTimeConverterUtil.toLocalDateTime(bizdate));
    }

    public LocalDateTime getStartTime(LocalDateTime bizdate) {
        switch (this) {
            case YEAR:
                return bizdate.with(TemporalAdjusters.firstDayOfYear()).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case MONTH:
                return bizdate.with(TemporalAdjusters.firstDayOfMonth()).withHour(0).withMinute(0).withSecond(0)
                        .withNano(0);
            case WEEK:
                return bizdate.with(DayOfWeek.MONDAY).withHour(0).withMinute(0).withSecond(0).withNano(0);
            case DAY:
                return bizdate.withHour(0).withMinute(0).withSecond(0).withNano(0);
            case HOUR:
                return bizdate.withMinute(0).withSecond(0).withNano(0);
            case MINUTE:
                return bizdate.withSecond(0).withNano(0);
            case SECOND:
                return bizdate.withNano(0);
            default:
                throw new IllegalArgumentException("Not supported time unit " + this.name());
        }
    }

    public LocalDateTime getEndTime(Date bizdate) {
        return getEndTime(DateTimeConverterUtil.toLocalDateTime(bizdate));
    }

    public LocalDateTime getEndTime(LocalDateTime bizdate) {
        switch (this) {
            case YEAR:
                return bizdate.with(TemporalAdjusters.lastDayOfYear()).withHour(23).withMinute(59).withSecond(59)
                        .withNano(999999999);
            // case QUARTER:
            // return DateUtils.getStartOrEndDayOfQuarter(bizdate.toLocalDate(),
            // false).atStartOfDay().withHour(23)
            // .withMinute(59).withSecond(59).withNano(999999999);
            case MONTH:
                return bizdate.with(TemporalAdjusters.lastDayOfMonth()).withHour(23).withMinute(59).withSecond(59)
                        .withNano(999999999);
            case WEEK:
                return bizdate.with(DayOfWeek.SUNDAY).withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case DAY:
                return bizdate.withHour(23).withMinute(59).withSecond(59).withNano(999999999);
            case HOUR:
                return bizdate.withMinute(59).withSecond(59).withNano(999999999);
            case MINUTE:
                return bizdate.withSecond(59).withNano(999999999);
            case SECOND:
                return bizdate.withNano(999999999);
            default:
                throw new IllegalArgumentException("Not supported time unit " + this.name());
        }
    }

    public List<LocalDateTime> getSubUnitList(LocalDateTime bizdate, Timeunit incrUnit) {
        return getSubUnitList(getStartTime(bizdate), getEndTime(bizdate), incrUnit);
    }

    public List<LocalDateTime> getSubUnitList(Date startTime, Date endTime, Timeunit incrUnit) {
        return getSubUnitList(DateTimeConverterUtil.toLocalDateTime(startTime), DateTimeConverterUtil.toLocalDateTime(endTime), incrUnit);
    }

    /**
     * 大区间时间分割成小区间时间点，获取区间时间节点列表，如：获取天中的整小时时间列表，获取月份的整天的时间列表
     *
     * @param bizdate  业务时间
     * @param incrUnit 时间分割粒度
     * @return 分割后的之间列表
     */
    public List<LocalDateTime> getSubUnitList(LocalDateTime startTime, LocalDateTime endTime, Timeunit incrUnit) {
        if (this.ordinal() > incrUnit.ordinal()) {
            throw new IllegalArgumentException("Increment timeunit '" + incrUnit.name()
                    + "' must be less than or equal to the current unit " + this.name() + " !");
        }
        List<LocalDateTime> dateTimes = Lists.newArrayList();
        LocalDateTime tempDateTime = startTime;
        switch (incrUnit) {
            case YEAR:
                while (tempDateTime.isBefore(endTime)) {
                    dateTimes.add(tempDateTime);
                    tempDateTime = tempDateTime.plusYears(1);
                }
                return dateTimes;
            case MONTH:
                while (tempDateTime.isBefore(endTime)) {
                    dateTimes.add(tempDateTime);
                    tempDateTime = tempDateTime.plusMonths(1);
                }
                return dateTimes;
            case WEEK:
                while (tempDateTime.isBefore(endTime)) {
                    dateTimes.add(tempDateTime);
                    tempDateTime = tempDateTime.plusWeeks(1);
                }
                return dateTimes;
            case DAY:
                while (tempDateTime.isBefore(endTime)) {
                    dateTimes.add(tempDateTime);
                    tempDateTime = tempDateTime.plusDays(1);
                }
                return dateTimes;
            case HOUR:
                while (tempDateTime.isBefore(endTime)) {
                    dateTimes.add(tempDateTime);
                    tempDateTime = tempDateTime.plusHours(1);
                }
                return dateTimes;
            case MINUTE:
                while (tempDateTime.isBefore(endTime)) {
                    dateTimes.add(tempDateTime);
                    tempDateTime = tempDateTime.plusMinutes(1);
                }
                return dateTimes;
            case SECOND:
                while (tempDateTime.isBefore(endTime)) {
                    dateTimes.add(tempDateTime);
                    tempDateTime = tempDateTime.plusSeconds(1);
                }
                return dateTimes;
            default:
                throw new IllegalArgumentException("Not supported time unit " + this.name());
        }
    }

    /**
     * 大区间时间分割成小区间时间点的格式化列表
     *
     * @param bizdate  业务时间
     * @param incrUnit 时间分割粒度
     * @return 分割后的之间列表
     */
    public List<String> getSubUnitFormatList(LocalDateTime bizdate, Timeunit incrUnit, String dateFormat) {
        List<LocalDateTime> subUnitList = getSubUnitList(bizdate, incrUnit);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(dateFormat);
        if (ICollections.hasElements(subUnitList)) {
            return subUnitList.stream().map(t -> t.format(dateTimeFormatter)).collect(Collectors.toList());
        }
        return Lists.newArrayList();
    }

    public static String[] shortValues() {
        return Arrays.stream(values()).map(t -> t.getValue()).toArray(String[]::new);
    }

    public static Timeunit of(String value) {
        for (Timeunit timeunit : values()) {
            if (timeunit.getValue().equals(value)) {
                return timeunit;
            }
        }
        throw new IllegalArgumentException(
                "Unsupported type value [" + value + "] which must be in " + Arrays.toString(shortValues()));
    }

    public static List<TimeunitEntity> entityList() {
        return Stream.of(Timeunit.values()).map(t -> new TimeunitEntity(t)).collect(Collectors.toList());
    }

    @Setter
    @Getter
    public static class TimeunitEntity {

        @ApiModelProperty(value = "主键")
        private final String id;
        @ApiModelProperty(value = "大写简写")
        private final String value;
        @ApiModelProperty(value = "小写简写")
        private final String abbr;
        @ApiModelProperty(value = "中文名称")
        private final String name;

        public TimeunitEntity(Timeunit timeunit) {
            this.id = timeunit.name();
            this.value = timeunit.getValue();
            this.abbr = timeunit.getAbbr();
            this.name = timeunit.getName();
        }
    }

    public static void main(String[] args) {
        Timeunit hour = Timeunit.HOUR;
        LocalDateTime previousCycle = hour.getPreviousCycle(LocalDateTime.now());
        List<String> subUnitFormatList = hour.getSubUnitFormatList(previousCycle, Timeunit.MINUTE, "HHmm");
        System.out.println(subUnitFormatList);

        Timeunit day = Timeunit.DAY;
        previousCycle = day.getPreviousCycle(LocalDateTime.now());
        subUnitFormatList = day.getSubUnitFormatList(previousCycle, Timeunit.HOUR, "HH");
        System.out.println(subUnitFormatList);
        subUnitFormatList = day.getSubUnitFormatList(previousCycle, Timeunit.MINUTE, "HHmm");
        System.out.println(subUnitFormatList);

        Timeunit month = Timeunit.MONTH;
        previousCycle = month.getPreviousCycle(LocalDateTime.now());
        subUnitFormatList = month.getSubUnitFormatList(previousCycle, Timeunit.DAY, "MM/dd");
        System.out.println(subUnitFormatList);
        subUnitFormatList = month.getSubUnitFormatList(previousCycle, Timeunit.WEEK, "ww");
        System.out.println(subUnitFormatList);
        subUnitFormatList = month.getSubUnitFormatList(previousCycle, Timeunit.MONTH, "MM");
        System.out.println(subUnitFormatList);
        subUnitFormatList = month.getSubUnitFormatList(previousCycle, Timeunit.YEAR, "ww");
        System.out.println(subUnitFormatList);
    }
}
