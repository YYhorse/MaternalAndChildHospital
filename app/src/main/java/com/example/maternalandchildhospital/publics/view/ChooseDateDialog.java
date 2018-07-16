package com.example.maternalandchildhospital.publics.view;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.interfaces.UpdateUi;
import com.example.maternalandchildhospital.publics.util.Utils;
import com.example.maternalandchildhospital.publics.view.wheel.DateWheelAdapter;
import com.example.maternalandchildhospital.publics.view.wheel.OnWheelChangedListener;
import com.example.maternalandchildhospital.publics.view.wheel.OnWheelScrollListener;
import com.example.maternalandchildhospital.publics.view.wheel.WheelView;

public class ChooseDateDialog extends Dialog implements OnClickListener, OnWheelChangedListener, OnWheelScrollListener {
	private Context context;
	private UpdateUi uu;

	private TextView tvTitle;
	private WheelView yearWheelView;

	private WheelView monthWheelView;

	private WheelView dayWheelView;

	private ImageView ivOK;

	private ImageView ivClose;

	private Calendar cal;

	private String setDate = ""; // yyyy年MM月dd日

	private String title = "";

	int nowYear = 0;
	int nowMonth = 0;
	int nowDay = 0;

	/**
	 * type 0 预产期 1末经期推断 为0 boundYear 为预产期最大值 最小值为 当天 为1 boundYear 为末经期最小值 最大值为
	 * 当天
	 */
	private int type;
	/** 边界 年 */
	private int boundYear = 0;
	/** 边界 月 */
	private int boundMonth = 0;
	/** 边界 日 */
	private int boundDay = 0;

