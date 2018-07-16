package com.example.maternalandchildhospital.publics.view.wheel;

import java.util.Calendar;

public class SpecialCalendar {

	private int daysOfMonth = 0; // 某月的天数
	private int dayOfWeek = 0; // 具体某一天是星期几

	// 判断是否为闰年
	public boolean isLeapYear(int year) {
		if (year % 100 == 0 && year % 400 == 0) {
			return true;
		} else if (year % 100 != 0 && year % 4 == 0) {
			return true;
		}
		return false;
	}

	// 得到某月有多少天数
	public int getDaysOfMonth(boolean isLeapyear, int month) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			daysOfMonth = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			daysOfMonth = 30;
			break;
		case 2:
			if (isLeapyear) {
				daysOfMonth = 29;
			} else {
				daysOfMonth = 28;
			}

		}
		return daysOfMonth;
	}

	// 指定某年中的某月的第一天是星期几
	public int getWeekdayOfMonth(int year, int month) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, 1);
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		return dayOfWeek;
	}

	public static String getWeekName(int year, int month, int day) {
		Calendar cal = Calendar.getInstance();
		cal.set(year, month - 1, day);
		int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		String weekName = "";
		switch (dayOfWeek) {
		case 0:
			weekName = "周日";
			break;
		case 1:
			weekName = "周一";
			break;
		case 2:
			weekName = "周二";
			break;
		case 3:
			weekName = "周三";
			break;
		case 4:
			weekName = "周四";
			break;
		case 5:
			weekName = "周五";
			break;
		case 6:
			weekName = "周六";
			break;
		}
		return weekName;
	}

}
