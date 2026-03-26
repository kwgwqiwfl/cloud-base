package com.ring.welkin.common.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DecimalUtils {

    /**
     * 按四舍五入保留指定小数位数，位数不够用0补充
     *
     * @param o        格式化前的小数
     * @param newScale 保留小数位数
     * @return 格式化后的小数
     */

    public static String formatDecimalWithZero(Object o, int newScale) {
        return String.format("%." + newScale + "f", o);
    }

    /**
     * 按四舍五入保留指定小数位数，位数不够用0补充
     *
     * @param d        格式化前的小数
     * @param newScale 保留小数位数
     * @return 格式化后的小数
     */
    public static String formatDecimalWithZero(double d, int newScale) {
        String pattern = "0.";
        for (int i = 0; i < newScale; i++) {
            pattern += "0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(d);
    }

    /**
     * 按四舍五入保留指定小数位数，位数不够用0补充
     *
     * @param d        格式化前的小数 String形式
     * @param newScale 保留小数位数
     * @return 格式化后的小数
     */
    public static String formatDecimalWithZero(String d, int newScale) {
        String pattern = "0.";
        for (int i = 0; i < newScale; i++) {
            pattern += "0";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(Double.valueOf(d));
    }

    /**
     * 按四舍五入保留指定小数位数，小数点后仅保留有效位数
     *
     * @param d        格式化前的小数
     * @param newScale 保留小数位数
     * @return 格式化后的小数
     */
    public static String formatDecimal(double d, int newScale) {
        String pattern = "#.";
        for (int i = 0; i < newScale; i++) {
            pattern += "#";
        }
        DecimalFormat df = new DecimalFormat(pattern);
        return df.format(d);
    }

    /**
     * 按四舍五入保留指定小数位数，小数点后仅保留有效位数
     *
     * @param d        格式化前的小数
     * @param newScale 保留小数位数
     * @return 格式化后的小数
     */
    public static String formatDecimal(String d, int newScale) {
        String pattern = "#.";
        for (int i = 0; i < newScale; i++) {
            pattern += "#";
        }
        DecimalFormat df = new DecimalFormat(pattern);

        return df.format(Double.valueOf(d));

    }

    /**
     * 按指定舍入模式保留指定小数位数
     *
     * @param d            格式化前的小数
     * @param newScale     保留小数位数
     * @param roundingMode 舍入模式
     *                     <p>
     *                     (RoundingMode.UP始终进一/DOWN直接舍弃/
     *                     <p>
     *                     CEILING正进负舍/FLOOR正舍负进/
     *                     <p>
     *                     HALF_UP四舍五入/HALF_DOWN五舍六进/
     *                     <p>
     *                     HALF_EVEN银行家舍入法/UNNECESSARY抛出异常)
     * @return 格式化后的小数
     */

    public static double formatDecimal(double d, int newScale, RoundingMode roundingMode) {
        BigDecimal bd = new BigDecimal(d).setScale(newScale, roundingMode);
        return bd.doubleValue();

    }

    /**
     * 按指定舍入模式保留指定小数位数
     *
     * @param d            格式化前的小数
     * @param newScale     保留小数位数
     * @param roundingMode 舍入模式
     *                     <p>
     *                     (RoundingMode.UP始终进一/DOWN直接舍弃/
     *                     <p>
     *                     CEILING正进负舍/FLOOR正舍负进/
     *                     <p>
     *                     HALF_UP四舍五入/HALF_DOWN五舍六进/
     *                     <p>
     *                     HALF_EVEN银行家舍入法/UNNECESSARY抛出异常)
     * @return 格式化后的小数
     */
    public static double formatDecimal(String d, int newScale, RoundingMode roundingMode) {
        BigDecimal bd = new BigDecimal(Double.valueOf(d)).setScale(newScale, roundingMode);
        return bd.doubleValue();
    }
}
