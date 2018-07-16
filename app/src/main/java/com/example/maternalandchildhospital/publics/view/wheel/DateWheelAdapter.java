package com.example.maternalandchildhospital.publics.view.wheel;

import java.util.Calendar;

import android.content.Context;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.publics.util.Utils;

public class DateWheelAdapter extends AbstractWheelTextAdapter {

	private int minYear = 1970;
	private int maxYear = 2050;

	private int[] dates;
	private String title = "";
	private boolean isShowDay;
	private int nowYear;
	private int nowMonth;
	private int nowDay;
	private int selectYear;
	private int selectMonth;

	public DateWheelAdapter(Context context, int type) {
		super(context);
		title = getTitle(type);
		Calendar cal = Calendar.getInstance();
		nowYear = cal.get(Calendar.YEAR);
		dates = new int[maxYear - minYear];
		initData(type);
		// 设置字体颜色
		setTextColor(context.getResources().getColor(R.color.color_white));
	}

	public DateWheelAdapter(Context context, boolean isShowDay) {
		super(context);
		this.isShowDay = isShowDay;
		Calendar cal = Calendar.getInstance();
		nowYear = cal.get(Calendar.YEAR);
		nowMonth = cal.get(Calendar.MONTH) + 1;
		nowDay = cal.get(Calendar.DAY_OF_MONTH) - 1;
		this.selectYear = nowYear;
		this.selectMonth = nowMonth;
		SpecialCalendar specialCalendar = new SpecialCalendar();
		int days = specialCalendar.getDaysOfMonth(specialCalendar.isLeapYear(nowYear), nowMonth);
		if (nowDay == days) {
			if (this.selectMonth == 12) {
				this.selectMonth = 1;
				this.selectYear++;
			} else {
				this.selectMonth++;
			}
		}
		initData(2);
		// 设置字体颜色
		setTextColor(context.getResources().getColor(R.color.color_white));
	}

	public int getItemData(int index) {
		return dates[index];
	}

	protected void configureTextView(TextView view) {
		super.configureTextView(view);
		view.setPadding(Utils.dpToPx(view.getContext(), 2), 0, Utils.dpToPx(view.getContext(), 2), 0);
	}

	@Override
	public int getItemsCount() {
		return dates == null ? 0 : dates.length;
	}

	public void refreshDateInfo(int selectYear, int selectMonth) {
//		Utils.Log("selectYear = " + selectYear);
//		Utils.Log("selectMonth = " + selectMonth);
//
//		Utils.Log("nowYear = " + nowYear);
//		Utils.Log("nowMonth = " + nowMonth);

		this.selectYear = selectYear;
		if (nowMonth > selectMonth) {
			this.selectYear = nowYear + 1;
		}
		this.selectMonth = selectMonth;
		initData(2);
		notifyDataChangedEvent();
	}

	@Override
	public CharSequence getItemText(int index) {
		return isShowDay ? showDay(dates[index]) : (dates[index] < 10 ? "0" + dates[index] : dates[index]) + title;
	}

	private String getTitle(int type) {
		switch (type) {
		case 0:
			// return "年";
			return "";
		case 1:
			return "月";
		}
		return "";
	}

	private void initData(int type) {
		switch (type) {
		case 0:// 年
			for (int i = 0; i < maxYear - minYear; i++) {
				dates[i] = i + minYear;
			}
			break;
		case 1:// 月
			dates = getMonthData(12);
			break;
		case 2:
			SpecialCalendar specialCalendar = new SpecialCalendar();
			dates = getTimeDataThree(specialCalendar.getDaysOfMonth(specialCalendar.isLeapYear(selectYear), selectMonth));
			break;
		case 3:
			dates = getTimeData(24);
			break;
		case 4:
			dates = getTimeDataTwo(60);
			break;
		}

	}

	// 去掉周几
	private String showDay(int day) {
		// if (nowYear == selectYear && nowMonth == selectMonth && day ==
		// nowDay) {
		// return "今天";
		// } else {
		// return (day < 10 ? "0" + day : day) + "日" +
		// SpecialCalendar.getWeekName(selectYear, selectMonth, day);
		// }
		return (day < 10 ? "0" + day : day) + "";
	}

	private int[] getTimeDataThree(int size) {
		dates = new int[size];
		for (int i = 0; i < dates.length; i++) {
			dates[i] = i + 1;
		}
		return dates;
	}

	private int[] getTimeData(int size) {
		dates = new int[size];
		for (int i = 0; i < dates.length; i++) {
			dates[i] = i;
			// if (dates[i] == 60 || dates[i] == 24) {
			// dates[i] = 0;
			// }
		}
		return dates;
	}

	private int[] getMonthData(int size) {
		dates = new int[size];
		for (int i = 0; i < dates.length; i++) {
			dates[i] = i + 1;

		}
		return dates;
	}

	private int[] getTimeDataTwo(int size) {
		dates = new int[size];
		for (int i = 0; i < dates.length; i++) {
			// dates[i] = (i + 1) * 10;
			// if (dates[i] == 60) {
			// dates[i] = 0;
			// }
			dates[i] = i;

		}
		return dates;
	}

}
