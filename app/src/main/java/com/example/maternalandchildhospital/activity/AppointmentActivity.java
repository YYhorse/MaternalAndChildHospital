package com.example.maternalandchildhospital.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.publics.util.PopMessageUtil;
import com.example.maternalandchildhospital.publics.util.SwitchUtil;

import java.util.Calendar;

public class AppointmentActivity extends Activity{
    private LinearLayout llReturn;
    private ImageView ivBack;
    private TextView tvTitle,Date_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        initUi();
    }

    private void initUi() {
        llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
        llReturn.setOnClickListener(new ClickReturnMethod());

        ivBack = (ImageView) findViewById(R.id.title_iv_back);
        ivBack.setImageResource(R.drawable.btn_back1);

        tvTitle = (TextView) findViewById(R.id.title_tv_name);
        tvTitle.setText("预约就诊");

        Date_txt = (TextView) findViewById(R.id.Appointment_Date_txt);
    }

    class ClickReturnMethod implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            CacheActivityManager.finishSingleActivity(AppointmentActivity.this);
        }
    }

    /**********************************************************************************************
     * * 功能说明：预约时间日历框选择器
     **********************************************************************************************/
    boolean todayorderState = false;
    public void ClickAppointmentDateMethod(View view){
        todayorderState = false;
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, DatePickerDialog.THEME_DEVICE_DEFAULT_LIGHT,new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker datePicker, int iyear, int monthOfYear, int dayOfMonth) {
                int now_year = datePicker.getYear();                                            //年
                int now_month = datePicker.getMonth() + 1;                                      //月-1
                int now_dayOfMonth = datePicker.getDayOfMonth();                                //日
                if (todayorderState == false) {
                    todayorderState = true;
                    Date_txt.setText(now_year + "-" + now_month + "-" + now_dayOfMonth);
                }
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    /**********************************************************************************************
     * * 功能说明：点击响应预约服务
     **********************************************************************************************/
    public void ClickAppointmentMethod(View view){
        PopMessageUtil.showToastLong("预约成功！");
        SwitchUtil.FinishActivity(this);
    }
}
