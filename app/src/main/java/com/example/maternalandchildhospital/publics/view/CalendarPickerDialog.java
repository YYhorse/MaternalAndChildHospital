package com.example.maternalandchildhospital.publics.view;

import android.app.Activity;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.Calendar;

public class CalendarPickerDialog {
    /******************************************************************************************
     * * 功能说明：日历控件获取UI的ID
     ******************************************************************************************/
    private static boolean todayorderState = false;                                                 //日历控件BUG 点击后运行两次 解决标志位
    public static void TurnoverChooseCalendar(final Activity activity) {
        todayorderState = false;
        Calendar c = Calendar.getInstance();
        //日历控件
        android.app.DatePickerDialog dp = new android.app.DatePickerDialog(activity, new android.app.DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int monthOfYear, int dayOfMonth) {
                int now_year = datePicker.getYear();                                                //年
                int now_month = datePicker.getMonth() + 1;                                          //月-1
                if (todayorderState == false) {
                    todayorderState = true;
                    Toast.makeText(activity,now_year+now_month+"",Toast.LENGTH_LONG).show();
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dp.show();
        //----------------去除日显示------------//
        int SDKVersion;
        try {
            SDKVersion = Integer.valueOf(Build.VERSION.SDK);
        } catch (NumberFormatException e) {
            SDKVersion = 0;
        }
        DatePicker dp2 = findDatePicker((ViewGroup) dp.getWindow().getDecorView());
        if (dp2 != null) {
            if (SDKVersion < 11) {
                ((ViewGroup) dp2.getChildAt(0)).getChildAt(1).setVisibility(View.GONE);
                ((ViewGroup) dp2.getChildAt(0)).getChildAt(2).setVisibility(View.GONE);
            } else if (SDKVersion > 14) {
                View view2 = ((ViewGroup) ((ViewGroup) dp2.getChildAt(0)).getChildAt(0)).getChildAt(2);
                view2.setVisibility(View.GONE);
            }
        }
    }

    /******************************************************************************************
     * * 功能说明：日历控件获取UI的ID
     ******************************************************************************************/
    private static DatePicker findDatePicker(ViewGroup group) {
        if (group != null) {
            for (int i = 0, j = group.getChildCount(); i < j; i++) {
                View child = group.getChildAt(i);
                if (child instanceof DatePicker) {
                    return (DatePicker) child;
                } else if (child instanceof ViewGroup) {
                    DatePicker result = findDatePicker((ViewGroup) child);
                    if (result != null)
                        return result;
                }
            }
        }
        return null;
    }
}