	/**
	 * @param type
	 *            类型 0 预产期 1末经期推断
	 * @param context
	 * @param title
	 * @param setDate
	 *            初始日期
	 * @param uu
	 */
	public ChooseDateDialog(int type, Context context, String title, String setDate, UpdateUi uu) {
		super(context, R.style.MyDialog);
		this.context = context;
		this.uu = uu;
		this.setDate = setDate;
		this.title = title;
		this.type = type;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.layout_choose_date_dialog);
		tvTitle = (TextView) findViewById(R.id.tv_title_date_dialog);
		tvTitle.setText(title);
		yearWheelView = (WheelView) findViewById(R.id.wv_year_date_dialog);
		monthWheelView = (WheelView) findViewById(R.id.wv_month_date_dialog);
		dayWheelView = (WheelView) findViewById(R.id.wv_day_date_dialog);
		yearWheelView.setViewAdapter(new DateWheelAdapter(getContext(), 0));
		monthWheelView.setViewAdapter(new DateWheelAdapter(getContext(), 1));
		dayWheelView.setViewAdapter(new DateWheelAdapter(getContext(), true));
		cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);

		if (Utils.strNullMeans(setDate)) {
			nowYear = cal.get(Calendar.YEAR);
			nowMonth = cal.get(Calendar.MONTH);
			nowDay = cal.get(Calendar.DAY_OF_MONTH) - 1;
		} else {
			nowYear = Integer.parseInt(setDate.substring(0, 4)) - year;
			nowMonth = Integer.parseInt(setDate.substring(5, 7)) - 1;
			nowDay = Integer.parseInt(setDate.substring(8, 10)) - 1;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

		try {
			Date data = sdf.parse(nowYear + "" + ((nowMonth+1) < 10 ? "0" + (nowMonth+1) : (nowMonth+1)) + "" + ((nowDay+1) < 10 ? "0" + (nowDay+1) : (nowDay+1)));
			Calendar now = Calendar.getInstance();
			now.setTime(data);
			if (type == 0) { // 预产期
				now.set(Calendar.DATE, now.get(Calendar.DATE) + 279);
			} else if (type == 1) {// 末经期
				now.set(Calendar.DATE, now.get(Calendar.DATE) - 279);
			}

			boundYear = now.get(Calendar.YEAR);
			boundMonth = now.get(Calendar.MONTH);
			boundDay = now.get(Calendar.DAY_OF_MONTH) - 1;

			Utils.Log("boundYear = " + boundYear + "|boundMonth = " + boundMonth + "|boundDay = " + boundDay);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// 年份要减去初始年份，才能显示当前年份
		yearWheelView.setCurrentItem(nowYear - 1970, false);
		yearWheelView.setWheelBackground(context.getResources().getColor(R.color.transparent));
		yearWheelView.setCyclic(true);
		yearWheelView.addChangingListener(this);
		yearWheelView.addScrollingListener(this);
		monthWheelView.setCurrentItem(nowMonth, false);
		monthWheelView.setCyclic(true);
		monthWheelView.addChangingListener(this);
		monthWheelView.addScrollingListener(this);
		dayWheelView.setCurrentItem(nowDay, false);
		dayWheelView.setCyclic(true);
		dayWheelView.addScrollingListener(this);

		ivOK = (ImageView) findViewById(R.id.iv_ok_date_dialog);
		ivOK.setOnClickListener(this);

		ivClose = (ImageView) findViewById(R.id.iv_close_date_dialog);
		ivClose.setOnClickListener(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			Utils.closeChooseDateDialog();
		}
		return false;
	}

	@Override
	public void onClick(View v) {

		if (!Utils.isFastDoubleClick()) {

			return;
		}
		if (v == ivOK) {
			DateWheelAdapter yearAdapter = (DateWheelAdapter) yearWheelView.getViewAdapter();
			DateWheelAdapter monthAdapter = (DateWheelAdapter) monthWheelView.getViewAdapter();
			DateWheelAdapter dayAdapter = (DateWheelAdapter) dayWheelView.getViewAdapter();
			String day = String.valueOf(dayAdapter.getItemText(dayWheelView.getCurrentItem()));
			String year = String.valueOf(yearAdapter.getItemText(yearWheelView.getCurrentItem()));
			String month = String.valueOf(monthAdapter.getItemText(monthWheelView.getCurrentItem())).trim();

			if (uu != null) {
				String tempDate = year + "-" + month.replace("月", "") + "-" + day;
				//不在这里处理
//				if (type == 1) {
//					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//					try {
//						Date data = sdf.parse(tempDate);
//						Calendar calendar = Calendar.getInstance();
//						calendar.setTime(data);
//						calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) + 279);
//
//						tempDate = sdf.format(calendar.getTime());
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}

				uu.updateUI(tempDate);
			}

			this.dismiss();

		} else if (v == ivClose) {
			Utils.closeChooseDateDialog();
		}

	}

	@Override
	public void onChanged(WheelView wheel, int oldValue, int newValue) {
		if (wheel == yearWheelView) {
			DateWheelAdapter yearAdapter = (DateWheelAdapter) yearWheelView.getViewAdapter();
			DateWheelAdapter monthAdapter = (DateWheelAdapter) monthWheelView.getViewAdapter();
			DateWheelAdapter dayAdapter = (DateWheelAdapter) dayWheelView.getViewAdapter();
			dayAdapter.refreshDateInfo(yearAdapter.getItemData(newValue), monthAdapter.getItemData(monthWheelView.getCurrentItem()));
		} else if (wheel == monthWheelView) {
			DateWheelAdapter yearAdapter = (DateWheelAdapter) yearWheelView.getViewAdapter();
			DateWheelAdapter monthAdapter = (DateWheelAdapter) monthWheelView.getViewAdapter();
			DateWheelAdapter dayAdapter = (DateWheelAdapter) dayWheelView.getViewAdapter();
			dayAdapter.refreshDateInfo(yearAdapter.getItemData(yearWheelView.getCurrentItem()), monthAdapter.getItemData(newValue));
		}
	}

	@Override
	public void onScrollingStarted(WheelView wheel) {

	}

	// hxc add 增加范围限制 推算 从本日起往前 279天 ， 预产期 从本日起往后 279天 在这范围之外的时间段不可选
	@Override
	public void onScrollingFinished(WheelView wheel) {

		if (wheel == yearWheelView) { // 年
			DateWheelAdapter yearAdapter = (DateWheelAdapter) yearWheelView.getViewAdapter();
			if (boundYear > 0) { // 判断边界是否有值
				if (type == 0) { // 预产期
					if (yearAdapter.getItemData(yearWheelView.getCurrentItem()) <= nowYear) { // 低于当前年份，重置
						yearWheelView.setCurrentItem(nowYear - 1970, true);
						monthWheelView.setCurrentItem(nowMonth, true);
						dayWheelView.setCurrentItem(nowDay, true);
					} else if (yearAdapter.getItemData(yearWheelView.getCurrentItem()) >= boundYear) { // 高于边界年份，重置
						yearWheelView.setCurrentItem(boundYear - 1970, true);
						monthWheelView.setCurrentItem(0, true);
						dayWheelView.setCurrentItem(0, true);

					}
				} else if (type == 1) {// 推算
					if (yearAdapter.getItemData(yearWheelView.getCurrentItem()) <= boundYear) {// 高于当前年份，重置
						yearWheelView.setCurrentItem(boundYear - 1970, true);
						monthWheelView.setCurrentItem(boundMonth, true);
						dayWheelView.setCurrentItem(boundDay, true);
					} else if (yearAdapter.getItemData(yearWheelView.getCurrentItem()) >= nowYear) {// 低于边界年份，重置
						yearWheelView.setCurrentItem(nowYear - 1970, true);
						monthWheelView.setCurrentItem(0, true);
						dayWheelView.setCurrentItem(0, true);
					}
				}
			}

		} else if (wheel == monthWheelView) {// 月
			DateWheelAdapter yearAdapter = (DateWheelAdapter) yearWheelView.getViewAdapter();
			DateWheelAdapter monthAdapter = (DateWheelAdapter) monthWheelView.getViewAdapter();

			if (boundMonth > 0) {
				if (type == 0) {
					if (boundYear == nowYear) {// 边界年份和当前年份在同一年
						if (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1 >= boundMonth) {// 高于边界月份
																											// 重置
							monthWheelView.setCurrentItem(boundMonth, true);
							dayWheelView.setCurrentItem(0, true);
						} else if (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1 <= nowMonth) {// 低于当前月份
																												// 重置
							monthWheelView.setCurrentItem(nowMonth, true);
							dayWheelView.setCurrentItem(nowDay, true);
						}
					} else if (boundYear == yearAdapter.getItemData(yearWheelView.getCurrentItem())) {
						if (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1 >= boundMonth) {
							monthWheelView.setCurrentItem(boundMonth, true);
							dayWheelView.setCurrentItem(0, true);
						}
					} else {
						if (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1 <= nowMonth) {
							monthWheelView.setCurrentItem(nowMonth, true);
							dayWheelView.setCurrentItem(nowDay, true);
						}
					}

				} else if (type == 1) {
					if (boundYear == nowYear) {
						if (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1 <= boundMonth) {
							monthWheelView.setCurrentItem(boundMonth, true);
							dayWheelView.setCurrentItem(boundDay, true);
						} else if (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1 >= nowMonth) {
							monthWheelView.setCurrentItem(nowMonth, true);
							dayWheelView.setCurrentItem(0, true);
						}
					} else if (boundYear == yearAdapter.getItemData(yearWheelView.getCurrentItem())) {
						if (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1 <= boundMonth) {
							monthWheelView.setCurrentItem(boundMonth, true);
							dayWheelView.setCurrentItem(boundDay, true);
						}
					} else {
						if (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1 >= nowMonth) {
							monthWheelView.setCurrentItem(nowMonth, true);
							dayWheelView.setCurrentItem(0, true);
						}
					}

				}
			}

		} else if (wheel == dayWheelView) { // 日
			DateWheelAdapter yearAdapter = (DateWheelAdapter) yearWheelView.getViewAdapter();
			DateWheelAdapter monthAdapter = (DateWheelAdapter) monthWheelView.getViewAdapter();
			DateWheelAdapter dayAdapter = (DateWheelAdapter) dayWheelView.getViewAdapter();

			if (boundDay > 0) {
				if (type == 0) {
					// 不能高于边界值
					if (boundYear == yearAdapter.getItemData(yearWheelView.getCurrentItem()) && boundMonth == (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1)) {
						if (dayAdapter.getItemData(dayWheelView.getCurrentItem()) - 1 > boundDay) {
							dayWheelView.setCurrentItem(boundDay, true);
						}
						// 不能低于当前值
					} else if (nowYear == yearAdapter.getItemData(yearWheelView.getCurrentItem()) && nowMonth == (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1)) {
						if (dayAdapter.getItemData(dayWheelView.getCurrentItem()) - 1 < nowDay) {
							dayWheelView.setCurrentItem(nowDay, true);
						}
					}

				} else if (type == 1) {
					// 不能低于边界值
					if (boundYear == yearAdapter.getItemData(yearWheelView.getCurrentItem()) && boundMonth == (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1)) {
						if (dayAdapter.getItemData(dayWheelView.getCurrentItem()) - 1 < boundDay) {
							dayWheelView.setCurrentItem(boundDay, true);
						}
						// 不能高于当前值
					} else if (nowYear == yearAdapter.getItemData(yearWheelView.getCurrentItem()) && nowMonth == (monthAdapter.getItemData(monthWheelView.getCurrentItem()) - 1)) {
						if (dayAdapter.getItemData(dayWheelView.getCurrentItem()) - 1 > nowDay) {
							dayWheelView.setCurrentItem(nowDay, true);
						}
					}

				}
			}

		}

	}
}
