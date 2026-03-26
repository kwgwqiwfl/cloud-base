package com.ring.welkin.common.utils;

import com.google.common.collect.ImmutableMap;
import com.xkzhangsan.time.formatter.DateTimeFormatterUtil;
import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author cloud
 * @date 2023年5月8日 上午11:34:55
 */
public class DateUtils {
    public static final Map<Integer, Character> charMap = new ImmutableMap.Builder<Integer, Character>().put(1, 'y').put(2, 'M').put(3, 'd').put(4, 'H')
            .put(5, 'm').put(6, 's').build();

    public static final Pattern p = Pattern.compile("^(\\d+)\\D*(\\d*)\\D*(\\d*)\\D*(\\d*)\\D*(\\d*)\\D*(\\d*)");

    /**
     * 解析字符串为日期，兼容尽可能多的时间格式
     *
     * @param dateStr 日期时间字符串
     * @return 解析成Date类型
     * @throws ParseException
     */
    public static Date parse(String dateStr) throws ParseException {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        dateStr = dateStr.trim().replaceAll("[a-zA-Z]", " ");
        if (Pattern.matches("^[-+]?\\d{13}$", dateStr)) {// 支持13位时间戳
            return new Date(Long.parseLong(dateStr));
        }
        Matcher m = p.matcher(dateStr);
        StringBuilder sb = new StringBuilder(dateStr);
        // 从被匹配的字符串中，充index = 0的下表开始查找能够匹配pattern的子字符串。m.matches()的意思是尝试将整个区域与模式匹配，不一样。
        if (m.find(0)) {
            int count = m.groupCount();
            for (int i = 1; i <= count; i++) {
                for (Map.Entry<Integer, Character> entry : charMap.entrySet()) {
                    if (entry.getKey() == i) {
                        sb.replace(m.start(i), m.end(i), replaceEachChar(m.group(i), entry.getValue()));
                    }
                }
            }
        } else {
            throw new ParseException("Wrong date format:" + dateStr, 0);
        }
        String format = sb.toString();
        SimpleDateFormat sf = new SimpleDateFormat(format);
        try {
            return sf.parse(dateStr);
        } catch (ParseException e) {
            throw new ParseException("Parse date string error:" + dateStr, 0);
        }
    }

    /**
     * 将指定字符串的所有字符替换成指定字符，跳过空白字符
     *
     * @param s 被替换字符串
     * @param c 字符
     * @return 新字符串
     */
    private static String replaceEachChar(String s, Character c) {
        StringBuilder sb = new StringBuilder("");
        for (Character c1 : s.toCharArray()) {
            if (c1 != ' ') {
                sb.append(String.valueOf(c));
            }
        }
        return sb.toString();
    }

    /**
     * 获取当前日期所在季度的开始日期和结束日期 季度一年四季， 第一季度：1月-3月， 第二季度：4月-6月， 第三季度：7月-9月， 第四季度：10月-12月
     *
     * @param isFirst true表示查询本季度开始日期 false表示查询本季度结束日期
     * @return
     */
    public static LocalDate getStartOrEndDayOfQuarter(LocalDate today, Boolean isFirst) {
        LocalDate resDate = LocalDate.now();
        if (today == null) {
            today = resDate;
        }
        Month month = today.getMonth();
        Month firstMonthOfQuarter = month.firstMonthOfQuarter();
        Month endMonthOfQuarter = Month.of(firstMonthOfQuarter.getValue() + 2);
        if (isFirst) {
            resDate = LocalDate.of(today.getYear(), firstMonthOfQuarter, 1);
        } else {
            resDate = LocalDate.of(today.getYear(), endMonthOfQuarter, endMonthOfQuarter.length(today.isLeapYear()));
        }
        return resDate;
    }

    public static String format(Date date, String format) {
        return asLocalDateTime(date).format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(LocalDate localDate, String format) {
        return localDate.format(DateTimeFormatter.ofPattern(format));
    }

    public static String format(LocalDateTime localDateTime, String format) {
        return localDateTime.format(DateTimeFormatter.ofPattern(format));
    }

    public static Date asDate(LocalDate localDate) {
        return Date.from(localDate.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date asDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static LocalDate asLocalDate(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    public static LocalDateTime asLocalDateTime(Date date) {
        return Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public static Duration between(LocalDateTime startTime, LocalDateTime endTime) {
        return Duration.between(startTime, endTime);
    }

    public static long betweenDays(LocalDateTime startTime, LocalDateTime endTime) {
        return between(startTime, endTime).toDays();
    }

    public static long betweenHours(LocalDateTime startTime, LocalDateTime endTime) {
        return between(startTime, endTime).toHours();
    }

    public static long betweenMinutes(LocalDateTime startTime, LocalDateTime endTime) {
        return between(startTime, endTime).toMinutes();
    }

    public static long betweenNanos(LocalDateTime startTime, LocalDateTime endTime) {
        return between(startTime, endTime).toNanos();
    }

    public static long betweenSeconds(LocalDateTime startTime, LocalDateTime endTime) {
        return between(startTime, endTime).getSeconds();
    }

    public static Duration between(Date startTime, Date endTime) {
        return between(asLocalDateTime(startTime), asLocalDateTime(endTime));
    }

    public static long betweenDays(Date startTime, Date endTime) {
        return between(startTime, endTime).toDays();
    }

    public static long betweenHours(Date startTime, Date endTime) {
        return between(startTime, endTime).toHours();
    }

    public static long betweenMinutes(Date startTime, Date endTime) {
        return between(startTime, endTime).toMinutes();
    }

    public static long betweenNanos(Date startTime, Date endTime) {
        return between(startTime, endTime).toNanos();
    }

    public static long betweenSeconds(Date startTime, Date endTime) {
        return between(startTime, endTime).getSeconds();
    }

    /**
     * 根据时间 和时间格式 校验是否正确
     *
     * @param length 校验的长度
     * @param sDate  校验的日期
     * @param format 校验的格式
     * @return true or false
     */
    public static boolean isLegalDate(String dateStr, String format) {
        if ((StringUtils.isBlank(dateStr)) || (dateStr.length() != format.length())) {
            return false;
        }
        DateFormat formatter = new SimpleDateFormat(format);
        try {
            Date date = formatter.parse(dateStr);
            return dateStr.equals(formatter.format(date));
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) throws ParseException {
        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime endTime = startTime.plusSeconds(70);
        long betweenSeconds = betweenSeconds(startTime, endTime);
        long betweenNanos = betweenNanos(startTime, endTime);
        long betweenMinutes = betweenMinutes(startTime, endTime);
        System.out.println(betweenSeconds);
        System.out.println(betweenMinutes);
        System.out.println(betweenNanos);
        System.out.println(isLegalDate("2023021518", "yyyy-MM-dd"));
        Date parse = parse("2023021518");
        Date smartParseToDate = DateTimeFormatterUtil.smartParseToDate("2023021518");
        System.out.println(smartParseToDate);
    }
}
