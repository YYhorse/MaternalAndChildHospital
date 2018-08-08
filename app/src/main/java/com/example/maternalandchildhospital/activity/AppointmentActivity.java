package com.example.maternalandchildhospital.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.bean.AppointmentInfo;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.net.HttpxUtils.HttpxUtils;
import com.example.maternalandchildhospital.net.HttpxUtils.SendCallBack;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Md5Util;
import com.example.maternalandchildhospital.publics.util.PopMessageUtil;
import com.example.maternalandchildhospital.publics.util.SwitchUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;

import java.util.Calendar;

public class AppointmentActivity extends Activity{
    private LinearLayout llReturn;
    private ImageView ivBack;
    private TextView tvTitle,Date_txt;
    private EditText Name_txt,Age_txt,Phone_txt;
    private boolean clickStatus = false;
    private Button Appointment_btn;

    private String AppointmentType = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);
        initUi();
        dealInter();
    }

    private void dealInter() {
        Intent Extraintent = this.getIntent();
        AppointmentType = Extraintent.getStringExtra("type");
        if(AppointmentType.compareTo("jance")==0)
            tvTitle.setText("预约就诊");
        else if(AppointmentType.compareTo("42day")==0)
            tvTitle.setText("产后42天");
        else if(AppointmentType.compareTo("ertongtj")==0)
            tvTitle.setText("0-6岁儿童体检");
        else if(AppointmentType.compareTo("rutuotj")==0)
            tvTitle.setText("入托体检预约");
    }

    private void initUi() {
        llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
        llReturn.setOnClickListener(new ClickReturnMethod());

        ivBack = (ImageView) findViewById(R.id.title_iv_back);
        ivBack.setImageResource(R.drawable.btn_back1);

        tvTitle = (TextView) findViewById(R.id.title_tv_name);

        Appointment_btn = (Button) findViewById(R.id.Appointment_btn);

        Date_txt = (TextView) findViewById(R.id.Appointment_Date_txt);
        Name_txt = (EditText) findViewById(R.id.Appointment_Name_txt);
        Age_txt  = (EditText) findViewById(R.id.Appointment_Age_txt);
        Phone_txt= (EditText) findViewById(R.id.Appointment_Phone_txt);
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
        if(clickStatus==false) {
            PopMessageUtil.Log("userId=" + GlobalInfo.userInfo.getUserId());
            if (Date_txt.getText().toString().compareTo("请选择预约时间") == 0)
                PopMessageUtil.showToastLong("请选择预约时间!");
            else {
                if (Name_txt.getText().length() == 0 || Age_txt.getText().length() == 0 || Phone_txt.getText().length() == 0)
                    PopMessageUtil.showToastLong("信息未填全");
                else {
                    //进行预约网络请求
                    String CheckStr = "user_id=" + GlobalInfo.userInfo.getUserId()
                            + "&name=" + Name_txt.getText().toString()
                            + "&age=" + Age_txt.getText().toString()
                            + "&appointment_time=" + Date_txt.getText().toString()
                            + "&mobile=" + Phone_txt.getText().toString()
                            + "&type="+AppointmentType
                            + "&key=123456";
                    String MD5String = Md5Util.generateMD5String(CheckStr);
                    String JsonString = "{\"check\":\"" + MD5String + "\",\"json\":{"
                            + "\"user_id\":" + GlobalInfo.userInfo.getUserId() +
                            ",\"name\":\"" + Name_txt.getText().toString() + "\"" +
                            ",\"age\":" + Age_txt.getText().toString() +
                            ",\"appointment_time\":\"" + Date_txt.getText().toString() + "\"" +
                            ",\"mobile\":\"" + Phone_txt.getText().toString() + "\"" +
                            ",\"type\":\""+AppointmentType+"\"" +
                            "}}";
                    PopMessageUtil.Log(JsonString);

                    //网络请求
                    clickStatus = true;
                    Appointment_btn.setText("正在预约中");
                    HttpxUtils.postHttp(new SendCallBack() {
                        @Override
                        public void onSuccess(String result) {
                            PopMessageUtil.Log("预约接口返回：" + result);
                            Gson gson = new Gson();
                            AppointmentInfo appointmentInfo = gson.fromJson(result,AppointmentInfo.class);

                            if(appointmentInfo.getStatus() == 1) {
                                //预约成功!
                                SwitchUtil.FinishActivity(AppointmentActivity.this);
                            }
                            else{
                                //预约失败!
                                clickStatus = false;
                                Appointment_btn.setText("预约");
                            }
                            PopMessageUtil.showToastLong(appointmentInfo.getInfo());
                        }

                        public void onError(Throwable ex, boolean isOnCallback) {
                            PopMessageUtil.Log("预约接口服务器返回："+ex.getMessage());
                            ex.printStackTrace();
                        }
                        public void onCancelled(Callback.CancelledException cex) {}
                        public void onFinished() {}
                    }).setUrl("http://fuyouapi.ichees.com/apiNew/public/index.php/api/appointment/act")
                            .addJsonParameter(JsonString)
                            .send();
                }
            }
        }

    }
}
