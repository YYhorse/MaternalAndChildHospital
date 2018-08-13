package com.example.maternalandchildhospital.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.maternalandchildhospital.R;
import com.example.maternalandchildhospital.adapter.ExpertInfoAdapter;
import com.example.maternalandchildhospital.bean.AppointmentInfo;
import com.example.maternalandchildhospital.bean.CacheActivityManager;
import com.example.maternalandchildhospital.bean.ExpertInfo;
import com.example.maternalandchildhospital.net.HttpxUtils.HttpxUtils;
import com.example.maternalandchildhospital.net.HttpxUtils.SendCallBack;
import com.example.maternalandchildhospital.publics.util.GlobalInfo;
import com.example.maternalandchildhospital.publics.util.Md5Util;
import com.example.maternalandchildhospital.publics.util.PopMessageUtil;
import com.example.maternalandchildhospital.publics.util.SwitchUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;

import java.util.ArrayList;
import java.util.List;

public class OnlineConsultingActivity extends Activity{
    private LinearLayout llReturn;
    private ImageView ivBack;
    private TextView tvTitle;

    private ListView Expertinfo_listview;
    private ExpertInfoAdapter expertInfoAdapter;
    private List<ExpertInfo.Data.Ydata> Ydata;


    private int pageNumber = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onlineconsulting);
        initUi();
        getUser();
    }

    private void initUi() {
        llReturn = (LinearLayout) findViewById(R.id.title_ll_back);
        llReturn.setOnClickListener(new OnlineConsultingActivity.ClickReturnMethod());

        ivBack = (ImageView) findViewById(R.id.title_iv_back);
        ivBack.setImageResource(R.drawable.btn_back1);

        tvTitle = (TextView) findViewById(R.id.title_tv_name);
        tvTitle.setText("在线咨询");

        Expertinfo_listview = (ListView) findViewById(R.id.Expertinfo_listview);
        Ydata = new ArrayList<ExpertInfo.Data.Ydata>();
        expertInfoAdapter = new ExpertInfoAdapter(this, Ydata);
        Expertinfo_listview.setAdapter(expertInfoAdapter);
    }
    class ClickReturnMethod implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            CacheActivityManager.finishSingleActivity(OnlineConsultingActivity.this);
        }
    }

    private void getUser(){
        //进行预约网络请求
        String CheckStr = "p=" + pageNumber
                + "&num=" + 20
                + "&key=123456";
        String MD5String = Md5Util.generateMD5String(CheckStr);
        String JsonString = "{\"check\":\"" + MD5String + "\",\"json\":{"
                + "\"p\":" + pageNumber +
                ",\"num\":20"+
                "}}";
        PopMessageUtil.Log(JsonString);
        //网络请求
        HttpxUtils.postHttp(new SendCallBack() {
            @Override
            public void onSuccess(String result) {
                Gson gson = new Gson();
                ExpertInfo expertInfo = gson.fromJson(result,ExpertInfo.class);
                if(expertInfo.getStatus()==1){
                    Ydata = expertInfo.getData().getData();
                    expertInfoAdapter.upDataData(Ydata);
                }
                PopMessageUtil.Log("在线咨询接口返回：" + result);
            }

            public void onError(Throwable ex, boolean isOnCallback) {
                PopMessageUtil.Log("在线咨询服务器返回："+ex.getMessage());
                ex.printStackTrace();
            }
            public void onCancelled(Callback.CancelledException cex) {}
            public void onFinished() {}
        }).setUrl("http://fuyouapi.ichees.com/apiNew/public/index.php/api/Userlogin/getUser")
                .addJsonParameter(JsonString)
                .send();
    }
}
