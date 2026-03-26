package com.ring.welkin.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class EL {

	public static Date date() {
		return new java.util.Date();
	}

	public static String format(Date date, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static String env(String key) {
		String v = System.getenv(key);
		return v == null ? System.getProperty(key) : v;
	}

	public static String curdate(String format) {
		return format(date(), format);
	}

	public static String yesterday(String format) {
		return LocalDateTime.now().minusDays(1).format(DateTimeFormatter.ofPattern(format));
	}

	public static String today() {
		return curdate("yyyyMMdd");
	}

	public static String todayformat(String format) {
		return curdate(format);
	}

	public static String uuid() {
		return UUID.randomUUID().toString();
	}

	public static String yyyyMMdd() {
		return todayformat("yyyyMMdd");
	}

	public static String yyyyMMddHHmm() {
		return todayformat("yyyyMMddHHmm");
	}

	public static String lastNWeekdays(String prefix, String dateFormat, String sep, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -(calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek()) - n);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<String> list = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			calendar.add(Calendar.DAY_OF_YEAR, 1);
			list.add(prefix + sdf.format(calendar.getTime()));
		}
		return String.join(sep, list);
	}

	public static String last7Weekdays(String prefix, String dateFormat, String sep) {
		return lastNWeekdays(prefix, dateFormat, sep, 7);
	}

	public static String last5Weekdays(String prefix, String dateFormat, String sep) {
		return lastNWeekdays(prefix, dateFormat, sep, 5);
	}

	public static String lastNWeekdayHours(String prefix, String dateFormat, String sep, int n) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, -(calendar.get(Calendar.DAY_OF_WEEK) - calendar.getFirstDayOfWeek()) - n);
		calendar.add(Calendar.DAY_OF_YEAR, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<String> list = new ArrayList<>();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < 24; j++) {
				list.add(prefix + sdf.format(calendar.getTime()));
				calendar.add(Calendar.HOUR_OF_DAY, 1);
			}
		}
		return String.join(sep, list);
	}

	public static String last7WeekdayHours(String prefix, String dateFormat, String sep) {
		return lastNWeekdayHours(prefix, dateFormat, sep, 7);
	}

	public static String last5WeekdayHours(String prefix, String dateFormat, String sep) {
		return lastNWeekdayHours(prefix, dateFormat, sep, 5);
	}

	public static String lastMonthOfYesterdayHours(String prefix, String dateFormat, String sep) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		List<String> list = new ArrayList<>();
		int minDayOfMonth = calendar.getActualMinimum(Calendar.DAY_OF_MONTH);
		int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
		calendar.add(Calendar.DAY_OF_MONTH, minDayOfMonth - dayOfMonth);
		for (int i = minDayOfMonth; i <= dayOfMonth; i++) {
			for (int j = 0; j < 24; j++) {
				list.add(prefix + sdf.format(calendar.getTime()));
				calendar.add(Calendar.HOUR_OF_DAY, 1);
			}
		}
		return String.join(sep, list);
	}

	public static String lastWeekFirstday(String dateFormat) {
		return LocalDateTime.now().minusWeeks(1).with(DayOfWeek.MONDAY).format(DateTimeFormatter.ofPattern(dateFormat));
	}

	public static String beforeOneHour(String dateFormat) {
		return LocalDateTime.now().minusHours(1).format(DateTimeFormatter.ofPattern(dateFormat));
	}

	public static String yesterdayHours(String prefix, String dateFormat, String sep) {
		LocalDateTime start = LocalDateTime.now().minusDays(1).withHour(0);
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(dateFormat);
		List<String> list = new ArrayList<>();
		for (int i = 0; i < 24; i++) {
			list.add(prefix + start.format(pattern));
			start = start.plusHours(1);
		}
		return String.join(sep, list);
	}

	public static String year(int offset) {
		return LocalDate.now().getYear() + offset + "";
	}

	public static String month(int offset) {
		LocalDate now = LocalDate.now().plusMonths(offset);
		int str_year = now.getYear();
		int month = now.getMonth().getValue() + 1;
		String str_month = month + "";
		if (month < 10) {
			str_month = "0" + month;
		}
		return str_year + "-" + str_month;
	}

	public static String today_offset(String time_type, int offset, String f) throws ParseException {
		Date d = null;
		switch (time_type) {
			case "Y":
				d = new SimpleDateFormat("yyyy").parse(year(offset));
				break;
			case "M":
				d = new SimpleDateFormat("yyyy-MM").parse(month(offset));
				break;
			case "D":
				d = new Date(date().getTime() + 3600 * 1000 * 24 * offset);
				break;
			case "H":
				d = new Date(date().getTime() + 3600 * 1000 * offset);
				break;
			case "m":
				d = new Date(date().getTime() + 60 * 1000 * offset);
				break;
			default:
				d = new Date(date().getTime());
		}
		return format(d, f);
	}

	public static String today_before_offset(String prefix, String dateFormat, String sep, int offset) {
		LocalDateTime start = LocalDateTime.now().minusDays(offset).withHour(0);
		List<String> list = new ArrayList<>();
		DateTimeFormatter pattern = DateTimeFormatter.ofPattern(dateFormat);
		for (int i = 0; i < offset; i++) {
			for (int j = 0; j < 24; j++) {
				list.add(prefix + start.format(pattern));
				start = start.plusHours(1);
			}
		}
		return String.join(sep, list);
	}

	public static void main(String[] args) {
		String lastMonthOfYesterdayHours = lastMonthOfYesterdayHours("PRE_", "YYYYMMdd HH", ";");
		System.out.println(lastMonthOfYesterdayHours);
	}
}
